package com.example.dispatchcentre.entity.vo;

import com.example.dispatchcentre.entity.Good;
import com.example.dispatchcentre.feign.Task;
import com.github.pagehelper.PageInfo;
import lombok.Data;

@Data
/**
 * @author YANG FUCHAO
 * @version 1.0
 * @date 2023-07-11 11:45
 */
public class Delivery {
private PageInfo<Good> pageInfo;
private Task task;

}
