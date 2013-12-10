package com.xynxs.main;

import java.util.ArrayList;
import java.util.List;

import com.xynxs.main.adapter.MyTextViewAdapter;
import com.xynxs.main.util.Const;
import com.xynxs.main.util.StringUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 年份选择界面
 */
public class SelectYearActivity extends BaseActivity{

	private int original_year;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		original_year = getIntent().getIntExtra(Const.ENTR_YEAR_KEY, 0);
		
		setContentView(R.layout.login_05);
		ListView listView = (ListView) findViewById(R.id.year_listview);
		findViewById(R.id.title_bar_left_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				back(original_year);
			}
		});
		List<String> yearList = new ArrayList<String>();
		int newestYear = StringUtil.getCurrentYear() + 3;
		for (int i = newestYear; i > (newestYear - 20); i--) {
			yearList.add(i + "");
		}

		final MyTextViewAdapter adapter = new MyTextViewAdapter(this, R.layout.textview, yearList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String y = view.getTag().toString();
				back(Integer.parseInt(y));
			}
		});
	}
	
	
	private void back(int year){
		Intent intent = new Intent();
		intent.putExtra(Const.ENTR_YEAR_KEY, year);
		setResult(12, intent);
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			back(original_year);
		}
		return true;
	}
	
}
