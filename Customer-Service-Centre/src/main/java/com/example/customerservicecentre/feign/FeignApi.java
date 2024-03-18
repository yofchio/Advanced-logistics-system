package com.example.customerservicecentre.feign;

import com.example.customerservicecentre.beans.HttpResponseEntity;
import com.example.customerservicecentre.entity.Buy;
import com.example.customerservicecentre.entity.Good;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author YANG FUCHAO
 * @version 1.0
 * @date 2023-06-20 13:10
 */
@Service
@FeignClient(name = "api-gateway")
public interface FeignApi {

  @RequestMapping(value = "/distribute/good/create",method = RequestMethod.POST)
  HttpResponseEntity addGoods(Good good);

  @RequestMapping(value = "/distribute/good/update",method = RequestMethod.POST)
  HttpResponseEntity updateGoodByid(Good good);

  @RequestMapping(value = "/distribute/good/delete/{id}",method = RequestMethod.POST)
  HttpResponseEntity deleteGoodByid(@PathVariable("id") String id);

  @RequestMapping(value = "/distribute/good/{id}",method = RequestMethod.POST)
  HttpResponseEntity getGoodByid(@PathVariable("id")Long id);

  @RequestMapping(value = "/distribute/good/deleteBuyByGoodId",method = RequestMethod.POST)
  HttpResponseEntity deleteBuyByGoodid(Map<String,Object > map);

  @RequestMapping(value = "/distribute/good/updateBuyByGoodId",method = RequestMethod.POST)
  HttpResponseEntity updateBuyByid(Map<String,Object > map);

}
