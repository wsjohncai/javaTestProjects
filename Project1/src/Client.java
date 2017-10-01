import java.net.*;
import java.io.*;
public class Client {

	public static void main(String[] args) {
		Socket s = null;
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			s = new Socket(InetAddress.getByName("192.168.3.5"),8000);
			byte[] b = new byte[512];
			bis = new BufferedInputStream(System.in);
			bos = new BufferedOutputStream(s.getOutputStream());
			while(bis.read(b)>=2) {
				bos.write(b);
				bos.flush();
			}
			bos.close();
			bis.close();
			s.shutdownOutput();
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
