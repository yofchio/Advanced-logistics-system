package com.example.warehousemanagementcentre.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-19
 */
@Data
@TableName("orders")
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String creater;

    /**
     * 用户名
     */
    private Double goodSum;

    /**
     * 用户名
     */
    @TableField(value = "`explain`")
    private String explain;

    /**
     * 用户名
     */
    private String remark;

    /**
     * 用户名
     */
    private String substation;

    /**
     * 用户名
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date orderDate;

    /**
     * 用户名
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date deliveryDate;

    /**
     * 用户名
     */
    private String orderType;

    /**
     * 用户名
     */
    private String orderStatus;

    /**
     * 用户名
     */
    private String customerAddress;

    /**
     * 用户名
     */
    private String customerId;

    /**
     * 用户名
     */
    private String customerName;

    /**
     * 用户名
     */
    private String mobilephone;

    /**
     * 用户名
     */
    private String postcode;

    /**
     * 用户名
     */
    private Integer isInvoice;

    /**
     * 用户名
     */
    private String goodStatus;

    private String receive_name;

}
