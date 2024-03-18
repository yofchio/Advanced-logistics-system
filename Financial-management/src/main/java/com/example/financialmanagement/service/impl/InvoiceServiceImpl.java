package com.example.financialmanagement.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.financialmanagement.common.utils.DateUtil;
import com.example.financialmanagement.mapper.InvoiceMapper;
import com.example.financialmanagement.service.InvoiceService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.financialmanagement.entity.*;
/**
 * <p>
 * 发票 服务实现类
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-20
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements InvoiceService {
  @Autowired
  private InvoiceMapper invoiceMapper;

  @Override
  public String addInvoice(Invoice invoice) {
    System.out.println("invoice"+invoice);
    switch (invoice.getInvoiceClass()){
      case "登记":
        if(checkIn(invoice))
          return "登记号码区间存在冲突";
          break;
      case "领用":
        if(checkOut(invoice))
          return "领用号码区间存在冲突";
          break;
      case "作废":
        if(checkPutAway(invoice))
          return "作废号码区间存在冲突";
        break;
      case "退回":
        if(checkReturn(invoice))
          return "退回号码区间存在冲突";
        break;
      default:
         break;
    }
    Date  now= DateUtil.getCreateTime();
    Integer batch=invoice.getBatch();
    invoice.setDate(now);
    Long start=invoice.getStartNumber();
    Long end=invoice.getEndNumber();
    String invoiceClass=invoice.getInvoiceClass();
    Invoice invoice1=invoiceMapper.selectOne(new QueryWrapper<Invoice>().eq("batch",batch).eq(
         "invoiceClass",invoiceClass).eq("end_number",start-1));//如果开始等于结束号码。直接更新；
    if(invoice1!=null){
      invoice1.setEndNumber(end);
      int res = invoiceMapper.updateById(invoice1);
      if(res==1){
        return "OK";
      }
    }
    int res = invoiceMapper.insert(invoice);
     if(res==1){
       return "OK";
     }
    return "NO";
  }

  private boolean checkIn(Invoice invoice){
    Long start=invoice.getStartNumber();
    Long end=invoice.getEndNumber();
    Integer batch=invoice.getBatch();
    List<Invoice>  invoices=invoiceMapper.selectList(new QueryWrapper<Invoice>().eq("batch",batch).le(
        "start_number"
        , end).ge("end_number", start).eq("invoiceClass","登记"));
   boolean hasOverlap = !invoices.isEmpty();
   return hasOverlap;//ture 为存在相同区间
 }

  private boolean checkOut(Invoice invoice){
    Long start=invoice.getStartNumber();
    Long end=invoice.getEndNumber();
    Integer batch=invoice.getBatch();
    List<Invoice>  resPut=
        invoiceMapper.selectList(new QueryWrapper<Invoice>().eq("batch",batch).le("start_number"
            , end).ge("end_number", start).eq("invoiceClass","作废"));
    if(!resPut.isEmpty()){
      return true;//true 为 不行
    }
    List<Invoice>  resRe=
        invoiceMapper.selectList(new QueryWrapper<Invoice>().eq("batch",batch).le("start_number"
        , end).ge("end_number", start).eq("invoiceClass","退回"));
    if(!resRe.isEmpty()){
      return false;//true 为 不行
    }
    List<Invoice>  invoices=invoiceMapper.selectList(new QueryWrapper<Invoice>().eq("batch",batch).le("start_number"
        , end).ge("end_number", start).eq("invoiceClass", "领用").or().eq("invoiceClass","作废"));
    if(!invoices.isEmpty()){//不再区间
      return true;
    }
    List<Invoice>  res=invoiceMapper.selectList(new QueryWrapper<Invoice>().eq("batch",batch).le("start_number"
        , end).ge("end_number", start).eq("invoiceClass","登记"));
    if(!res.isEmpty()){//不再区间
      return false;//true 为 不行
    }
    return true;
  }

  private boolean checkReturn(Invoice invoice){
    Long start=invoice.getStartNumber();
    Long end=invoice.getEndNumber();
    Integer batch=invoice.getBatch();
    List<Invoice>  invoices=invoiceMapper.selectList(new QueryWrapper<Invoice>().eq("batch",batch).le("start_number"
        , end).ge("end_number", start).eq("invoiceClass","退回"));
    if(!invoices.isEmpty()){//不再区间
      return true;//true 为 不行
    }
    List<Invoice>  res=invoiceMapper.selectList(new QueryWrapper<Invoice>().eq("batch",batch).le("start_number"
        , end).ge("end_number", start).eq("invoiceClass", "领用"));
    return res.isEmpty();
  }

  private boolean checkPutAway(Invoice invoice){
    Long start=invoice.getStartNumber();
    Long end=invoice.getEndNumber();
    Integer batch=invoice.getBatch();
    List<Invoice>  invoices=invoiceMapper.selectList(new QueryWrapper<Invoice>().eq("batch",batch).le("start_number"
        , end).ge("end_number", start).eq("invoiceClass","作废"));
    if(!invoices.isEmpty()){//不再区间
      return true;//true 为 不行
    }
    List<Invoice>  res=invoiceMapper.selectList(new QueryWrapper<Invoice>().eq("batch",batch).le("start_number"
        , end).ge("end_number", start).eq("invoiceClass", "领用"));
    return res.isEmpty();

  }

  public boolean checkUse(Long id){
    //已经领用，并且没有退回，并且没有作废，并且不在use中
    List<Invoice>  resOut=
        invoiceMapper.selectList(new QueryWrapper<Invoice>().le("start_number"
            , id).ge("end_number", id).eq("invoiceClass","领用"));
    if(resOut.isEmpty()){
      return true;//true 为 不行
    }
    List<Invoice>  resRe=
        invoiceMapper.selectList(new QueryWrapper<Invoice>().le("start_number"
            , id).ge("end_number", id).eq("invoiceClass","退回"));
    if(!resRe.isEmpty()){
      return true;//true 为 不行
    }
    List<Invoice>  resPut=
        invoiceMapper.selectList(new QueryWrapper<Invoice>().le("start_number"
            , id).ge("end_number", id).eq("invoiceClass","作废"));
    if(!resPut.isEmpty()){
      return true;//true 为 不行
    }
    return false;
  }
  @Override
  public PageInfo selectInvoice(Map<String, Object> map) {
    PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
        Integer.valueOf(String.valueOf(map.get("pageSize"))));
    QueryWrapper<Invoice> queryWrapper = new QueryWrapper<>();

    String invoiceClass= String.valueOf(map.get("invoiceClass"));
    ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
    // 格式化中国时区时间为指定格式的字符串
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String startDate = LocalDateTime.parse(String.valueOf(map.get("startTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
    String endDate = LocalDateTime.parse(String.valueOf(map.get("endTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);


    Long invoiceStartNumber= Long.valueOf(String.valueOf(map.get("invoiceStartNumber")));
    Long invoiceEndNumber=Long.valueOf(String.valueOf(map.get("invoiceEndNumber")));
    // 根据前端传来条件进行拼接查询条件
    queryWrapper.eq(ObjectUtils.isNotEmpty(invoiceClass), "invoiceClass", invoiceClass);
    queryWrapper.between("date",startDate, endDate);
    queryWrapper.and(i -> i.ge("start_number",invoiceStartNumber).le("end_number",
        invoiceEndNumber));
    List<Invoice> res=invoiceMapper.selectList(queryWrapper);
    PageInfo<Invoice> pageInfo=new PageInfo<>(res);
    return pageInfo;
  }
}
