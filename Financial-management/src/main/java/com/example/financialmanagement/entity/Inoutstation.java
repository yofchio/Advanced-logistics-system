package com.example.financialmanagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * <p>
 * 库房出入库
 * </p>
 *
 * @author 杨富超
 * @since 2023-06-29
 */
@Data
public class Inoutstation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private Integer stationClass;

    /**
     * 用户名
     */
    private Long alloId;

    /**
     * 用户名
     */
    private Long stationId;

    /**
     * 用户名
     */
    private Long taskId;

    /**
     * 用户名
     */
    private Long goodId;

    /**
     * 用户名
     */
    private Object goodPrice;

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
    private String goodFactory;

    /**
     * 用户名
     */
    private Long number;

    /**
     * 用户名
     */
    private Date date;

    /**
     * 用户名
     */
    private String remark;

    /**
     * 用户名
     */
    private String type;

}
