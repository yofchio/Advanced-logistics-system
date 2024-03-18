package com.example.distributionmanagementcenter.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.distributionmanagementcenter.entity.Buy;
import com.example.distributionmanagementcenter.entity.CentralStation;
import com.example.distributionmanagementcenter.entity.FirstCategory;
import com.example.distributionmanagementcenter.entity.StationInOut;
import com.example.distributionmanagementcenter.mapper.BuyMapper;
import com.example.distributionmanagementcenter.mapper.CentralStationMapper;
import com.example.distributionmanagementcenter.mapper.FirstCategoryMapper;
import com.example.distributionmanagementcenter.mapper.StationInOutMapper;
import com.example.distributionmanagementcenter.service.BuyService;
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
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional(rollbackFor=Exception.class)
public class BuyServiceImpl extends ServiceImpl<BuyMapper, Buy> implements BuyService {
      @Autowired
      private  BuyMapper buyMapper;
      @Autowired
      private CentralStationMapper centralStationMapper;
      @Autowired
      private StationInOutMapper stationInOutMapper;
      @Autowired
      private FirstCategoryMapper firstCategoryMapper;
    @Override
    public PageInfo getListByDateSupply(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        QueryWrapper<Buy> queryWrapper = new QueryWrapper<>();
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
        if(map.get("id")!=null&&map.get("id")!=""){
            queryWrapper.eq("good_id",map.get("id"));
        }
        List<Buy> records= buyMapper.selectList(queryWrapper);
        for(Buy buy:records){
          String typeName="";

              if(buy.getType()==1){
                  typeName="进货单";
              }else {
                  if(buy.getType()==2){
                      typeName="购货入库调拨单";
                  }else {
                      typeName="未设定";
                  }
              }

          buy.setTypeName(typeName);
            String BuyTypeName="";
                switch (buy.getBuyType()){
                    case 0:
                        BuyTypeName="删除";
                        break;
                    case 1:
                        BuyTypeName="已进货";
                        break;
                    case 2:
                        BuyTypeName="已支付";
                        break;
                    case 3:
                        BuyTypeName="中心库房已入库";
                        break;
                    default:
                        BuyTypeName="未设定";
                }

            buy.setBuyTypeName(BuyTypeName);

            CentralStation centralStation = centralStationMapper.selectById(buy.getGoodId());
            buy.setGoodName(centralStation.getGoodName());
            buy.setWaitAllo(centralStation.getWaitAllo());
            buy.setWithdrawal(centralStation.getWithdrawal());
            FirstCategory firstCategory = firstCategoryMapper.selectById(centralStation.getGoodClassId());
            buy.setGoodClass(firstCategory.getFName());
            buyMapper.updateById(buy);
        }
        PageInfo pageInfo = new PageInfo(records);
        return pageInfo;
    }
    @Override
    public List<Buy> getListByDateSupplyBuyType(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        String supplyName=(String)map.get("supplyName");
        System.out.println("后端"+map);

        ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
        // 格式化中国时区时间为指定格式的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startDate = LocalDateTime.parse(String.valueOf(map.get("startTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
        String endDate = LocalDateTime.parse(String.valueOf(map.get("endTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
        QueryWrapper<Buy> queryWrapper = new QueryWrapper<>();
        System.out.println(map.get("buyType"));
        System.out.println(map.get("buyType").equals("全部"));
        if(!(map.get("buyType").equals("全部"))){
            if(map.get("buyType").equals("已支付"))
            {queryWrapper.eq("buy_type",2);}
            else if(map.get("buyType").equals("未支付")){
                queryWrapper.ne("buy_type",2);
            }
        }

        queryWrapper.between("date", startDate, endDate);
        if(!supplyName.isBlank()){
            queryWrapper.eq("supply",supplyName);
        }

        List<Buy> records= buyMapper.selectList(queryWrapper);
        return records;
    }

    @Override
    public int deleteBuyByIds(Map<String, Object> map) throws ParseException {
//        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
//                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        Integer goodId=Integer.valueOf(String.valueOf(map.get("good_id")));
        Integer orderId=Integer.valueOf(String.valueOf(map.get("order_id")));
        Integer buyType=Integer.valueOf(String.valueOf(map.get("buy_type")));
        QueryWrapper<Buy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("good_id",goodId).eq("order_id",orderId).eq("buy_type",buyType);
        return buyMapper.delete(queryWrapper);
    }

    @Override
    public int updateBuyByIds(Map<String, Object> map) throws ParseException {
        int flag=1;
        Integer goodId=Integer.valueOf(String.valueOf(map.get("good_id")));
        Integer orderId=Integer.valueOf(String.valueOf(map.get("order_id")));
        Long number=Long.valueOf(String.valueOf(map.get("number")));
        QueryWrapper<Buy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("good_id",goodId)
                .eq("order_id",orderId);
        List<Buy> records = buyMapper.selectList(queryWrapper);
        for (Buy buy:records){
            buy.setNumber(number);
           flag=flag* buyMapper.updateById(buy);
        }
        return flag;
    }

    @Override
    public int changeBuyTypeNotify(Map<String, Object> map) throws ParseException {
        ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
        // 格式化中国时区时间为指定格式的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String startDate = LocalDateTime.parse(String.valueOf(map.get("startTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
        String endDate = LocalDateTime.parse(String.valueOf(map.get("endTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);

        String supplyName=(String)map.get("supplyName");
        Integer goodId=Integer.valueOf(String.valueOf(map.get("good_id")));
        QueryWrapper<Buy> queryWrapper = new QueryWrapper<>();
        System.out.println(map.get("buyType"));

        queryWrapper.between("date", startDate, endDate)
            .eq("supply",supplyName)
            .eq("good_id",goodId);

        Buy buy = new Buy();
        buy.setBuyType((byte) Integer.parseInt(String.valueOf(map.get("buyType"))));
        int res=buyMapper.update(buy,queryWrapper);
        return res;
    }

    @Override
    public List<Buy> getList(Map<String, Object> map) throws ParseException {
        return buyMapper.selectList(null);
    }

    @Override
    public String withdrawal(Map<String, Object> map) throws ParseException {
        System.out.println(map);
        int flag=0;
        String result="";
        for (Map.Entry<String,Object> entry : map.entrySet()) {
            if(!Objects.equals(String.valueOf(entry.getValue()), "") &&String.valueOf(entry.getValue())!=null){
                Buy buy = buyMapper.selectById(Long.valueOf(entry.getKey()));
                CentralStation centralStation=centralStationMapper.selectById(buy.getGoodId());
                Long number =Long.valueOf(String.valueOf(entry.getValue()));
                if(number<=centralStation.getWaitAllo()){
                    StationInOut stationInOut = new StationInOut();
                    stationInOut.setGoodId(centralStation.getId());
                    stationInOut.setGoodName(centralStation.getGoodName());
                    stationInOut.setGoodPrice(centralStation.getGoodPrice());
                    stationInOut.setDate(new Date());
                    stationInOut.setNumber(number);
                    stationInOut.setGoodUnit(centralStation.getGoodUnit());
                    stationInOut.setStationClass(1);
                    stationInOut.setType("中心退货");
                    stationInOutMapper.insert(stationInOut);
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

//    @Override
//    public String withdrawalConfirm(Map<String, Object> map) throws ParseException {
//        int flag=1;
//        String result="";
//        for (Map.Entry<String,Object> entry : map.entrySet()) {
//            if(!Objects.equals(String.valueOf(entry.getValue()), "") &&String.valueOf(entry.getValue())!=null){
//                StationInOut stationInOut = stationInOutService.getById(Long.valueOf(entry.getKey()));
//                CentralStation centralStation=centralStationMapper.selectById(stationInOut.getGoodId());
//                Long number =Long.valueOf(String.valueOf(entry.getValue()));
//                if(number<=centralStation.getWaitAllo()){
//                    stationInOut.setType("中心已退回供应商");
//                    centralStation.setWithdrawal(centralStation.getWithdrawal()+number);
//                    centralStation.setWaitAllo(centralStation.getWaitAllo()-number);
//                    centralStationMapper.updateById(centralStation);
//                    stationInOutService.updateById(stationInOut);
//                }else {
//                    flag=0;
//                }
//            }
//        }
//        if(flag==0){
//            result="Fail";
//        }
//        else{
//            result="Success";
//        }
//        return result;
//    }
}
