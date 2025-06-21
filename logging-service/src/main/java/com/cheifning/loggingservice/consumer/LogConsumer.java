package com.cheifning.loggingservice.consumer;

import com.cheifning.loggingservice.entity.LogEvent;
import com.cheifning.loggingservice.mapper.LogMapper;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.Action;

/**
 * @description: TODO
 * @author: ChiefNing
 * @date: 2025年06月19日
 */
@Service
@RocketMQMessageListener(
        topic = "operation_log_topic",
        consumerGroup = "log-consumer-group"
)
public class LogConsumer implements RocketMQListener<LogEvent> {

    @Autowired
    private LogMapper logMapper;

    @Override
    public void onMessage(LogEvent logEvent) {
        try{
            System.out.println(logEvent.toString());
            logMapper.insert(logEvent);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
