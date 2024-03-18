package com.example.financialmanagement.entity.vo;

import com.github.pagehelper.PageInfo;
import java.util.Date;
import lombok.Data;

/**
 * @author YANG FUCHAO
 * @version 1.0
 * @date 2023-06-20 11:48
 */
@Data
public class ResultStation {
  private PageInfo pageInfo;
  private double sumMoney;
  private double getMoney;
  private double returnMoney;
}
