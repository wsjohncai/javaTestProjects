package com.wsjc.view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JPanel;

public class ChessPanel extends JPanel {

	public static final int FIRSTHAND = 1;
	public static final int SECONDHAND = -1;
	public static final int LASTCHESS = -2;
	public static final int NO_SPACE = -3;

	private static final long serialVersionUID = 8195854421200688304L;
	private int width, height;
	private HashMap<Integer, Integer> chessPoi;
	private Vector<Integer> saveSteps;
	private Image bg, ochess, xchess;

	public ChessPanel(int width, int height) {
		this.width = width;
		this.height = height;
		this.setPreferredSize(new Dimension(this.width, this.height));
		this.setLayout(new GridLayout(3, 3));
		bg = getToolkit().createImage("res/background.png");
		ochess = getToolkit().createImage("res/o.png");
		xchess = getToolkit().createImage("res/x.png");
		init();
	}

	/**
	 * 初始化棋盘
	 */
	public void init() {
		chessPoi = new HashMap<>();
		saveSteps = new Vector<>();
		repaintPanel();
	}

	public int regret() {
		int last = saveSteps.size() - 1;
		if (last == 0) {
			chessPoi.remove(saveSteps.remove(last));
			return LASTCHESS;
		}
		int lastStep = chessPoi.remove(saveSteps.remove(last));
		repaintPanel();
		return lastStep;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(bg, 0, 0, width, height, null);
		// 画横向线
		for (int i = 0; i < 4; i++) {
			g.drawLine(0, 0, width, 0);
			g.drawLine(0, height / 3, width, height / 3);
			g.drawLine(0, height / 3 * 2, width, height / 3 * 2);
			g.drawLine(0, height, width, height);
		}
		// 画纵向线
		for (int i = 0; i < 4; i++) {
			g.drawLine(0, 0, 0, height);
			g.drawLine(width / 3, 0, width / 3, height);
			g.drawLine(width / 3 * 2, 0, width / 3 * 2, height);
			g.drawLine(width, 0, width, height);
		}
		// 画棋子
		drawChess(g);
	}

	/**
	 * 下棋落子
	 * 
	 * @param poi
	 */
	public void putChess(int poi, int type) {
		if (!chessPoi.containsKey(poi)) {
			chessPoi.put(poi, type);
			saveSteps.add(poi);
			repaintPanel();
		}
	}

	/**
	 * 获得当前棋局的数组
	 * @return
	 */
	public int[] getMap() {
		int[] map = new int[9];
		for (int poi : saveSteps) {
			map[poi] = chessPoi.get(poi);
		}
		return map;
	}

	/**
	 * 检查是否有行、列或者对角线有3个成一线的情况
	 * @param map
	 * @return 如果有，返回成3的棋子的类型
	 */
	public static int checkResult(int[] map) {
		// 是否横向成3
		for (int i = 0; i < 3; i++) {
			if (map[i * 3] == map[i * 3 + 2] && map[i * 3] == map[i * 3 + 1] && map[i * 3] != 0)
				if (map[i * 3] == FIRSTHAND)
					return FIRSTHAND;
				else
					return SECONDHAND;
		}

		// 是否纵向成3
		for (int i = 0; i < 3; i++) {
			if (map[i] == map[i + 6] && map[i] == map[i + 3] && map[i] != 0)
				if (map[i] == FIRSTHAND)
					return FIRSTHAND;
				else
					return SECONDHAND;
		}

		// 是否对角对称
		if (map[4] == map[2] && map[6] == map[2] && map[2] != 0)
			if (map[2] == FIRSTHAND)
				return FIRSTHAND;
			else
				return SECONDHAND;
		if (map[0] == map[4] && map[0] == map[8] && map[0] != 0)
			if (map[0] == FIRSTHAND)
				return FIRSTHAND;
			else
				return SECONDHAND;
		
		int space = 0;
		for (int i : map) {
			if (i == 0)
				space++;
		}
		if(space == 0)
			return NO_SPACE;
		return 0;
	}

	/**
	 * 在面板中画出所有棋子
	 * 
	 * @param g
	 */
	private void drawChess(Graphics g) {
		Iterator<Integer> it = chessPoi.keySet().iterator();
		while (it.hasNext()) {
			int key = it.next();
			if (chessPoi.get(key) != null) {
				int type = chessPoi.get(key);
				int spV = height / 3, spH = width / 3;
				int y = key / 3 * spV + 1, x = key % 3 * spH + 1;
				if (type == FIRSTHAND)
					g.drawImage(ochess, x, y, spH - 1, spV - 1, null);
				else if (type == SECONDHAND) {
					g.drawImage(xchess, x, y, spH - 1, spV - 1, null);
				}
			}
		}
	}

	private int Time = 0;

	private void repaintPanel() {
		if (Time <= 0) {
			Time = 20;
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					while (Time-- > 0) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						ChessPanel.this.repaint();
					}
					System.out.println("Finished refresh");
				}
			});
			t.start();
		} else
			Time = 20;
	}

	// public static void main(String[] args) {
	// ChessPanel cp = new ChessPanel(400, 400);
	// cp.init();
	// }

}
