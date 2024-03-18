package com.example.warehousemanagementcentre.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author hzn
 * @create 2023-07-17 9:35
 */
@Data
public class AllOutStation {
    private List<OutStationNote> outStationNotes;
    private Long goodNumberSum;
    private double priceSum;
}
