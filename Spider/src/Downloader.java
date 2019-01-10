import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Objects;

public class Downloader {

    public static final int FAILED = -1;
    public static final int SUCCESS = 0;
    public static final int ALREADY_EXIST = 1;

    private HttpURLConnection conn;
    private RandomAccessFile file;
    private String fileName;
    private int status = FAILED;
    private long curLength = 0;
    private long totalLength = -1;
    private DownloadListener listener;
    private File fileref;
    private FileChannel channel;
    private FileLock lock;
    private boolean isExist = false;

    Downloader(DownloadListener listener) {
        this.listener = listener;
    }

    private void setStartPoi(String value) {
        if (conn != null) {
            conn.setRequestProperty("Range", value);
        }
    }

    private InputStream byteStream() throws IOException {
        if (conn != null)
            return conn.getInputStream();
        return null;
    }

    private long getContentlenngth() {
        if (totalLength != -1)
            return totalLength;
        else if (conn != null) {
            URL u = conn.getURL();
            try {
                totalLength = u.openConnection().getContentLengthLong();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return totalLength;
        } else
            return -1;
    }

    Downloader url(String url) {
        try {
            URL url1 = new URL(url);
            conn = (HttpURLConnection) url1.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            return this;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Bad url");
        }
        return null;
    }

    Downloader save(String path) {
        fileName = path.substring(path.lastIndexOf('\\') + 1
                , path.length());
        File temp = new File(path);
        this.fileref = temp;
        boolean fileExist = false;
        boolean dirExist = false;
        try {
            if (!temp.exists()) {
                String tp = path.substring(0, path.lastIndexOf('\\'));
                File tpf = new File(tp);
                if (!tpf.exists()) { //如果文件夹不存在，创建文件夹
                    //如果创建文件夹失败
                    if (tpf.mkdir())
                        dirExist = true;
                } else {
                    dirExist = true;
                }
                if (temp.createNewFile())
                    fileExist = true;
            } else {
                dirExist = true;
                fileExist = true;
                curLength = temp.length();
                //如果已下载长度等于网络文件长度，结束
                if (curLength >= getContentlenngth()) {
                    isExist = true;
                    return this;
                }
            }
            if (fileExist && dirExist) {
                file = new RandomAccessFile(path, "rws");
                channel = file.getChannel();
                while (true) {
                    lock = channel.lock();
                    break;
                }
                if ((curLength = fileref.length()) >= getContentlenngth())
                    isExist = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Fail to create RandomAccessFile");
        }
        return this;
    }

    private void callback() {
        if (status == SUCCESS)
            System.out.println("Download: " + fileName + " success.");
        else if (status == FAILED)
            System.out.println("Download: " + fileName + " failed.");
        else
            System.out.println("Download: " + fileName + " already exists.");
        listener.onFinished(status);
    }

    void execute() {
        new Thread(() -> {
            if (isExist)
                status = ALREADY_EXIST;
            else if (conn != null && file != null) {
                BufferedInputStream bis = null;
                try {
                    //如果已下载一部分，那么继续下载
                    conn.disconnect();
                    if (curLength > 0) {
                        setStartPoi("byte=" + curLength + "-");
                        file.seek(curLength);
                    }
                    bis = new BufferedInputStream(Objects.requireNonNull(byteStream()));
                    byte[] bytes = new byte[1024];
                    int readLen, totalLen = 0;
                    while ((readLen = bis.read(bytes)) != -1) {
                        file.write(bytes, 0, readLen);
                        totalLen += readLen;
                        //当下载结束时，跳出
                        if (totalLen == totalLength)
                            break;
                    }
                    status = SUCCESS;
                } catch (IOException e) {
                    e.printStackTrace();
                    status = FAILED;
                    callback();
                } finally {
                    try {
                        if (bis != null)
                            bis.close();
                        if(lock != null)
                            lock.release();
                        if(channel != null)
                            channel.close();
                        if (file != null)
                            file.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (fileref != null && status == FAILED)
                        fileref.delete();
                    if (conn != null)
                        conn.disconnect();
                }

            } else {
                System.out.println("Initialization fail.");
            }
            callback();
        }).start();
    }

}
