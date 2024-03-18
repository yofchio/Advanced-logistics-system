package com.example.customerservicecentre.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.customerservicecentre.entity.Orders;
import com.github.pagehelper.PageInfo;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-19
 */
public interface OrderService extends IService<Orders> {
/*  新订，查询*/
  int insert(Map<String,Object > map);
  PageInfo getOrdersByCriteria(Map<String, Object> map) throws ParseException;
  PageInfo getAllOrder(Map<String, Object> map);
  PageInfo selectOrderbyCustomer(Map<String, Object> map);
  int updatebyId(Orders orders) throws ParseException;
  PageInfo getWorkByid(Map<String, Object> map) throws ParseException;
  Orders getOrderByid(Long id);
  /*  退订*/
  int checkUnsub(Orders orders);
  int addUnsub(Map<String,Object > map);
  /*  退换货*/
  int checkReturn(Orders orders);
  int addReturn(Map<String,Object > map);
  List<Orders> getOrderByStationFin(Map<String, Object> map) throws ParseException;
  PageInfo getOrderDis(Map<String, Object> map);
  PageInfo getlack(Map<String, Object> map) throws ParseException;
  int addOrderGood(Map<String,Object > map);

}
