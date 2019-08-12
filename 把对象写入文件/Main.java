package 把对象写入文件;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/12 15:45
 */
public class Main {
    private static Map<String, List<String>> list=new HashMap<>();

    public static void main(String[] args) throws IOException {
        list.put("hello",new ArrayList<>());
        list.put("chaoqiwen",new ArrayList<>());
        list.get("hello").add("0");
        list.get("hello").add("1");
        list.get("hello").add("2");
        list.get("hello").add("3");

        list.get("chaoqiwen").add("A");
        list.get("chaoqiwen").add("B");
        list.get("chaoqiwen").add("C");

       try( ObjectOutputStream os=new ObjectOutputStream(new FileOutputStream("list.bin"))){

           os.writeObject(list);
       }
    }
}
