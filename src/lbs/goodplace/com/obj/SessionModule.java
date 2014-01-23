package lbs.goodplace.com.obj;

/**
 * 2.3.1.10 手机签到会话
 * @author Administrator
 *
 */
public class SessionModule {
	private String lbsappid;
	private String devicetoken;
	private float lat;
	private float lng;
	//返回
	private String sessionkey;
	private CityModule city;
	
	public String getSessionkey() {
		return sessionkey;
	}
	public void setSessionkey(String sessionkey) {
		this.sessionkey = sessionkey;
	}
	public CityModule getCity() {
		return city;
	}
	public void setCity(CityModule city) {
		this.city = city;
	}
	
	public String getLbsappid() {
		return lbsappid;
	}
	public void setLbsappid(String lbsappid) {
		this.lbsappid = lbsappid;
	}
	public String getDevicetoken() {
		return devicetoken;
	}
	public void setDevicetoken(String devicetoken) {
		this.devicetoken = devicetoken;
	}
	public float getLat() {
		return lat;
	}
	public void setLat(float lat) {
		this.lat = lat;
	}
	public float getLng() {
		return lng;
	}
	public void setLng(float lng) {
		this.lng = lng;
	}
}
