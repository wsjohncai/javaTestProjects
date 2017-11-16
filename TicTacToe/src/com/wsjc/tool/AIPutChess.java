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
	 * �õ����map�Ŀո�����
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
	 * ���ĳ����ֿ������ӵ�λ��
	 * @param map
	 * @return
	 */
	private int[] getAvailableStep(int[] map) {
		int[] steps = new int[getSpace(map)];
		for (int i = 0; i < steps.length; i++) {
			steps[i] = -1;
		}
		boolean lr = false, ud = false, corl = false, corr = false;

		// �Ƿ����ҶԳ�
		for (int i = 0; i < 3; i++) {
			if (map[i * 3] == map[i * 3 + 2])
				lr = true;
			else {
				lr = false;
				break;
			}
		}

		// �Ƿ����¶Գ�
		for (int i = 0; i < 3; i++) {
			if (map[i] == map[i + 6])
				ud = true;
			else {
				ud = false;
				break;
			}
		}

		// �Ƿ�ԽǶԳ�
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
	 * ����ǰ��������Ƚ��Ƚϣ��ж��Ƿ���Ҫ��֦
	 * 
	 * @param node
	 *            ��ǰ���
	 * @return �����Ҫ��֦���򷵻�true�����򷵻�false
	 */
	private boolean compareToAncesters(Node node) {
		Node parent = node.getParent();
		while (parent != null) {
			if (parent.getType() == node.getType() || !parent.isModified()) {
				parent = parent.getParent();
				continue;
			}
			if (parent.getType() == mainUI.AI_Chess) {
				if (parent.getValue() < node.getValue()) { // ����֦
					parent = parent.getParent();
				} else
					return true;
			} else if (parent.getValue() > node.getValue())
				parent = parent.getParent();
			else // �¼�֦
				return true;
		}
		return false;
	}

	/**
	 * Ϊ����Ľڵ�node�����丸�ڵ��ֵ
	 * @param node
	 */
	private void setParentValue(Node node) {
		Node parent = node.getParent();
		boolean isReplace = false;
		if (!parent.isModified()) { //������ڵ���δ������ֵ��ֱ�Ӹ�ֵ
			parent.setValue(node.getValue());
			parent.setModified(true);
			isReplace = true;
		} else if (parent.getType() == mainUI.AI_Chess) { //������ݸ��ڵ�����ͣ��ж��Ƿ�ֵ
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
	 * ������-��������,���ҵõ���һ��������λ��
	 * 
	 * @param node
	 *            ����һ���������
	 * @param map
	 *            ������������
	 * @param depth
	 *            ��ǰ��ĸ߶�
	 * @return ������һ����λ��
	 */
	private int createSearchTree(Node node, int[] map, int depth) {

		System.out.println("depth: " + depth + ", type: " + node.getType() + ", poi: " + node.getPoi() + ", value: "
				+ node.getValue());

		if (depth > 1) {

			int type;
			int[] steps = getAvailableStep(map);

			if (steps.length == 1) { //�����ʼ���ֻʣ��һ���ո���ôֱ�ӷ���
				if (depth == searchDepth)
					return steps[0];
			} else if (steps.length == 0) { //���γ��������Ĺ����У��ﲻ��ָ����������ȣ���ô��ǰ�ڵ�Ϊ�ӽڵ㣬��ֵ�󷵻�
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
			for (int i = 0; i < steps.length; i++) { //��ȡ�����ߵ�λ�ú����β�����Ӧ�Ľڵ㣬����������������
				if (steps[i] == -1)
					continue;
				int poi = steps[i];
				int[] tmap = map.clone();
				tmap[poi] = node.getType();
				Node n = new Node(node, type, poi);
				
				if(ChessPanel.checkResult(tmap)!=0) { //�����ǰ�ӽڵ��Ѿ������˿���ʤ�����߲�����ô������֦
					int value = getInvestment(tmap,type);
					n.setValue(value);
					n.setModified(true);
					setParentValue(n);
					break;
				}
				//�������������ڵ㿪ʼ���ݹ鴴��������
				createSearchTree(n, tmap, depth - 1);

				System.out.println("depth: " + depth + ", value changed: " + node.getValue());
				//����Ѿ�û�п����ߵ��ӣ���ô����
				if (i == steps.length - 1)
					break;
				//����ǰ�ڵ�ֵ�����Ƚڵ�Ƚ�
				if (compareToAncesters(node))
					break;
			}
			Node parent = node.getParent();
			if (parent != null) //���ýڵ�Ϊ��ʼ�ڵ�ʱ��������һ����λ�ã����򽫵�ǰ�ڵ������ֵ�������ڵ�
				setParentValue(node);
			else
				return node.getPoi();
		} else if (depth == 1) { //���ߵ��߶�Ϊ1ʱ����ȡ��ǰ���̵�����ֵ�������丸�ڵ�
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
	 * �����һ�����ӵ�λ��
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
	 * ����һ��������ͷ���������ֵ
	 * 
	 * @param ch ��Ҫ�жϵ�һ�С�һ�л�Խ��ߵ�����
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
	 * ����ĳ����ֵ�����ֵ�������̷ֱ�����˫�������Ӻ� �ó���ĳ�3��������ȥ���ַ��ĳ�3����
	 * 
	 * @param map
	 *            ��Ҫ��������
	 * @return map������ֵ
	 */
	private int getInvestment(int[] map,int type) {
		int temp[] = new int[3];
		int value = 0;
		// ���������е�����ֵ
		for (int i = 0; i < 3; i++) {
			temp[0] = map[i * 3];
			temp[1] = map[i * 3 + 1];
			temp[2] = map[i * 3 + 2];
			value += getChessTypeValue(temp);
		}
		// ���������е�����ֵ
		for (int i = 0; i < 3; i++) {
			temp[0] = map[i];
			temp[1] = map[i + 3];
			temp[2] = map[i + 6];
			value += getChessTypeValue(temp);
		}
		// ���㽻�������ֵ
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
