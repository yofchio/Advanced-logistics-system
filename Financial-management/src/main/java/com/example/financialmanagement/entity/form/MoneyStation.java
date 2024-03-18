package com.example.financialmanagement.entity.form;

import lombok.Data;

@Data
/**
 * @author YANG FUCHAO
 * @version 1.0
 * @date 2023-06-27 20:01
 */
public class MoneyStation {
  private String id;
  private String goodClass;
  private String goodName;
  private Integer goodGetNumber;
  private Double goodGetMoney;
  private Integer goodReturnNumber;
  private Double goodReturnMoney;
  private String goodType;//结算金额
}
