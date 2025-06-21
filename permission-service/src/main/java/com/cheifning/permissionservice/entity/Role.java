package com.cheifning.permissionservice.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description: TODO
 * @author: ChiefNing
 * @date: 2025年06月18日
 */

@Data
@TableName("roles")
public class Role {
    private int roleId;
    private String roleCode;
}
