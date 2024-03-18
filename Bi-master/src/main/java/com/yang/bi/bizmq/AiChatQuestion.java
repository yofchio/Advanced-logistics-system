package com.yang.bi.bizmq;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.yang.bi.manager.AiManager;
import com.yang.bi.model.entity.AiAssistant;
import com.yang.bi.model.enums.ChartStatusEnum;
import com.yang.bi.service.AiAssistantService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.yang.bi.constant.BiMqConstant.*;
import static com.yang.bi.constant.CommonConstant.AI_MODEL_ID;

/**
 * @author YANG FUCHAO
 * CreateTime 2023/6/25 20:07
 */
@Slf4j
@Component
@AllArgsConstructor
public class AiChatQuestion {

    private final static Gson GSON = new Gson();

    @Autowired
    private AiManager aiManager;
    @Autowired
    private AiAssistantService aiAssistantService;


    @RabbitListener(
            bindings = @QueueBinding(value = @Queue(AI_QUESTION_QUEUE),
                    exchange = @Exchange(AI_QUESTION_EXCHANGE_NAME)
                    , key = AI_QUESTION_ROUTING_KEY))
    public void handle(Message message, Channel channel) throws IOException {
        AiAssistant aiAssistant = null;
        try {
            String data = new String(message.getBody());
            aiAssistant = GSON.fromJson(data, AiAssistant.class);
            String questionGoal = aiAssistant.getQuestionGoal();

            String result = aiManager.doAiChat(AI_MODEL_ID, questionGoal);
            aiAssistant.setQuestionResult(result);
            aiAssistant.setQuestionStatus(ChartStatusEnum.SUCCEED.getValue());
            aiAssistantService.updateById(aiAssistant);
            // 交付标签，消息id
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            // 拒绝后丢弃
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            if (aiAssistant != null) {
                aiAssistant.setQuestionStatus(ChartStatusEnum.FAILED.getValue());
                aiAssistantService.updateById(aiAssistant);
            }
        }
    }
}
