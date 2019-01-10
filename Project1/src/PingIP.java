import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.Cookie;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.protocol.HttpClientContext;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingIP {

    public static void main(String[] args) {
        PingIP p = new PingIP();
//        p.useHttpClientGet();
        System.out.println(p.get360WeatherData());
    }

    public boolean ping(String IP) {
        boolean isOnline = false;
        try {
            Process ping = Runtime.getRuntime().exec("ping " + IP + " -w 30 -n 1");
            BufferedReader cmdline = new BufferedReader(new InputStreamReader(ping.getInputStream()));
            String line;
            isOnline = true;
            while ((line = cmdline.readLine()) != null) {
                if (line.equals("请求超时。")) {
                    isOnline = false;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isOnline;
    }

    public String getIP() {
        String ip = "";
        String chinaz = "http://ip.chinaz.com";

        StringBuilder inputLine = new StringBuilder();
        String read;
        URL url;
        HttpURLConnection urlConnection;
        BufferedReader in = null;
        try {
            url = new URL(chinaz);
            urlConnection = (HttpURLConnection) url.openConnection();
            int reqCode = urlConnection.getResponseCode();
            if (reqCode == HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
                while ((read = in.readLine()) != null) {
                    inputLine.append(read).append("\r\n");
                }
            } else {
                System.out.println("Request Failed");
            }
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

        Pattern p = Pattern.compile("<dd class=\"fz24\">(.*?)</dd>");
        Matcher m = p.matcher(inputLine.toString());
        if (m.find()) {
            ip = m.group(1);
            // System.out.println(ipstr);
        }
        return ip;
    }

    private void useHttpClientGet() {
        long code = 101280102;
        Calendar c = Calendar.getInstance();
        long t = c.getTimeInMillis();
        //http://tq.360.cn101010100&t=1524546353156&c=1524647363256&_jsonp=renderData&_=1524546353159
        long x = t + code;
        System.out.println(code + " " + t + " " + x);
        String link = "http://tq.360.cn/api/weatherquery/querys?app=tq360&code=" + code + "&t=" + t + "&c=" + x;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            // Create a local instance of cookie store
            final CookieStore cookieStore = new BasicCookieStore();

            // Create local HTTP context
            final HttpClientContext localContext = HttpClientContext.create();
            // Bind custom cookie store to the local context
            localContext.setCookieStore(cookieStore);

            final HttpGet httpget = new HttpGet(link);

            System.out.println("Executing request " + httpget.getMethod() + " " + httpget.getUri());

            // Pass local context as a parameter
            try (CloseableHttpResponse response = httpclient.execute(httpget, localContext)) {
                System.out.println("----------------------------------------");
                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                final List<Cookie> cookies = cookieStore.getCookies();
                for (Cookie cooky : cookies) {
                    System.out.println("Local cookie: " + cooky);
                }
                response.getEntity().writeTo(System.out);
                EntityUtils.consume(response.getEntity());
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public String get360WeatherData() {
        String link = "http://tq.360.cn";
        StringBuilder inputLine = new StringBuilder();
        String read;
        URL url;
        HttpURLConnection urlConnection;
        BufferedReader in = null;
        try {
            Calendar c = Calendar.getInstance();
            long code = 101280101;
            long t = c.getTimeInMillis();
            //http://tq.360.cn101010100&t=1524546353156&c=1524647363256&_jsonp=renderData&_=1524546353159
            long x = t + code;
            System.out.println(code + " " + t + " " + x);
            link += "/api/weatherquery/querys?app=tq360&code=" + code + "&t=" + t + "&c=" + x;
            url = new URL(link);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(8000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(8000);
            urlConnection.setRequestProperty("connection", "Keep-Alive");
            urlConnection.connect();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            while ((read = in.readLine()) != null) {
                inputLine.append(read).append("\r\n");
            }
            urlConnection.disconnect();
            return inputLine.toString();
            // System.out.println(inputLine.toString());
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
        return "NoData";
    }
}
