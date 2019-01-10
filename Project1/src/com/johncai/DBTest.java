package com.johncai;

import com.mysql.cj.xdevapi.*;

public class DBTest {

    public static void main(String[] args) {
        String user = "wsjohncai";
        String passwd = "1092647174";
        String url = "mysqlx://localhost:33060/test";

        Session session = new SessionFactory().getSession(url+"?user="+user+"&password="+passwd);
        Schema myDb = session.getSchema("test");

        //通过collection来记录数据
//        Collection coll = myDb.createCollection("users", true);
//        coll.add("{\"name\":\"admin\",\"password\":\"1092Czg\"}").execute();
//        DocResult docs = coll.find("name like :n").bind("n","a%").execute();
//        while (docs.hasNext()) {
//            System.out.println(docs.fetchOne());
//        }

        //通过table记录数据
        Table table = myDb.getTable("user");
        RowResult rows = table.select("id,name,password")
                .where("name like :n")
                .bind("n","a%")
                .execute();
        while (rows.hasNext()) {
            Row row = rows.fetchOne();
            System.out.println(row.getString("name")+","+row.getString("password"));
        }
    }

}
