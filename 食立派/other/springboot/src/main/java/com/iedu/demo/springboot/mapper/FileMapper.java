package com.iedu.demo.springboot.mapper;

import com.iedu.demo.springboot.entity.File;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface FileMapper {

    @Insert("insert into file(type,id,path,name) values(#{type},#{id},#{path},#{name})")
    void insertFile(File f);
}
