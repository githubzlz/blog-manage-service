package com.zlz.blog.common.entity.article;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zlz.blog.common.entity.common.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-06-23 14:52
 * @description 文章类型
 */
@Data
@TableName("blog_types_user")
public class BlogType extends BaseEntity {
    private static final long serialVersionUID = -165290882890926008L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private Long userId;

    /**
     * 分类名称
     */
    @NotNull(message = "分类名称 不允许为空")
    private String typeName;

    /**
     * 分类等级
     */
    @NotNull(message = "分类等级 不允许为空")
    private Integer level;

    /**
     * 分类父级id
     */
    @NotNull(message = "父级分类id 不允许为空")
    private Long pId;

    /**
     * 分级码
     */
    private String levelCode;

    /**
     * 0 启用 1 停用
     */
    private Integer state;

    private Integer type;
}
