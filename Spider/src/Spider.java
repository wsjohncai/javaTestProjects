import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Spider {

    private static final int MAXDOWNLOAD = 1000;
    private static final int MAXURL = 1000;
    private static final int MAXTASK = 3;
    private static final String STOREPATH = "E:\\Temp\\test";

    private String[] filter;
    private int taskNum = 0;
    private Vector<String> urls = new Vector<>(),
            downloads = new Vector<>();
    private int nap = 2000; //a nap of scan time
    private int downloaded = 0; //Maximum of file download
    private boolean downloadInRun = false, analyzeInRun = false;
    private Proxy proxy = null;
    private DownloadListener listener = new DownloadListener() {
        @Override
        public synchronized void onFinished(int status) {
            if (status == Downloader.SUCCESS)
                downloaded++;
            if (taskNum > 0)
                taskNum--;
            System.out.println("Downloaded " + downloaded + "; Task number: " + taskNum);
        }
    };

    /**
     * Read address and download type from console
     */
    private void analyzeInput() {
        System.out.print("Setup proxy? y/n: ");
        Scanner sc = new Scanner(System.in);
        String arg1 = sc.nextLine();
        if(arg1.matches("[yY]")){
            System.out.println("Input the proxy address(e.g. 127.0.0.1): ");
            String proxyAddr = sc.nextLine();
            System.out.println("Input the proxy port: ");
            String p = sc.nextLine();
            int proxyPort = Integer.valueOf(p);
            proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyAddr, proxyPort));
        }
        System.out.print("Input a address: ");
        String first_addr = sc.nextLine();
        System.out.print("Input download type, separate type with \",\": ");
        String types = sc.nextLine();
        if (types.length() > 0) {
            if (types.indexOf(',') != -1)
                filter = types.split(",");
            else
                filter = new String[]{types};
        }
        if (!first_addr.matches("https?://.+")) {
            first_addr = "http://" + first_addr;
        }
        urls.add(first_addr);
    }

    /**
     * 从指定网页数据中提取URL
     */
    private void extractUrls(String addr, String data) {
        Pattern p = Pattern.compile("(?:https?://|www|(?<![A-Z</])/(?=[A-Z0-9]))" +
                "[-A-Z0-9+&@#/%=~_|$?!:,.]*[A-Z0-9+&@#/%=~_|$]", Pattern.CASE_INSENSITIVE);
        if (data == null)
            return;
        Matcher matcher = p.matcher(data);
//        int urlC = 0, downC = 0;
        while (matcher.find()) {
            String s = matcher.group();
            if (s.matches(".+(?:css|js)"))
                continue;
            if (s.matches("/.+")) {
                if (addr.matches(".+/"))
                    s = s.substring(1, s.length());
                s = addr + s;
            } else if (s.matches("www.+"))
                s = "http://" + s;
            //检查是否含有特定的后缀
            boolean isAdded = false;
            for (String subfix : filter) {
                if (s.matches(".+\\." + subfix)) {
                    String name = s.substring(s.lastIndexOf('/') + 1, s.length());
                    if (name.matches("[^/\\\\*\"?:><|]+"))
                        downloads.add(s);
                    isAdded = true;
//                    downC++;
//                    System.out.println("AddToDownloadList: " + s);
                    break;
                }
            }
            if (!isAdded) {
                if (s.matches(".+\\.\\w+"))
                    continue;
                if (urls.size() < MAXURL) {
                    urls.add(s);
//                    urlC++;
                }
//                System.out.println("AddToURLList: " + s);
            }
        }
//        System.out.print("Get " + (downC + urlC) + " URL from " + addr);
//        System.out.print("; DownloadList size: " + downloads.size());
//        System.out.println("; URL List size " + urls.size());
    }

    /**
     * Continually download file in the download list,
     * if the list is empty, wait
     */
    private void downloadLoop() {
        new Thread(() -> {
            System.out.println("Download Thread Started");
            downloadInRun = true;
            String[] downloading = new String[]{"", "", ""};
            int index = 0;
            while (downloaded < MAXDOWNLOAD) {
                try {
                    if (taskNum < MAXTASK) {
                        if (downloads.size() > 0) {
                            boolean isIn = false;
                            String link = downloads.get(0);
                            downloads.remove(0);
                            for (String s : downloading) {
                                if (s.equals(link)) {
                                    isIn = true;
                                    break;
                                }
                            }
                            if (isIn)
                                continue;
                            downloading[index] = link;
                            index = (index + 1) % 3;
                            String fileName = link.substring(link
                                    .lastIndexOf('/') + 1, link.length());
                            System.out.println("Starting Download " + link);
                            Downloader d = new Downloader(listener).url(link);
                            if (d != null) {
                                d.save(STOREPATH + "\\" + fileName).execute();
                                taskNum++;
                            }
                        }
                    } else
                        Thread.sleep(nap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            downloadInRun = false;
            System.out.println("Download Thread Finished");
        }).start();
    }

    /**
     * Get info from a url and extract all links inside it
     */
    private void analyzeUrlFromList() {
        ConnectionHelper helper = new ConnectionHelper();
        new Thread(() -> {
            analyzeInRun = true;
            while (true) {
                try {
                    if (urls.size() > 0 && downloaded < MAXDOWNLOAD) {
                        String addr = urls.get(0);
                        urls.remove(0);
//                    System.out.println("Opening " + addr);
                        if(proxy != null) helper.setProxy(proxy);
                        String data = helper.url(addr).body();
                        extractUrls(addr, data);
                    } else
                        break;
                    Thread.sleep(nap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            analyzeInRun = false;
            System.out.println("Analyze Url Finished");
        }).start();
    }

    private void monitor() {
        new Thread(() -> {
            while (true) {
                try {
                    if (downloaded == MAXDOWNLOAD)
                        break;
                    if (!analyzeInRun) {
                        if (urls.size() == 0) {
                            System.out.println("No url in list, start new");
                            analyzeInput();
                        }
                        analyzeUrlFromList();
                    }
                    if (!downloadInRun)
                        downloadLoop();
                    Thread.sleep(nap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        Spider spider = new Spider();
        spider.analyzeInput();
        if (spider.urls.size() == 0)
            System.exit(-1);
        spider.monitor();
    }
}
