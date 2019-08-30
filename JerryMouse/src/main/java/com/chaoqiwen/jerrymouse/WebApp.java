package com.chaoqiwen.jerrymouse;

import com.chaoqiwen.jerrymouse.http.Controller;
import com.chaoqiwen.jerrymouse.http.Request;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/29 21:51
 */
public class WebApp {
    private Map<String,String> nameToClassName=new HashMap<>();
    private Map<String,String> urlToName=new HashMap<>();
    private final ClassLoader classLoader=new JMClassLoader();
    private static final String HOME=System.getenv("JM_HOME");

    public static WebApp parseXML() throws DocumentException, MalformedURLException {
        /*dom4j做web.xml的解析*/
        WebApp webApp=new WebApp();


        SAXReader reader = new SAXReader();
        URL url=xmlFilename();

        Document doc = reader.read(url);
        Element root = doc.getRootElement();
       /* System.out.println(root);
        System.out.println(root.element("controller"));*/
        for (Iterator<Element> it = root.elementIterator(); it.hasNext(); ) {
            Element element = it.next();
            switch (element.getName()) {
                case "controller": {
                    String name = element.element("name").getText();
                    String className = element.element("class").getText();
                    webApp.nameToClassName.put(name, className);
                    break;
                }
                case "mapping": {
                    String name = element.element("name").getText();
                    String urlPattern = element.element("url-pattern").getText();
                    webApp.urlToName.put(urlPattern,name);
                }
                default:

            }
        }
      return webApp;

        /*1、解析web.xml
         * 2、根据URL最终得到了处理该url的controller的类名称
         * 3、request。getUrl（）*/
    }
    private static URL xmlFilename() throws MalformedURLException {
        return new File(HOME+ File.separator+"webapp"+File.separator+"WEB-INF"+File.separator+"web.xml")
                .toURI().toURL();
    }

    public Controller findController(Request request) throws IOException {
        String name=findName(request.getUrl());
        if(name==null){
            return null;
        }
        String className =findClassName(name);
        if(className==null){
            return null;
        }
        Controller controller=findInstance(className);
        return controller;
    }

    //TODO:
    private Controller findInstance(String className) throws IOException {
        try {
            Class<?> cls= classLoader.loadClass(className);
            return (Controller) cls.newInstance();
        }catch (IllegalAccessException|InstantiationException|ClassNotFoundException e) {
            throw new IOException(e);
        }


    }

    private String findClassName(String name) {
        return nameToClassName.get(name);
    }

    private String findName(String url) {
        return urlToName.get(url);
    }


}
