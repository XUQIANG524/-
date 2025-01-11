package com.iedu.demo.springboot.service;

import com.iedu.demo.springboot.entity.Merchant;
import com.iedu.demo.springboot.mapper.MerchantMapper;
import org.apache.catalina.session.StoreBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MerchantService {

    void updateMerchantInfo(Merchant merchant);

    List<Merchant> showMerchant();

    List<Merchant> findMerchantByName(String shopName);

    void addMerchant(Integer id);

    void submitMerchantImage(int merchantId, String imageUrl); // 提交店面图片

    void updateMerchantCertification(int merchantId, boolean approved); // 更新商家认证状态

    int getOrdercnt(int merchantId);

    int getOrderprice(int merchantId);

    Merchant getMerchantById(int id);
}
