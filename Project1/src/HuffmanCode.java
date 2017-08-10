import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

public class HuffmanCode {

	private TreeNode root, node = null; // ���ڵ㣬��ĸ�ڵ�
	private TreeNode[] nodes = null; // ��ĸ����
	private HashMap<Character, TreeNode> hm = new HashMap<Character, TreeNode>(); // ��ϣ�����ڴ洢��ȡ�����ַ�

	public Object fileOperate(File file, int op) {
		// ����ļ������ڣ�����
		if (op == 0)
			if (file == null && !file.exists())
				return null;
		if (op == 1) {
			if (!file.exists())
				try {
					file.createNewFile();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}

		BufferedReader bf = null;
		FileOutputStream bos = null;
		try {
			if (op == 0)
				bf = new BufferedReader(new FileReader(file));
			else
				bos = new FileOutputStream(file);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (op == 0)
			return bf;
		else
			return bos;

	}

	// ����������������
	public void sort(int left, int right) {
		TreeNode privot;
		int i, j;

		if (left >= right)
			return;

		i = left;
		j = right;
		privot = nodes[i];
		while (i < j) {
			while (i < j && nodes[j].getFreq() >= privot.getFreq())
				j--;
			nodes[i] = nodes[j];
			while (i < j && nodes[i].getFreq() <= privot.getFreq())
				i++;
			nodes[j] = nodes[i];
		}
		nodes[i] = privot;

		sort(left, i - 1);
		sort(i + 1, right);
	}

	public void huffmanTree() {
		// ��ʼ����
		root = new TreeNode();
		if (nodes.length == 1)
			root.setRchild(nodes[0]);
		else if (nodes.length < 1)
			return;
		root.setLchild(nodes[0]);
		root.setRchild(nodes[1]);
		root.setFreq(nodes[0].getFreq() + nodes[1].getFreq());
		TreeNode temp;

		// ������������
		for (int i = 2; i < nodes.length; i++) {
			// ��ǰ�����ڵ�С����һ����㣬�����Ϊ����
			if (nodes[i].getFreq() < root.getFreq()) {
				temp = root.clone();
				root.setRchild(temp);
				root.setLchild(nodes[i]);
				root.setFreq(temp.getFreq() + nodes[i].getFreq());
			} else {
				temp = root.clone();
				root.setLchild(temp);
				root.setRchild(nodes[i]);
				root.setFreq(temp.getFreq() + nodes[i].getFreq());
			}
		}

	}

	public String readTree(char c) {
		String str = "";
		TreeNode temp = root;
		while (temp != null) {
			if (temp.getLchild() != null) {
				if (temp.getLchild().getText() == c) {
					str += "0";
					break;
				} else if (temp.getRchild().getText() == c) {
					str += "1";
					break;
				} else if (temp.getLchild().getLchild() == null && temp.getLchild().getRchild() == null) {
					str += "1";
					temp = temp.getRchild();
				} else {
					str += "0";
					temp = temp.getLchild();
				}

			} else
				str += "1";
		}

		// System.out.println(str);//test
		return str;
	}

	// ���ļ����й���������
	public void coding(File inf) {
		BufferedReader bf = (BufferedReader) fileOperate(inf, 0);
		char c;
		int eof;
		try {
			eof = bf.read();
			while (eof != -1) {
				// �����ȡ���ַ���δ�洢����������ϣ��
				c = (char) eof;
				if (hm.get(c) == null) {
					node = new TreeNode(c, 1);
					hm.put(c, node);
				} else {
					node = hm.get(c);
					node.setFreq(node.getFreq() + 1);
				}
				// System.out.print(c);//test
				eof = bf.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bf.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// ����ϣ���еĽ��ȡ��
		int count = hm.size();
		nodes = new TreeNode[count];
		Iterator<Character> it = hm.keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			char key = it.next();
			nodes[i] = hm.get(key);
			
			i++;
		}
		// ��������
		sort(0, nodes.length - 1);
		for(TreeNode temp : nodes) {
			System.out.println(temp.getText() + ", " + temp.getFreq()); // test
		}
		huffmanTree();
	}

	// �������ļ����
	public void compressedFile(File file) {
		char c;
		int eof;
		int i=0;
		byte[] out = new byte[256];
		BufferedReader bf = (BufferedReader) fileOperate(file, 0);
		String[] filename = file.getName().split("\\.");
		FileOutputStream bos = (FileOutputStream) fileOperate(new File(filename[0]+ ".cod"), 1);
		try {
			String text = "";
			eof = bf.read();
			while (eof != -1) {
				c = (char) eof;
				
				//��ת�����ı�ת�����ֽ�
				text += readTree(c);
				if(text.length()<8) { //�������С��һ���ֽڣ�������ȡ
					eof = bf.read();
					if(eof!=-1) continue;
				} else { //����ת��Ϊһ���ֽڣ������ֽ��´�ʹ�ã����������������
					while(text.length()>=8) {
					out[i++]=transtoByte(text.substring(0, 8));
					text = text.substring(8);
					if(i>=256) {
						bos.write(out);
						i=0;
					}
					}
					eof = bf.read();
				}
				if(eof==-1&&text.length()>0) { //�����һ���ַ���ȡ���ʱ����ʣ����ַ����
					while(text.length()<8) {
						text+="0";
					}
					out[i++] = transtoByte(text);
					bos.write(out, 0, i);
				}
				// System.out.println("char: " + c + " " + s);//test
				// ���õ��ı���������ļ�
			}

			System.out.println("ѹ����ϣ�");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bos.close();
				bf.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// ������ת��
	public Byte transtoByte(String s) {
		int i = 7, mul = 1, temp;
		byte result = 0;
		while (i >= 0) {
			temp = Integer.parseInt(s.substring(i, i + 1));
			result += mul * temp;
			mul *= 2;
			i--;
		}
		return result;
	}

	// ����
	public void decoding(File file) {
		BufferedReader bf = null;
		BufferedWriter bft = null;
		char c = 0;
		int eof;
		TreeNode temp;

		try {
			bf = (BufferedReader) fileOperate(file, 0);
			bft = (BufferedWriter) fileOperate(new File(file.getName() + ".txt"), 1);
			eof = bf.read();
			while (eof != -1) {
				// ����ȡ���ַ���Ӧ�����������н���
				temp = root;
				while (temp.getLchild() != null && temp.getRchild() != null) {
					c = (char) eof;
					if (c == '1') {
						temp = temp.getRchild();
					} else {
						temp = temp.getLchild();
					}
					eof = bf.read();
				}
				bft.write(c);
				// System.out.print(c);//test
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bf.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		 if (args.length != 1)
		 System.out.println("Usage: HuffmanCode [input]");
		HuffmanCode code = new HuffmanCode();
		 File inf = new File(args[0]);
		 code.coding(inf);
		 code.compressedFile(inf);
	}

}

// �����
class TreeNode implements Cloneable {
	private TreeNode lchild, rchild = null;
	private char text;
	private int freq;

	public TreeNode() {

	}

	public TreeNode(char text, int freq) {
		this.text = text;
		this.freq = freq;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	public TreeNode getLchild() {
		return lchild;
	}

	public char getText() {
		return text;
	}

	public void setText(char text) {
		this.text = text;
	}

	public void setLchild(TreeNode lchild) {
		this.lchild = lchild;
	}

	public TreeNode getRchild() {
		return rchild;
	}

	public void setRchild(TreeNode rchild) {
		this.rchild = rchild;
	}

	// ���ڸ��ƶ���
	@Override
	protected TreeNode clone() {
		TreeNode o = null;

		try {
			o = (TreeNode) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}

}
