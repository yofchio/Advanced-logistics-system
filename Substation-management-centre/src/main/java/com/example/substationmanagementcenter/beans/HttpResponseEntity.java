package com.example.substationmanagementcenter.beans;

import lombok.Data;

import java.io.Serializable;


//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Data
public class HttpResponseEntity<T> implements Serializable {

    private String code; //状态码
    private T data; //内容
    private String message; //状态消息

}
