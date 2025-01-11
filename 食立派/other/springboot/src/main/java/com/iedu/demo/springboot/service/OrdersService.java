package com.iedu.demo.springboot.service;

import cn.dev33.satoken.util.SaResult;
import com.iedu.demo.springboot.commons.TbData;
import com.iedu.demo.springboot.entity.OrderDetail;
import com.iedu.demo.springboot.entity.Orders;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrdersService {

    SaResult deleteOrdersById(int id);

    SaResult findAllById(int id);

    SaResult updateOrders(Orders orders);

    SaResult addOrders(Orders orders);

    TbData<Orders> findAll(int currentPage, int pageSize);

    List<Orders> findAllByUserId(Orders orders);

    List<OrderDetail> getDetailById(int id);

    List<Orders> findAllByMerchantId(Orders orders);

    void updateState1(int id);

    void updateState2(int id);

    List<Orders> findAllByState0();

    List<Orders> findAllByWorkerId(int workerId);
}
