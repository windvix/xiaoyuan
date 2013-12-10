package com.xynxs.main.component;

import java.util.List;

import com.xynxs.main.BaseActivity;
import com.xynxs.main.R;
import com.xynxs.main.bean.Post;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class PostListAdapter extends ArrayAdapter<Post>{

	private BaseActivity act;
	
	private int resId;
	
	private List<Post> list;
	
	public PostListAdapter(BaseActivity act, int resId, List<Post> objects) {
		super(act, resId, objects);
		this.act = act;
		this.resId = resId;
		this.list = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(list.size()==1){
			convertView = getNoDataView();
		}
		return convertView;
	}
	
	
	private View getNoDataView(){
		View  view = act.createView(R.layout.list_no_data);
		LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		int height = act.getScreenHeight()-act.getResources().getDimensionPixelSize(R.dimen.TITLE_BAR_HEIGHT)*2-act.getResources().getDimensionPixelSize(R.dimen.SELECT_TAB_HEIGHT)-act.getResources().getDimensionPixelSize(R.dimen.BOTTOM_BAR_HEIGHT);
		params.height = height;
		
		view.findViewById(R.id.no_data_root_layout).setLayoutParams(params);
		return view;
	}
	
}
