package com.example.distributionmanagementcenter.controller;

import com.example.distributionmanagementcenter.entity.FirstCategory;
import com.example.distributionmanagementcenter.entity.Constans;
import com.example.distributionmanagementcenter.entity.HttpResponseEntity;
import com.example.distributionmanagementcenter.service.FirstCategoryService;
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
@RequestMapping("/distribute/firstcategory")
public class FirstCategoryController {

    private final Logger logger = LoggerFactory.getLogger(FirstCategoryController.class);
    @Autowired
    private FirstCategoryService firstCategoryService;



    @PostMapping(value = "/{id}")
    public HttpResponseEntity<FirstCategory> getById(@PathVariable("id") String id) {
        HttpResponseEntity<FirstCategory> httpResponseEntity = new HttpResponseEntity<FirstCategory>();
        try {
            FirstCategory firstCategory = firstCategoryService.getById(id);
            if(firstCategory !=null)
            {
                httpResponseEntity.setData(firstCategory);

            }
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("getById ID查找种类>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
//确定插入值唯一
    @PostMapping(value = "/create")
    public HttpResponseEntity create(@RequestBody FirstCategory params) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int flag=0;
            List<FirstCategory> firstCategoryList = firstCategoryService.list();
            for(FirstCategory firstCategory : firstCategoryList){
                if(firstCategory.getFName().equals(params.getFName())){
                    flag=1;
                    break;
                }
            }
            if(flag==0){
                firstCategoryService.save(params);
                httpResponseEntity.setData("新增成功");
            }
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("create 新建种类>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }

    @PostMapping(value = "/delete/{id}")
    public HttpResponseEntity<FirstCategory> delete(@PathVariable("id") String id) {

        HttpResponseEntity<FirstCategory> httpResponseEntity = new HttpResponseEntity<FirstCategory>();
        try {
             firstCategoryService.removeById(id);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);


        } catch (Exception e) {
            logger.info("delete 删除种类>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }


    @PostMapping(value = "/update")
    public HttpResponseEntity update(@RequestBody FirstCategory params) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try{
            int flag=0;
            List<FirstCategory> firstCategoryList = firstCategoryService.list();
            for(FirstCategory firstCategory : firstCategoryList){
                if(firstCategory.getFName().equals(params.getFName())&&firstCategory.getId()!=params.getId()){
                    flag=1;
                    break;
                }
            }
            if(flag==0){
                firstCategoryService.updateById(params);
                httpResponseEntity.setData("修改成功");
            }

            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("update 更新种类>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
    @PostMapping(value = "/getList")
    public HttpResponseEntity getList(@RequestBody Map<String, Object> map) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try{
                PageInfo pageInfo= firstCategoryService.getList(map);
                httpResponseEntity.setData(pageInfo);
                httpResponseEntity.setCode(Constans.SUCCESS_CODE);
                httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);
        } catch (Exception e) {
            logger.info("一级商品种类列表>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
}
