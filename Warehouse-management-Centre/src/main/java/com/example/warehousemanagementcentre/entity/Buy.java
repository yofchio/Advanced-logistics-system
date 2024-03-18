package com.example.warehousemanagementcentre.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 进货单
 * </p>
 *
 * @author Jason_Cai
 * @since 2023-06-20
 */
@Data
public class Buy implements Serializable {

    private static final long serialVersionUID = 1L;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    private int type;
    @TableField("buy_type")
    private Long buyType;
    @TableField("order_id")
    private int orderId;


}
