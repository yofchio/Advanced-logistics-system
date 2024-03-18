package com.example.financialmanagement.entity.vo;

import com.github.pagehelper.PageInfo;
import lombok.Data;

/**
 * @author YANG FUCHAO
 * @version 1.0
 * @date 2023-06-20 11:48
 */
@Data
public class ResultSupply {
  private PageInfo pageInfo;
  private double sumMoney;
}
