package com.example.substationmanagementcenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.substationmanagementcenter.entity.Receipt;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 回执单 Mapper 接口
 * </p>
 *
 * @author hzn
 * @since 2023-06-21
 */
@Mapper
public interface ReceiptMapper extends BaseMapper<Receipt> {

}
