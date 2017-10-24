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
		System.out.println("请输入八数码的初始位置（空格请用0代替）：");
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

	// 定义存储当前状态的节点
	private Node curNode;
	//Opened表和Closed表
	private Vector<Node> visited = new Vector<Node>();
	private Vector<Node> opened = new Vector<Node>();
	//目标结点的逆序值
	private int dstreverseOrder;

	/**
	 * 获取数组arr的逆序值
	 * @param arr
	 * @return 计算得到数组的逆序值
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
	 * 计算最佳移动路径
	 */
	private void getSolution() {
		// 判断输入的八数码图是否存在解，若不存在则结束
		if ((dstreverseOrder + getReverseOrder(curNode.map)) % 2 != 0) {
			System.out.println("This map can't not be solveed out; \n" + "The destination reverse order is: "
					+ dstreverseOrder + ", and the input map's is " + getReverseOrder(curNode.map));
			return;
		}
		// 寻找移动路径
		while (curNode != null && curNode.hn != 0) {
			expandNodes();
			setNextNode();
		}
		//打印最终结果
		printTrace();
	}

	/**
	 * 扩展下一结点，并将其加入或者更新Opened表
	 */
	private void expandNodes() {
		int i = 0, j = 0;
		int[][] cur = curNode.map;
		// 获取可移动的0的位置
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
		if (i - 1 >= 0) { // 与上面的格子交换
			temp = cloneArray(cur);
			move(temp, i, j, MV_UP);
			if (!isVisited(temp)) { // 如果不在Closed表中
				tempd = getDistance(temp);
				if ((index = isInOpened(temp)) == -1) {
					// 如果不在Opened表中，则加入Opened表，否则更新Opened表中已存在的结点的值
					// System.out.println("up: " + tempd);
					opened.add(new Node(curNode, tempd, curNode.gn + 1, temp));
				} else
					updateOpened(index, new Node(curNode, tempd, curNode.gn + 1, temp));
			}
		}
		if (i + 1 <= 2) { // 与下面的格子交换
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
		if (j - 1 >= 0) { // 与左边的格子交换
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
		if (j + 1 <= 2) { // 与右边的格子交换
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
	 * 从Opened表中拿到f(n)值最小的结点，替代当前的结点
	 */
	private void setNextNode() {
		// 对Opened表进行排序
		opened.sort(new MyComparator());
		// 获取优先度最高的结点作为当前的结点
		curNode = opened.firstElement();
		opened.removeElementAt(0);
		visited.add(curNode);
	}

	/**
	 * 计算当前状态的启发值（所有不在位的数字到目标位置所需移动的步数总和）
	 * 
	 * @param digits
	 * @return 返回启发值h(n)
	 */
	private int getDistance(int[][] digits) {
		int height = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				switch (digits[i][j]) { // 根据数字的所在位置计算其到目标的距离
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
	 * 打印出数组
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
	 * 打印整个移动的路径
	 */
	private void printTrace() {
		Node temp = curNode;
		while (temp != null) { // 顺着父节点回溯
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

	// 查询是否为已经访问过的结点
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
	 * 检查是否在Opened列表中。如果存在，则返回其 在Opened表中的位置，否则返回-1。
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
	 * 在index位置使用用来更新的节点newNode替换
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
	 * 比较arr1 和arr2 两个二维数组是否相等
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

	// 用于将结点排序
	class MyComparator implements Comparator<Node> {

		@Override
		public int compare(Node o1, Node o2) { // 根据fn值的大小排序，若fn相同，则gn值大的排在Opened表前
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
	 * 将空格移动一步
	 * 
	 * @param arr
	 *            需要移动的数组
	 * @param zi
	 *            空格所在行的索引值
	 * @param zj
	 *            空格所在列的索引值
	 * @param dire
	 *            移动的方向
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
	 * 复制一个二维数组
	 * 
	 * @param arr
	 *            被复制的数组
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
	 * 根据数组arr的原始下标（zi,zj)的值与新下标（i,j）交换
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
