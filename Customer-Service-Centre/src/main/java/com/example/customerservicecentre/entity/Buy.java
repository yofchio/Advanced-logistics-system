package com.example.customerservicecentre.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 进货单
 * </p>
 *
 * @author 杨富超
 * @since 2023-06-27
 */
public class Buy implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer type;

    /**
     * 用户名
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String goodClass;

    /**
     * 用户名
     */
    private Long goodId;

    /**
     * 用户名
     */
    private String goodName;

    /**
     * 用户名
     */
    private String goodUnit;

    /**
     * 用户名
     */
    private String supply;

    /**
     * 用户名
     */
    private Integer number;

    /**
     * 用户名
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date date;
    private Long orderId;
    private Byte buyType;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodClass() {
        return goodClass;
    }

    public void setGoodClass(String goodClass) {
        this.goodClass = goodClass;
    }

    public Long getGoodId() {
        return goodId;
    }

    public void setGoodId(Long goodId) {
        this.goodId = goodId;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public String getGoodUnit() {
        return goodUnit;
    }

    public void setGoodUnit(String goodUnit) {
        this.goodUnit = goodUnit;
    }

    public String getSupply() {
        return supply;
    }

    public void setSupply(String supply) {
        this.supply = supply;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Byte getBuyType() {
        return buyType;
    }

    public void setBuyType(Byte buyType) {
        this.buyType = buyType;
    }

    @Override
    public String toString() {
        return "Buy{" +
        ", type = " + type +
        ", id = " + id +
        ", goodClass = " + goodClass +
        ", goodId = " + goodId +
        ", goodName = " + goodName +
        ", goodUnit = " + goodUnit +
        ", supply = " + supply +
        ", number = " + number +
        ", date = " + date +
        ", orderId = " + orderId +
        ", buyType = " + buyType +
        "}";
    }
}
