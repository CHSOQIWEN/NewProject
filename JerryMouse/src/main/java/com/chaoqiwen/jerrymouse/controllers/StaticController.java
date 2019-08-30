package com.chaoqiwen.jerrymouse.controllers;

import com.chaoqiwen.jerrymouse.http.Controller;
import com.chaoqiwen.jerrymouse.http.Request;
import com.chaoqiwen.jerrymouse.http.Response;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
/**
 * @Author:chaoqiwen
 * @Date:2019/8/29 21:38
 */
public class StaticController extends Controller {
    /*1、找到文件
     * 2、把文件内容作为response body写入
     * 3、ContentType
     *    .html     text/html
     *    .js       application/javascript
     *    .css      text/css
     *    .jpg      image/jpeg*/
    private static final String HOME=System.getenv("JM_HOME");
    public void doGet(Request request, Response response) throws IOException {
        //找到文件
        String filename=getFilename(request.getUrl());
        //根据文件名的后缀决定contentType
        String suffix=getSuffix(filename);//得到文件 后缀
        String contentType=getContentType(suffix);
        response.setContentType(contentType);

        //把文件 的所有内容作为response的body发送
        InputStream is=new FileInputStream(filename);
        byte[] buf=new byte[1024];
        int len;
        while ((len=is.read(buf))!=-1){
            response.write(buf,0,len);
        }
    }

    private  final Map<String,String> CONTENT_TYPE=new HashMap<String,String>(){
        {
            put("html","text/html");
            put("css","text/css");
            put("js","application/js");
        }
        //匿名类没有构造方法，不能进行初始化，利用构造代码块进行初始化
    };
    private String getContentType(String suffix) {
        String contentType=CONTENT_TYPE.get(suffix);
        if(contentType==null){
            contentType="text/html";
        }
        return contentType;
    }

    private String getSuffix(String filename) {
        int index=filename.lastIndexOf(".");
        if(index==-1){
            return null;
        }
        return filename.substring(index+1);
    }

    private String getFilename(String url) {
        if(url.equals("/")){
            url="/index.html";
        }
        return HOME+ File.separator+"webapp"+url.replace("/",File.separator);
    }
}
