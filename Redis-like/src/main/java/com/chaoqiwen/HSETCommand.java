package com.chaoqiwen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/11 21:36
 *//**/

    /*更新返回0，插入返回1*/
public class HSETCommand implements  Command {
    private  static final Logger logger= LoggerFactory.getLogger(LPUSHCommand.class);
    private List<Object> args;
    @Override
    public void setArgs(List<Object> args) {
        this.args=args;

    }

    @Override
    public void run(OutputStream os) throws IOException {
        String key=new String((byte[])args.get(0));
        String field=new String((byte[])args.get(1));
        String value=new String((byte[])args.get(2));
        Map<String,String> hash=Database.getHashes(key);
        boolean isupdate=hash.containsKey(field);//存在就更新
        hash.put(field,value);
        if(isupdate){
            Protocol.writeInteger(os,0);
        }else {
            Protocol.writeInteger(os,1);
        }


    }
}
