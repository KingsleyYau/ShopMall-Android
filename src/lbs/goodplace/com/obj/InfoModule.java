package lbs.goodplace.com.obj;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * 2.3.2.10 商家的资讯列表信息
 * 2.3.3.2 同城最热排行资讯
 * 2.3.3.3 资讯详细信息
 * @author Administrator
 *
 */
public class InfoModule {
	private String id;
	private String typeid;
	private String typename = "";
	private long begindate;	//资讯开始公布时间（自1970年1月1日起的秒数）
	private long enddate;	//资讯截至公布时间（自1970年1月1日起的秒数）
	private long infobegindate;	//资讯生效有效开始时间（自1970年1月1日起的秒数）
	private long infoenddate;	//资讯有效截至时间（自1970年1月1日起的秒数）
	private String title = "";		//资讯标题
	public String defaultpicurl;//thumb图片URL
	private String shopid = "";		//商家ID
	private String shopname = "";	//商家名称
	private String desc = "";	//资讯内容描述（字符串,HTML格式）
	private String photo = "";	//资讯内容图片URL
	private String cityid = "";	//城市ID
	private String regionid = "";//地区ID
	private String regionname = "";//地区名称
	public ArrayList<InfoModule> mBranchlist = new ArrayList<InfoModule>();
	private boolean hassms;	//是否需短信验证码申请
	private String smsinfo = "";	//短信基础内容
	private String tips = "";	//资讯获取方式说明
	private String showtips = "";	//资讯使用说明
	private String gettype = "";	//资讯获取类型ID
	private String showtype = "";//资讯使用类型ID
	private String buytips = "";	//对用户能否获取资讯的说明
	private long canceltime = 0;	//过期或无效取消时间
	private long gettime = 0;		//获取时间
	private int status = 1;			//使用状态（1待用;2为已用;3:取消或无效）
	private long usetime = 0;
	private String barimgurl = "";	//2维码图片 
	private String barid = "";
	private String myinfoid = "";
	private String vericode = "";	//条形码
	private String memberid = "";
	private String infophoto = "";
	private String getprice = "";
	
	public String getBarimgurl() {
		return barimgurl;
	}
	public void setBarimgurl(String barimgurl) {
		this.barimgurl = barimgurl;
	}
	public String getGetprice() {
		return getprice;
	}
	public void setGetprice(String getprice) {
		this.getprice = getprice;
	}
	public String getInfophoto() {
		return infophoto;
	}
	public void setInfophoto(String infophoto) {
		this.infophoto = infophoto;
	}
	public String getMemberid() {
		return memberid;
	}
	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}
	public ArrayList<InfoModule> getmBranchlist() {
		return mBranchlist;
	}
	public void setmBranchlist(ArrayList<InfoModule> mBranchlist) {
		this.mBranchlist = mBranchlist;
	}
	public long getCanceltime() {
		return canceltime;
	}
	public void setCanceltime(long canceltime) {
		this.canceltime = canceltime;
	}
	public long getGettime() {
		return gettime;
	}
	public void setGettime(long gettime) {
		this.gettime = gettime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getUsetime() {
		return usetime;
	}
	public void setUsetime(long usetime) {
		this.usetime = usetime;
	}
	public String getBarid() {
		return barid;
	}
	public void setBarid(String barid) {
		this.barid = barid;
	}
	public String getMyinfoid() {
		return myinfoid;
	}
	public void setMyinfoid(String myinfoid) {
		this.myinfoid = myinfoid;
	}
	public String getVericode() {
		return vericode;
	}
	public void setVericode(String vericode) {
		this.vericode = vericode;
	}
	public String getBuytips() {
		return buytips;
	}
	public void setBuytips(String buytips) {
		this.buytips = buytips;
	}
	public String getShowtips() {
		return showtips;
	}
	public void setShowtips(String showtips) {
		this.showtips = showtips;
	}
	public String getGettype() {
		return gettype;
	}
	public void setGettype(String gettype) {
		this.gettype = gettype;
	}
	public String getShowtype() {
		return showtype;
	}
	public void setShowtype(String showtype) {
		this.showtype = showtype;
	}
	
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getCityid() {
		return cityid;
	}
	public void setCityid(String cityid) {
		this.cityid = cityid;
	}
	public String getRegionid() {
		return regionid;
	}
	public void setRegionid(String regionid) {
		this.regionid = regionid;
	}
	public String getRegionname() {
		return regionname;
	}
	public void setRegionname(String regionname) {
		this.regionname = regionname;
	}
	public boolean isHassms() {
		return hassms;
	}
	public void setHassms(boolean hassms) {
		this.hassms = hassms;
	}
	public String getSmsinfo() {
		return smsinfo;
	}
	public void setSmsinfo(String smsinfo) {
		this.smsinfo = smsinfo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTypeid() {
		return typeid;
	}
	public void setTypeid(String typeid) {
		this.typeid = typeid;
	}
	public String getTypename() {
		return typename;
	}
	public void setTypename(String typename) {
		this.typename = typename;
	}
	public long getBegindate() {
		return begindate;
	}
	public void setBegindate(long begindate) {
		this.begindate = begindate;
	}
	public long getEnddate() {
		return enddate;
	}
	public void setEnddate(long enddate) {
		this.enddate = enddate;
	}
	public long getInfobegindate() {
		return infobegindate;
	}
	public void setInfobegindate(long infobegindate) {
		this.infobegindate = infobegindate;
	}
	public long getInfoenddate() {
		return infoenddate;
	}
	public void setInfoenddate(long infoenddate) {
		this.infoenddate = infoenddate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDefaultpicurl() {
		return defaultpicurl;
	}
	public void setDefaultpicurl(String defaultpicurl) {
		this.defaultpicurl = defaultpicurl;
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
	
	
	public boolean result = true;
	public int errorcode;	//错误代码
	
	public boolean getResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public int getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}
}
