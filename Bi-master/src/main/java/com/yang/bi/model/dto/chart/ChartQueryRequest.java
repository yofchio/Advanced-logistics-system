package com.yang.bi.model.dto.chart;

import com.yang.bi.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 * @author YANG FUCHAO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ChartQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表名称
     */
    private String chartName;


    /**
     * 图表类型
     */
    private String chartType;

    /**
     * 图表类型
     */
    private String chartStatus;

    /**
     * 创建图标用户 id
     */
    private String creator;


    private static final long serialVersionUID = 1L;
}
