package com.example.warehousemanagementcentre.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.warehousemanagementcentre.entity.Inoutstation;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * <p>
 * 库房出入库 服务类
 * </p>
 *
 * @author hzn
 * @since 2023-06-26
 */
public interface InoutstationService extends IService<Inoutstation> {

    PageInfo selectByType(Map<String,Object> map);
    PageInfo getOut(Map<String,Object> map);
     int changeOutType(Long id);
}
