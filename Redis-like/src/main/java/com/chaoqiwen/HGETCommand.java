package com.chaoqiwen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/12 14:33
 */
/*如果 有值，就以bulk string的方式返回
* 没有值返回null*/
public class HGETCommand implements Command {
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


        Map<String,String> hash=Database.getHashes(key);
        String value=hash.get(field);
        logger.debug("运行的是 hget命令：{} {} {}",key,field,value);
        if(value!=null){
            Protocol.writeBulkString(os,value);
        }else {
            Protocol.writeNull(os);
        }
    }
}
