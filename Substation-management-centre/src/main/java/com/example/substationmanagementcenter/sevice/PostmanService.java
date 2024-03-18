package com.example.substationmanagementcenter.sevice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.substationmanagementcenter.entity.Postman;
import com.github.pagehelper.PageInfo;

import java.util.Map;


/**
 * <p>
 * 配送员 服务类
 * </p>
 *
 * @author hzn
 * @since 2023-06-21
 */
public interface PostmanService extends IService<Postman> {

    PageInfo selectAll(Map<String,Object> map);
}
