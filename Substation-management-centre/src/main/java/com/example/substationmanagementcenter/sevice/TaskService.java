package com.example.substationmanagementcenter.sevice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.substationmanagementcenter.entity.Task;
import com.github.pagehelper.PageInfo;

import java.text.ParseException;
import java.util.Map;

/**
 * <p>
 * 任务单 服务类
 * </p>
 *
 * @author hzn
 * @since 2023-06-19
 */
public interface TaskService extends IService<Task> {

    PageInfo selectAll(Map<String,Object> map);
    PageInfo getTaskListByCriteria(Map<String,Object> map) throws ParseException;

    int updateTaskPostmanById(Map<String, Object> map);

    PageInfo getTaskToDistribute(Map<String,Object> map) throws ParseException;

    PageInfo getTaskToReceipt(Map<String,Object> map) throws ParseException;

    PageInfo selectTaskById(Map<String,Object> map) throws ParseException;

    PageInfo pirntDeliveryNote(Map<String,Object> map) throws ParseException;

    Long getOrderIdByTaskId(Map<String,Object> map);



}
