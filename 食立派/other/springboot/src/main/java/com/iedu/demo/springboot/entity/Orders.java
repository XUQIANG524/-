package com.iedu.demo.springboot.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.security.Timestamp;

@Table(name = "orders")
@Data
public class Orders {
    @Id
    private int id;
    private int userId;
    private int merchantId;
    private String address;
    private String phone;
    private String name;
    private int workerId;
    private String description;
    private String beginTime;
    private String endTime;
    private String orderState;
    private int price;
}
