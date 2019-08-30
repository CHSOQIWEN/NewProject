package com.chaoqiwen.jerrymouse.http;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/29 21:19
 */
public class Response {
    /*相应组装：行的组装、头的组装、体的组装*/
    private OutputStream os;
    private Status status;
    private Map<String,String> headers =new HashMap<>();
    private final byte[] buf=new byte[8192];
    private int offset=0;

    public Response(OutputStream os){
        this.os=os;
    }

    public static Response build(OutputStream os){

        Response response=new Response(os);
        response.setStatus(Status.OK);
        response.setContentType("text/html");
        response.setServer();
        response.setDate();

        return response;
    }

    private void setDate() {
        SimpleDateFormat sdf=new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        setHeader("Date",sdf.format(new Date()));

    }

    private void setServer() {
        setHeader("Server","jerrymouse/1.0");
    }

    public void setStatus(Status status) {
        this.status=status;

    }

    public void setContentType(String s){
        setHeader("Content-type",s+";charset=UTF-8");
    }

    private void setHeader(String key, String value) {
        headers.put(key,value);
    }
    public void  print(Object o) throws IOException {
        //TODO:不考虑buf溢出的问题
        byte[] src=o.toString().getBytes("UTF-8");
        System.arraycopy(src,0,buf,offset,src.length);
        offset+=src.length;
    }

    public void println(Object o) throws IOException{
        print(String.format("%s%n",o));
    }
    public void printf(String format,Object... o) throws IOException {
        print(new Formatter().format(format,o));
    }

    public void flush() throws IOException {
        setHeader("Content-Length",String.valueOf(offset));
        sendResponseLine();
        sendResponseHeaders();
        sendResponseBody();
    }



    private void sendResponseLine() throws IOException {
        String responseLine=String.format("HTTP/1.0 %d %s\r\n",status.getCode(),status.getReason());
        os.write(responseLine.getBytes("UTF-8"));
    }

    private void sendResponseHeaders() throws IOException {
        for(Map.Entry<String,String> entry:headers.entrySet()){
            String key=entry.getKey();
            String value=entry.getValue();
            String headerLine=String.format("%s: %s\r\n",key,value);
            os.write(headerLine.getBytes("UTF-8"));
        }
        os.write('\r');
        os.write('\n');
    }

    private void sendResponseBody() throws IOException {
        os.write(buf,0,offset);
    }

    public void write(byte[] b, int off, int len) {
        System.arraycopy(b,off,buf,offset,len);
        offset+=len;

    }
}
