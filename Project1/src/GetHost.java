
import java.net.*;
import java.io.*;
import java.util.*;

public class GetHost {

	public static void main(String[] args) {
//		Scanner sc = new Scanner(System.in);
//		String url = sc.nextLine();
//		URL link;
//		try {
//			link = new URL(url);
//			System.out.println("Original:"+url);
//			String s = removeWwwFromUrl(link.toString());
//			System.out.println(s);
//			System.out.println("Host:"+link.getHost());
//			System.out.println("File:"+link.getFile());
//			System.out.println("Path:"+link.getPath());
//			System.out.println("Port:"+link.getPort());
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} finally {
//			sc.close();
//		}
		GetHost gh = new GetHost();
		gh.localHost();
		
	}
	
	private static String removeWwwFromUrl(String url) {
		int index = url.indexOf("://www.");
		if (index != -1) {
			return url.substring(0, index + 3) + url.substring(index + 7);

		}

		return (url);

	}
	
	public void localHost() {
		try {
			Enumeration<NetworkInterface> access = NetworkInterface.getNetworkInterfaces();
			
			while(access.hasMoreElements()) {
				NetworkInterface iphost = access.nextElement();
				Enumeration<InetAddress> ips = iphost.getInetAddresses();
				print(iphost.toString());
				while(ips.hasMoreElements())
				print(ips.nextElement().toString());
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void print(String s) {
		System.out.println(s);
	}
	
}
