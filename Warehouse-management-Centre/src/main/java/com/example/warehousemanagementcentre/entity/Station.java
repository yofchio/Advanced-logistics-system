package com.example.warehousemanagementcentre.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 库房
 * </p>
 *
 * @author Jason_cai
 * @since 2023-06-19
 */
@Data
public class Station implements Serializable{

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private String address;

    private String admin;

    private Integer stationClass;

    @TableField(exist = false)
    private String stationClassName;

    @TableField(exist = false)
    private List<CentralStation> centralStationList;

}
