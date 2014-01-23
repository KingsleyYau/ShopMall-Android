package lbs.goodplace.com.obj;

/**
 * 2.3.2.8 商家的签到留言信息
 * 2.3.2.9 商家的网友推荐信息
 * @author Administrator
 *
 */
public class SigninModule {
	private String id;
	private String shopid;
	private String shopname;
	private String shopbranchname;
	private String userid;
	private String usernickname;
	private String userface;
	private long signtime;
	private long lat;
	private long lng;
	private int score;
	private String body;
	private String attachedimgurl;
	private String recommendtype;	//推荐类型，包括“产品、氛围、其它”三种（字符串）
	public String getRecommendtype() {
		return recommendtype;
	}
	public void setRecommendtype(String recommendtype) {
		this.recommendtype = recommendtype;
	}
	public String getRecommendtitle() {
		return recommendtitle;
	}
	public void setRecommendtitle(String recommendtitle) {
		this.recommendtitle = recommendtitle;
	}
	private String recommendtitle;	//推荐内容
	
	
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
	public String getShopbranchname() {
		return shopbranchname;
	}
	public void setShopbranchname(String shopbranchname) {
		this.shopbranchname = shopbranchname;
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
	public String getUserface() {
		return userface;
	}
	public void setUserface(String userface) {
		this.userface = userface;
	}
	public long getSigntime() {
		return signtime;
	}
	public void setSigntime(long signtime) {
		this.signtime = signtime;
	}
	public long getLat() {
		return lat;
	}
	public void setLat(long lat) {
		this.lat = lat;
	}
	public long getLng() {
		return lng;
	}
	public void setLng(long lng) {
		this.lng = lng;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getAttachedimgurl() {
		return attachedimgurl;
	}
	public void setAttachedimgurl(String attachedimgurl) {
		this.attachedimgurl = attachedimgurl;
	}
	
}
