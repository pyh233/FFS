package com.example.flyfishshop.api;

import com.example.flyfishshop.util.JsonResult;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping(value = "/admin/api/v1/ue", produces = MediaType.APPLICATION_JSON_VALUE)
public class UEditorApi {

    @Value("${custom.img.upload.location}")
    private String uploadLocation;

    @RequestMapping
    public ResponseEntity<Object> initUE(String action, MultipartFile file, HttpServletRequest req) throws IOException {
        //  读取json配置文件
        if (action.equals("config")) {
            try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("ueconfig.json")) {
                String config = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
                return ResponseEntity.ok(config);
            }
        } else if (action.equals("image")) {
            String type = "ueditor";
            File fileDir = new File(uploadLocation + type);
            if (!fileDir.exists()) {
                boolean success = fileDir.mkdirs();
                if (!success) {
                    return ResponseEntity.badRequest().body(JsonResult.fail("无法在服务器中创建文件夹"));
                }
            }
            // 获取拓展名(带点.)
            String fileName = file.getOriginalFilename();
            String ext = fileName.substring(fileName.lastIndexOf("."));
            // 改名
            fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            // 取随机数
            Random random = new Random();
            int n = random.nextInt(1000, 10000);
            // 拼好服务器上的名字
            fileName += '-' + n + ext;
            // 保存文件
            File desc = new File(uploadLocation + type + File.separator + fileName);
            file.transferTo(desc);
            // 返回给前端数据
            Map<String, Object> data = Map.of("state", "SUCCESS", "url", req.getContextPath() + "/static/" + type + File.separator + fileName, "title", "", "original", "");
            return ResponseEntity.ok(data);
        }
        return ResponseEntity.notFound().build();
    }
}
