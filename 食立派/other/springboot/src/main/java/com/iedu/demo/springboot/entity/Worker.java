package com.iedu.demo.springboot.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.security.Timestamp;

@Table(name = "worker")
@Data
public class Worker {
    @Id
    private int workerId;
    private String workerName;
    private String idNum;
    private int cityId;
    private String address;
    private String certification;
    private double rating;
    private String phone;
    private String certificationTime;
    private String avatar;
}
