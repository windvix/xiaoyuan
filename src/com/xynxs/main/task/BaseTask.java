package com.xynxs.main.task;

import java.util.ArrayList;
import java.util.List;

import com.xynxs.main.BaseActivity;
import com.xynxs.main.util.ServerHelper;

import android.os.AsyncTask;

public abstract class BaseTask extends AsyncTask<Void, Void, Void>{
	
	private boolean isForceStop = false;
	
	private BaseActivity activity;
	
	private List<ServerHelper> helpers;
	
	public BaseTask(BaseActivity activity){
		this.activity = activity;
		helpers = new ArrayList<ServerHelper>();
		if(ServerHelper.getServerHelperVersionCode()<0){
			ServerHelper.setVersionCode(activity.getVersionCode());
		}
	}
	
	
	public ServerHelper getHelper(){
		ServerHelper helper=  new ServerHelper();
		helpers.add(helper);
		return helper;
	}
	
	/**
	 * 停止当前任务
	 */
	public void stopTask(){
		isForceStop = true;
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
		isForceStop = true;
		cancel(true);
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		if(!isForceStop){
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
		if(!isForceStop){
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
	
	/**
	 * 是否强行停止
	 */
	public boolean isForceStop(){
		return isForceStop;
	}
	
	protected BaseActivity getActivity(){
		return activity;
	}
}
