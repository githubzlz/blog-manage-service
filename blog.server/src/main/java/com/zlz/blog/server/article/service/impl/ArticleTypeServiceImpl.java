package com.zlz.blog.server.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zlz.blog.common.constants.article.ArticleConstants;
import com.zlz.blog.common.entity.article.BlogType;
import com.zlz.blog.common.entity.common.LoginUser;
import com.zlz.blog.common.enums.article.OperateTypeEnum;
import com.zlz.blog.common.enums.article.TypeLevelEnum;
import com.zlz.blog.common.exception.BlogException;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.common.response.TreeNode;
import com.zlz.blog.common.util.SqlResultUtil;
import com.zlz.blog.common.util.TokenUtil;
import com.zlz.blog.server.article.mapper.ArticleTypeMapper;
import com.zlz.blog.server.article.service.ArticleTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-06-24 17:10
 * @description
 */
@Service
public class ArticleTypeServiceImpl implements ArticleTypeService {

    @Resource
    private ArticleTypeMapper articleTypeMapper;

    @Override
    public ResultSet createType(Long pId, String name, Integer type, HttpServletRequest request) {

        //获取登陆人
        LoginUser loginUser = TokenUtil.getLoginUser(request);

        //补全信息
        BlogType blogType = new BlogType();
        blogType.setLevel(2);
        if ("-1".equals(pId.toString())) {
            blogType.setLevel(1);
        }
        blogType.setPId(pId);
        blogType.setTypeName(name);
        blogType.setType(type);
        blogType.setUserId(loginUser.getId());
        blogType.setLastModifiedTime(new Date());
        blogType.setCreatedTime(new Date());
        blogType.setCreator(loginUser.getUsername());
        blogType.setLastModifier(loginUser.getUsername());
        //检查类型名是否重复
        QueryWrapper<BlogType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type_name", blogType.getTypeName())
                .eq("creator", loginUser.getUsername());
        List<BlogType> blogTypes = articleTypeMapper.selectList(queryWrapper);
        if (!blogTypes.isEmpty()) {
            return ResultSet.error("类型插入失败，重复的类型");
        }

        //插入并返回操作结果
        int insert = articleTypeMapper.insert(blogType);
        return SqlResultUtil.isOneRow(insert);
    }

    @Override
    public ResultSet queryBlogType(HttpServletRequest request, Integer operate, Integer type) {

        //获取登陆人
        LoginUser loginUser = TokenUtil.getLoginUser(request);
        //查询所有分类
        QueryWrapper<BlogType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", loginUser.getId());
        if (OperateTypeEnum.MANAGEMENT.getCode() != operate) {
            queryWrapper.eq("state", ArticleConstants.IN_USE);
        }
        if(type != null && (type == 1 || type == 0)){
            queryWrapper.eq("type", type);
        }

        List<BlogType> blogTypes = articleTypeMapper.selectList(queryWrapper);

        //组装分类树返回给前端
        return ResultSet.success("查询成功", getTypeTreeNode(blogTypes));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultSet updateState(Long id, Integer state, HttpServletRequest request) {

        //获取登陆人
        LoginUser loginUser = TokenUtil.getLoginUser(request);

        //查询分类所属等级
        BlogType blogType = articleTypeMapper.selectById(id);
        if (null == blogType) {
            return ResultSet.error("未知的分类");
        }

        //停用
        BlogType deleteEntity = new BlogType();
        deleteEntity.setId(id);
        deleteEntity.setState(state);
        String op;
        if (state == 0) {
            op = "启用";
        } else {
            op = "停用";
        }
        TypeLevelEnum enumByCode = TypeLevelEnum.getEnumByCode(blogType.getLevel());
        switch (enumByCode) {
            case ONE:
                //修改本级分类
                QueryWrapper<BlogType> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id", id).eq("creator", loginUser.getUsername());
                int i = articleTypeMapper.update(deleteEntity, queryWrapper);
                if (i != 1) {
                    throw new BlogException(op + "本级分类失败");
                }
                //修改下级分类
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("p_id", id)
                        .eq("creator", loginUser.getUsername());
                deleteEntity.setId(null);
                int i2 = articleTypeMapper.update(deleteEntity, queryWrapper);
                if (i2 == 0) {
                    return ResultSet.success("成功" + op + "该分类");
                }
                return ResultSet.success("成功" + op + "该分类,及" + i2 + "个下级分类");
            case TWO:
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id", id).eq("creator", loginUser.getUsername());
                int j = articleTypeMapper.update(deleteEntity, queryWrapper);
                return SqlResultUtil.isOneRow(j, "成功" + op + "该分类", op + "该分类失败");
            default:
                return ResultSet.error("未知的分类层级");
        }
    }

    @Override
    public ResultSet deleteState(Long id, HttpServletRequest request) {

        //获取登陆人
        LoginUser loginUser = TokenUtil.getLoginUser(request);

        //查询分类所属等级
        BlogType blogType = articleTypeMapper.selectById(id);
        if (null == blogType) {
            return ResultSet.error("未知的分类");
        }

        TypeLevelEnum enumByCode = TypeLevelEnum.getEnumByCode(blogType.getLevel());
        switch (enumByCode) {
            case ONE:
                //修改本级分类
                QueryWrapper<BlogType> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("id", id).eq("creator", loginUser.getUsername());
                int i = articleTypeMapper.delete(queryWrapper);
                if (i != 1) {
                    throw new BlogException("删除本级分类失败");
                }
                //修改下级分类
                queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("p_id", id)
                        .eq("creator", loginUser.getUsername());
                int i2 = articleTypeMapper.delete(queryWrapper);
                if (i2 == 0) {
                    return ResultSet.success("成功删除该分类");
                }
                return ResultSet.success("成功删除该分类,及" + i2 + "个下级分类");
            case TWO:
                QueryWrapper<BlogType> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("id", id).eq("creator", loginUser.getUsername());
                int j = articleTypeMapper.delete(queryWrapper2);
                return SqlResultUtil.isOneRow(j, "删除分类成功", "删除分类失败");
            default:
                return ResultSet.error("未知的分类层级");
        }

    }

    @Override
    public ResultSet updateTypeName(Long id, String name, HttpServletRequest request) {

        //获取登陆人
        LoginUser loginUser = TokenUtil.getLoginUser(request);

        QueryWrapper<BlogType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id).eq("creator", loginUser.getUsername());

        BlogType updateEntity = new BlogType();
        updateEntity.setId(id);
        updateEntity.setTypeName(name);
        updateEntity.setLastModifiedTime(new Date());
        int i = articleTypeMapper.updateById(updateEntity);
        return SqlResultUtil.isOneRow(i, "修改名称成功", "修改名称失败");
    }

    /**
     * 创建分级树
     *
     * @return
     */
    private List<TreeNode<BlogType>> getTypeTreeNode(List<BlogType> blogTypes) {

        Map<Long, TreeNode<BlogType>> parent = new HashMap<>(16);
        Map<Long, TreeNode<BlogType>> children = new HashMap<>(16);

        blogTypes.forEach(item -> {
                    TreeNode<BlogType> typeTreeNode = new TreeNode<>();
                    typeTreeNode.setId(item.getId());
                    typeTreeNode.setName(item.getTypeName());
                    typeTreeNode.setData(item);
                    typeTreeNode.setChildren(new ArrayList<>());
                    typeTreeNode.setPId(item.getPId());
                    if (item.getPId() == -1) {
                        parent.put(item.getId(), typeTreeNode);
                        return;
                    }
                    children.put(item.getId(), typeTreeNode);
                }
        );

        children.forEach((id, item) -> {
            TreeNode<BlogType> typeTreeNode = parent.get(item.getPId());
            if (null != typeTreeNode) {
                typeTreeNode.getChildren().add(item);
            }
        });

        List<TreeNode<BlogType>> list = new ArrayList<>();
        parent.forEach((key, value) -> {
                    list.add(value);
                }
        );

        //排序
        list.sort((o1, o2) -> o2.getData().getLastModifiedTime().compareTo(o1.getData().getLastModifiedTime()));
        for (TreeNode<BlogType> typeTreeNode : list) {
            List<TreeNode<BlogType>> childrenSort = typeTreeNode.getChildren();
            if (null != childrenSort) {
                childrenSort.sort((o1, o2) -> o2.getData().getLastModifiedTime().compareTo(o1.getData().getLastModifiedTime()));
            }
        }
        return list;
    }
}
