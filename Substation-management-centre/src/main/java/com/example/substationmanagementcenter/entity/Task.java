package com.example.substationmanagementcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 退订
 * </p>
 *
 * @author hzn
 * @since 2023-06-19
 */
@Data
public class Task implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @TableId(value = "id", type = IdType.AUTO)
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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /**
     * 用户名
     */
    private Date getDate;
}
