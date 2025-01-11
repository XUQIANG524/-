package com.iedu.demo.springboot.service.impl;

import com.iedu.demo.springboot.entity.Merchant;
import com.iedu.demo.springboot.entity.Worker;
import com.iedu.demo.springboot.mapper.WorkerMapper;
import com.iedu.demo.springboot.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkerServiceImpl implements WorkerService {

    @Autowired
    public WorkerMapper workerMapper;
    @Override
    public void addWorker(Integer id) {
        workerMapper.addWorker(id);
    }

    @Override
    public void updateWorkerCertification(int workerId, boolean approved) {
        Worker worker = new Worker();
        worker.setWorkerId(workerId);
        worker.setCertification(approved ? "TRUE" : "FALSE"); // 更新认证状态
        workerMapper.updateWorkerCertification(worker); // 更新数据库中的认证状态
    }

    @Override
    public void updateWorkerInfo(Worker worker) {
        workerMapper.updateWorkerInfo(worker);
    }

    @Override
    public Object getWorkerById(int userId) {
        return workerMapper.getWorkerById(userId);
    }

    @Override
    public void updateWorker(Worker worker) {
        workerMapper.updateWorker(worker);
    }

    @Override
    public Worker findWorkerById(int workerId) {
        return workerMapper.findWorkerById(workerId);
    }

    @Override
    public List<Worker> showWorker() {
        return workerMapper.showWorker();
    }

}
