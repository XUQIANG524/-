package com.iedu.demo.springboot.service.impl;

import cn.dev33.satoken.secure.SaSecureUtil;
import com.iedu.demo.springboot.entity.*;
import com.iedu.demo.springboot.mapper.UserMapper;
import com.iedu.demo.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper usermapper;
    @Override
    public User getUserByNameAndPwd(String username, String pwd) {
        User user  = usermapper.getUserByNameAndPwd(username, pwd);
        return user;
    }

    @Override
    public User getUserByName(String username) {
        User user  = usermapper.getUserByName(username);
        return user;
    }

    @Override
    public void addUser(User cuser) {
        List<User> userlist  =  usermapper.SetterByPhone(cuser.getPhone());
        if(userlist.size()==0) {
            cuser.setPwd(SaSecureUtil.md5(cuser.getPwd()));
            usermapper.add(cuser);
        }
    }

    @Override
    public void addAddress(int user_id, String name, String phone, String cityId, String detailAddress) {
        usermapper.addAddress(user_id,name,phone,cityId,detailAddress);
    }

    @Override
    public void addDish(int userId, int dishId) {
        //先看购物车表里有没有userId和dishId对应的列，有的话cnt++,没有的话新建一个，cnt设为1
        if(usermapper.getDish(userId,dishId)!=null){
            usermapper.updateDishAdd(userId,dishId);
        }else usermapper.addDish(userId,dishId);
    }

    @Override
    public void deleteDish(int userId, int dishId) {
        //如果对应的菜的数量为0，那么什么也不做，不然就减一
        if(usermapper.getDish(userId,dishId)!=0){
            usermapper.updateDishSub(userId,dishId);
        }
    }

    @Override
    public void saveOrders(Orders orders) {
        usermapper.saveOrders(orders);
    }

    @Override
    public void addOrderDetail(int userId, int orderId) {
        //根据userId取出购物车表中的dishId和cnt，然后插入到订单详情表中
        List<Pair<Integer, Integer>> shopcartList = usermapper.getDishIdAndCnt(userId);
        //在购物车表中删除取出的部分
        usermapper.deleteDish(userId);
        for(Pair<Integer, Integer> pair : shopcartList){
            usermapper.addOrderDetail(orderId, pair.getFirst(), pair.getSecond());
        }
    }

    @Override
    public void updateState(int loginIdAsInt) {
        usermapper.updateState(loginIdAsInt);
    }

    @Override
    public User getUserById(int userId) {
        return usermapper.getUserById(userId);
    }

    @Override
    public void updatePassword(int userId, String newPwd) {
        usermapper.updatePassword(userId,newPwd);
    }

    @Override
    public void updateUserInfo(int userId, String username, String phone, String address) {
        usermapper.updateUserInfo(userId,username,phone,address);
    }

    @Override
    public List<String> getFileById(int id) {
        List<String> fileList = usermapper.getFileById(id);
        return fileList;
    }

    @Override
    public List<Dish> getDishesByMerchantId(int merchantId) {
        return usermapper.getDishesByMerchantId(merchantId);
    }

    @Override
    public void deleteCart(int userId) {
        usermapper.deleteDish(userId);
    }

    @Override
    public List<Shopcart> getCart(int userId) {
        return usermapper.getCart(userId);
    }

    @Override
    public Dish getDishById(int dishId) {
        return usermapper.getDishById(dishId);
    }

    @Override
    public Dish getDishesByDishId(int dishId) {
        return usermapper.getDishesByDishId(dishId);
    }

    @Override
    public void userShopCntAdd(int userId, int merchantId) {
        if(usermapper.getUserShopCnt(userId,merchantId)==null){
            usermapper.addUserShop(userId,merchantId);
        }
        else usermapper.userShopCntAdd(userId,merchantId);
    }

    @Override
    public int getOrdercnt(int userId, int merchantId) {
        Integer ct =  usermapper.getOrdercnt(userId,merchantId);
        if(ct==null){
            return 0;
        }else return ct;
    }

    @Override
    public int getViewcnt(int userId, int merchantId) {
        Integer ct =  usermapper.getViewcnt(userId,merchantId);
        if(ct==null){
            return 0;
        }else return ct;
    }
}
