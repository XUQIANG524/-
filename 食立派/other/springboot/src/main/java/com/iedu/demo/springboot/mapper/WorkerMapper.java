package com.iedu.demo.springboot.mapper;

import com.iedu.demo.springboot.entity.Worker;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface WorkerMapper {
    void updateWorkerInfo(Worker worker);
    @Insert("insert into worker(worker_id) values(#{id})")
    void addWorker(Integer id);

    Object getWorkerById(int userId);

    void updateWorker(Worker worker);

    Worker findWorkerById(int workerId);

    @Select("select * from worker")
    List<Worker> showWorker();

    void updateWorkerCertification(Worker worker);
}