package com.example.customerservicecentre.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import java.io.Serializable;
import lombok.Data;

/**
 * <p>
 * 用户表信息
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-19
 */
@Data
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

    private int userId;
    @Version
    private Integer version;//版本号

}
