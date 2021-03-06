package com.zlz.blog.common.entity.tag;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author peeterZ
 * @version 2.0 CreateTime:2020-12-12 14:14
 * @description 标签
 */
@Data
@TableName("tag")
public class Tag implements Serializable {
    private static final long serialVersionUID = 7641958658080628099L;
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 类型id
     */
    private Long typeId;

    /**
     * 标签名
     */
    private String name;

    /**
     * 预留字段，暂不使用
     */
    private String code;

    /**
     * 0停用 1启用
     */
    private Boolean state;

    private Long creator;

    private Date createdTime;

    private Long lastModifier;

    private Date lastModifiedTime;

}