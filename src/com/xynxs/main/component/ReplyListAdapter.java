package com.xynxs.main.component;

import java.util.List;

import com.xynxs.main.MainActivity;
import com.xynxs.main.R;
import com.xynxs.main.bean.Reply;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ReplyListAdapter extends ArrayAdapter<Reply>{

	private MainActivity act;
	
	public ReplyListAdapter(MainActivity act, int resourceId, List<Reply> objects) {
		super(act.getApplicationContext(), resourceId, objects);
		this.act = act;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView = act.createView(R.layout.post_msg);
		}
		
		
		//Reply reply = getItem(position);
		
		return convertView;
	}
	
}
