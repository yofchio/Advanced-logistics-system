package com.example.customerservicecentre.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.customerservicecentre.entity.Customer;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
/**
 * <p>
 * 用户表信息 服务类
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-19
 */
public interface CustomerService extends IService<Customer> {
    int insert(Customer customer);
    int updatebyId(Customer customer);
    Customer selectbyId(Long id);
    PageInfo selectAll(Map<String,Object> map);
    int  deletebyId(Long id);
    PageInfo searchbykey(Map<String,Object> map);
    PageInfo selectOrder(Map<String, Object> map);
    Customer  getUserByUserId(int id);


}
