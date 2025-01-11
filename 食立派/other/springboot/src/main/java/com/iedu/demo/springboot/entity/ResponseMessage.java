package com.iedu.demo.springboot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // 生成全参数构造函数
public class ResponseMessage {
    private boolean success; // 操作是否成功
    private String message; // 返回消息
    private Object data; // 返回的数据，可以是任何类型
}
