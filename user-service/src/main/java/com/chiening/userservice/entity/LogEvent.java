package com.chiening.userservice.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: 日志服务，封装日志类传入rocketmq
 * @author: ChiefNing
 * @date: 2025年06月18日
 */
@Data
public class LogEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String action;
    private String ip;
    private String detail;
}