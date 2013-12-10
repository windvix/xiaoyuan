package com.xynxs.main.adapter;

import java.util.ArrayList;
import java.util.List;

import com.xynxs.main.BaseActivity;
import com.xynxs.main.R;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyTextViewAdapter extends ArrayAdapter<String>{
	
	private BaseActivity act;
	
	private List<String> allList = null;
	
	private List<String> copyList = new ArrayList<String>();
	
	public MyTextViewAdapter(BaseActivity act, int resourceId, List<String> objects) {
		super(act.getApplicationContext(), resourceId, objects);
		this.act = act;
		allList = objects;
		for(String a:allList){
			copyList.add(a);
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView==null){
			convertView = act.createView(R.layout.textview);
		}
		
		TextView tv  = (TextView)convertView.findViewById(R.id.one_text);
		if(tv!=null){
			tv.setText(getItem(position));
			convertView.setTag(getItem(position));
		}
		return convertView;
	}
	
	public void filter(String content){
		List<String> tempList = new ArrayList<String>();
		if(content==null){
			content = "";
		}
		content = content.trim();
		if(!content.equals("")){
			for(String str:copyList){
				if(str.contains(content)){
					tempList.add(str);
				}
			}
			
			
			allList.clear();
			notifyDataSetChanged();
			for(String one:tempList){
				allList.add(one);
				notifyDataSetChanged();
			}
		}else{
			
			allList.clear();
			notifyDataSetChanged();
			
			for(String one:copyList){
				allList.add(one);
				notifyDataSetChanged();
			}
		}
		
	}
}
