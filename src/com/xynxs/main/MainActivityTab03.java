package com.xynxs.main;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnPullEventListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.xynxs.main.bean.Post;
import com.xynxs.main.bean.User;
import com.xynxs.main.component.ListUserPostHelper;
import com.xynxs.main.task.GetUserPostCountTask;
import com.xynxs.main.task.ListUserPostTask;
import com.xynxs.main.task.LoadHeadImgTask;
import com.xynxs.main.task.LoadImgTask;
import com.xynxs.main.util.Const;
import com.xynxs.main.util.LevelUtil;
import com.xynxs.main.util.ServerHelper;
import com.xynxs.main.util.StringUtil;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivityTab03 implements ListUserPostHelper, OnClickListener, OnRefreshListener<ScrollView> {

	private MainActivity act;

	private View root;

	private User user;

	private int index = 0;
	private int PAGE_SIZE = 15;

	private ListUserPostTask userPostTask;
	private GetUserPostCountTask postCountTask;

	private PullToRefreshScrollView scrollView;

	public MainActivityTab03(MainActivity activity, View view03) {

		this.root = view03;
		this.act = activity;
		user = act.getUser();

		loadMoreView = root.findViewById(R.id.user_center_post_loadmore_layout);
		contentLayout = (LinearLayout) root.findViewById(R.id.user_center_post_layout);

		scrollView = (PullToRefreshScrollView) root.findViewById(R.id.user_center_scrollview);

		scrollView.setOnPullEventListener(new OnPullEventListener<ScrollView>() {

			@Override
			public void onPullEvent(PullToRefreshBase<ScrollView> refreshView, State state, Mode direction) {
				refreshView.findViewById(R.id.pull_to_refresh_sub_text).setVisibility(View.VISIBLE);
			}
		});

		scrollView.setOnRefreshListener(this);

		initData();

		root.findViewById(R.id.title_bar_left_btn).setOnClickListener(this);
		root.findViewById(R.id.title_bar_right_btn).setOnClickListener(this);

	}

	private TextView theColege;
	private TextView nameTv;
	private TextView gradeTv;
	private TextView fav;
	private TextView gender;
	private ImageView head;
	private TextView intro;
	private TextView sayCount;
	private TextView levelTv;

	private View loadMoreView;
	private LinearLayout contentLayout;

	@SuppressWarnings("unchecked")
	private void initData() {
		View view = root;
		theColege = (TextView) view.findViewById(R.id.main_bottom_tab_03_college);
		nameTv = (TextView) view.findViewById(R.id.main_bottom_tab_03_name);
		gradeTv = (TextView) view.findViewById(R.id.main_bottom_tab_03_grade);
		fav = (TextView) view.findViewById(R.id.main_bottom_tab_03_fav_count);
		gender = (TextView) view.findViewById(R.id.main_bottom_tab_03_gender);
		head = (ImageView) view.findViewById(R.id.main_bottom_tab_03_head);
		intro = (TextView) view.findViewById(R.id.main_bottom_tab_03_intro);
		sayCount = (TextView) view.findViewById(R.id.main_bottom_tab_03_saying_count);
		levelTv = (TextView) view.findViewById(R.id.main_bottom_tab_03_level);

		levelTv.setText(LevelUtil.getLevel(user.getScore()) + "级");

		view.findViewById(R.id.main_bottom_tab_03_detail_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(act, MyInfoActivity.class);
				act.startActivity(intent);
			}
		});

		nameTv.setText(user.getName());
		gradeTv.setText(user.getEntranceYear() + "级");

		head.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(act, MyInfoActivity.class);
				act.startActivity(intent);
			}
		});

		// 我的
		view.findViewById(R.id.main_bottom_tab_03_saying_layout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});

		// 关注
		view.findViewById(R.id.main_bottom_tab_03_fav_layout).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

		head.setTag(user.getId() + Const.MIN_JPG);

		theColege.setText(user.getCollege_name());
		fav.setText("-");

		act.setGenderText(gender, user.getGender());

		String inr = user.getSign();
		if (inr == null) {
			inr = "";
		}
		if (inr.equals("")) {
			inr = "这个人很懒， 什么也没留下...";
		}
		if (inr.length() > 24) {
			inr = inr.substring(0, 23) + "......";
		}
		intro.setText(inr);
		new LoadHeadImgTask(act, head, user.getHead_img());
		sayCount.setText("-");

		// 加载历史
		String postsStr = act.getDataString(Const.MY_POST_LIST_KEY);
		if (!StringUtil.isEmpty(postsStr)) {
			List<String> strList = (List<String>) act.convert(postsStr, ArrayList.class);
			List<Post> postList = new ArrayList<Post>();
			for (String one : strList) {
				Post post = (Post) act.convert(one, Post.class);
				
				String tempOne = act.getDataString(post.getId());
				if(!StringUtil.isEmpty(tempOne)){
					Post tempPost = (Post) act.convert(tempOne, Post.class);
					if(!StringUtil.isEmpty(tempPost.getId())){
						post = tempPost;
					}
				}
				
				
				postList.add(post);
			}
			long lastUpt = act.getDataLong(Const.MY_POST_LIST_TIME_KEY);
			setRefreshTime(lastUpt);

			showMyPost(postList, false);
			showNoMore();
		} else {
			setRefreshTime(0);
			showLoadingMore();
		}
	}

	private boolean isRefresh = false;

	public boolean isRefeshed() {
		return isRefresh;
	}

	public void loadData() {
		isRefresh = true;

		long lastUpt = act.getDataLong(Const.MY_POST_LIST_TIME_KEY);

		if (System.currentTimeMillis() - lastUpt > Const.REFRESH_PERIOD) {
			refresh();
		}
	}

	public void refresh() {
		if(userPostTask!=null){
			userPostTask.stopTask();
		}
		if(postCountTask!=null){
			postCountTask.stopTask();
		}
		scrollView.setRefreshing();
		userPostTask = new ListUserPostTask(MainActivityTab03.this, user.getId(), 0, PAGE_SIZE);
		userPostTask.startTask();
		postCountTask = new GetUserPostCountTask(MainActivityTab03.this, user.getId());
		postCountTask.startTask();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void getPostResult(String data, boolean isAppend) {
		scrollView.onRefreshComplete();
		if (StringUtil.isEmpty(data)) {
			if (isAppend) {
				act.toast("网络不给力，请稍后再试");
			}
			showLoadMore();
		} else {
			List<String> strList = null;
			if (data.startsWith("FAIL")) {
				strList = new ArrayList<String>();
			} else {
				strList = (List<String>) act.convert(data, ArrayList.class);
			}

			if (!isAppend) {
				index = 0;
			}

			List<Post> postList = new ArrayList<Post>();
			for (String oneStr : strList) {
				Post post = (Post) act.convert(oneStr, Post.class);
				
				//保存一条
				act.saveDataString(post.getId(), oneStr);
				postList.add(post);
			}
			if (index >= PAGE_SIZE) {
				index = index + PAGE_SIZE;
				showLoadMore();
			} else {
				showNoMore();
			}

			if (postList.size() == 0 && !isAppend) {
				showNoPost();
			} else {

				showMyPost(postList, isAppend);
			}
			if (!isAppend) {
				saveMyPost(strList);
			}
		}
	}

	private void showMyPost(List<Post> postList, boolean isAppend) {
		if (!isAppend) {
			contentLayout.removeAllViews();
		}

		for (Post post : postList) {
			View postTemp = act.createView(R.layout.list_post_template);
			initOnePost(post, postTemp);
			contentLayout.addView(postTemp);
		}

	}

	private void saveMyPost(List<String> postList) {
		String val = new Gson().toJson(postList);
		act.saveDataString(Const.MY_POST_LIST_KEY, val);
		act.saveDataLong(Const.MY_POST_LIST_TIME_KEY, System.currentTimeMillis());
		setRefreshTime(System.currentTimeMillis());
	}

	private void initOnePost(final Post post, View root) {

		View postTemp = root;

		ImageView headView = (ImageView) postTemp.findViewById(R.id.post_user_img);
		TextView nameView = (TextView) postTemp.findViewById(R.id.post_user_name);
		TextView collegeView = (TextView) postTemp.findViewById(R.id.post_user_college);
		TextView genderView = (TextView) postTemp.findViewById(R.id.post_user_gender);
		TextView timeView = (TextView) postTemp.findViewById(R.id.post_time);
		TextView contentView = (TextView) postTemp.findViewById(R.id.post_content);
		ImageView imgView = (ImageView) postTemp.findViewById(R.id.post_content_img);
		View thankLayout = postTemp.findViewById(R.id.post_thank_layout);
		TextView thankView = (TextView) postTemp.findViewById(R.id.post_thank_type);
		TextView visitView = (TextView) postTemp.findViewById(R.id.post_visit_count);
		TextView replyView = (TextView) postTemp.findViewById(R.id.post_reply_count);

		int noname = post.getIsNoname();
		// 加载头像
		if (noname < 1) {
			headView.setTag(act.getUser().getId() + Const.MIN_JPG);
			new LoadHeadImgTask(act, headView, user.getHead_img());

		}
		act.setGenderText(genderView, act.getUser().getGender());
		// 名字
		if (noname > 0) {
			nameView.setText("匿名用户");
		} else {
			nameView.setText(act.getUser().getName());
		}

		// CO
		collegeView.setText(act.getUser().getCollege_name());
		// 时间
		timeView.setText(StringUtil.getDateStr(post.getCreateDate()));
		// ID
		postTemp.setTag(post.getId());
		// 增加一个post的点击事件
		postTemp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(act, PostDetailActivity.class);
				intent.putExtra(Const.POST_ID_KEY, post.getId());
				intent.putExtra(Const.GENDER_KEY, act.getUser().getGender());
				intent.putExtra(Const.POST_OWNER_ID_KEY, act.getUser().getId());
				act.startActivity(intent);
			}
		});

		// 内容
		if (post.getTopic().contains("助")) {
			thankLayout.setVisibility(View.VISIBLE);
			contentView.setText("【" + post.getTopic() + "】" + post.getContent().substring(0, post.getContent().lastIndexOf("【")));
			thankView.setText(post.getContent().substring(post.getContent().lastIndexOf("】") + 1));
		} else {
			thankLayout.setVisibility(View.INVISIBLE);
			contentView.setText("【" + post.getTopic() + "】" + post.getContent());
		}

		// 加载图片
		if (post.getHasImg() > 0) {
			imgView.setTag(post.getId() + Const.MIN_JPG);
			new LoadImgTask(act, imgView, ServerHelper.getUploadImgURL(post.getOwner_id(), post.getId() + Const.MIN_JPG));
			postTemp.findViewById(R.id.post_content_img_layout).setVisibility(View.VISIBLE);
			imgView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(act, BigPictureActivity.class);
					intent.putExtra(Const.BIG_PIC_URL, ServerHelper.getUploadImgURL(post.getOwner_id(), post.getId() + Const.MAX_JPG));
					intent.putExtra(Const.BIG_PIC_FILE_NAME, post.getId() + Const.MAX_JPG);
					intent.putExtra(Const.MIN_PIC_URL, ServerHelper.getUploadImgURL(post.getOwner_id(), post.getId() + Const.MIN_JPG));
					intent.putExtra(Const.MIN_PIC_FILE_NAME, post.getId() + Const.MIN_JPG);
					act.startActivity(intent);
				}
			});
		} else {
			postTemp.findViewById(R.id.post_content_img_layout).setVisibility(View.GONE);
		}

		// 浏览
		visitView.setText(post.getVisitCount() + "");
		replyView.setText(post.getCommentCount() + "");

	}

	private void showLoadMore() {
		TextView tv = (TextView) loadMoreView.findViewById(R.id.loadmore_tv);
		loadMoreView.findViewById(R.id.loadmore_icon).setVisibility(View.INVISIBLE);
		loadMoreView.setClickable(true);
		loadMoreView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		tv.setText("加载更多");
	}

	private void showLoadingMore() {
		TextView tv = (TextView) loadMoreView.findViewById(R.id.loadmore_tv);
		loadMoreView.findViewById(R.id.loadmore_icon).setVisibility(View.VISIBLE);
		loadMoreView.setClickable(false);
		tv.setText("正在加载");
	}

	private void showNoPost() {
		TextView tv = (TextView) loadMoreView.findViewById(R.id.loadmore_tv);
		loadMoreView.findViewById(R.id.loadmore_icon).setVisibility(View.INVISIBLE);
		loadMoreView.setClickable(false);
		tv.setText("没有发表任何动态");
	}

	private void showNoMore() {
		TextView tv = (TextView) loadMoreView.findViewById(R.id.loadmore_tv);
		loadMoreView.findViewById(R.id.loadmore_icon).setVisibility(View.INVISIBLE);
		loadMoreView.setClickable(false);
		tv.setText("没有更多了");
	}

	@Override
	public BaseActivity getActivity() {
		return act;
	}

	@Override
	public void getPostCountResult(String result) {
		if (StringUtil.isEmpty(result)) {
			result = "-1";
		}
		long count = -1;
		try {
			count = Long.parseLong(result.toString());
		} catch (Exception e) {

		}
		if (count >= 0) {
			sayCount.setText(count + "");
		} else {
			sayCount.setText("-");
		}

	}

	private void setRefreshTime(long time) {
		if (scrollView != null) {
			TextView tv = (TextView) scrollView.findViewById(R.id.pull_to_refresh_sub_text);
			if (tv != null) {
				if (time <= 100) {
					tv.setText("上次刷新 未知");
				} else {
					tv.setText("上次刷新 " + StringUtil.getDateStr(time));
				}
			}

		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id==R.id.title_bar_left_btn){
			Intent intent = new Intent(act, MyInfoActivity.class);
			act.startActivity(intent);
		}else if(id==R.id.title_bar_right_btn){
			refresh();
		}
	}

	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
		refresh();
	}
}
