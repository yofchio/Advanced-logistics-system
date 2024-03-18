package com.example.financialmanagement.entity;

import java.util.Date;
import lombok.Data;

/**
 * @author YANG FUCHAO
 * @version 1.0
 * @date 2023-06-20 11:40
 */
@Data
public class SettlementSupply {
  private String goodName;
  private int goodUnit;
  private long settleNum;
  private long returnNum;
  private long sum;//数量
  private double money;//金额
  private Date date;//进货日期
}
