package Transform;

/**
 * 状态转换机
 * @Author:chaoqiwen
 * @Date:2019/8/8 17:15
 */
enum State{
    NORMAL,        //开始状态，平时状态
    READ_CR,         //已经读到一个\r的状态
    READ_LF;          //已经读到\r\n的状态

        }
public class Test {
    private  static  String process(byte[] bytes){
        State state=State.NORMAL;
        StringBuilder sb=new StringBuilder();
        for(byte b:bytes){
            switch (b){
                case '\r' :
                    if(state==State.READ_CR){
                        sb.append((char)b);
                    }
                    state=State.READ_CR;
                    break;
                case '\n':
                    if(state==State.READ_CR){
                        state=State.READ_LF;
                        return sb.toString();
                    }
                    sb.append((char)b);
                    break;
                default:
                    if(state==State.READ_CR){
                        sb.append('\r');
                    }
                    state=State.NORMAL;
                    sb.append((char)b);

            }
        }
        return null;
    }
    public static void main(String[] args) {
        String m="hello\r\n";
        System.out.println(process(m.getBytes()));

    }
}
