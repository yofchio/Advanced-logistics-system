package com.example.distributionmanagementcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.distributionmanagementcenter.entity.Good;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 商品 Mapper 接口
 * </p>
 *
 * @author jason_cai
 * @since 2023-06-19
 */
@Mapper
public interface GoodMapper extends BaseMapper<Good> {

}
