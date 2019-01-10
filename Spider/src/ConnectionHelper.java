import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

class ConnectionHelper {

    private static int SUCCESS = 1;
    private static int FAIL = -1;

    private URL url;
    private String body;
    private int timeout = 8000;
    private int mState = 0;
    private Proxy proxy;
    private static String[] freq_used_subfix = new String[]{"mp3;audio", "jpeg;image"
            , "mp4;video", "jpg;image", "png;image", "apk;app", "exe;app", "gif;image"};

    /**
     * set the url to be open
     */
    ConnectionHelper url(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("Url fromat is wrong");
        }
        return this;
    }

    void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    void setProxy(Proxy p){
        this.proxy = p;
    }

    private void setBody(String data) {
        this.body = data;
    }

    /**
     * get the string data from the url
     */
    String body() {
        execute();
        int wait = 0;
        int INITIAL = 0;
        while (mState == INITIAL) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            if (++wait == timeout / 100)
//                break;
        }
        System.out.println(mState);
        return body;
    }

    private void getData() {
        BufferedReader bf = null;
        try {
            if (url != null) {
                HttpURLConnection conn;
                if (proxy == null)
                    conn = (HttpURLConnection) url.openConnection();
                else
                    conn = (HttpURLConnection) url.openConnection(proxy);
                bf = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                int i;
                char[] chars = new char[1024];
                StringBuilder data = new StringBuilder();
                while ((i = bf.read(chars)) != -1) {
                    data.append(chars, 0, i);
//                    System.out.println(chars);
                }
                if (data.length() > 0) {
                    setBody(data.toString());
                    mState = ConnectionHelper.SUCCESS;
                } else {
                    setBody("ERROR GET DATA");
                    mState = ConnectionHelper.FAIL;
                    System.out.println("No data is acquired from " + url.getPath());
                }
                bf.close();
            } else
                System.out.println("No URL Set");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Can not open url");
        } finally {
            if (bf != null) {
                try {
                    bf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void execute() {
        new Thread(this::getData).start();
    }
}
