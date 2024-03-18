package com.example.warehousemanagementcentre.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * <p>
 * 商品
 * </p>
 *
 * @author hzn
 * @since 2023-06-20
 */
@Data
@TableName("good")
public class Good implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "good_id")
    private Long goodId;
    private Integer classId;
    private Integer keyId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date goodDate;

    private String goodClass;

    private String goodSubclass;

    private String goodName;

    private Long goodNumber;

    private String goodUnit;

    private Double goodPrice;

    private Double goodSale;

    private Double goodCost;

    private String type;

    private String supply;

    private String goodFactory;

    private String sellDate;

    private Byte isReturn;

    private Byte isChange;

    private String remark;
}
