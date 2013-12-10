package com.xynxs.main;

import java.util.ArrayList;
import java.util.List;

import com.xynxs.main.component.MyViewPager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivityTab01 {

	private MainActivity act;
	
	private View rootView;
	
	private MyViewPager viewpager = null;
	
	private List<View> list = null;
	
	private View selectView01 = null;
	private View selectView02 = null;
	private View selectView03 = null;
	
	private MainActivityTab01_01 tab01;
	
	public MainActivityTab01(MainActivity activity, View view01){
		this.act = activity;
		this.rootView = view01;
		
		viewpager = (MyViewPager) rootView.findViewById(R.id.select_tabpager);
		
		list = new ArrayList<View>();
		
		selectView01 = act.createView(R.layout.select_tab_01);
		selectView02 = act.createView(R.layout.select_tab_02);
		selectView03 = act.createView(R.layout.select_tab_03);
		
		list.add(selectView01);
		list.add(selectView02);
		list.add(selectView03);
		
		viewpager.setAdapter(new TabViewPagerAdapter(list));
		viewpager.setOnPageChangeListener(new TabViewPagerPageChangeListener());
		
		rootView.findViewById(R.id.select_new_tab).setOnClickListener(new MyOnClickListener(0));
		rootView.findViewById(R.id.select_hot_tab).setOnClickListener(new MyOnClickListener(1));
		rootView.findViewById(R.id.select_intr_tab).setOnClickListener(new MyOnClickListener(2));
	
		tab01 = new MainActivityTab01_01(activity, list.get(0));
	}

	
	
	private class TabViewPagerAdapter extends PagerAdapter {

		private List<View> list = null;

		public TabViewPagerAdapter(List<View> list) {
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

	private class TabViewPagerPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onPageScrolled(int page, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int page) {
			((TextView)rootView.findViewById(R.id.select_new_tab_tv)).setTextColor(act.getResources().getColor(R.color.select_tab_default));
			((TextView)rootView.findViewById(R.id.select_hot_tab_tv)).setTextColor(act.getResources().getColor(R.color.select_tab_default));
			((TextView)rootView.findViewById(R.id.select_intr_tab_tv)).setTextColor(act.getResources().getColor(R.color.select_tab_default));
			
			rootView.findViewById(R.id.select_hot_tab_line).setBackgroundResource(R.drawable.select_tab_bg);
			rootView.findViewById(R.id.select_new_tab_line).setBackgroundResource(R.drawable.select_tab_bg);
			rootView.findViewById(R.id.select_intr_tab_line).setBackgroundResource(R.drawable.select_tab_bg);
			
			if(page==0){
				((TextView)rootView.findViewById(R.id.select_new_tab_tv)).setTextColor(act.getResources().getColor(R.color.select_tab_pressed));
				rootView.findViewById(R.id.select_new_tab_line).setBackgroundResource(R.drawable.select_tab_bg_click);
				
			}
			if(page==1){
				((TextView)rootView.findViewById(R.id.select_hot_tab_tv)).setTextColor(act.getResources().getColor(R.color.select_tab_pressed));
				rootView.findViewById(R.id.select_hot_tab_line).setBackgroundResource(R.drawable.select_tab_bg_click);
			}
			if(page==2){
				((TextView)rootView.findViewById(R.id.select_intr_tab_tv)).setTextColor(act.getResources().getColor(R.color.select_tab_pressed));
				rootView.findViewById(R.id.select_intr_tab_line).setBackgroundResource(R.drawable.select_tab_bg_click);
			}
		}
	}
	
	private class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewpager.setCurrentItem(index);
		}
	};

}
