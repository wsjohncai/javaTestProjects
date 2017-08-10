package com.johncai;

public class SellTicket {

	public static void main(String[] args) {
		TicketWindow tw = new TicketWindow();
		Thread[] t = new Thread[3];
		for(int i=0;i<3;i++) {
			t[i] = new Thread(tw);
			t[i].start();
		}
	}

}

class TicketWindow implements Runnable{
	private static int ticket = 15;
	
	public synchronized void selling() {
		if(ticket>=1) {
		System.out.println(Thread.currentThread().getName()+"Selling the "+ticket+" ticket.");
		ticket--;
		}
	}
	
	public void run() {
		while(true) {
			if(ticket==0) break;
			try {
				Thread.sleep(1000+(int)(Math.random()*500));
			} catch (Exception e) {
				e.printStackTrace();
			}
			selling();
		}
	}
	
}