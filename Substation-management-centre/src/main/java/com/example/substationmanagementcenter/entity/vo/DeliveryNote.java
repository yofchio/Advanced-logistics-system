package com.example.substationmanagementcenter.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.pagehelper.PageInfo;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hzn
 * @create 2023-07-12 15:52
 */
@Data
public class DeliveryNote implements Serializable {
    /**
     * 任务号t
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 客户姓名t
     */
    private String customerName;

    /**
     * 联系电话(order)
     */
    private String mobilephone;

    /**
     * 邮编(order)
     */
    private String postcode;

    /**
     * 送货地址t
     */
    private String address;

    /**
     * 送货日期（order）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date deliveryDate;


    /**
     * 送货分站（t）
     */
    private String substation;

    /**
     * 是否要发票（order）
     */
    private Integer isInvoice;

    /**
     * 商品名（中心）
     */
    private String goodName;

    /**
     * 商品单价（中心）
     */
    private Double goodPrice;

    /**
     * 总额（order）
     */
    private Double goodSum;

    /**
     * 任务类型t
     */
    private String taskType;

    /**
     * 备注（order）
     */
    private String remark;

    /**
     * 客户反馈
     */
    private String feedback;

    /**
     * 客户签名
     */
    private String signature;


    private PageInfo goods;

}
