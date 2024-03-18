package com.example.distributionmanagementcenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.distributionmanagementcenter.entity.CentralStation;
import com.example.distributionmanagementcenter.entity.FirstCategory;
import com.example.distributionmanagementcenter.entity.Good;
import com.example.distributionmanagementcenter.mapper.FirstCategoryMapper;
import com.example.distributionmanagementcenter.service.FirstCategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jason_cai
 * @since 2023-06-19
 */
@Service
@Transactional(rollbackFor=Exception.class)
public class FirstCategoryServiceImpl extends ServiceImpl<FirstCategoryMapper, FirstCategory> implements FirstCategoryService {
    @Autowired
    private FirstCategoryMapper firstCategoryMapper;
    @Override
    public PageInfo getList(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        QueryWrapper<FirstCategory> queryWrapper = new QueryWrapper<>();
        if((String) map.get("keywords")!=null&& !Objects.equals((String) map.get("keywords"), "")){
            String pattern = (String) map.get("keywords");
            queryWrapper.like("f_name",pattern);
        }
        List<FirstCategory> records= firstCategoryMapper.selectList(queryWrapper);
        PageInfo pageInfo = new PageInfo(records);
        return pageInfo;
    }
}
