package com.example.financialmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * <p>
 * 中心库房存量
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-26
 */
@Data
@TableName("stock_central_station")
public class CentralStation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String goodName;

    @TableField("good_class_id")
    private Integer goodClassId;

    @TableField("good_subclass_id")
    private Integer goodSubclassId;
    /**
     * 用户名
     */
    private Integer stock;

    /**
     * 用户名
     */
    private Integer withdrawal;

    /**
     * 用户名
     */
    private Integer waitAllo;

    /**
     * 用户名
     */
    private Integer doneAllo;

    /**
     * 用户名
     */
    private Integer warn;

    /**
     * 用户名
     */
    private Integer max;
    private Double goodPrice;
    private Double goodSale;
    private Double goodCost;
    private String goodUnit;
    private String supplyName;


}
