import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FileHandler implements Runnable {
	private File workPath;
	private HelperUI ui;
	private HashMap<Integer, String> fileList;
	private ArrayList<String> dirList = null;
	private HashMap<Integer, String> poiRcd;
	private File ignTXT;
	private File fileTXT;
	private int lID = 1;

	public FileHandler(HelperUI helperUI, File workPath) {
		ui = helperUI;
		this.workPath = workPath;
	}

	public void initWork() {
		if (workPath != null && workPath.exists()) {
			readDir();
			fileTXT = new File(workPath.getAbsolutePath() + "\\filelist.txt");
			fileList = new HashMap<Integer, String>();
			poiRcd = new HashMap<Integer, String>();
			if (!fileTXT.exists() || fileTXT.length() == 0) {
				try {
					fileTXT.createNewFile();
					readAllFile(workPath);
					writeFileTxt();
					writeIgn();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else
				readInMem();
			ui.loadWork(1, getWorkLoad());
		}
	}

	// ��ȡ�����ĵ�
	private void readDir() {
		dirList = new ArrayList<String>();
		ignTXT = new File(workPath.getAbsolutePath() + "\\ignore.txt");
		BufferedReader bf;
		try {
			if (ignTXT.exists()) {
				bf = new BufferedReader(new FileReader(ignTXT));
				String name;
				while ((name = bf.readLine()) != null) {
					dirList.add(name);
				}
				bf.close();
			} else
				ignTXT.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ����ָ�����ļ�
	public File getFile(int id) {
		File result = null;
		String name = fileList.get(id);
		if (name == null)
			return null;
		String[] n = name.split(";;;");
		result = new File(n[0]);
		return result;
	}

	// ��ȡ�����ļ�������
	public int getWorkLoad() {
		return fileList.size();
	}

	// �ݹ��������ļ�
	private void readAllFile(File path) {
		File[] files = path.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				if (!dirList.contains(file.getAbsolutePath())) {
					poiRcd.put(lID, file.getAbsolutePath());
					readAllFile(file);
				}
			} else {
				if (file.getName().matches(
						".*\\.jpg|.*\\.gif|.*\\.png|.*\\.img|.*\\.JPEG|.*\\.JPG|.*\\.PNG|.*\\.jpeg|.*\\.GIF")) {
					fileList.put(lID++, file.getAbsolutePath());
				}
			}
		}
	}

	// ��ȡ��ǰ����Ŀ¼�µ�������Ŀ¼�µ��ļ�
	private void readTempFile(File path) {
		File[] files = path.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				poiRcd.put(lID, path.getAbsolutePath());
				readTempFile(file);
			} else {
				if (file.getName().matches(
						".*\\.jpg|.*\\.gif|.*\\.png|.*\\.img|.*\\.JPEG|.*\\.JPG|.*\\.PNG|.*\\.jpeg|.*\\.GIF")) {
					fileList.put(lID++, file.getAbsolutePath() + ";;;sorted");
				}
			}
		}
	}

	// ��fileList�ļ��н�δ�����ͼƬ��ȡ���ڴ�
	private void readInMem() {
		BufferedReader bf;
		String path1 = null, path2;
		if (fileTXT != null && fileTXT.exists())
			try {
				bf = new BufferedReader(new FileReader(fileTXT));
				String text;
				while ((text = bf.readLine()) != null) {
					String[] temp = text.split(";;;");
					if (temp.length == 2) { // ����ļ�δ���࣬��Ŀ¼�ӽ�λ���б����ļ���ӵ��������б�
						String name = temp[1];
						if (!new File(name).exists())
							continue;
						path2 = name.substring(0, name.lastIndexOf("\\"));
						if (path1 == null || !path1.equals(path2)) { // ����µ�Ŀ¼����λ��
							poiRcd.put(lID, path2);
							path1 = path2;
						}
						fileList.put(lID++, name);
					}
				}
				if (bf != null)
					bf.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	// ���Ӧ�ú��Ե��ļ���
	public void addDir(String name) {
		if (!dirList.contains(name)) {
			dirList.add(name);
		}
	}

	public void removeDir(String name) {
		if (dirList.contains(name)) {
			dirList.remove(name);
		}
	}

	public ArrayList<String> getDir() {
		return dirList;
	}

	// д��ignore.txt
	private void writeIgn() {
		FileWriter fw;
		if (ignTXT != null && ignTXT.exists()) {
			try {
				fw = new FileWriter(ignTXT);
				for (String text : dirList) {
					fw.write(text + "\n");
				}
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// д��fileList.txt�ļ�
	private void writeFileTxt() {
		FileWriter fw;
		if (fileTXT != null && fileTXT.exists()) {
			try {
				fw = new FileWriter(fileTXT);
				Iterator<Integer> it = fileList.keySet().iterator();
				while (it.hasNext()) {
					int id = it.next();
					String filename = fileList.get(id);
					fw.write(id + ";;;" + filename + "\n");
				}
				fw.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// ����Ŀ¼
	public boolean createDir(File path) {
		return path.mkdir();
	}

	// ����ѷ���
	public void markSorted(int id, String newPath) {
		newPath += ";;;sorted";
		// print("markedSorted: " + id + ":" + newPath);
		fileList.put(id, newPath);
	}

	// ȡ�����
	public void removeMark(int id, String name) {
		// String name = fileList.get(id);
		// String nameNew = name.substring(0, name.indexOf(";;;sorted"));
		// print("removedMark " + name);
		fileList.put(id, name);
	}

	// ���ظ�ͼƬ�Ƿ��Ѿ�����
	public boolean isSorted(int id) {
		return fileList.get(id).contains(";;;sorted");
	}

	// �ƶ��ļ�����
	public File moveFile(File srcFile, File dstFile) {
		FileInputStream fis;
		FileOutputStream fos;
		byte[] bytes = new byte[4096];
		if (!srcFile.exists())
			return null;
		try {
			if (dstFile.exists())
				dstFile = dealDup(dstFile);
			dstFile.createNewFile();
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(dstFile);
			int read;
			while ((read = fis.read(bytes)) != -1) {
				fos.write(bytes, 0, read);
			}
			fos.close();
			fis.close();
			srcFile.delete();
			return dstFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// ����ͬ���ļ�
	private File dealDup(File file) {
		int i = 1;
		File temp;
		String abs = file.getAbsolutePath();
		String type = abs.substring(abs.lastIndexOf('.'), abs.length());
		String name = abs.substring(0, abs.lastIndexOf('.'));
		while ((temp = new File(name + "(" + i + ")" + type)).exists())
			i++;
		return temp;
	}

	// ���Ĺ�����
	public int changeWork(String newPath) {
		if (newPath.contains(workPath.getAbsolutePath())) {
			// print("changeWork: " + newPath + ":" +
			// workPath.getAbsolutePath());
			Iterator<Integer> it = poiRcd.keySet().iterator();
			while (it.hasNext()) {
				int cur = it.next();
				String name = poiRcd.get(cur);
				print("changeWork: " + name);
				if (name.equals(newPath)) {
					// print("changeWork: success: " + cur);
					return cur;
				}
			}
			int tID = lID;
			readTempFile(new File(newPath));
			if (tID == lID)
				return -2;
			ui.loadWork(tID, getWorkLoad());
			return 0;
		}
		// print("changeWork:failed");
		return -1;

	}

	// ��β����
	public void endWork() {
		writeFileTxt();
		writeIgn();
	}

	void print(String x) {
		System.out.println(x);
	}

	// ����
	// public static void main(String[] args) {
	// String t = "E:\\Temp\\file.txt.jpg";
	// new FileHandler(null, null).dealDup(new File(t));
	// }

	@Override
	public void run() {
		initWork();
	}
}
