package com.example.distributionmanagementcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * <p>
 * 商品
 * </p>
 *
 * @author jason_cai
 * @since 2023-06-19
 */
@Data
//订单记录
public class Good implements Serializable, Comparable<Good>{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @TableField(value = "good_id")
    private Long goodId;
    private Integer classId;
    private Integer keyId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date goodDate;

    private String goodClass;

    private String goodSubclass;

    private String goodName;

    private Long goodNumber;

    private String goodUnit;

    private Double goodPrice;

    private Double goodSale;

    private Double goodCost;

    private String type;

    private String supply;

    private String goodFactory;

    private String sellDate;

    private Byte isReturn;

    private Byte isChange;

    private String remark;

    private Integer version;
    //排序
    @Override
    public int compareTo(Good good) {
        return  good.getGoodNumber() - this.getGoodNumber() > 0 ? 1 : ((this.getGoodNumber()== good.getGoodNumber()) ? 0 :-1);   //降序：返回值为1 或-1 升序改变变量位置即可
    }
}
