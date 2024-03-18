package com.example.distributionmanagementcenter.feign;

import com.example.distributionmanagementcenter.entity.HttpResponseEntity;
import com.example.distributionmanagementcenter.entity.Receipt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Service
@FeignClient(name = "api-gateway")
public interface FeignApi {
  @RequestMapping(value = "/dispatch/selectByDate")
  HttpResponseEntity selectByDate(Map<String, Object> map);

  @PostMapping(value = "/substation/receipt/getReceiptList1")
  HttpResponseEntity<List<Receipt>> getReceiptList();
}
