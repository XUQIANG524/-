package com.iedu.demo.springboot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "address")
@Data
public class Address {
    @Id
    private int id;
    private int userId;
    private String name;
    private String phone;
    private String cityId;
    private String detailAddress;
}
