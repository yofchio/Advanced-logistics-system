package com.example.distributionmanagementcenter.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;


@Data
@ColumnWidth(20)
public class Receipt implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

//    @ExcelProperty(value = "用户名")
    @ExcelIgnore
    private String customerName;
    @ExcelIgnore
    private String mobilephone;
//    @ExcelProperty(value = "分站名")
      @ExcelIgnore
    private String substation;
//    @ExcelProperty(value = "任务类型")
    @ExcelIgnore
    private String taskType;
//    @ExcelProperty(value = "地址")
@ExcelIgnore
    private String address;
//    @ExcelProperty(value = "商品名称")
@ExcelIgnore
    private String goodName;
//    @ExcelProperty(value = "购买数量")
//    private String number;
    @ExcelProperty(value = "商品价格")
    private Double price;
    @ExcelIgnore
    private String taskStatus;
    @ExcelProperty(value = "客户满意度")
    private String customerSatis;
    @ExcelIgnore
    private String remark;
    @ExcelIgnore
    private Long goodSum;
    @ExcelIgnore
    private Long invoiceNumber;
    @ExcelIgnore
    private String postman;
}
