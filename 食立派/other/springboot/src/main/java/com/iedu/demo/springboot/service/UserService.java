package com.iedu.demo.springboot.service;

import com.iedu.demo.springboot.entity.*;

import java.util.List;

public interface UserService {
    User getUserByNameAndPwd(String username, String pwd);

    User getUserByName(String username);

    void addUser(User cuser);

    void addAddress(int id, String name, String phone, String cityId, String detailAddress);

    void addDish(int userId, int dishId);

    void deleteDish(int userId, int dishId);

    void saveOrders(Orders orders);

    void addOrderDetail(int userId, int id);

    void updateState(int loginIdAsInt);

    User getUserById(int userId);

    void updatePassword(int userId, String newPwd);

    void updateUserInfo(int userId, String username, String phone,String address);

    List<String> getFileById(int id);

    List<Dish> getDishesByMerchantId(int merchantId);

    void deleteCart(int userId);

    List<Shopcart> getCart(int userId);

    Dish getDishById(int dishId);

    Dish getDishesByDishId(int dishId);

    void userShopCntAdd(int userId, int merchantId);

    int getOrdercnt(int userId, int merchantId);

    int getViewcnt(int userId, int merchantId);
}
