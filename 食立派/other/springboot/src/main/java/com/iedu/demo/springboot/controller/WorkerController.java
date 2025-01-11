package com.iedu.demo.springboot.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.iedu.demo.springboot.entity.File;
import com.iedu.demo.springboot.entity.Worker;
import com.iedu.demo.springboot.service.FileService;
import com.iedu.demo.springboot.service.UserService;
import com.iedu.demo.springboot.service.WorkerService;
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
@RequestMapping("api/workers")
@CrossOrigin
public class WorkerController {
    @Autowired
    private WorkerService workerService;
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;

    @Operation(summary="骑手完善个人信息")
    @PutMapping("/{id}")
    public void updateWorker(@PathVariable int id, @RequestBody Worker worker) {
        worker.setWorkerId(id);
        workerService.updateWorkerInfo(worker);
    }
    //骑手认证个人信息
    @RequestMapping(value="addWorker",method={RequestMethod.POST})
    @Operation(summary="骑手添加信息")
    public SaResult addWorker(String workerName,String idNum,String address) {
        int user_id = StpUtil.getLoginIdAsInt();
        Worker worker = new Worker();
        worker.setWorkerId(user_id);
        worker.setWorkerName(workerName);
        worker.setIdNum(idNum);
        worker.setAddress(address);
        workerService.updateWorkerInfo(worker);
        return SaResult.ok().setData(worker);
    }
    //骑手上传身份证
    @RequestMapping(value = "workeruploadauthentication", method = RequestMethod.POST)
    @Operation(summary = "骑手上传认证文件")
    public SaResult filUpload(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 生成文件名
        if(StpUtil.isLogin()) {
            String fileName = "pic/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
            File f = new File();
            f.setId(StpUtil.getLoginIdAsInt());
            f.setName(file.getOriginalFilename());
            f.setPath(fileName);
            f.setType("骑手身份证");
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
    @RequestMapping(value = "workeruploadhealth", method = RequestMethod.POST)
    @Operation(summary = "骑手上传健康证明")
    public SaResult filUpload2(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 生成文件名
        if(StpUtil.isLogin()) {
            String fileName = "pic/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
            File f = new File();
            f.setId(StpUtil.getLoginIdAsInt());
            f.setName(file.getOriginalFilename());
            f.setPath(fileName);
            f.setType("骑手健康证明");
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

    @Operation(summary = "骑手修改个人信息")
    @PutMapping("/updateWorker/{workerId}")
    public ResponseEntity<?> updateWorker(
            @PathVariable int workerId,
            @RequestBody Worker worker,
            @RequestHeader("Authorization") String token) {

        try {
            // 移除 Bearer 前缀并解析 token
            String tokenValue = token.replace("Bearer ", "");
            String workerIdFromTokenString = StpUtil.getLoginIdByToken(tokenValue).toString();
            int workerIdFromToken = Integer.parseInt(workerIdFromTokenString);

            // 校验骑手是否有权限修改自己的信息
            if (workerIdFromToken != workerId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("没有权限修改该骑手信息");
            }

            // 校验传入的骑手信息是否完整
            if (worker.getWorkerName() == null  || worker.getPhone() == null) {
                return ResponseEntity.badRequest().body("骑手信息不完整");
            }

            // 设置 workerId 并更新信息
            worker.setWorkerId(workerId);
            workerService.updateWorker(worker);

            return ResponseEntity.ok("骑手信息更新成功");

        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("无效的骑手 ID 格式");
        } catch (Exception e) {
            // 捕获其他异常，避免敏感信息泄露
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("服务器内部错误，请稍后再试");
        }
    }

    @GetMapping("/getWorkerById")
    public int getWorkerIdFromToken(@RequestHeader("Authorization") String token) {
        try {
            String tokenValue = token.replace("Bearer ", ""); // 移除 Bearer 前缀
            String loginId = StpUtil.getLoginIdByToken(tokenValue).toString();
            return Integer.parseInt(loginId); // 转换为整数
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("骑手 ID 无法转换为整数", e);
        } catch (Exception e) {
            throw new RuntimeException("获取骑手 ID 时发生错误", e);
        }
    }

    @GetMapping("/getWorker/{workerId}")
    public ResponseEntity<Worker> getWorker(@PathVariable int workerId) {
        Worker worker = workerService.findWorkerById(workerId);
        if (worker != null) {
            return ResponseEntity.ok(worker);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @RequestMapping(value = "workerUploadAvatar", method = RequestMethod.POST)
    @Operation(summary = "骑手上传头像")
    public SaResult filUpload1(MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 生成文件名
        if(StpUtil.isLogin()) {
            String fileName = "pic/" + System.currentTimeMillis() + "-" + file.getOriginalFilename();
            File f = new File();
            f.setId(StpUtil.getLoginIdAsInt());
            f.setName(file.getOriginalFilename());
            f.setPath(fileName);
            f.setType("骑手头像图");
            fileService.insertFile(f); // 保存文件信息
            // 构建上传参数
            Worker worker  = new Worker();
            worker.setWorkerId(StpUtil.getLoginIdAsInt());
            worker.setAvatar(fileName);
            workerService.updateWorker(worker);
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
    @Operation(summary="审核骑手认证")
    @PutMapping("/certification/{id}")
    public void updateCertification(@PathVariable int id, @RequestParam boolean approved) {
        workerService.updateWorkerCertification(id, approved); // 更新商家认证状态
    }
}