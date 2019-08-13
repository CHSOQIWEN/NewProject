package 从文件读;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/12 15:55
 */
public class Main {

    private static Map<String, List<String>> list = new HashMap<>();

    public static void main(String[] args) throws IOException {


        try (ObjectInputStream is = new ObjectInputStream(new FileInputStream("list.bin"))) {

            try {
                list = (Map<String, List<String>>) is.readObject();
                System.out.println(list);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}



