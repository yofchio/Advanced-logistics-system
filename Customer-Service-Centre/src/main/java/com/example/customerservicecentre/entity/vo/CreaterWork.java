package com.example.customerservicecentre.entity.vo;

import lombok.Data;

/**
 * @author YANG FUCHAO
 * @version 1.0
 * @date 2023-06-26 10:53
 * 操作员工作量查询，返回给前端的实体类
 */
@Data
public class CreaterWork {

  private  String creater;

  /**
   * 用户名
   */
  private String goodSubclass;

  /**
   * 用户名
   */
  private String goodName;

  private Integer newOrderNum;

  private Integer newGoodNum;

  private Double newOrderMoney;

  private Integer returnOrderNum;

  private Integer returnGoodNum;

  private Double returnOrderMoney;

  private Integer changeOrderNum;

  private Integer changeGoodNum;

  private Double changeOrderMoney;

  private Integer unsubOrderNum;

  private Integer unsubGoodNum;

  private Double unsubOrderMoney;

  private Double income;

}
