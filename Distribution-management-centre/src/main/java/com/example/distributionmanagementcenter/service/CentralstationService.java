package com.example.distributionmanagementcenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.distributionmanagementcenter.entity.CentralStation;
import com.github.pagehelper.PageInfo;

import java.text.ParseException;
import java.util.Map;

/**
 * <p>
 * 中心库房存量  服务类
 * </p>
 *
 * @author Jason_cai
 * @since 2023-06-19
 */
public interface CentralstationService extends IService<CentralStation> {
    PageInfo getListByCondition(Map<String, Object> map) throws ParseException;

    CentralStation checkById(Map<String, Object> map) throws ParseException;
    PageInfo getList(Map<String, Object> map) throws ParseException;

    int updateList(Map<String, Object> map) throws ParseException;

   String addBuyList(Map<String, Object> map) throws ParseException;

   String addRegistList(Map<String, Object> map) throws ParseException;

  CentralStation getAliGoodName(String name) throws ParseException;

}
