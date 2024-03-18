package com.example.warehousemanagementcentre.entity.vo;

import com.example.warehousemanagementcentre.entity.Inoutstation;
import lombok.Data;

import java.util.List;

/**
 * @author hzn
 * @create 2023-07-17 9:35
 */
@Data
public class AllDistribute {
    private List<Inoutstation> inoutstations;
    private Long goodNumberSum;
    private double priceSum;
}
