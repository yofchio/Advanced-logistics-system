package com.example.financialmanagement.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.financialmanagement.common.utils.DateUtil;
import com.example.financialmanagement.entity.Invoice;
import com.example.financialmanagement.entity.Use;
import com.example.financialmanagement.mapper.UseMapper;
import com.example.financialmanagement.service.UseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

/**
 * <p>
 * 发票 服务实现类
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-28
 */
@Service
public class UseServiceImpl extends ServiceImpl<UseMapper, Use> implements UseService {
  @Autowired
  private UseMapper useMapper;
  @Autowired
  private InvoiceServiceImpl invoiceService;
  @Override
  public int addUseInvoice(Use use) {
    int res=useMapper.insert(use);
    return res;
  }

/*  回执单的发票*/
  @Override
  public Long addReceiptInvoice(Use use) {

    Use inUse= getNewNumber();
    use.setType("领用");
    use.setBatch(inUse.getBatch());
    Date  now= DateUtil.getCreateTime();
    use.setDate(now);
    Long number=inUse.getNumber();
    while (invoiceService.checkUse(number)){
      number++;
      if(number>5000){
        return 5000L;//5000条数据自动退出
      }
    }
    use.setNumber(number);
    int res=useMapper.insert(use);
    if(res==1)
      return  number;
    return 5000L;
  }
/*  发票状态修改*/
  @Override
  public int changeUseByid(Use use) {
    int res=useMapper.updateById(use);
    return res;
  }

  @Override
  public int setPutAway(Long number) {
   QueryWrapper<Use> queryWrapper= new QueryWrapper<>();
    queryWrapper.eq("number",number);
  Use use=useMapper.selectOne(queryWrapper);
  use.setType("作废");
  return useMapper.updateById(use);
  }

  @Override
  public PageInfo select(Map<String,Object> map) throws ParseException {
    PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
        Integer.valueOf(String.valueOf(map.get("pageSize"))));

    QueryWrapper<Use> queryWrapper = new QueryWrapper<>();
    String invoiceClass= String.valueOf(map.get("invoiceClass"));
    ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
    // 格式化中国时区时间为指定格式的字符串
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String startDate = LocalDateTime.parse(String.valueOf(map.get("startTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
    String endDate = LocalDateTime.parse(String.valueOf(map.get("endTime")), DateTimeFormatter.ISO_DATE_TIME).atZone(
        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);

    String invoiceNumber=String.valueOf(map.get("invoiceNumber"));
    String useName=String.valueOf(map.get("username"));

    queryWrapper.like(ObjectUtils.isNotEmpty(invoiceNumber), "number", invoiceNumber);
    queryWrapper.like(ObjectUtils.isNotEmpty(useName), "name", useName);
    queryWrapper.between("date",startDate, endDate);
    List<Use> res= useMapper.selectList(queryWrapper);
//    System.out.println(res);
    PageInfo pageInfo = new PageInfo(res);
    return pageInfo;
  }

  @Override
  public Use getNewNumber() {
    QueryWrapper<Use> queryWrapper = new QueryWrapper<>();
    queryWrapper.orderByDesc("date").last("limit 1");
    return this.getOne(queryWrapper);
  }



}
