package com.iedu.demo.springboot.service.impl;

import com.iedu.demo.springboot.entity.Merchant;
import com.iedu.demo.springboot.mapper.MerchantMapper;
import com.iedu.demo.springboot.mapper.OrdersMapper;
import com.iedu.demo.springboot.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private OrdersMapper ordersMapper;

    @Override
    public List<Merchant> showMerchant() {
        return merchantMapper.showMerchant();
    }

    @Override
    public List<Merchant> findMerchantByName(String shopName) {
        return merchantMapper.findMerchantByName(shopName);
    }

    @Override
    public void addMerchant(Integer id) {
        merchantMapper.addMerchant(id);
    }

    @Override
    public void updateMerchantInfo(Merchant merchant) {
        merchantMapper.updateMerchantInfo(merchant);
    }

    @Override
    public void submitMerchantImage(int merchantId, String imageUrl) {
        Merchant merchant = new Merchant();
        merchant.setMerchantId(merchantId);
        merchant.setImage(imageUrl); // 设置店面图片
        merchantMapper.updateMerchantImage(merchant); // 更新数据库中的图片路径
    }

    @Override
    public void updateMerchantCertification(int merchantId, boolean approved) {
        Merchant merchant = new Merchant();
        merchant.setMerchantId(merchantId);
        merchant.setCertification(approved ? "TRUE" : "FALSE"); // 更新认证状态
        merchantMapper.updateMerchantCertification(merchant); // 更新数据库中的认证状态
    }

    @Override
    public int getOrdercnt(int merchantId) {
        return ordersMapper.getOrdercnt(merchantId);
    }

    @Override
    public int getOrderprice(int merchantId) {
        return ordersMapper.getOrderprice(merchantId);
    }

    @Override
    public Merchant getMerchantById(int id) {
        return merchantMapper.getMerchantById(id);
    }

}
