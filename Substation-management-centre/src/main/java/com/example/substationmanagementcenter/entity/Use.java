package com.example.substationmanagementcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 发票
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-28
 */
@Data
@TableName("invoice_use")
public class Use implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String type;

    /**
     * 用户名
     */
    private Long number;

    /**
     * 用户名
     */
    private Integer batch;

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户名
     */
    private Date date;

    /**
     * 用户名
     */
    private Long orderId;

    /**
     * 用户名
     */
    private Object money;

    /**
     * 用户名
     */
    private String station;

}
