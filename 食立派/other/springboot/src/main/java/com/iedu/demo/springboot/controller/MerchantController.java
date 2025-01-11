package com.iedu.demo.springboot.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.iedu.demo.springboot.entity.File;
import com.iedu.demo.springboot.entity.Merchant;
import com.iedu.demo.springboot.entity.User;
import com.iedu.demo.springboot.service.FileService;
import com.iedu.demo.springboot.service.MerchantService;
import com.iedu.demo.springboot.service.UserService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;

    // 商家完善个人信息
    @Operation(summary = "商家完善个人信息")
    @PostMapping("{id}")
    public ResponseEntity<?> updateMerchantInfo(
            @PathVariable int id,
            @RequestBody Merchant merchant,
            @RequestHeader("Authorization") String token) {
            System.out.println("商家信息: " + merchant);

        try {

            // 移除 Bearer 前缀并解析 token
            String tokenValue = token.replace("Bearer ", "");
            String merchantIdFromTokenString = StpUtil.getLoginIdByToken(tokenValue).toString();
            int merchantIdFromToken = Integer.parseInt(merchantIdFromTokenString);

            // 校验商家是否有权限修改自己的信息
            if (merchantIdFromToken != id) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("没有权限修改该商家信息");
            }

            // 设置 merchantId 并更新信息
            merchant.setMerchantId(id);
            merchantService.updateMerchantInfo(merchant);

            System.out.println("商家信息: " + merchant); // 打印商家信息
            // 确保 merchant 对象的属性被正确设置
            if (merchant.getMerchantName() == null || merchant.getIdNum() == null || merchant.getShopName() == null) {
                return ResponseEntity.badRequest().body("商家信息不完整");
            }
            merchant.setMerchantId(id);
            merchantService.updateMerchantInfo(merchant);
            return ResponseEntity.ok("商家信息更新成功");
        

        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("无效的商家 ID 格式");
        } catch (Exception e) {
            // 捕获其他异常，避免敏感信息泄露
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("服务器内部错误，请稍后再试");
        }
    }


    // 提交店面图片
    @Operation(summary="商家提交认证图片")
    @PostMapping("/{id}/submit-image")
    public void submitImage(@PathVariable int id, @RequestParam String imageUrl, @RequestHeader("Authorization") String token) {
        // 获取 token 并解析出 merchantId
        String tokenValue = token.replace("Bearer ", ""); // 移除 Bearer 前缀
        String merchantIdFromTokenString = StpUtil.getLoginIdByToken(tokenValue).toString(); // 获取商家 ID 字符串
        int merchantIdFromToken = Integer.parseInt(merchantIdFromTokenString); // 将字符串转换为整数

        // 校验商家是否有权限提交认证图片
        if (merchantIdFromToken != id) {
            throw new RuntimeException("没有权限提交该商家的认证图片");
        }

        merchantService.submitMerchantImage(id, imageUrl); // 提交认证图片
    }

    // 管理员审核商家认证
    @Operation(summary="管理员审核商家认证")
    @PutMapping("/{id}/certification")
    public void updateCertification(@PathVariable int id, @RequestParam boolean approved) {
        merchantService.updateMerchantCertification(id, approved); // 更新商家认证状态
    }

    // 上传认证文件

    @RequestMapping(value = "merchantuploadauthentication", method = RequestMethod.POST)
    @Operation(summary = "商家上传认证文件")
    public SaResult filUpload(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 生成文件名
        if(StpUtil.isLogin()) {
            String fileName = "pic/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
            File f = new File();
            f.setId(StpUtil.getLoginIdAsInt());
            f.setName(file.getOriginalFilename());
            f.setPath(fileName);
            f.setType("商家身份证");
            fileService.insertFile(f); // 保存文件信息
            userService.updateState(StpUtil.getLoginIdAsInt());
            // 更新商家认证状态
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
        }else return SaResult.error("未登录");
    }
    //骑手上传健康证明
    @RequestMapping(value = "merchantuploadhealth", method = RequestMethod.POST)
    @Operation(summary = "商家上传营业执照")
    public SaResult filUpload2(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 生成文件名
        if(StpUtil.isLogin()) {
            String fileName = "pic/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
            File f = new File();
            f.setId(StpUtil.getLoginIdAsInt());
            f.setName(file.getOriginalFilename());
            f.setPath(fileName);
            f.setType("商家营业执照");
            fileService.insertFile(f); // 保存文件信息
            // 更新商家认证状态
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
        }else return SaResult.error("未登录");
    }
    //商户上传照片
    @RequestMapping(value = "merchantupload", method = RequestMethod.POST)
    @Operation(summary = "商家上传店铺照片")
    public SaResult filUpload1(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 生成文件名
        if(StpUtil.isLogin()) {
            String fileName = "pic/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
            File f = new File();
            f.setId(StpUtil.getLoginIdAsInt());
            f.setName(file.getOriginalFilename());
            f.setPath(fileName);
            f.setType("商家店铺图");
            fileService.insertFile(f); // 保存文件信息
            // 更新商家认证状态
            // 构建上传参数
            Merchant merchant  = new Merchant();
            merchant.setMerchantId(StpUtil.getLoginIdAsInt());
            merchant.setImage(fileName);
            merchantService.updateMerchantInfo(merchant);
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
        }else return SaResult.error("未登录");
    }
    //获取当前商户信息
    @RequestMapping(value = "getMerchant", method = RequestMethod.POST)
    @Operation(summary = "获取当前商户信息")
    public SaResult getMerchant() {
        //打印token
        int id = StpUtil.getLoginIdAsInt();
        Merchant merchant = merchantService.getMerchantById(id);
        return SaResult.ok().setData(merchant);
    }
    //商家修改个人信息
    @RequestMapping(value = "merchantupdateinfo", method = RequestMethod.POST)
    @Operation(summary = "商家修改个人信息")
    public SaResult updateMerchantInfo(String name,String phone,String address,String description,String time){
        int id = StpUtil.getLoginIdAsInt();
        Merchant merchant = new Merchant();
        merchant.setMerchantId(id);
        merchant.setShopName(name);
        merchant.setPhone(phone);
        merchant.setAddress(address);
        merchant.setDescription(description);
        merchant.setTime(time);
        merchantService.updateMerchantInfo(merchant);
        return SaResult.ok().setData(merchant);
    }

    // 获取商家 ID
    @GetMapping("/getMerchantId")
    public int getMerchantIdFromToken(@RequestHeader("Authorization") String token) {
        try {
            String tokenValue = token.replace("Bearer ", ""); // 移除 Bearer 前缀
            String loginId = StpUtil.getLoginIdByToken(tokenValue).toString();
            return Integer.parseInt(loginId); // 转换为整数
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("商家 ID 无法转换为整数", e);
        } catch (Exception e) {
            throw new RuntimeException("获取商家 ID 时发生错误", e);
        }
    }


}
