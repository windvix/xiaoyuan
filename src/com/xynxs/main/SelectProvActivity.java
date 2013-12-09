package com.xynxs.main;

import java.util.ArrayList;
import java.util.List;

import com.xynxs.main.component.MyTextViewAdapter;
import com.xynxs.main.util.Const;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 省份选择界面
 */
public class SelectProvActivity extends BaseActivity {

	private ViewPager viewpager;
	
	
	private List<View> views;
	
	
	private String orginal_college;
	private String orginal_city;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_prov_select);
		
		orginal_college = getIntent().getStringExtra(Const.COLLEGE_NAME_KEY);
		orginal_city = getIntent().getStringExtra(Const.CITY_NAME_KEY);
		
		viewpager = (ViewPager)findViewById(R.id.viewpager);
		
		
		views = new ArrayList<View>();
		views.add(createView(R.layout.login_03));
		views.add(createView(R.layout.login_04));
		
		
		viewpager.setAdapter(new ViewPagerAdapter(views));
		viewpager.setCurrentItem(0);
		initProvs();
		super.onCreate(savedInstanceState);
	}

	/**
	 * 初始化省份视图
	 */
	private void initProvs() {
		View provView = views.get(0);
		final EditText provEt = (EditText) provView.findViewById(R.id.prov_search_et);
		ListView plistView = (ListView) provView.findViewById(R.id.prov_listview);
		provView.findViewById(R.id.title_bar_left_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				back(orginal_city, orginal_college);
			}
		});
		final MyTextViewAdapter adapter = new MyTextViewAdapter(this, R.layout.textview, initProvList());
		plistView.setAdapter(adapter);
		provEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String content = provEt.getText().toString();
				adapter.filter(content);
			}
		});
		plistView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String select_prov = view.getTag().toString();
				showCollegeView(select_prov);
			}
		});
	}

	/**
	 * 显示选择学校视图
	 */
	private void showCollegeView(final String prov) {
		View collegeView = views.get(1);
		final EditText colEt = (EditText) collegeView.findViewById(R.id.college_search_et);
		collegeView.findViewById(R.id.title_bar_left_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				viewpager.setCurrentItem(0);
			}
		});
		ListView listView = (ListView) collegeView.findViewById(R.id.college_listview);
		final MyTextViewAdapter adapter = new MyTextViewAdapter(this, R.layout.textview, initCollegeList(prov));
		listView.setAdapter(adapter);
		viewpager.setCurrentItem(1);

		colEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String content = colEt.getText().toString();
				adapter.filter(content);
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String select_college = view.getTag().toString();
				back(prov, select_college);
			}
		});
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if(viewpager.getCurrentItem()==1){
				viewpager.setCurrentItem(0);
			}else{
				back(orginal_city, orginal_college);
			}
		}
		return true;
	}
	
	/**
	 * 返回
	 */
	private void back(String city, String college){
		Intent intent = new Intent();
		intent.putExtra(Const.CITY_NAME_KEY, city);
		intent.putExtra(Const.COLLEGE_NAME_KEY, college);
		setResult(11, intent);
		finish();
	}
	
	class ViewPagerAdapter extends PagerAdapter {
		private List<View> list = null;

		public ViewPagerAdapter(List<View> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(list.get(position));
			return list.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(list.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	class ViewPagerPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int state) {

		}

		@Override
		public void onPageScrolled(int page, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int page) {
		}
	}
}
