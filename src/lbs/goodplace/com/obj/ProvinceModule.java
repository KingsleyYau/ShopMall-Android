package lbs.goodplace.com.obj;

import java.util.ArrayList;
import java.util.List;

/**
 * 省份信息
 * @author zhaojunjie
 *
 */
public class ProvinceModule {
	private String name;
	private List<CityModule> cityList = new ArrayList<CityModule>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<CityModule> getCityList() {
		return cityList;
	}
	public void setCityList(List<CityModule> cityList) {
		this.cityList = cityList;
	}
}
