package com.chiening.userservice.entity;

/**
 * @description: users对应的类user
 * @author: ChiefNing
 * @date: 2025年06月17日
 */
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 使用雪花算法生成分布式id
     */
    @TableId(value = "user_id", type = IdType.ASSIGN_ID)
    private Long userId;

    private String username;

    /**
     *  密码，这里使用md5加密
     */
    private String password;
    private String email;
    private String phone;

    @TableField(value = "gmt_create")
    private Date gmtCreate;

    /**
     *  随机生成的盐
     */
    private String salt;
}