package lbs.goodplace.com.obj;

import java.util.ArrayList;

/**
 * 2.3.1.7 资讯类型信息
 * @author Administrator
 *
 */
public class InfotypeModule {
	private String id;
	private String iconurl;
	private String name;
	private ArrayList<CarrankModule> carrankList = new ArrayList<CarrankModule>();
	
	public ArrayList<CarrankModule> getCarrankList() {
		return carrankList;
	}
	public void setCarrankList(ArrayList<CarrankModule> carrankList) {
		this.carrankList = carrankList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIconurl() {
		return iconurl;
	}
	public void setIconurl(String iconurl) {
		this.iconurl = iconurl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
