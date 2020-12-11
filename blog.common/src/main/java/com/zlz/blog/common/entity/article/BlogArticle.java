package com.zlz.blog.common.entity.article;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zlz.blog.common.entity.common.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 文章实体类
 *
 * @author zhulinzhong
 */
@Data
@TableName("blog_article")
public class BlogArticle extends BaseEntity {

    private static final long serialVersionUID = -8570325751738264311L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     * 所属人
     */
    private Long userId;

    /**
     * 标题
     */
    private String title;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 作者
     */
    private String author;

    /**
     * 标签（字符串"，"分割）
     */
    private String tag;

    /**
     * 类型（字符串"，"分割）
     */
    private String type;

    /**
     * 二级分类
     */
    @TableField(exist = false)
    private String subType;

    /**
     * 0 未删除 1 删除
     */
    private Integer isDeleted;

    /**
     * 出处 0 原创 1 转载 2 翻译
     */
    private Integer provenance;

    /**
     * 可见策略 0 所有人 1 粉丝 2 付费
     */
    private Integer visibleStrategy;

    /**
     * 0 不允许 1 允许
     */
    private Integer isShow;

    /**
     * 图片地址
     */
    private String imgSrc;

    /**
     * 展示图片
     */
    private Integer imgShow;

    /**
     * 文章类型
     */
    private Integer blogType;

    /**
     * 标签的集合
     */
    @TableField(exist = false)
    private List<String> tags;

    /**
     * 标签的集合
     */
    @TableField(exist = false)
    private List<BlogTag> tags2;

    /**
     * 类型的集合
     */
    @TableField(exist = false)
    private List<String> types;

    /**
     * md字符串
     */
    @TableField(exist = false)
    private String md;

    /**
     * html字符串
     */
    @TableField(exist = false)
    private String html;

    /**
     * 文章内容
     */
    @TableField(exist = false)
    private BlogContent blogContent;

    /**
     * 文章浏览信息
     */
    @TableField(exist = false)
    private BlogPublicInfo blogPublicInfos;

    @TableField(exist = false)
    private BlogRecommend blogRecommend;

}