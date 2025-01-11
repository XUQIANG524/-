package com.iedu.demo.springboot.mapper;

import com.iedu.demo.springboot.entity.OrderDetail;
import com.iedu.demo.springboot.entity.Orders;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface OrdersMapper {

    @Select("SELECT * FROM orders LIMIT #{pageSize} OFFSET #{currentPage} ")
    List<Orders> findAll(int currentPage, int pageSize);

    @Select("select * from orders where id = #{id}")
    Orders findAllById(int id);

    @Delete("delete from orders where id = #{id}")
    int deleteOrdersById(int id);

    @Select("select count(*) from orders")
    int getRecordCount();

    @Update("<script>" +
            "update orders set" +
            "<trim suffixOverrides=\",\">" +
            "    <if test=\"userId != null and userId !=''\">user_id=#{userId},</if>" +
            "    <if test=\"merchantId != null and merchantId !=''\">merchant_id=#{merchantId},</if>" +
            "    <if test=\"workerId != null and workerId !=''\">worker_id=#{workerId},</if>" +
            "    <if test=\"description != null and description !=''\">description=#{description},</if>" +
            "    <if test=\"beginTime != null and beginTime !=''\">begin_time=#{beginTime},</if>" +
            "    <if test=\"endTime != null and endTime !=''\">end_time=#{endTime},</if>" +
            "    <if test=\"orderState != null and orderState !=''\">order_state=#{orderState},</if>" +
            "</trim>" +
            "where id = #{id}"+
            "</script>")
    int updateOrders(Orders orders);

    @Insert("<script>" +
            "insert into orders " +
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">" +
            "    <if test=\"userId != null and userId != ''\">user_id,</if>" +
            "    <if test=\"merchantId != null and merchantId != ''\">merchant_id,</if>" +
            "    <if test=\"workerId != null and workerId != ''\">worker_id,</if>" +
            "    <if test=\"description != null and description != ''\">description,</if>" +
            "    <if test=\"beginTime != null and beginTime != ''\">begin_time,</if>" +
            "    <if test=\"endTime != null and endTime != ''\">end_time,</if>" +
            "    <if test=\"orderState != null and orderState != ''\">order_state,</if>" +
            "</trim>" +
            "values " +
            "<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">" +
            "    <if test=\"userId != null and userId != ''\">#{userId},</if>" +
            "    <if test=\"merchantId != null and merchantId != ''\">#{merchantId},</if>" +
            "    <if test=\"workerId != null and workerId != ''\">#{workerId},</if>" +
            "    <if test=\"description != null and description != ''\">#{description},</if>" +
            "    <if test=\"beginTime != null and beginTime != ''\">#{beginTime},</if>" +
            "    <if test=\"endTime != null and endTime != ''\">#{endTime},</if>" +
            "    <if test=\"orderState != null and orderState != ''\">#{orderState},</if>" +
            "</trim>" +
            "</script>")
    int addOrders(Orders orders);

    @Select("select count(*) from orders where merchant_id = #{merchantId} and order_state = '已完成' ")
    int getOrdercnt(int merchantId);

    @Select("select sum(price) from orders where merchant_id = #{merchantId} and order_state = '已完成'")
    int getOrderprice(int merchantId);

    @Select("select * from orders where user_id = #{userId}")
    List<Orders> findAllByUserId(Orders orders);

    @Select("select * from order_detail where order_id = #{id}")
    List<OrderDetail> getDetailById(int id);

    @Select("select * from orders where merchant_id = #{merchantId}")
    List<Orders> findAllByMerchantId(Orders orders);

    @Update("update orders set order_state = '已接单' where id = #{id}")
    void updateState1(int id);

    @Update("update orders set order_state = '已完成' where id = #{id}")
    void updateState2(int id);

    @Update("update orders set end_time = #{endTime} where id = #{id}")
    void updateEndTime(int id, String endTime);

    @Select("select * from orders where order_state = '待接单'")
    List<Orders> findAllByState0();

    @Select("select * from orders where worker_id = #{workerId}")
    List<Orders> findAllByWorkerId(int workerId);
}
