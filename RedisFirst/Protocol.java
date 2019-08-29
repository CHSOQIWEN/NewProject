package First;

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
    public static Object read(ProtocolInputStream is) throws IOException {
        return process(is);
    }
    /*=>List<Object>{lpush,key,1,2,3}
    * =>commandName =lpush;
    * =>className=LPUSHCommand
    * =>LPUSHCommand 对象 command
    * command.setArgs({key,1,2,3});
    * command.run();   打印所有的args
    * */
    public static Command readCommand(ProtocolInputStream is) throws IOException, AllException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        Object o=read(is);
        if(!(o instanceof List)){
            throw new AllException("命令必须是Array类型");
        }
        List<Object> list=(List<Object>) o;
        if(list.size()<=1){
            throw new AllException("命令元素个数必须大于1");
        }
        Object o2=list.remove(0);
        if(!(o2 instanceof byte[])){
            throw new AllException("错误命令类型");
        }
        byte[] array=(byte[])o2;
        String commandName=new String(array);

        String classsName=String.format("First.%sCommand",commandName.toUpperCase());
        Class<?> cls=Class.forName(classsName);

        if(!Command.class.isAssignableFrom(cls)){
            throw new AllException("错误的命令");
        }
        Command command= (Command)cls.newInstance();
        command.setArgs(list);

        return command;
    }
    public static Object process(ProtocolInputStream is) throws  IOException {
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


    public  static  String processSimpleString(ProtocolInputStream is) throws IOException {

        return is.readLine();
    }
    public static String processError(ProtocolInputStream is) throws IOException {

        return is.readLine();
    }
    public static long processInteger(ProtocolInputStream is) throws IOException {

        return is.readInteger();
    }
    public static byte[] processBulkString(ProtocolInputStream is) throws IOException {
        int len=(int)is.readInteger();
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
    public static List<Object> processArray(ProtocolInputStream is) throws IOException {
        int len=(int )is.readInteger();
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



    /*public static void writeSimpleString(OutputStream os,String string) throws IOException {
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
    }*/

}
