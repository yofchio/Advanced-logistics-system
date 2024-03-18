package com.example.distributionmanagementcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.distributionmanagementcenter.entity.Buy;
import com.example.distributionmanagementcenter.entity.CentralStation;
import com.example.distributionmanagementcenter.entity.Good;
import com.example.distributionmanagementcenter.entity.StationInOut;
import com.example.distributionmanagementcenter.mapper.CentralStationMapper;
import com.example.distributionmanagementcenter.mapper.GoodMapper;
import com.example.distributionmanagementcenter.mapper.StationInOutMapper;
import com.example.distributionmanagementcenter.mapper.StationMapper;
import com.example.distributionmanagementcenter.service.StationInOutService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
@Transactional(rollbackFor=Exception.class)
public class StationInOutServiceImpl extends ServiceImpl<StationInOutMapper, StationInOut> implements StationInOutService {
    @Autowired
    private  StationInOutMapper stationInOutMapper;
//    @Autowired
//    private GoodMapper goodMapper;
    @Autowired
    private CentralStationMapper centralStationMapper;
    @Autowired
    private StationMapper stationMapper;
    @Override
    public PageInfo getListByConditions(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        QueryWrapper<StationInOut> queryWrapper = new QueryWrapper<>();
        if(map.get("stationKeyword")!=null&&map.get("stationKeyword")!=""){
            Integer stationType=Integer.valueOf(String.valueOf(map.get("stationKeyword")));
            queryWrapper.eq("station_class",stationType);
        }
        if(map.get("typeKeyword")!=null&&map.get("typeKeyword")!=""){
            String outType=(String)map.get("typeKeyword");
            queryWrapper.like("type",outType);
        }

        if(map.get("goodKeyword")!=null&&map.get("goodKeyword")!="") {
            String goodName=(String)map.get("goodKeyword");
            queryWrapper.like("good_name",goodName);
        }

       if(map.get("startTime")!=null&&map.get("endTime")!=null&&map.get("startTime")!=""&&map.get("endTime")!=""){
           ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
           // 格式化中国时区时间为指定格式的字符串
           DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
           String startDate = LocalDateTime.parse(String.valueOf(map.get("startTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                   ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
           String endDate = LocalDateTime.parse(String.valueOf(map.get("endTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                   ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
           queryWrapper.between("date", startDate, endDate);
       }

        List<StationInOut> records= stationInOutMapper.selectList(queryWrapper);
       for(StationInOut station:records){
           String goodName="";
           String goodFactory="";
           String goodUnit="";
           if(centralStationMapper.selectById(station.getGoodId())!=null){
               goodName=centralStationMapper.selectById(station.getGoodId()).getGoodName();
               goodFactory=centralStationMapper.selectById(station.getGoodId()).getGoodFactory();
               station.setGoodFactory(goodFactory);
               goodUnit=centralStationMapper.selectById(station.getGoodId()).getGoodUnit();
               station.setGoodUnit(goodUnit);
           }
           else{
               goodName="EMPTY";
           }
           station.setGoodName(goodName);

           String stationName="";
           Integer stationClass=0;
           if(stationMapper.selectById(station.getStationId())!=null){
               stationName=stationMapper.selectById(station.getStationId()).getName();
               stationClass=stationMapper.selectById(station.getStationId()).getStationClass();
               station.setStationClass(stationClass);
           }
           else{
               stationName="EMPTY";
           }
           station.setStationName(stationName);
           String stationClassName="";
           if(station.getStationClass()==1){
               stationClassName="中心库房";
           }
           else{
               if(station.getStationClass()==2){
                   stationClassName="分站库房";
               }
               else{
                   stationClassName="EMPTY";
               }

           }
           station.setStationClassName(stationClassName);
           //刷新掉不对应的字段
           stationInOutMapper.updateById(station);
       }
        PageInfo pageInfo = new PageInfo(records);
       //PageHelper只对第一次查询生效，二次查询需要手动设置
//        pageInfo.setPageSize( Integer.valueOf(String.valueOf(map.get("pageSize"))));
//        pageInfo.setPageNum(Integer.valueOf(String.valueOf(map.get("pageNum"))));
        return pageInfo;
    }

    @Override
    public List<StationInOut> getListByConditions1(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        Integer stationType=Integer.valueOf(String.valueOf(map.get("stationType")));
        Integer goodId=(Integer)map.get("good_id");
        String outType=(String)map.get("outType");

        ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
        // 格式化中国时区时间为指定格式的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startDate = LocalDateTime.parse(String.valueOf(map.get("startTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
        String endDate = LocalDateTime.parse(String.valueOf(map.get("endTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
        QueryWrapper<StationInOut> queryWrapper = new QueryWrapper<>();
        queryWrapper.between("date", startDate, endDate)
                .eq("type",outType)
                .eq("station_class",stationType)
                .eq("good_id",goodId);
        List<StationInOut> records= stationInOutMapper.selectList(queryWrapper);
        return records;
    }

    @Override
    public List<StationInOut> getListByTaskId(Long id) throws ParseException {
        QueryWrapper<StationInOut> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_id",id);
        List<StationInOut> records= stationInOutMapper.selectList(queryWrapper);
        return records;
    }

}
