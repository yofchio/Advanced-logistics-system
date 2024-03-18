package com.example.distributionmanagementcenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.distributionmanagementcenter.entity.Constans;
import com.example.distributionmanagementcenter.entity.Good;
import com.example.distributionmanagementcenter.entity.HttpResponseEntity;
import com.example.distributionmanagementcenter.entity.StationInOut;
import com.example.distributionmanagementcenter.service.StationInOutService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 库房出库 前端控制器
 * </p>
 *
 * @author Jason_Cai
 * @since 2023-06-20
 */
@RestController
@RequestMapping("/distribute/stationInOut")
public class StationInOutController {
    private final Logger logger = LoggerFactory.getLogger(StationInOutController.class);

    @Autowired
    private StationInOutService stationInOutService;


    @PostMapping(value = "/{id}")
    public HttpResponseEntity<StationInOut> getById(@PathVariable("id") String id) {
        HttpResponseEntity<StationInOut> httpResponseEntity = new HttpResponseEntity<StationInOut>();
        try {
            StationInOut stationInOut=stationInOutService.getById(id);
            if(stationInOut!=null)
            {
                httpResponseEntity.setData(stationInOut);

            }
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("getById ID查找出入站记录>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }


    @PostMapping(value = "/create")
    public HttpResponseEntity create(@RequestBody StationInOut params) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
                 boolean flag=stationInOutService.save(params);
            if(flag){
                httpResponseEntity.setData("Success");
            }
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);


        } catch (Exception e) {
            logger.info("create 新建出入站记录>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }


    @PostMapping(value = "/delete/{id}")
    public HttpResponseEntity<StationInOut> delete(@PathVariable("id") String id) {

        HttpResponseEntity<StationInOut> httpResponseEntity = new HttpResponseEntity<StationInOut>();
        try {
                stationInOutService.removeById(id);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);


        } catch (Exception e) {
            logger.info("delete 删除出入站记录>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }


    @PostMapping(value = "/update")
    public HttpResponseEntity<StationInOut> update(@RequestBody StationInOut params) {

        HttpResponseEntity<StationInOut> httpResponseEntity = new HttpResponseEntity<StationInOut>();
        try {
               stationInOutService.updateById(params);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);


        } catch (Exception e) {
            logger.info("update 更新出入站记录>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @PostMapping(value = "/getListByConditions")
    public HttpResponseEntity getListByConditions(@RequestBody Map<String, Object> map) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo pageInfo = stationInOutService.getListByConditions(map);
             httpResponseEntity.setData(pageInfo);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("根据库房类型，商品名称，日期，出库类型查询出入库记录>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @PostMapping(value = "/getListByConditions1")
    public HttpResponseEntity getListByConditions1(@RequestBody Map<String, Object> map) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            httpResponseEntity.setData(stationInOutService.getListByConditions1(map));
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("后端专用：根据库房类型，商品名称，日期，出库类型查询出入库记录>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
}
