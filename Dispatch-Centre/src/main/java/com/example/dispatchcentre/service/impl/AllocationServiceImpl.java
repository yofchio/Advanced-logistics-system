package com.example.dispatchcentre.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dispatchcentre.beans.HttpResponseEntity;
import com.example.dispatchcentre.common.utils.DateUtil;
import com.example.dispatchcentre.entity.Allocation;
import com.example.dispatchcentre.entity.Good;
import com.example.dispatchcentre.entity.Orders;
import com.example.dispatchcentre.entity.Station;
import com.example.dispatchcentre.feign.Task;
import com.example.dispatchcentre.feign.FeignApi;
import com.example.dispatchcentre.mapper.AllocationMapper;
import com.example.dispatchcentre.service.AllocationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品调拨 服务实现类
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-25
 */
@Service
public class AllocationServiceImpl extends ServiceImpl<AllocationMapper, Allocation> implements
    AllocationService {
  @Autowired
  private AllocationMapper allocationMapper;
  @Autowired
  private TaskServiceImpl taskService;
  @Autowired
  private FeignApi feignApi;
  @Override
  public int insertTaskDispatch(Map<String,Object> map) throws ParseException {
    Long taskID= taskService.insert(map);
   Allocation allocation = new Allocation();
   allocation.setTaskId(taskID);
   allocation.setOrderId(Long.valueOf(String.valueOf(map.get("orderId"))));
   allocation.setInStationId(1L);
   allocation.setOutStationId(Long.valueOf(map.get("outStationId").toString()));
   allocation.setAlloType(Byte.valueOf(String.valueOf(map.get("alloType"))));
    HttpResponseEntity res= feignApi.getById(String.valueOf(map.get("inStationId")));
    String jsonString2 = JSON.toJSONString(res.getData());  // 将对象转换成json格式数据
    Station stationin = JSON.parseObject(jsonString2, Station.class);
    allocation.setInStationName(stationin.getName());
    HttpResponseEntity res2= feignApi.getById(String.valueOf(map.get("outStationId")));
    String jsonString3 = JSON.toJSONString(res2.getData());  // 将对象转换成json格式数据
    Station stationout = JSON.parseObject(jsonString3, Station.class);
    allocation.setInStationName(stationout.getName());
   int res3=allocationMapper.insert(allocation);
    return res3;
  }
  @Override
  public int insertTaskDispatchlist(Map<String,Object> map) throws ParseException {

    System.out.println("insertTaskDispatchlist");
    String jsonString1 = JSON.toJSONString(map);  // 将对象转换成json格式数据
    JSONObject jsonObject = JSON.parseObject(jsonString1); // 在转回去
    List<Orders> order = JSON.parseArray(jsonObject.getString("Orders"), Orders.class); // 这样就可以了
    ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
    // 格式化中国时区时间为指定格式的字符串
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    for (Orders order1:order) {
      Map<String,Object> newMap=new HashMap<>();
      newMap.put("creator",map.get("creator"));
      newMap.put("orderId",order1.getId());
      newMap.put("substation",order1.getSubstation());
      newMap.put("deadline",map.get("deadline"));
      newMap.put("substationId",order1.getSubstationId());
      Long taskID = taskService.insert(newMap);
      Allocation allocation = new Allocation();
      allocation.setTaskId(taskID);
      allocation.setOrderId(order1.getId());
      allocation.setInStationId(order1.getSubstationId());
      allocation.setOutStationId(1L);
      allocation.setAlloType((byte) 1);//未完成中心出库
     if(order1.getOrderType().equals("退货"))
     {
       allocation.setOutStationId(order1.getSubstationId());
       allocation.setInStationId(1L);
       allocation.setAlloType((byte) 4);//未完成中心出库
     }
      String deadline = LocalDateTime.parse(String.valueOf(map.get("allocationDate")),
          DateTimeFormatter.ISO_DATE_TIME).atZone(
          ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
      Date allodate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(deadline);
      allocation.setAllocationDate(allodate);

      Date date = DateUtil.getCreateTime();
      allocation.setCreatDate(date);//创建时间

      HttpResponseEntity res = feignApi.getById(String.valueOf(order1.getSubstationId()));
      String jsonString2 = JSON.toJSONString(res.getData());  // 将对象转换成json格式数据
      Station stationin = JSON.parseObject(jsonString2, Station.class);
      allocation.setInStationName(stationin.getName());
//      HttpResponseEntity res2 = feignApi.getById(String.valueOf(map.get("outStationId")));
//      String jsonString3 = JSON.toJSONString(res2.getData());  // 将对象转换成json格式数据
//      Station stationout = JSON.parseObject(jsonString3, Station.class);
      allocation.setOutStationName("中心库房");
      Map<String,Object> orderMap=new HashMap<>();
      orderMap.put("id",order1.getId());
      orderMap.put("orderStatus","已调度");
      HttpResponseEntity res7= feignApi.changeOrderStatusById(orderMap);
      System.out.println(res7);
      if(order1.getOrderType().equals("退货"))
        continue;
      int res3 = allocationMapper.insert(allocation);
      if (res3 == 0) {
        return res3;
      }
    }
    return 1;
  }
 //退货的调度
  @Override
  public int insertWithDrawDispatch(Map<String,Object> map) throws ParseException {
    Allocation allocation=new Allocation();
    ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
    // 格式化中国时区时间为指定格式的字符串
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String datetime = LocalDateTime.parse(String.valueOf(map.get("allocationDate")),
        DateTimeFormatter.ISO_DATE_TIME).atZone(
        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
    Date allodate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetime);
    allocation.setAllocationDate(allodate);//日期
    Task task=taskService.selectbyId(Long.valueOf(String.valueOf(map.get("taskId"))));
    allocation.setTaskId(task.getId());
    allocation.setOrderId(task.getOrderId());
    allocation.setOutStationId(task.getSubstationId());
    allocation.setOutStationName(task.getSubstation());
    allocation.setInStationName("中心库房");
    allocation.setInStationId(1L);
    allocation.setAlloType((byte) 5);
    int res= allocationMapper.insert(allocation);
    return res;
  }

  @Override
  public int insertSationDispatch(Allocation allocation) {
    int res=allocationMapper.insert(allocation);
    return res;
  }
/*
  根据id来更改type
 */
  @Override
  public int updatebyId(Map<String, Object> map) {
    UpdateWrapper<Allocation> updateWrapper = new UpdateWrapper<>();
    updateWrapper.eq("id",map.get("id")).set("allo_type", map.get("allo_type"));
    Integer rows = allocationMapper.update(null, updateWrapper);
    return rows;
  }

   public int updatePeobyId(Allocation allocation) {

    return allocationMapper.updateById(allocation);
  }
  @Override
  public Allocation selectbyId(Map<String,Object> map) {
    QueryWrapper<Allocation> queryWrapper = new QueryWrapper<>();

    int type=Integer.valueOf(String.valueOf(map.get("alloType")));
    Long id=Long.valueOf(String.valueOf(map.get("id")));
    System.out.println(map);
    queryWrapper.eq("allo_type",type);
    queryWrapper.eq("id",id);
    Allocation allocation=allocationMapper.selectOne(queryWrapper);
    return allocation;
  }

  @Override
  public PageInfo selectAll(Map<String, Object> map) {
    return null;
  }

  @Override
  public PageInfo searchbykey(Map<String, Object> map) {
    PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
        Integer.valueOf(String.valueOf(map.get("pageSize"))));
    QueryWrapper<Allocation> queryWrapper = new QueryWrapper<>();
    int type=Integer.valueOf(String.valueOf(map.get("alloType")));
    ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
    // 格式化中国时区时间为指定格式的字符串
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String startDate = LocalDateTime.parse(String.valueOf(map.get("startTime")),
        DateTimeFormatter.ISO_DATE_TIME).atZone(
        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
    String endDate = LocalDateTime.parse(String.valueOf(map.get("endTime")),
        DateTimeFormatter.ISO_DATE_TIME).atZone(
        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
    if(!map.get("id").equals("")) {
      Long id = Long.valueOf(String.valueOf(map.get("id")));
      queryWrapper.eq("id", id);
    }
    queryWrapper.between("allocation_date", startDate, endDate);
    queryWrapper.eq("allo_type",type);
    List<Allocation> res= allocationMapper.selectList(queryWrapper);
//    System.out.println(res);
    PageInfo pageInfo = new PageInfo(res);
    return pageInfo;
  }
  @Override
  public PageInfo getGoodListByAlloId(Map<String, Object> map) {
    PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
        Integer.valueOf(String.valueOf(map.get("pageSize"))));
    QueryWrapper<Allocation> queryWrapper = new QueryWrapper<>();
    System.out.println(map);
    if(!map.get("id").equals("")) {
      Long id = Long.valueOf(String.valueOf(map.get("id")));
      queryWrapper.eq("id", id);
    }
    if(!map.get("alloType").equals("")) {
      int type=Integer.valueOf(String.valueOf(map.get("alloType")));
      queryWrapper.eq("allo_type", type);
    }
    Allocation allocation=allocationMapper.selectOne(queryWrapper);
    HttpResponseEntity res= feignApi.getGoodByOrderId(allocation.getOrderId());
    String jsonString2 = JSON.toJSONString(res.getData());  // 将对象转换成json格式数据
    List<Good> goodList = JSON.parseArray(jsonString2,Good.class);
    PageInfo pageInfo = new PageInfo(goodList);
    return pageInfo;
  }
//  @Override
//  public PageInfo getGoodListByAlloId(Map<String, Object> map) {
//    PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
//        Integer.valueOf(String.valueOf(map.get("pageSize"))));
//    QueryWrapper<Allocation> queryWrapper = new QueryWrapper<>();
//    int type=Integer.valueOf(String.valueOf(map.get("alloType")));
//
//    System.out.println(map);
//    queryWrapper.eq("allo_type",type);
//    if(!map.get("id").equals(0)) {
//      Long id = Long.valueOf(String.valueOf(map.get("id")));
//      queryWrapper.eq("id", id);
//    }
//    ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
//    // 格式化中国时区时间为指定格式的字符串
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    String startDate = LocalDateTime.parse(String.valueOf(map.get("startLine")),
//        DateTimeFormatter.ISO_DATE_TIME).atZone(
//        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
//    String endDate = LocalDateTime.parse(String.valueOf(map.get("endLine")), DateTimeFormatter.ISO_DATE_TIME).atZone(
//        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
//    queryWrapper.between("allocation_date", startDate, endDate);
//    Allocation allocation=allocationMapper.selectOne(queryWrapper);
//    HttpResponseEntity res= feignApi.getGoodByOrderId(allocation.getOrderId());
//    String jsonString2 = JSON.toJSONString(res.getData());  // 将对象转换成json格式数据
//    List<Good> goodList = JSON.parseArray(jsonString2,Good.class);
//    PageInfo pageInfo = new PageInfo(goodList);
//    return pageInfo;
//  }
}
