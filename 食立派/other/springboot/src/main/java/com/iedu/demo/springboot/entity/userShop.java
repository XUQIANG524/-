package com.iedu.demo.springboot.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "user_shop")
@Data
public class userShop {
    @Id
    private Integer id;
    private Integer userId;
    private Integer merchantId;
    private Integer cnt;
}
