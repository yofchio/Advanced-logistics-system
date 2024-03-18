package com.example.distributionmanagementcenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.distributionmanagementcenter.entity.Constans;
import com.example.distributionmanagementcenter.entity.Good;
import com.example.distributionmanagementcenter.entity.HttpResponseEntity;
import com.example.distributionmanagementcenter.entity.Supply;
import com.example.distributionmanagementcenter.service.SupplyService;
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
 * 供应商 前端控制器
 * </p>
 *
 * @author jason_cai
 * @since 2023-06-19
 */
@RestController
@RequestMapping("/distribute/supply")
public class SupplyController {

    private final Logger logger = LoggerFactory.getLogger(SupplyController.class);
    @Autowired
    private SupplyService supplyService;

    @PostMapping(value = "/{id}")
    public HttpResponseEntity<Supply> getById(@PathVariable("id") String id) {
        HttpResponseEntity<Supply> httpResponseEntity = new HttpResponseEntity<Supply>();
        try {
            Supply supply=supplyService.getById(id);
            if(supply!=null)
            {
                httpResponseEntity.setData(supply);

            }
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("getById ID查找供货商>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @PostMapping(value = "/getList")
    public HttpResponseEntity getList(@RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo pageInfo=supplyService.getList(map);
            httpResponseEntity.setData(pageInfo);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("查找供应商列表" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @PostMapping(value = "/getListByCondition")
    public HttpResponseEntity getListByCondition(@RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo pageInfo=supplyService.getListByConditions(map);
            httpResponseEntity.setData(pageInfo);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("根据条件查找供应商列表" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @PostMapping(value = "/create")
    public HttpResponseEntity create(@RequestBody Supply params) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            if(supplyService.save(params)){
                  httpResponseEntity.setData("Success");
            }
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);


        } catch (Exception e) {
            logger.info("create 新增供货商>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }


    @PostMapping(value = "/delete/{id}")
    public HttpResponseEntity delete(@PathVariable("id") String id) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
                boolean flag=supplyService.removeById(id);
                if(flag){
                    httpResponseEntity.setData("Success");
                }
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("delete 删除供货商>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }


    @PostMapping(value = "/update")
    public HttpResponseEntity update(@RequestBody Supply params) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
               if(supplyService.updateById(params)){
                   httpResponseEntity.setData("Success");
               }
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("update 更新供货商>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
}
