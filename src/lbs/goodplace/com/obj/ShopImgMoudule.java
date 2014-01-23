package lbs.goodplace.com.obj;

/**
 * 2.3.2.6 商家图片信息
 * @author Administrator
 *
 */
public class ShopImgMoudule {
	private String id;
	private String name;
	private String type;
	private String thumburl;
	private String fullurl;
	private String uploaduser;
	private long uploadtime;
	private int star;	//总体评价
	private String tag;
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getThumburl() {
		return thumburl;
	}
	public void setThumburl(String thumburl) {
		this.thumburl = thumburl;
	}
	public String getFullurl() {
		return fullurl;
	}
	public void setFullurl(String fullurl) {
		this.fullurl = fullurl;
	}
	public String getUploaduser() {
		return uploaduser;
	}
	public void setUploaduser(String uploaduser) {
		this.uploaduser = uploaduser;
	}
	public long getUploadtime() {
		return uploadtime;
	}
	public void setUploadtime(long uploadtime) {
		this.uploadtime = uploadtime;
	}
	public int getStar() {
		return star;
	}
	public void setStar(int star) {
		this.star = star;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	
}
