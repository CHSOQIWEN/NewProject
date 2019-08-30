package com.chaoqiwen.jerrymouse;

import com.chaoqiwen.jerrymouse.controllers.StaticController;
import com.chaoqiwen.jerrymouse.http.Controller;
import com.chaoqiwen.jerrymouse.http.Request;
import com.chaoqiwen.jerrymouse.http.Response;
import com.chaoqiwen.jerrymouse.http.Status;
import org.dom4j.DocumentException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/29 21:28
 */
public class Server {
    /*1、得到request和response
    * 2、根据url算出如果是静态文件，文件名是什么
    * 3、如果文件存在，当成静态请求，否则当成动态请求处理
    * 4、如果controller为空，说明既不是静态文件 也不是动态文件，404
    * 5、否则，根据方法是get还是post，调用doGet/doPost
    *    如果其他方法：405*/
    private  static  final String HOME=System.getenv("JM_HOME");
    private final Controller staticController=new StaticController();
    private final WebApp webApp=WebApp.parseXML();

    public Server() throws DocumentException, MalformedURLException {
    }


    public void run(int port) throws IOException {
        ServerSocket serverSocket=new ServerSocket(port);
        while (true){
            Socket socket=serverSocket.accept();
            Request request=Request.parse(socket.getInputStream());
            Response response=Response.build(socket.getOutputStream());

            //如果url对应的静态文件存在，就当成静态文件处理，不然就当成动态文件处理
           String filename=getFilename(request.getUrl());
           File file=new File(filename);
            Controller controller=null;
           if(file.exists()){
               //文件存在，当成静态文件处理
               controller=staticController;
           }else {
               //文件不存在，当成动态controller处理
               controller=webApp.findController(request);
           }
           /*根据url得到了动态controller的类名称，但是无法进行实例化，为什么呢？
           * AppClassLoader不知道ListController.class在哪
           * 怎么解决？
           *  自定义ClassLoader*/
           if(controller==null){
               response.setStatus(Status.NOT_FOUND);
               response.println(Status.NOT_FOUND.getReason());
           }else {
              switch (request.getMethod()){
                  case "GET":
                      controller.doGet(request,response);
                      break;
                  case "POST":
                      controller.doPost(request,response);
                      break;
                      default:
                          response.setStatus(Status.METHOD_NOT_ALLOWED);
                          response.println(Status.METHOD_NOT_ALLOWED.getReason());
              }
           }
           response.flush();

            socket.close();
        }
    }


    private String getFilename(String url) {
        if(url.equals("/")){
            url="/index.html";
        }
        return HOME+ File.separator+"webapp"+File.separator+url.replace("/",File.separator);
    }

    public static void main(String[] args) throws IOException, DocumentException {
        new Server().run(8088);
    }

    /*遇到的问题：unknown protocol e
    *         String filename；  new File（filename）.toURI().toURL();*/

    /*整个动态controller加载的过程
    * 1、提前解析好web.xml
    * 2、根据url得到controller名字
    * 3、根据controller名字得到controller完整类名称
    * 4、利用自定义的类加载器，加载controller类
    *   1）根据类名称得到*。class的文件路径 HOME/webapp/...
    *   2)读取文件内容
    *   3）调用defineClass变为Class对象
    * 5、利用反射，将该类实例化
    * */
}
