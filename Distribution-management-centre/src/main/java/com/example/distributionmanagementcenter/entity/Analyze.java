package com.example.distributionmanagementcenter.entity;

import lombok.Data;

@Data
public class Analyze implements Comparable<Analyze> {

    private Long number;
    private Double totalMoney;
    private Integer totalTask;
    private String stationName;
    //按总钱数排序
    @Override
    public int compareTo(Analyze analyze) {
        return  analyze.getTotalMoney() - this.getTotalMoney() > 0 ? 1 : ((this.getTotalMoney() == analyze.getTotalMoney() ) ? 0 :-1);   //降序：返回值为1 或-1 升序改变变量位置即可
    }

}
