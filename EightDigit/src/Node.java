
public class Node {
	public Node child,parent;
	public int hn;
	public int gn;
	public int[][] map;
	public int fn;
	/**
	 * ����һ����㣬ʹ�丸�ڵ�Ϊparent����������ֵ
	 * @param parent
	 * @param hn
	 * @param gn
	 * @param map
	 */
	public Node(Node parent, int hn, int gn, int[][] map) {
		this.parent = parent;
		this.hn = hn;
		this.gn = gn;
		this.map = map;
		this.fn = hn+gn;
	}
	
}
