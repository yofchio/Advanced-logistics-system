package com.example.distributionmanagementcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.distributionmanagementcenter.entity.*;
import com.example.distributionmanagementcenter.mapper.*;
import com.example.distributionmanagementcenter.service.GoodService;
;
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
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 商品 服务实现类
 * </p>
 *
 * @author jason_cai
 * @since 2023-06-19
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class GoodServiceImpl extends ServiceImpl<GoodMapper, Good> implements GoodService {
    @Autowired
    private GoodMapper goodMapper;
    @Autowired
    private CentralStationMapper centralStationMapper;
    @Autowired
    private FirstCategoryMapper firstCategoryMapper;
    @Autowired
    private SecondaryCategoryMapper secondaryCategoryMapper;
    @Autowired
    private SupplyMapper supplyMapper;
    @Override
    public PageInfo getList(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        QueryWrapper<Good> queryWrapper = new QueryWrapper<>();
        if(map.get("supplyName")!=null&&map.get("supplyName")!=""){
            queryWrapper.like("supply",map.get("supplyName"));
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

        List<Good> records= goodMapper.selectList(queryWrapper);
        for(Good good:records){
            CentralStation centralStation = centralStationMapper.selectById(good.getGoodId());
            good.setGoodName(centralStation.getGoodName());
            Supply supply = supplyMapper.selectById(centralStation.getSupplyId());
            good.setSupply(supply.getName());
            good.setIsChange(centralStation.getIsChange());
            good.setIsReturn(centralStation.getIsReturn());
            FirstCategory firstCategory = firstCategoryMapper.selectById(centralStation.getGoodClassId());
            good.setGoodClass(firstCategory.getFName());
            SecondaryCategory secondaryCategory = secondaryCategoryMapper.selectById(centralStation.getGoodSubclassId());
            good.setGoodSubclass(secondaryCategory.getSName());
            goodMapper.updateById(good);
        }
        PageInfo pageInfo = new PageInfo(records);
        return pageInfo;
    }
    @Override
    public PageInfo getListByOrderId(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
       Long keyId=Long.valueOf(String.valueOf(map.get("keyId")));
        QueryWrapper<Good> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("key_id",keyId);
        List<Good> records= goodMapper.selectList(queryWrapper);
        PageInfo pageInfo = new PageInfo(records);
        return pageInfo;
    }
//功能同上
    @Override
    public List<Good> getGoodByOrderId(Long id) {
        QueryWrapper<Good> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("key_id",id);
        List<Good> records= goodMapper.selectList(queryWrapper);
        return records;
    }

    @Override
    public PageInfo getRanking(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        QueryWrapper<Good> queryWrapper = new QueryWrapper<>();
        if(map.get("startTime")!=null&&map.get("endTime")!=null&&map.get("startTime")!=""&&map.get("endTime")!=""){

            ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
            // 格式化中国时区时间为指定格式的字符串
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String startDate = LocalDateTime.parse(String.valueOf(map.get("startTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                    ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
            String endDate = LocalDateTime.parse(String.valueOf(map.get("endTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                    ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
            queryWrapper.between("good_date", startDate, endDate);
        }
        List<Good> records = goodMapper.selectList(queryWrapper);
        List<Good> records1 = new ArrayList<Good>();
        for(Good good :records){
            int flag=0;
            for(Good good1:records1){
                if(good1.getGoodId()==good.getGoodId()){
                    good1.setGoodNumber(good1.getGoodNumber()+good.getGoodNumber());
                    flag=1;
                }
            }
            if(flag==0){
                records1.add(good);
            }
        }
//        records1.sort(Comparator.naturalOrder());
        Collections.sort(records1);
        PageInfo pageInfo = new PageInfo(records1);
        return pageInfo;
    }

    @Override
    public PageInfo getListByGoodId(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        Integer goodId=Integer.valueOf(String.valueOf(map.get("goodId")));
        System.out.println(map);
        QueryWrapper<Good> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("good_id",goodId);
        List<Good> records= goodMapper.selectList(queryWrapper);
        PageInfo pageInfo = new PageInfo(records);
        return pageInfo;
    }
    @Override
    public  List<Good>  getListByGoodId1(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        Integer goodId=Integer.valueOf(String.valueOf(map.get("goodId")));
        System.out.println(map);
        QueryWrapper<Good> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("good_id",goodId);
        List<Good> records= goodMapper.selectList(queryWrapper);
        return records;
    }

    @Override
    public boolean saveGood(Good params) {
        try {
            CentralStation centralStation=centralStationMapper.selectById(params.getGoodId());
            centralStation.setWaitAllo(centralStation.getWaitAllo()-params.getGoodNumber());
            centralStation.setDoneAllo(centralStation.getDoneAllo()+params.getGoodNumber());
            Date date = new Date();
            String dateString=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
             date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateString);
             params.setGoodDate(date);
            centralStationMapper.updateById(centralStation);
            goodMapper.insert(params);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean deleteGoodById(Long id) {
        try {
            Good good = goodMapper.selectById(id);
            CentralStation centralStation=centralStationMapper.selectById(good.getGoodId());
            centralStation.setWaitAllo(centralStation.getWaitAllo()+good.getGoodNumber());
            centralStation.setDoneAllo(centralStation.getDoneAllo()-good.getGoodNumber());
            centralStationMapper.updateById(centralStation);
            goodMapper.deleteById(id);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    @Override
    public boolean updateGood(Good params) {
        try {
            Good good =goodMapper.selectById(params.getId());
            CentralStation centralStation=centralStationMapper.selectById(params.getGoodId());
            centralStation.setWaitAllo(centralStation.getWaitAllo()-params.getGoodNumber()+good.getGoodNumber());
            centralStation.setDoneAllo(centralStation.getDoneAllo()+params.getGoodNumber()-good.getGoodNumber());
            centralStationMapper.updateById(centralStation);
            goodMapper.updateById(params);
            return true;
        }catch (Exception e){
            return false;
        }

    }
}
