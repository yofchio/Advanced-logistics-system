package com.example.warehousemanagementcentre.controller;

import com.example.warehousemanagementcentre.beans.HttpResponseEntity;
import com.example.warehousemanagementcentre.common.Constans;
import com.example.warehousemanagementcentre.service.CentralstationService;
import com.example.warehousemanagementcentre.service.InoutstationService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 中心库房管理  前端控制器
 * </p>
 *
 * @author hzn
 * @since 2023-06-25
 */
@RestController
@RequestMapping("/warehouse/centralcontroller")
public class CentralStationController {
    private final Logger logger = LoggerFactory.getLogger(CentralStationController.class);
    @Autowired
    private CentralstationService centralstationService;

    @Autowired
    private InoutstationService inoutstationService;


//    @RequestMapping(value = "/searchBuy",method = RequestMethod.GET, headers = "Accept"
//            + "=application/json")
//    public HttpResponseEntity searchBuy(@RequestBody Map<String,Object> map) {
//        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
//        try {
//            PageInfo pageInfo = centralstationService.selectBuy(map);
//            httpResponseEntity.setData(pageInfo);
//            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
//            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
//        } catch (Exception e) {
//            logger.info("search 搜索购货单信息>>>>>>>>>>>" + e.getLocalizedMessage());
//            httpResponseEntity.setCode(Constans.EXIST_CODE);
//            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
//        }
//        return httpResponseEntity;
//    }
//
//
//    @RequestMapping(value = "/inStation",method = RequestMethod.POST, headers = "Accept"
//            + "=application/json")
//    public HttpResponseEntity inStation(@RequestBody Map<String,Object> map) {
//        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
//        try {
//            System.out.println(map);
//            int res=centralstationService.toInStation(map);
//            if(res==1)
//            {
//                httpResponseEntity.setData(res);
//                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
//                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
//            }else
//            {
//                httpResponseEntity.setCode(Constans.EXIST_CODE);
//                httpResponseEntity.setMessage(Constans.ADD_FAIL);
//            }
//
//        } catch (Exception e) {
//            logger.info("中心客房入库>>>>>>>>>>>" + e.getLocalizedMessage());
//            httpResponseEntity.setCode(Constans.EXIST_CODE);
//            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
//        }
//        return httpResponseEntity;
//    }
//
//    @RequestMapping(value = "/outStation",method = RequestMethod.POST, headers = "Accept"
//            + "=application/json")
//    public HttpResponseEntity outStation(@RequestBody Map<String,Object> map) {
//        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
//        try {
//            int res=centralstationService.toOutStation(map);
//            if(res==1)
//            {
//                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
//                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
//            }else
//            {
//                httpResponseEntity.setCode(Constans.EXIST_CODE);
//                httpResponseEntity.setMessage(Constans.ADD_FAIL);
//            }
//
//        } catch (Exception e) {
//            logger.info("中心库房出库>>>>>>>>>>>" + e.getLocalizedMessage());
//            httpResponseEntity.setCode(Constans.EXIST_CODE);
//            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
//        }
//        return httpResponseEntity;
//    }


    @RequestMapping(value = "/searchInCentral",method = RequestMethod.POST, headers = "Accept"
            + "=application/json")
    public HttpResponseEntity searchInCentral(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo res = centralstationService.searchInCentral(map);
            httpResponseEntity.setData(res);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("search 搜索购货单信息>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/inCentral",method = RequestMethod.POST, headers = "Accept"
            + "=application/json")
    public HttpResponseEntity inCentral(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=centralstationService.inCentral(map);
            if(res==1)
            {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else if(res==2){
                httpResponseEntity.setCode(Constans.RETURN_FAILED_CODE);
                httpResponseEntity.setMessage(Constans.Change_Central_ERROR);
            }else{
                httpResponseEntity.setCode(Constans.RETURN_FAILED_CODE);
                httpResponseEntity.setMessage(Constans.RETURN_FAILED_MESSAGE);
            }
        } catch (Exception e) {
            logger.info("中心购货入库>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/outCentral",method = RequestMethod.POST, headers = "Accept"
            + "=application/json")
    public HttpResponseEntity outCentral(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=centralstationService.outCentral(map);
            if(res==1)
            {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else if(res==2){
                httpResponseEntity.setCode(Constans.RETURN_FAILED_CODE);
                httpResponseEntity.setMessage(Constans.STORAGE_SHORTAGE);
            }else if(res == 3){
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.Good_NOT_EXIST);
            }else
            {
                httpResponseEntity.setCode(Constans.RETURN_FAILED_CODE);
                httpResponseEntity.setMessage(Constans.RETURN_FAILED_MESSAGE);
            }
        } catch (Exception e) {
            logger.info("search 中心出库>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }


    @RequestMapping(value = "/inSubstation",method = RequestMethod.POST, headers = "Accept"
            + "=application/json")
    public HttpResponseEntity inSubstation(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=centralstationService.toInSubstation(map);
            if(res==1)
            {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else
            {
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.ADD_FAIL);
            }

        } catch (Exception e) {
            logger.info("分站库房入库>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/takeGoods",method = RequestMethod.POST, headers = "Accept"
            + "=application/json")
    public HttpResponseEntity takeGoods(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=centralstationService.takeGoods(map);
            if(res==1)
            {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else if(res == 3){
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.Good_NOT_EXIST);
            }
            else
            {
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.ADD_FAIL);
            }

        } catch (Exception e) {
            logger.info("分站库房入库>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/returnGoodsToSub",method = RequestMethod.POST, headers = "Accept"
            + "=application/json")
    public HttpResponseEntity returnGoodsToSub(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            System.out.println(map);
            int res=centralstationService.returnGoodsToSub(map);
            if(res==1)
            {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else if(res==2){
                httpResponseEntity.setCode(Constans.RETURN_FAILED_CODE);
                httpResponseEntity.setMessage(Constans.RETURN_FOBIDDEN_MESSAGE);
            }else if(res == 3){
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.Good_NOT_EXIST);
            }
            else
            {
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.RETURN_FAILED_MESSAGE);
            }
        } catch (Exception e) {
            logger.info("退货，分站库房入库>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/returnGoodsOutSub",method = RequestMethod.POST, headers = "Accept"
            + "=application/json")
    public HttpResponseEntity returnGoodsOutSub(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=centralstationService.returnGoodsOutSub(map);
            if(res==1)
            {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else if(res==2){
                httpResponseEntity.setCode(Constans.RETURN_FAILED_CODE);
                httpResponseEntity.setMessage(Constans.RETURN_FOBIDDEN_MESSAGE);
            }else if(res == 3){
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.Good_NOT_EXIST);
            }else
            {
                httpResponseEntity.setCode(Constans.RETURN_FAILED_CODE);
                httpResponseEntity.setMessage(Constans.RETURN_FAILED_MESSAGE);
            }

        } catch (Exception e) {
            logger.info("退货，分站退回中心库房>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/returnGoodsToCenter",method = RequestMethod.POST, headers = "Accept"
            + "=application/json")
    public HttpResponseEntity returnGoodsToCenter(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=centralstationService.returnGoodsToCenter(map);
            if(res==1)
            {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else if(res==2){
                httpResponseEntity.setCode(Constans.RETURN_FAILED_CODE);
                httpResponseEntity.setMessage(Constans.RETURN_FOBIDDEN_MESSAGE);
            }else if(res == 3){
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.Good_NOT_EXIST);
            }else
            {
                httpResponseEntity.setCode(Constans.RETURN_FAILED_CODE);
                httpResponseEntity.setMessage(Constans.RETURN_FAILED_MESSAGE);
            }

        } catch (Exception e) {
            logger.info("退货，分站退回中心库房>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/centralStationReturn",method = RequestMethod.POST, headers = "Accept"
            + "=application/json")
    public HttpResponseEntity centralStationReturn(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=centralstationService.centralStationReturn(map);
            if(res==1)
            {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else if(res==2){
                httpResponseEntity.setCode(Constans.RETURN_FAILED_CODE);
                httpResponseEntity.setMessage(Constans.RETURN_FOBIDDEN_MESSAGE);
            }else if(res == 3){
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.Good_NOT_EXIST);
            }else
            {
                httpResponseEntity.setCode(Constans.RETURN_FAILED_CODE);
                httpResponseEntity.setMessage(Constans.RETURN_FAILED_MESSAGE);
            }

        } catch (Exception e) {
            logger.info("中心库房退回供应商>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/printOutCentral",method = RequestMethod.POST, headers = "Accept"
            + "=application/json")
    public HttpResponseEntity printOutCentral(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo res = centralstationService.printOutCentral(map);
            httpResponseEntity.setData(res);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("打印出库单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @RequestMapping(value = "/printDistribute",method = RequestMethod.POST, headers = "Accept"
            + "=application/json")
    public HttpResponseEntity printDistribute(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo res = centralstationService.printDistribute(map);
            httpResponseEntity.setData(res);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("打印分发单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }


}

