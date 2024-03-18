package com.example.warehousemanagementcentre.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.warehousemanagementcentre.beans.HttpResponseEntity;
import com.example.warehousemanagementcentre.entity.*;
import com.example.warehousemanagementcentre.entity.vo.AllDistribute;
import com.example.warehousemanagementcentre.entity.vo.AllOutStation;
import com.example.warehousemanagementcentre.entity.vo.OutStationNote;
import com.example.warehousemanagementcentre.entity.vo.ResultInCentral;
import com.example.warehousemanagementcentre.feign.FeignApi;
import com.example.warehousemanagementcentre.mapper.AllocationMapper;
import com.example.warehousemanagementcentre.mapper.BuyMapper;
import com.example.warehousemanagementcentre.mapper.CentralStationMapper;
import com.example.warehousemanagementcentre.mapper.InoutstationMapper;
import com.example.warehousemanagementcentre.service.CentralstationService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 中心库房存量  服务实现类
 * </p>
 *
 * @author hzn
 * @since 2023-06-25
 */
@Service
public class CentralStationServiceImpl extends ServiceImpl<CentralStationMapper, CentralStation> implements CentralstationService {

    @Autowired
    private CentralStationMapper centralStationMapper;

    @Autowired
    private InoutstationMapper inoutstationMapper;

    @Autowired
    private AllocationMapper allocationMapper;

    @Autowired
    private BuyMapper buyMapper;


    @Autowired
    private FeignApi feignApi;


    //新建一个ResultInCentral类，用于给前端返回两表的综合信息
    //由前端传回的“id”找到对应的buy单再判断是不是没有进货过buy单,若“id”为空，则通过微服务查找所有的buy单，再遍历它们，筛选出所有需要购货入库的buy单
    //遍历符合条件的buy单，得到每一个buy单对应的中心库房的商品，再得到每一个对应上品的一级和二级类别
    //综合buy和中心库房的信息及对应商品的一级和二级分类信息，形成ResultInCentral类的列表，通过PageInfo返回前端
    @Override
    public PageInfo searchInCentral(Map<String, Object> map) {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));

        List<ResultInCentral> resultInCentrals = new ArrayList<>();
        List<Buy> buys = new ArrayList<>();

        if (map.get("id") != null && !map.get("id").equals("")){
            System.out.println("map1"+map);
            //通过buy_id得到购物单信息
            HttpResponseEntity resBuy1 = feignApi.selectBuy(String.valueOf(map.get("id")));
            if (resBuy1.getData() != null){
                String jsonString1 = JSON.toJSONString(resBuy1.getData());  // 将对象转换成json格式数据
                JSONObject jsonObject1 = JSON.parseObject(jsonString1); // 在转回去
                Buy buy = JSON.parseObject(String.valueOf(jsonObject1), Buy.class);
                if(buy.getType() == 2){
                    buys.add(buy);
                }
            }

        }else {
            //没传buyId
            HttpResponseEntity resBuys = feignApi.getListByConditions1(map);
            String jsonString2 = JSON.toJSONString(resBuys.getData());  // 将对象转换成json格式数据
            System.out.println(jsonString2);
            JSONArray jsonArray2 = JSON.parseArray(jsonString2); // 在转回去
            buys = JSON.parseArray(String.valueOf(jsonArray2), Buy.class);
            List<Buy> buys1 = new ArrayList<>();
            for(Buy buy:buys){
                if(buy.getType() == 2){
                    buys1.add(buy);
                }
            }
            buys = buys1;
            System.out.println(buys);
        }

        for (Buy buy:buys){
            CentralStation centralStation = centralStationMapper.selectById(buy.getGoodId());
            System.out.println("centralStation"+centralStation);
            if (centralStation != null){
                ResultInCentral resultInCentral = new ResultInCentral();
                resultInCentral.setBuyId(buy.getId());
                resultInCentral.setCentralGoodId(centralStation.getId());
                resultInCentral.setSupply(centralStation.getSupplyId());
                //buy-购货日期
                resultInCentral.setBuyDate(buy.getDate());
                resultInCentral.setGoodName(centralStation.getGoodName());

                HttpResponseEntity resFirst = feignApi.getById1(String.valueOf(centralStation.getGoodClassId()));
                String jsonString1 = JSON.toJSONString(resFirst.getData());  // 将对象转换成json格式数据
                JSONObject jsonObject1 = JSON.parseObject(jsonString1); // 在转回去
                FirstCategory firstCategory = JSON.parseObject(String.valueOf(jsonObject1), FirstCategory.class);
                HttpResponseEntity resSecond = feignApi.getById2(String.valueOf(centralStation.getGoodSubclassId()));
                String jsonString2 = JSON.toJSONString(resSecond.getData());  // 将对象转换成json格式数据
                JSONObject jsonObject2 = JSON.parseObject(jsonString2); // 在转回去
                SecondaryCategory secondaryCategory = JSON.parseObject(String.valueOf(jsonObject2), SecondaryCategory.class);

                resultInCentral.setGoodClass(firstCategory.getFName());
                resultInCentral.setGoodSubclass(secondaryCategory.getSName());
                resultInCentral.setGoodUnit(centralStation.getGoodUnit());
                resultInCentral.setBuyNumber(buy.getNumber());
                resultInCentrals.add(resultInCentral);
                System.out.println(resultInCentral);
            }

        }

        //设置给前端的返回值
        PageInfo pageInfo = new PageInfo(resultInCentrals);
        System.out.println("pageInfo"+pageInfo);
        return pageInfo;
    }

    //通过前端传回的“buyId”得到前端选择的buy单，由此得到对应商品在中心库房的库存等信息
    //根据前端传来的实际入库数量，修改中心库房的对应商品的库存信息
    //根据以上信息进行inoutStation表的生成,并将其状态设置为"调拨出库"，以提示库房管理人员有这些商品需要出库
    //将对应buy单的状态设置为已购货，再将以上生成的inoutStation插入数据库
    @Override
    public int inCentral(Map<String, Object> map) {
        ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
        // 格式化中国时区时间为指定格式的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int res = 1;
        Buy buy = buyMapper.selectById(String.valueOf(map.get("buyId")));

        //入库改库存
        CentralStation centralStation = centralStationMapper.selectById(String.valueOf(map.get("goodId")));
        centralStation.setWaitAllo(centralStation.getWaitAllo()+Long.valueOf(String.valueOf(map.get("number"))));
        System.out.println("number!!!"+map.get("number"));
        centralStation.setStock(centralStation.getWaitAllo()+centralStation.getWithdrawal()+centralStation.getDoneAllo());
        int res1 = centralStationMapper.updateById(centralStation);
        //库存修改失败
        if(res1 != 1){
            res = 2;
        }
        System.out.println(res1);

        //入库加入库表
        Inoutstation inoutstation = new Inoutstation();
        inoutstation.setStationClass(1);
        inoutstation.setAlloId(Long.valueOf(0));
        inoutstation.setStationId(Long.valueOf(1));
        inoutstation.setStationName("中心库房");
        inoutstation.setTaskId(Long.valueOf(0));
        inoutstation.setGoodId(Long.valueOf(centralStation.getId()));
        inoutstation.setGoodPrice(centralStation.getGoodPrice());
        inoutstation.setGoodName(centralStation.getGoodName());
        inoutstation.setGoodUnit(centralStation.getGoodUnit());
        inoutstation.setGoodFactory(String.valueOf(map.get("supply")));
        inoutstation.setSigner(String.valueOf(map.get("signer")));
        inoutstation.setNumber(Long.valueOf(String.valueOf(map.get("number"))));


        String date = LocalDateTime.parse(String.valueOf(map.get("date")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
        try {
            inoutstation.setDate(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //修改购货单信息
        buy.setType(Integer.parseInt("3"));
        int res3 = buyMapper.updateById(buy);

        inoutstation.setType("调拨出库");
        inoutstation.setRemark((String) map.get("remark"));
        int res4 = inoutstationMapper.insert(inoutstation);

//        if(res3*res4 != 1){
//            res = 0;
//        }
        return res;
    }

    //转化传入的goods列表，通过“alloId”得到对应的的调拨单
    //遍历goods,通过GoodId得到对应商品的中心库房的库存信息，用if判断库存量是否足够，如果库存足够就进行出库，修改中心库房的库存
    //出库的同时综合以上几个表的信息形成inoutStation单，并将其状态设置为"调拨入库"，以提示库房管理人员有这些商品需要入库到分站库房
    //改任务单订单和调拨单状态,将订单状态改为“中心库房出库”，将调拨单状态改为“2”，表示已调拨出库
    @Override
    public int outCentral(Map<String, Object> map) {
        ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
        // 格式化中国时区时间为指定格式的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //转化传入货物表信息
        String jsonString1 = JSON.toJSONString(map.get("goods"));  // 将对象转换成json格式数据
        JSONArray jsonArray2 = JSON.parseArray(jsonString1); // 在转回去
        List<Good> goods = JSON.parseArray(String.valueOf(jsonArray2), Good.class);
        System.out.println("goods###"+goods);

        QueryWrapper<Allocation> allocationQueryWrapper = new QueryWrapper<>();
        allocationQueryWrapper.eq("id", map.get("alloId"));
        List<Allocation> allocations = allocationMapper.selectList(allocationQueryWrapper);
        Allocation allocation = allocations.get(0);

        //goodlist依次入库
        int res = 0;
        for(Good good:goods){
            //中心库存中对应的数据
            QueryWrapper<CentralStation> centralStationQueryWrapper = new QueryWrapper<>();
            centralStationQueryWrapper.eq("id", good.getGoodId());
            System.out.println("good.getGoodId()!!!"+good.getGoodId());
            List<CentralStation> centralStations = centralStationMapper.selectList(centralStationQueryWrapper);
            CentralStation centralStation = centralStations.get(0);
            if(centralStation == null){
                //找不到对应商品
                res = 3;
            }else {
                System.out.println("good???"+good);
                System.out.println("good.getGoodNumber()!!"+good.getGoodNumber());
                if(centralStation.getWaitAllo() >= good.getGoodNumber()){
                    //可分配库存足够，依次出库
                    centralStation.setDoneAllo(centralStation.getDoneAllo()+good.getGoodNumber());
                    centralStation.setWaitAllo(centralStation.getWaitAllo()-good.getGoodNumber());
                    centralStation.setStock(centralStation.getWaitAllo()+centralStation.getWithdrawal()+centralStation.getDoneAllo());
                    res = centralStationMapper.updateById(centralStation);
                    System.out.println("res"+res);

                    //出库加出库表
                    Inoutstation inoutstation = new Inoutstation();
                    inoutstation.setStationClass(1);
                    inoutstation.setAlloId(Long.valueOf(String.valueOf(map.get("alloId"))));
                    inoutstation.setStationId(allocation.getOutStationId());
                    inoutstation.setStationName(allocation.getOutStationName());
                    inoutstation.setTaskId(allocation.getTaskId());
                    inoutstation.setGoodId(Long.valueOf(centralStation.getId()));
                    inoutstation.setGoodPrice(centralStation.getGoodPrice());
                    inoutstation.setGoodName(centralStation.getGoodName());
                    inoutstation.setGoodUnit(centralStation.getGoodUnit());
                    inoutstation.setGoodFactory(good.getGoodFactory());
                    inoutstation.setNumber(Long.valueOf(good.getGoodNumber()));

                    //设置时间
                    String dateTime = LocalDateTime.parse(String.valueOf(map.get("date")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                            ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(dateTime);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    inoutstation.setDate(date);
                    //设置状态
                    inoutstation.setType("调拨入库");
                    inoutstation.setSigner(String.valueOf(map.get("signer")));
                    inoutstation.setDistributor(String.valueOf(map.get("distributor")));
                    inoutstation.setRemark(String.valueOf(map.get("remark")));
                    int insertRes = inoutstationMapper.insert(inoutstation);



                    //改订单状态
                    Map map1 =new HashMap<String,Object>();
                    map1.put("id",inoutstation.getTaskId());
                    System.out.println(inoutstation.getTaskId());
                    map1.put("task_status","");
                    map1.put("order_status","中心库房出库");
                    HttpResponseEntity res1= feignApi.changeTaskOrderType(map1);

                    //改调拨单状态
                    Map map2 =new HashMap<String,Object>();
                    map2.put("id",map.get("alloId"));
                    map2.put("distributors",map.get("distributor"));
                    map2.put("signer",map.get("signer"));
                    System.out.println(map.get("alloId"));
                    map2.put("allo_type","2");
                    HttpResponseEntity res2 = feignApi.updateAllo(map2);
                    HttpResponseEntity res3 = feignApi.updateAllocationbyId(map2);
//
//                if(res*insertRes*(int)res1.getData()*(int)res2.getData() != 1){
//                    res = 0;
//                }

                }else {
                    //库存不够
                    res = 2;
                }

            }
        }


        return res;
    }


    @Override
    public int updatebyId(CentralStation centralStation) {
        int res=centralStationMapper.updateById(centralStation);
        return res;
    }

    @Override
    public PageInfo selectBuy(Map<String, Object> map) {
        PageHelper.startPage(Integer.valueOf((String)map.get("pageNum")), Integer.valueOf((String)map.get("pageSize")));
        String id = String.valueOf(map.get("id"));
        HttpResponseEntity res= feignApi.selectBuy(id);
        String jsonString1 = JSON.toJSONString(res.getData());  // 将对象转换成json格式数据
        JSONObject jsonObject = JSON.parseObject(jsonString1); // 在转回去
        Buy buy = JSON.parseObject(String.valueOf(jsonObject), Buy.class);
        List<Buy> buys = new ArrayList<>();
        buys.add(buy);
        PageInfo pageInfo = new PageInfo(buys);
        return pageInfo;
    }


    //转化传入的goods列表，通过前端传回的“alloId”得到对应的的调拨单
    //遍历goods,通过GoodId得到对应商品的中心库房的库存信息
    //分站库房入库的同时综合以上几个表的信息形成inoutStation单，并将其状态设置为"调拨出库"，以提示库房管理人员有这些商品需要进行分站库房出库（即领货）
    //改任务单订单和调拨单状态,将task单状态改为“可分配”，将订单状态改为“配送站到货”，将调拨单状态改为“3”，表示已调拨出库
    @Override
    public int toInSubstation(Map<String, Object> map) {
        ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
        // 格式化中国时区时间为指定格式的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //得到要进行分站入库的good列表
        List<Good> goods = new ArrayList<>();
        String jsonString2 = JSON.toJSONString(map.get("goods"));
        JSONArray jsonArray2 = JSON.parseArray(jsonString2);
        goods = JSON.parseArray(String.valueOf(jsonArray2),Good.class);
        System.out.println("goodList"+goods);

        QueryWrapper<Allocation> allocationQueryWrapper = new QueryWrapper<>();
        allocationQueryWrapper.eq("id", map.get("alloId"));
        List<Allocation> allocations = allocationMapper.selectList(allocationQueryWrapper);
        Allocation allocation = allocations.get(0);
        System.out.println("allo!!!"+allocation);


        int res = 0;
        //按列表依次入库
        for (Good good:goods){
            //得到对应商品信息
            QueryWrapper<CentralStation> centralStationQueryWrapper =new QueryWrapper<>();
            centralStationQueryWrapper.eq("id",good.getGoodId());
            System.out.println("good.getGoodName()"+good.getGoodName());
            List<CentralStation> centralStations = centralStationMapper.selectList(centralStationQueryWrapper);
            CentralStation centralStation = centralStations.get(0);
            System.out.println(centralStation);

            //入分站库加入库表
            Inoutstation inoutstation = new Inoutstation();
            inoutstation.setStationClass(2);
            inoutstation.setAlloId(allocation.getId());
            inoutstation.setStationName(allocation.getInStationName());
            inoutstation.setStationId(allocation.getInStationId());
            inoutstation.setTaskId(allocation.getTaskId());
            inoutstation.setGoodId(Long.valueOf(centralStation.getId()));
            inoutstation.setGoodPrice(centralStation.getGoodPrice());
            inoutstation.setGoodName(centralStation.getGoodName());
            inoutstation.setGoodUnit(centralStation.getGoodUnit());
            inoutstation.setGoodFactory(good.getGoodFactory());
            inoutstation.setNumber(good.getGoodNumber());
            //设置时间
            String dateTime = LocalDateTime.parse(String.valueOf(map.get("date")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                    ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
            Date date = null;
            try {
                date = simpleDateFormat.parse(dateTime);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            inoutstation.setDate(date);
            inoutstation.setType("调拨出库（分站）");
            inoutstation.setRemark(good.getRemark());
            inoutstation.setSigner(String.valueOf(map.get("signer")));
            inoutstation.setDistributor(String.valueOf(map.get("distributor")));
            System.out.println(inoutstation);
            res = inoutstationMapper.insert(inoutstation);

        }


        //修改任务单状态
        Map map1 =new HashMap<String,Object>();
        map1.put("id",allocation.getTaskId());
        map1.put("task_status","可分配");
        map1.put("order_status","配送站到货");
        HttpResponseEntity res2= feignApi.changeTaskOrderType(map1);
        System.out.println("res3"+res2.getData());

        //修改配送单状态
        Map map2 =new HashMap<String,Object>();
        map2.put("id",map.get("alloId"));
        map2.put("distributors",map.get("distributor"));
        map2.put("signer",map.get("signer"));
        System.out.println(map.get("alloId"));
        map2.put("allo_type","3");
        HttpResponseEntity res1 = feignApi.updateAllo(map2);
        HttpResponseEntity res3 = feignApi.updateAllocationbyId(map2);

//        if(res * (int)res1.getData() * (int)res2.getData() != 1){
//            res = 0;
//        }
        return res;
    }


    //前端查到对应的（已分配）task单及对应的good列表，转化传入的goods列表，遍历goods,通过GoodId得到对应商品的中心库房的库存信息
    //领货出库的同时综合以上几个表的信息形成inoutStation单，并将其状态设置为"分站已领货出库"，以提示库房管理人员有这些商品需要从分站库房出库，送到客户手中
    //改任务单订单和调拨单状态,将订单状态和任务单状态均改为“已领货”
    @Override
    public int takeGoods(Map<String, Object> map) {
        ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
        // 格式化中国时区时间为指定格式的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //得到要进行分站出库的good列表
        List<Good> goods = new ArrayList<>();
        String jsonString2 = JSON.toJSONString(map.get("goods"));
        JSONArray jsonArray2 = JSON.parseArray(jsonString2);
        goods = JSON.parseArray(String.valueOf(jsonArray2),Good.class);
        System.out.println("goodList"+goods);

        int res = 1;
        //按列表依次入库
        for (Good good:goods){
            //得到对应商品信息
            QueryWrapper<CentralStation> centralStationQueryWrapper =new QueryWrapper<>();
            centralStationQueryWrapper.eq("id",good.getGoodId());
            System.out.println("good.getGoodName()"+good.getGoodName());
            List<CentralStation> centralStations = centralStationMapper.selectList(centralStationQueryWrapper);
            CentralStation centralStation = centralStations.get(0);
            System.out.println(centralStation);

            //入分站库加入库表
            Inoutstation inoutstation = new Inoutstation();
            inoutstation.setStationClass(2);
            inoutstation.setAlloId(Long.valueOf(0));
            inoutstation.setStationName(String.valueOf(map.get("stationName")));
            inoutstation.setStationId(Long.valueOf(String.valueOf(map.get("stationId"))));
            inoutstation.setTaskId(Long.valueOf(String.valueOf(map.get("taskId"))));
            inoutstation.setGoodId(good.getGoodId());
            inoutstation.setGoodPrice(centralStation.getGoodPrice());
            inoutstation.setGoodName(centralStation.getGoodName());
            inoutstation.setGoodUnit(centralStation.getGoodUnit());
            inoutstation.setDistributor(String.valueOf(map.get("distributor")));
            inoutstation.setGoodFactory(good.getGoodFactory());
            inoutstation.setNumber(good.getGoodNumber());
            //设置时间
            String dateTime = LocalDateTime.parse(String.valueOf(map.get("date")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                    ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
            Date date = null;
            try {
                date = simpleDateFormat.parse(dateTime);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            inoutstation.setDate(date);
            inoutstation.setType("分站已领货出库");
            inoutstation.setRemark(good.getRemark());
//            inoutstation.setSigner(String.valueOf(map.get("signer")));
            inoutstation.setDistributor(String.valueOf(map.get("distributor")));
            System.out.println(inoutstation);
            res = inoutstationMapper.insert(inoutstation);

        }


        //修改任务单和订单状态
        Map map1 =new HashMap<String,Object>();
        map1.put("id",map.get("taskId"));
        map1.put("task_status","已领货");
        map1.put("order_status","已领货");

        HttpResponseEntity res1= feignApi.changeTaskOrderType(map1);
//        if(res * (int)res1.getData() != 1){
//            res = 0;
//        }
        return res;
    }

    //前端查到退货对应的（已分配）task单及对应的good列表，转化传入的goods列表
    //遍历goods,通过GoodId得到对应商品的中心库房的库存信息，并判断商品是否允许退货，库存量是否符合要求等
    //退货入库的同时综合以上几个表的信息形成inoutStation单，并将其状态设置为"退货出库"，以提示库房管理人员有这些商品需要从分站库房出库到中心库房
    @Override
    public int returnGoodsToSub(Map<String, Object> map) {
        ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
        // 格式化中国时区时间为指定格式的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //得到要进行分站入库的good列表
        List<Good> goods = new ArrayList<>();
        String jsonString2 = JSON.toJSONString(map.get("goods"));
        JSONArray jsonArray2 = JSON.parseArray(jsonString2);
        goods = JSON.parseArray(String.valueOf(jsonArray2),Good.class);
        System.out.println("goodList"+goods);

        int res = 0;
        for(Good good:goods){
            //得到对应商品信息,判断能否退货
            QueryWrapper<CentralStation> centralStationQueryWrapper =new QueryWrapper<>();
            centralStationQueryWrapper.eq("id",good.getGoodId());
            List<CentralStation> centralStations = centralStationMapper.selectList(centralStationQueryWrapper);
            CentralStation centralStation = centralStations.get(0);
            System.out.println("centralStation"+centralStation);

            //判断有无货物
            if (centralStation == null) {
                res = 3;
            } else {
                //判断该商品是否允许退货
                System.out.println(centralStation.getIsReturn());
                if (centralStation.getIsReturn() == 1) {
                    System.out.println("!!!!!");
                    //不得大于中心库房已出库数目
                    if(centralStation.getDoneAllo() >= good.getGoodNumber()){
                        //入分站库加入库表
                        Inoutstation inoutstation = new Inoutstation();
                        inoutstation.setStationClass(2);
                        inoutstation.setAlloId(Long.valueOf(0));
                        inoutstation.setStationName(String.valueOf(map.get("stationName")));
                        inoutstation.setStationId(Long.valueOf(String.valueOf(map.get("stationId"))));
                        inoutstation.setTaskId(Long.valueOf(String.valueOf(map.get("taskId"))));
                        inoutstation.setGoodId(Long.valueOf(centralStation.getId()));
                        inoutstation.setGoodPrice(centralStation.getGoodPrice());
                        inoutstation.setGoodName(centralStation.getGoodName());
                        inoutstation.setGoodUnit(centralStation.getGoodUnit());
                        inoutstation.setGoodFactory(good.getGoodFactory());
                        inoutstation.setNumber(good.getGoodNumber());
                        //设置时间
                        String dateTime = LocalDateTime.parse(String.valueOf(map.get("date")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                                ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
                        Date date = null;
                        try {
                            date = simpleDateFormat.parse(dateTime);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        inoutstation.setDate(date);
                        inoutstation.setType("退货出库");
                        inoutstation.setRemark(good.getRemark());
                        inoutstation.setSigner(String.valueOf(map.get("signer")));
                        inoutstation.setDistributor(String.valueOf(map.get("distributor")));
                        System.out.println("inoutstation!"+inoutstation);
                        res = inoutstationMapper.insert(inoutstation);
                    }
                }else {
                    //不允许退货
                    res = 2;
                    break;
                }
            }
        }
        return res;
    }

    //前端通过调拨单得到对应商品的列表
    //转化传入的goods列表，通过前端传入的“alloId”得到对应的的调拨单
    //遍历goods,通过GoodId得到对应商品的中心库房的库存信息
    //出库的同时综合以上几个表的信息形成inoutStation单，并将其状态设置为"退货入库"，以提示库房管理人员有这些商品需要入库到分站库房
    //将调拨单状态改为“6”，表示分站货物已调拨出库
    @Override
    public int returnGoodsOutSub(Map<String, Object> map) {
        ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
        // 格式化中国时区时间为指定格式的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //得到要进行分站入库的good列表
        List<Good> goods = new ArrayList<>();
        String jsonString2 = JSON.toJSONString(map.get("goods"));
        JSONArray jsonArray2 = JSON.parseArray(jsonString2);
        goods = JSON.parseArray(String.valueOf(jsonArray2),Good.class);
        System.out.println("goodList"+goods);

        QueryWrapper<Allocation> allocationQueryWrapper = new QueryWrapper<>();
        allocationQueryWrapper.eq("id", map.get("alloId"));
        List<Allocation> allocations = allocationMapper.selectList(allocationQueryWrapper);
        Allocation allocation = allocations.get(0);

        int res = 0;
        for(Good good:goods){
            //得到对应商品信息,能否退货
            QueryWrapper<CentralStation> centralStationQueryWrapper =new QueryWrapper<>();
            centralStationQueryWrapper.eq("id",good.getGoodId());
            List<CentralStation> centralStations = centralStationMapper.selectList(centralStationQueryWrapper);
            CentralStation centralStation = centralStations.get(0);
            System.out.println("centralStation"+centralStation);

            if(centralStation == null){
                res = 3;

            }else {

                if(centralStation.getIsReturn() == 1){
                    //入分站库加入库表
                    Inoutstation inoutstation = new Inoutstation();
                    inoutstation.setStationClass(2);
                    inoutstation.setAlloId(allocation.getId());
                    inoutstation.setStationName(allocation.getOutStationName());
                    inoutstation.setStationId(allocation.getOutStationId());
                    inoutstation.setTaskId(allocation.getTaskId());
                    inoutstation.setGoodId(Long.valueOf(centralStation.getId()));
                    inoutstation.setGoodPrice(centralStation.getGoodPrice());
                    inoutstation.setGoodName(centralStation.getGoodName());
                    inoutstation.setGoodUnit(centralStation.getGoodUnit());
                    inoutstation.setGoodFactory(good.getGoodFactory());
                    inoutstation.setNumber(good.getGoodNumber());
                    //设置时间
                    String dateTime = LocalDateTime.parse(String.valueOf(map.get("date")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                            ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(dateTime);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    inoutstation.setDate(date);
                    inoutstation.setType("退货入库");
                    inoutstation.setRemark(good.getRemark());
                    inoutstation.setSigner(String.valueOf(map.get("signer")));
                    inoutstation.setDistributor(String.valueOf(map.get("distributor")));
                    System.out.println("inoutstation!"+inoutstation);
                    res = inoutstationMapper.insert(inoutstation);

                    //修改配送单状态
                    Map map2 =new HashMap<String,Object>();
                    map2.put("id",map.get("alloId"));
                    map2.put("distributors",map.get("distributor"));
                    map2.put("signer",map.get("signer"));
                    System.out.println(map.get("alloId"));
                    map2.put("allo_type","6");
                    HttpResponseEntity res1 = feignApi.updateAllo(map2);
                    HttpResponseEntity res3 = feignApi.updateAllocationbyId(map2);

                    if(res * (int)res1.getData() != 1){
                        res = 0;
                    }

                }else {
                    res = 2;
                }
            }
        }

        return res;
    }


    //前端通过调拨单（6）得到对应商品的列表
    //转化传入的goods列表，通过前端传入的“alloId”得到对应的的调拨单
    //遍历goods,通过GoodId得到对应商品的中心库房的库存信息，入库修改中心库房的库存
    //入库的同时综合以上几个表的信息形成inoutStation单，并将其状态设置为"中心退货入库完成"
    //将调拨单状态改为“7”，表示退货的货物已进入中心库房
    @Override
    public int returnGoodsToCenter(Map<String, Object> map) {
        ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
        // 格式化中国时区时间为指定格式的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //得到要进行分站入库的good列表
        List<Good> goods = new ArrayList<>();
        String jsonString2 = JSON.toJSONString(map.get("goods"));
        JSONArray jsonArray2 = JSON.parseArray(jsonString2);
        goods = JSON.parseArray(String.valueOf(jsonArray2),Good.class);
        System.out.println("goodList"+goods);

        QueryWrapper<Allocation> allocationQueryWrapper = new QueryWrapper<>();
        allocationQueryWrapper.eq("id", map.get("alloId"));
        List<Allocation> allocations = allocationMapper.selectList(allocationQueryWrapper);
        Allocation allocation = allocations.get(0);

        int res = 0;
        for(Good good:goods){
            //得到对应商品信息,能否退货
            QueryWrapper<CentralStation> centralStationQueryWrapper =new QueryWrapper<>();
            centralStationQueryWrapper.eq("id",good.getGoodId());
            List<CentralStation> centralStations = centralStationMapper.selectList(centralStationQueryWrapper);
            CentralStation centralStation = centralStations.get(0);
            System.out.println("centralStation"+centralStation);

            //仓库里有没有这种商品
            if(centralStation == null){
                //没有
                res = 3;
            }else {
                if(centralStation.getIsReturn() == 1){
                    //入分站库加入库表
                    Inoutstation inoutstation = new Inoutstation();
                    inoutstation.setStationClass(1);
                    inoutstation.setAlloId(allocation.getId());
                    inoutstation.setStationName(allocation.getInStationName());
                    inoutstation.setStationId(allocation.getInStationId());
                    inoutstation.setTaskId(allocation.getTaskId());
                    inoutstation.setGoodId(Long.valueOf(centralStation.getId()));
                    inoutstation.setGoodPrice(centralStation.getGoodPrice());
                    inoutstation.setGoodName(centralStation.getGoodName());
                    inoutstation.setGoodUnit(centralStation.getGoodUnit());
                    inoutstation.setGoodFactory(good.getGoodFactory());
                    inoutstation.setNumber(good.getGoodNumber());
                    //设置时间
                    String dateTime = LocalDateTime.parse(String.valueOf(map.get("date")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                            ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
                    Date date = null;
                    try {
                        date = simpleDateFormat.parse(dateTime);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    inoutstation.setDate(date);
                    inoutstation.setType("中心退货入库完成");
                    inoutstation.setRemark(good.getRemark());
                    inoutstation.setSigner(String.valueOf(map.get("signer")));
                    inoutstation.setDistributor(String.valueOf(map.get("distributor")));
                    System.out.println("inoutstation!"+inoutstation);
                    res = inoutstationMapper.insert(inoutstation);

                    //入库改库存
                    centralStation.setWithdrawal(centralStation.getWithdrawal()+good.getGoodNumber());
                    centralStation.setDoneAllo(centralStation.getDoneAllo()-good.getGoodNumber());
                    centralStation.setStock(centralStation.getWaitAllo()+centralStation.getWithdrawal()+centralStation.getDoneAllo());
                    int res1 = centralStationMapper.updateById(centralStation);
                    System.out.println(res1);

                    if(res * res1 != 1){
                        res = 0;
                    }

                }else {
                    res = 2;
                }
            }
        }

        //修改配送单状态
        Map map2 =new HashMap<String,Object>();
        map2.put("id",map.get("alloId"));
        System.out.println(map.get("alloId"));
        map2.put("distributors",map.get("distributor"));
        map2.put("signer",map.get("signer"));
        map2.put("allo_type","7");
        HttpResponseEntity res2 = feignApi.updateAllocationbyId(map2);
        HttpResponseEntity res3 = feignApi.updateAllo(map2);

//        if(res * (int)res2.getData() != 1){
//            res = 0;
//        }

        return res;
    }


    //前端搜索类型为“中心退货”的inoutStation单，选择需要进行操作的单，并从前端返回对应的inoutStationId
    //根据inoutStationId查到对应的inoutStaion对象，再由此找到对应的centralStation即中心库房的库存表
    //中心库房退货出库回供应商,修改对应商品的库存。修改已操作的inoutStaion单状态为“中心退货(已完成)”。
    //生成新的inoutStation,类型为“中心已退回供应商”
    @Override
    public int centralStationReturn(Map<String, Object> map) {
        ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
        // 格式化中国时区时间为指定格式的字符串
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        int res = 0;
        int res1 = 0;

            Inoutstation inoutstation = inoutstationMapper.selectById(String.valueOf(map.get("inoutStationId")));
            System.out.println("inoutstation!!1"+inoutstation);

            //得到对应商品信息,能否退货
            QueryWrapper<CentralStation> centralStationQueryWrapper =new QueryWrapper<>();
            centralStationQueryWrapper.eq("id",inoutstation.getGoodId());
            CentralStation centralStation = centralStationMapper.selectById(inoutstation.getGoodId());
            System.out.println("centralStation"+centralStation);

            if(inoutstation.getType() != null &&inoutstation.getType().equals("中心退货")){
                centralStation.setWithdrawal(centralStation.getWithdrawal()- inoutstation.getNumber());
                centralStation.setStock(centralStation.getWaitAllo()+centralStation.getWithdrawal()+centralStation.getDoneAllo());
                res = centralStationMapper.updateById(centralStation);
                inoutstation.setType("中心退货(已完成)");
                inoutstationMapper.updateById(inoutstation);


                inoutstation.setId(null);
                //设置时间
                String dateTime = LocalDateTime.parse(String.valueOf(map.get("date")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                        ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
                Date date = null;
                try {
                    date = simpleDateFormat.parse(dateTime);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                inoutstation.setDate(date);
                inoutstation.setType("中心已退回供应商");
                inoutstation.setRemark(String.valueOf(map.get("remark")));
                inoutstation.setSigner(String.valueOf(map.get("signer")));
                inoutstation.setDistributor(String.valueOf(map.get("distributor")));
                res1 = inoutstationMapper.insert(inoutstation);
            }
        System.out.println("res!!!"+res);
        System.out.println("res1!!!"+res1);
        if(res * res1 != 1){
            res = 0;
        }
        return res;
    }

    //创建AllOutStation类和OutStationNote类用于整合多表数据来返回前端
    //在中心库房入库时形成的inoutStaion单（类型为“调拨出库”）中，用QueryWrapper按条件条件筛选，筛选条件有“startLine”、“endLine”和“goodName”
    //遍历筛选得到的inoutStation列表，将出库信息整合到allOutStation中的OutStationNote列表，同时统计商品总数和总价，并用PageInfo返回这个allOutStation对象给前端
    @Override
    public PageInfo printOutCentral(Map<String, Object> map) throws ParseException{
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");

        //筛选
        QueryWrapper<Inoutstation> queryWrapper = new QueryWrapper<>();
        if(map.get("startLine") != null && !map.get("startLine").equals("")){
            // 格式化中国时区时间为指定格式的字符串
            String date = LocalDateTime.parse(String.valueOf(map.get("startLine")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                    ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
            Date startline = simpleDateFormat.parse(date);
            System.out.println("start"+startline);
            queryWrapper.ge("date",startline);
        }
        if(map.get("endLine") != null && !map.get("endLine").equals("")){
            String date = LocalDateTime.parse(String.valueOf(map.get("endLine")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                    ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
            Date endline = simpleDateFormat.parse(date);
            System.out.println("end!!!"+endline);
            queryWrapper.lt("date",endline);
        }
        if(map.get("goodName") != null && !map.get("goodName").equals("")){
            System.out.println("goodName");
            queryWrapper.eq("good_name",map.get("goodName"));
        }

        //“调拨出库”是中心库房的商品入库时形成的单子，表示需要出库
        queryWrapper.eq("type","调拨出库");
        List<Inoutstation> res = inoutstationMapper.selectList(queryWrapper);
        List<AllOutStation> res1 = new ArrayList<>();


        AllOutStation allOutStation = new AllOutStation();
        allOutStation.setGoodNumberSum(Long.valueOf(0));
        allOutStation.setPriceSum(0);
        allOutStation.setOutStationNotes(new ArrayList<>());
        for (Inoutstation inoutstation:res){
            OutStationNote outStationNote = new OutStationNote();
            outStationNote.setGoodId(inoutstation.getGoodId());
            outStationNote.setGoodName(inoutstation.getGoodName());
            outStationNote.setGoodPrice(inoutstation.getGoodPrice());
            outStationNote.setNumber(inoutstation.getNumber());
            outStationNote.setGoodFactory(inoutstation.getGoodFactory());
            outStationNote.setRemark(inoutstation.getRemark());
            outStationNote.setDate(inoutstation.getDate());

            allOutStation.getOutStationNotes().add(outStationNote);
            allOutStation.setGoodNumberSum(allOutStation.getGoodNumberSum()+inoutstation.getNumber());
            allOutStation.setPriceSum(allOutStation.getPriceSum()+inoutstation.getGoodPrice()*inoutstation.getNumber());
        }
        System.out.println("res!!1"+res);
        res1.add(allOutStation);
        //设置给前端的返回值
        PageInfo pageInfo = new PageInfo(res1);

        return pageInfo;
    }

    //创建AllDistribute类用于整合多表数据来返回前端
    //在中心库房出库时形成的inoutStaion单（类型为“调拨入库”）中，用QueryWrapper按条件条件筛选，筛选条件有“startLine”、“endLine”和“goodName”
    //遍历筛选得到的inoutStation列表，将出库信息整合到allDistribute中的inoutstation列表，同时统计商品总数和总价，并用PageInfo返回这个allDistribute对象给前端
    @Override
    public PageInfo printDistribute(Map<String, Object> map) throws ParseException {
        PageHelper.startPage(Integer.valueOf(String.valueOf(map.get("pageNum"))),
                Integer.valueOf(String.valueOf(map.get("pageSize"))));

        //筛选task
        QueryWrapper<Inoutstation> queryWrapper = new QueryWrapper<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId chinaZoneId = ZoneId.of("Asia/Shanghai");
        if(map.get("stationName") != null && !map.get("stationName").equals("")){
            System.out.println("goodName");
            queryWrapper.eq("station_name",map.get("stationName"));
        }
        if(map.get("startLine") != null && !map.get("startLine").equals("")){

            // 格式化中国时区时间为指定格式的字符串
            String date = LocalDateTime.parse(String.valueOf(map.get("startLine")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                    ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
            Date startline = simpleDateFormat.parse(date);
            System.out.println("start"+startline);
            queryWrapper.ge("date",startline);
        }
        if(map.get("endLine") != null && !map.get("endLine").equals("")){
            String date = LocalDateTime.parse(String.valueOf(map.get("endLine")), DateTimeFormatter.ISO_DATE_TIME).atZone(
                    ZoneOffset.UTC).withZoneSameInstant(chinaZoneId).format(formatter);
            Date endline = simpleDateFormat.parse(date);
            System.out.println("end!!!"+endline);
            queryWrapper.lt("date",endline);
        }
        if(map.get("goodName") != null && !map.get("goodName").equals("")){
            System.out.println("goodName");
            queryWrapper.eq("good_name",map.get("goodName"));
        }

        //表示商品已出库，要进行入库
        queryWrapper.eq("type","调拨入库");
        List<Inoutstation> res = inoutstationMapper.selectList(queryWrapper);
        List<AllDistribute> res1 = new ArrayList<>();


        AllDistribute allDistribute = new AllDistribute();
        allDistribute.setGoodNumberSum(Long.valueOf(0));
        allDistribute.setPriceSum(0);
        allDistribute.setInoutstations(new ArrayList<>());
        for (Inoutstation inoutstation:res){
            allDistribute.getInoutstations().add(inoutstation);
            allDistribute.setGoodNumberSum(allDistribute.getGoodNumberSum()+inoutstation.getNumber());
            allDistribute.setPriceSum(allDistribute.getPriceSum()+inoutstation.getGoodPrice()*inoutstation.getNumber());
        }
        System.out.println("res!!1"+res);
        res1.add(allDistribute);
        //设置给前端的返回值
        PageInfo pageInfo = new PageInfo(res1);

        return pageInfo;
    }


}
