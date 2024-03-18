package com.example.warehousemanagementcentre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.warehousemanagementcentre.entity.Allocation;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品调拨 Mapper 接口
 * </p>
 *
 * @author yangfuchao
 * @since 2023-06-25
 */
@Mapper
public interface AllocationMapper extends BaseMapper<Allocation> {

}
