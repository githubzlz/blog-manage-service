package com.zlz.blog.common.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhulinzhong
 * @version 1.0 CreateTime:2019/12/20 18:15
 */
@TableName("sys_user")
@Data
public class LoginUser implements Serializable {

    private static final long serialVersionUID = -749363217569096813L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 盐值
     */
    private String salt;

    /**
     * email
     */
    private String email;

    /**
     * 电话号
     */
    private String phone;

    /**
     * 用户角色
     */
    @TableField(exist = false)
    private List<SysRole> sysRoles;
}
