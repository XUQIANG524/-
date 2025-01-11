package com.iedu.demo.springboot.service.impl;

import cn.dev33.satoken.util.SaResult;
import com.iedu.demo.springboot.commons.TbData;
import com.iedu.demo.springboot.entity.OrderDetail;
import com.iedu.demo.springboot.entity.Orders;
import com.iedu.demo.springboot.mapper.OrdersMapper;
import com.iedu.demo.springboot.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Override
    public SaResult deleteOrdersById(int id) {
        return ordersMapper.deleteOrdersById(id) > 0 ? SaResult.ok() : SaResult.error();
    }

    @Override
    public SaResult findAllById(int id) {
        return SaResult.data(ordersMapper.findAllById(id));
    }

    @Override
    public SaResult updateOrders(Orders orders) {
        return ordersMapper.updateOrders(orders) > 0 ? SaResult.ok() : SaResult.error();
    }

    @Override
    public SaResult addOrders(Orders orders) {
        return ordersMapper.addOrders(orders) > 0 ? SaResult.ok() : SaResult.error();
    }

    @Override
    public TbData<Orders> findAll(int currentPage, int pageSize) {
        TbData<Orders> tbData=new TbData<>();
        tbData.setData(ordersMapper.findAll((currentPage-1)*pageSize,pageSize));
        tbData.setCount(ordersMapper.getRecordCount());
        return tbData;
    }

    @Override
    public List<Orders> findAllByUserId(Orders orders) {
        return ordersMapper.findAllByUserId(orders);
    }

    @Override
    public List<OrderDetail> getDetailById(int id) {
        return ordersMapper.getDetailById(id);
    }

    @Override
    public List<Orders> findAllByMerchantId(Orders orders) {
        return ordersMapper.findAllByMerchantId(orders);
    }

    @Override
    public void updateState1(int id) {
        ordersMapper.updateState1(id);
    }

    @Override
    public void updateState2(int id) {
        //设置结束时间为当前时间
        long currentTimeMillis = System.currentTimeMillis();
        Instant instant = Instant.ofEpochMilli(currentTimeMillis);
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        // 格式化为 MySQL DATETIME 格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String endTime = localDateTime.format(formatter);
        ordersMapper.updateState2(id);
        ordersMapper.updateEndTime(id,endTime);
    }

    @Override
    public List<Orders> findAllByState0() {
        return ordersMapper.findAllByState0();
    }

    @Override
    public List<Orders> findAllByWorkerId(int workerId) {
        return ordersMapper.findAllByWorkerId(workerId);
    }
}
