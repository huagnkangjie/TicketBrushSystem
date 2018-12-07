package com.hust.grid.leesf.entrance;

import com.hust.grid.leesf.bean.CountBean;
import com.hust.grid.leesf.bean.IpInfo;
import com.hust.grid.leesf.thread.IPCollectTask;
import com.hust.grid.leesf.thread.VoteThread;

import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Vote {
	private BlockingQueue<IpInfo> ipInfoQueue;
	private IPCollectTask ipCollectTask;
	private VoteThread voteThread1;
	private VoteThread voteThread2;
	private VoteThread voteThread3;
	private VoteThread voteThread4;
	private VoteThread voteThread5;
	private VoteThread voteThread6;
	private VoteThread voteThread7;
	private VoteThread voteThread8;
	private VoteThread voteThread9;
	private VoteThread voteThread10;
	private CountBean countBean = new CountBean();

	public Vote() {
		ipInfoQueue = new LinkedBlockingQueue<IpInfo>();
		ipCollectTask = new IPCollectTask(ipInfoQueue);
		voteThread1 = new VoteThread(ipInfoQueue, countBean);
		voteThread2 = new VoteThread(ipInfoQueue, countBean);
		voteThread3 = new VoteThread(ipInfoQueue, countBean);
		voteThread4 = new VoteThread(ipInfoQueue, countBean);
		voteThread5 = new VoteThread(ipInfoQueue, countBean);
		voteThread6 = new VoteThread(ipInfoQueue, countBean);
		voteThread7 = new VoteThread(ipInfoQueue, countBean);
		voteThread8 = new VoteThread(ipInfoQueue, countBean);
		voteThread9 = new VoteThread(ipInfoQueue, countBean);
		voteThread10 = new VoteThread(ipInfoQueue, countBean);
	}
	
	public void vote() {
		Timer timer = new Timer();
		long delay = 0;
		long period = 1000 * 60 * 60;	
		// 每一个小时采集一次ip
		timer.scheduleAtFixedRate(ipCollectTask, delay, period);
		
		// 开启投票任务
		voteThread1.start();
		voteThread2.start();
		voteThread3.start();
		voteThread4.start();
		voteThread5.start();
		voteThread6.start();
		voteThread7.start();
		voteThread8.start();
		voteThread9.start();
		voteThread10.start();
	}
	
	public static void main(String[] args) {
		Vote vote = new Vote();
		vote.vote();
	}
}
