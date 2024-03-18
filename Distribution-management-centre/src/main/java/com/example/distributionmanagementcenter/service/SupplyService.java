package com.example.distributionmanagementcenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.distributionmanagementcenter.entity.Supply;
import com.github.pagehelper.PageInfo;

import java.text.ParseException;
import java.util.Map;

/**
 * <p>
 * 供应商 服务类
 * </p>
 *
 * @author jason_cai
 * @since 2023-06-19
 */
public interface SupplyService extends IService<Supply> {
    public PageInfo getList(Map<String, Object> map) throws ParseException;
    public PageInfo getListByConditions(Map<String, Object> map) throws ParseException;
}
