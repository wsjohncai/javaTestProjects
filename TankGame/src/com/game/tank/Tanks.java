package com.game.tank;

import java.util.Vector;

public class Tanks implements Runnable {
	Bullets bullet = null;
	Vector<Bullets> bullets = new Vector<Bullets>();
	protected int X, Y;
	protected int SP_X = 0, SP_Y = 0;
	protected int DIR = 2;
	protected int LIFE = 1;
	boolean isAlive = true;
	boolean isBlocked = false;
	boolean isRun = true;

	public Tanks(int x, int y) {
		X = x;
		Y = y;
		Thread t = new Thread(this);
		t.start();
	}

	public void move() {
		switch (DIR) {
		case 0:
			if (X < 0) {
				isBlocked = true;
				DIR = 2;
			} else
				isBlocked = false;
			SP_X = -1;
			SP_Y = 0;
			break;
		case 1:
			if (Y > 400) {
				isBlocked = true;
				DIR = 3;
			} else
				isBlocked = false;
			SP_X = 0;
			SP_Y = 1;
			break;
		case 2:
			if (X >= 400) {
				isBlocked = true;
				DIR = 0;
			} else
				isBlocked = false;
			SP_X = 1;
			SP_Y = 0;
			break;
		case 3:
			if (Y < 0) {
				isBlocked = true;
				DIR = 1;
			} else
				isBlocked = false;
			SP_X = 0;
			SP_Y = -1;
			break;
		}
		X += SP_X;
		Y += SP_Y;
	}

	public void isBlocked(Tanks tank) {
		switch (DIR) {
		case 0:
			if (X < tank.X + 35 && X > tank.X && Y < tank.Y + 35 && Y > tank.Y - 35) {
				isBlocked = true;
				DIR = 2;
			} else
				isBlocked = false;
			break;
		case 1:
			if (Y + 35 > tank.Y && Y < tank.Y + 35 && X > tank.X - 35 && X < tank.X + 35) {
				isBlocked = true;
				DIR = 3;
			} else
				isBlocked = false;
			break;
		case 2:
			if (X + 35 > tank.X && X < tank.X && Y > tank.Y - 35 && Y < tank.Y + 35) {
				isBlocked = true;
				DIR = 0;
			} else
				isBlocked = false;
			break;
		case 3:
			if (Y < tank.Y + 35 && Y > tank.Y && X > tank.X - 35 && X < tank.X + 35) {
				isBlocked = true;
				DIR = 1;
			} else
				isBlocked = false;
			break;
		}
	}

	public void shot() {
		if (bullets.size() < 5) {
			bullet = new Bullets(X, Y, DIR);
			bullets.add(bullet);
		}
	}

	public void bshot() {
		LIFE--;
		if (LIFE == 0)
			isAlive = false;
	}

	public void run() {
		while (isAlive) {
			try {
				Thread.sleep(70);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (isRun) {
				for (int i = 0; i < bullets.size(); i++) {
					bullet = bullets.get(i);
					if (!bullet.isExist)
						bullets.remove(bullet);
				}
			}
		}
	}

}

class enemyTanks extends Tanks {

	Vector<enemyTanks> enTanks = null;

	public enemyTanks(int x, int y) {
		super(x, y);
	}

	public void getEnemyTanks(Vector<enemyTanks> enTanks) {
		this.enTanks = enTanks;
	}

	public void avoidCrash() {
		enemyTanks enTank = null;

		// 判断敌人坦克是否相撞
		for (int i = 0; i < enTanks.size(); i++) {
			enTank = enTanks.get(i);
			if (this != enTank)
				this.isBlocked(enTank);
			if (isBlocked)
				break;
		}
	}

	public void run() {
		int time = 0;
		while (isAlive) {
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (isRun) {
				time++;
				if (time % 60 == 0)
					shot();
				if (time % 60 == 0) {
					DIR = (int) (Math.random() * 4);
					time = 0;
				}
				for (int i = 0; i < bullets.size(); i++) {
					bullet = bullets.get(i);
					if (!bullet.isExist)
						bullets.remove(bullet);
				}
				avoidCrash();
				move();
			}
		}
	}
}

class meTank extends Tanks {

	public meTank(int x, int y) {
		super(x, y);
		DIR = 3;
		LIFE = 3;
	}

	public void bshot() {
		LIFE--;
		if (LIFE == 0)
			isAlive = false;
		X = 200;
		Y = 400;
		DIR = 3;
	}

}

class Bullets implements Runnable {
	int X, Y;
	int SP = 3;
	int DIR;
	boolean isExist = true;
	boolean isRun = true;

	public Bullets(int x, int y, int dir) {
		X = x;
		Y = y;
		DIR = dir;
		Thread t = new Thread(this);
		t.start();
	}

	public void shot(Tanks tank) {
		int x, y;
		x = X;
		y = Y;
		switch (DIR) {
		case 0:
			y += 15;
			break;
		case 1:
			x += 15;
			y += 30;
			break;
		case 2:
			x += 30;
			y += 15;
			break;
		case 3:
			x += 15;
			break;
		}
		if (x > tank.X && x < tank.X + 30 && y > tank.Y && y < tank.Y + 30) {
			tank.bshot();
			isExist = false;
		}
	}

	public void trace() {
		switch (DIR) {
		case 0:
			X -= SP;
			break;
		case 1:
			Y += SP;
			break;
		case 2:
			X += SP;
			break;
		case 3:
			Y -= SP;
			break;
		}
		if (X < -3 || Y < -3 || X > 403 || Y > 403)
			isExist = false;
	}

	public void run() {
		while (isExist) {
			try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
			if (isRun) {
				trace();
			}
		}
	}
}
