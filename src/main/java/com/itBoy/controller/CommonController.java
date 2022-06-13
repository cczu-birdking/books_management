package com.itBoy.controller;


import com.itBoy.entity.Common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

//主要进行文件的上传和下载
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    //上传文件到仓库中
    //参数名同样有要求！！！！
    @PostMapping("/upload")
    public R upload(MultipartFile file){
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        //为了防止覆盖，我们随机生成文件名,使用uuid
        String fileName = UUID.randomUUID().toString()+substring;
        //动态把原始文件名称后缀改了

        //创建一个目录对象来判断basePath是否存在
        File dir = new File(basePath);
        if(!dir.exists()){
            //目录不存在
            dir.mkdirs();
        }
        //file是一个临时文件，需要转存到其他位置。否则本次请求完成后临时文件会自动消失
        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info(file.toString());
        return new R(true,fileName);
    }


    //图片下载过来
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        //输入流来读取文件内容
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
            //输出流将文件写会浏览器，展示图片
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len = 0;
            byte[] bytes = new byte[1024];
            while( (len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
