package First;

import java.util.List;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/8 19:12
 */
public interface Command {
    void setArgs(List<Object> args);
    void run();
}
