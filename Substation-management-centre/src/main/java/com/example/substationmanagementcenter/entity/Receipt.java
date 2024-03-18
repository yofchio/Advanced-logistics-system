package com.example.substationmanagementcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 回执单
 * </p>
 *
 * @author hzn
 * @since 2023-06-21
 */
@Data
public class Receipt implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

//    private Integer replyClass;

    private Long taskId;

    private String customerName;

    private String mobilephone;

    private String substation;

    private String taskType;

    private String address;

//    private String goodName;

//    private String number;

    private Double price;

    private String taskStatus;

    private String customerSatis;

    private String remark;

//    private Long goodSum;

    private Long invoiceNumber;
    private String postman;

    /**
     * 用户名
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;
}
