import java.util.Comparator;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

public class EightDigit {

	public static final int MV_UP = 0;
	public static final int MV_DOWN = 1;
	public static final int MV_LEFT = 2;
	public static final int MV_RIGHT = 3;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EightDigit ei = new EightDigit();
		System.out.println("�����������ĳ�ʼλ�ã��ո�����0���棩��");
		Scanner sc = new Scanner(System.in);
		int[][] map = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				map[i][j] = sc.nextInt();
			}
		}
		ei.init(map);
		sc.close();
		ei.getSolution();
	}

	private void init(int[][] map) {
		curNode = new Node(null, getDistance(map), 0, map);
		visited.add(curNode);
		int[][] dst = new int[][] { { 1, 2, 3 }, { 8, 0, 4 }, { 7, 6, 5 } };
		dstreverseOrder = getReverseOrder(dst);
	}

	// ����洢��ǰ״̬�Ľڵ�
	private Node curNode;
	//Opened���Closed��
	private Vector<Node> visited = new Vector<Node>();
	private Vector<Node> opened = new Vector<Node>();
	//Ŀ���������ֵ
	private int dstreverseOrder;

	/**
	 * ��ȡ����arr������ֵ
	 * @param arr
	 * @return ����õ����������ֵ
	 */
	private int getReverseOrder(int[][] arr) {
		int[] sarr = new int[9];
		int roCount = 0;
		for (int i = 0, k = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				sarr[k] = arr[i][j];
				k++;
			}
		}
		for (int i = 1; i < sarr.length; i++) {
			if (sarr[i] != 0)
				for (int j = 0; j < i; j++) {
					if (sarr[j] > sarr[i]) {
						roCount++;
					}
				}
		}
		return roCount;
	}

	/**
	 * ��������ƶ�·��
	 */
	private void getSolution() {
		// �ж�����İ�����ͼ�Ƿ���ڽ⣬�������������
		if ((dstreverseOrder + getReverseOrder(curNode.map)) % 2 != 0) {
			System.out.println("This map can't not be solveed out; \n" + "The destination reverse order is: "
					+ dstreverseOrder + ", and the input map's is " + getReverseOrder(curNode.map));
			return;
		}
		// Ѱ���ƶ�·��
		while (curNode != null && curNode.hn != 0) {
			expandNodes();
			setNextNode();
		}
		//��ӡ���ս��
		printTrace();
	}

	/**
	 * ��չ��һ��㣬�����������߸���Opened��
	 */
	private void expandNodes() {
		int i = 0, j = 0;
		int[][] cur = curNode.map;
		// ��ȡ���ƶ���0��λ��
		for (i = 0; i < 3; i++) {
			for (j = 0; j < 3; j++) {
				if (cur[i][j] == 0)
					break;
			}
			if (j < 3 && cur[i][j] == 0)
				break;
		}

		int tempd, index;
		int[][] temp = null;
		if (i - 1 >= 0) { // ������ĸ��ӽ���
			temp = cloneArray(cur);
			move(temp, i, j, MV_UP);
			if (!isVisited(temp)) { // �������Closed����
				tempd = getDistance(temp);
				if ((index = isInOpened(temp)) == -1) {
					// �������Opened���У������Opened���������Opened�����Ѵ��ڵĽ���ֵ
					// System.out.println("up: " + tempd);
					opened.add(new Node(curNode, tempd, curNode.gn + 1, temp));
				} else
					updateOpened(index, new Node(curNode, tempd, curNode.gn + 1, temp));
			}
		}
		if (i + 1 <= 2) { // ������ĸ��ӽ���
			temp = cloneArray(cur);
			move(temp, i, j, MV_DOWN);
			if (!isVisited(temp)) {
				tempd = getDistance(temp);
				if ((index = isInOpened(temp)) == -1) {
					// System.out.println("down: " + tempd);
					opened.add(new Node(curNode, tempd, curNode.gn + 1, temp));
				} else
					updateOpened(index, new Node(curNode, tempd, curNode.gn + 1, temp));
			}
		}
		if (j - 1 >= 0) { // ����ߵĸ��ӽ���
			temp = cloneArray(cur);
			move(temp, i, j, MV_LEFT);
			if (!isVisited(temp)) {
				tempd = getDistance(temp);
				if ((index = isInOpened(temp)) == -1) {
					// System.out.println("left: " + tempd);
					opened.add(new Node(curNode, tempd, curNode.gn + 1, temp));
				} else
					updateOpened(index, new Node(curNode, tempd, curNode.gn + 1, temp));
			}
		}
		if (j + 1 <= 2) { // ���ұߵĸ��ӽ���
			temp = cloneArray(cur);
			move(temp, i, j, MV_RIGHT);
			if (!isVisited(temp)) {
				tempd = getDistance(temp);
				if ((index = isInOpened(temp)) == -1) {
					// System.out.println("right: " + tempd);
					opened.add(new Node(curNode, tempd, curNode.gn + 1, temp));
				} else
					updateOpened(index, new Node(curNode, tempd, curNode.gn + 1, temp));
			}
		}
	}

	/**
	 * ��Opened�����õ�f(n)ֵ��С�Ľ�㣬�����ǰ�Ľ��
	 */
	private void setNextNode() {
		// ��Opened���������
		opened.sort(new MyComparator());
		// ��ȡ���ȶ���ߵĽ����Ϊ��ǰ�Ľ��
		curNode = opened.firstElement();
		opened.removeElementAt(0);
		visited.add(curNode);
	}

	/**
	 * ���㵱ǰ״̬������ֵ�����в���λ�����ֵ�Ŀ��λ�������ƶ��Ĳ����ܺͣ�
	 * 
	 * @param digits
	 * @return ��������ֵh(n)
	 */
	private int getDistance(int[][] digits) {
		int height = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				switch (digits[i][j]) { // �������ֵ�����λ�ü����䵽Ŀ��ľ���
				case 1:
					height += (i + j);
					break;
				case 2:
					height += (i + Math.abs(j - 1));
					break;
				case 3:
					height += (i + Math.abs(j - 2));
					break;
				case 4:
					height += (Math.abs(i - 1) + Math.abs(j - 2));
					break;
				case 8:
					height += (Math.abs(i - 1) + j);
					break;
				case 7:
					height += (Math.abs(i - 2) + j);
					break;
				case 6:
					height += (Math.abs(i - 2) + Math.abs(j - 1));
					break;
				case 5:
					height += (Math.abs(i - 2) + Math.abs(j - 2));
					break;
				default:
					break;
				}
			}
		}

		return height;
	}

	/**
	 * ��ӡ������
	 * @param arr
	 * @param other
	 */
	private void print(Node node) {
		System.out.println("f(n)=" + node.fn + " h(n)=" + node.hn + ", g(n)=" + node.gn);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(node.map[i][j] + " ");
			}
			System.out.println("");
		}
	
	}

	/**
	 * ��ӡ�����ƶ���·��
	 */
	private void printTrace() {
		Node temp = curNode;
		while (temp != null) { // ˳�Ÿ��ڵ����
			if (temp.parent == null)
				break;
			temp.parent.child = temp;
			temp = temp.parent;
		}
		System.out.println("The whole movements are like below: ");
		while (temp != null) {
			print(temp);
			temp = temp.child;
		}

	}

	// ��ѯ�Ƿ�Ϊ�Ѿ����ʹ��Ľ��
	private boolean isVisited(int[][] arr) {
		Iterator<Node> t = visited.iterator();
		while (t.hasNext()) {
			int[][] temp = t.next().map;
			if (isEqual(temp, arr))
				return true;
		}
		return false;
	}

	/**
	 * ����Ƿ���Opened�б��С�������ڣ��򷵻��� ��Opened���е�λ�ã����򷵻�-1��
	 * 
	 * @param arr
	 * @return
	 */
	private int isInOpened(int[][] arr) {
		Iterator<Node> t = opened.iterator();
		int index = 0;
		while (t.hasNext()) {
			Node node = t.next();
			int[][] temp = node.map;
			if (isEqual(temp, arr))
				return index;
			index++;
		}
		return -1;
	}

	/**
	 * ��indexλ��ʹ���������µĽڵ�newNode�滻
	 * 
	 * @param index
	 * @param newNode
	 */
	private void updateOpened(int index, Node newNode) {
		Node old = opened.get(index);
		if (old.fn > newNode.fn)
			opened.set(index, newNode);
	}

	/**
	 * �Ƚ�arr1 ��arr2 ������ά�����Ƿ����
	 * 
	 * @param arr1
	 * @param arr2
	 * @return
	 */
	private boolean isEqual(int[][] arr1, int[][] arr2) {
		int i, j = 0;
		for (i = 0; i < 3; i++) {
			for (j = 0; j < 3; j++) {
				if (arr1[i][j] != arr2[i][j])
					break;
			}
			if (j < 3 && arr1[i][j] != arr2[i][j])
				break;
		}
		if (i == 3 && j == 3)
			return true;
		return false;
	}

	// ���ڽ��������
	class MyComparator implements Comparator<Node> {

		@Override
		public int compare(Node o1, Node o2) { // ����fnֵ�Ĵ�С������fn��ͬ����gnֵ�������Opened��ǰ
			if (o1.fn < o2.fn)
				return -1;
			else if (o1.fn > o2.fn)
				return 1;
			else if (o1.gn > o2.gn)
				return -1;
			else if (o1.gn < o2.gn)
				return 1;
			else
				return 0;
		}

	}

	/**
	 * ���ո��ƶ�һ��
	 * 
	 * @param arr
	 *            ��Ҫ�ƶ�������
	 * @param zi
	 *            �ո������е�����ֵ
	 * @param zj
	 *            �ո������е�����ֵ
	 * @param dire
	 *            �ƶ��ķ���
	 */
	private void move(int[][] arr, int zi, int zj, int dire) {
		switch (dire) {
		case MV_UP:
			swap(arr, zi, zj, zi - 1, zj);
			break;
		case MV_DOWN:
			swap(arr, zi, zj, zi + 1, zj);
			break;
		case MV_LEFT:
			swap(arr, zi, zj, zi, zj - 1);
			break;
		case MV_RIGHT:
			swap(arr, zi, zj, zi, zj + 1);
			break;
		default:
			break;
		}
	}

	/**
	 * ����һ����ά����
	 * 
	 * @param arr
	 *            �����Ƶ�����
	 * @return
	 */
	private int[][] cloneArray(int[][] arr) {
		int[][] rs = new int[3][3];
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				rs[i][j] = arr[i][j];
			}
		}
		return rs;
	}

	/**
	 * ��������arr��ԭʼ�±꣨zi,zj)��ֵ�����±꣨i,j������
	 * 
	 * @param arr
	 * @param zi
	 * @param zj
	 * @param i
	 * @param j
	 */
	private void swap(int[][] arr, int zi, int zj, int i, int j) {
		int temp;
		temp = arr[i][j];
		arr[i][j] = 0;
		arr[zi][zj] = temp;
	}

}
