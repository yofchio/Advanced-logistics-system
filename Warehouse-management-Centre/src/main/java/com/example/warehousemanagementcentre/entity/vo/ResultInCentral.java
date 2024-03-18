package com.example.warehousemanagementcentre.entity.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author hzn
 * @create 2023-07-03 16:59
 */
@Data
public class ResultInCentral {

    private Long buyId;
//    private PageInfo pageInfo;
    private Long centralGoodId;
    //供应商
    private int supply;
    //购货日期
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date buyDate;
    //签收人
    private String signee;
    //分发人
    private String distributors;
    //签收日期
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private Date receiptDate;

    private String remark;

    private String goodName;

    private String goodClass;

    private String goodSubclass;

    private String goodUnit;

    private int buyNumber;



}
