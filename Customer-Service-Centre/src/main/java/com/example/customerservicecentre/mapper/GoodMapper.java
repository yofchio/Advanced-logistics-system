package com.example.customerservicecentre.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.customerservicecentre.entity.Good;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品 Mapper 接口
 * </p>
 *
 * @author
 * @since 2023-06-26
 */
@Mapper
public interface GoodMapper extends BaseMapper<Good> {

}
