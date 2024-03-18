package com.example.distributionmanagementcenter.entity;

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
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String goodClass;
    private Long goodId;
    private String goodName;
    private String goodUnit;
    private String supply;
    private Long number;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
    private Integer type;
    @TableField(exist = false)
    private String typeName;
    @TableField("buy_type")
    private Byte buyType;
    @TableField(exist = false)
    private String buyTypeName;
    @TableField("order_id")
    private Long orderId;
    //前端显示专用
    @TableField(exist = false)
    private Long waitAllo;
    @TableField(exist = false)
    private Long withdrawal;

}
