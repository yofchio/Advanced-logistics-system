package com.example.customerservicecentre.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.customerservicecentre.common.utils.DateUtil;
import com.example.customerservicecentre.entity.Customer;
import com.example.customerservicecentre.entity.Orders;
import com.example.customerservicecentre.mapper.CustomerMapper;
import com.example.customerservicecentre.mapper.OrderMapper;
import com.example.customerservicecentre.service.CustomerService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表信息 服务实现类
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-19
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {
  @Autowired
  private CustomerMapper customerMapper;
  @Autowired
  private OrderMapper orderMapper;

  @Override
  public int insert(Customer customer) {
    int res= customerMapper.insert(customer);
    return res;
  }

  @Override
  public int updatebyId(Customer customer) {
    int res=customerMapper.updateById(customer);
    return res;
  }

  @Override
  public Customer selectbyId(Long id) {
    return customerMapper.selectById(id);
  }

  @Override
  public PageInfo selectAll(Map<String,Object> map) {
    PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
        Integer.valueOf(String.valueOf(map.get("pageSize"))));
    System.out.println("mapsss"+map);
      List<Customer> res= customerMapper.selectList(null);
    System.out.println(res);
    PageInfo pageInfo = new PageInfo(res);
    return pageInfo;
  }


  @Override
  public int deletebyId(Long id) {
    HashMap<String,Object> mapid=new HashMap<>();
    mapid.put("customer_id",String.valueOf(id));
    List<Orders> res= orderMapper.selectByMap(mapid);
    if(res.isEmpty()){
      return customerMapper.deleteById(id);
    }
      return 0;
  }

/*  按照客户名、电话或身份证号查找*/
  @Override
  public PageInfo searchbykey(Map<String, Object> map) {

    PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
        Integer.valueOf(String.valueOf(map.get("pageSize"))));

    QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();

    // 判断name属性是否为空，如果不为空则作为查询条件
    if (!map.get("name").equals("")) {
      queryWrapper.like("name", map.get("name"));
    }
    if (!map.get("mobilephone").equals("")) {
      queryWrapper.like("mobilephone", map.get("mobilephone"));
    }
    if (!map.get("idcard").equals("")) {
      queryWrapper.like("idcard", map.get("idcard"));
    }
    List<Customer> res= customerMapper.selectList(queryWrapper);
    System.out.println(res);
    PageInfo pageInfo = new PageInfo(res);
    return pageInfo;
  }

  @Override
  public PageInfo selectOrder(Map<String, Object> map) {
    System.out.println(map);
    PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
        Integer.valueOf(String.valueOf(map.get("pageSize"))));
//    List<Customer> res= customerMapper.getAllCustomer();
//    QueryWrapper<Order> wrapper = new QueryWrapper<>();
//    wrapper.eq("customer_id",map.get("customerId"));
    HashMap<String,Object> mapid=new HashMap<>();
    mapid.put("customer_id",map.get("customerId"));
    List<Orders> res= orderMapper.selectByMap(mapid);
//    System.out.println(res);
    PageInfo pageInfo = new PageInfo(res);
    return pageInfo;
  }

  @Override
  public Customer getUserByUserId(int id) {
    QueryWrapper<Customer> queryWrapper=new QueryWrapper();
    queryWrapper.eq("user_id",id);
    Customer res= customerMapper.selectOne(queryWrapper);
    return res;
  }
}
