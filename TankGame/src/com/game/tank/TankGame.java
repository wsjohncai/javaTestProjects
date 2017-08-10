/**
 * 坦克游戏不完整版，用于测试
 * @author WSJohnCai
 */
package com.game.tank;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
import javax.swing.*;

public class TankGame extends JFrame {

	private static final long serialVersionUID = 6774459471146615858L;

	MyPanel mp = null;

	public static void main(String[] args) {
		TankGame game = new TankGame();
	}

	public TankGame() {
		mp = new MyPanel();
		this.setTitle("坦克大战");
		this.setSize(460, 490);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		mp.setSize(430, 430);
		this.addKeyListener(mp);
		this.add(mp);
		this.setVisible(true);
		Thread t1 = new Thread(mp);
		t1.start();
	}

}

class MyPanel extends JPanel implements Runnable, KeyListener {

	private static final long serialVersionUID = -3428636106881274803L;

	boolean gamePaused = false;
	enemyTanks enTank = null;
	meTank me = null;
	Bullets bullet = null;
	Vector<enemyTanks> enTanks = new Vector<enemyTanks>();

	public MyPanel() {
		me = new meTank(200, 400);
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 430, 430);
		if (me.isAlive)
			drawTanks(g, me.DIR, 0, me.X, me.Y);
		for (int i = 0; i < enTanks.size(); i++) {
			enTank = enTanks.get(i);
			drawTanks(g, enTank.DIR, 1, enTank.X, enTank.Y);
		}
		for (int j = 0; j < enTanks.size(); j++) {
			for (int i = 0; i < enTanks.get(j).bullets.size(); i++) {
				bullet = enTanks.get(j).bullets.get(i);
				if (bullet.isExist)
					drawBullets(g, bullet.X, bullet.Y, bullet.DIR);
			}
		}
		for (int i = 0; i < me.bullets.size(); i++) {
			bullet = me.bullets.get(i);
			if (bullet.isExist)
				drawBullets(g, bullet.X, bullet.Y, bullet.DIR);
		}
	}

	public void drawTanks(Graphics g, int dir, int type, int x, int y) {
		if (type == 1)
			g.setColor(Color.BLUE);
		if (type == 0)
			g.setColor(Color.YELLOW);

		switch (dir) {
		case 2:
			g.drawLine(x + 15, y + 15, x + 30, y + 15);
			g.fillArc(x + 8, y + 8, 12, 12, 0, 360);
			g.setColor(Color.CYAN);
			g.fill3DRect(x, y, 30, 10, true);
			g.fill3DRect(x, y + 20, 30, 10, true);
			break;
		case 0:
			g.drawLine(x + 15, y + 15, x, y + 15);
			g.fillArc(x + 8, y + 8, 12, 12, 0, 360);
			g.setColor(Color.CYAN);
			g.fill3DRect(x, y, 30, 10, true);
			g.fill3DRect(x, y + 20, 30, 10, true);
			break;
		case 1:
			g.drawLine(x + 15, y + 15, x + 15, y + 30);
			g.fillArc(x + 8, y + 8, 12, 12, 0, 360);
			g.setColor(Color.CYAN);
			g.fill3DRect(x, y, 10, 30, true);
			g.fill3DRect(x + 20, y, 10, 30, true);
			break;
		case 3:
			g.drawLine(x + 15, y + 15, x + 15, y);
			g.fillArc(x + 8, y + 8, 12, 12, 0, 360);
			g.setColor(Color.CYAN);
			g.fill3DRect(x, y, 10, 30, true);
			g.fill3DRect(x + 20, y, 10, 30, true);
			break;
		}
	}

	public void drawBullets(Graphics g, int x, int y, int d) {
		g.setColor(Color.WHITE);
		switch (d) {
		case 0:
			g.fillArc(x - 5, y + 10, 10, 10, -30, 60);
			break;
		case 2:
			g.fillArc(x + 25, y + 10, 10, 10, 150, 60);
			break;
		case 1:
			g.fillArc(x + 10, y + 25, 10, 10, 60, 60);
			break;
		case 3:
			g.fillArc(x + 10, y - 5, 10, 10, -60, -60);
			break;
		}
	}

	public void attack() {
		// 判断我是否打中敌方坦克
		for (int i = 0; i < me.bullets.size(); i++) {
			bullet = me.bullets.get(i);
			for (int j = 0; j < enTanks.size(); j++) {
				enTank = enTanks.get(j);
				bullet.shot(enTank);
				if (!enTank.isAlive)
					enTanks.remove(enTank);
			}
		}
		// 判断敌方坦克是否击中我
		for (int i = 0; i < enTanks.size(); i++) {
			enTank = enTanks.get(i);
			for (int j = 0; j < enTank.bullets.size(); j++) {
				bullet = enTank.bullets.get(j);
				bullet.shot(me);
			}
		}
	}

	@Override
	public void run() {

		while (true) {
			try {
				Thread.sleep(60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			attack();
			repaint();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_K && gamePaused == false) {
			int x = 0;
			enTank = new enemyTanks(x, 0);
			enTank.shot();
			enTanks.add(enTank);
			enTank.getEnemyTanks(enTanks);
			x += 50;
		}
		if (e.getKeyCode() == KeyEvent.VK_J && me.bullets.size() < 5 && gamePaused == false) {
			me.shot();
		}

		if (e.getKeyCode() == KeyEvent.VK_W && me.Y > 0) {
			me.DIR = 3;
			me.move();
		} else if (e.getKeyCode() == KeyEvent.VK_A && me.X > 0) {
			me.DIR = 0;
			me.move();
		} else if (e.getKeyCode() == KeyEvent.VK_S && me.Y < 400) {
			me.DIR = 1;
			me.move();
		} else if (e.getKeyCode() == KeyEvent.VK_D && me.X < 400) {
			me.DIR = 2;
			me.move();
		}

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			if (gamePaused) {
				me.isRun = true;
				for (int i = 0; i < me.bullets.size(); i++) {
					bullet = me.bullets.get(i);
					bullet.isRun = true;
				}
				for (int i = 0; i < enTanks.size(); i++) {
					enTank = enTanks.get(i);
					for (int j = 0; j < enTank.bullets.size(); j++) {
						bullet = enTank.bullets.get(j);
						bullet.isRun = true;
					}
					enTank.isRun = true;
				}
				gamePaused = false;
			} else {
				me.isRun = false;
				for (int i = 0; i < me.bullets.size(); i++) {
					bullet = me.bullets.get(i);
					bullet.isRun = false;
				}
				for (int i = 0; i < enTanks.size(); i++) {
					enTank = enTanks.get(i);
					for (int j = 0; j < enTank.bullets.size(); j++) {
						bullet = enTank.bullets.get(j);
						bullet.isRun = false;
					}
					enTank.isRun = false;
				}
				gamePaused = true;
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

}