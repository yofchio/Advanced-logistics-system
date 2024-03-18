package com.example.distributionmanagementcenter.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.distributionmanagementcenter.entity.Buy;
import com.example.distributionmanagementcenter.entity.CentralStation;
import com.example.distributionmanagementcenter.entity.FirstCategory;
import com.example.distributionmanagementcenter.mapper.*;
import com.example.distributionmanagementcenter.service.CentralstationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 中心库房存量  服务实现类
 * </p>
 *
 * @author Jason_cai
 * @since 2023-06-19
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class CentralStationServiceImpl extends ServiceImpl<CentralStationMapper, CentralStation> implements CentralstationService {
   @Autowired
   private CentralStationMapper centralStationMapper;
   @Autowired
   private SupplyMapper supplyMapper;
   @Autowired
   private FirstCategoryMapper firstCategoryMapper;
   @Autowired
   private SecondaryCategoryMapper secondaryCategoryMapper;
   @Autowired
   private StationMapper stationMapper;

   @Autowired
   private BuyMapper buyMapper;

    @Override
    public PageInfo getListByCondition(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        QueryWrapper<CentralStation> queryWrapper = new QueryWrapper<>();
        if(map.get("goodClassId")!=null){
            queryWrapper.eq("good_class_id",map.get("goodClassId"));
        }
        if(map.get("goodSubclassId")!=null){
            queryWrapper.eq("good_subclass_id",map.get("goodSubclassId"));
        }
        if((String) map.get("keywords")!=null&& !Objects.equals((String) map.get("keywords"), "")){
            String pattern = (String) map.get("keywords");
            queryWrapper.like("good_name",pattern);
        }
       if(map.get("stationId")!=null){
           queryWrapper.eq("station_id",map.get("stationId"));
       }
        if(map.get("supplyId")!=null){
            queryWrapper.eq("supply_id",map.get("supplyId"));
        }
        List<CentralStation> records= centralStationMapper.selectList(queryWrapper);
        for(CentralStation centralStation :records){
            String goodClassName="";
            if(firstCategoryMapper.selectById(centralStation.getGoodClassId())==null){
                goodClassName="EMPTY";
            }
            else{
                goodClassName=firstCategoryMapper.selectById(centralStation.getGoodClassId()).getFName();
            }
            centralStation.setGoodClassName(goodClassName);
            String goodSubClassName ="";
            if(secondaryCategoryMapper.selectById(centralStation.getGoodSubclassId())==null){
                goodSubClassName="EMPTY";
            }
            else{
                goodSubClassName= secondaryCategoryMapper.selectById(centralStation.getGoodSubclassId()).getSName();
            }
            centralStation.setGoodSubClassName(goodSubClassName);
            String stationName ="";
            if(stationMapper.selectById(centralStation.getStationId())==null){
                stationName="EMPTY";
            }
            else{
                stationName= stationMapper.selectById(centralStation.getStationId()).getName();
            }
            centralStation.setStationName(stationName);
            String supplyName ="";
            if(supplyMapper.selectById(centralStation.getSupplyId())==null){
                supplyName="EMPTY";
            }
            else{
                supplyName= supplyMapper.selectById(centralStation.getSupplyId()).getName();
            }
            centralStation.setSupplyName(supplyName);
            if(centralStation.getIsReturn()==1){
                centralStation.setIsReturnName("是");
            }
            if(centralStation.getIsReturn()==0){
                centralStation.setIsReturnName("否");
            }
            if(centralStation.getIsChange()==1){
             centralStation.setIsChangeName("是");
            }
            if(centralStation.getIsChange()==0){
                centralStation.setIsChangeName("否");
            }
            //缺货检查
            if(centralStation.getWaitAllo()!=null&&centralStation.getWarn()!=null){
                if(centralStation.getWaitAllo()<centralStation.getWarn()){
                    centralStation.setVacancy(centralStation.getWarn()-centralStation.getWaitAllo());
                }
                else{
                    centralStation.setVacancy(0L);
                }
            }

        }
        PageInfo pageInfo = new PageInfo(records);
        return pageInfo;
    }

    @Override
    public CentralStation checkById(Map<String, Object> map) throws ParseException {
        CentralStation centralStation=centralStationMapper.selectById(Integer.valueOf(String.valueOf(map.get("id"))));

        Long num =Long.valueOf(String.valueOf(map.get("goodNumber")));
        if(centralStation.getWaitAllo()!=null&&centralStation.getWarn()!=null){
            if(centralStation.getWaitAllo()<centralStation.getWarn()){
                centralStation.setVacancy(centralStation.getWarn()-centralStation.getWaitAllo());
            }
            else{
                if(num <=centralStation.getWaitAllo()){
                    centralStation.setWaitAllo(centralStation.getWaitAllo()- num);
                    centralStation.setDoneAllo(centralStation.getDoneAllo()+ num);
                    centralStation.setVacancy(0L);
                }
                else{
                    centralStation.setVacancy(num-centralStation.getWaitAllo());
                }
            }
        }
        return centralStation;
    }

    @Override
    public PageInfo getList(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));

        List<CentralStation> records= centralStationMapper.selectList(null);
        PageInfo pageInfo = new PageInfo(records);
        return pageInfo;
    }

    @Override
    public int updateList(Map<String, Object> map) throws ParseException {
        int flag=0;
        List<Integer> list = (List<Integer>) map.get("idList");
        if( (Long.valueOf((String)map.get("max")))<=(Long.valueOf((String)map.get("warn")))){
            flag=0;
        }else{
            for(Integer integer :list){
                CentralStation centralStation = new CentralStation();
                centralStation.setId(integer.longValue());
                centralStation.setMax(Long.valueOf((String)map.get("max")));
                centralStation.setWarn(Long.valueOf((String)map.get("warn")));
                centralStationMapper.updateById(centralStation);
            }
            flag=1;
        }

        return flag;
    }

    @Override
    public String addBuyList(Map<String, Object> map) throws ParseException {
        int flag=0;
        String result="";
        Map<String,String> list =  (Map<String,String>)map.get("list");
       Date date= new Date();
        if(map.get("time")!=null&&map.get("time")!=""){
            ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
            // 格式化中国时区时间为指定格式的字符串
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateString = LocalDateTime.parse(String.valueOf(map.get("time")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                    ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);

            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
        }
        else{
            String dateString=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
           date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
        }

        for (Map.Entry<String,String> entry : list.entrySet()) {

            if(!Objects.equals(entry.getValue(), "") &&entry.getValue()!=null){
               Buy buy = new Buy();
               buy.setGoodId(Long.valueOf(entry.getKey()));
               CentralStation centralStation=centralStationMapper.selectById(Long.valueOf(entry.getKey()));
               buy.setGoodName(centralStation.getGoodName());
               buy.setGoodUnit(centralStation.getGoodUnit());
               buy.setType(1);
               Long number =Long.valueOf(entry.getValue());
               buy.setNumber(number);
                buy.setDate(date);
                buy.setSupply((String)map.get("supply"));
                if(number<=centralStation.getMax()-centralStation.getWaitAllo()){
                    buyMapper.insert(buy);
                    centralStation.setWaitAllo(centralStation.getWaitAllo()+number);
                    centralStationMapper.updateById(centralStation);
                }else {
                    flag++;
                }
            }
        }
        if(flag!=0){
            result=null;
        }
        else{
            result="Success";
        }

        return result;
    }

    @Override
    public String addRegistList(Map<String, Object> map) throws ParseException {
        String result="";
        int flag=0;
        Map<String,String> list =  (Map<String,String>)map.get("list");
        Date date= new Date();
        if(map.get("time")!=null&&map.get("time")!=""){
            ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
            // 格式化中国时区时间为指定格式的字符串
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String dateString = LocalDateTime.parse(String.valueOf(map.get("time")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                    ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
        }
        else{
            String dateString=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);

        }

        for (Map.Entry<String,String> entry : list.entrySet()) {

            if(!Objects.equals(entry.getValue(), "") &&entry.getValue()!=null){
                Buy buy = new Buy();
                buy.setGoodId(Long.valueOf(entry.getKey()));
                CentralStation centralStation=centralStationMapper.selectById(Long.valueOf(entry.getKey()));
                buy.setGoodName(centralStation.getGoodName());
                buy.setGoodUnit(centralStation.getGoodUnit());
                buy.setType(2);
                Long number =Long.valueOf(entry.getValue());
                buy.setNumber(number);
                buy.setDate(date);
                buy.setSupply((String)map.get("supply"));
                if(number<=centralStation.getMax()-centralStation.getWaitAllo()){
                    buyMapper.insert(buy);

                }else {
                    flag++;
                }

            }
        }
        if(flag!=0){
            result=null;
        }
        else{
            result="Success";
        }
        return result;
    }

  @Override
  public CentralStation getAliGoodName(String name) throws ParseException {
      QueryWrapper<CentralStation>  queryWrapper=new QueryWrapper();
    queryWrapper.eq("good_name",name);
    CentralStation centralStation=centralStationMapper.selectOne(queryWrapper);
    return centralStation;
  }
}
