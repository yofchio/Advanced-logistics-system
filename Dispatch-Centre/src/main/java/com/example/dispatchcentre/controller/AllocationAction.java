package com.example.dispatchcentre.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.example.dispatchcentre.beans.HttpResponseEntity;
import com.example.dispatchcentre.common.Constans;
import com.example.dispatchcentre.entity.Allocation;
import com.example.dispatchcentre.service.AllocationService;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 商品调拨 前端控制器
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-25
 */
@RestController
@RequestMapping("/dispatch")
public class AllocationAction {
    private final Logger logger = LoggerFactory.getLogger(AllocationAction.class);

    @Autowired
    private AllocationService allocationService;

    @RequestMapping(value = "/addTaskDispatch",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity addTaskDispatch(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
           int res=allocationService.insertTaskDispatch(map);
           if (res==1) {
               httpResponseEntity.setCode(Constans.SUCCESS_CODE);
               httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
           }
        } catch (Exception e) {
            logger.info("addTaskDispatch 添加任务调度单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/addSationDispatch",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity insertSationDispatch(@RequestBody Allocation allocation) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=allocationService.insertSationDispatch(allocation);
            if (res==1) {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }
        } catch (Exception e) {
            logger.info("addSationDispatch 添加库房调度单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/insertTaskDispatchlist",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity insertTaskDispatchlist(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=allocationService.insertTaskDispatchlist(map);
            if (res==1) {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }
        } catch (Exception e) {
            logger.info("addSationDispatch 添加库房调度单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/insertWithDrawDispatch",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity insertWithDrawDispatch(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=allocationService.insertWithDrawDispatch(map);
            if (res==1) {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }
        } catch (Exception e) {
            logger.info("insertWithDrawDispatch 添加库房调度单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/updateAllocationbyId",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity updateAllocationbyId(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int  res=allocationService.updatebyId(map);
            httpResponseEntity.setData(res);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("updateAllocationbyId 更新调度单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/updateAllocationMesbyId",method = RequestMethod.POST, headers =
        "Accept"
        + "=application/json")
    public HttpResponseEntity updateAllocationMesbyId(@RequestBody Allocation allocation) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int  res=allocationService.updatePeobyId(allocation);
            httpResponseEntity.setData(res);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("updateAllocationbyId 更新调度单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/getAllocation",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity getAllocation(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo res=allocationService.searchbykey(map);
            httpResponseEntity.setData(res);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("getAllocation 得到所有的调度单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/getGoodListByAlloId",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity getGoodListByAlloId(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo res=allocationService.getGoodListByAlloId(map);
            httpResponseEntity.setData(res);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("getGoodListByAlloId good得到调度单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/getByAlloId",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity getByAlloId(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            Allocation res=allocationService.selectbyId(map);
            httpResponseEntity.setData(res);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("getGoodListByAlloId good得到调度单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }
}
