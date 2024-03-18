package com.yang.bi.manager;

import com.yang.bi.common.ErrorCode;
import com.yang.bi.config.AiModelConfig;
import com.yang.bi.exception.BusinessException;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author YANG FUCHAO
 * CreateTime 2023/5/21 10:42
 */
@Service
public class AiManager {

//    @Value("${yupi.client.access_key:DefaultValue}")
//    private String access_key;
//    @Value("${yupi.client.secret_key:DefaultValue}")
//    private String secret_key;
    @Autowired
    private Environment environment;

    @Autowired
    private AiModelConfig aiModelConfig;

    /**
     * AI 对话
     *
     * @param message 消息
     * @return
     */
    public String doChat(String message) {
        YuCongMingClient congMingClient=new YuCongMingClient(environment.getProperty("yuapi.client.access_key"),environment.getProperty("yuapi.client.secret_key"));
        DevChatRequest devChatRequest = new DevChatRequest();
        // 鱼聪明平台模型ID
        devChatRequest.setModelId(aiModelConfig.getModelId());
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = congMingClient.doChat(devChatRequest);
        if (response == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 响应错误");
        }
        return response.getData().getContent();
    }

    public String doAiChat(Long modelId, String message) {
        YuCongMingClient congMingClient=new YuCongMingClient("gr86i5u3vt18aj06zq7c80k5qtddxbu8","upcu9s1tx0qwcgfyh5zp6h3mcugymgu3");
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = congMingClient.doChat(devChatRequest);
        if (response == null) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 响应错误");
        }
        return response.getData().getContent();
    }
}
