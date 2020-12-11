package com.zlz.blog.server.article.controller;

import com.zlz.blog.common.entity.article.BlogArticle;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.server.article.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 文章操作的控制器层
 *
 * @author zhulinzhong
 * @version 1.0 CreateTime:2019/12/20 17:37
 */
@RequestMapping("/blog/article")
@RestController
public class ArticleController {

    @Resource
    private ArticleService articleService;

    /**
     * 新增文章
     *
     * @param article article
     * @param request request
     * @return ResultSet
     */
    @PostMapping("/create")
    public ResultSet insertArticle(@RequestBody BlogArticle article, HttpServletRequest request) {
        return articleService.insertArticle(article, request);
    }

    /**
     * 修改文章
     *
     * @param article article
     * @param request request
     * @return ResultSet
     */
    @PostMapping("/update")
    public ResultSet updateArticle(@RequestBody BlogArticle article, HttpServletRequest request) {
        return articleService.updateArticle(article, request);
    }

    /**
     * 修改文章（仅标题摘要）
     *
     * @param article article
     * @param request request
     * @return ResultSet
     */
    @PostMapping("/titleorsummary")
    public ResultSet updateArticleTitle(@RequestBody @Valid BlogArticle article, HttpServletRequest request) {
        return articleService.updateArticleTitle(article, request);
    }

    /**
     * 分页查询,模糊查询
     *
     * @param blogArticle blogArticle
     * @param request     request
     * @return ResultSet
     */
    @PostMapping("/list")
    public ResultSet selectAll(@RequestBody BlogArticle blogArticle, HttpServletRequest request) {
        return articleService.selectList(blogArticle, request);
    }

    /**
     * 查询一篇文章
     *
     * @param id      id
     * @param request request
     * @return return
     */
    @GetMapping("/queryarticle/{id}")
    public ResultSet selectArticle(@PathVariable("id") Long id, HttpServletRequest request) {
        return articleService.queryArticle(id, request);
    }

    /**
     * 逻辑删除文章
     *
     * @param id      id
     * @param request request
     * @return ResultSet
     */
    @GetMapping("/remove/{id}")
    public ResultSet deleteArticle(@PathVariable("id") Long id, HttpServletRequest request) {
        return articleService.deleteArticle(id, request);
    }

    /**
     * 恢复已经删除的文章
     *
     * @param id      id
     * @param request request
     * @return ResultSet
     */
    @GetMapping("/remove/revoke/{id}")
    public ResultSet revokeDeletedArticle(@PathVariable("id") Long id, HttpServletRequest request) {
        return articleService.revokeDeletedArticle(id, request);
    }

    /**
     * 查询所有未推荐的文章
     * @param param
     * @param request
     * @return
     */
    @GetMapping("/search/all")
    public ResultSet searchAll(@RequestParam(required = false) String param, HttpServletRequest request) {
        return articleService.searchAll(param, request);
    }
}
