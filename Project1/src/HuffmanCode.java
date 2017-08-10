import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

public class HuffmanCode {

	private TreeNode root, node = null; // 根节点，字母节点
	private TreeNode[] nodes = null; // 字母数组
	private HashMap<Character, TreeNode> hm = new HashMap<Character, TreeNode>(); // 哈希表，用于存储读取到的字符

	public Object fileOperate(File file, int op) {
		// 如果文件不存在，返回
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

	// 将结点数组进行排序
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
		// 初始化树
		root = new TreeNode();
		if (nodes.length == 1)
			root.setRchild(nodes[0]);
		else if (nodes.length < 1)
			return;
		root.setLchild(nodes[0]);
		root.setRchild(nodes[1]);
		root.setFreq(nodes[0].getFreq() + nodes[1].getFreq());
		TreeNode temp;

		// 建立哈夫曼树
		for (int i = 2; i < nodes.length; i++) {
			// 当前树根节点小于下一树结点，将其变为左孩子
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

	// 将文件进行哈夫曼编码
	public void coding(File inf) {
		BufferedReader bf = (BufferedReader) fileOperate(inf, 0);
		char c;
		int eof;
		try {
			eof = bf.read();
			while (eof != -1) {
				// 如果读取的字符还未存储，则将其存进哈希表
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

		// 将哈希表中的结点取出
		int count = hm.size();
		nodes = new TreeNode[count];
		Iterator<Character> it = hm.keySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			char key = it.next();
			nodes[i] = hm.get(key);
			
			i++;
		}
		// 进行排序
		sort(0, nodes.length - 1);
		for(TreeNode temp : nodes) {
			System.out.println(temp.getText() + ", " + temp.getFreq()); // test
		}
		huffmanTree();
	}

	// 将编码文件输出
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
				
				//将转码后的文本转换成字节
				text += readTree(c);
				if(text.length()<8) { //如果编码小于一个字节，继续读取
					eof = bf.read();
					if(eof!=-1) continue;
				} else { //否则转换为一个字节，多余字节下次使用，当缓冲区满后输出
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
				if(eof==-1&&text.length()>0) { //当最后一个字符读取完毕时，将剩余的字符输出
					while(text.length()<8) {
						text+="0";
					}
					out[i++] = transtoByte(text);
					bos.write(out, 0, i);
				}
				// System.out.println("char: " + c + " " + s);//test
				// 将得到的编码输出到文件
			}

			System.out.println("压缩完毕！");
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

	// 二进制转换
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

	// 解码
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
				// 将读取的字符对应哈夫曼树进行解码
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

// 结点类
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

	// 用于复制对象
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
