package com.iedu.demo.springboot.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.iedu.demo.springboot.entity.*;
import com.iedu.demo.springboot.service.MerchantService;
import com.iedu.demo.springboot.service.UserService;
import com.iedu.demo.springboot.service.WorkerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("logic/user")
@CrossOrigin
public class UserController {
    @Autowired
    public MerchantService merchantService;
    @Autowired
    public UserService userService;
    @Autowired
    private WorkerService workerService;
    //展示店铺(后续增加智能推荐功能）
    @RequestMapping(value="showShop",method={RequestMethod.POST})
    @Operation(summary="展示店铺")
    public SaResult showShop()
    {
        User cuser = (User) StpUtil.getSession().get("user");//获取登录人信息（后续增加智能推荐功能）
        List<Merchant> merchants= merchantService.showMerchant();
        return SaResult.ok().setData(merchants);
    }
    //搜索店铺
    @RequestMapping(value="findShop",method={RequestMethod.POST})
    @Operation(summary="搜索店铺")
    public SaResult searchShop(String shopName)
    {
        List<Merchant> merchants= merchantService.findMerchantByName(shopName);
        return SaResult.ok().setData(merchants);
    }
    //用户添加地址
    @RequestMapping(value="addAddress",method={RequestMethod.POST})
    @Operation(summary="用户添加地址")
    public SaResult addAddress(String name, String phone, String city_id, String detail_address)
    {
        int user_id = StpUtil.getLoginIdAsInt();
        userService.addAddress(user_id, name, phone, city_id, detail_address);
        return SaResult.ok();
    }
    //购物车加菜
    @RequestMapping(value="addDish",method={RequestMethod.POST})
    @Operation(summary="购物车加菜")
    public SaResult addDish(int dish_id)
    {
        int user_id = StpUtil.getLoginIdAsInt();
        userService.addDish(user_id, dish_id);
        return SaResult.ok();
    }
    //购物车减菜
    @RequestMapping(value="deleteDish",method={RequestMethod.POST})
    @Operation(summary="购物车减菜")
    public SaResult deleteDish(int dish_id)
    {
        int user_id = StpUtil.getLoginIdAsInt();
        userService.deleteDish(user_id, dish_id);
        return SaResult.ok();
    }
    //用户下订单
    @RequestMapping(value="order",method={RequestMethod.POST})
    @Operation(summary="用户下订单")
    public SaResult order(String description)
    {
        int user_id = StpUtil.getLoginIdAsInt();
        Orders orders = new Orders();
        orders.setUserId(user_id);
        orders.setDescription(description);
        //通过user_id,找到ussername,phone,address
        User cuser = userService.getUserById(user_id);
        orders.setAddress(cuser.getAddress());
        orders.setPhone(cuser.getPhone());
        orders.setName(cuser.getUsername());
        //通过菜品找到商家id
        int merchant_id = userService.getDishesByDishId(userService.getCart(user_id).get(0).getDishId()).getMerchantId();
        orders.setMerchantId(merchant_id);
        //计算总价
        orders.setPrice(userService.getCart(user_id).stream().mapToInt(cartItem -> {
            Dish dish = userService.getDishesByDishId(cartItem.getDishId());
            return dish.getPrice() * cartItem.getCnt();
        }).sum());
        userService.saveOrders(orders);
        //把购物车里的菜转移到订单详细表中
        userService.addOrderDetail(user_id, orders.getId());
        return SaResult.ok().setData(orders);
    }
    //用户修改密码
    @RequestMapping(value="updatePassword",method={RequestMethod.POST})
    @Operation(summary="用户修改密码")
    public SaResult updatePassword(String old_pwd,String new_pwd)
    {
        int user_id = StpUtil.getLoginIdAsInt();
        if(userService.getUserById(user_id).getPwd().equals(old_pwd)){
            userService.updatePassword(user_id,new_pwd);
        }else return SaResult.error("原密码错误");
        return SaResult.ok("修改成功");
    }
    //修改用户名和手机号
    @RequestMapping(value="updateUserInfo",method={RequestMethod.POST})
    @Operation(summary="修改用户名和手机号")
    public SaResult updateUserInfo(String username,String phone,String address)
    {
        int user_id = StpUtil.getLoginIdAsInt();
        userService.updateUserInfo(user_id,username,phone,address);
        return SaResult.ok().setData(userService.getUserById(user_id));
    }
    //获取当前商家的菜品
    @RequestMapping(value="getDishes",method={RequestMethod.POST})
    @Operation(summary="获取当前商家的菜品")
    public SaResult getDishes(int merchant_id)
    {
        int user_id = StpUtil.getLoginIdAsInt();
        userService.userShopCntAdd(user_id,merchant_id);
        List<Dish> dishes = userService.getDishesByMerchantId(merchant_id);
        return SaResult.ok().setData(dishes);
    }
    //清空购物车
    @RequestMapping(value="deleteCart",method={RequestMethod.POST})
    @Operation(summary="清空购物车")
    public SaResult deleteCart()
    {
        int user_id = StpUtil.getLoginIdAsInt();
        userService.deleteCart(user_id);
        return SaResult.ok();
    }
    //获取当前用户的购物车内容
    @RequestMapping(value = "getCart", method = {RequestMethod.POST})
    @Operation(summary = "获取当前用户的购物车内容")
    public SaResult getCart() {
        int userId = StpUtil.getLoginIdAsInt();
        List<Shopcart> shopcartList = userService.getCart(userId);

        // 定义返回的购物车数据列表
        List<Map<String, Object>> cartDetails = shopcartList.stream()
                .map(cartItem -> {
                    Map<String, Object> cartDetail = new HashMap<>();
                    
                    cartDetail.put("count", cartItem.getCnt());

                    // 查询 Dish 表获取价格和图片等信息
                    Dish dish = userService.getDishById(cartItem.getDishId());
                    if (dish != null) {
                        cartDetail.put("price", dish.getPrice());
                        cartDetail.put("image", dish.getImage());
                        cartDetail.put("name", dish.getDishName());
                    }
                    return cartDetail;
                })
                .collect(Collectors.toList());

        return SaResult.ok().setData(cartDetails);
    }


}
