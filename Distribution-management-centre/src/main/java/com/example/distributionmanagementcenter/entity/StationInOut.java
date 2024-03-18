package com.example.distributionmanagementcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 库房出库
 * </p>
 *
 * @author Jason_Cai
 * @since 2023-06-20
 */
@TableName("inoutstation")
@Data
public class StationInOut implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Integer stationClass;
    @TableField(exist = false)
    private String stationClassName;
    private Long alloId;
    private Long stationId;
    private String stationName;
    private Long taskId;
    private Long goodId;
    private Double goodPrice;
    private String goodName;
    private String goodUnit;
    private String goodFactory;
    private Long number;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
    private String remark;
    private String type;
    private String distributor;
    private String signer;


}
