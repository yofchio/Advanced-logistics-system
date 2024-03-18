package com.example.warehousemanagementcentre.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.warehousemanagementcentre.entity.CentralStation;
import com.example.warehousemanagementcentre.entity.Inoutstation;
import com.example.warehousemanagementcentre.mapper.InoutstationMapper;
import com.example.warehousemanagementcentre.service.InoutstationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库房出入库 服务实现类
 * </p>
 *
 * @author hzn
 * @since 2023-06-26
 */
@Service
public class InoutstationServiceImpl extends ServiceImpl<InoutstationMapper, Inoutstation> implements InoutstationService {

    @Autowired
    private InoutstationMapper inoutstationMapper;
    @Override
    public PageInfo selectByType(Map<String, Object> map) {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        //中心库存中对应的数据
        QueryWrapper<Inoutstation> inoutstationQueryWrapper = new QueryWrapper<>();
        if(!map.get("type").equals("")){
            inoutstationQueryWrapper.eq("type", map.get("type"));
        }


        List<Inoutstation> inoutstations = inoutstationMapper.selectList(inoutstationQueryWrapper);
        PageInfo pageInfo = new PageInfo(inoutstations);
        return pageInfo;
    }

    @Override
    public PageInfo getOut(Map<String, Object> map) {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
            Integer.valueOf(String.valueOf(map.get("pageSize"))));
        //中心库存中对应的数据
        QueryWrapper<Inoutstation> inoutstationQueryWrapper = new QueryWrapper<>();
        ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
        // 格式化中国时区时间为指定格式的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startDate = LocalDateTime.parse(String.valueOf(map.get("startTime")),
            DateTimeFormatter.ISO_DATE_TIME).atZone(
            ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
        String endDate = LocalDateTime.parse(String.valueOf(map.get("endTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
            ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);

        inoutstationQueryWrapper.between("date", startDate, endDate);

        if(!map.get("id").equals("")) {
            Long id = Long.valueOf(String.valueOf(map.get("id")));
            inoutstationQueryWrapper.eq("id", id);
        }
        inoutstationQueryWrapper.eq("type", "中心退货");
        List<Inoutstation> inoutstations = inoutstationMapper.selectList(inoutstationQueryWrapper);
        PageInfo pageInfo = new PageInfo(inoutstations);
        return pageInfo;
    }

    @Override
    public int changeOutType(Long id) {
        //中心库存中对应的数据
        Inoutstation inoutstation=new Inoutstation();
        inoutstation.setId(id);
        inoutstation.setType("中心退货完成");
        return  inoutstationMapper.updateById(inoutstation);
    }
}
