package com.iedu.demo.springboot.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.iedu.demo.springboot.entity.Merchant;
import com.iedu.demo.springboot.entity.User;
import com.iedu.demo.springboot.entity.Worker;
import com.iedu.demo.springboot.service.MerchantService;
import com.iedu.demo.springboot.service.UserService;
import com.iedu.demo.springboot.service.WorkerService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("logic/plat")
@CrossOrigin
public class PlatController {

    @Autowired
    public UserService userService;
    @Autowired
    public MerchantService merchantService;
    @Autowired
    public WorkerService workerService;
    @Autowired
    private MinioClient minioClient;
    //用户注册
    @RequestMapping(value="UserRegister",method={RequestMethod.POST})
    @Operation(summary="用户注册")
    public SaResult UserRegister(String username,String pwd,String phone,int role){
        User cuser;
        cuser = userService.getUserByName(username);
        if(cuser!=null) return SaResult.error("用户名已存在");
        cuser = new User();
        cuser.setUsername(username);
        cuser.setPwd(pwd);
        cuser.setPhone(phone);
        cuser.setRole(role);
        cuser.setState(0);
        userService.addUser(cuser);
        if(cuser.getId()!=null){
            return SaResult.ok("注册成功").setData(cuser);
        }else return SaResult.error("手机号已注册请重新输入");
    }
    //用户登录（1：用户，2：商家，3：骑手，4：管理员）
    @RequestMapping(value="UserLogin", method={RequestMethod.POST})
    @Operation(summary="用户登录")
    public SaResult UserLogin(String username, String pwd){
        User cuser = userService.getUserByNameAndPwd(username, pwd);
        if(cuser != null){
            StpUtil.login(cuser.getId());
            StpUtil.getSession().set("user", cuser);

            // 如果是商家角色 (role = 2)，直接使用 userId 作为 merchantId
            Integer merchantId = (cuser.getRole() == 2) ? cuser.getId() : null;
            // 如果是骑手角色 (role = 3)，直接使用 userId 作为 workerId
            Integer workerId = (cuser.getRole() == 3) ? cuser.getId() : null;

            // 返回响应时，将 merchantId 一并返回
            return SaResult.ok(StpUtil.getTokenValue())
                    .setData(cuser)
                    .set("workerId", workerId)
                    .set("merchantId", merchantId);  // 直接返回 userId 作为 merchantId
        } else {
            return SaResult.error("用户名或密码错误");
        }
    }

    //用户登出
    @RequestMapping(value="UserLogout",method={RequestMethod.POST})
    @Operation(summary="用户登出")
    public SaResult UserLogout(){
        User cuser = (User) StpUtil.getSession().get("user");
        cuser.setPwd("");
        StpUtil.logout();
        return SaResult.ok("登出成功").setData(cuser);
    }

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @Operation(summary = "文件上传")
    public SaResult filUpload(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 生成文件名
        String fileName = "pic/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();

        // 构建上传参数
        PutObjectArgs args = PutObjectArgs.builder()
                .object(fileName)
                .bucket("tjpu22s06")
                .contentType(file.getContentType())
                .stream(file.getInputStream(), file.getSize(), -1)
                .build();

        // 上传文件到 MinIO
        minioClient.putObject(args);

        // 返回结果
        return SaResult.ok().setMsg(fileName);
    }
    //获取所有骑手信息
    @RequestMapping(value="getAllWorker",method={RequestMethod.POST})
    @Operation(summary="获取所有骑手信息")
    public SaResult getAllWorker(){
        List<Worker> workers = workerService.showWorker();
        List<Map<String, Object>> workersWithImages = workers.stream()
                .map(worker -> {
                    Map<String, Object> workerWithImages = new HashMap<>();
                    workerWithImages.put("id", worker.getWorkerId());
                    workerWithImages.put("name", worker.getWorkerName());
                    workerWithImages.put("certificationTime", worker.getCertificationTime());
                    workerWithImages.put("address", worker.getAddress());
                    workerWithImages.put("certification", worker.getCertification());
                    // 添加 certificationImages 字段，值为一个空数组
                    workerWithImages.put("certificationImages", userService.getFileById(worker.getWorkerId()));
                    return workerWithImages;
                })
                .collect(Collectors.toList());
        return SaResult.ok().setData(workersWithImages);
    }
    //获取所有商家信息
    @RequestMapping(value="getAllMerchant",method={RequestMethod.POST})
    @Operation(summary="获取所有商家信息")
    public SaResult getAllMerchant(){
        List<Merchant> merchants = merchantService.showMerchant();
        List<Map<String, Object>> merchantsWithImages = merchants.stream()
                .map(merchant -> {
                    Map<String, Object> merchantWithImages = new HashMap<>();
                    merchantWithImages.put("id", merchant.getMerchantId());
                    merchantWithImages.put("name", merchant.getShopName());
                    merchantWithImages.put("certificationTime", merchant.getCertificationTime());
                    merchantWithImages.put("address", merchant.getAddress());
                    // 添加 certificationImages 字段，值为一个空数组
                    merchantWithImages.put("certification", merchant.getCertification());
                    merchantWithImages.put("certificationImages", userService.getFileById(merchant.getMerchantId()));
                    return merchantWithImages;
                })
                .collect(Collectors.toList());
        return SaResult.ok().setData(merchantsWithImages);
    }
    //获取所有店铺信息
    @RequestMapping(value = "getAllShop", method = {RequestMethod.POST})
    @Operation(summary = "获取所有店铺信息")
    public SaResult getAllShop() {
        int userId = StpUtil.getLoginIdAsInt();
        List<Merchant> merchants = merchantService.showMerchant();
        List<Map<String, Object>> merchantsWithImages = merchants.stream()
                // 过滤掉 shopName 为空的商家
                .filter(merchant -> merchant.getImage() != null)
                .map(merchant -> {
                    Map<String, Object> merchantWithImages = new HashMap<>();
                    merchantWithImages.put("id", merchant.getMerchantId());
                    merchantWithImages.put("name", merchant.getShopName());
                    merchantWithImages.put("rate", merchant.getRating());
                    merchantWithImages.put("description", merchant.getDescription());
                    merchantWithImages.put("images", merchant.getImage());

                    // 获取用户购买次数和浏览次数
                    int orderCount = userService.getOrdercnt(userId,merchant.getMerchantId());
                    int viewCount = userService.getViewcnt(userId, merchant.getMerchantId()); // 假设有此方法获取浏览次数
                    int oCount = merchantService.getOrdercnt(merchant.getMerchantId());
                    // 计算平均价格，避免除以 0 的问题
                    double price = (oCount > 0)
                            ? (double) merchantService.getOrderprice(merchant.getMerchantId()) / orderCount
                            : 0.0;
                    //小数点精确到第二位
                    price = Math.round(price * 10.0) / 10.0;
                    // 计算推荐评分（购买次数 * 0.7 + 浏览次数 * 0.3）
                    double score = orderCount * 0.7 + viewCount * 0.3;

                    // 填充数据
                    merchantWithImages.put("sales", orderCount);
                    merchantWithImages.put("views", viewCount);
                    merchantWithImages.put("price", price);
                    merchantWithImages.put("score", score);// 添加综合评分字段
                    return merchantWithImages;
                })
                .sorted((m1, m2) -> Double.compare((double) m2.get("score"), (double) m1.get("score"))) // 根据评分降序排序
                .collect(Collectors.toList());

        return SaResult.ok().setData(merchantsWithImages);
    }






}
