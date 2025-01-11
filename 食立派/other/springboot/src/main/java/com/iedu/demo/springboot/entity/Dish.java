package com.iedu.demo.springboot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;

@Entity
@Table(name = "dish")
@Data
public class Dish {
    @Id
    private int dishId; // 主键

    @Column(name = "merchant_id") // 映射数据库中的列名
    private Integer merchantId; // 商家ID

    private String dishName; // 菜品名称
    private Integer price; // 价格
    private String image; // 图片链接
    private int saleCnt; // 销售数量
    private String flavour; // 口味
    private int state; // 状态
    private String description;
}
