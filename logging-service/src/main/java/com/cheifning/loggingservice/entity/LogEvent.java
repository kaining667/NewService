package com.cheifning.loggingservice.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description: TODO
 * @author: ChiefNing
 * @date: 2025年06月19日
 */

@Data
@TableName("operation_logs")
public class LogEvent{
    @TableId(value = "log_id")
    private Long logId;

    @TableField(value = "user_id")
    private Long userId;

    private String action;

    private String ip;

    /**
     * 操作详情
     */
    private String detail;
}
