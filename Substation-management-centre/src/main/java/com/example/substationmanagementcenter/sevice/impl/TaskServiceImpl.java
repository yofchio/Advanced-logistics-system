package com.example.substationmanagementcenter.sevice.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.substationmanagementcenter.beans.HttpResponseEntity;
import com.example.substationmanagementcenter.entity.*;
import com.example.substationmanagementcenter.entity.vo.DeliveryNote;
import com.example.substationmanagementcenter.entity.vo.TaskOrder;
import com.example.substationmanagementcenter.feign.FeignApi;
import com.example.substationmanagementcenter.mapper.CentralStationMapper;
import com.example.substationmanagementcenter.mapper.TaskMapper;
import com.example.substationmanagementcenter.sevice.TaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 退订 服务实现类
 * </p>
 *
 * @author hzn
 * @since 2023-06-19
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private FeignApi feignApi;

    @Autowired
    private CentralStationMapper centralStationMapper;


    @Override
    public PageInfo selectAll(Map<String,Object> map) {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        List<Task> res= taskMapper.selectList(null);
        PageInfo pageInfo = new PageInfo(res);
        return pageInfo;
    }

    //新建一个TaskOrder类，用于给前端返回两表的综合信息
    //使用QueryWrapper对任务单进行条件筛选，转化输入的时间的时区使其符合上海地区的时间
    //可选的筛选条件有"startLine"、"endLine"、"taskType"、"taskStatus"、"substation"、"postman"，通过if判断进行筛选条件的选择
    //将符合前端输入的筛选条件的任务单列表进行遍历，同时通过微服务找到每个任务单对应的订单
    //将这些信息整合成一个TaskOrder的列表，存于一个pageInfo中并返回给前端
    @Override
    public PageInfo getTaskListByCriteria(Map<String,Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));

        //筛选task
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
        if(map.get("startLine") != null && !map.get("startLine").equals("")){

            // 格式化中国时区时间为指定格式的字符串
            String date = LocalDateTime.parse(String.valueOf(map.get("startLine")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                    ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
            Date startline = simpleDateFormat.parse(date);
            System.out.println("start"+startline);
            queryWrapper.ge("deadline",startline);
        }
        if(map.get("endLine") != null && !map.get("endLine").equals("")){
            String date = LocalDateTime.parse(String.valueOf(map.get("endLine")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                    ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
            Date endline = simpleDateFormat.parse(date);
            System.out.println("end!!!"+endline);
            queryWrapper.lt("deadline",endline);
        }
        if(map.get("taskType") != null && !map.get("taskType").equals("")){
            System.out.println("taskType");
            queryWrapper.eq("task_type",map.get("taskType"));
        }
        if(map.get("taskStatus") != null && !map.get("taskStatus").equals("")){
            queryWrapper.eq("task_status",map.get("taskStatus"));
        }
        if(map.get("substation") != null && !map.get("substation").equals("")){
            queryWrapper.eq("substation",map.get("substation"));
        }
        if(map.get("postman") != null && !map.get("postman").equals("")){
            queryWrapper.eq("postman",map.get("postman"));
        }
        List<Task> res= taskMapper.selectList(queryWrapper);
        System.out.println("res!!1"+res);


        //设置返回前端的列表
        List<TaskOrder> taskOrders = new ArrayList<>();
        for(Task task:res){
            System.out.println("for");
            //由task找对应的order
            HttpResponseEntity resOrder = feignApi.getOrderByid(task.getOrderId());
            String jsonString1 = JSON.toJSONString(resOrder.getData());  // 将对象转换成json格式数据
            Orders order = JSON.parseObject(jsonString1, Orders.class); // 这样就可以了


            //列表内每一列的数据
            TaskOrder taskOrder = new TaskOrder();
            taskOrder.setOrderId(task.getOrderId());
            taskOrder.setId(task.getId());
            taskOrder.setTaskType(task.getTaskType());
            taskOrder.setCustomerName(task.getCustomerName());
            taskOrder.setAddress(task.getAddress());
            taskOrder.setReceiveName(order.getReceiveName());
            taskOrder.setDeadline(task.getDeadline());
            taskOrder.setGoodSum(order.getGoodSum());
            taskOrder.setEndDate(task.getEndDate());
            System.out.println("task.getTaskDate()!!"+task.getTaskDate());
            taskOrder.setTaskDate(task.getTaskDate());
            taskOrder.setTaskStatus(task.getTaskStatus());
            taskOrder.setIsInvoice(order.getIsInvoice());
            taskOrder.setSubstation(task.getSubstation());
            taskOrder.setPostman(task.getPostman());
            taskOrder.setMobilePhone(order.getMobilephone());
            taskOrder.setCustomerAddress(order.getCustomerAddress());
            taskOrders.add(taskOrder);
        }
        PageInfo pageInfo = new PageInfo(taskOrders);
        return pageInfo;
    }

    //根据前端传来的“id”找到对应的任务单，给任务单分配配送员并将任务单状态更新成“已分配”
    @Override
    public int updateTaskPostmanById(Map<String, Object> map) {
        UpdateWrapper<Task> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",map.get("id"));
        if(!map.get("taskStatus").equals("已分配")){
            System.out.println("!!!!!");
            updateWrapper.set("task_status","已分配");
        }
        updateWrapper.set("postman",map.get("postman"));
        Integer rows = taskMapper.update(null, updateWrapper);
        return rows;
    }

    @Override
    public PageInfo getTaskToDistribute(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_status","可分配");
        List<Task> res= taskMapper.selectList(queryWrapper);
        PageInfo pageInfo = new PageInfo(res);
        return pageInfo;
    }

    @Override
    public PageInfo getTaskToReceipt(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("task_status","已分配");
        List<Task> res= taskMapper.selectList(queryWrapper);
        PageInfo pageInfo = new PageInfo(res);
        return pageInfo;
    }

    @Override
    public PageInfo selectTaskById(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));

        Task res= taskMapper.selectById((int)map.get("id"));
        List<Task> tasks = new ArrayList<>();
        tasks.add(res);
        PageInfo pageInfo = new PageInfo(tasks);
        return pageInfo;
    }

    //新建一个DeliveryNote类，用于给前端返回两表的综合信息
    //由前端传回的“taskId”找到对应的任务单再由此找到对应的订单
    //遍历前端传回的货物列表，结合任务单和对应订单的信息得到deliveryNote列表，并通过pageInfo返回前端
    @Override
    public PageInfo pirntDeliveryNote(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));

        Task task = taskMapper.selectById(String.valueOf(map.get("taskId")));
        //由task找对应的order
        HttpResponseEntity resOrder = feignApi.getOrderByid(task.getOrderId());
        String jsonString1 = JSON.toJSONString(resOrder.getData());  // 将对象转换成json格式数据
        Orders order = JSON.parseObject(jsonString1, Orders.class); // 这样就可以了

        //由前端得到good列表
        List<Good> goods = new ArrayList<>();
        String jsonString2 = JSON.toJSONString(map.get("goods"));
        JSONArray jsonArray2 = JSON.parseArray(jsonString2);
        goods = JSON.parseArray(String.valueOf(jsonArray2),Good.class);
        System.out.println("goodList"+goods);

        List<DeliveryNote>deliveryNotes = new ArrayList<>();

        DeliveryNote deliveryNote = new DeliveryNote();
        deliveryNote.setId(task.getId());
        deliveryNote.setCustomerName(task.getCustomerName());
        deliveryNote.setMobilephone(order.getMobilephone());
        deliveryNote.setPostcode(order.getPostcode());
        deliveryNote.setAddress(task.getAddress());
        deliveryNote.setDeliveryDate(order.getDeliveryDate());
        deliveryNote.setSubstation(task.getSubstation());
        deliveryNote.setIsInvoice(order.getIsInvoice());
        deliveryNote.setGoodSum(order.getGoodSum());
        deliveryNote.setTaskType(task.getTaskType());
        deliveryNote.setRemark(order.getRemark());
        deliveryNote.setFeedback(String.valueOf(map.get("customerFeedback")));
        deliveryNote.setSignature(String.valueOf(map.get("customerSignature")));

        for(Good good:goods){
            //得到对应商品信息
            QueryWrapper<CentralStation> centralStationQueryWrapper =new QueryWrapper<>();
            centralStationQueryWrapper.eq("id",good.getGoodId());
            List<CentralStation> centralStations = centralStationMapper.selectList(centralStationQueryWrapper);
            CentralStation centralStation = centralStations.get(0);
            System.out.println("centralStation"+centralStation);

            deliveryNote.setGoodName(centralStation.getGoodName());
            deliveryNote.setGoodPrice(centralStation.getGoodPrice());
            deliveryNotes.add(deliveryNote);
        }

        PageInfo pageInfo = new PageInfo(deliveryNotes);
        return pageInfo;
    }

    @Override
    public Long getOrderIdByTaskId(Map<String, Object> map) {
        Task task = taskMapper.selectById(String.valueOf(map.get("taskId")));
        return task.getOrderId();
    }


}
