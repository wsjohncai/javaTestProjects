package com.wsjohncai.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.wsjohncai.common.DesNode;

public class ID3Algorithm {

	private static final double DIFF_DSC_SUM_ZERO = -1.0;
	private static final double SAME_DSC_SUM_ZERO = -2.0;
	private List<List<Integer>> attrs;
	private List<List<Integer>> attrTypes;
	private List<Integer> decision;
	private List<Integer> dcsType;
	private DesNode HEAD;
	Scanner sc;

	public ID3Algorithm() {
		init();
		initData();
	}

	private void init() {
		attrs = new ArrayList<List<Integer>>();
		attrTypes = new ArrayList<List<Integer>>();
		decision = new ArrayList<>();
		dcsType = new ArrayList<>();
		HEAD = new DesNode(-1, -1, null);
		new ArrayList<>();
		sc = new Scanner(System.in);
	}

	private void initData() {
		int[] x1 = { 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3 };
		int[] x2 = { 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 2, 2, 2, 2 };
		int[] x3 = { 1, 1, 2, 2, 1, 1, 2, 2, 1, 1, 2, 2, 1, 1, 2, 2, 1, 1, 2, 2, 1, 1, 2, 2, };
		int[] x4 = { 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2 };
		int[] dec = { 3, 2, 3, 1, 3, 2, 3, 1, 3, 2, 3, 1, 3, 2, 3, 3, 3, 3, 3, 1, 3, 2, 3, 3, };
		List<Integer> attr1 = new ArrayList<>();
		List<Integer> attr2 = new ArrayList<>();
		List<Integer> attr3 = new ArrayList<>();
		List<Integer> attr4 = new ArrayList<>();
		List<Integer> attrt1 = new ArrayList<>();
		List<Integer> attrt2 = new ArrayList<>();
		List<Integer> attrt3 = new ArrayList<>();
		List<Integer> attrt4 = new ArrayList<>();
		for (int i = 0; i < 24; i++) {
			attr1.add(x1[i]);
			attr2.add(x2[i]);
			attr3.add(x3[i]);
			attr4.add(x4[i]);
			decision.add(dec[i]);
		}
		attrt1.add(1);
		attrt1.add(2);
		attrt1.add(3);
		attrt2.add(1);
		attrt2.add(2);
		attrt3.add(1);
		attrt3.add(2);
		attrt4.add(1);
		attrt4.add(2);
		dcsType.add(1);
		dcsType.add(2);
		dcsType.add(3);
		attrs.add(attr1);
		attrs.add(attr2);
		attrs.add(attr3);
		attrs.add(attr4);
		attrTypes.add(attrt1);
		attrTypes.add(attrt2);
		attrTypes.add(attrt3);
		attrTypes.add(attrt4);
	}

	private void menu() {
		while (true) {
			System.out.println("1.�鿴��������    2.���������\n3.���������");
			String sop = sc.nextLine();
			if (sop.contains("quit"))
				break;
			int op = Integer.parseInt(sop);
			switch (op) {
			case 1:
				outputInstances();
				break;
			case 2:
				init();
				getFacts();
				break;
			case 3:
				System.out.println("������ɣ��������ɾ����������Ժ�...");
				generateDecision();
				break;
				default:
					System.exit(0);
			}
		}
		sc.close();
	}

	private void outputInstances() {
		System.out.print("��� ");
		for (int i = 1; i <= attrs.size(); i++) {
			System.out.print("����" + i + " ");
		}
		System.out.println("����");
		int seq = 1;
		int size = decision.size();
		while (size-- > 0) {
			System.out.print("\t" + seq + ": \t ");
			for (List<Integer> list : attrs) {
				System.out.print(list.get(seq - 1) + "\t ");
			}
			System.out.println(decision.get(seq - 1));
			seq++;
		}

	}

	/**
	 * �Ӽ��̻��������ѧϰʵ��
	 */
	private void getFacts() {
		Scanner sc = new Scanner(System.in);
		System.out.print("���������Եĸ�����");
		int attrbutions = sc.nextInt();
		sc.nextLine();
		while (attrbutions-- > 0) {
			List<Integer> tempattr = new ArrayList<Integer>();
			List<Integer> temptype = new ArrayList<Integer>();
			attrs.add(tempattr);
			attrTypes.add(temptype);
		}
		System.out.println("������������������ݣ�ÿ�����ݵ����ʽΪ��\n��" + "����1,����2...,���ߡ���ÿ��ֵ֮����Ӣ�Ķ��Ÿ����������롰ok��������");
		int seq = 1;
		while (true) {
			System.out.print(seq + "��");
			String s = sc.nextLine();
			if (s.contains("ok"))
				break;
			else {
				String[] attr = s.split(",");
				if (attr.length != (attrs.size() + 1)) {
					System.out.println("�����������������ԣ�");
					continue;
				}
				for (int i = 0; i < attr.length - 1; i++) {
					String value = attr[i];
					int pheo = Integer.parseInt(value);
					List<Integer> attrArray = attrs.get(i);
					List<Integer> attrTypeArray = attrTypes.get(i);
					attrArray.add(pheo);
					if (!attrTypeArray.contains(pheo))
						attrTypeArray.add(pheo);
				}
				int val = Integer.parseInt(attr[attr.length - 1]);
				decision.add(val);
				if (!dcsType.contains(val))
					dcsType.add(val);
			}
			seq++;
		}
	}

	private double log2(double num) {
		double result = Math.log(num) / Math.log(2);
		return result;
	}

	/**
	 * ��������attr1����ֵ������attr1��ȡֵ��Χ��indexes �����б�ʾ
	 * 
	 * @param attr1
	 * @param attrTypes
	 * @param indexes
	 * @return
	 */
	private double getEntropy(List<Integer> attr1, List<Integer> attrtype, List<Integer> indexes) {
		double result = 0;
		int dcstemp = decision.get(indexes.get(0));
		boolean isUnique = true;
		;
		for (int type : attrtype) {// ����attr1��ĳһ�������еı����� type
			int total = 0;
			int[] rtype = new int[dcsType.size()];
			for (int idx : indexes) {
				if (type == attr1.get(idx)) { // ���ݸñ����Ͳ�����ÿ�־��ߵ�����
					int dcs = decision.get(idx);
					if (dcs != dcstemp)
						isUnique = false;
					rtype[dcsType.indexOf(dcs)] += 1;
					total++;
				}
			}
			for (int i = 0; i < rtype.length; i++) { // ��������ֱ����͵���ֵ
				if (rtype[i] == 0)
					continue;
				result += (rtype[i] * log2(rtype[i] * 1.0 / total));
			}
		}
		if (result == 0) {
			if (isUnique)
				return SAME_DSC_SUM_ZERO;
			else
				return DIFF_DSC_SUM_ZERO;
		}
		return -result / indexes.size();

	}

	/**
	 * ���ؿ����ڼ��������ֵ��
	 * 
	 * @param node
	 * @return
	 */
	private List<Integer> getIndexes(DesNode node) {
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i = 0; i < decision.size(); i++) {
			indexes.add(i);
		}
		while (node != HEAD) {
			int attr = node.getAttr();
			int pheo = node.getPheotype();
			List<Integer> list = attrs.get(attr);
			for (int i = 0; i < list.size(); i++) {
				if (pheo != list.get(i)) {
					if (indexes.contains(i))
						indexes.remove(indexes.indexOf(i));
				}
			}
			node = node.getParent();
		}
		return indexes;
	}

	private boolean shouldCount(DesNode node, int attr) {
		while (node != HEAD) {
			if (node.getAttr() == attr)
				return false;
			node = node.getParent();
		}
		return true;
	}

	/**
	 * ���ɾ�����
	 * @param node
	 */
	private void createID3Tree(DesNode node) {
		List<Integer> indexes = getIndexes(node);
		double min = -1;
		int minattr = -1;
		for (int i = 0; i < attrs.size(); i++) { // �����ֵ��С�����Լ�
			if (!shouldCount(node, i))
				continue;
			double temp = getEntropy(attrs.get(i), attrTypes.get(i), indexes);
//			System.out.println("x" + (i + 1) + " = " + temp);
			// �����ֵΪ0�����䲻���ڲ�ͬ�ľ���ֵ����ô����ڵ�ΪҶ�ӽڵ㣬���������ֵ��������
			if (temp == SAME_DSC_SUM_ZERO) {
				node.setDecision(decision.get(indexes.get(0)));
//				while (node != HEAD) {
//					System.out.print(
//							"X" + (node.getAttr() + 1) + "=" + node.getPheotype() + ": " + node.getDesicion() + " <- ");
//					node = node.getParent();
//				}
//				System.out.println("");
				return;
			} else if (temp == DIFF_DSC_SUM_ZERO) {
				DesNode sib = null;
				List<Integer> pheotemp = new ArrayList<>();
				for (int idx : indexes) {
					int pheo = attrs.get(i).get(idx);
					if (!pheotemp.contains(pheo)) {
						pheotemp.add(pheo);
						DesNode leaf = new DesNode(i, pheo, node);
						leaf.setDecision(decision.get(idx));
						leaf.setSiblings(sib);
						sib = leaf;
//						while (leaf != HEAD) {
//							System.out.print(
//									"X" + (leaf.getAttr() + 1) + "=" + leaf.getPheotype() + ": " + leaf.getDesicion() + " <- ");
//							leaf = leaf.getParent();
//						}
//						System.out.println("");
					}
				}
				node.setChild(sib);
				return;
			} else if (min > temp || min == -1) {
				min = temp;
				minattr = i;
			}
		}
		if (min == -1)
			return;
		DesNode tempNode = null;
		for (int type : attrTypes.get(minattr)) { // ��ֵ��Ϊ0������ÿ�������������¼�������������
			DesNode childnode = new DesNode(minattr, type, node);
			childnode.setSiblings(tempNode);
			tempNode = childnode;
			createID3Tree(childnode);
		}
		node.setChild(tempNode);
	}

	private void outputDecision(DesNode node, String out) {
		DesNode siblings = node.getSiblings();
		if (siblings != null) {
			outputDecision(siblings, out);
		}
		DesNode child = node.getChild();
		if (child == null) {
			System.out.println(out + "X" + (node.getAttr() + 1) + "=" + node.getPheotype() + "->" + node.getDesicion());
			return;
		}
		String next = out + "X" + (node.getAttr() + 1) + "=" + node.getPheotype() + " �� ";
		outputDecision(child, next);
	}

	private void generateDecision() {
		createID3Tree(HEAD);
		 outputDecision(HEAD.getChild(), "");
	}

	public static void main(String[] args) {
		ID3Algorithm id3 = new ID3Algorithm();
		id3.menu();
	}

}
