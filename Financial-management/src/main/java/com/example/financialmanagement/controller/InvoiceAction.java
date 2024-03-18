package com.example.financialmanagement.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.financialmanagement.beans.HttpResponseEntity;
import com.example.financialmanagement.common.Constans;
import com.example.financialmanagement.entity.Invoice;
import com.example.financialmanagement.entity.Use;
import com.example.financialmanagement.entity.vo.ResultStation;
import com.example.financialmanagement.service.InvoiceService;
import com.example.financialmanagement.service.UseService;
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
 * 发票 前端控制器
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-20
 */
@RestController
@RequestMapping("/financial")
public class InvoiceAction {

    private final Logger logger = LoggerFactory.getLogger(FinancialAction.class);
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private UseService useService;
    @RequestMapping(value = "/addInvoice",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity addInvoice(@RequestBody Invoice invoice) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        System.out.println(invoice);
        try {
            String res=invoiceService.addInvoice(invoice);
            if(res=="OK")
            {
                httpResponseEntity.setData(res);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else
            {
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(res);
            }

        } catch (Exception e) {
            logger.info("invoiceService 添加发票>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/addUseInvoice",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity addUseInvoice(@RequestBody Use use) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=useService.addUseInvoice(use);
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
            logger.info("addUseInvoice 添加使用的发票>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

//    @SentinelResource(value = "two")
    @RequestMapping(value = "/selectInvoice",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity selectInvoice(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        System.out.println(map);
        try {
            PageInfo res=invoiceService.selectInvoice(map);
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
            logger.info("addUseInvoice 添加使用的发票>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @SentinelResource(value = "ten")
    @RequestMapping(value = "/selectUseInvoice",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity selectUseInvoice(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        System.out.println(map);
        try {
            PageInfo res=useService.select(map);
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
            logger.info("selectUseInvoice 搜索使用的发票>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/addReceiptInvoice",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity addReceiptInvoice(@RequestBody Use use) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            Long res=useService.addReceiptInvoice(use);
            System.out.println(res);
            if(res!=0L)
            {
                httpResponseEntity.setData(res);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else
            {
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.INVOICE_FAIL);
            }

        } catch (Exception e) {
            logger.info("selectUseInvoice 搜索使用的发票>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @RequestMapping(value = "/changeUseByid",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity changeUseByid(@RequestBody Use use) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=useService.changeUseByid(use);
            if(res==1)
            {
                httpResponseEntity.setData(res);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else
            {
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.INVOICE_FAIL);
            }

        } catch (Exception e) {
            logger.info("changeUseByid 搜索使用的发票>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @RequestMapping(value = "/setPutAway",method = RequestMethod.POST, headers = "Accept"
        + "=application/json")
    public HttpResponseEntity setPutAway(@RequestBody Long id) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int res=useService.setPutAway(id);
            if(res==1)
            {
                httpResponseEntity.setData(res);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
            }else
            {
                httpResponseEntity.setCode(Constans.EXIST_CODE);
                httpResponseEntity.setMessage(Constans.INVOICE_FAIL);
            }

        } catch (Exception e) {
            logger.info("changeUseByid 搜索使用的发票>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

}
