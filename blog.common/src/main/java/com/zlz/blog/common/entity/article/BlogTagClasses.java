package com.zlz.blog.common.entity.article;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zlz.blog.common.entity.common.BaseEntity;
import lombok.Data;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-07-07 11:20
 * @description 标签分类
 */
@Data
@TableName("blog_tag_classes")
public class BlogTagClasses extends BaseEntity {
    private static final long serialVersionUID = -2657666035992144565L;

    /**
     * id
     */
    private Long id;

    /**
     * 类型
     */
    private String type;
}
