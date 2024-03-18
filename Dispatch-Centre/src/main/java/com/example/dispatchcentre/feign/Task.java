package com.example.dispatchcentre.feign;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 任务单
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-25
 */
@Data
public class Task implements Serializable {

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

    /**
     * 用户名
     */
    private Long customerId;

    /**
     * 用户名
     */
    private String customerName;

    /**
     * 用户名
     */
    private Date taskDate;

    /**
     * 用户名
     */
    private Date deadline;

    /**
     * 用户名
     */
    private String taskType;

    /**
     * 用户名
     */
    private String taskStatus;

    /**
     * 用户名
     */
    private String address;

    /**
     * 用户名
     */
    private String postman;

    /**
     * 用户名
     */
    private Long substationId;
    /**
     * 用户名
     */
    private String substation;

    /**
     * 用户名
     */
    private Integer printNumber;

    /**
     * 用户名
     */
    private String creater;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    /**
     * 用户名
     */
    private Date getDate;
    @Version
    private Integer version;//版本号

}
