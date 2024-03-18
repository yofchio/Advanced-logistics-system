package com.example.distributionmanagementcenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.distributionmanagementcenter.entity.FirstCategory;
import com.github.pagehelper.PageInfo;

import java.text.ParseException;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jason_cai
 * @since 2023-06-19
 */
public interface FirstCategoryService extends IService<FirstCategory> {
    PageInfo getList(Map<String, Object> map) throws ParseException;
}
