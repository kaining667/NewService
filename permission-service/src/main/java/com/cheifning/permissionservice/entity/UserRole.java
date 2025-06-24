package com.cheifning.permissionservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @description: TODO
 * @author: ChiefNing
 * @date: 2025年06月18日
 */

@Data
@TableName("user_roles")
public class UserRole implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "role_id")
    private Integer roleId;
}
