package com.example.dispatchcentre.feign;

import com.example.dispatchcentre.beans.HttpResponseEntity;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author YANG FUCHAO
 * @version 1.0
 * @date 2023-06-20 13:10
 */
@Service
@FeignClient(name = "api-gateway")
public interface FeignApi {

  @RequestMapping(value = "/customer/changeOrderStatusById")
  HttpResponseEntity  changeOrderStatusById(Map<String, Object> map);

  @RequestMapping(value = "/distribute/good/getGoodByOrderId")
  HttpResponseEntity  getGoodByOrderId(Long id);

  @RequestMapping(value = "/customer/getOrderByid")
  HttpResponseEntity  getOrderByid(Long id);


  @PostMapping(value = "/distribute/station/{id}")
   HttpResponseEntity getById(@PathVariable("id") String id);

}
