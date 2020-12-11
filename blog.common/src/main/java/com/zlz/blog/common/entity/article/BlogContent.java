package com.zlz.blog.common.entity.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zlz.blog.common.entity.common.BaseEntity;
import lombok.Data;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020/1/15 18:32
 */
@Data
@TableName("blog_content")
public class BlogContent extends BaseEntity {

    private static final long serialVersionUID = 3607516735993785983L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 文章id
     */
    private Long blogId;

    /**
     * html
     */
    private String contentHtml;

    /**
     * md
     */
    private String contentMd;

    /**
     * html的大小
     */
    private String htmlSize;

    /**
     * md的大小
     */
    private String mdSize;
}
