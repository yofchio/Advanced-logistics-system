package com.example.warehousemanagementcentre.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.warehousemanagementcentre.entity.Station;
import com.example.warehousemanagementcentre.mapper.StationMapper;
import com.example.warehousemanagementcentre.service.StationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库房出库 服务实现类
 * </p>
 *
 * @author hzn
 * @since 2023-06-21
 */
@Service
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> implements StationService {

    @Autowired
    private StationMapper stationMapper;




    @Override
    public PageInfo selectAll(Map<String, Object> map) {
        PageHelper.startPage(Integer.valueOf((String)map.get("pageNum")),
                Integer.valueOf((String)map.get("pageSize")));
        List<Station> res= stationMapper.selectList(null);
        PageInfo pageInfo = new PageInfo(res);
        return pageInfo;
    }

//    @Override
//    public PageInfo getInStation(Map<String, Object> map) {
//        PageHelper.startPage(Integer.valueOf((String)map.get("pageNum")),
//                Integer.valueOf((String)map.get("pageSize")));
//        Integer id = (Integer) map.get("id");
//        Long lid = id.longValue();
//        HttpResponseEntity res= distributionFeign.getInStation(lid);
//        PageInfo pageInfo = new PageInfo((List<Station>)res.getData());
//        return pageInfo;
//    }

    @Override
    public int updatebyId(Station station) {
        int res = stationMapper.updateById(station);
        return res;
    }
}
