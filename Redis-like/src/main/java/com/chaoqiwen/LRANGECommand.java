package com.chaoqiwen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/12 14:06
 */
/*返回某个定义区间的数据*/
public class LRANGECommand implements Command {
    private  static final Logger logger= LoggerFactory.getLogger(LPUSHCommand.class);
    private List<Object> args;
    @Override
    public void setArgs(List<Object> args) {
        this.args=args;
    }

    @Override
    public void run(OutputStream os) throws IOException {
        String key=new String((byte[])args.get(0));
        int start=Integer.parseInt(new String((byte[])args.get(1)));
        int end=Integer.parseInt(new String((byte[])args.get(2)));
        logger.debug("运行的是 lrange命令：{} {} {}",key,start,end);

        List<String> list=Database.getList(key);
        if(end<0){
            end=list.size()+end;

        }
        List<String> result=list.subList(start,end+1);
        try {
            Protocol.writeArray(os,result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("运行结果是：{}",list.toString());

    }
}
