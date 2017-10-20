package com.johncai;

import java.io.*;
import java.util.regex.*;

public class DeleteChar {

	public static void main(String[] args) {
		DeleteChar dc = new DeleteChar();
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		try {
			Pattern p = Pattern.compile("(.+)\\s+(\\b\\w*\\b)$");
			Matcher m = p.matcher(bf.readLine());
			if(m.find()) {
				String path = m.group(1);
				String s = m.group(2);
				System.out.println("Group 1:"+path+"  Gropu 2: "+s);
				dc.Op(path,s);
			}
			else 
				System.out.println("Input Error!");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	File f,tf;

	public void Op(String fp, String s) {
		f = new File(fp);
		if(!f.exists()) {
			System.out.println("�ļ������ڣ�");
			return;
		}
		if(s==null) {
			System.out.println("Ҫɾ�����ַ���Ϊ�գ�");
			return;
		}
		Pattern p = null;
		String s1 = null;
		BufferedReader bf = null;
		
		try {
			bf = new BufferedReader(new FileReader(f));
			while ((s1=bf.readLine()) != null) {
				p = Pattern.compile(s,Pattern.CASE_INSENSITIVE);
				Matcher match = p.matcher(s1);
					System.out.println(match.replaceAll("b")); //
			}
			System.out.println("ɾ���ɹ���");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				bf.close();
			}catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
