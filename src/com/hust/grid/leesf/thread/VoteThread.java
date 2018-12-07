package com.hust.grid.leesf.thread;

import com.hust.grid.leesf.bean.CountBean;
import com.hust.grid.leesf.bean.IpInfo;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class VoteThread extends Thread {
	private BlockingQueue<IpInfo> ipInfoQueue;
	private CountBean countBean;
	
	public VoteThread(BlockingQueue<IpInfo> ipInfoQueue, CountBean countBean) {
		this.ipInfoQueue = ipInfoQueue;
		this.countBean = countBean;
	}
	
	@Override
	public void run() {
		HttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 10000);
		HttpConnectionParams.setSoTimeout(params, 15000);
		HttpResponse response = null;
		//HttpGet get = null;
		HttpPost post = null;
		HttpEntity entity =  null;
		HttpHost proxy = null;

		while (true) {
			vote(proxy, client, post, response, entity, 1);

		}
	}


	public void vote(HttpHost proxy, HttpClient client, HttpPost post, HttpResponse response, HttpEntity entity, int
					 no) {
		IpInfo ipInfo = null;
		try {
			System.out.println("抓取ip个数：" + ipInfoQueue.size());
			ipInfo = ipInfoQueue.take();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		proxy = new HttpHost(ipInfo.getIpAddress(),
			ipInfo.getPort());
		System.out.println("请求IP:" + ipInfo.getIpAddress());
		System.out.println("请求Port:" + ipInfo.getPort());
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
			proxy);
		post = new HttpPost(
			"http://365.vapp.cc/vote/index/add.html");
		post.addHeader("Host", "www.hnxdf.com");
		post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");
		post.addHeader("Referer", "http://365.vapp.cc/vote/?id=2&from=groupmessage");
		//设置编码
		// 设置参数
		for(int i = 0; i < 30; i ++) {
			try {
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("id", "107"));
				post.setEntity(new UrlEncodedFormEntity(nvps));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			try {
				response = client.execute(post);
				entity = response.getEntity();
				byte[] bytes = EntityUtils.toByteArray(entity);
				String temp = new String(bytes, "utf-8");
				byte[] contentData = temp.getBytes("utf-8");
				String rs = new String(contentData);
				if(rs.contains("投票成功") || rs.contains("每天最多投")) {
					System.out.println("第" + i + "次，IP：" + ipInfo.getIpAddress() + ", Port: " + ipInfo.getPort() + ", " +
						"刷票成功，返回信息 " + "= " + rs);
					countBean.setCount(countBean.getCount() + 1);
					System.err.println("已成功刷票：" + countBean.getCount());
					System.out.println("-----------------------------------");
				}


			} catch (ClientProtocolException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
	}
}
