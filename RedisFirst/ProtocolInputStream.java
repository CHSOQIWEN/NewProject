package First;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/8 16:05
 */
public class ProtocolInputStream extends FilterInputStream {
    public ProtocolInputStream(InputStream in) {
        super(in);
    }

    public String readLine() throws IOException {
        boolean needread=true;
        StringBuilder sb=new StringBuilder();
        int b=-1;
        while (true){

            if(needread==true) {
                b = in.read();
                if (b == -1) {
                    throw new RuntimeException("不应该读到结尾");
                }
            }else {
                needread=true;
            }
            if(b=='\r'){
                int c=in.read();
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
    public long readInteger() throws IOException {
        int b=in.read();
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
            b=in.read();
            if (b == '\r') {
                int c = in.read();
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
}
