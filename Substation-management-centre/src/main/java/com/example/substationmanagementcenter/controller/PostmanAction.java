package com.example.substationmanagementcenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.substationmanagementcenter.beans.HttpResponseEntity;
import com.example.substationmanagementcenter.common.Constans;
import com.example.substationmanagementcenter.entity.Postman;
import com.example.substationmanagementcenter.sevice.PostmanService;
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
 * 配送员 前端控制器
 * </p>
 *
 * @author hzn
 * @since 2023-06-21
 */
@RestController
@RequestMapping("/substation/postman")
public class PostmanAction {

    private final Logger logger = LoggerFactory.getLogger(TaskAction.class);

    @Autowired
    private PostmanService postmanService;

    @RequestMapping(value = "/selectAllPostman",method = RequestMethod.POST, headers = "Accept"
            + "=application/json")
    public HttpResponseEntity selectAllPostman(@RequestBody Map<String,Object> map){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            PageInfo pageInfo= postmanService.selectAll(map);
            httpResponseEntity.setData(pageInfo);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);


        } catch (Exception e) {
            logger.info("selectAllPostman 展示投递员信息>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
}
