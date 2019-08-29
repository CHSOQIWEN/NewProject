package First;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @Author:chaoqiwen
 * @Date:2019/8/2 15:43
 */
public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, AllException {
        //InputStream is=System.in;
        String message=":100\r\n";
        ByteInputStream is=new ByteInputStream(message.getBytes(),message.getBytes().length);
        Object o=Protocol.read(new ProtocolInputStream(is));
        System.out.println(o);

        String message2="$5\r\nhello\r\n";
        ByteInputStream is2=new ByteInputStream(message2.getBytes(),message2.getBytes().length);
        Object o2=Protocol.read(new ProtocolInputStream(is2));
        System.out.println(new String((byte[]) o2));

        String message3="*5\r\n$5\r\nlpush\r\n$3\r\nkey\r\n$1\r\n1\r\n$1\r\n2\r\n$1\r\n3\r\n";
        ProtocolInputStream is3 = new ProtocolInputStream(new ByteArrayInputStream(message3.getBytes()));
        Command command=Protocol.readCommand(is3);
        command.run();

    }
}


