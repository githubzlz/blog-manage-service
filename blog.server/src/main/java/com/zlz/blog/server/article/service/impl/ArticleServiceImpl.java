package com.zlz.blog.server.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zlz.blog.common.constants.article.ArticleConstants;
import com.zlz.blog.common.entity.article.*;
import com.zlz.blog.common.entity.common.ExcludeItem;
import com.zlz.blog.common.entity.common.LoginUser;
import com.zlz.blog.common.enums.article.ProvenanceEnum;
import com.zlz.blog.common.enums.article.VisibleStrategyEnum;
import com.zlz.blog.common.exception.BlogException;
import com.zlz.blog.common.response.PageInfo;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.common.util.ListUtil;
import com.zlz.blog.common.util.PageUtil;
import com.zlz.blog.common.util.SqlResultUtil;
import com.zlz.blog.common.util.TokenUtil;
import com.zlz.blog.server.article.mapper.ArticleMapper;
import com.zlz.blog.server.article.mapper.BlogRecommendMapper;
import com.zlz.blog.server.article.service.ArticleContentService;
import com.zlz.blog.server.article.service.ArticlePublicInfoService;
import com.zlz.blog.server.article.service.ArticleService;
import com.zlz.blog.server.article.service.ArticleTagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 文章操作service实现类
 *
 * @author zhulinzhong
 * @version 1.0 CreateTime:2019/12/20 17:35
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ArticleContentService contentService;
    @Resource
    private ArticlePublicInfoService publicInfoService;
    @Resource
    private ArticleTagService tagService;
    @Resource
    private BlogRecommendMapper blogRecommendMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultSet insertArticle(BlogArticle article, HttpServletRequest request) {

        if (null == article || StringUtils.isEmpty(article.getTitle())) {
            throw new BlogException("插入错误,数据为空");
        }

        //设置其他默认数据
        LoginUser user = TokenUtil.getLoginUser(request);

        //初始化文章信息
        article.setUserId(user.getId());
        article.setAuthor(user.getUsername());
        article.setIsDeleted(ArticleConstants.IS_NOT_DELETED);
        //todo:预先设置为展示，方便测试
        article.setIsShow(ArticleConstants.IS_SHOW);
        //获取标签和类型的集合,转化为字符串
        List<String> tags = new ArrayList<>();
        article.getTags2().forEach(tag -> {
            tags.add(tag.getName());
        });
        article.setTag(ListUtil.toString(tags));
        article.setType(ListUtil.toString(article.getTypes()));
        //补全创建人信息
        article.setLastModifier(user.getUsername());
        article.setLastModifiedTime(new Date());
        article.setCreator(user.getUsername());
        article.setCreatedTime(new Date());

        //插入主表数据
        int row = articleMapper.insert(article);
        if (row != 1 || StringUtils.isEmpty(article.getId())) {
            throw new BlogException("文章数据创建失败");
        }

        //插入文章正文相关内容
        insertArticleContent(article, request);

        //插入文章浏览信息相关内容
        insertBlogInfos(article, request);

        //插入文章标签信息相关内容
        insertBlogTag(article.getTags2(), request);

        return ResultSet.success("文章保存成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultSet updateArticle(BlogArticle article, HttpServletRequest request) {

        LoginUser loginUser = TokenUtil.getLoginUser(request);
        if (StringUtils.isEmpty(loginUser.getUsername()) || null == article.getId()) {
            return ResultSet.inputError();
        }

        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", article.getId())
                .eq("user_id", loginUser.getId());

        BlogArticle blogArticle = new BlogArticle();
        blogArticle.setTag(ListUtil.toString(article.getTags()));
        blogArticle.setType(ListUtil.toString(article.getTypes()));
        blogArticle.setSummary(article.getSummary());
        blogArticle.setTitle(article.getTitle());
        blogArticle.setAuthor(article.getAuthor());
        //仅可以为枚举指定的类型
        if (null != ProvenanceEnum.getEnumByCode(article.getProvenance())) {
            blogArticle.setProvenance(article.getProvenance());
        }
        if (null != VisibleStrategyEnum.getEnumByCode(article.getVisibleStrategy())) {
            blogArticle.setVisibleStrategy(article.getVisibleStrategy());
        }

        //修改文章信息
        int update = articleMapper.update(blogArticle, queryWrapper);
        if (update != 1 && update != 0) {
            throw new BlogException("文章数据修改失败");
        }

        if (article.getBlogContent() == null) {
            return ResultSet.success();
        }

        BlogContent blogContent = article.getBlogContent();
        blogContent.setBlogId(article.getId());

        return contentService.updateBody(blogContent, request);
    }

    @Override
    public ResultSet updateArticleTitle(BlogArticle article, HttpServletRequest request) {

        //获取登录用户
        LoginUser loginUser = TokenUtil.getLoginUser(request);

        //创建修改实体
        BlogArticle updateEntity = new BlogArticle();
        updateEntity.setId(article.getId());
        updateEntity.setTitle(article.getTitle());
        updateEntity.setSummary(article.getSummary());
        updateEntity.setLastModifier(loginUser.getUsername());
        updateEntity.setLastModifiedTime(new Date());

        //修改信息
        int i = articleMapper.updateById(updateEntity);
        return SqlResultUtil.isOneRow(i);
    }

    @Override
    public ResultSet queryArticle(Long id, HttpServletRequest request) {

        //数据检查
        LoginUser loginUser = TokenUtil.getLoginUser(request);
        if (StringUtils.isEmpty(loginUser.getUsername()) || null == id) {
            return ResultSet.inputError();
        }

        //设置查询条件
        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id)
                .eq("user_id", loginUser.getId())
                .last("limit 1");

        BlogArticle blogArticle = articleMapper.selectOne(queryWrapper);
        if (null == blogArticle) {
            return ResultSet.outError();
        }

        //设置标签数组
        blogArticle.setTypes(ListUtil.toList(blogArticle.getType()));
        blogArticle.setTags(ListUtil.toList(blogArticle.getTag()));

        ResultSet resultSet = contentService.queryBody(id, request);
        BlogContent blogContent = (BlogContent) resultSet.getEntity();
        blogArticle.setBlogContent(blogContent);

        ResultSet resultSet1 = publicInfoService.queryPublicInfo(id);
        BlogPublicInfo blogPublicInfo = (BlogPublicInfo) resultSet1.getEntity();
        blogArticle.setBlogPublicInfos(blogPublicInfo);

        return ResultSet.success(blogArticle);
    }

    @Override
    public ResultSet selectList(BlogArticle blogArticle, HttpServletRequest request) {

        //获取当前登录用户
        LoginUser user = TokenUtil.getLoginUser(request);
        if (StringUtils.isEmpty(user.getUsername())) {
            return ResultSet.inputError();
        }

        //设置查询条件
        blogArticle.setUserId(user.getId());

        //获取并设置筛选条件
        PageInfo pageInfo = blogArticle.getPageInfo();
        excludeColumn(pageInfo, blogArticle);

        //返回结果转换并返回消息
        IPage<BlogArticle> iPage = articleMapper.selectPage(PageUtil.getIPage(pageInfo), blogArticle);
        return ResultSet.success(PageUtil.setPageInfo(iPage, blogArticle.getPageInfo()));
    }

    @Override
    public ResultSet deleteArticle(Long id, HttpServletRequest request) {

        //数据检查
        LoginUser loginUser = TokenUtil.getLoginUser(request);
        if (null == id || StringUtils.isEmpty(loginUser.getUsername())) {
            return ResultSet.inputError("删除失败,未找到文章或者登陆人为空");
        }

        //查询文章是否推荐
        QueryWrapper<BlogRecommend> recommendQueryWrapper = new QueryWrapper<>();
        recommendQueryWrapper.eq("blog_id", id);
        Integer integer = blogRecommendMapper.selectCount(recommendQueryWrapper);
        if(integer != 0){
            return ResultSet.error("已推荐文章无法删除,请先取消推荐");
        }

        //设置条件
        QueryWrapper<BlogArticle> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).eq("user_id", loginUser.getId());

        //删除
        int update = articleMapper.deleteById(id);

        return SqlResultUtil.isOneRow(update);
    }

    @Override
    public ResultSet revokeDeletedArticle(Long id, HttpServletRequest request) {

        //数据检查
        LoginUser loginUser = TokenUtil.getLoginUser(request);
        if (id == null || StringUtils.isEmpty(loginUser.getUsername())) {
            return ResultSet.inputError();
        }

        //设置修改条件
        BlogArticle blogArticle = new BlogArticle();
        blogArticle.setId(id);
        blogArticle.setIsDeleted(ArticleConstants.IS_NOT_DELETED);

        //处理修改结果
        int i = articleMapper.updateById(blogArticle);
        return SqlResultUtil.isOneRow(i);
    }

    @Override
    public ResultSet searchAll(String param, HttpServletRequest request) {
        return ResultSet.success(articleMapper.selectAll(param));
    }

    /**
     * 设置查询筛选条件
     *
     * @param pageInfo    pageInfo
     * @param blogArticle blogArticle
     */
    private void excludeColumn(PageInfo pageInfo, BlogArticle blogArticle) {
        if (pageInfo.getExclude() != null && !pageInfo.getExclude().isEmpty()) {
            List<ExcludeItem> exclude = pageInfo.getExclude();
            List<ExcludeItem> needExclude = new ArrayList<>();
            PageInfo newPageInfo = new PageInfo();
            exclude.forEach(item -> {
                if ("isDeleted".equals(item.getColumn()) && !StringUtils.isEmpty(item.getValue())) {
                    ExcludeItem excludeItem = new ExcludeItem();
                    excludeItem.setColumn("is_deleted");
                    excludeItem.setValue(item.getValue());
                    needExclude.add(excludeItem);
                } else if ("isShow".equals(item.getColumn()) && !StringUtils.isEmpty(item.getValue())) {
                    ExcludeItem excludeItem = new ExcludeItem();
                    excludeItem.setColumn("is_show");
                    excludeItem.setValue(item.getValue());
                    needExclude.add(excludeItem);
                }
            });
            newPageInfo.setExclude(needExclude);
            blogArticle.setPageInfo(newPageInfo);
        }
    }

    /**
     * 插入文章正文相关内容
     *
     * @param article article
     */
    private void insertArticleContent(BlogArticle article, HttpServletRequest request) {
        BlogContent blogContent = new BlogContent();
        if (null == article.getBlogContent()) {
            blogContent.setBlogId(article.getId());
        } else {
            blogContent = article.getBlogContent();
            blogContent.setBlogId(article.getId());
            //将md，html插入至内容表
            blogContent.setBlogId(article.getId());
            // >> 10 = * 2的10次方
            double size = blogContent.getContentMd().getBytes().length >> 10;
            blogContent.setMdSize(size + "KB");
            size = blogContent.getContentHtml().getBytes().length >> 10;
            blogContent.setHtmlSize(size + "KB");
        }
        //插入
        ResultSet resultSet = contentService.insertBody(blogContent, request);
        if (!ResultSet.isSuccess(resultSet)) {
            throw new BlogException("文章内容插入失败");
        }
    }

    /**
     * 插入文章非用户控制相关信息（浏览量等）
     *
     * @param article article
     */
    private void insertBlogInfos(BlogArticle article, HttpServletRequest request) {

        BlogPublicInfo blogPublicInfo = new BlogPublicInfo();
        blogPublicInfo.setBlogId(article.getId());
        blogPublicInfo.setStars(0L);
        blogPublicInfo.setComments(0L);
        blogPublicInfo.setGoods(0L);
        blogPublicInfo.setCollect(0L);
        blogPublicInfo.setReadings(0L);

        ResultSet resultSet = publicInfoService.insertPublicInfo(blogPublicInfo, request);
        if (!ResultSet.isSuccess(resultSet)) {
            throw new BlogException("文章浏览信息插入失败");
        }
    }

    /**
     * 插入标签，分类相关信息
     *
     * @param blogTags blogTags
     */
    private void insertBlogTag(List<BlogTag> blogTags, HttpServletRequest request) {

        ResultSet resultSet = tagService.insertTagList(blogTags, request);

        if (!ResultSet.isSuccess(resultSet)) {
            throw new BlogException("文章浏览信息插入失败");
        }
    }
}
