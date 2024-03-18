package com.example.distributionmanagementcenter.controller;

import com.example.distributionmanagementcenter.entity.Constans;
import com.example.distributionmanagementcenter.entity.FirstCategory;
import com.example.distributionmanagementcenter.entity.HttpResponseEntity;
import com.example.distributionmanagementcenter.entity.SecondaryCategory;
import com.example.distributionmanagementcenter.service.FirstCategoryService;
import com.example.distributionmanagementcenter.service.SecondaryCategoryService;
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
@RequestMapping("/distribute/secondarycategory")
public class SecondaryCategoryController {

    private final Logger logger = LoggerFactory.getLogger(FirstCategoryController.class);
    @Autowired
    private SecondaryCategoryService secondaryCategoryService;

    @PostMapping(value = "/{id}")
    public HttpResponseEntity<SecondaryCategory> getById(@PathVariable("id") String id) {
        HttpResponseEntity<SecondaryCategory> httpResponseEntity = new HttpResponseEntity<SecondaryCategory>();
        try {
            SecondaryCategory secondaryCategory = secondaryCategoryService.getById(id);
            if(secondaryCategory !=null)
            {
                httpResponseEntity.setData(secondaryCategory);

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
    public HttpResponseEntity create(@RequestBody SecondaryCategory params) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            int flag=0;
            List<SecondaryCategory> secondaryCategoryList = secondaryCategoryService.list();
            for(SecondaryCategory secondaryCategory : secondaryCategoryList){
                if(secondaryCategory.getSName().equals(params.getSName())){
                    if(secondaryCategory.getFId()==params.getFId()){
                        flag=1;
                        break;
                    }
                }
            }
            if(flag==0){
                secondaryCategoryService.save(params);
                httpResponseEntity.setData("插入成功！");
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
    public HttpResponseEntity delete(@PathVariable("id") String id) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
              secondaryCategoryService.removeById(id);
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
    public HttpResponseEntity update(@RequestBody SecondaryCategory params) {

        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try{
            int flag=0;
            List<SecondaryCategory> secondaryCategoryList = secondaryCategoryService.list();
            for(SecondaryCategory secondaryCategory : secondaryCategoryList){
                if(secondaryCategory.getSName().equals(params.getSName())&&secondaryCategory.getId()!=params.getId()){
                    if(secondaryCategory.getFId()==params.getFId()){
                        flag=1;
                        break;
                    }
                }
            }
            if(flag==0){
                 secondaryCategoryService.updateById(params);
                httpResponseEntity.setData("插入成功！");
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
            PageInfo pageInfo= secondaryCategoryService.getList(map);
            httpResponseEntity.setData(pageInfo);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.STATUS_MESSAGE);

        } catch (Exception e) {
            logger.info("二级级商品种类列表>>>>>>>>>>>" + e.getLocalizedMessage());
            httpResponseEntity.setCode(Constans.EXIST_CODE);
            httpResponseEntity.setMessage(Constans.EXIST_MESSAGE);
        }
        return httpResponseEntity;
    }
}
