package com.iedu.demo.springboot.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Table(name = "file")
@Data
public class File {
    @Id
    private int fileId;
    private String type;
    private int id;
    private String path;
    private String name;
}
