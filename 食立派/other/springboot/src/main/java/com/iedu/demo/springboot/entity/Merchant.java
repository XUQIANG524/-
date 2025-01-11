package com.iedu.demo.springboot.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "merchant")
@Data
public class Merchant {
    @Id
    private int merchantId;
    private String merchantName;
    private String idNum;
    private String shopName;
    private int cityId;
    private String address;
    private String type;
    private String image;
    private String certification;
    private double rating;
    private String certificationTime;
    private String description;
    private String phone;
    private String time;
}
