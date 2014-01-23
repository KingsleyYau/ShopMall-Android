package lbs.goodplace.com.obj;

/**
 * 2.3.2.7 商家的点评信息
 * @author Administrator
 *
 */
public class ReviewModule {
	private String id;
	private String shopid;
	private String shopname;
	private String userid;
	private String usernickname;
	private String userfaceurl;	//用户头像URL
	private long posttime;
	private int score;		//总体评分
	private int score1;		//产品评分
	private int score2;		//氛围评分
	private int score3;		//客服评分
	private int score4;		//其它评分
	private String body;	//点评内容
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getShopname() {
		return shopname;
	}
	public void setShopname(String shopname) {
		this.shopname = shopname;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUsernickname() {
		return usernickname;
	}
	public void setUsernickname(String usernickname) {
		this.usernickname = usernickname;
	}
	public String getUserfaceurl() {
		return userfaceurl;
	}
	public void setUserfaceurl(String userfaceurl) {
		this.userfaceurl = userfaceurl;
	}
	public long getPosttime() {
		return posttime;
	}
	public void setPosttime(long posttime) {
		this.posttime = posttime;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getScore1() {
		return score1;
	}
	public void setScore1(int score1) {
		this.score1 = score1;
	}
	public int getScore2() {
		return score2;
	}
	public void setScore2(int score2) {
		this.score2 = score2;
	}
	public int getScore3() {
		return score3;
	}
	public void setScore3(int score3) {
		this.score3 = score3;
	}
	public int getScore4() {
		return score4;
	}
	public void setScore4(int score4) {
		this.score4 = score4;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
}
