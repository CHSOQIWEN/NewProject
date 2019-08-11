package http请求解析;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/11 10:17
 */
public class Request {
   private InputStream is;
   private String method;
   private String url;
   private String protocol;
   private Map<String, String> requestParams = new HashMap<>();
   private Map<String,String > headers=new HashMap<>();

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }
    public String getHeader(String header){
        return headers.get(header.toUpperCase());
    }

    public static  Request parse(InputStream is) throws IOException {
        Request request=new Request();
        request.is=is;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        parseRequestLine(reader,request);
        parseRequestHead(reader,request);
        if(request.method.equals("POST")){
            parseRequestBody(reader,request);
        }

        return request;

    }
    public static void parseRequestBody(BufferedReader reader,Request request) throws IOException {
        if(request.getHeader("Contene-Type").toLowerCase().equals("application/x-wwwform-urlencoded")){
            int len=Integer.parseInt(request.getHeader("Contene-Length"));
            System.out.println(len);
            char[] buf=new char[len];
            reader.read(buf,0,len);
            request.setURLparam(new String(buf));
        }

    }
    public static void parseRequestHead(BufferedReader reader,Request request) throws IOException {
        String line;
        while((line=reader.readLine())!=null&&line.length()!=0){
            String[] kv=line.split(":");
            String key=kv[0];
            String value=kv[1];

            request.headers.put(key,value);
        }
    }
    public static void parseRequestLine(BufferedReader reader,Request request) throws IOException {
        String line=reader.readLine();
        if(line==null){
            throw new IOException("读到了结尾");
        }
        String[] fragments=line.split(" ");
        if(fragments.length!=3){
            throw new IOException("错误的请求行");
        }
        request.setMethod(fragments[0]);
        request.seturl(fragments[1]);
        request.setProtocol(fragments[2]);
    }

    private void setProtocol(String protocol) {
        this.protocol=protocol;
    }

    private void seturl(String url) throws UnsupportedEncodingException {
        String[] requesturl1=url.split("\\?");
        if(requesturl1.length>1) {
            setURLparam(requesturl1[1]);

        }

    }
    public void setURLparam(String queryString) throws UnsupportedEncodingException {
        for(String kv:queryString.split("&")){
            String[] keyValue=kv.split("=");
            String key=keyValue[0];
            String Value="";
            if(keyValue.length>1){
                Value=URLDecoder.decode(keyValue[1],"UTF-8");
            }
            requestParams.put(key,Value);
            System.out.println(requestParams);
        }
        
    }

    private void setMethod(String method) throws IOException {
        if(method.equals("POST")||method.equals("GET")){
            this.method=method;
            return;
        }
        throw new IOException("不支持的方法："+method);
    }


}
