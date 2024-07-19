package com.blog.back.controller;

import com.blog.back.framework.context.ReqInfoContext;
import com.blog.back.framework.resp.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/media")
public class UploadController {

    @Value("${blog.media_url}")
    String baseMediaUrl;

    @PostMapping("/upload")
    public Result uploadImage(MultipartFile file){
        String url = UUID.randomUUID().toString();
        File newFile=new File(baseMediaUrl+"/"+ url);
        try {
            if(!newFile.createNewFile()){
                return Result.fail("上传失败!");
            }
            file.transferTo(newFile);
        } catch (IOException e) {
            return Result.fail("上传失败!");
        }
        Integer user_id = ReqInfoContext.getReqInfo().getUser().getId();
        String viewUrl = "http://localhost:8090/media/"+url;
        return Result.success(viewUrl);
    }

    @ResponseBody
    @GetMapping("/{url}")
    public byte[] getImage(@PathVariable("url") String url){
        File file=new File(baseMediaUrl+"/"+url);
        if(file.exists()){
            try {
                FileInputStream inputStream = new FileInputStream(file);
                byte[] bytes = new byte[inputStream.available()];

                inputStream.read(bytes, 0, inputStream.available());

                return bytes;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
