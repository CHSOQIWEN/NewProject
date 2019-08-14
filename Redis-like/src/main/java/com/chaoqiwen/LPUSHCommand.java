package com.chaoqiwen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/8 20:06
 */

/*头插的命令*/
    /*返回插入后的长度*/
public class LPUSHCommand implements Command {
    private  static final Logger logger= LoggerFactory.getLogger(LPUSHCommand.class);
    private List<Object> args;

    @Override
    public void setArgs(List<Object> args) {
        this.args=args;
    }

    @Override
    public void run(OutputStream os) throws IOException {
        if(args.size()!=2){
            Protocol.writeError(os,"命令至少需要两个参数");
        }
        //lpush 20190810_list 1 2 3 4
        Database database=Database.getInstance();
        String key=new String((byte[])args.get(0));
        /*String value="";*/
        String value=new String((byte[])args.get(1));
        logger.debug("运行的是 lpush命令：{} {}",key,value);


        List<String> list=  database.getList(key);//????????????????????????????
        list.add(0,value);
        logger.debug("插入后数据共有{}个",list.size());

        /*for(Object e:args) {
             value = new String((byte[]) e);
            list.add(0, value);
        }*/


        Protocol.writeInteger(os,list.size());


    }
}
