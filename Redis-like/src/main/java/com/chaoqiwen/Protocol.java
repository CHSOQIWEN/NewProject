package com.chaoqiwen;

import com.chaoqiwen.AllException;
import com.chaoqiwen.Command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.rmi.RemoteException;



/**
 * @Author:chaoqiwen
 * @Date:2019/8/2 11:35
 */
public class Protocol {
    public static Object read(InputStream is) throws IOException {
        return process(is);
    }
    /*=>List<Object>{lpush,key,1,2,3}
    * =>commandName =lpush;
    * =>className=LPUSHCommand
    * =>LPUSHCommand 对象 command
    * command.setArgs({key,1,2,3});
    * command.run();   打印所有的args
    * */
    public static Command readCommand(InputStream is) throws IOException, AllException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Object o=read(is);
        if(!(o instanceof List)){
            throw new AllException("命令必须是Array类型");
        }
        List<Object> list=(List<Object>) o;
        if(list.size()<1){
            throw new AllException("命令元素个数必须大于1");
        }
        Object o2=list.remove(0);
        if(!(o2 instanceof byte[])){
            throw new AllException("错误命令类型");
        }
        byte[] array=(byte[])o2;
        String commandName=new String(array);

        String classsName=String.format("com.chaoqiwen.%sCommand",commandName.toUpperCase());
        Class<?> cls=Class.forName(classsName);

        if(!Command.class.isAssignableFrom(cls)){
            throw new AllException("错误的命令");
        }
        Command command= (Command)cls.newInstance();
        command.setArgs(list);

        return command;
    }
    public static Object process(InputStream is) throws  IOException {
        int b=is.read();
        if(b==-1){
            throw new RuntimeException("不应该读到结尾的");
        }
        switch (b){
            case '+':
                return processSimpleString(is);
            case '-':
                throw new RemoteException(processError(is));
            case ':':
                return processInteger(is);
            case '$':
                return processBulkString(is);
            case '*':
                return processArray(is);
            default:
                throw new RuntimeException("读到了错误类型");
        }
    }


    public  static  String processSimpleString(InputStream is) throws IOException {

        return readLine(is);
    }
    public static String processError(InputStream is) throws IOException {

        return readLine(is);
    }
    public static long processInteger(InputStream is) throws IOException {

        return readInteger(is);
    }
    public static byte[] processBulkString(InputStream is) throws IOException {
        int len=(int)readInteger(is);
        if(len==-1){
            return null;
        }
        byte[] r=new byte[len];
        for(int i=0;i<len;i++){
            int b=is.read();
            r[i]=(byte)b;

        }
        is.read();
        is.read();
        return r;
    }
    public static List<Object> processArray(InputStream is) throws IOException {
        int len=(int )readInteger( is);
        if(len==-1){
            return null;
        }
        List<Object> list=new ArrayList<>(len);
        for(int i=0;i<len;i++){
            try {
                list.add(process(is));
            }catch (RemoteException e){
                list.add(e);
            }

        }
        return list;
    }
    public static String readLine(InputStream is) throws IOException {
        boolean needread=true;
        StringBuilder sb=new StringBuilder();
        int b=-1;
        while (true){

            if(needread==true) {
                b = is.read();
                if (b == -1) {
                    throw new RuntimeException("不应该读到结尾");
                }
            }else {
                needread=true;
            }
            if(b=='\r'){
                int c=is.read();
                if(c==-1){
                    throw new RuntimeException("不应该读到结尾");
                }
                if(c=='\n'){
                    break;
                }
                if(c=='\r'){
                    sb.append(b);
                    b=c;
                    needread=false;
                }else {
                    sb.append((char) b);
                    sb.append((char) c);
                }
            }else {
                sb.append((char)b);
            }

        }
        return sb.toString();
    }

    public static long readInteger(InputStream is) throws IOException {
        int b=is.read();
        if(b==-1){
            throw new RuntimeException("不应该读到结尾");
        }
        StringBuilder sb=new StringBuilder();
        boolean isN=false;
        if(b==-1){
            throw new RuntimeException("不应该读到结尾");
        }
        if(b=='-'){
            isN=true;
        }else {
            sb.append((char)b);
        }
        while (true) {
            b=is.read();
            if (b == '\r') {
                int c = is.read();
                if (c == -1) {
                    throw new RuntimeException("不应该读到结尾");

                }
                if (c == '\n') {
                    break;
                }
                throw new RuntimeException("没有读到\\r\\n");

            }else {
                sb.append((char)b);
            }

        }
        long v=Long.parseLong(sb.toString());
        if(isN){
            v=-v;

        }
        return v;
    }




    public static void writeSimpleString(OutputStream os,String string) throws IOException {
        os.write('+');
        os.write(string.getBytes());
        os.write("\r\n".getBytes());
    }

    public static void writeError(OutputStream os,String message) throws IOException {
        os.write('-');
        os.write(message.getBytes());
        os.write("\r\n".getBytes());
    }
    public static void writeInteger(OutputStream os,long v) throws IOException {

        os.write(':');
        os.write(String.valueOf(v).getBytes());
        os.write("\r\n".getBytes());
    }

    public static void writeArray(OutputStream os, List<?> list) throws Exception {
        os.write('*');
        os.write(String.valueOf(list.size()).getBytes());
        os.write("\r\n".getBytes());
        for(Object o:list){
            if(o instanceof String){

                writeBulkString(os,(String)o);
            }else if(o instanceof Integer){
                writeInteger(os,(Integer)o);
            }else if(o instanceof Long){
                writeInteger(os,(Long)o);
            }else {
                throw new Exception("错误的类型");
            }
        }
    }

    public static void writeBulkString(OutputStream os, String s) throws IOException {
        byte[] buf=s.getBytes();
        os.write('$');
        os.write(String.valueOf(buf.length).getBytes());
        os.write("\r\n".getBytes());
        os.write(buf);
        os.write("\r\n".getBytes());

    }

    public static void writeNull(OutputStream os) throws IOException {
        os.write('$');
        os.write('-');
        os.write('1');
        os.write('\r');
        os.write('\n');
    }
}
