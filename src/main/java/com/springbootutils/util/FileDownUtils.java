package com.springbootutils.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class FileDownUtils {

    public void fileDown(HttpServletRequest request, HttpServletResponse response, String path, String fileName) throws UnsupportedEncodingException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        // response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        // 下载文件能正常显示中文
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        // response.setHeader("Content-Disposition", "attachment;filename=" + getFileName(request, fileName));
        // 实现文件下载
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        InputStream is = null;
        try {
            is = classPathResource.getInputStream();
            bis = new BufferedInputStream(is);
            OutputStream os = response.getOutputStream();
            int len = 0;
            while ((len = bis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
//            return Resp.success();
        } catch (Exception e) {
//            return Resp.error();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getFileName(HttpServletRequest request, String fileName) throws UnsupportedEncodingException {
        if (request.getHeader("USER-AGENT").toLowerCase().contains("edge") // Edge-win10新的浏览器内核
                || request.getHeader("USER-AGENT").toLowerCase().contains("trident")) { // trident-IE浏览器内核
            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = fileName.replace("+", "%20"); // 处理空格变“+”的问题
        } else { // 谷歌 火狐 360
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1");
        }
        return fileName;
    }
}
