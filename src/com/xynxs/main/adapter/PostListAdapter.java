package com.xynxs.main.adapter;

import java.util.List;

import com.xynxs.main.BaseActivity;
import com.xynxs.main.BigPictureActivity;
import com.xynxs.main.MainActivity;
import com.xynxs.main.MainActivityTab01;
import com.xynxs.main.PostDetailActivity;
import com.xynxs.main.R;
import com.xynxs.main.bean.Post;
import com.xynxs.main.component.ListPostTaskHelper;
import com.xynxs.main.component.PostListAdapterHelper;
import com.xynxs.main.task.LoadHeadImgTask;
import com.xynxs.main.task.LoadImgTask;
import com.xynxs.main.util.Const;
import com.xynxs.main.util.ServerHelper;
import com.xynxs.main.util.StringUtil;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class PostListAdapter extends ArrayAdapter<Post> implements ListPostTaskHelper {

	private MainActivity act;

	private int resId;

	private List<Post> list;

	private String topic;

	private boolean showTopic = false;

	private boolean hasMore = false;

	private int page_size = 10;
	private PostListAdapterHelper helper;

	private int index = 0;

	private boolean isLoadingMore = false;

	public PostListAdapter(PostListAdapterHelper helper, int resId, List<Post> objects, String topic, int page_size) {
		super(helper.getActivity(), resId, objects);
		this.act = helper.getActivity();
		this.helper = helper;
		this.resId = resId;
		this.list = objects;
		this.topic = topic;
		this.page_size = page_size;
		if (topic != null) {
			if (topic.startsWith("全")) {
				showTopic = true;
			} else {
				showTopic = false;
			}
		}
		if (list.size() >= page_size) {
			hasMore = true;
		} else {
			hasMore = false;
		}
		if (hasMore) {
			index = page_size;
		}
		isLoadingMore  = false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Post post = getItem(position);

		boolean hasData = true;
		if (getCount() == 1) {
			if (StringUtil.isEmpty(post.getId())) {
				convertView = getNoDataView();
				hasData = false;
			}
		}
		if (hasData) {
			
			//非最后一条
			if (position < (getCount() - 1) && post!=null && post.getId()!=null) {
				if (convertView == null) {
					convertView = act.createView(R.layout.list_post_template);
				} else if (convertView.findViewById(R.id.post_user_img) == null) {
					convertView = act.createView(R.layout.list_post_template);
				}

				View postTemp = convertView;
				ImageView headView = (ImageView) postTemp.findViewById(R.id.post_user_img);
				TextView nameView = (TextView) postTemp.findViewById(R.id.post_user_name);
				TextView genderView = (TextView) postTemp.findViewById(R.id.post_user_gender);
				TextView collegeView = (TextView) postTemp.findViewById(R.id.post_user_college);
				TextView timeView = (TextView) postTemp.findViewById(R.id.post_time);
				TextView contentView = (TextView) postTemp.findViewById(R.id.post_content);
				ImageView imgView = (ImageView) postTemp.findViewById(R.id.post_content_img);
				View thankLayout = postTemp.findViewById(R.id.post_thank_layout);
				TextView thankView = (TextView) postTemp.findViewById(R.id.post_thank_type);
				TextView visitView = (TextView) postTemp.findViewById(R.id.post_visit_count);
				TextView replyView = (TextView) postTemp.findViewById(R.id.post_reply_count);

				int noname = post.getIsNoname();

				// 加载头像与名称
				if (noname < 1) {
					nameView.setText(post.getOwner_name());
					headView.setTag(post.getOwner_id() + Const.MIN_JPG);
					new LoadHeadImgTask(act, headView, post.getOwner_head_img());
				} else {
					nameView.setText("匿名用户");
					headView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							act.toast("无法获知对方身份");
						}
					});
				}

				setGenderText(genderView, post.getOwner_gender());

				// CO
				collegeView.setText(post.getOwner_college_name());
				// 时间
				timeView.setText(StringUtil.getDateStr(post.getCreateDate()));
				// ID
				postTemp.setTag(post.getId());

				// 增加一个post的点击事件
				postTemp.findViewById(R.id.list_post_rootLayout).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(act, PostDetailActivity.class);
						intent.putExtra(Const.POST_ID_KEY, post.getId());
						intent.putExtra(Const.GENDER_KEY, post.getOwner_gender());
						intent.putExtra(Const.POST_OWNER_ID_KEY, post.getOwner_id());
						intent.putExtra(Const.CAN_VISIT_HOME, 1);
						act.startActivity(intent);
					}
				});

				if (showTopic) {
					// 内容
					if (post.getTopic().contains("助")) {
						thankLayout.setVisibility(View.VISIBLE);
						contentView.setText("【" + post.getTopic() + "】" + post.getContent().substring(0, post.getContent().lastIndexOf("【")));
						thankView.setText(post.getContent().substring(post.getContent().lastIndexOf("】") + 1));
					} else {
						thankLayout.setVisibility(View.INVISIBLE);
						contentView.setText("【" + post.getTopic() + "】" + post.getContent());
					}
				} else {
					// 内容
					if (post.getTopic().contains("助")) {
						thankLayout.setVisibility(View.VISIBLE);
						contentView.setText(post.getContent().substring(0, post.getContent().lastIndexOf("【")));
						thankView.setText(post.getContent().substring(post.getContent().lastIndexOf("】") + 1));
					} else {
						thankLayout.setVisibility(View.INVISIBLE);
						contentView.setText(post.getContent());
					}
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
			// 最后一条
			else {
				if (convertView == null) {
					convertView = act.createView(R.layout.list_end_no_more);
				} else if (convertView.findViewById(R.id.loadmore_tv) == null) {
					convertView = act.createView(R.layout.list_end_no_more);
				}
				TextView msg = (TextView) convertView.findViewById(R.id.loadmore_tv);
				if (!isLoadingMore) {
					if (hasMore) {
						msg.setText("加载更多");
						nextPage(convertView.findViewById(R.id.refreshable_view_loadmore_layout));
						convertView.findViewById(R.id.loadmore_icon).setVisibility(View.GONE);
						convertView.findViewById(R.id.refreshable_view_loadmore_layout).setClickable(true);
					} else {
						msg.setText("没有更多了");
						convertView.findViewById(R.id.loadmore_icon).setVisibility(View.GONE);
						convertView.findViewById(R.id.refreshable_view_loadmore_layout).setClickable(false);
					}
				} else {
					msg.setText("正在加载");
					convertView.findViewById(R.id.refreshable_view_loadmore_layout).setClickable(false);
					convertView.findViewById(R.id.loadmore_icon).setVisibility(View.VISIBLE);
				}
			}
		}
		return convertView;
	}

	private View loadView;
	
	private void nextPage(final View loadView) {
		this.loadView = loadView;
		loadView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setClickable(false);
				TextView msg = (TextView) loadView.findViewById(R.id.loadmore_tv);
				msg.setText("正在加载");
				isLoadingMore = true;
				loadView.findViewById(R.id.loadmore_icon).setVisibility(View.VISIBLE);
				helper.nextPage(index);
			}
		});
	}

	@Override
	public void setPostList(String data) {
		isLoadingMore = false;
		if(StringUtil.isEmpty(data)){
			act.toast("网络不给力，请稍后再试");
			if(loadView!=null && loadView.findViewById(R.id.loadmore_tv)!=null){
				TextView msg = (TextView) loadView.findViewById(R.id.loadmore_tv);
				msg.setText("加载更多");
				loadView.findViewById(R.id.loadmore_icon).setVisibility(View.GONE);
				loadView.findViewById(R.id.refreshable_view_loadmore_layout).setClickable(true);
			}
		}else{
			List<Post> pList = helper.convertPostList(data);
			if(pList.size()>=page_size){
				hasMore = true;
			}
			
			TextView msg = (TextView) loadView.findViewById(R.id.loadmore_tv);
			
			if (hasMore) {
				msg.setText("加载更多");
				loadView.findViewById(R.id.loadmore_icon).setVisibility(View.GONE);
				loadView.findViewById(R.id.refreshable_view_loadmore_layout).setClickable(true);
			} else {
				msg.setText("没有更多了");
				loadView.findViewById(R.id.loadmore_icon).setVisibility(View.GONE);
				loadView.findViewById(R.id.refreshable_view_loadmore_layout).setClickable(false);
			}
			
			
			//删除adapter最后的空post
			this.remove(getItem(getCount()-1));
			for(Post post: pList){
				this.add(post);
			}
			index+=page_size;
			//告诉数据已变化
			this.notifyDataSetChanged();
		}
	}
	
	
	public void setGenderText(TextView genderView, int gender) {
		if (genderView != null) {
			if (gender >= 2) {
				String text = "<font color='#ef2e71'><b>♀</b></font>";
				genderView.setText(Html.fromHtml(text));
			} else {
				String text = "<font color='#0385e0'><b>♂</b></font>";
				genderView.setText(Html.fromHtml(text));
			}
		}
	}

	private View getNoDataView() {
		View view = act.createView(R.layout.list_no_data);
		LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		int height = act.getScreenHeight() - act.getResources().getDimensionPixelSize(R.dimen.TITLE_BAR_HEIGHT) * 2 - act.getResources().getDimensionPixelSize(R.dimen.SELECT_TAB_HEIGHT) - act.getResources().getDimensionPixelSize(R.dimen.BOTTOM_BAR_HEIGHT);
		params.height = height;

		view.findViewById(R.id.no_data_root_layout).setLayoutParams(params);
		return view;
	}

	@Override
	public MainActivity getActivity() {
		return act;
	}



}
