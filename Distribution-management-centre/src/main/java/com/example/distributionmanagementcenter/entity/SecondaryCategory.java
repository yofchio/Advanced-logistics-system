package com.example.distributionmanagementcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 *
 * </p>
 *
 * @author Jason_cai
 * @since 2023-06-19
 */
//二级商品分类
@TableName("secondary_category")
@Data
public class SecondaryCategory implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableField(value="sname")
    private String sName;
    @TableField(value="f_id")
    private Integer fId;
    @TableField(exist = false)
    private String fName;
    private String description;
}
