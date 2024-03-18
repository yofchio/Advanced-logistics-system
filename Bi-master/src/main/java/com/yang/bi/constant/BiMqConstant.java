package com.yang.bi.constant;

/**
 * @author YANG FUCHAO
 * CreateTime 2023/6/24 16:26
 * 应用到BI项目当中的mq常量
 */

public interface BiMqConstant {
    String BI_EXCHANGE_NAME = "bi_exchange";
    String BI_QUEUE = "bi_queue";
    String BI_ROUTING_KEY = "bi_routingKey";

    String BI_DIRECT_EXCHANGE = "direct";


    /**
     * AI 问答
     */
    String AI_QUESTION_EXCHANGE_NAME = "ai_question_exchange";
    String AI_QUESTION_QUEUE = "ai_question_queue";
    String AI_QUESTION_ROUTING_KEY = "ai_question_routingKey";

    String AI_QUESTION_DIRECT_EXCHANGE = "direct";

    /**
     * MQ ip地址
     */
    String BI_MQ_HOST = "62.234.23.101";
    /**
     * MQ 用户名
     */
    String BI_MQ_USERNAME = "root";
    /**
     * MQ 密码
     */
    String BI_MQ_PASSWORD = "Neu123";




}
