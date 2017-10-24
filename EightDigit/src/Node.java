
public class Node {
	public Node child,parent;
	public int hn;
	public int gn;
	public int[][] map;
	public int fn;
	/**
	 * 构建一个结点，使其父节点为parent，并设置其值
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
