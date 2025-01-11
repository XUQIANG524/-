package com.iedu.demo.springboot.mapper;

import com.iedu.demo.springboot.entity.Merchant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MerchantMapper {

    @Select("select * from merchant")
    List<Merchant> showMerchant();

    @Select("select * from merchant where shop_name = #{shopName}")
    List<Merchant> findMerchantByName(String shopName);

    @Insert("insert into merchant(merchant_id) values(#{id})")
    void addMerchant(Integer id);

    void updateMerchantInfo(Merchant merchant);

    void updateMerchantImage(Merchant merchant); // 更新商家图片

    void updateMerchantCertification(Merchant merchant); // 更

    @Select("select count(*) from order where merchant_id = #{merchantId} and order_status = 1")
    int getOrdercnt(int merchantId);

    @Select("select * from merchant where merchant_id = #{id}")
    Merchant getMerchantById(int id);
}