package lbs.goodplace.com.obj;

import java.util.ArrayList;

/**
 * 2.3.1.5 行业分类信息
 * @author Administrator
 *
 */
public class CategoryModule {
	private String id = "";
	private String faviconurl = "";
	private String name = "";
	private String parentid;
	private String dsfshopcount = "";
	private String hyfshopcount = "";
	private ArrayList<CarrankModule> carrankList = new ArrayList<CarrankModule>();
	private ArrayList<RegionModule> regionList = new ArrayList<RegionModule>();
	
	/**
	 * 商家分布情况列表
	 * @return
	 */
	public ArrayList<RegionModule> getRegionList() {
		return regionList;
	}
	public void setRegionList(ArrayList<RegionModule> regionList) {
		this.regionList = regionList;
	}
	/**
	 * 商家排行榜列表
	 * @return
	 */
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
	public String getFaviconurl() {
		return faviconurl;
	}
	public void setFaviconurl(String faviconurl) {
		this.faviconurl = faviconurl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
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
