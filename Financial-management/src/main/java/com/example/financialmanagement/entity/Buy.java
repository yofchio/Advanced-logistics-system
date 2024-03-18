package com.example.financialmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
/**
 * <p>
 * 进货单
 * </p>
 *
 * @author 杨富超
 * @since 2023-06-29
 */
public class Buy implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer type;

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
    private Date date;
    private Long orderId;
    private Byte buyType;

}
