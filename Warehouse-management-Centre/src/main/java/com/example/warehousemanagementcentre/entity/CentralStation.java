package com.example.warehousemanagementcentre.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 中心库房存量
 * </p>
 *
 * @author hzn
 * @since 2023-06-27
 */
@Data
@TableName("stock_central_station")
public class CentralStation implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("good_class_id")
    private Integer goodClassId;

    @TableField(exist = false)
    private String goodClassName;

    @TableField("good_subclass_id")
    private Integer goodSubclassId;

    @TableField(exist = false)
    private String goodSubClassName;

    @TableField("good_name")
    private String goodName;

    private Long stock;

    private Long withdrawal;

    private Long waitAllo;

    private Long doneAllo;

    private Long warn;

    private Long max;

    @TableField("good_price")
    private Double goodPrice;

    @TableField("good_sale")
    private Double goodSale;

    @TableField("good_cost")
    private Double goodCost;

    private String goodUnit;

    private Integer supplyId;
    @TableField(exist = false)
    private String supplyName;

    @TableField("sell_date")
    private Integer sellDate;

    private Byte isReturn;
    @TableField(exist = false)
    private String isReturnName;

    private Byte isChange;
    @TableField(exist = false)
    private String isChangeName;

    private String remark;

    @TableField("station_id")
    private Integer stationId;
    @TableField(exist = false)
    private String stationName;

}
