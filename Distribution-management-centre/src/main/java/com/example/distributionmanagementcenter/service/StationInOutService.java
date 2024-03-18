package com.example.distributionmanagementcenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.distributionmanagementcenter.entity.StationInOut;
import com.github.pagehelper.PageInfo;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库房出库 服务类
 * </p>
 *
 * @author Jason_Cai
 * @since 2023-06-20
 */
public interface StationInOutService extends IService<StationInOut> {
    PageInfo getListByConditions(Map<String, Object> map) throws ParseException;
    List<StationInOut> getListByConditions1(Map<String, Object> map) throws ParseException;

    List<StationInOut> getListByTaskId(Long id) throws ParseException;
}
