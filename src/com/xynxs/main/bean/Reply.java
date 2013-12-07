package com.xynxs.main.bean;

/**
 * 帖子回复
 */
public class Reply {

	/**
	 * 编号
	 */
	private String id;

	/**
	 * 回复者
	 */
	private String owner_id;

	/**
	 * 引用他人的回复
	 */
	private String quote;

	/**
	 * 他人的ID
	 */
	private String quote_id;

	/**
	 * 自己回复的内容
	 */
	private String content;

	/**
	 * 回复对应的帖子
	 */
	private String post_id;

	/**
	 * 创建日期
	 */
	private long createDate;

	/**
	 * 是否是匿名
	 */
	private int isNoname;

	/**
	 * 引用者是否已读
	 */
	private int isQuoterRead;
	

	
	
	
	public int getIsQuoterRead() {
		return isQuoterRead;
	}

	public void setIsQuoterRead(int isQuoterRead) {
		this.isQuoterRead = isQuoterRead;
	}

	/**
	 * 以下非表属性
	 */
	private String owner_name;
	private String owner_college_name;
	private String owner_head_img;
	private Long owner_score;
	
	private String post_topic;
	private String post_content;
	private Integer post_hasImg;
	private String post_Owner_id;
	private Integer post_Owner_gender;
	private Integer owner_gender;
	
	public Integer getOwner_gender() {
		return owner_gender;
	}
	public Long getOwner_score() {
		return owner_score;
	}

	public void setOwner_score(Long owner_score) {
		this.owner_score = owner_score;
	}

	public void setOwner_gender(Integer owner_gender) {
		this.owner_gender = owner_gender;
	}
	
	public Integer getPost_Owner_gender() {
		return post_Owner_gender;
	}

	public void setPost_Owner_gender(Integer post_Owner_gender) {
		this.post_Owner_gender = post_Owner_gender;
	}
	
	public String getPost_topic() {
		return post_topic;
	}

	public void setPost_topic(String post_topic) {
		this.post_topic = post_topic;
	}

	public String getPost_content() {
		return post_content;
	}

	public void setPost_content(String post_content) {
		this.post_content = post_content;
	}

	public Integer getPost_hasImg() {
		return post_hasImg;
	}

	public void setPost_hasImg(Integer post_hasImg) {
		this.post_hasImg = post_hasImg;
	}

	public String getPost_Owner_id() {
		return post_Owner_id;
	}

	public void setPost_Owner_id(String post_Owner_id) {
		this.post_Owner_id = post_Owner_id;
	}

	public String getOwner_head_img() {
		return owner_head_img;
	}

	public void setOwner_head_img(String owner_head_img) {
		this.owner_head_img = owner_head_img;
	}

	public String getOwner_name() {
		return owner_name;
	}

	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}

	public String getOwner_college_name() {
		return owner_college_name;
	}

	public void setOwner_college_name(String owner_college_name) {
		this.owner_college_name = owner_college_name;
	}
	
	public int getIsNoname() {
		return isNoname;
	}

	public void setIsNoname(int isNoname) {
		this.isNoname = isNoname;
	}

	/**
	 * 回复的状态
	 */
	private int state;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(String owner_id) {
		this.owner_id = owner_id;
	}

	public String getQuote() {
		return quote;
	}

	public void setQuote(String quote) {
		this.quote = quote;
	}

	public String getQuote_id() {
		return quote_id;
	}

	public void setQuote_id(String quote_id) {
		this.quote_id = quote_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}
}
