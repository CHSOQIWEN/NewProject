package First;

import java.util.List;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/10 17:27
 */
public class LRANGECommand implements Command {
    private List<Object> args;
    @Override
    public void setArgs(List<Object> args) {
        this.args=args;
    }

    @Override
    public void run() {

    }
}
