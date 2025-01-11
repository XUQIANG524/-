package com.iedu.demo.springboot.service;

import com.iedu.demo.springboot.entity.Worker;
import com.iedu.demo.springboot.mapper.WorkerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WorkerService {

    void updateWorkerCertification(int workerId, boolean approved);

    void updateWorkerInfo(Worker worker);

    void addWorker(Integer id);


    Object getWorkerById(int userId);

    void updateWorker(Worker worker);

    Worker findWorkerById(int workerId);

    List<Worker> showWorker();
}
