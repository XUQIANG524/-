package com.iedu.demo.springboot.service;

import com.iedu.demo.springboot.entity.Dish;

import java.util.List;

public interface DishService {

    List<Dish> showAllDishes();

    List<Dish> findDishesByMerchantId(int merchantId);

    List<Dish> findDishesByName(String dishName);

    boolean addDish(Dish dish);  // 修改为返回 boolean

    void updateDish(Dish dish);

    void deleteDish(int dishId);

    boolean isMerchantIdValid(Integer merchantId);

    Dish findDishById(int dishId);
}
