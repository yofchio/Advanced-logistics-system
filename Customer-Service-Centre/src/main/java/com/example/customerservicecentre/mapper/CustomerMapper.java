package com.example.customerservicecentre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.customerservicecentre.entity.Customer;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 * 用户表信息 Mapper 接口
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-19
 */
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
   List<Customer> getAllCustomer();
}
