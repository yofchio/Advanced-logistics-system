package com.example.customerservicecentre.controller;

import com.example.customerservicecentre.beans.HttpResponseEntity;
import com.example.customerservicecentre.common.Constans;
import com.example.customerservicecentre.entity.Orders;
import com.example.customerservicecentre.service.OrderService;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-19
 */
@RestController
@RequestMapping("/customer")
public class OrderAction {

    private final Logger logger = LoggerFactory.getLogger(CustomerAction.class);

    @Autowired
    private OrderService OrdersService;

    @RequestMapping(value = "/addOrder",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity addOrder(@RequestBody Map<String,Object > map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=OrdersService.insert(map);
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
            logger.info("addOrder 添加订单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/getOrdersByCriteria",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity getOrdersByCriteria(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo res=OrdersService.getOrdersByCriteria(map);
                httpResponseEntity.setData(res);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("getOrdersByCriteria 搜索订单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/getCreaterwork",method = RequestMethod.GET, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity getCreaterwork(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();

        try {
            PageInfo res=OrdersService.getWorkByid(map);

            httpResponseEntity.setData(res);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("addOrder 添加订单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/getAllOrder",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity getAllOrder(@RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo res=OrdersService.getAllOrder(map);
            httpResponseEntity.setData(res);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("getAllOrder 返回所有>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/getOrderByid",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity getOrderByid(@RequestBody Long id) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            Orders res=OrdersService.getOrderByid(id);
            httpResponseEntity.setData(res);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("getOrderByid 通过id得到单个order信息>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/changeOrderStatusById",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity changeOrderStatusById(@RequestBody Orders orders) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=OrdersService.updatebyId(orders);
            httpResponseEntity.setData(res);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("changeOrderStatusById 通过id得到改变order的状态>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/addReturn",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity addReturn(@RequestBody  Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=OrdersService.addReturn(map);
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
            logger.info("addReturn 添加退换货单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/checkReturn",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity checkReturn(@RequestBody Orders params) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=OrdersService.checkReturn(params);
            if(res==1)
            {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else if (res==0)
            {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.checkRETURN);
            }

        } catch (Exception e) {
            logger.info("checkReturn 检查是否可以退换货>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }
    @RequestMapping(value = "/addUnsubscribe",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity addUnsubscribe(@RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=OrdersService.addUnsub(map);
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
            logger.info("addUnsubscribe 添加退订单>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }
    /* 检查是否可以退订*/
    @RequestMapping(value = "/checkUnsubscribe",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity checkUnsubscribe(@RequestBody Orders params) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=OrdersService.checkUnsub(params);
            if(res==1)
            {
                httpResponseEntity.setData(res);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else
            {
                httpResponseEntity.setData(res);
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.checkUnsubscribeAction);
            }

        } catch (Exception e) {
            logger.info("checkUnsubscribe 检查是否可以退订>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/getOrderByStationFin",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity getOrderByStationFin(@RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            List<Orders> res=OrdersService.getOrderByStationFin(map);
            if(res!=null)
            {
                httpResponseEntity.setData(res);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else
            {
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.ADD_FAIL);
            }

        } catch (Exception e) {
            logger.info("getOrderByStationFin 多种条件找oderlist>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/getOrderDis",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity getOrderDis(@RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
           PageInfo res=OrdersService.getOrderDis(map);
            if(res!=null)
            {
                httpResponseEntity.setData(res);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else
            {
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.ADD_FAIL);
            }

        } catch (Exception e) {
            logger.info("getOrderDis 调度中心查询>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/updateOrder",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity updateOrder(@RequestBody Orders order) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=OrdersService.updatebyId(order);
            if(res!=0)
            {
                httpResponseEntity.setData(res);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else
            {
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.ADD_FAIL);
            }

        } catch (Exception e) {
            logger.info("getOrderDis 调度中心查询>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/getlack",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity getlack(@RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo res=OrdersService.getlack(map);
            if(res!=null)
            {
                httpResponseEntity.setData(res);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else
            {
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.ADD_FAIL);
            }

        } catch (Exception e) {
            logger.info("getlack 手动调度的订单状态修改>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/addOrderGood",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity addOrderGood(@RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=OrdersService.addOrderGood(map);
            if(res!=0)
            {
                httpResponseEntity.setData(res);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else
            {
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.ADD_FAIL);
            }

        } catch (Exception e) {
            logger.info("getlack 手动调度的订单状态修改>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }


}
