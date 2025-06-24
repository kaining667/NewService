package com.chiening.userservice.entity;

import lombok.Data;

/**
 * @description: 信息反馈
 * @author: ChiefNing
 * @date: 2025年06月18日
 */
@Data
public class UserInfo {
    private Long userId;
    private String username;
    private String token;
}
