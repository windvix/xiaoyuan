package com.xynxs.main.task;

import java.util.ArrayList;
import java.util.List;

import com.xynxs.main.BaseActivity;
import com.xynxs.main.util.ServerHelper;

import android.os.AsyncTask;
import android.os.Handler;

public abstract class BaseTask extends AsyncTask<Void, Void, Void> {

	private BaseActivity activity;

	private List<ServerHelper> helpers;

	private Handler handler;

	private Runnable cancelRun;

	public BaseTask(BaseActivity activity) {
		this.activity = activity;
		helpers = new ArrayList<ServerHelper>();
		if (ServerHelper.getServerHelperVersionCode() < 0) {
			ServerHelper.setVersionCode(activity.getVersionCode());
		}
		cancelRun = new Runnable() {
			@Override
			public void run() {
				if (!isFinished) {
					cancel(true);
					if (helpers != null && helpers.size() > 0) {
						for (ServerHelper h : helpers) {
							h.stopConnection();
						}
						helpers.clear();
					}
					onPostExecute();
				}
			}
		};
		handler = activity.getHandler();
		handler.postDelayed(cancelRun, 60 * 1000);
	}

	private boolean isFinished = false;

	/**
	 * 获取服务器连接助手
	 */
	public ServerHelper getHelper() {
		ServerHelper helper = new ServerHelper(this);
		helpers.add(helper);
		return helper;
	}

	/**
	 * 停止当前任务
	 */
	public void stopTask() {
		cancel(true);
		isFinished = true;
		if (handler != null) {
			handler.removeCallbacks(cancelRun);
			handler = null;
		}
		if (helpers != null && helpers.size() > 0) {
			for (ServerHelper h : helpers) {
				h.stopConnection();
			}
			helpers.clear();
		}
	}

	/**
	 * 取消任务(和停止的区别在于,这里通知了activity的视图)
	 */
	public void cancelTask() {
		stopTask();
	}

	@Override
	protected Void doInBackground(Void... params) {
		if (!isCancelled()) {
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
		if (!isCancelled()) {
			isFinished = true;
			onPostExecute();
		}
	}

	protected abstract void onPostExecute();

	/**
	 * 开启执行
	 */
	public void startTask() {
		execute();
	}

	protected BaseActivity getActivity() {
		return activity;
	}
}
