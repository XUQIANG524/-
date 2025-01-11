package com.iedu.demo.springboot.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "order_detail")
@Data
public class OrderDetail {
    @Id
    private int id;
    private int orderId;
    private int dishId;
    private int dishCnt;
}
