package com.zlz.blog.server.article.controller;

import com.zlz.blog.common.entity.article.BlogType;
import com.zlz.blog.common.response.ResultSet;
import com.zlz.blog.server.article.service.ArticleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-06-24 17:09
 * @description 文章类型操作
 */
@RestController
@RequestMapping("/article/type")
public class ArticleTypeController {

    @Autowired
    private ArticleTypeService articleTypeService;

    /**
     * 创建新的分类
     *
     * @param pId
     * @param name
     * @param request
     * @return
     */
    @GetMapping("/create/{pId}/{name}/{type}")
    public ResultSet createType(@PathVariable("pId") Long pId,
                                @PathVariable("name") String name,
                                @PathVariable("type") Integer type,
                                HttpServletRequest request) {
        return articleTypeService.createType(pId, name,type, request);
    }

    /**
     * 查询分类树
     *
     * @param request
     * @return
     */
    @GetMapping("/queryblogtype/{operate}")
    public ResultSet queryBlogType(@PathVariable("operate") Integer operate,
                                   HttpServletRequest request,
                                   @RequestParam(required = false) Integer type) {
        return articleTypeService.queryBlogType(request, operate, type);
    }

    /**
     * 修改分类状态(停用启用)
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/updatestate/{id}/{state}")
    public ResultSet updateState(@PathVariable("id") Long id,
                                 @PathVariable("state") Integer state, HttpServletRequest request) {
        return articleTypeService.updateState(id, state, request);
    }

    /**
     * 删除分类
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/removestate/{id}")
    public ResultSet removeState(@PathVariable("id") Long id, HttpServletRequest request) {
        return articleTypeService.deleteState(id, request);
    }

    /**
     * 修改分类名称
     *
     * @param blogType
     * @param request
     * @return
     */
    @PostMapping("/changetypename")
    public ResultSet updateTypeName(@RequestBody BlogType blogType, HttpServletRequest request) {
        return articleTypeService.updateTypeName(blogType.getId(), blogType.getTypeName(), request);
    }

}
