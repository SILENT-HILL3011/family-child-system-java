package com.parent.service.controller;

import com.child.common.annotation.GlobalInterceptor;
import com.child.common.constants.Constant;
import com.child.common.result.R;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@RestController
@RequestMapping("/child/file")
public class FileController {

    private static final String SAVE_PATH = "E:/webser/web_app/family-client/avatar/";

    private static final String MESSAGE_PATH = "E:/webser/web_app/family-client/message/";
    @RequestMapping("/upload/avatar")
    @GlobalInterceptor(checkLogin = true)
    public R<String> uploadAvatar(MultipartFile file) throws IOException {
        File dir = new File(SAVE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = UUID.randomUUID() + getFileExtension(file.getOriginalFilename());
        File dest = new File(SAVE_PATH + fileName);
        file.transferTo(dest);
        File thumb = new File(SAVE_PATH + Constant.THUMBNAIL_PREFIX + fileName);
        Thumbnails.of(dest).size(200, 200).outputQuality(0.8f).toFile(thumb);
        return R.success(Constant.ACCESS_PREFIX + fileName);
    }

    @RequestMapping("/avatar/{fileName:.+}")
    public byte[] getAvatar(@PathVariable String fileName) throws IOException {
        File file = new File(SAVE_PATH + fileName);
        return Files.readAllBytes(file.toPath());
    }

    // 读取缩略图
    @RequestMapping("/avatar/thumb/{filename:.+}")
    public byte[] getThumbAvatar(@PathVariable String filename) throws IOException {
        File file = new File(SAVE_PATH + Constant.THUMBNAIL_PREFIX + filename);
        return Files.readAllBytes(file.toPath());
    }

    @RequestMapping("/upload/message")
//    @GlobalInterceptor(checkLogin = true)
    public R<String> uploadMessage(MultipartFile file) throws IOException{
        File dir = new File(MESSAGE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = UUID.randomUUID() + getFileExtension(file.getOriginalFilename());
        File dest = new File(MESSAGE_PATH + fileName);
        file.transferTo(dest);
        return R.success("/child/file/message/" + fileName);
    }

    @RequestMapping("/message/{fileName:.+}")
    public byte[] getMessageImage(@PathVariable String fileName) throws IOException {
        File file = new File(MESSAGE_PATH + fileName);
        return Files.readAllBytes(file.toPath());
    }


    private String getFileExtension(String originalFileName) {
        if (originalFileName == null || !originalFileName.contains(".")) {
            return ".jpg";
        }
        return originalFileName.substring(originalFileName.lastIndexOf("."));
    }
}