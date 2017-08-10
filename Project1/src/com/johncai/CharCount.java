package com.johncai;

import java.io.*;
import java.util.regex.*;

public class CharCount {

	public static void main(String[] args) {
		CharCount cc = new CharCount();
		cc.find("D:\\i.txt");
	}

	File f;

	public void find(String path) {
		f = new File(path);
		BufferedReader bf = null,bf1=null;
		FileReader fr = null;
		int num = 0,line = 0;
		String s,regex=null;

		try {
			fr = new FileReader(f);
			bf = new BufferedReader(fr);
			bf1 = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("input regex: ");
			regex = bf1.readLine();
			Pattern pattern = Pattern.compile(regex);
			Matcher match;
			while ((s = bf.readLine()) != null) {
				line++;
				match = pattern.matcher(s);
				while(match.find()) {
					int cout = match.groupCount();
					num=0;
					while(num<=cout) {
						System.out.println("line "+line+","+"group "+num+" : ");
						System.out.println(match.group(num++));
				}}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try{
			bf.close();
			bf1.close();
			fr.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

}
