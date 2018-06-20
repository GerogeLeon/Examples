package com.example.fileupload.controller;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author billjiang 475572229@qq.com
 * @create 17-8-29
 */
@RestController
public class FileUploadController {

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String handleFileUpload(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
        byte[] bytes=file.getBytes();
        File fileToSave=new File(file.getOriginalFilename());
        FileCopyUtils.copy(bytes,fileToSave);
        return fileToSave.getAbsolutePath();
    }
}
