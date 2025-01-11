package com.iedu.demo.springboot.commons;

import lombok.Data;

import java.util.List;

@Data

public class TbData <T>{

    private int code;
    private int count;
    private List<T> data;

}
