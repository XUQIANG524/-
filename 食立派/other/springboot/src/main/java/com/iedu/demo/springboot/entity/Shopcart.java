package com.iedu.demo.springboot.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "shopcart")
@Data
public class Shopcart {
    @Id
    private int id;
    private int userId;
    private int dishId;
    private int cnt;
}
