package com.example.warehousemanagementcentre.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.warehousemanagementcentre.entity.Station;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * <p>
 * 库房出库 服务类
 * </p>
 *
 * @author hzn
 * @since 2023-06-21
 */
public interface StationService extends IService<Station> {

    PageInfo selectAll(Map<String,Object> map);

//    PageInfo getInStation(Map<String,Object> map);

    int updatebyId(Station station);
}
