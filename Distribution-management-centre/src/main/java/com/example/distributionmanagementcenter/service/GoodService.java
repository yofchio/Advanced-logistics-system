package com.example.distributionmanagementcenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.distributionmanagementcenter.entity.Good;
import com.github.pagehelper.PageInfo;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品 服务类
 * </p>
 *
 * @author jason_cai
 * @since 2023-06-19
 */
public interface GoodService extends IService<Good> {
    PageInfo getListByOrderId(Map<String, Object> map) throws ParseException;
    List<Good> getGoodByOrderId(Long id);

    PageInfo getRanking(Map<String, Object> map) throws ParseException;

    PageInfo getListByGoodId(Map<String, Object> map) throws ParseException;
     List<Good> getListByGoodId1(Map<String, Object> map) throws ParseException;

     boolean saveGood(Good params);
    boolean deleteGoodById(Long id);
    boolean updateGood(Good params);
    public PageInfo getList(Map<String, Object> map) throws ParseException;
}
