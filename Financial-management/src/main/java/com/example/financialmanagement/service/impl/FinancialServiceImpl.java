package com.example.financialmanagement.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.financialmanagement.beans.HttpResponseEntity;
import com.example.financialmanagement.common.utils.UUIDUtil;
import com.example.financialmanagement.entity.Buy;
import com.example.financialmanagement.entity.CentralStation;
import com.example.financialmanagement.entity.Good;
import com.example.financialmanagement.entity.Inoutstation;
import com.example.financialmanagement.entity.Orders;
import com.example.financialmanagement.entity.form.MoneyStation;
import com.example.financialmanagement.entity.form.MoneySupply;
import com.example.financialmanagement.entity.vo.ResultStation;
import com.example.financialmanagement.entity.vo.ResultSupply;
import com.example.financialmanagement.feign.FeignApi;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 进货单 服务实现类
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-20
 */
@Service
public class FinancialServiceImpl {

   @Autowired
   private FeignApi feignApi;
   @Autowired
   private RedisTemplate redisTemplate;

   private Double sumMoney=0.0;
  public ResultStation settlementStation(Map<String, Object> map)  {
    PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
        Integer.valueOf(String.valueOf(map.get("pageSize"))));
    System.out.println(map);
    HttpResponseEntity res= feignApi.getOrderByStationFin(map);
    String jsonString1 = JSON.toJSONString(res.getData());  // 将对象转换成json格式数据
//    JSONObject jsonObject = JSON.parseObject(jsonString1); // 在转回去
//    System.out.println("test");
    List<Orders> orderList=JSON.parseArray(jsonString1, Orders.class);
    System.out.println(orderList);
    Map<String, Map<String, Object>> statistics = new HashMap<>();
    Double getMoney=0.0;
    Double returnMoney=0.0;
    for (Orders order : orderList) {

      HttpResponseEntity resgood= feignApi.getGoodByOrderId(Math.toIntExact(order.getId()));
      /*查询并统计结果*/
      String jsonString2 = JSON.toJSONString(resgood.getData());  // 将对象转换成json格式数据
//    JSONObject jsonObject = JSON.parseObject(jsonString1); // 在转回去
//    System.out.println("test");
      List<Good> goodsList = JSON.parseArray(jsonString2,Good.class);
      if(goodsList==null)
        continue;
      for (Good goods : goodsList) {
        String category = goods.getGoodSubclass();
        String name = goods.getGoodName();
        Long id = goods.getId();
        double price = goods.getGoodPrice();
        Long quantity = goods.getGoodNumber();

        String category_name = category + "_" + name+"_"+id;
        /*  结果是否包含*/
        if (!statistics.containsKey(category_name)) {
          statistics.put(category_name, new HashMap<>());
        }

        Map<String, Object> categoryStats = statistics.get(category_name);

        switch (order.getOrderType()) {
          case "新订":
            categoryStats.put("新订(数量)",
                Integer.parseInt(String.valueOf(categoryStats.getOrDefault("新订(数量)",
                    0))) + quantity);
            categoryStats.put("新订(金额)",
                Double.parseDouble(String.valueOf(categoryStats.getOrDefault("新订(金额)",
                    0.0))) + price * quantity);
            getMoney+=price * quantity;
            break;
          case "退货":
            categoryStats.put("退货(数量)",
                Integer.parseInt(String.valueOf(categoryStats.getOrDefault("退货(数量)",
                    0))) + quantity);
            categoryStats.put("退货(金额)",
                Double.parseDouble(String.valueOf(categoryStats.getOrDefault("退货(金额)",
                    0.0))) + price * quantity);
            returnMoney+=price * quantity;
            break;
        }
      }
    }
    List<MoneyStation> reslist=getMoneyStationMap(statistics);
    PageInfo<MoneyStation> resinfo=new PageInfo<>(reslist);
    ResultStation resultStation =new ResultStation();
    resultStation.setPageInfo(resinfo);
    resultStation.setGetMoney(getMoney);
    resultStation.setReturnMoney(returnMoney);
    resultStation.setSumMoney(getMoney-returnMoney);
    return resultStation;
  }

  public List<MoneyStation> getMoneyStationMap(Map<String, Map<String, Object>> map){
    List<MoneyStation> moneyStationList =new ArrayList<>();

    for(Map.Entry<String, Map<String, Object>> entry : map.entrySet()){
      MoneyStation moneyStation =new MoneyStation();
      String goodClass= entry.getKey().split("_")[0];
      String goodName= entry.getKey().split("_")[1];
      String id= entry.getKey().split("_")[2];
      moneyStation.setGoodName(goodName);
      moneyStation.setGoodClass(goodClass);
      moneyStation.setId(id);
      Map<String, Object> message=entry.getValue();
      moneyStation.setGoodGetNumber(Integer.parseInt(String.valueOf(message.getOrDefault("新订(数量)",0))));
      moneyStation.setGoodGetMoney(Double.parseDouble(String.valueOf(message.getOrDefault("新订(金额)"
          ,0.0))));

      moneyStation.setGoodReturnNumber(Integer.parseInt(String.valueOf(message.getOrDefault("退货(数量)",0))));
      moneyStation.setGoodReturnMoney(Double.parseDouble(String.valueOf(message.getOrDefault("退货(金额)",
          0.0))));
      moneyStationList.add(moneyStation);
    }
    return moneyStationList;
  }

  public ResultSupply settlementSupply(Map<String, Object> map)  {
    PageHelper.startPage(Integer.parseInt(String.valueOf(map.get("pageNum"))),
        Integer.parseInt(String.valueOf(map.get("pageSize"))));
    //0是删除，1是已经进货，2是已经支付（已操作），3是已经操作对应中心库房入库
    System.out.println(map);
    HttpResponseEntity res= feignApi.getBuyBySupplyFin(map);
    String jsonString1 = JSON.toJSONString(res.getData());  // 将对象转换成json格式数据
//    JSONObject jsonObject = JSON.parseObject(jsonString1); // 在转回去
//    System.out.println("test");
    List<Buy> buyList=JSON.parseArray(jsonString1, Buy.class);

    Map<String, Map<String, Object>> statistics = new HashMap<>();
    Map<Long, Long> good_id_map = new HashMap<>();
    for (Buy buy : buyList) {
      //供货量的
      Long id = buy.getGoodId();
      String nameBuy = buy.getGoodName();
      String ca=nameBuy+"_"+id;
      if (!statistics.containsKey(ca)) {
        statistics.put(ca, new HashMap<>());
      }
      Map<String, Object> categoryStatsBuy = statistics.get(ca);

      categoryStatsBuy.put("供货数量",
          Integer.parseInt(String.valueOf(categoryStatsBuy.getOrDefault("供货数量", 0) ))+ buy.getNumber());
      //0是删除，1是已经进货，2是已经支付（已操作），3是已经操作对应中心库房入库
      if(buy.getBuyType()==2){
        categoryStatsBuy.put("支付状态", "已支付");
      }else
      {
        categoryStatsBuy.put("支付状态", "未支付");
      }
      //从stock中查原价
      HttpResponseEntity res2= feignApi.getStockByGoodId(String.valueOf(buy.getGoodId()));
      String jsonString2 = JSON.toJSONString(res2.getData());  // 将对象转换成json格式数据
//    JSONObject jsonObject = JSON.parseObject(jsonString1); // 在转回去
//    System.out.println("test");
      CentralStation centralStation =JSON.parseObject(jsonString2, CentralStation.class);
      if (!categoryStatsBuy.containsKey("原价")) {
        categoryStatsBuy.put("原价",
            Double.parseDouble(String.valueOf(categoryStatsBuy.getOrDefault("原价", 0) ))+ centralStation.getGoodPrice());
      }
      if (!categoryStatsBuy.containsKey("进货价")) {
        categoryStatsBuy.put("进货价",
            Double.parseDouble(String.valueOf(categoryStatsBuy.getOrDefault("进货价", 0) ))+ centralStation.getGoodCost());
      }
      //可能出现进货进多次同一种商品的情况
      if (!good_id_map.containsKey(buy.getGoodId())) {
        good_id_map.put(buy.getGoodId(),buy.getGoodId());
      }else{
        continue;//有了就继续，只加供货量
      }
      //从输入库表单查buyType
      map.put("stationType","1");
      map.put("outType","退货出库");
      map.put("good_id",buy.getGoodId());
      HttpResponseEntity res3= feignApi.getStationByGoodIdDate(map);
      String jsonString3 = JSON.toJSONString(res3.getData());  // 将对象转换成json格式数据

      List<Inoutstation> inoutstationList =JSON.parseArray(jsonString3, Inoutstation.class);

      for (Inoutstation infor : inoutstationList) {
        categoryStatsBuy.put("退回数量",
                Integer.parseInt(String.valueOf(categoryStatsBuy.getOrDefault("退回数量", 0))) + infor.getNumber());
        }
      }

    List<MoneySupply> reslist=getMoneySupplyMap(statistics);
    PageInfo<MoneySupply> resinfo=new PageInfo<>(reslist);
    ResultSupply resultSupply =new ResultSupply();
    resultSupply.setPageInfo(resinfo);
    resultSupply.setSumMoney(this.sumMoney);

    //redis
    redisTemplate.opsForValue().set("startTime", map.get("startTime"));
    redisTemplate.opsForValue().set("endTime", map.get("endTime"));
    redisTemplate.opsForValue().set("supplyName", map.get("supplyName"));
    return resultSupply;
  }

  public List<MoneySupply> getMoneySupplyMap(Map<String, Map<String, Object>> map){
    List<MoneySupply> moneyStationList =new ArrayList<>();

    for(Map.Entry<String, Map<String, Object>> entry : map.entrySet()){
      MoneySupply moneySupply =new MoneySupply();
      String goodName= entry.getKey().split("_")[0];
      moneySupply.setId(entry.getKey().split("_")[1]);
      moneySupply.setGoodName(goodName);
      Map<String, Object> message=entry.getValue();
      moneySupply.setGoodType(String.valueOf(message.get("支付状态")));

      moneySupply.setGoodSupplyNumber(Integer.parseInt(String.valueOf(message.getOrDefault("供货数量",0))));
      moneySupply.setGoodReturnNumber(Integer.parseInt(String.valueOf(message.getOrDefault("退回数量",0))));

      moneySupply.setGoodPrice(Double.parseDouble(String.valueOf(message.getOrDefault("原价",0.0))));
      moneySupply.setGoodCost(Double.parseDouble(String.valueOf(message.getOrDefault("进货价",0.0))));

      moneySupply.setGoodSettleNumber(moneySupply.getGoodSupplyNumber()-moneySupply.getGoodReturnNumber());//结算数量
      Double money=
          (moneySupply.getGoodPrice()-moneySupply.getGoodCost())*(moneySupply.getGoodSettleNumber());//结算金额
      money= (double) Math.round(money* 100) / 100;
      moneySupply.setGoodSettleMoney(money);
      this.sumMoney+=money;
      moneyStationList.add(moneySupply);
    }
    return moneyStationList;
  }

   public int  changeBuyTypeByGoodId(String goodId){
    Map<String,Object>map =new HashMap<>();
    map.put("good_id",goodId);
    map.put("buyType",2);
    map.put("startTime",redisTemplate.opsForValue().get("startTime"));
    map.put("endTime",redisTemplate.opsForValue().get("endTime"));
    map.put("supplyName",redisTemplate.opsForValue().get("supplyName"));
    System.out.println("notify"+map);
    HttpResponseEntity res=  feignApi.changeBuyTypeNotify(map);
     return (int) res.getData();
   }

   public CentralStation getAilipayGood(String name){
     HttpResponseEntity resgood= feignApi.getAliGoodName(name);
     /*查询并统计结果*/
     String jsonString2 = JSON.toJSONString(resgood.getData());  // 将对象转换成json格式数据

     CentralStation centralStation = JSON.parseObject(jsonString2,CentralStation.class);
       return centralStation;
   }
}
