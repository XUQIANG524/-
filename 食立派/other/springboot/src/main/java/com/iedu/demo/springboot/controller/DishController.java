package com.iedu.demo.springboot.controller;

import cn.dev33.satoken.util.SaResult;
import com.iedu.demo.springboot.entity.Dish;
import com.iedu.demo.springboot.entity.ResponseMessage;
import com.iedu.demo.springboot.service.DishService;
import com.iedu.demo.springboot.service.FileService;
import io.minio.MinioClient;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import cn.dev33.satoken.stp.StpUtil;
import com.iedu.demo.springboot.entity.File;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api/dishes")
@CrossOrigin(origins = "http://localhost:5173") // 允许来自该地址的跨域请求
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private FileService fileService;

    @Autowired
    private MinioClient minioClient;

    @Operation(summary="商家查询所有菜品")
    @GetMapping("/merchant")
    public ResponseEntity<List<Dish>> getDishesByMerchantId(@RequestParam("id") int merchantId) {
        List<Dish> dishes = dishService.findDishesByMerchantId(merchantId);
        return ResponseEntity.ok(dishes);
    }

    @Operation(summary="商家添加菜品")
    @PostMapping
    public ResponseEntity<?> addDish(@RequestBody Dish dish) {
        // 检查 merchant_id 是否有效
        if (dish.getMerchantId() == null || !dishService.isMerchantIdValid(dish.getMerchantId())) {
            return ResponseEntity.badRequest().body(new ResponseMessage(false, "无效的商家ID", null));
        }

        boolean success = dishService.addDish(dish);
        if (success) {
            return ResponseEntity.ok(new ResponseMessage(true, "菜品添加成功", dish.getDishId()));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage(false, "菜品添加失败", null));
        }
    }

    @Operation(summary="商家删除菜品")
    @DeleteMapping
    public ResponseEntity<?> deleteDish(@RequestParam("id") int dishId) {
        try {
            dishService.deleteDish(dishId);
            return ResponseEntity.ok(new ResponseMessage(true, "菜品删除成功", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage(false, "菜品删除失败", null));
        }
    }

    @Operation(summary="商家更新菜品")
    @PutMapping("/{id}") // 使用路径变量来接收菜品 ID
    public ResponseEntity<?> updateDish(@PathVariable int id, @RequestBody Dish dish) {
        try {
            dish.setDishId(id); // 设置菜品 ID
            dishService.updateDish(dish); // 调用服务层更新菜品
            return ResponseEntity.ok(new ResponseMessage(true, "菜品更新成功", null));
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常信息
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseMessage(false, "菜品更新失败", null));
        }
    }

    @RequestMapping(value = "dishUpload", method = RequestMethod.POST)
    @Operation(summary = "商家上传菜品图片")
    public SaResult dishUpload(@RequestParam int dishId, @RequestParam("file") MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (StpUtil.isLogin()) {
            // 生成文件名
            String fileName = "pic/" + System.currentTimeMillis() + "-" + file.getOriginalFilename(); // 修改路径为菜品图片路径

            // 保存文件信息
            File f = new File();
            f.setId(StpUtil.getLoginIdAsInt());
            f.setName(file.getOriginalFilename());
            f.setPath(fileName);
            f.setType("菜品图片");
            fileService.insertFile(f); // 保存文件信息

            // 上传文件到 MinIO
            PutObjectArgs args = PutObjectArgs.builder()
                    .object(fileName)
                    .bucket("tjpu22s06") // 确保使用正确的桶名
                    .contentType(file.getContentType())
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build();
            minioClient.putObject(args);

            // 更新菜品的图片路径
            Dish dish = dishService.findDishById(dishId); // 根据 dishId 获取当前菜品
            if (dish != null) {
                dish.setImage(fileName); // 只更新图片路径
                dishService.updateDish(dish); // 更新菜品信息
            }

            // 返回结果
            return SaResult.ok().setMsg(fileName);
        } else {
            return SaResult.error("未登录");
        }
    }
}