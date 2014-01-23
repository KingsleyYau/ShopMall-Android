package lbs.goodplace.com.obj;

/**
 * 城市信息 (2.3.1.3)
 * 
 * @author Administrator
 * 
 */
public class CityModule {
	private int id;
	private String cityareacode;
	private String name;
	private String firstchar; // 城市名称首拼音字母
	private boolean isHot; // 是否热点城市
	private boolean isPromo; // 是否促销城市
	private double lat; // 城市坐标经度
	private double lng; // 城市坐标纬度
	private String region; // 地区类型（字符串类型，直辖市、华东地区、华南地区、华北地区、西南地区、西北地区、东北地区）
	private boolean topcity; // 是否顶级城市（整形，0为否，1为是）

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityareacode() {
		return cityareacode;
	}

	public void setCityareacode(String cityareacode) {
		this.cityareacode = cityareacode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstchar() {
		return firstchar;
	}

	public void setFirstchar(String firstchar) {
		this.firstchar = firstchar;
	}

	public boolean isHot() {
		return isHot;
	}

	public void setHot(boolean isHot) {
		this.isHot = isHot;
	}

	public boolean isPromo() {
		return isPromo;
	}

	public void setPromo(boolean isPromo) {
		this.isPromo = isPromo;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public boolean getTopcity() {
		return topcity;
	}

	public void setTopcity(boolean topcity) {
		this.topcity = topcity;
	}

}