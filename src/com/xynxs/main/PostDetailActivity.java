package com.xynxs.main;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.xynxs.main.bean.Post;
import com.xynxs.main.bean.Reply;
import com.xynxs.main.component.CommentHelper;
import com.xynxs.main.dialog.CommentDialog;
import com.xynxs.main.task.ListCommentTask;
import com.xynxs.main.task.LoadHeadImgTask;
import com.xynxs.main.task.LoadImgTask;
import com.xynxs.main.task.PostDetailTask;
import com.xynxs.main.util.Const;
import com.xynxs.main.util.ServerHelper;
import com.xynxs.main.util.StringUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class PostDetailActivity extends BaseActivity implements OnClickListener, OnRefreshListener<ScrollView>, CommentHelper{

	private String post_id;

	private Post currentPost;

	private PostDetailTask detailTask;
	private ListCommentTask listCommentTask;
	
	private PullToRefreshScrollView scrollview;
	
	private static final int COUNT = 20;
	
	private int index = 0;
	
	private LinearLayout commentContentLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.activity_post_detail);
		post_id = getIntent().getStringExtra(Const.POST_ID_KEY);
		
		commentContentLayout = (LinearLayout)findViewById(R.id.post_detail_comment_layout);
		
		scrollview = (PullToRefreshScrollView)findViewById(R.id.post_detail_scrollview);
		
		String postStr = getDataString(post_id);
		if (StringUtil.isEmpty(postStr)) {
			currentPost = new Post();
		} else {
			currentPost = (Post) convert(postStr, Post.class);
		}

		super.onCreate(savedInstanceState);
		
		
		findViewById(R.id.title_bar_left_btn).setOnClickListener(this);
		findViewById(R.id.title_bar_right_btn).setOnClickListener(this);
		
		findViewById(R.id.post_detail_loadmore_layout).setOnClickListener(this);
		findViewById(R.id.post_detail_loadmore_layout).setClickable(false);
		
		initData();

		// 加载数据
		detailTask = new PostDetailTask(this, currentPost.getId(), currentPost.getOwner_id());
		detailTask.startTask();
		
		listCommentTask = new ListCommentTask(this, currentPost.getId(), 0, COUNT);
		listCommentTask.startTask();
		showLoadingMoreComment();
		
		
		findViewById(R.id.post_comment_btn).setOnClickListener(this);
		
		scrollview.setMode(Mode.PULL_FROM_START);
		
		scrollview.setOnRefreshListener(this);
		
		
	}

	/**
	 * 初始化数据
	 */
	private void initData() {

		final Post post = currentPost;

		ImageView headView = (ImageView) findViewById(R.id.post_user_img);
		TextView nameView = (TextView) findViewById(R.id.post_user_name);
		TextView genderView = (TextView) findViewById(R.id.post_user_gender);
		TextView collegeView = (TextView) findViewById(R.id.post_user_college);
		TextView timeView = (TextView) findViewById(R.id.post_time);
		TextView contentView = (TextView) findViewById(R.id.post_content);
		ImageView imgView = (ImageView) findViewById(R.id.post_content_img);
		View thankLayout = findViewById(R.id.post_thank_layout);
		TextView thankView = (TextView) findViewById(R.id.post_thank_type);
		TextView visitView = (TextView) findViewById(R.id.post_visit_count);
		TextView replyView = (TextView) findViewById(R.id.post_reply_count);

		int noname = currentPost.getIsNoname();

		// 加载头像与名称
		if (noname < 1) {
			nameView.setText(currentPost.getOwner_name());
			headView.setTag(currentPost.getOwner_id() + Const.MIN_JPG);
			headView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

				}
			});
			new LoadHeadImgTask(this, headView, currentPost.getOwner_head_img());
		} else {
			nameView.setText("匿名用户");
			headView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					toast("无法获知对方身份");
				}
			});
		}

		setGenderText(genderView, currentPost.getOwner_gender());

		// CO
		collegeView.setText(currentPost.getOwner_college_name());
		// 时间
		timeView.setText(StringUtil.getDateStr(currentPost.getCreateDate()));

		if (currentPost.getTopic().contains("助")) {
			thankLayout.setVisibility(View.VISIBLE);
			contentView.setText("【" + currentPost.getTopic() + "】" + currentPost.getContent().substring(0, currentPost.getContent().lastIndexOf("【")));
			thankView.setText(currentPost.getContent().substring(currentPost.getContent().lastIndexOf("】") + 1));
		} else {
			thankLayout.setVisibility(View.INVISIBLE);
			contentView.setText("【" + currentPost.getTopic() + "】" + currentPost.getContent());
		}

		// 加载图片
		if (currentPost.getHasImg() > 0) {
			imgView.setTag(post.getId() + Const.MIN_JPG);
			new LoadImgTask(this, imgView, ServerHelper.getUploadImgURL(post.getOwner_id(), post.getId() + Const.MIN_JPG));
			findViewById(R.id.post_content_img_layout).setVisibility(View.VISIBLE);
			imgView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(PostDetailActivity.this, BigPictureActivity.class);
					intent.putExtra(Const.BIG_PIC_URL, ServerHelper.getUploadImgURL(post.getOwner_id(), post.getId() + Const.MAX_JPG));
					intent.putExtra(Const.BIG_PIC_FILE_NAME, post.getId() + Const.MAX_JPG);
					intent.putExtra(Const.MIN_PIC_URL, ServerHelper.getUploadImgURL(post.getOwner_id(), post.getId() + Const.MIN_JPG));
					intent.putExtra(Const.MIN_PIC_FILE_NAME, post.getId() + Const.MIN_JPG);
					startActivity(intent);
				}
			});
		} else {
			imgView.setVisibility(View.GONE);
		}

		// 浏览
		visitView.setText(post.getVisitCount() + "");
		replyView.setText(post.getCommentCount() + "");

	}

	
	
	public void getPostDetailResult(String data) {
		if (!StringUtil.isEmpty(data)) {
			if (data.startsWith("FAIL")) {
				//toast("加载评论数失败");
			} else {
				Post post = (Post) convert(data, Post.class);
				if (post.getId() != null) {
					saveDataString(post_id, data);
					currentPost = post;
					initData();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void getCommentResult(String data, boolean isAppend){
		scrollview.onRefreshComplete();
		if(StringUtil.isEmpty(data)){
			toast("加载评论失败");
			showLoadMoreComment();
		}else{
			List<String> strList = (ArrayList<String>) convert(data, ArrayList.class);

			if(!isAppend){
				index = 0;
			}
			
			if(strList.size()==0){
				showNoComment();
			}else{
				List<Reply> replyList = convert(strList);
				showCommentList(replyList,isAppend);
				if(strList.size()>=COUNT){
					index = index + COUNT;
					showLoadMoreComment();
				}else{
					showNoMoreComment();
				}
			}
		}
	}
	
	
	private void showCommentList(List<Reply> replyList, boolean isAppend){
		//是否是追加
		if(!isAppend){
			commentContentLayout.removeAllViews();
		}
		
		for(Reply reply:replyList){
			View oneComment = initOneComment(reply);
			commentContentLayout.addView(oneComment);
		}
	}
	
	private View initOneComment(final Reply reply){
		View view = createView(R.layout.list_post_comment);
		
		final int isNoname = reply.getIsNoname();
		final String commentUserId = reply.getOwner_id();
		final String commentUserName = reply.getOwner_name();
		final String content = reply.getContent();
		
		//
		ImageView headImg = (ImageView) view.findViewById(R.id.post_comment_user_img);
		TextView userName = (TextView) view.findViewById(R.id.post_comment_user_name);
		TextView userCollege = (TextView) view.findViewById(R.id.post_comment_user_college);
		TextView userTime = (TextView) view.findViewById(R.id.post_comment_time);

		View replyBtn = view.findViewById(R.id.post_comment_reply_btn);
		View quoteLayout = view.findViewById(R.id.post_comment_quote_layout);

		TextView quoterName = (TextView) view.findViewById(R.id.post_comment_quoter_name);
		TextView quoteContent = (TextView) view.findViewById(R.id.post_comment_quote_content);
		TextView commentContent = (TextView) view.findViewById(R.id.post_comment_content);

		// 加载头像
		if (isNoname < 1) {
			headImg.setTag(commentUserId + Const.MIN_JPG);
			new LoadHeadImgTask(this, headImg, reply.getOwner_head_img());
			userName.setText(commentUserName);

			headImg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
				}
			});

		} else {
			userName.setText("匿名用户");
		}

		userCollege.setText(reply.getOwner_college_name());
		userTime.setText(StringUtil.getDateStr(reply.getCreateDate()));

		final String quote = reply.getQuote();
		if (quote == null || quote.equals("")) {
			quoteLayout.setVisibility(View.GONE);
		} else {
			quoterName.setTag(reply.getQuote_id());
			if (quote.startsWith("【")) {
				quoterName.setText(quote.substring(1, quote.indexOf("】")));
				quoteContent.setText(quote.substring(quote.indexOf("】") + 1));
			} else {
				quoterName.setText("匿名用户");
				quoteContent.setText(quote);
			}

		}
		commentContent.setText(content);
		// 增加reply事件
		replyBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String quoteName = "";
				if (isNoname > 0) {
					quoteName = "匿名用户";
				} else {
					quoteName = commentUserName;
				}
				String commentUserContent = "【" + quoteName + "】" + content;
				CommentDialog dialog = new CommentDialog(PostDetailActivity.this, currentPost.getId(), commentUserId, commentUserContent);
				dialog.show();
			}
		});
		return view;
	}
	
	
	
	private List<Reply> convert(List<String> comments){
		List<Reply> replies = new ArrayList<Reply>();
		for(String str:comments){
			Reply reply = (Reply)convert(str, Reply.class);
			replies.add(reply);
		}
		return replies;
	}
	
	
	private void showNoComment(){
		TextView tv = (TextView)findViewById(R.id.post_detail_loadmore_layout).findViewById(R.id.loadmore_tv);
		findViewById(R.id.post_detail_loadmore_layout).findViewById(R.id.loadmore_icon).setVisibility(View.INVISIBLE);
		findViewById(R.id.post_detail_loadmore_layout).setClickable(false);
		tv.setText("快来第一个评论吧！");
	}
	
	private void showNoMoreComment(){
		TextView tv = (TextView)findViewById(R.id.post_detail_loadmore_layout).findViewById(R.id.loadmore_tv);
		findViewById(R.id.post_detail_loadmore_layout).findViewById(R.id.loadmore_icon).setVisibility(View.INVISIBLE);
		findViewById(R.id.post_detail_loadmore_layout).setClickable(false);
		tv.setText("没有更多了");
	}
	
	
	private void showLoadMoreComment(){
		TextView tv = (TextView)findViewById(R.id.post_detail_loadmore_layout).findViewById(R.id.loadmore_tv);
		findViewById(R.id.post_detail_loadmore_layout).findViewById(R.id.loadmore_icon).setVisibility(View.INVISIBLE);
		findViewById(R.id.post_detail_loadmore_layout).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(listCommentTask!=null){
					listCommentTask.stopTask();
				}
				listCommentTask = new ListCommentTask(PostDetailActivity.this, currentPost.getId(), index, COUNT);
				listCommentTask.startTask();
				showLoadingMoreComment();
			}
		});
		findViewById(R.id.post_detail_loadmore_layout).setClickable(true);
		tv.setText("加载更多");
	}
	
	
	private void showLoadingMoreComment(){
		TextView tv = (TextView)findViewById(R.id.post_detail_loadmore_layout).findViewById(R.id.loadmore_tv);
		findViewById(R.id.post_detail_loadmore_layout).findViewById(R.id.loadmore_icon).setVisibility(View.VISIBLE);
		findViewById(R.id.post_detail_loadmore_layout).setClickable(false);
		tv.setText("正在加载评论");
	}

	
	private CommentDialog commentDialog;
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id==R.id.title_bar_left_btn){
			finish();
		}else if(id==R.id.title_bar_right_btn){
			if(!scrollview.isRefreshing()){
				scrollview.setRefreshing();
			}else{
				toast("正在刷新中");
			}
		}else if(id==R.id.post_comment_btn){
			if(commentDialog == null){
				commentDialog = new CommentDialog(this, currentPost.getId(), currentPost.getOwner_id(),"");
			}
			commentDialog.show();
		}			
	}
	
	@Override
	protected void onDestroy() {
		if(listCommentTask!=null){
			listCommentTask.stopTask();
		}
		if(detailTask!=null){
			detailTask.stopTask();
		}
		super.onDestroy();
	}

	@Override
	public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
		if(detailTask!=null){
			detailTask.stopTask();
		}
		// 加载数据
		detailTask = new PostDetailTask(this, currentPost.getId(), currentPost.getOwner_id());
		detailTask.startTask();
				
		if(listCommentTask!=null){
			listCommentTask.stopTask();
		}
		listCommentTask = new ListCommentTask(this, currentPost.getId(), 0, COUNT);
		listCommentTask.startTask();
		
	}

	@Override
	public void commentSuccess() {
		scrollview.setRefreshing();
	}

	@Override
	public BaseActivity getActivity() {
		return this;
	}
}
