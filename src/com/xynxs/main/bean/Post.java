package com.xynxs.main.bean;

/**
 * 微型帖子
 */
public class Post{

	/**
	 * 编号，由系统生成
	 */
	private String id;
	
	/**
	 * 版块名称
	 */
	private String topic;
	
	/**
	 * 内容
	 */
	private String content;
	
	/**
	 * 是否是匿名发布
	 */
	private int isNoname;
	
	/**
	 * 此条信息，是否包含图片
	 */
	private int hasImg;
	
	/**
	 * 帖子的发布日期
	 */
	private long createDate;
	
	/**
	 * 帖子的阅读人数
	 */
	private long visitCount;
	
	/**
	 * 帖子的评论人数
	 */
	private long commentCount;
	
	/**
	 * 帖子的可视范围
	 */
	private int scope;
	
	public static final int SCOPE_ALL = 0;
	public static final int SCOPE_CITY = 1;
	public static final int SCOPE_COLLEGE = 2;
	
	/**
	 * 帖子的状态
	 */
	private int state;
	
	public static final int STATE_NORMAL = 0;
	public static final int STATE_WRONG = 1;
	
	/**
	 * 发布者的ID
	 */
	private String owner_id;
	
	/**
	 * 以下是查询时的附加属性
	 */
	private Integer owner_gender;
	private String owner_college_name;
	private String owner_city_name;
	private String owner_head_img;
	private String owner_name;
	private Integer is_owner_fav;
	private Long owner_score;
	
	public Long getOwner_score() {
		return owner_score;
	}

	public void setOwner_score(Long owner_score) {
		this.owner_score = owner_score;
	}

	public Integer getIs_owner_fav() {
		return is_owner_fav;
	}

	public void setIs_owner_fav(Integer is_owner_fav) {
		this.is_owner_fav = is_owner_fav;
	}
	
	public String getOwner_name() {
		return owner_name;
	}

	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}
	
	public Integer getOwner_gender() {
		return owner_gender;
	}

	public void setOwner_gender(Integer owner_gender) {
		this.owner_gender = owner_gender;
	}

	public String getOwner_college_name() {
		return owner_college_name;
	}

	public void setOwner_college_name(String owner_college_name) {
		this.owner_college_name = owner_college_name;
	}

	public String getOwner_city_name() {
		return owner_city_name;
	}

	public void setOwner_city_name(String owner_city_name) {
		this.owner_city_name = owner_city_name;
	}

	public String getOwner_head_img() {
		return owner_head_img;
	}

	public void setOwner_head_img(String owner_head_img) {
		this.owner_head_img = owner_head_img;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getIsNoname() {
		return isNoname;
	}

	public void setIsNoname(int isNoname) {
		this.isNoname = isNoname;
	}

	public int getHasImg() {
		return hasImg;
	}

	public void setHasImg(int hasImg) {
		this.hasImg = hasImg;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public long getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(long visitCount) {
		this.visitCount = visitCount;
	}

	public long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(long commentCount) {
		this.commentCount = commentCount;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}
	
	public int getScope() {
		return scope;
	}

	public void setScope(int scope) {
		this.scope = scope;
	}
}
