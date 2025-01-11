package com.iedu.demo.springboot.mapper;

import com.iedu.demo.springboot.entity.Dish;
import com.iedu.demo.springboot.entity.Merchant;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    // 查询所有菜品
    List<Dish> findAllDishes();

    // 根据菜品名称模糊查询
    List<Dish> findByDishNameContaining(@Param("name") String name);

    // 根据商家ID查询菜品
    @Select("SELECT * FROM dish WHERE merchant_id = #{merchantId}")
    List<Dish> findDishesByMerchantId(@Param("merchantId") int merchantId);

    // 添加新菜品
    void insertDish(Dish dish);

    // 更新菜品信息
    void updateDish(Dish dish);

    // 删除菜品
    @Delete("DELETE FROM dish WHERE dish_id = #{dishId}")
    void deleteDish(@Param("dishId") int dishId);

    @Select("SELECT * FROM merchant WHERE merchant_id = #{merchantId}")
    Merchant findMerchantById(Integer merchantId);

    @Select("SELECT * FROM dish WHERE dish_id = #{dishId}")
    Dish findDishById(int dishId);
}
