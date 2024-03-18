package com.yang.bi.bizmq;

import com.yang.bi.constant.BiMqConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author YANG FUCHAO
 * CreateTime 2023/6/24 15:53
 * BI项目 生产者
 */
@Component
public class BiMqMessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     * @param message
     */
    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(BiMqConstant.BI_EXCHANGE_NAME,BiMqConstant.BI_ROUTING_KEY, message);
    }
}
