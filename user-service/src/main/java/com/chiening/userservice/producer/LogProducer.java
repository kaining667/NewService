package com.chiening.userservice.producer;

import com.chiening.userservice.entity.LogEvent;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description: rockmq对应方法
 * @author: ChiefNing
 * @date: 2025年06月18日
 */
@Service
public class LogProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    private static final String TOPIC = "operation_log_topic";

    public void sendLogEvent(LogEvent event) {
        rocketMQTemplate.convertAndSend(TOPIC, event);
    }

}