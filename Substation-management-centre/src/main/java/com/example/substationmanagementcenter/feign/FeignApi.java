package com.example.substationmanagementcenter.feign;

import com.example.substationmanagementcenter.beans.HttpResponseEntity;
import com.example.substationmanagementcenter.entity.Use;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author hzn
 * @version 1.0
 * @date 2023-06-20 13:10
 */
@Service
@FeignClient(name = "api-gateway")
public interface FeignApi {

//  @RequestMapping(value = "/customer/getOrderByid",method = RequestMethod.POST)
//  HttpResponseEntity getOrderByid(Map<String, Object> map);
@RequestMapping(value = "/customer/getOrderByid",method = RequestMethod.POST)
HttpResponseEntity getOrderByid(Long id);

  @RequestMapping(value = "/customer/selectByCustomerId",method = RequestMethod.POST)
  HttpResponseEntity selectByCustomerId(Long id);

  @RequestMapping(value = "/dispatch/changeTaskOrderType")
  HttpResponseEntity changeTaskOrderType(Map<String,Object> map);

//  @RequestMapping(value = "/financial/getUseNumber")
//  HttpResponseEntity getUseNumber(Map<String,Object> map);

  @RequestMapping(value = "/financial/addReceiptInvoice")
  HttpResponseEntity addReceiptInvoice(Use use);

  @RequestMapping(value = "/dispatch/getOrTaskId")
  Long getOrTaskId(Long id);

  @RequestMapping(value = "/financial/setPutAway")
  int setPutAway(Long id);

}
