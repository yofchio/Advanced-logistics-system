package com.example.customerservicecentre.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.customerservicecentre.beans.HttpResponseEntity;
import com.example.customerservicecentre.common.utils.DateUtil;
import com.example.customerservicecentre.entity.Customer;
import com.example.customerservicecentre.entity.Good;
import com.example.customerservicecentre.entity.Orders;
import com.example.customerservicecentre.entity.vo.CreaterWork;
import com.example.customerservicecentre.feign.FeignApi;
import com.example.customerservicecentre.mapper.GoodMapper;
import com.example.customerservicecentre.mapper.OrderMapper;
import com.example.customerservicecentre.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-19
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

  @Autowired
  private OrderMapper orderMapper;
  @Autowired
  private FeignApi feignApi;
  @Autowired
  private GoodMapper goodMapper;
  @Autowired
  private CustomerServiceImpl customerService;

  @Override
  public int insert(Map<String,Object > map) {
    String jsonString1 = JSON.toJSONString(map);  // 将对象转换成json格式数据
    JSONObject jsonObject = JSON.parseObject(jsonString1); // 在转回去
    Orders order = JSON.parseObject(jsonObject.getString("Orders"), Orders.class); // 这样就可以了

    List<Good> goods=JSON.parseArray(jsonObject.getString("Goods"), Good.class);
    Date date=new Date();
    order.setOrderDate(date);

    int res = orderMapper.insert(order);//添加order;
    Long orderId= order.getId();
    System.out.println("goodslist"+goods);
    for(Good good:goods){
      good.setKeyId(Math.toIntExact(orderId));
      HttpResponseEntity ss= feignApi.addGoods(good);
      System.out.println("good添加"+ss);
    }
    return res;
  }

  @Override
  public PageInfo getOrdersByCriteria(Map<String, Object> map) throws ParseException {
    PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
        Integer.valueOf(String.valueOf(map.get("pageSize"))));

    QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
    ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
    // 格式化中国时区时间为指定格式的字符串
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String startDate ="2019-07-10 00:00:00";
    String endDate ="2045-07-10 00:00:00";
    if(map.get("startTime")!=""&&map.get("startTime")!=null) {
     startDate = LocalDateTime.parse(String.valueOf(map.get("startTime")),
          DateTimeFormatter.ISO_DATE_TIME).atZone(
          ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
    }
    if(map.get("endTime")!=""&&map.get("endTime")!=null) {
      endDate = LocalDateTime.parse(String.valueOf(map.get("endTime")),
          DateTimeFormatter.ISO_DATE_TIME).atZone(
          ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
    }
    System.out.println(map);
    // 判断name属性是否为空，如果不为空则作为查询条件
    if (!map.get("customerName").equals("")) {
      queryWrapper.like("customer_name", map.get("customerName"));
    }
    if (!map.get("id").equals("")&&!map.get("id").equals(0L)) {
      queryWrapper.like("id", map.get("id"));
    }
    if (!map.get("receiveName").equals("")) {
      queryWrapper.like("receive_name", map.get("receiveName"));
    }

    System.out.println("startDate"+startDate);
      queryWrapper.between("order_date", startDate, endDate);

    if (!map.get("orderType").equals("")) {
      queryWrapper.eq("order_type", map.get("orderType"));
    }
    List<Orders> res= orderMapper.selectList(queryWrapper);
    for (Orders orders:res ) {
      Long id = Long.valueOf(orders.getCustomerId());
      Customer customer= customerService.selectbyId(id);
      orders.setCustomerName(customer.getName());
    }
//    System.out.println(res);
    PageInfo pageInfo = new PageInfo(res);
    return pageInfo;
}

  @Override
  public PageInfo getlack(Map<String, Object> map) throws ParseException {
    PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
        Integer.valueOf(String.valueOf(map.get("pageSize"))));

    QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
    ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
    // 格式化中国时区时间为指定格式的字符串
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String startDate = LocalDateTime.parse(String.valueOf(map.get("startTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
    String endDate = LocalDateTime.parse(String.valueOf(map.get("endTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
    queryWrapper.between("order_date", startDate, endDate);

//    if (!map.get("orderType").equals("")) {
//      queryWrapper.eq("order_type", map.get("orderType"));
//    }

    queryWrapper.eq("order_status", "缺货");

    List<Orders> res= orderMapper.selectList(queryWrapper);
    for (Orders orders:res ) {
      Long id = Long.valueOf(orders.getCustomerId());
      Customer customer= customerService.selectbyId(id);
      orders.setCustomerName(customer.getName());
    }
//    System.out.println(res);
    PageInfo pageInfo = new PageInfo(res);
    return pageInfo;
  }

  @Override
  public int addOrderGood(Map<String, Object> map) {
    String jsonString1 = JSON.toJSONString(map);  // 将对象转换成json格式数据
    JSONObject jsonObject = JSON.parseObject(jsonString1); // 在转回去
    Orders order = JSON.parseObject(jsonObject.getString("Orders"), Orders.class); // 这样就可以了

    List<Good> goods=JSON.parseArray(jsonObject.getString("Goods"), Good.class);

    int res = orderMapper.updateById(order);//添加order;
    Long orderId= order.getId();

    System.out.println("goodslist"+goods);
    for(Good good:goods){
      Date  now= DateUtil.getCreateTime();
      good.setGoodDate(now);
      good.setKeyId(Math.toIntExact(orderId));
      HttpResponseEntity ss= feignApi.addGoods(good);
      System.out.println("good添加"+ss);
    }
    return res;
  }

  @Override
  public PageInfo getOrderDis(Map<String, Object> map) {
    System.out.println(map);
    PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
        Integer.valueOf(String.valueOf(map.get("pageSize"))));
    QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
    ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
    // 格式化中国时区时间为指定格式的字符串
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String startDate = LocalDateTime.parse(String.valueOf(map.get("startTime")),
        DateTimeFormatter.ISO_DATE_TIME).atZone(
        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
    String endDate = LocalDateTime.parse(String.valueOf(map.get("endTime")),
        DateTimeFormatter.ISO_DATE_TIME).atZone(
        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
    queryWrapper.between("order_date", startDate, endDate);
    if (!map.get("orderType").equals("")) {
      queryWrapper.eq("order_type", map.get("orderType"));
    }
    queryWrapper.eq("order_status", "可分配");
    //good_status=="",则为自动调度，good_status=”缺货“，则为手动调度
    queryWrapper.eq("good_status", map.get("goodStatus"));
    List<Orders> res=orderMapper.selectList(queryWrapper);
    Random r=new Random();
    for (Orders orders:res ) {
      if(orders.getSubstationId()==null){
        Long id= Long.valueOf(r.nextInt(3)+1);
        if (id==1L){
          orders.setSubstationId(id);
          orders.setSubstation("长沙分站");
        }else if (id==2L){
          orders.setSubstationId(id);
          orders.setSubstation("沈阳分站");
        }else if (id==3L){
          orders.setSubstationId(id);
          orders.setSubstation("广东分站");
        }
      }
      Long id = Long.valueOf(orders.getCustomerId());
      Customer customer= customerService.selectbyId(id);
      orders.setCustomerName(customer.getName());
    }
    //sada
    PageInfo<Orders> pageInfo = new PageInfo<Orders>(res);
    return pageInfo;
  }


  @Override
  public PageInfo getAllOrder(Map<String, Object> map) {
    System.out.println(map);
    PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
        Integer.valueOf(String.valueOf(map.get("pageSize"))));
    List<Orders> res=orderMapper.selectList(null);
    for (Orders orders:res ) {
       Long id = Long.valueOf(orders.getCustomerId());
       Customer customer= customerService.selectbyId(id);
       orders.setCustomerName(customer.getName());
    }
    //sada
    PageInfo<Orders> pageInfo = new PageInfo<Orders>(res);
    return pageInfo;
  }

  @Override
  public PageInfo selectOrderbyCustomer(Map<String, Object> map) {
    PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
        Integer.valueOf(String.valueOf(map.get("pageSize"))));
    QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();

    // 判断name属性是否为空，如果不为空则作为查询条件
    if (!map.get("customerId").equals("")) {
      queryWrapper.eq("customer_id", map.get("customerId"));
    }
    List<Orders> res=orderMapper.selectList(queryWrapper);
    for (Orders orders:res ) {
      Long id = Long.valueOf(orders.getCustomerId());
      Customer customer= customerService.selectbyId(id);
      orders.setCustomerName(customer.getName());
    }
    PageInfo<Orders> pageInfo = new PageInfo<>(res);
    return pageInfo;
  }

  @Override
  public int updatebyId(Orders orders) throws ParseException {
//    ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
//    // 格式化中国时区时间为指定格式的字符串
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    String startDate =
//        LocalDateTime.parse(String.valueOf(orders.getDeliveryDateFront()), DateTimeFormatter.ISO_DATE_TIME).atZone(
//        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
//    Date dedate= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startDate);
//    orders.setDeliveryDate(dedate);
    System.out.println("update+order"+orders);
    int res =orderMapper.updateById(orders);
    return res;
  }

  @Override
  public PageInfo getWorkByid(Map<String, Object> map) throws ParseException {
    PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
        Integer.valueOf(String.valueOf(map.get("pageSize"))));

    QueryWrapper<Orders> orderWrapper = new QueryWrapper<>();
    ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
    // 格式化中国时区时间为指定格式的字符串
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String startDate = LocalDateTime.parse(String.valueOf(map.get("startTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
    String endDate = LocalDateTime.parse(String.valueOf(map.get("endTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);


    // 判断name属性是否为空，如果不为空则作为查询条件
    if (!map.get("creater").equals("")) {
      orderWrapper.eq("creater", map.get("creater"));
    }

    /* 根据订单表查询结果获取对应的order_id列表*/
    orderWrapper.between("order_date", startDate, endDate);
    List<Orders> orderList = orderMapper.selectList(orderWrapper);
//    List<Integer> orderIdList = new ArrayList<>();
    /*    结果表*/
    Map<String, Map<String, Object>> statistics = new HashMap<>();

    for (Orders order : orderList) {
//      orderIdList.add(Math.toIntExact(order.getId()));
//    }
      /*    构建商品表查询条件*/
      QueryWrapper<Good> goodsWrapper = new QueryWrapper<>();
      goodsWrapper.eq("key_id", Math.toIntExact(order.getId()));
      /*查询并统计结果*/
      List<Good> goodsList = goodMapper.selectList(goodsWrapper);

      for (Good goods : goodsList) {
        String category = goods.getGoodSubclass();
        String name = goods.getGoodName();
        double price = goods.getGoodPrice();
        Long quantity = goods.getGoodNumber();

        String category_name = category + "_" + name;
        /*  结果是否包含*/
        if (!statistics.containsKey(category_name)) {
          statistics.put(category_name, new HashMap<>());
        }

        Map<String, Object> categoryStats = statistics.get(category_name);
        if (!categoryStats.containsKey("原价")) {
          categoryStats.put("原价",
              Integer.parseInt(String.valueOf(categoryStats.getOrDefault("原价", 0) ))+ goods.getGoodCost());
        }

//        if (!categoryStats.containsKey(name)) {
//          categoryStats.put(name, new HashMap<>());
//        }
//
//        Map<String, Object> productStats = (Map<String, Object>) categoryStats.get(name);

        switch (order.getOrderType()) {
          case "新订":
            categoryStats.put("新订(笔数)",
                Integer.parseInt(String.valueOf(categoryStats.getOrDefault("新订(笔数)", 0) ))+ 1);
            categoryStats.put("新订(数量)",
                Integer.parseInt(String.valueOf(categoryStats.getOrDefault("新订(数量)", 0))) + quantity);
            categoryStats.put("新订(金额)",
                Double.parseDouble(String.valueOf(categoryStats.getOrDefault("新订(金额)", 0.0))) + price * quantity);
            break;
          case "退货":
            categoryStats.put("退货(笔数)",
                Integer.parseInt(String.valueOf(categoryStats.getOrDefault("退货(笔数)", 0))) + 1);
            categoryStats.put("退货(数量)",
                Integer.parseInt(String.valueOf(categoryStats.getOrDefault("退货(数量)", 0))) + quantity);
            categoryStats.put("退货(金额)",
                Double.parseDouble(String.valueOf(categoryStats.getOrDefault("退货(金额)", 0.0))) + price * quantity);
            break;
          case "换货":
            categoryStats.put("换货(笔数)",
                Integer.parseInt(String.valueOf(categoryStats.getOrDefault("换货(笔数)", 0))) + 1);
            categoryStats.put("换货(数量)",
                Integer.parseInt(String.valueOf(categoryStats.getOrDefault("换货(数量)", 0))) + quantity);
            categoryStats.put("换货(金额)",
                Double.parseDouble(String.valueOf(categoryStats.getOrDefault("换货(金额)", 0.0))) + price * quantity);

            break;
          case "退订":
            categoryStats.put("退订(笔数)",
                Integer.parseInt(String.valueOf(categoryStats.getOrDefault("退订(笔数)", 0))) + 1);
            categoryStats.put("退订(数量)",
                Integer.parseInt(String.valueOf(categoryStats.getOrDefault("退订(数量)", 0))) + quantity);
            categoryStats.put("退订(金额)",
                Double.parseDouble(String.valueOf(categoryStats.getOrDefault("退订(金额)", 0.0))) + price * quantity);
            break;
        }
      }
    }
    List<CreaterWork> res=getCreaterWorkMap(statistics, String.valueOf(map.get("creater")));
    PageInfo<CreaterWork> resinfo=new PageInfo<>(res);
    return resinfo;
  }

  @Override
  public Orders getOrderByid(Long id) {

    return orderMapper.selectById(id);
  }

  @Override
  public int checkUnsub(Orders orders) {
    Orders res= orderMapper.selectById(orders.getId());
    if(res.getOrderStatus().equals("缺货")||res.getOrderStatus().equals("可分配")){
      return 1;
    }
    return 0;
  }

  @Override
  public int addUnsub(Map<String,Object > map) {
    String jsonString1 = JSON.toJSONString(map);  // 将对象转换成json格式数据
    JSONObject jsonObject = JSON.parseObject(jsonString1); // 在转回去
    Orders order = JSON.parseObject(jsonObject.getString("Orders"), Orders.class); // 这样就可以了

    List<Good> goods=JSON.parseArray(jsonObject.getString("Goods"), Good.class);  //现在的
    Date date = DateUtil.getCreateTime();
    order.setReDate(date);//退订日期
    order.setId(null);
    int res = orderMapper.insert(order);//添加order;
    Long orderId= order.getId();
    Long or_orderId= order.getOrNumber();
    Orders OrOrders=orderMapper.selectById(or_orderId);
    OrOrders.setGoodSum(OrOrders.getGoodSum()-order.getGoodSum());//更新总数量;
    int res1=orderMapper.updateById(OrOrders);

    System.out.println(goods);//先通过id更新
    //先通过id更新原始订单表的东西
    for(Good good:goods){
      System.out.println("good.getId()"+good.getId());
      HttpResponseEntity res2 = feignApi.getGoodByid(good.getId());
      String jsonString3 = JSON.toJSONString(res2.getData());  // 将对象转换成json格式数据
      Good goodOr = JSON.parseObject(jsonString3, Good.class);
      Long number=  goodOr.getGoodNumber()-good.getChangeNumber();//good剩余多少;
      if(number==0L)//退订商品数量为原来的商品数量
      {
        HttpResponseEntity delete = feignApi.deleteGoodByid(String.valueOf(goodOr.getId()));
        good.setKeyId(Math.toIntExact(orderId));
        good.setId(null);
        HttpResponseEntity addGoodhttp= feignApi.addGoods(good);//新good，更新了数量
//        Map<String,Object> goodmap=new HashMap<>();
//        goodmap.put("good_id",good.getId());
//        goodmap.put("order_id",or_orderId);
//        goodmap.put("buy_type",0);
//        HttpResponseEntity deletebuy= feignApi.deleteBuyByGoodid(goodmap);//删除对应的buy
      }
      else
      {
        goodOr.setGoodNumber(number);
        HttpResponseEntity updatehttp= feignApi.updateGoodByid(goodOr);//新good，更新了数量
        good.setKeyId(Math.toIntExact(orderId));
        good.setGoodNumber(good.getChangeNumber());
        good.setId(null);
        HttpResponseEntity addGoodhttp= feignApi.addGoods(good);//新good，更新了数量
//        Map<String,Object > goodmap=new HashMap<>();
//        goodmap.put("good_id",goodOr.getGoodId());
//        goodmap.put("order_id",or_orderId);
//        goodmap.put("number",number);
//        HttpResponseEntity updateBuy= feignApi.updateBuyByid(goodmap);//新good，更新了数量
      }
    }
    return res;
  }

  @Override
  public int checkReturn(Orders orders) {
    Orders res= orderMapper.selectById(orders.getId());
    if(res.getOrderStatus().equals("完成")){
      return 1;
    }
    return 0;
  }

  @Override
  public int addReturn(Map<String,Object > map) {
    String jsonString1 = JSON.toJSONString(map);  // 将对象转换成json格式数据
    JSONObject jsonObject = JSON.parseObject(jsonString1); // 在转回去
    Orders order = JSON.parseObject(jsonObject.getString("Orders"), Orders.class); // 这样就可以了

    List<Good> goods=JSON.parseArray(jsonObject.getString("Goods"), Good.class);
    Date date = DateUtil.getCreateTime();
    order.setOrderDate(date);
    order.setId(null);
    int res = orderMapper.insert(order);//添加order;
    Long orderId= order.getId();
//    Long or_orderId= order.getOrNumber();
//    Orders OrOrders=orderMapper.selectById(or_orderId);
//    OrOrders.setGoodSum(OrOrders.getGoodSum()-order.getGoodSum());//更新总数量;
//    int res1=orderMapper.updateById(OrOrders);
    System.out.println(goods);
    for(Good good:goods){
      good.setId(null);
      good.setGoodNumber(good.getChangeNumber());
      good.setKeyId(Math.toIntExact(orderId));
      HttpResponseEntity ss= feignApi.addGoods(good);
      System.out.println(ss);
    }
    return res;
  }

  @Override
  public List<Orders> getOrderByStationFin(Map<String, Object> map) throws ParseException {

    QueryWrapper<Orders> orderWrapper = new QueryWrapper<>();
    ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
    // 格式化中国时区时间为指定格式的字符串
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String startDate = LocalDateTime.parse(String.valueOf(map.get("startTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
    String endDate = LocalDateTime.parse(String.valueOf(map.get("endTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);

    orderWrapper.and(i -> i.or().eq("order_type", "新订")
            .or().eq("order_type", "退货"));
    orderWrapper.eq("order_status","已完成");
    orderWrapper.eq("substation",map.get("station"));


    /* 根据订单表查询结果获取对应的order_id列表*/
    orderWrapper.and(i -> i.between("order_date", startDate, endDate).or().between(
        "re_date", startDate, endDate));
    List<Orders> orderList = orderMapper.selectList(orderWrapper);

    return orderList;
  }

  public List<CreaterWork> getCreaterWorkMap(Map<String, Map<String, Object>> map,String creater){
    List<CreaterWork> createrWorkList=new ArrayList<>();
    for(Map.Entry<String, Map<String, Object>> entry : map.entrySet()){
      CreaterWork createrWork=new CreaterWork();
      String goodClass= entry.getKey().split("_")[0];
      String goodName= entry.getKey().split("_")[1];
      createrWork.setCreater(creater);
      createrWork.setGoodName(goodName);
      createrWork.setGoodSubclass(goodClass);

      Map<String, Object> message=entry.getValue();
      createrWork.setNewOrderNum(Integer.parseInt(String.valueOf(message.getOrDefault("新订(笔数)",0))));
      createrWork.setNewGoodNum(Integer.parseInt(String.valueOf(message.getOrDefault("新订(数量)",0))));
      createrWork.setNewOrderMoney(Double.parseDouble(String.valueOf(message.getOrDefault("新订(金额)"
          ,0.0))));

      createrWork.setReturnOrderNum(Integer.parseInt(String.valueOf(message.getOrDefault("退货(笔数)"
          ,0))));
      createrWork.setReturnGoodNum(Integer.parseInt(String.valueOf(message.getOrDefault("退货(数量)",0))));
      createrWork.setReturnOrderMoney(Double.parseDouble(String.valueOf(message.getOrDefault("退货(金额)",0.0))));

      createrWork.setChangeOrderNum(Integer.parseInt(String.valueOf(message.getOrDefault("换货(笔数)"
          ,0))));
      createrWork.setChangeGoodNum(Integer.parseInt(String.valueOf(message.getOrDefault("换货(数量)",0))));
      createrWork.setChangeOrderMoney(Double.parseDouble(String.valueOf(message.getOrDefault("换货(金额)",0.0))));

      createrWork.setUnsubOrderNum(Integer.parseInt(String.valueOf(message.getOrDefault("退订(笔数)"
          ,0))));
      createrWork.setUnsubGoodNum(Integer.parseInt(String.valueOf(message.getOrDefault("退订(数量)",0))));
      createrWork.setUnsubOrderMoney(Double.parseDouble(String.valueOf(message.getOrDefault("退订"
          + "(金额)",0.0))));

      double money=
          createrWork.getNewGoodNum()*Double.parseDouble(String.valueOf(message.getOrDefault(
              "单价",0.0)));
      createrWork.setIncome(money);
      createrWorkList.add(createrWork);
    }
    return createrWorkList;
  }

}
