package com.zlz.blog.common.entity.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zlz.blog.common.entity.common.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2020/1/16 8:46
 */
@Data
@TableName("blog_tag")
public class BlogTag extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -8477772435264148303L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * name
     */
    private String name;

    /**
     * 被使用的数量
     */
    private Integer usedNumber;

    /**
     * 类型id
     */
    private Long typeId;
}
