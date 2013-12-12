package com.xynxs.main;

import java.util.ArrayList;
import java.util.List;

import com.xynxs.main.component.ListPostTaskHelper;
import com.xynxs.main.component.MyViewPager;
import com.xynxs.main.dialog.SearchDialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivityTab01 implements OnClickListener , ListPostTaskHelper{

	private MainActivity act;

	private View rootView;

	private MyViewPager viewpager = null;

	private List<View> list = null;

	private View selectView01 = null;
	private View selectView02 = null;
	private View selectView03 = null;

	private MainPostListNewest tab01;
	private MainPostListHotest tab02;
	private MainPostListFocus tab03;

	private TextView titleTv;
	private Button rightBtn;
	private Button leftBtn;

	public MainActivityTab01(MainActivity activity, View view01) {
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

		titleTv = (TextView) rootView.findViewById(R.id.title_bar_title_tv);
		rightBtn = (Button) rootView.findViewById(R.id.title_bar_right_btn);
		leftBtn = (Button) rootView.findViewById(R.id.title_bar_left_btn);

		tab01 = new MainPostListNewest(this, list.get(0), rightBtn, titleTv);
		tab02 = new MainPostListHotest(this, list.get(1), rightBtn, titleTv, leftBtn);
		tab03 = new MainPostListFocus(this, list.get(2), titleTv);

		rightBtn.setOnClickListener(this);
		leftBtn.setOnClickListener(this);
		rootView.findViewById(R.id.title_bar_title_layout).setOnClickListener(this);

		viewpager.setCurrentItem(0);
	}

	public void searchKeyword(String keyword) {
		tab01.refresh(keyword);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		int currentTab = viewpager.getCurrentItem();
		if (id == R.id.title_bar_right_btn) {
			// 只有tab1和tab2执行
			if (currentTab <= 1) {
				showScopeSelect();
			}
		} else if (id == R.id.title_bar_title_layout) {
			// 共有，三个tab都要执行
			showTopicSelect();
		} else if (id == R.id.title_bar_left_btn) {
			// 第一个tab执行搜索
			if (currentTab == 0) {
				new SearchDialog(this).show();
			}
			// 第二个tab选择时间
			else if (currentTab == 1) {
				showTimeSelect();
			}
		}
	}

	private static final String[] TIMES = new String[] { "今日", "本周", "本月" };

	private void showTimeSelect() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(act);
		dialog.setTitle("时间范围");
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		int checked = 0;
		String ti = leftBtn.getText().toString();
		for (int i = 0; i < TIMES.length; i++) {
			String temp = TIMES[i];
			if (temp.startsWith(ti)) {
				checked = i;
			}
		}
		dialog.setSingleChoiceItems(TIMES, checked, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String temp = TIMES[which];
				leftBtn.setText(temp);
				dialog.dismiss();
				tab02.refresh();
			}
		});
		dialog.setNegativeButton("取消", null);
		dialog.show();
	}

	private static final String[] TOPICS = new String[] { "全部话题", "校园求助", "宿舍水塘", "学习交流", "休闲娱乐", "情感天地", "体育沙龙", "游戏国度", "旅行摄影" };

	private void showTopicSelect() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(act);
		dialog.setTitle("话题");
		dialog.setIcon(android.R.drawable.ic_dialog_info);
		int checked = 0;
		String top = titleTv.getText().toString();
		for (int i = 0; i < TOPICS.length; i++) {
			String temp = TOPICS[i];
			if (temp.startsWith(top)) {
				checked = i;
			}
		}
		dialog.setSingleChoiceItems(TOPICS, checked, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String temp = TOPICS[which];
				titleTv.setText(temp);
				dialog.dismiss();
				int currentTab = viewpager.getCurrentItem();
				if (currentTab == 0) {
					tab01.refresh("");
				}else if(currentTab==1){
					tab02.refresh();
				}
			}
		});
		dialog.setNegativeButton("取消", null);
		dialog.show();
	}

	private static final String[] SCOPES = new String[] { "全国", "全校" };

	private void showScopeSelect() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(act);
		dialog.setTitle("范围");
		dialog.setIcon(android.R.drawable.ic_dialog_info);

		int checked = 0;
		String scope = rightBtn.getText().toString();
		for (int i = 0; i < SCOPES.length; i++) {
			String temp = SCOPES[i];
			if (temp.startsWith(scope)) {
				checked = i;
			}
		}

		dialog.setSingleChoiceItems(SCOPES, checked, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String temp = SCOPES[which];
				rightBtn.setText(temp);
				dialog.dismiss();
				int currentTab = viewpager.getCurrentItem();
				if(currentTab==0){
					tab01.refresh("");
				}else if(currentTab==1){
					tab02.refresh();
				}
			}
		});
		dialog.setNegativeButton("取消", null);
		dialog.show();
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
			((TextView) rootView.findViewById(R.id.select_new_tab_tv)).setTextColor(act.getResources().getColor(R.color.select_tab_default));
			((TextView) rootView.findViewById(R.id.select_hot_tab_tv)).setTextColor(act.getResources().getColor(R.color.select_tab_default));
			((TextView) rootView.findViewById(R.id.select_intr_tab_tv)).setTextColor(act.getResources().getColor(R.color.select_tab_default));

			rootView.findViewById(R.id.select_hot_tab_line).setBackgroundResource(R.drawable.select_tab_bg);
			rootView.findViewById(R.id.select_new_tab_line).setBackgroundResource(R.drawable.select_tab_bg);
			rootView.findViewById(R.id.select_intr_tab_line).setBackgroundResource(R.drawable.select_tab_bg);

			if (page == 0) {
				((TextView) rootView.findViewById(R.id.select_new_tab_tv)).setTextColor(act.getResources().getColor(R.color.select_tab_pressed));
				rootView.findViewById(R.id.select_new_tab_line).setBackgroundResource(R.drawable.select_tab_bg_click);
				rootView.findViewById(R.id.title_bar_left_icon).setVisibility(View.VISIBLE);
				leftBtn.setText("");
				leftBtn.setClickable(true);
				rightBtn.setClickable(true);
				rootView.findViewById(R.id.title_bar_right_layout).setVisibility(View.VISIBLE);
				rootView.findViewById(R.id.title_bar_left_layout).setVisibility(View.VISIBLE);
				((ImageView) rootView.findViewById(R.id.title_bar_left_icon)).setImageResource(R.drawable.title_bar_search_icon);
				tab02.stopAllTask();
				tab03.stopAllTask();
			}
			if (page == 1) {
				((TextView) rootView.findViewById(R.id.select_hot_tab_tv)).setTextColor(act.getResources().getColor(R.color.select_tab_pressed));
				rootView.findViewById(R.id.select_hot_tab_line).setBackgroundResource(R.drawable.select_tab_bg_click);
				rootView.findViewById(R.id.title_bar_left_icon).setVisibility(View.GONE);
				leftBtn.setText("本周");
				leftBtn.setClickable(true);
				rightBtn.setClickable(true);
				rootView.findViewById(R.id.title_bar_right_layout).setVisibility(View.VISIBLE);
				rootView.findViewById(R.id.title_bar_left_layout).setVisibility(View.VISIBLE);
				tab01.stopAllTask();
				tab03.stopAllTask();
			}
			if (page == 2) {
				((TextView) rootView.findViewById(R.id.select_intr_tab_tv)).setTextColor(act.getResources().getColor(R.color.select_tab_pressed));
				rootView.findViewById(R.id.select_intr_tab_line).setBackgroundResource(R.drawable.select_tab_bg_click);
				rootView.findViewById(R.id.title_bar_left_icon).setVisibility(View.GONE);
				rootView.findViewById(R.id.title_bar_left_layout).setVisibility(View.INVISIBLE);
				rootView.findViewById(R.id.title_bar_right_layout).setVisibility(View.INVISIBLE);
				leftBtn.setClickable(false);
				rightBtn.setClickable(false);
				tab02.stopAllTask();
				tab01.stopAllTask();
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
			int cur = viewpager.getCurrentItem();
			if(cur==index){
				if(cur==0){
					tab01.refresh();
				}else if(cur==1){
					tab02.refresh();
				}else if(cur==2){
					tab03.refresh();
				}
			}else{
				viewpager.setCurrentItem(index, false);
			}
		}
	};

	/**
	 * 
	 */
	@Override
	public MainActivity getActivity() {
		return act;
	}

	@Override
	public void setPostList(String data) {
		int cur = viewpager.getCurrentItem();
		if (cur == 0) {
			tab01.onRefreshResult(data);
		}else if(cur==1){
			tab02.onRefreshResult(data);
		}else if(cur==2){
			tab03.onRefreshResult(data);
		}
	}
	
	
	public void refresh(){
		int cur = viewpager.getCurrentItem();
		if(cur==0){
			tab01.refresh();
		}else if(cur==1){
			tab02.refresh();
		}else if(cur==2){
			tab03.refresh();
		}
	}
}
