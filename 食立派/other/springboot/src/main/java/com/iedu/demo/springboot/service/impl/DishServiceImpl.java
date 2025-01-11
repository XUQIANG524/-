package com.iedu.demo.springboot.service.impl;

import com.iedu.demo.springboot.entity.Dish;
import com.iedu.demo.springboot.mapper.DishMapper;
import com.iedu.demo.springboot.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Override
    public List<Dish> showAllDishes() {
        return dishMapper.findAllDishes();
    }

    @Override
    public List<Dish> findDishesByMerchantId(int merchantId) {
        return dishMapper.findDishesByMerchantId(merchantId);
    }

    @Override
    public List<Dish> findDishesByName(String dishName) {
        return dishMapper.findByDishNameContaining(dishName);
    }

    @Override
    public boolean addDish(Dish dish) {
        System.out.println("Adding dish with merchant_id: " + dish.getMerchantId()); // 添加调试信息
        try {
            dishMapper.insertDish(dish);
            return true;  // 添加成功
        } catch (Exception e) {
            e.printStackTrace();  // 打印异常信息
            return false;  // 添加失败
        }
    }


    @Override
    public void updateDish(Dish dish) {
        dishMapper.updateDish(dish);
    }

    @Override
    public void deleteDish(int dishId) {
        dishMapper.deleteDish(dishId);
    }

    @Override
    public boolean isMerchantIdValid(Integer merchantId) {
        // 这里可以调用一个商家 Mapper 方法来检查商家 ID 是否存在
        return dishMapper.findMerchantById(merchantId) != null; // 假设你有这个方法
    }

    @Override
    public Dish findDishById(int dishId) {
        return dishMapper.findDishById(dishId);
    }

}
