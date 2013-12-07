package com.xynxs.main;

import java.util.ArrayList;
import java.util.List;

import com.xynxs.main.util.Const;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class GuideActivity extends BaseActivity implements OnClickListener {

	private ViewPager viewpager = null;
	private List<View> list = null;
	private ImageView[] img = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_nav);
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		list = new ArrayList<View>();

		View view4 = createView(R.layout.nav_04);
		view4.findViewById(R.id.guide_btn).setOnClickListener(this);
		list.add(createView(R.layout.nav_01));
		list.add(createView(R.layout.nav_02));
		list.add(createView(R.layout.nav_03));
		list.add(view4);

		img = new ImageView[list.size()];
		LinearLayout layout = (LinearLayout) findViewById(R.id.viewGroup);
		for (int i = 0; i < list.size(); i++) {
			img[i] = new ImageView(this);
			int size = getResources().getDimensionPixelSize(R.dimen.nav_round_size);
			LayoutParams lp = new LayoutParams(size, size);
			int margin = getResources().getDimensionPixelSize(R.dimen.nav_round_padding);
			lp.setMargins(margin, 0, margin, 0);
			img[i].setLayoutParams(lp);
			if (0 == i) {
				img[i].setBackgroundResource(R.drawable.nav_select);
			} else {
				img[i].setBackgroundResource(R.drawable.nav_default);
			}
			img[i].getBackground().setAlpha(150);

			layout.addView(img[i]);
		}
		viewpager.setAdapter(new ViewPagerAdapter(list));
		viewpager.setOnPageChangeListener(new ViewPagerPageChangeListener());
		super.onCreate(savedInstanceState);
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

	@Override
	public void onClick(View v) {
		saveDataInt(Const.VERSION_KEY, getVersionCode());
		Intent intent = null;
		if(hasLogin()){
			intent = new Intent(getApplicationContext(), MainActivity.class);
		}else{
			intent = new Intent(getApplicationContext(), LoginActivity.class);
		}
		finish();
		startActivity(intent);
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
			// 更新图标
			for (int i = 0; i < list.size(); i++) {
				if (page == i) {
					img[i].setBackgroundResource(R.drawable.nav_select);
				} else {
					img[i].setBackgroundResource(R.drawable.nav_default);
				}
			}
		}
	}

}
