package com.iedu.demo.springboot.mapper;

import com.iedu.demo.springboot.entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface UserMapper {

    @Select("select * from user where username = #{username} and pwd = #{pwd}")
    User getUserByNameAndPwd(String username, String pwd);

    @Select("select * from user where username = #{username}")
    User getUserByName(String username);

    @Select("select * from user where phone = #{phone}")
    List<User> SetterByPhone(String phone);

    @Insert("insert into user(username,pwd,phone,role,state) values(#{username},#{pwd},#{phone},#{role},#{state})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void add(User cuser);

    @Insert("insert into address(user_id,name,phone,city_id,detail_address) values(#{userId},#{name},#{phone},#{cityId},#{detailAddress})")
    void addAddress(int userId, String name, String phone, String cityId, String detailAddress);


    @Insert("insert into shopcart(user_id,dish_id) values(#{userId},#{dishId})")
    void addDish(int userId, int dishId);

    //返回cnt
    @Select("select cnt from shopcart where user_id = #{userId} and dish_id = #{dishId}")
    Integer getDish(int userId, int dishId);

    //让cnt+1
    @Update("update shopcart set cnt = cnt + 1 where user_id = #{userId} and dish_id = #{dishId}")
    void updateDishAdd(int userId, int dishId);

    //让cnt-1
    @Update("update shopcart set cnt = cnt - 1 where user_id = #{userId} and dish_id = #{dishId}")
    void updateDishSub(int userId, int dishId);

    @Insert("insert into orders(user_id,merchant_id,description,address,phone,name,price) values(#{userId},#{merchantId},#{description},#{address},#{phone},#{name},#{price})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="id")
    void saveOrders(Orders orders);

    //只有当cnt不为0时才返回
    @Select("SELECT dish_id, cnt FROM shopcart WHERE user_id = #{userId} AND cnt != 0")
    List<Pair<Integer, Integer>> getDishIdAndCnt(int userId);

    @Insert("insert into order_detail(order_id,dish_id,dish_cnt) values(#{orderId},#{first},#{second})")
    void addOrderDetail(int orderId, Integer first, Integer second);

    @Delete("delete from shopcart where user_id = #{userId}")
    void deleteDish(int userId);

    @Update("update user set state = 1 where id = #{loginIdAsInt}")
    void updateState(int loginIdAsInt);

    @Select("select * from user where id = #{userId}")
    User getUserById(int userId);

    @Update("update user set pwd = #{newPwd} where id = #{userId}")
    void updatePassword(int userId, String newPwd);

    @Update("update user set username = #{username},phone = #{phone} ,address = #{address} where id = #{userId}")
    void updateUserInfo(int userId, String username, String phone, String address);

    @Select("select path from file where id = #{id}")
    List<String> getFileById(int id);

    @Select("select * from dish where merchant_id = #{merchantId} and state = 1")
    List<Dish> getDishesByMerchantId(int merchantId);

    @Select("select * from shopcart where user_id = #{userId}")
    List<Shopcart> getCart(int userId);

    @Select("select * from dish where dish_id = #{dishId}")
    Dish getDishById(int dishId);

    @Select("select * from dish where dish_id = #{dishId}")
    Dish getDishesByDishId(int dishId);

    @Update("update user_shop set cnt = cnt + 1 where user_id = #{userId} and merchant_id = #{merchantId}")
    void userShopCntAdd(int userId, int merchantId);

    @Select("select * from user_shop where user_id = #{userId} and merchant_id = #{merchantId}")
    userShop getUserShopCnt(int userId, int merchantId);

    @Insert("insert into user_shop(user_id,merchant_id,cnt) values(#{userId},#{merchantId},1)")
    void addUserShop(int userId, int merchantId);

    @Select("select count(*) from orders where user_id = #{userId} and merchant_id = #{merchantId}")
    Integer getOrdercnt(int userId, int merchantId);

    @Select("select cnt from user_shop where user_id = #{userId} and merchant_id = #{merchantId}")
    Integer getViewcnt(int userId, int merchantId);
}
