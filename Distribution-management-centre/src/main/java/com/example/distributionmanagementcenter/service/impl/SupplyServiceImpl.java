package com.example.distributionmanagementcenter.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.distributionmanagementcenter.entity.CentralStation;
import com.example.distributionmanagementcenter.entity.FirstCategory;
import com.example.distributionmanagementcenter.entity.Supply;
import com.example.distributionmanagementcenter.mapper.SupplyMapper;
import com.example.distributionmanagementcenter.service.SupplyService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 供应商 服务实现类
 * </p>
 *
 * @author jason_cai
 * @since 2023-06-19
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class SupplyServiceImpl extends ServiceImpl<SupplyMapper, Supply> implements SupplyService {
@Autowired
private SupplyMapper supplyMapper;
//可以作废：下面的方法不填关键字即可达到一样的效果
    @Override
    public PageInfo getList(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        List<Supply> records= supplyMapper.selectList(null);
        PageInfo pageInfo = new PageInfo(records);
        return pageInfo;
    }

    @Override
    public PageInfo getListByConditions(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        QueryWrapper<Supply> queryWrapper = new QueryWrapper<>();
        String name=(String) map.get("nameKeywords");
        String addr=(String) map.get("addrKeywords");
        String admin=(String) map.get("adminKeywords");
        String tel=(String) map.get("telKeywords");
        if(name!=null&&name!=""){
            queryWrapper.like("name",name);
        }
        if(addr!=null&&addr!=""){
            queryWrapper.like("address",addr);
        }
        if(admin!=null&&admin!=""){
            queryWrapper.like("admin_name",admin);
        }
        if(tel!=null&&tel!=""){
            queryWrapper.like("phone",tel);
        }
        List<Supply> records= supplyMapper.selectList(queryWrapper);
        PageInfo pageInfo = new PageInfo(records);
        return pageInfo;
    }
}
