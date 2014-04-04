package esgi.threadpool;

import esgi.threadpool.FixedThreadPool;

public class PoolWorker extends Thread {

	FixedThreadPool pool;
	
	public PoolWorker(FixedThreadPool pool) {
		this.pool = pool;
	}
	
	public void run() {
		while(true) {
			try {
				pool.getJob().run();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
