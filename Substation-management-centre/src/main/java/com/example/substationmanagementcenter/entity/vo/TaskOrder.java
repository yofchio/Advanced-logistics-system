package com.example.substationmanagementcenter.entity.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hzn
 * @create 2023-07-04 15:25
 */
@Data
public class TaskOrder implements Serializable {
    private static final long serialVersionUID = 1L;


    private String receiveName;
    private Double goodSum;
    private Integer isInvoice;
    private String mobilePhone;
    private String customerAddress;

//    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private Long orderId;

    /**
     * 用户名
     */
    private Long customerId;

    /**
     * 用户名
     */
    private String customerName;

    /**
     * 用户名
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date taskDate;

    /**
     * 用户名
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deadline;

    /**
     * 用户名
     */
    private String taskType;

    /**
     * 用户名
     */
    private String taskStatus;

    /**
     * 用户名
     */
    private String address;

    /**
     *
     * 用户名
     */
    private String postman;

    /**
     * 用户名
     */
    private String substation;

    /**
     * 用户名
     */
    private Integer printNumber;

    /**
     * 用户名
     */
    private String creater;
    /**
     * 用户名
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;


}
