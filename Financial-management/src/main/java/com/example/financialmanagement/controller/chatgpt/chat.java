package com.example.financialmanagement.controller.chatgpt;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.util.Proxys;
import java.net.Proxy;
import java.util.Arrays;

/**
 * @author YANG FUCHAO
 * @version 1.0
 * @date 2023-07-03 18:04
 */
public class chat {

  public static void main(String[] args) {
    //国内需要代理
    Proxy proxy = Proxys.http("127.0.0.1", 7890);
    //socks5 代理
    // Proxy proxy = Proxys.socks5("127.0.0.1", 1080);

    ChatGPT chatGPT = ChatGPT.builder()
        .apiKey("")
        .proxy(proxy)
        .apiHost("https://api.openai.com/") //反向代理地址
        .build()
        .init();
    Message system = Message.ofSystem("你现在是一个诗人，专门写七言绝句");
    Message message = Message.of("写一段七言绝句诗，题目是：火锅！");

    ChatCompletion chatCompletion = ChatCompletion.builder()
        .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
        .messages(Arrays.asList(system, message))
        .maxTokens(3000)
        .temperature(0.9)
        .build();
    ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
    Message res = response.getChoices().get(0).getMessage();
    System.out.println(res);

  }
}
