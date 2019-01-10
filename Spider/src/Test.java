import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;

public class Test {
    public static void main(String[] args){
        BufferedReader bf = null;
        try {
            Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1", 54365));
            URL u = new URL("https://www.redtube.com/gay");
            bf = new BufferedReader(new InputStreamReader(u.openConnection(proxy).getInputStream()));
            int i;
            char[] chars = new char[1024];
            StringBuilder data = new StringBuilder();
            while ((i = bf.read(chars)) != -1) {
                data.append(chars, 0, i);
                System.out.println(chars);
            }
            System.out.println(data);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
