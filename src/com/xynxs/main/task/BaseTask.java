package com.xynxs.main.task;

import java.util.ArrayList;
import java.util.List;

import com.xynxs.main.BaseActivity;
import com.xynxs.main.util.ServerHelper;

import android.os.AsyncTask;

public abstract class BaseTask extends AsyncTask<Void, Void, Void>{

	private BaseActivity activity;
	
	private List<ServerHelper> helpers;
	
	public BaseTask(BaseActivity activity){
		this.activity = activity;
		helpers = new ArrayList<ServerHelper>();
		if(ServerHelper.getServerHelperVersionCode()<0){
			ServerHelper.setVersionCode(activity.getVersionCode());
		}
	}
	
	/**
	 * 获取服务器连接助手
	 */
	public ServerHelper getHelper(){
		ServerHelper helper=  new ServerHelper(this);
		helpers.add(helper);
		return helper;
	}
	
	/**
	 * 停止当前任务
	 */
	public void stopTask(){
		cancel(true);
		if(helpers!=null && helpers.size()>0){
			for(ServerHelper h:helpers){
				h.stopConnection();
			}
			helpers.clear();
		}
	}
	
	/**
	 * 取消任务(和停止的区别在于,这里通知了activity的视图)
	 */
	public void cancelTask(){
		cancel(true);
		if(helpers!=null && helpers.size()>0){
			for(ServerHelper h:helpers){
				h.stopConnection();
			}
			helpers.clear();
		}
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		if(!isCancelled()){
			doInBackground();
		}
		return null;
	}
	
	/**
	 * 后台执行的任务
	 */
	protected abstract void doInBackground();
	
	
	@Override
	protected void onPostExecute(Void result) {
		if(!isCancelled()){
			onPostExecute();
		}
	}
	
	protected abstract void onPostExecute();
	
	/**
	 * 开启执行
	 */
	public void startTask(){
		execute();
	}
	
	protected BaseActivity getActivity(){
		return activity;
	}
}
