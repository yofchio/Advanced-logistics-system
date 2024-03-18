package com.example.distributionmanagementcenter.controller;

import com.example.distributionmanagementcenter.entity.*;
import com.example.distributionmanagementcenter.service.BuyService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jason_cai
 * @since 2023-06-19
 */
@RestController
@RequestMapping("/distribute/buy")
public class BuyController {

    private final Logger logger = LoggerFactory.getLogger(BuyController.class);
    @Autowired
    private BuyService buyService;

    @PostMapping(value = "/{id}")
    public HttpResponseEntity<Buy> getById(@PathVariable("id") String id) {
        HttpResponseEntity<Buy> httpResponseEntity = new HttpResponseEntity<Buy>();
        try {
           Buy buy=buyService.getById(id);
            if(buy!=null)
            {
                httpResponseEntity.setData(buy);
            }
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("getById ID查找订单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @PostMapping(value = "/create")
    public HttpResponseEntity<Buy> create(@RequestBody Buy params) {
        HttpResponseEntity<Buy> httpResponseEntity = new HttpResponseEntity<Buy>();
        try {
                buyService.save(params);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("create 新建购货单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @PostMapping(value = "/delete/{id}")
    public HttpResponseEntity<Buy> delete(@PathVariable("id") String id) {

        HttpResponseEntity<Buy> httpResponseEntity = new HttpResponseEntity<Buy>();
        try {
                buyService.removeById(id);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("delete 删除购货单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }


    @PostMapping(value = "/update")
    public HttpResponseEntity<Buy> update(@RequestBody Buy params) {

        HttpResponseEntity<Buy> httpResponseEntity = new HttpResponseEntity<Buy>();
        try{
               buyService.updateById(params);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("update 更新购货单单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @PostMapping(value="/queryByDateSupply")
    public HttpResponseEntity getListByDateSupply(@RequestBody Map<String, Object> map) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try{
            PageInfo pageInfo =buyService.getListByDateSupply(map);
                httpResponseEntity.setData(pageInfo);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);


        } catch (Exception e) {
            logger.info("根据供货商和日期查询订单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @PostMapping(value="/getListByDateSupplyBuyType")
    public HttpResponseEntity getListByDateSupplyBuyType(@RequestBody Map<String, Object> map) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try{

            httpResponseEntity.setData(buyService.getListByDateSupplyBuyType(map));
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("后端专用：根据供货商和日期查询订单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @PostMapping(value="/deleteBuyByGoodId")
    public HttpResponseEntity deleteBuyByGoodId(@RequestBody Map<String, Object> map) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try{
            httpResponseEntity.setData(buyService.deleteBuyByIds(map));
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("根据货物与订单ID删掉购物单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @PostMapping(value="/updateBuyByGoodId")
    public HttpResponseEntity updateBuyByGoodId(@RequestBody Map<String, Object> map) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try{
            httpResponseEntity.setData(buyService.updateBuyByIds(map));
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("根据货物与订单ID修改商品数量>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @PostMapping(value="/changeBuyTypeNotify")
    public HttpResponseEntity changeBuyTypeNotify(@RequestBody Map<String, Object> map) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try{
            httpResponseEntity.setData(buyService.changeBuyTypeNotify(map));
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("支付后的通知修改状态  changeBuyTypeNotify>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @PostMapping(value="/getList1")

    public HttpResponseEntity getListByConditions1(@RequestBody Map<String, Object> map) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            httpResponseEntity.setData(buyService.getList(map));
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("后端专用：根据库房类型，商品名称，日期，出库类型查询出入库记录>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @PostMapping(value="/withdrawal")

    public HttpResponseEntity withdrawal(@RequestBody Map<String, Object> map) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            httpResponseEntity.setData(buyService.withdrawal(map));
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("退货>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }


//    @PostMapping(value="/withdrawal1")
//    public HttpResponseEntity withdrawal1(@RequestBody Map<String, Object> map) {
//
//        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
//        try {
//            httpResponseEntity.setData(buyService.withdrawalConfirm(map));
//            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
//            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
//
//        } catch (Exception e) {
//            logger.info("中心站退货>>>>>>>>>>>" + e.getLocalizedMessage());
//            httpResponseEntity.setCode(Constans.EXIST_CODE);
//            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
//        }
//        return httpResponseEntity;
//    }




}
