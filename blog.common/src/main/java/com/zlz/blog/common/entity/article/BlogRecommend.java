package com.zlz.blog.common.entity.article;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zlz.blog.common.entity.common.BaseEntity;
import lombok.Data;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-08-11 14:40
 * @description 博客推荐列表
 */
@Data
@TableName("blog_recommend")
public class BlogRecommend extends BaseEntity {

    private static final long serialVersionUID = -5361152023637663717L;
    /**
     * id
     */
    private Long id;

    /**
     * 博客id
     */
    private Long blogId;

    /**
     * 推荐类型
     */
    private Integer recommendType;

    /**
     * 图片地址
     */
    private String imageUrl;
}
