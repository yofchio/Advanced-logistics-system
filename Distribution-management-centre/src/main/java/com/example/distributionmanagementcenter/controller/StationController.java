package com.example.distributionmanagementcenter.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.distributionmanagementcenter.entity.*;
//import com.example.distributionmanagementcenter.feign.FeignApi;
import com.example.distributionmanagementcenter.service.CentralstationService;
import com.example.distributionmanagementcenter.service.StationService;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/distribute/station")
public class StationController {
    private final Logger logger = LoggerFactory.getLogger(StationController.class);
    @Autowired
    private StationService stationService;
    @Autowired
    private CentralstationService centralstationService;
//    @Autowired
//    private FeignApi feignApi;

    @PostMapping(value = "/{id}")
    public HttpResponseEntity<Station> getById(@PathVariable("id") String id) {
        HttpResponseEntity<Station> httpResponseEntity = new HttpResponseEntity<Station>();
        try {
            Station station=stationService.getById(id);
            if(station!=null)
            {
                httpResponseEntity.setData(station);
            }
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("getById ID查找站>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @PostMapping(value = "/getList")
    public HttpResponseEntity getList(@RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo pageInfo=stationService.getList(map);
            httpResponseEntity.setData(pageInfo);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("查找库房列表" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @PostMapping(value = "/create")
    public HttpResponseEntity create(@RequestBody Station params) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {

            int flag=0;
            List<Station> stationList =stationService.list();
            for(Station station:stationList){
                if(params.getName().equals(station.getName())){
                    flag=1;
                }
            }
            if(flag==0){
                stationService.save(params);
                httpResponseEntity.setData("创建成功！");

            }
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("create 新建站>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }


    @PostMapping(value = "/delete/{id}")
    public HttpResponseEntity<Station> delete(@PathVariable("id") String id) {

        HttpResponseEntity<Station> httpResponseEntity = new HttpResponseEntity<Station>();
        try {
            stationService.removeById(id);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);


        } catch (Exception e) {
            logger.info("delete 删除站>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @PostMapping(value = "/update")
    public HttpResponseEntity update(@RequestBody Station params) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
                int flag=0;
                 List<Station> stationList =stationService.list();
                 for(Station station:stationList){
                     if(params.getName().equals(station.getName())&&params.getId()!=station.getId()){
                         flag=1;
                     }
                 }
                 if(flag==0){
                     stationService.updateById(params);
                     httpResponseEntity.setData("更新成功");
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

    //库存量查询
//@RequestMapping(value = "/stock/{sid}/{cid}",method = RequestMethod.POST, headers = "Accept"
//        + "=application/json")
//public HttpResponseEntity  stockQuery(@PathVariable("sid") String sid,@PathVariable("cid") String cid) {
//    HttpResponseEntity  httpResponseEntity = new HttpResponseEntity();
//    try {
//        Station station = stationService.getById(sid);
//        CentralStation centralStation= centralstationService.getById(cid);
//        HashMap<String,Object> responseContent=new HashMap<String,Object>();
//
//        responseContent.put("StationName",station.getName());
//        responseContent.put("GoodName",centralStation.getGoodName());
//        Long allStock=centralStation.getWaitAllo()+centralStation.getWithdrawal()+centralStation.getDoneAllo();
//        responseContent.put("AllStock",allStock);
//        responseContent.put("Withdrawal",centralStation.getWithdrawal());
//        responseContent.put("DoneAllo",centralStation.getDoneAllo());
//        responseContent.put("WaitAllo",centralStation.getWaitAllo());
//        httpResponseEntity.setData(responseContent);
//        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
//        httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
//
//
//    } catch (Exception e) {
//        logger.info("stockQuery 库存量查询>>>>>>>>>>>" + e.getLocalizedMessage());
//        httpResponseEntity.setCode(Constans.EXIST_CODE);
//        httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
//    }
//    return httpResponseEntity;
//}

    @RequestMapping(value = "/stationInOut/{sid}/{gid}",method = RequestMethod.POST, headers = "Accept"
            + "=application/json")
    public HttpResponseEntity StationInOutQuery(@PathVariable("sid") String sid, @PathVariable("gid") String gid, @RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            Station station = stationService.getById(sid);
            HashMap<String,Object> responseContent=new HashMap<String,Object>();
            responseContent.put("StationName",station.getName());
            responseContent.put("RecordList",stationService.stationInOutQueryService(map));
            httpResponseEntity.setData(responseContent);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);


        } catch (Exception e) {
            logger.info("StationInOutQuery 出入库单查询>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/withdrawalQuery",method = RequestMethod.POST, headers = "Accept"
            + "=application/json")
    public HttpResponseEntity withdrawalQuery(@RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo pageInfo =stationService.withdrawalQueryBuyService(map);
            HashMap<String,Object> responseContent=new HashMap<String,Object>();
            responseContent.put("buy",pageInfo);
            for(Buy buy:(List<Buy>)pageInfo.getList()){
           CentralStation centralStation= centralstationService.getById(buy.getGoodId());
                responseContent.put("withdrawal"+centralStation.getGoodName(),centralStation.getWithdrawal());
                responseContent.put("WaitAllocation"+centralStation.getGoodName(),centralStation.getWaitAllo());
             }
            httpResponseEntity.setData(responseContent);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("Withdrawal 退货管理查询>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    //退货确认，直接修改GOOD属性即可
//    @RequestMapping(value = "/withdrawalConfirm",method = RequestMethod.POST, headers = "Accept"
//            + "=application/json")
//    public HttpResponseEntity withdrawalConfirm(@RequestBody Map<String, Object> map) {
//        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
//        try {
//            PageInfo pageInfo =stationService.withdrawalQueryBuyService(map);
//            HashMap<String,Object> responseContent=new HashMap<String,Object>();
//            responseContent.put("buy",pageInfo);
//            for(Buy buy:(List<Buy>)pageInfo.getList()){
//                CentralStation centralStation= centralstationService.getById(buy.getGoodId());
//                responseContent.put("withdrawal"+centralStation.getGoodName(),centralStation.getWithdrawal());
//                responseContent.put("WaitAllocation"+centralStation.getGoodName(),centralStation.getWaitAllo());
//            }
//            httpResponseEntity.setData(responseContent);
//            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
//            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
//
//        } catch (Exception e) {
//            logger.info("Withdrawal 退货管理查询>>>>>>>>>>>" + e.getLocalizedMessage());
//            httpResponseEntity.setCode(Constans.EXIST_CODE);
//            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
//        }
//        return httpResponseEntity;
//    }

        @RequestMapping(value = "/stationAnalyze",method = RequestMethod.POST, headers = "Accept"
            + "=application/json")
    public HttpResponseEntity stationAnalyze(@RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            httpResponseEntity.setData(stationService.stationAnalyze(map));
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("分站配送情况分析>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

//    @RequestMapping(value = "/exportExcel",method = RequestMethod.POST, headers = "Accept"
//            + "=application/json")
//    public void exportExcel(HttpServletResponse response) throws IOException {
//        String fileName = "Receipt.xlsx";
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
//
//        HttpResponseEntity<List<Receipt>> httpResponseEntity1 = feignApi.getReceiptList();
//
//      List<Receipt> list= httpResponseEntity1.getData();
//        EasyExcel.write(response.getOutputStream(), Receipt.class).sheet("ReceiptList")
//                .doWrite(list);
//    }
}
