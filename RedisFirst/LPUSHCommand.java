package First;

import java.util.List;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/8 20:06
 */

/*头插的命令*/
public class LPUSHCommand implements Command {
    private List<Object> args;

    @Override
    public void setArgs(List<Object> args) {
        this.args=args;
    }

    @Override
    public void run() {
        //lpush 20190810_list 1 2 3 4
        //Database database=Database.getInstance();
        String key=new String((byte[])args.get(0));

        List<String> list=  Database.getList(key);//????????????????????????????

        for(Object e:args){
            String value=new String((byte[])e);
           list.add(0,value);
        }
        System.out.println(list.size());

    }
}
