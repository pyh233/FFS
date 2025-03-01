package com.example.flyfishshop.util;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;

public class DeletePicsUtil {
    @Value("${custom.img.upload.location}")
    private static String prefixPath;
    public static void delPics(String fileName,String type) {
        // TODO:判断路径是否合法?
        File file = new File(prefixPath+type+File.separator+fileName);
        if(file.exists()){
            file.delete();
        }
    }
}
