package com.example.financialmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * <p>
 * 发票
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-20
 */
@Data
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField(value = "invoiceClass")
    private String invoiceClass;

    /**
     * 用户名
     */
    private Long startNumber;

    /**
     * 用户名
     */
    private Long endNumber;

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
    @Version
    private Integer version;//版本号

}
