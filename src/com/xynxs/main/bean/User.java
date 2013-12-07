package com.xynxs.main.bean;

/**
 * 用户基本信息类
 */
public class User{
	
	/**
	 * ID，这里由第三方提供（QQ，微博等），唯一，不可变
	 */
	private String id;
	
	/**
	 * 昵称
	 */
	private String name;
	
	/**
	 * 大学名称
	 */
	private String college_name;
	
	/**
	 * 性别1：男； 2：女； 0：未填写
	 */
	private int gender;
	
	/**
	 * 城市名称（与大学所在的城市对应，即大学确定了所在的城市）
	 */
	private String city_name;
	
	/**
	 * 用户当前积分（回复，发布消息，或者登录，都可以获得积分，积分可以体现用户的活跃度）
	 */
	private long score;
	
	/**
	 * 用户头像的链接地址
	 */
	private String head_img;
	
	/**
	 * 用户注册时间
	 */
	private long registerDate;
	
	/**
	 * 用户的入学年份（用于识别用户属于哪一届）
	 */
	private int entranceYear;
	
	/**
	 * 用户所处的状态
	 */
	private int state;
	
	/**
	 * 帖子数量
	 */
	private long post_count;
	
	/**
	 * 用户关注的标签
	 */
	private String label;
	
	/**
	 * 用户的个性签名
	 */
	private String sign;
	
	/**
	 * 用户的密码,可为空
	 */
	private String password;
	
	/**
	 * 用户登录的有效日期, 用于检测用户账号是否已过期需要重新登录
	 */
	private long avaibleDate;
	
	/**
	 * 生日,形式为yyyyMMdd
	 */
	private String birthDay;
	
	/**
	 * QQ
	 */
	private String QQ;
	
	/**
	 * email
	 */
	private String email;
	
	/**
	 * 手机号
	 */
	private String tel;
	
	/**
	 * 地址
	 */
	private String address;
	
	/**
	 * 院系
	 */
	private String department;
	
	/**
	 * 真实姓名
	 */
	private String realName;
	
	/**
	 * 身高,单位为厘米
	 */
	private Integer height;
	
	private String media_type;
	private String media_id;
	
	/**
	 * 上次登录时间
	 */
	private long lastLoginTime;
	
	/**
	 * 今天发帖得分，最多得50分
	 */
	private int todayPostScore;
	
	/**
	 * 今天的回复得分，最多得50分
	 */
	private int todayReplyScore;
	
	/**
	 * 余额
	 */
	private long balance;
	
	public long getBalance() {
		return balance;
	}

	public void setBalance(long balance) {
		this.balance = balance;
	}

	public long getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public int getTodayPostScore() {
		return todayPostScore;
	}

	public void setTodayPostScore(int todayPostScore) {
		this.todayPostScore = todayPostScore;
	}

	public int getTodayReplyScore() {
		return todayReplyScore;
	}

	public void setTodayReplyScore(int todayReplyScore) {
		this.todayReplyScore = todayReplyScore;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String qQ) {
		QQ = qQ;
	}

	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public long getAvaibleDate() {
		return avaibleDate;
	}

	public void setAvaibleDate(long avaibleDate) {
		this.avaibleDate = avaibleDate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCollege_name() {
		return college_name;
	}

	public void setCollege_name(String college_name) {
		this.college_name = college_name;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getCity_name() {
		return city_name;
	}

	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}

	public long getScore() {
		return score;
	}

	public void setScore(long score) {
		this.score = score;
	}

	public String getHead_img() {
		return head_img;
	}

	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}

	public long getRegisterDate() {
		return registerDate;
	}

	public String getMedia_type() {
		return media_type;
	}

	public void setMedia_type(String media_type) {
		this.media_type = media_type;
	}

	public String getMedia_id() {
		return media_id;
	}

	public void setMedia_id(String media_id) {
		this.media_id = media_id;
	}

	public void setRegisterDate(long registerDate) {
		this.registerDate = registerDate;
	}

	public int getEntranceYear() {
		return entranceYear;
	}

	public void setEntranceYear(int entranceYear) {
		this.entranceYear = entranceYear;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public long getPost_count() {
		return post_count;
	}

	public void setPost_count(long post_count) {
		this.post_count = post_count;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}
