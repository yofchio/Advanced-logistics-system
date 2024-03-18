package com.example.distributionmanagementcenter.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 供应商
 * </p>
 *
 * @author jason_cai
 * @since 2023-06-19
 */
@Data
public class Supply implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String address;
    private String adminName;
    private String phone;
    private String bank;
    private String bankNumer;
    private String fax;
    private String postcode;
    private String legalPerson;
    private String remark;

}
