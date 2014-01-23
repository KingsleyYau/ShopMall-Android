package lbs.goodplace.com.obj;

/**
 * 2.3.7.1 我的信息
 * @author Administrator
 *
 */
public class UserInfoModule {
	private String id;
	private String nickname;
	private String avatarurl;	//用户头像URL
	private int spscore ;	//导购平台积分
	private int otherscore ;	//第三方会员积分(整形，不同订制客户端对应不同第三方，如电信、联通、银行)
	private String level;	//等级
	private int checkincount;	//签到次数
	private int reviewcount;	//点评次数
	private int photocount;		//上传图片数量
	private int favoshopcount;	//收藏商家数量
	private int favoshopinfocount;	//所收藏商家新发未读资讯数量
	private int myinfocount;		//未使用的有效卷卷数量
	
	public int getMyinfocount() {
		return myinfocount;
	}
	public void setMyinfocount(int myinfocount) {
		this.myinfocount = myinfocount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAvatarurl() {
		return avatarurl;
	}
	public void setAvatarurl(String avatarurl) {
		this.avatarurl = avatarurl;
	}
	public int getSpscore() {
		return spscore;
	}
	public void setSpscore(int spscore) {
		this.spscore = spscore;
	}
	public int getOtherscore() {
		return otherscore;
	}
	public void setOtherscore(int otherscore) {
		this.otherscore = otherscore;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public int getCheckincount() {
		return checkincount;
	}
	public void setCheckincount(int checkincount) {
		this.checkincount = checkincount;
	}
	public int getReviewcount() {
		return reviewcount;
	}
	public void setReviewcount(int reviewcount) {
		this.reviewcount = reviewcount;
	}
	public int getPhotocount() {
		return photocount;
	}
	public void setPhotocount(int photocount) {
		this.photocount = photocount;
	}
	public int getFavoshopcount() {
		return favoshopcount;
	}
	public void setFavoshopcount(int favoshopcount) {
		this.favoshopcount = favoshopcount;
	}
	public int getFavoshopinfocount() {
		return favoshopinfocount;
	}
	public void setFavoshopinfocount(int favoshopinfocount) {
		this.favoshopinfocount = favoshopinfocount;
	}

}
