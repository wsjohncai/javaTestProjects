import net.sf.json.JSONObject;

import java.io.*;
import java.util.Iterator;

public class Temp {
    public static void main(String[] args) {
        File jsonFile = new File("E:\\Temp\\num_wea.json");
        File file0 = new File("E:\\Temp\\weather_icon.xml");
        StringBuilder jsonb = new StringBuilder(), xmlb = new StringBuilder();
        BufferedReader br0, br1;
        try{
            br0 = new BufferedReader(new FileReader(jsonFile));
            String temp0;
            while((temp0 = br0.readLine()) != null) {
                jsonb.append(temp0);
            }
            br0.close();
            br1 = new BufferedReader(new FileReader(file0));
            while ((temp0 = br1.readLine()) != null){
                xmlb.append(temp0).append("\n");
            }
            br1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject weaJ = JSONObject.fromObject(jsonb.toString());
        String xml = xmlb.toString();
        System.out.println(xml);
        Iterator<String> it = weaJ.keys();
        while (it.hasNext()) {
            String key = it.next();
            xml = xml.replace("d"+key+"{", "d"+weaJ.getString(key)+"{");
            xml = xml.replace("n"+key+"{", "n"+weaJ.getString(key)+"{");
        }
        System.out.println(xml);
        try {
            FileWriter fw = new FileWriter(file0);
            fw.write(xml);
            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
