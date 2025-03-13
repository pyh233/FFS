package com.example.flyfishshop.api;

import com.example.flyfishshop.util.JsonResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@RestController
@RequestMapping(value = "/admin/api/v1/upload", produces = MediaType.APPLICATION_JSON_VALUE)
public class UploadApi {
    @Value("${custom.img.upload.location}")
    private String uploadLocation;

    @PostMapping("/image")
    public ResponseEntity<JsonResult> uploadImage(MultipartFile file, String type) throws IOException {
        // 创建文件夹
        if (!StringUtils.hasText(type)) {
            type = "unkonwn";
        }
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
        //
        File desc = new File(uploadLocation + type + File.separator + fileName);
        file.transferTo(desc);

        JsonResult jr = JsonResult.success("保存成功", type + "/" + fileName);
        return ResponseEntity.ok(jr);
    }
    @DeleteMapping("/cancel")
    public ResponseEntity<JsonResult> cancelUpload(String fileUrl) throws IOException {
        File file = new File(uploadLocation + fileUrl);
        if(!file.exists()) {
            return ResponseEntity.ofNullable(JsonResult.fail("000"));
        }
        boolean success = file.delete();
        if(success) {
            return ResponseEntity.ok(JsonResult.success("000",null));
        }else{
            return ResponseEntity.ofNullable(JsonResult.fail("111"));
        }
    }
}
