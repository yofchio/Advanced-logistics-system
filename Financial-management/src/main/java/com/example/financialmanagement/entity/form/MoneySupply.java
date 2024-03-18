package com.example.financialmanagement.entity.form;

import lombok.Data;

@Data
/**
 * @author YANG FUCHAO
 * @version 1.0
 * @date 2023-06-27 20:01
 */
public class MoneySupply {
  private String id;
  private String goodName;
  private Double goodPrice;
  private Double goodCost;
  private Integer goodSupplyNumber;
  private Integer goodReturnNumber;
  private Integer goodSettleNumber;//结算数量
  private Double goodSettleMoney;//结算金额
  private String goodType;//结算金额
}
