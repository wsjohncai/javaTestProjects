import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingIP {

	public static void main(String[] args) {
		PingIP p = new PingIP();
		// System.out.println(p.getV4IP());
		if(p.ping("192.168.197.1")) {
			System.out.println("ping OK");
		} else System.out.println("Ping failed");
	}

	public boolean ping(String IP) {
		boolean isOnline = false;
		try {
			Process ping = Runtime.getRuntime().exec("ping " + IP + " -w 30 -n 1");
			BufferedReader cmdline = new BufferedReader(new InputStreamReader(ping.getInputStream()));
			String line = null;
			isOnline = true;
			while ((line = cmdline.readLine()) != null) {
				if(line.equals("«Î«Û≥¨ ±°£")) {
					isOnline = false;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return isOnline;
	}

	public String getV4IP() {
		String ip = "";
		String chinaz = "http://ip.chinaz.com";

		StringBuilder inputLine = new StringBuilder();
		String read = "";
		URL url = null;
		HttpURLConnection urlConnection = null;
		BufferedReader in = null;
		try {
			url = new URL(chinaz);
			urlConnection = (HttpURLConnection) url.openConnection();
			in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
			while ((read = in.readLine()) != null) {
				inputLine.append(read + "\r\n");
			}
			// System.out.println(inputLine.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
		Matcher m = p.matcher(inputLine.toString());
		if (m.find()) {
			String ipstr = m.group(1);
			ip = ipstr;
			// System.out.println(ipstr);
		}
		return ip;
	}
}
