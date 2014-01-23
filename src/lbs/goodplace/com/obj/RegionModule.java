package lbs.goodplace.com.obj;

/**
 * 商圈（区）信息
 * (2.3.1.4 )
 * @author Administrator
 *
 */
public class RegionModule {
	private String id;
	private String parentid;	//父商圈ID
	private String name;
	private String lat;		//商圈坐标经度
	private String lng;		//商圈坐标纬度
	private String dsfshopcount = "";
	private String hyfshopcount = "";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getDsfshopcount() {
		return dsfshopcount;
	}
	public void setDsfshopcount(String dsfshopcount) {
		this.dsfshopcount = dsfshopcount;
	}
	public String getHyfshopcount() {
		return hyfshopcount;
	}
	public void setHyfshopcount(String hyfshopcount) {
		this.hyfshopcount = hyfshopcount;
	}
}