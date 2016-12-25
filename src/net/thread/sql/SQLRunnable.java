package net.thread.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SQLRunnable implements Runnable {
	
	private List<SQLRequest> list = new ArrayList<SQLRequest>();
	private static long LOOP_TIMER = 10;
	
	public SQLRunnable() {
		this.list = Collections.synchronizedList(this.list);
	}
	
	@Override
	public void run() {
		System.out.println("SQLRunnable run");
		long timer;
		long delta;
		while(true) {
			timer = System.currentTimeMillis();
			while(this.list.size() > 0) {
				this.list.get(0).execute();
				this.list.remove(0);
			}
			delta = System.currentTimeMillis()-timer;
			if(delta < LOOP_TIMER) {
				try {
					Thread.sleep((LOOP_TIMER-delta));
				} 
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void addRequest(SQLRequest request) {
		synchronized(this.list) {
			this.list.add(request);
		}
	}
}
