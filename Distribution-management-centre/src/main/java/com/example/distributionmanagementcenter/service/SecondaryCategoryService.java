package com.example.distributionmanagementcenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.distributionmanagementcenter.entity.SecondaryCategory;
import com.github.pagehelper.PageInfo;

import java.text.ParseException;
import java.util.Map;

public interface SecondaryCategoryService extends IService<SecondaryCategory> {
    PageInfo getList(Map<String, Object> map) throws ParseException;
}