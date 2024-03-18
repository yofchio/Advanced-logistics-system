package com.example.distributionmanagementcenter.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class HttpResponseEntity<T> implements Serializable {

    private String code; //状态码
    private T data; //内容
    private String message; //状态消息

}
