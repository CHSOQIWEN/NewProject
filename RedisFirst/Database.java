package First;

import java.util.*;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/10 16:31
 */
/*管理存储的所有数据
* */
public class Database {
/*
* 单例模式*/
    private  static Database instance=new Database();
    public   static  Database getInstance(){
        return instance;
    }

    /*Redis支持五种数据类型*/
    //String类型
    private Map<String,String> strings;
    //hash类型
    private  Map<String ,Map<String,String>> hashes;
    //list类型
    private static Map<String, List<String>> lists=new HashMap<>();
    //set类型
    private Map<String, Set<String>> sets;
    //zset类型  有序set
    private  Map<String, LinkedHashSet<String>> zsets;


    public static List<String> getList(String key){
        List<String> list=lists.get(key);
        if(list==null){
            list=new ArrayList<>();
            lists.put(key,list);

        }
        return list;
    }

}
