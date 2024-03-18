package com.example.distributionmanagementcenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.distributionmanagementcenter.entity.Constans;
import com.example.distributionmanagementcenter.entity.Good;
import com.example.distributionmanagementcenter.entity.HttpResponseEntity;
import com.example.distributionmanagementcenter.service.GoodService;
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
 * 商品 前端控制器
 * </p>
 *
 * @author jason_cai
 * @since 2023-06-19
 */
@RestController
@RequestMapping("/distribute/good")
public class GoodController {
    private final Logger logger = LoggerFactory.getLogger(GoodController.class);

    @Autowired
    private GoodService goodService;


    @PostMapping(value = "/{id}")
    public HttpResponseEntity<Good> getById(@PathVariable("id") String id) {
        HttpResponseEntity<Good> httpResponseEntity = new HttpResponseEntity<Good>();
        try {
            Good good=goodService.getById(id);
            if(good!=null)
            {
                httpResponseEntity.setData(good);
            }
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("getById ID查找货物>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @PostMapping(value = "/getList")
    public HttpResponseEntity getList(@RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            httpResponseEntity.setData(goodService.getList(map));
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("获取订单信息数据>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @PostMapping(value = "/getByOrderId")
    public HttpResponseEntity getByOrderId(@RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo pageInfo=goodService.getListByOrderId(map);
            httpResponseEntity.setData(pageInfo);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("getById 订单ID查找货物>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @PostMapping(value = "/create")
    public HttpResponseEntity<Good> create(@RequestBody Good params) {
        HttpResponseEntity<Good> httpResponseEntity = new HttpResponseEntity<Good>();
        try {

                if(goodService.saveGood(params)){
                    httpResponseEntity.setData(params);
                }
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);


        } catch (Exception e) {
            logger.info("create 新建货物>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @PostMapping(value = "/delete/{id}")
    public HttpResponseEntity delete(@PathVariable("id") String id) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity<Good>();
        try {
            Long idParse=Long.valueOf(id);
            if(goodService.deleteGoodById(idParse)){
             httpResponseEntity.setData("Successfully delete");
            }
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);


        } catch (Exception e) {
            logger.info("delete 删除货物>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @PostMapping(value = "/update")
    public HttpResponseEntity<Good> update(@RequestBody Good params) {

        HttpResponseEntity<Good> httpResponseEntity = new HttpResponseEntity<Good>();
        try{
               if( goodService.updateGood(params)){
                   httpResponseEntity.setData(params);
               }

                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("update 更新货物>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @PostMapping(value = "/getGoodByOrderId")
    public HttpResponseEntity getGoodByOrderId(@RequestBody Long id) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
                httpResponseEntity.setData(goodService.getGoodByOrderId(id));
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("后端用通过id得到good列表>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @PostMapping(value = "/getGoodByGoodId")
    public HttpResponseEntity getGoodByGoodId(@RequestBody Map<String, Object> map) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            httpResponseEntity.setData(goodService.getListByGoodId(map));
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("根据货物ID查找商品记录>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @PostMapping(value = "/getGoodByGoodId1")
    public HttpResponseEntity getGoodByGoodId1(@RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            httpResponseEntity.setData(goodService.getListByGoodId1(map));
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("后端专用：根据货物ID查找商品记录>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @PostMapping(value = "/getRanking")
    public HttpResponseEntity getRanking(@RequestBody  Map<String, Object> map) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            httpResponseEntity.setData(goodService.getRanking(map));
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("商品排行>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
}
