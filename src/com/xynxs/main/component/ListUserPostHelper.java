package com.xynxs.main.component;

import com.xynxs.main.BaseActivity;

public interface ListUserPostHelper {

	
	public void getPostResult(String data, boolean isAppend);
	
	public void getPostCountResult(String result);
	
	public BaseActivity getActivity();
	
	
}
