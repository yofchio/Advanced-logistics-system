package com.example.dispatchcentre.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dispatchcentre.entity.Allocation;
import com.github.pagehelper.PageInfo;
import java.text.ParseException;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
/**
 * <p>
 * 商品调拨 服务类
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-25
 */
public interface AllocationService extends IService<Allocation> {
  int insertTaskDispatch(Map<String,Object> map) throws ParseException;
  int insertSationDispatch(Allocation allocation) ;
  int updatebyId(Map<String, Object> map);
  Allocation selectbyId(Map<String,Object> map);
  PageInfo selectAll(Map<String,Object> map);
  PageInfo searchbykey(Map<String,Object> map);
  PageInfo getGoodListByAlloId(Map<String, Object> map);
   int insertTaskDispatchlist(Map<String,Object> map) throws ParseException;
   int insertWithDrawDispatch(Map<String,Object> map) throws ParseException;
   int updatePeobyId(Allocation allocation);
}
