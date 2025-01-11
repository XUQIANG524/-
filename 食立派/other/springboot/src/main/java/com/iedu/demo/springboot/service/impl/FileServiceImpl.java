package com.iedu.demo.springboot.service.impl;

import com.iedu.demo.springboot.entity.File;
import com.iedu.demo.springboot.mapper.FileMapper;
import com.iedu.demo.springboot.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileMapper fileMapper;
    @Override
    public void insertFile(File f) {
        fileMapper.insertFile(f);
    }
}
