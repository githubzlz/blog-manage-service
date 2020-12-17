package com.zlz.blog.common.entity.module;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author peeterZ
 * @version 2.0 CreateTime:2020-12-12 14:14
 * @description 模块
 */
@Data
@TableName("module")
public class Module implements Serializable {
    private static final long serialVersionUID = -3086115441709363100L;
    /**
     * 模块id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 模块标题
     */
    private String title;

    /**
     * 模块介绍
     */
    private String introduction;

    /**
     * 模块图片路径
     */
    private String imageUrl;

    /**
     * 0 不发布 1发布
     */
    private Integer isPublish;

    /**
     * 创建人
     */
    private Long creator;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 最后修改人
     */
    private Long lastModifier;

    /**
     * 最后修改时间
     */
    private Date lastModifiedTime;

    /**
     * 模块的菜单
     */
    @TableField(exist = false)
    private List<ModuleMenu> menus;
}