package com.zlz.blog.common.entity.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.zlz.blog.common.entity.common.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020-05-25 10:17
 * @description 文章点赞收藏等数量信息
 */
@Data
public class BlogPublicInfo extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -7327288298331830139L;

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
     * 星星数量
     */
    private Long stars;

    /**
     * 点赞数量
     */
    private Long goods;

    /**
     * 评论数量
     */
    private Long comments;

    /**
     * 阅读数量
     */
    private Long readings;

    /**
     * 收藏数量
     */
    private Long collect;
}
