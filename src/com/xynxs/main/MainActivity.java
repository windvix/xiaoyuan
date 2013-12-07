package com.xynxs.main;

import java.util.ArrayList;
import java.util.List;

import com.xynxs.main.component.MyViewPager;


import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainActivity extends BaseActivity {

	private MyViewPager viewpager = null;
	private List<View> list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.activity_main);

		viewpager = (MyViewPager) findViewById(R.id.tabpager);
		list = new ArrayList<View>();

		View view01 = createView(R.layout.main_tab_01);
		View view02 = createView(R.layout.main_tab_02);
		View view03 = createView(R.layout.main_tab_03);
		View view04 = createView(R.layout.main_tab_04);
		
		new MainActivityTab01(this, view01);
		new MainActivityTab02(this, view02);
		new MainActivityTab03(this, view03);
		new MainActivityTab04(this, view04);
		
		list.add(view01);
		list.add(view02);
		list.add(view03);
		list.add(view04);

		viewpager.setAdapter(new MainViewPagerAdapter(list));
		viewpager.setOnPageChangeListener(new MainViewPagerPageChangeListener());

		
		findViewById(R.id.show1).setOnClickListener(new MyOnClickListener(0));
		findViewById(R.id.show2).setOnClickListener(new MyOnClickListener(1));
		findViewById(R.id.show3).setOnClickListener(new MyOnClickListener(2));
		findViewById(R.id.show4).setOnClickListener(new MyOnClickListener(3));
		
		findViewById(R.id.img_frd).setOnClickListener(new MyOnClickListener(0));
		findViewById(R.id.img_msg).setOnClickListener(new MyOnClickListener(1));
		findViewById(R.id.img_info).setOnClickListener(new MyOnClickListener(2));
		findViewById(R.id.img_settings).setOnClickListener(new MyOnClickListener(3));

		
		viewpager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		
		super.onCreate(savedInstanceState);
	}
	
	private class MainViewPagerAdapter extends PagerAdapter {

		private List<View> list = null;

		public MainViewPagerAdapter(List<View> list) {
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

	private class MainViewPagerPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onPageScrolled(int page, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int page) {
			((ImageView) findViewById(R.id.img_frd)).setImageResource(R.drawable.tab_find_frd_normal);
			((ImageView) findViewById(R.id.img_msg)).setImageResource(R.drawable.tab_msg_normal);
			((ImageView) findViewById(R.id.img_info)).setImageResource(R.drawable.tab_address_normal);
			((ImageView) findViewById(R.id.img_settings)).setImageResource(R.drawable.tab_settings_normal);
			findViewById(R.id.show1).setBackgroundResource(R.drawable.bottom_tab_default);
			findViewById(R.id.show2).setBackgroundResource(R.drawable.bottom_tab_default);
			findViewById(R.id.show3).setBackgroundResource(R.drawable.bottom_tab_default);
			findViewById(R.id.show4).setBackgroundResource(R.drawable.bottom_tab_default);
			if (page == 0) {
				((ImageView) findViewById(R.id.img_frd)).setImageResource(R.drawable.tab_find_frd_pressed);
				findViewById(R.id.show1).setBackgroundResource(R.drawable.bottom_tab_click);
			}
			if (page == 1) {
				((ImageView) findViewById(R.id.img_msg)).setImageResource(R.drawable.tab_msg_pressed);
				findViewById(R.id.show2).setBackgroundResource(R.drawable.bottom_tab_click);
			}
			if (page == 2) {
				((ImageView) findViewById(R.id.img_info)).setImageResource(R.drawable.tab_address_pressed);
				findViewById(R.id.show3).setBackgroundResource(R.drawable.bottom_tab_click);
			}
			if (page == 3) {
				((ImageView) findViewById(R.id.img_settings)).setImageResource(R.drawable.tab_settings_pressed);
				findViewById(R.id.show4).setBackgroundResource(R.drawable.bottom_tab_click);
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			toQuitTheApp();
		}
		return false;
	}

}
