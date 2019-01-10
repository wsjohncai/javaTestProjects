import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WeatherHtmlAnalyse {
    Scanner sc = null;

    private static final int LVL_PROV = 1;
    private static final int LVL_CITY = 2;
    private static final int LVL_COUNTY = 3;
    private static final String DIRECTCITY = "北京|上海|重庆|天津|香港|澳门";

    private String readFromConsole(String hint) {
        println(hint);
        StringBuilder line = new StringBuilder();
        if (sc != null) {
            while (true) {
                String t = sc.nextLine();
                if (t.equals("\\q"))
                    break;
                line.append(t);
            }
            println("文本读取结束！正在进行解析...");
        }
        return line.toString();
    }

    private void println(String text) {
        System.out.println(text);
    }

    private void print(String text) {
        System.out.print(text);
    }

    private int provId = -1, cityId = -1, countyId = -1;

    private void init() {
        println("1.查询所有存储信息  2.存市区级信息 3.存单个区信息 4.还原上一步操作 5.查询数据");
        sc = new Scanner(System.in);
        int op = sc.nextInt();
        sc.nextLine();
        switch (op) {
            case 1:
                readAll();
                break;
            case 2:
            case 3:
            case 5:
                backup();
                readFromJson(LVL_PROV);
                System.out.println("请选择一个省级ID：");
                provId = sc.nextInt();
                sc.nextLine();
                if (provId == -1)
                    break;
                if (op == 2)
                    storeCounties();
                else if(op==3){
                    readFromJson(LVL_CITY);
                    println("请选择一个市级ID：");
                    cityId = sc.nextInt();
                    sc.nextLine();
                    readFromJson(LVL_COUNTY);
                    storeACounty();
                } else {
                    readFromJson(LVL_CITY);
                    println("请选择一个市级ID：");
                    cityId = sc.nextInt();
                    sc.nextLine();
                    readFromJson(LVL_COUNTY);
                    println("请选择一个区级ID：");
                    countyId = sc.nextInt();
                }
                break;
            case 4:
                restore();
            default:
                break;
        }
    }

    private void requestWeatherData() {
        println("请输入查询的数据类型：");
        String type = sc.nextLine();
        JSONObject county = countyArray.getJSONObject(countyId-1);
        String urls = "http://www.weather.com.cn/weather1dn/"+county.getString("weatherId")+".shtml";
        try {
            URL url = new URL(urls);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        switch (type) {
            case "h" :
                break;
            case "w":
                break;
        }
    }

    //存储指定区
    private void storeACounty() {
        String data = readFromConsole("请输入所有区的数据：");
        Matcher matcher = Pattern.compile("<a.*?href=\".+?(\\d+)\\.shtml\".*?>(.+?)</a>").matcher(data);
        int idx = 1;
        String n = ((JSONObject) cityArray.get(cityId - 1)).getString("name");
        if (countyArray == null)
            countyArray = new JSONArray();
        countyArray.clear();
        while (matcher.find()) {
            Map<String, String> map = new HashMap<>();
            map.put("id", "" + idx++);
            map.put("name", matcher.group(2).equals("城区") ? n : matcher.group(2));
            map.put("weatherId", matcher.group(1));
            println(map.get("id") + ":" + map.get("name") + "," + map.get("weatherId"));
            countyArray.element(map);
        }
        city.element("counties", countyArray);
        cityArray.element(cityId - 1, city);
        province.element("cities", cityArray);
        provArray.element(provId - 1, province);
        country.element("provs", provArray);
        writeToFile(country);
        println("保存成功");
        init();
    }

    //还原备份文件
    private void restore() {
        File res = new File("E:\\Temp\\china.json"), bak = new File("E:\\Temp\\china.bak");
        if (!bak.exists())
            println("备份文件不存在！");
        if (res.exists())
            if (res.delete()) {
                if (bak.renameTo(res)) {
                    println("还原成功");
                } else
                    println("删除原文件失败，请手动还原文件");
            } else
                println("删除原文件失败，请手动还原文件");

    }

    private void backup() {
        File ori = new File("E:\\Temp\\china.json"), bak = new File("E:\\Temp\\china.bak");
        try {
            FileReader fr = new FileReader(ori);
            FileWriter fw = new FileWriter(bak);
            char[] chars = new char[512];
            int len;
            while ((len = fr.read(chars)) != -1) {
                fw.write(chars, 0, len);
                fw.flush();
            }
            fr.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取所有信息
    private void readAll() {
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fr = new FileReader("E:\\Temp\\china.json");
            char[] chars = new char[215];
            int len;
            while ((len = fr.read(chars)) != -1) {
                sb.append(chars, 0, len);
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONArray provs = JSONObject.fromObject(sb.toString()).getJSONArray("provs");
        for (Object prov1 : provs) {
            JSONObject prov = (JSONObject) prov1;
            println(prov.getString("name") + ": ");
            JSONArray cities = prov.getJSONArray("cities");
            for (Object city1 : cities) {
                print("\t");
                JSONObject city = (JSONObject) city1;
                println(city.toString() + "; ");
            }
            println("");
        }
        init();
    }

    //存储区级信息
    private void storeCounties() {
        readFromJson(LVL_CITY);
        JSONArray array = new JSONArray();
        String citiesRaw = readFromConsole("请输入省内所有市数据：");
        int idx = 1;
        List<String> cityFirstList = new ArrayList<>();
        Matcher matcher = Pattern.compile("<a\\s*?href=\"(.+?)\".*?>(.+?)</a>").matcher(citiesRaw);
        print("更细城市信息：");
        while (matcher.find()) {
            Map<String, String> map = new HashMap<>();
            String name = matcher.group(2);
            String url = matcher.group(1);
            if (province.getString("name").matches(DIRECTCITY) && name.equals("城区"))
                name = province.getString("name");
            map.put("name", name);
            map.put("id", (idx++) + "");
            array.element(map);
            cityFirstList.add(name + ";" + url);
            print(name + " ");
        }
        println("");
        province.put("cities", array);
        cityArray = province.getJSONArray("cities");
        provArray.element(provId - 1, province);
        country.element("provs", provArray);
        writeToFile(country);
        handleUrls(cityFirstList);
    }

    private void handleUrls(List<String> cityFirstList) {
        new Thread(() -> {
            while (cityFirstList.size() > 0) {
                //判断是否为直辖市
                boolean isDirectCity = province.getString("name").matches(DIRECTCITY);
                String[] info = cityFirstList.get(0).split(";");
                String name = info[0];
                String urls = info[1];
                int cityIdx = 0;
                boolean isInArray = false;
                JSONObject cityObj = null;
                for (Object o : cityArray) {
                    String temp = ((JSONObject) o).getString("name");
                    if ((isDirectCity && name.equals("城区")) || temp.equals(name)) {
                        cityObj = (JSONObject) o;
                        isInArray = true;
                        break;
                    }
                    cityIdx++;
                }
                cityFirstList.remove(0);
                if (!isInArray) continue;
                println("读取：" + name + ":" + urls);
                if (isDirectCity) { //若为直辖市，那么直接将此URL作为区级信息存储
                    storeCountiesIntoJson(urls, cityIdx, cityObj, true);
                    continue;
                }
                URL url = null;
                try {
                    url = new URL(urls);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    println("读取链接错误");
                }
                try {
                    if (url != null) {
                        BufferedReader br;
                        StringBuilder body = new StringBuilder();
                        br = new BufferedReader(new InputStreamReader(url.openStream()));
                        int idx = 1;
                        while (true) {
                            String temp = br.readLine();
                            if (idx++ < 250)
                                continue;
                            else if (idx > 400)
                                break;
                            if (temp == null)
                                break;
                            body.append(temp);
                        }
                        br.close();
                        //读取网页中区级信息
                        Matcher m_cities = Pattern.compile("5002(?!\\d+).+?</div>", Pattern.DOTALL).matcher(body.toString());
                        if (m_cities.find()) {
                            String countiesRaw = m_cities.group();
                            storeCountiesIntoJson(countiesRaw, cityIdx, cityObj, false);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            provArray.element(provId - 1, province);
            country.element("provs", provArray);
            writeToFile(country);
            println("保存成功");
            init();
        }).start();
    }

    //存储指定市的区级信息
    private void storeCountiesIntoJson(String countiesRaw, int cityIdx, JSONObject cityObj, boolean isDirectCity) {
        String name = cityObj.getString("name");
        Matcher m_info;
        if (!isDirectCity)
            m_info = Pattern.compile("<a.*?href=\".+?(\\d+)\\.shtml\".*?>(.+?)</a>").matcher(countiesRaw);
        else m_info = Pattern.compile(".+?(\\d+)\\.shtml").matcher(countiesRaw);
        int idx1 = 1;
        Map<String, String> map = new HashMap<>();
        JSONArray array = new JSONArray();
        while (m_info.find()) {
            String n;
            if (isDirectCity) {
                n = name;
            } else
                n = m_info.group(2);
            if (n.equals("城区"))
                n = name;
            String wi = m_info.group(1);
            print(n + ":" + wi + " ");
            map.put("name", n);
            map.put("id", "" + (idx1++));
            map.put("weatherId", wi);
            array.element(map);
        }
        println("");
        cityObj.element("counties", array);
        cityArray.element(cityIdx, cityObj);
        province.element("cities", cityArray);
    }

    //写入文件
    private void writeToFile(JSONObject jsonObject) {
        try {
            FileWriter writer = new FileWriter("E:\\Temp\\china.json");
            jsonObject.write(writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject country, province, city;
    private JSONArray provArray, cityArray, countyArray;

    private void readFromJson(int level) {
        //从文件中读取出json文本
        File file = new File("E:\\Temp\\china.json");
        StringBuilder text = new StringBuilder();
        FileReader fr;
        char[] chars = new char[256];
        try {
            fr = new FileReader(file);
            int len;
            while ((len = fr.read(chars)) != -1) {
                text.append(chars, 0, len);
            }
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //根据json文本读取相应内容
        //读取所有的省
        country = JSONObject.fromObject(text.toString());
        provArray = country.getJSONArray("provs");
        for (Object aJsonArray : provArray) {
            JSONObject o = (JSONObject) aJsonArray;
            if (level == LVL_PROV)
                print(o.getString("id") + ":" + o.getString("name") + "; ");
        }
        if (level == LVL_PROV)
            System.out.println();
        if (level == LVL_PROV)
            return;
        //读取对应ID省的所有的市信息
        province = provArray.getJSONObject(provId - 1);
        cityArray = province.getJSONArray("cities");
        for (Object oa : cityArray) {
            JSONObject o = (JSONObject) oa;
            if (level == LVL_CITY)
                print(o.getString("id") + ":" + o.getString("name") + "; ");
        }
        if (level == LVL_CITY)
            System.out.println();
        if (level == LVL_CITY)
            return;
        city = cityArray.getJSONObject(cityId - 1);
        if (city.has("counties")) {
            countyArray = city.getJSONArray("counties");
            for (Object oa : countyArray) {
                JSONObject o = (JSONObject) oa;
                if (level == LVL_COUNTY)
                    print(o.getString("id") + ":" + o.getString("name") + "; ");
            }
            System.out.println();
        }
    }

//    private void readCountry() {
//        String input = readFromConsole();
//        StringBuilder jsonArray = new StringBuilder("{\"provs\":[");
//        int idx = 1;
//        Matcher matcher = Pattern.compile("<a\\s+href=\"[\\w:/.]+\".+?>(.+?)</a>").matcher(input);
//        while (matcher.find()) {
//            String name = "{\"name\":\"" + matcher.group(1) + "\",";
//            String id = "\"id\":\"" + (idx++) + "\"}";
//            jsonArray.append(name).append(id);
//            jsonArray.append(",");
//        }
//        jsonArray.replace(jsonArray.length() - 1, jsonArray.length(), "]}");
//        println(jsonArray.toString());
//        File file = new File("E:\\Temp\\china.json");
//        boolean re = false;
//        if (!file.exists()) {
//            try {
//                re = file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else
//            re = true;
//        if (re) {
//            FileWriter fw;
//            try {
//                fw = new FileWriter(file);
//                fw.write(jsonArray.toString());
//                fw.flush();
//                fw.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public static void main(String[] args) {
        WeatherHtmlAnalyse analyse = new WeatherHtmlAnalyse();
        analyse.init();
    }
}
