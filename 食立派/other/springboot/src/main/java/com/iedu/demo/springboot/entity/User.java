package com.iedu.demo.springboot.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "user")
@Data
public class User {
    @Id
    private Integer id;

    private String pwd;
    private String username;
    private String phone;
    //role（1：用户，2：商家，3：骑手，4：管理员）
    private int role;
    private  int state;
    private String address;

}
