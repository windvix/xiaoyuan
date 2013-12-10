package com.xynxs.main.adapter;

import java.util.List;

import com.xynxs.main.BaseActivity;
import com.xynxs.main.R;
import com.xynxs.main.bean.Post;
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

public class PostListAdapter extends ArrayAdapter<Post> {

	private BaseActivity act;

	private int resId;

	private List<Post> list;

	private String topic;

	private boolean showTopic = false;

	private boolean hasMore = false;

	public PostListAdapter(BaseActivity act, int resId, List<Post> objects, String topic, boolean hasMore) {
		super(act, resId, objects);
		this.act = act;
		this.resId = resId;
		this.list = objects;
		this.topic = topic;
		if (topic != null) {
			if (topic.startsWith("全")) {
				showTopic = true;
			} else {
				showTopic = false;
			}
		}
		this.hasMore = hasMore;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Post post = getItem(position);

		boolean hasData = true;
		if (list.size() == 1) {
			if (StringUtil.isEmpty(post.getId())) {
				convertView = getNoDataView();
				hasData = false;
			}
		}
		if (hasData) {
			if (position < (list.size() - 1)) {
				if (convertView == null) {
					convertView = act.createView(R.layout.list_post_template);
				}else if(convertView.findViewById(R.id.post_user_img)==null){
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
				postTemp.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

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
				} else {
					postTemp.findViewById(R.id.post_content_img_layout).setVisibility(View.GONE);
				}

				// 浏览
				visitView.setText(post.getVisitCount() + "");
				replyView.setText(post.getCommentCount() + "");

			}
			//最后一条
			else{
				if(convertView==null){
					convertView = act.createView(R.layout.list_end_no_more);
				}else if(convertView.findViewById(R.id.loadmore_tv)==null){
					convertView = act.createView(R.layout.list_end_no_more);
				}
				TextView msg = (TextView)convertView.findViewById(R.id.loadmore_tv);
				if(hasMore){
					msg.setText("加载更多");
				}else{
					msg.setText("没有更多了");
				}
			}
		}
		return convertView;
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

}
