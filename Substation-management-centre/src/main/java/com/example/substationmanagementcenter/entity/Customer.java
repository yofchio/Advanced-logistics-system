package com.example.substationmanagementcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * <p>
 * 用户表信息
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-19
 */
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idcard;

    /**
     * 地址
     */
    private String address;

    /**
     * 用户名
     */
    private String addressphone;

    /**
     * 手机号
     */
    private String mobilephone;

    /**
     * 是否删除(0-未删, 1-已删)
     */
    private Byte isDeleted;

    /**
     * 用户名
     */
    private String work;

    /**
     * 用户名
     */
    private String postcode;

    /**
     * 用户名
     */
    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressphone() {
        return addressphone;
    }

    public void setAddressphone(String addressphone) {
        this.addressphone = addressphone;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public Byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Byte isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
        ", id = " + id +
        ", name = " + name +
        ", idcard = " + idcard +
        ", address = " + address +
        ", addressphone = " + addressphone +
        ", mobilephone = " + mobilephone +
        ", isDeleted = " + isDeleted +
        ", work = " + work +
        ", postcode = " + postcode +
        ", email = " + email +
        "}";
    }
}
