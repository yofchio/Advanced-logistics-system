package com.example.customerservicecentre.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.customerservicecentre.beans.HttpResponseEntity;
import com.example.customerservicecentre.common.Constans;
import com.example.customerservicecentre.common.utils.UUIDUtil;
import com.example.customerservicecentre.entity.Customer;
import com.example.customerservicecentre.service.CustomerService;
import com.example.customerservicecentre.service.OrderService;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户表信息 前端控制器
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-19
 */
@RestController
@RequestMapping("/customer")
public class CustomerAction {
    private final Logger logger = LoggerFactory.getLogger(CustomerAction.class);

    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;


    @RequestMapping(value = "/addUser",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity addUser(@RequestBody Customer params) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=customerService.insert(params);
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
            logger.info("addUser 添加客户>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }
    @RequestMapping(value = "/updateUser",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity updateUser(@RequestBody Customer params) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=customerService.updatebyId(params);
            if(res==1)
            {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else
            {
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.UPDATE_FAIL);
            }
        } catch (Exception e) {
            logger.info("updateUser 更新客户信息>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }
    @RequestMapping(value = "/selectAllUser",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity selectAllUser(@RequestBody Map<String,Object> map) {
        System.out.println(map+"\n\n\n\n");
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo pageInfo= customerService.selectAll(map);
            System.out.println(pageInfo+"\n\n\n\n");
            httpResponseEntity.setData(pageInfo);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);


        } catch (Exception e) {
            logger.info("updateUser 更新客户信息>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/delectUserByid",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity delectUserByid(@RequestBody Customer params) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        System.out.println(params);
        try {
            int res = customerService.deletebyId(params.getId());
            if(res==1)
            {
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else
            {
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.DELETE_FAIL);
            }

        } catch (Exception e) {
            logger.info("delectUserByid 删除客户信息>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/search",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity search(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo pageInfo = customerService.searchbykey(map);
                httpResponseEntity.setData(pageInfo);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("search 搜索客户信息>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/selectOrderbyCustomer",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity selectOrderbyCustomer(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo pageInfo = orderService.selectOrderbyCustomer(map);
            httpResponseEntity.setData(pageInfo);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("selectOrderbyCustomer 客户订单信息>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }
    @RequestMapping(value = "/selectByCustomerId",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity selectByCustomerId(@RequestBody Long Id) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            Customer customer = customerService.selectbyId(Id);
            httpResponseEntity.setData(customer);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("selectByCustomerId 通过id得到客户>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }
    @RequestMapping(value = "/getUUId",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity gteUUId() {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            String res= UUID.randomUUID().toString().substring(0,8);
            httpResponseEntity.setData(res);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("getUUId getuuid>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/getUserByUserId",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity getUserByUserId(@RequestBody int Id) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            Customer res = customerService.getUserByUserId(Id);
            httpResponseEntity.setData(res);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("search 搜索客户信息>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.null_MESSAGE);
        }
        return httpResponseEntity;
    }

}
