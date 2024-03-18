package com.example.dispatchcentre.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dispatchcentre.feign.Task;
import com.example.dispatchcentre.entity.vo.Delivery;
import com.github.pagehelper.PageInfo;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
/**
 * <p>
 * 任务单 服务类
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-25
 */
public interface TaskService extends IService<Task> {
  Long insert(Map<String,Object > map) throws ParseException;
  int updatebyId(Task task);
  Task selectbyId(Long id);
  PageInfo selectAll(Map<String,Object> map);
  int  deletebyId(Long id);
  PageInfo searchbykey(Map<String,Object> map) ;
  PageInfo selectOrder(Map<String, Object> map);
  int changeTaskOrderType(Map<String,Object > map);

  Long getOrTaskId(Long id);
  List<Task> selectByDate(Map<String, Object> map);

  PageInfo getGoodListByTaskId(Map<String, Object> map);
  Delivery getDelivery(Map<String, Object> map);
  PageInfo getGoodByTaskId(Map<String, Object> map);
}
