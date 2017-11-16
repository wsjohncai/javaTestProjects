package com.wsjc.tool;

import com.wsjc.common.Node;
import com.wsjc.view.ChessPanel;
import com.wsjc.view.MainUI;

public class AIPutChess {

	
	public static final int NO_AVAILABLE_STEP = -1;

	private MainUI mainUI;
	private int searchDepth;
	private static final int MIN = -100;
	private static final int MAX = 100;

	public AIPutChess(MainUI mainui, int depth) {
		this.mainUI = mainui;
		searchDepth = depth;
	}

	/**
	 * 得到棋局map的空格数量
	 * 
	 * @param map
	 * @return
	 */
	private int getSpace(int[] map) {
		int space = 0;
		for (int i : map) {
			if (i == 0)
				space++;
		}
		return space;
	}

	/**
	 * 获得某个棋局可以落子的位置
	 * @param map
	 * @return
	 */
	private int[] getAvailableStep(int[] map) {
		int[] steps = new int[getSpace(map)];
		for (int i = 0; i < steps.length; i++) {
			steps[i] = -1;
		}
		boolean lr = false, ud = false, corl = false, corr = false;

		// 是否左右对称
		for (int i = 0; i < 3; i++) {
			if (map[i * 3] == map[i * 3 + 2])
				lr = true;
			else {
				lr = false;
				break;
			}
		}

		// 是否上下对称
		for (int i = 0; i < 3; i++) {
			if (map[i] == map[i + 6])
				ud = true;
			else {
				ud = false;
				break;
			}
		}

		// 是否对角对称
		if (map[3] == map[1] && map[6] == map[2] && map[7] == map[5])
			corl = true;
		if (map[5] == map[1] && map[0] == map[8] && map[7] == map[3])
			corr = true;

		int place = 0;
		if (lr && ud) {
			if (map[0] == 0)
				steps[place++] = 0;
			if (map[1] == 0)
				steps[place++] = 1;
			if (map[3] == 0)
				steps[place++] = 3;
			if (map[4] == 0)
				steps[place++] = 4;
		} else if (lr) {
			for (int i = 0; i < 3; i++) {
				if (map[i * 3] == 0)
					steps[place++] = i * 3;
				if (map[i * 3 + 1] == 0)
					steps[place++] = i * 3 + 1;
			}
		} else if (ud) {
			for (int i = 0; i < 6; i++) {
				if (map[i] == 0)
					steps[place++] = i;
			}
		} else if (corl) {
			if (map[0] == 0)
				steps[place++] = 0;
			if (map[6] == 0)
				steps[place++] = 6;
			if (map[3] == 0)
				steps[place++] = 3;
			if (map[7] == 0)
				steps[place++] = 7;
			if (map[8] == 0)
				steps[place++] = 8;
			if (map[4] == 0)
				steps[place++] = 4;
		} else if (corr) {
			if (map[0] == 0)
				steps[place++] = 0;
			if (map[1] == 0)
				steps[place++] = 1;
			if (map[2] == 0)
				steps[place++] = 2;
			if (map[3] == 0)
				steps[place++] = 3;
			if (map[6] == 0)
				steps[place++] = 6;
			if (map[4] == 0)
				steps[place++] = 4;
		} else
			for (int i = 0; i < 9; i++) {
				if (map[i] == 0)
					steps[place++] = i;
			}

		return steps;
	}

	/**
	 * 将当前结点与祖先结点比较，判断是否需要剪枝
	 * 
	 * @param node
	 *            当前结点
	 * @return 如果需要剪枝，则返回true，否则返回false
	 */
	private boolean compareToAncesters(Node node) {
		Node parent = node.getParent();
		while (parent != null) {
			if (parent.getType() == node.getType() || !parent.isModified()) {
				parent = parent.getParent();
				continue;
			}
			if (parent.getType() == mainUI.AI_Chess) {
				if (parent.getValue() < node.getValue()) { // α剪枝
					parent = parent.getParent();
				} else
					return true;
			} else if (parent.getValue() > node.getValue())
				parent = parent.getParent();
			else // β剪枝
				return true;
		}
		return false;
	}

	/**
	 * 为传入的节点node设置其父节点的值
	 * @param node
	 */
	private void setParentValue(Node node) {
		Node parent = node.getParent();
		boolean isReplace = false;
		if (!parent.isModified()) { //如果父节点尚未有评价值，直接赋值
			parent.setValue(node.getValue());
			parent.setModified(true);
			isReplace = true;
		} else if (parent.getType() == mainUI.AI_Chess) { //否则根据父节点的类型，判断是否赋值
			if (parent.getValue() < node.getValue()) {
				parent.setValue(node.getValue());
				isReplace = true;
			}
		} else if (parent.getValue() > node.getValue()) {
			parent.setValue(node.getValue());
			isReplace = true;
		}
		if (isReplace && parent.getParent() == null)
			parent.setPoi(node.getPoi());

	}

	/**
	 * 建立α-β搜索树,并且得到下一步的落子位置
	 * 
	 * @param node
	 *            传入一个父辈结点
	 * @param map
	 *            父辈结点的棋谱
	 * @param depth
	 *            当前层的高度
	 * @return 返回下一步的位置
	 */
	private int createSearchTree(Node node, int[] map, int depth) {

		System.out.println("depth: " + depth + ", type: " + node.getType() + ", poi: " + node.getPoi() + ", value: "
				+ node.getValue());

		if (depth > 1) {

			int type;
			int[] steps = getAvailableStep(map);

			if (steps.length == 1) { //如果起始棋局只剩下一个空格，那么直接返回
				if (depth == searchDepth)
					return steps[0];
			} else if (steps.length == 0) { //当形成搜索树的过程中，达不到指定的搜索深度，那么当前节点为子节点，传值后返回
				if (depth == searchDepth)
					return NO_AVAILABLE_STEP;
				else {
					int value = getInvestment(map,node.getType());
					node.setValue(value);
					node.setModified(true);
					setParentValue(node);
				}
			}

			if (node.getType() == mainUI.AI_Chess)
				type = mainUI.HM_Chess;
			else
				type = mainUI.AI_Chess;
			for (int i = 0; i < steps.length; i++) { //获取可以走的位置后，依次产生相应的节点，并往下生成搜索树
				if (steps[i] == -1)
					continue;
				int poi = steps[i];
				int[] tmap = map.clone();
				tmap[poi] = node.getType();
				Node n = new Node(node, type, poi);
				
				if(ChessPanel.checkResult(tmap)!=0) { //如果当前子节点已经产生了可以胜利的走步，那么产生剪枝
					int value = getInvestment(tmap,type);
					n.setValue(value);
					n.setModified(true);
					setParentValue(n);
					break;
				}
				//从搜索树最左侧节点开始，递归创建搜索树
				createSearchTree(n, tmap, depth - 1);

				System.out.println("depth: " + depth + ", value changed: " + node.getValue());
				//如果已经没有可以走的子，那么返回
				if (i == steps.length - 1)
					break;
				//将当前节点值与祖先节点比较
				if (compareToAncesters(node))
					break;
			}
			Node parent = node.getParent();
			if (parent != null) //当该节点为起始节点时，返回下一步的位置，否则将当前节点的评估值传给父节点
				setParentValue(node);
			else
				return node.getPoi();
		} else if (depth == 1) { //当走到高度为1时，获取当前棋盘的评估值并传给其父节点
			int value = getInvestment(map,node.getType());

			// System.out.println(
			// "depth: " + depth + ", type: " + node.getType() + ", poi: " +
			// node.getPoi() + ", value: " + value);

			node.setValue(value);
			node.setModified(true);
			setParentValue(node);
		}
		return NO_AVAILABLE_STEP;
	}

	/**
	 * 获得下一步棋子的位置
	 * 
	 * @param map
	 * @return
	 */
	public int getNextStep(int[] map) {
		int poi = 0;
		Node node = new Node(null, mainUI.AI_Chess, -1);
		poi = createSearchTree(node, map, searchDepth);
		return poi;
	}

	/**
	 * 根据一行棋的类型返回其评估值
	 * 
	 * @param ch 需要判断的一行、一列或对角线的棋子
	 * @return
	 */
	private int getChessTypeValue(int[] ch) {
		int AIChess = 0, HMChess = 0;
		for (int i : ch) {
			if (i == mainUI.AI_Chess)
				AIChess++;
			else if (i == mainUI.HM_Chess)
				HMChess++;
		}
		if (AIChess == 3)
			return MAX;
		else if (HMChess == 3)
			return MIN;
		else if ((AIChess + HMChess) == 3 || (AIChess == 1 && HMChess == 1))
			return 0;
		else if (AIChess == 2)
			return 20;
		else if (HMChess == 2)
			return -20;
		else if (AIChess == 1)
			return 1;
		else
			return -1;
	}

	/**
	 * 计算某个棋局的评估值：将棋盘分别填满双方的棋子后， 用程序的成3的数量减去棋手方的成3数量
	 * 
	 * @param map
	 *            需要计算的棋局
	 * @return map的评估值
	 */
	private int getInvestment(int[] map,int type) {
		int temp[] = new int[3];
		int value = 0;
		// 计算所有行的评估值
		for (int i = 0; i < 3; i++) {
			temp[0] = map[i * 3];
			temp[1] = map[i * 3 + 1];
			temp[2] = map[i * 3 + 2];
			value += getChessTypeValue(temp);
		}
		// 计算所有列的评估值
		for (int i = 0; i < 3; i++) {
			temp[0] = map[i];
			temp[1] = map[i + 3];
			temp[2] = map[i + 6];
			value += getChessTypeValue(temp);
		}
		// 计算交叉的评估值
		temp[0] = map[0];
		temp[1] = map[4];
		temp[2] = map[8];
		value += getChessTypeValue(temp);
		temp[0] = map[2];
		temp[1] = map[4];
		temp[2] = map[6];
		value += getChessTypeValue(temp);

		return value;
	}

}
