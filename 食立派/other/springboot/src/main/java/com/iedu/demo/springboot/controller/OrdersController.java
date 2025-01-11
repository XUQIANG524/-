package com.iedu.demo.springboot.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.iedu.demo.springboot.commons.TbData;
import com.iedu.demo.springboot.entity.Dish;
import com.iedu.demo.springboot.entity.OrderDetail;
import com.iedu.demo.springboot.entity.Orders;
import com.iedu.demo.springboot.service.MerchantService;
import com.iedu.demo.springboot.service.OrdersService;
import com.iedu.demo.springboot.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.criteria.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    //deleteOrdersById
    @Autowired
    public OrdersService ordersService;
    @Autowired
    public MerchantService merchantService;
    @Autowired
    public UserService userService;
    @RequestMapping(value ="deleteOrdersById",method={RequestMethod.POST})
    @Operation(summary="通过id删除订单")
    public SaResult deleteOrdersById(int id){
        return ordersService.deleteOrdersById(id);
    }
    //findAllById
    @RequestMapping(value ="findAllById",method={RequestMethod.POST})
    @Operation(summary="通过id查找订单")
    public SaResult findAllById(int id){
        return ordersService.findAllById(id);
    }
    //updateOrders
    @RequestMapping(value ="updateOrders",method={RequestMethod.POST})
    @Operation(summary="更新订单")
    public SaResult updateOrders(int id, int merchant_id, String address, int worker_id, String description,String order_state){
        Orders orders = new Orders();
        orders.setId(id);
        orders.setMerchantId(merchant_id);
        orders.setAddress(address);
        orders.setWorkerId(worker_id);
        orders.setDescription(description);
        orders.setOrderState(order_state);
        return ordersService.updateOrders(orders);
    }
    //insertOrders
    @RequestMapping(value ="insertOrders",method={RequestMethod.POST})
    @Operation(summary="插入订单")
    public SaResult insertOrders(int user_id, int merchant_id, String address, int worker_id, String description){
        Orders orders = new Orders();
        orders.setUserId(user_id);
        orders.setMerchantId(merchant_id);
        orders.setAddress(address);
        orders.setWorkerId(worker_id);
        orders.setDescription(description);
        return ordersService.addOrders(orders);
    }
    //findAll
    @RequestMapping(value ="findAll",method={RequestMethod.POST})
    @Operation(summary="查找所有订单")
    public SaResult findAll(int currentPage, int pageSize){
        TbData<Orders> tbData = ordersService.findAll(currentPage, pageSize);
        return SaResult.ok().data(tbData);
    }
    //查询当前用户的全部订单
    @RequestMapping(value ="findAllByUserId",method={RequestMethod.POST})
    @Operation(summary="查找当前用户的所有订单")
    public SaResult findAllByUserId(){
        int user_id = StpUtil.getLoginIdAsInt();
        Orders orders = new Orders();
        orders.setUserId(user_id);
        List<Orders> orderList= ordersService.findAllByUserId(orders);
        List<Map<String, Object>> ordersDetails = orderList.stream()
                .map(orderItem -> {
                    Map<String, Object> ordersDetail = new HashMap<>();
                    ordersDetail.put("id", orderItem.getId());
                    //通过merchantId查询商家信息
                    ordersDetail.put("storename",merchantService.getMerchantById(orderItem.getMerchantId()).getShopName());
                    ordersDetail.put("customerName",orderItem.getName());
                    ordersDetail.put("ordertime", orderItem.getBeginTime());
                    ordersDetail.put("status", orderItem.getOrderState());
                    ordersDetail.put("price", orderItem.getPrice());
                    List<OrderDetail> ods = ordersService.getDetailById(orderItem.getId());
                    List<Map<String, Object>> detailList = ods.stream()
                            .map(detail -> {
                                Map<String, Object> detailMap = new HashMap<>();
                                //通过dish_id查询菜品名字
                                detailMap.put("name", userService.getDishById(detail.getDishId()).getDishName());
                                //通过dish_id查询菜品图片
                                detailMap.put("image", userService.getDishById(detail.getDishId()).getImage());
                                return detailMap;
                            })
                            .collect(Collectors.toList());
                    ordersDetail.put("orderdetail", detailList);
                    return ordersDetail;
                })
                .collect(Collectors.toList());
        Collections.reverse(ordersDetails);
        return SaResult.ok().data(ordersDetails);
    }
    //查询当前商家的全部订单
    @RequestMapping(value ="findAllByMerchantId",method={RequestMethod.POST})
    @Operation(summary="查找当前商家的所有订单")
    public SaResult findAllByMerchantId(){
        int user_id = StpUtil.getLoginIdAsInt();
        Orders orders = new Orders();
        orders.setMerchantId(user_id);
        List<Orders> orderList= ordersService.findAllByMerchantId(orders);
        List<Map<String, Object>> ordersDetails = orderList.stream()
                .map(orderItem -> {
                    Map<String, Object> ordersDetail = new HashMap<>();
                    ordersDetail.put("id", orderItem.getId());
                    //通过merchantId查询商家信息
                    ordersDetail.put("customerName",orderItem.getName());
                    ordersDetail.put("ordertime", orderItem.getBeginTime());
                    ordersDetail.put("status", orderItem.getOrderState());
                    ordersDetail.put("price", orderItem.getPrice());
                    ordersDetail.put("address", orderItem.getAddress());
                    ordersDetail.put("date", orderItem.getDescription());
                    List<OrderDetail> ods = ordersService.getDetailById(orderItem.getId());
                    List<Map<String, Object>> detailList = ods.stream()
                            .map(detail -> {
                                Map<String, Object> detailMap = new HashMap<>();
                                //通过dish_id查询菜品名字
                                detailMap.put("dishName", userService.getDishById(detail.getDishId()).getDishName());
                                //通过dish_id查询菜品图片
                                detailMap.put("image", userService.getDishById(detail.getDishId()).getImage());
                                detailMap.put("amount", detail.getDishCnt());
                                return detailMap;
                            })
                            .collect(Collectors.toList());
                    ordersDetail.put("orderdetail", detailList);
                    return ordersDetail;
                })
                .collect(Collectors.toList());
        return SaResult.ok().data(ordersDetails);
    }
    @RequestMapping(value ="updateState1",method={RequestMethod.POST})
    @Operation(summary="更新订单状态已接单")
    public SaResult updateState(int id){
        int worker_id = StpUtil.getLoginIdAsInt();
        Orders orders = new Orders();
        orders.setWorkerId(worker_id);
        orders.setId(id);
        orders.setOrderState("已接单");
        ordersService.updateOrders(orders);
        return SaResult.ok();
    }
    @RequestMapping(value ="updateState2",method={RequestMethod.POST})
    @Operation(summary="更新订单状态已完成")
    public SaResult updateState2(int id){
        ordersService.updateState2(id);
        return SaResult.ok();
    }
    //查找所有未接单的订单
    @RequestMapping(value ="findAllByState0",method={RequestMethod.POST})
    @Operation(summary="查找所有未接单的订单")
    public SaResult findAllByState0(){
        List<Orders> orders = ordersService.findAllByState0();
        List<Map<String, Object>> ordersDetails = orders.stream()
                .map(orderItem -> {
                    Map<String, Object> ordersDetail = new HashMap<>();
                    ordersDetail.put("id", orderItem.getId());
                    //得到商家地址
                    ordersDetail.put("pickupAddress",merchantService.getMerchantById(orderItem.getMerchantId()).getAddress());
                    ordersDetail.put("time", orderItem.getBeginTime());
                    ordersDetail.put("price", orderItem.getPrice());
                    ordersDetail.put("deliveryAddress", orderItem.getAddress());

                    return ordersDetail;
                })
                .collect(Collectors.toList());
        return SaResult.ok().data(ordersDetails);
    }
    //骑手获取自己的订单
    @RequestMapping(value ="findAllByWorkerId",method={RequestMethod.POST})
    @Operation(summary="骑手获取自己的订单")
    public SaResult findAllByWorkerId(){
        int worker_id = StpUtil.getLoginIdAsInt();
        List<Orders> orders = ordersService.findAllByWorkerId(worker_id);
        return SaResult.ok().data(orders);
    }
}
