package com.example.substationmanagementcenter.sevice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.substationmanagementcenter.entity.Postman;
import com.example.substationmanagementcenter.entity.Task;
import com.example.substationmanagementcenter.mapper.PostmanMapper;
import com.example.substationmanagementcenter.sevice.PostmanService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 配送员 服务实现类
 * </p>
 *
 * @author hzn
 * @since 2023-06-21
 */
@Service
public class PostmanServiceImpl extends ServiceImpl<PostmanMapper, Postman> implements PostmanService {

    @Autowired
    private PostmanMapper postmanMapper;


    @Override
    public PageInfo selectAll(Map<String, Object> map) {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));
        List<Postman> res= postmanMapper.selectList(null);
        PageInfo pageInfo = new PageInfo(res);
        return pageInfo;
    }
}
