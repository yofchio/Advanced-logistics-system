package com.example.warehousemanagementcentre.entity.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author hzn
 * @create 2023-07-17 9:28
 */
@Data
public class OutStationNote implements Serializable {
    /**
     * 商品编号
     */
    private Long goodId;

    /**
     * 商品名
     */
    private String goodName;

    /**
     * 售价
     */
    private Double goodPrice;

    /**
     * 商品数量
     */
    private Long number;

    /**
     * 厂商
     */
    private String goodFactory;

    /**
     * 备注
     */
    private String remark;

    /**
     * 日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
}
