package com.example.warehousemanagementcentre.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author hzn
 * @create 2023-07-05 10:50
 */
@Data
public class ResultTakeGoods {
    private int taskId;
    private int orderId;
    private Date taskDate;
    private String subStation;
    private String postman;

}
