package com.example.dispatchcentre.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * <p>
 * 商品调拨
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-25
 */
@Data
@TableName("good_allocation")
public class Allocation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private Long orderId;

    private Long taskId;
    /**
     * 用户名
     */
    private Long outStationId;

    /**
     * 用户名
     */
    private Long inStationId;
    /**
     * 用户名
     */
    private String outStationName;

    /**
     * 用户名
     */
    private String inStationName;

    /**
     * 用户名
     */
    private Date allocationDate;

    /**
     * 类型
     */
    private Byte alloType;

    private String distributors;

    private String signer;
    private Date creatDate;
    @Version
    private Integer version;//版本号


}
