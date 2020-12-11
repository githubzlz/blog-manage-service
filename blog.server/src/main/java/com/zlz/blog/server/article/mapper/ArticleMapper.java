package com.zlz.blog.server.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zlz.blog.common.entity.article.BlogArticle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文章的持久层
 *
 * @author zhulinzhong
 * @version 1.0 CreateTime:2019/12/20 17:36
 */
@Mapper
public interface ArticleMapper extends BaseMapper<BlogArticle> {

    /**
     * 文章分页查询sql
     *
     * @param page    page
     * @param article article
     * @return IPage
     */
    IPage<BlogArticle> selectPage(IPage<BlogArticle> page, @Param("article") BlogArticle article);

    /**
     * 根据热度排序
     *
     * @param num
     * @return
     */
    List<BlogArticle> selectListOrderByHot(@Param("num") Long num);

    /**
     * 通过id查找文章
     *
     * @param ids
     * @return
     */
    List<BlogArticle> selectListByIds(@Param("ids") List<Long> ids);

    /**
     * 查询所有
     * @return
     */
    List<BlogArticle> selectAll(@Param("param") String param);

    List<BlogArticle> selectBlogRecommend(Integer type);
}
