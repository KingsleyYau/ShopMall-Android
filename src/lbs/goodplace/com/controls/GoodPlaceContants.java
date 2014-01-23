package lbs.goodplace.com.controls;

import lbs.goodplace.com.obj.SessionModule;
import lbs.goodplace.com.obj.UserInfoModule;

/**
 * 
 * @author zhaojunjie
 * (从[中心服务器]取得网关地址[就是接口的服务器地址])
 */
public class GoodPlaceContants {
	public static final String URL_IP = "210.22.12.21:13080/shoppinglbs/gw"; // 中心服务器IP地址
	public static String URL_WGIP = "";	//网关地址
	public static final String URL_SEVERNAME = "/shoppinglbs/gw";	//服务名
	public static String LBSAPPID = ""; // 导购平台id
	public static String SESSION_KEY = "";//"KH3hmY6q5JVuT2lBLKitwjtBYiClgYR3"; // 2.3.1.11手机签到后产生会话key
	public static String CITY_ID = "1005"; // 2.3.1.11手机签到后产生会话key
	public static String DEVICETOKEN = "0000000";	//手机唯一标识
	public static double HXLNG = 0.0;			//定位所得当前城市经纬度(存放火星系的坐标值)(高德地图定位是火星坐标)
	public static double HXLAT = 0.0;
	public static double LNG = 0.0;				//定位所得当前城市经纬度(存放Google地图的坐标值)
	public static double LAT = 0.0;
	public static SessionModule SESSEIONMOUDLE;
	public static UserInfoModule USERINFO;		//登录成功后赋值
	public static String LOCACITYNAME = "";		//定位所得当前城市名
//	public static boolean ISYOURCITY = true;	//用户选择的城市,是否他当前所在的城市
	
	/**
	 * 2.3.1.1 获取接口服务器IP或域名接口
	 */
	public static final String URL_APPWG = "http://" + URL_IP + "/getapp.gw";
	
	
	//
	public static final String URL_RANKING_LIST = "http://210.22.12.21:13080/shoppinglbs/gw/allcatranks.gw?lbsappid=1";

	// 2.3.18商家排行榜类型
	public static String URL_SHOP_RANKING_TYEP = "";							//完整URL
	private static final String SHOP_RANKING_TYEP = "/allcatranks.gw?lbsappid=";			//对应接口

	// 2.3.2.4 由排行榜查询商家列表
	public static String URL_SHOP_LIST_OF_RANKING = "";	
	public static String URL_SHOP_LIST_OF_RANKING_SERVICE = "/searchshopslist.gw?";	
	
	//2.3.2.5 商家具体信息查询
	public static String URL_SHOP_INFO_OF_ID = "";	
	public static String URL_SHOP_INFO_OF_ID_SERVICE = "/shopinfo.gw?";	
	
	//2.3.2.6 商家图片信息查询
	public static String URL_SHOP_IMAGE_LIST = "";	
	public static String URL_SHOP_IMAGE_LIST_SERVICE = "/getshopimages.gw?";	
	
	//2.3.2.7 商家的点评信息查询
	public static String URL_SHOP_COMMENT = "";
	private static String URL_SHOP_COMMENT_SERVICE = "/getshopreviews.gw?";
	
	//2.3.2.8 商家的签到留言信息查询
	public static String URL_SHOP_SIGN_WALL = "";	
	public static String URL_SHOP_SIGN_WALL_SERVICE = "/getshopsignins.gw?";	
	
	
	//2.3.5.1 商家签到留言提交（需用户成功登录为前提）
	public static String URL_USER_SIGN = "";	
	public static String URL_USER_SIGN_SERVICE = "/postsignin.gw?";	
	
	//2.3.5.2 商家点评提交（需用户成功登录为前提）
	public static String URL_USER_COMMONT = "";	
	public static String URL_USER_COMMONT_SERVICE = "/postshopreview.gw?";	
	
	//2.3.5.3 商家产品查询
	public static String URL_SHOP_PRODUCT_LIST = "";	
	public static String URL_SHOP_PRODUCT_LIST_SERVICE = "/getshopproducts.gw?";	
	
	//2.3.5.4 商家拍照上传处理（需用户成功登录为前提）
	public static String URL_SEND_IMAGE = "";	
	public static String URL_SEND_IMAGE_SERVICE = "/postshopimg.gw?";	
	
	
	//2.3.5.5 收藏商家处理（需用户登录）
	public static String URL_SHOP_FAV = "";	
	public static String URL_SHOP_FAV_SERVICE = "/postshopfavorite.gw?";	
	
	
	// 商家排行榜类型
	public static String URL_SHOP_LIST = "";	
	private static String SHOP_LIST = "/allCatRanks?lbsappid=";

	//
	public static String URL_SHOP_IONFO = "";
	private static final String SHOP_IONFO = "shop_info";
	
	//城市列表
	public static String URL_CITY_LIST = "";
	private static final String CITY_LIST = "/allcity.gw?lbsappid=";
	
	//积分类型
	public static String URL_CREDITTYPES_LIST = "";
	private static final String CREDITTYPES_LIST = "/allcredittypes.gw?";
	
	//签到
	public static String URL_SEESION = "";
	private static final String SEESION_SERVICE = "/mobilesignin.gw?";
	
	//注册验证码
	public static String URL_MOBILECODE = "";
	private static final String MOBILECODE_SERVICE = "/getmobilecode.gw?";
	
	//注册（电话）
	public static String URL_REGISTEPHONE = "";
	private static final String REGISTEPHONE_SERVICE = "/regmemberbymobile.gw?";
	
	//注册（邮箱）
	public static String URL_REGISTEMAIL = "";
	private static final String REGISTEMAIL_SERVICE = "/regmemberbyemail.gw?";
	
	//修改密码
	public static String URL_CHANGEPW = "";
	private static final String CHANGEPW_SERVICE = "/changepwd.gw?";
	
	//登录
	public static String URL_LOGIN = "";
	private static final String LOGIN_SERVICE = "/login.gw?";
	
	//注销
	public static String URL_LOGOUT = "";
	private static final String LOGOUT_SERVICE = "/logout.gw?";
	
	//上传头像
	public static String URL_UPLOADUSERIMG = "";
	private static final String UPLOADUSERIMG_SERVICE = "/uploadmemberimg.gw?";
	
	//资讯类型
	public static String URL_INFOTYPE = "";
	private static final String INFOTYPE_SERVICE = "/allinfotypes.gw?";
	
	//行业分类类型
	public static String URL_CATEGORIES = "";
	private static final String CATEGORIES_SERVICE = "/allcategories.gw?";
	
	//资讯列表
	public static String URL_ALLINFO = "";
	private static final String ALLINFO_SERVICE = "/searchinfolist.gw?";
	
	//收藏商店列表
	public static String URL_MYFAV = "";
	private static final String MYFAV_SERVICE = "/getmyshops.gw?";
	
	//排序方式列表
	public static String URL_SHOPSORTTYPES = "";
	private static final String SHOPSORTTYPES_SERVICE = "/allshopsorttypes.gw?";
	
	//查询某城市的商家列表
	public static String URL_SEARCHSHOPLIST = "";
	private static final String SEARCHSHOPLIST_SERVICE = "/searchshopslist.gw?";
	
	//城市商圈
	public static String URL_REGIONS = "";
	private static final String REGIONS_SERVICE = "/regions.gw?";
	
	//我的签到留言信息查询
	public static String URL_MY_SIGN_WALL = "";	
	private static String URL_MY_SIGN_WALL_SERVICE = "/getmysignins.gw?";	
	
	//我的点评信息查询
	public static String URL_MY_COMMENT = "";	
	private static String URL_MY_COMMENT_SERVICE = "/getmyreviews.gw?";	
	
	//我的图片信息查询
	public static String URL_MY_IMAGE_LIST = "";	
	private static String URL_MY_IMAGE_LIST_SERVICE = "/getmyimages.gw?";	
	
	//我的图片信息查询
	public static String URL_MY_INFO_LIST = "";	
	private static String URL_MY_INFO_LIST_SERVICE = "/getmyinfolist.gw?";	
	
	//用户信息查询
	public static String URL_USERINFO = "";	
	private static String URL_USERINFO_SERVICE = "/getmyaccinfo.gw?";	
	
	//资讯详细
	public static String URL_INFODETAIL = "";
	private static final String INFODETAIL_SERVICE = "/infodetail.gw?";
	
	//2.3.7.7 我的卷卷详细
	public static String URL_MYINFODETAIL = "";
	private static final String MYINFODETAIL_SERVICE = "/myinfodetail.gw?";
	
	//2.3.6.4 资讯兑换或购买预判
	public static String URL_INFOPREGET = "";
	private static final String INFOPREGET_SERVICE = "/infopreget.gw?";
	
	//2.3.6.5 资讯兑换或购买接口
	public static String URL_BUYINFO = "";
	private static final String BUYINFO_SERVICE = "/buymyinfo.gw?";
	
	//资讯SMS
	public static String URL_INFOSMS = "";
	private static final String INFOSMS_SERVICE = "/getinfosms.gw?";
	
	//资讯图片下载地址
	public static String URL_INFOPIC = "";
	private static final String INFOPIC_SERVICE = "/getinfoimg.gw?";
	
	//切换城市
	public static String URL_CHANGECITY = "";
	private static final String CHANGECITY_SERVICE = "/changecity.gw?";
	
	//取消收藏
	public static String URL_CANCELMYFAV = "";
	private static final String CANCELMYFAV_SERVICE = "/cancelshopfavorite.gw?";
	
	//取消收藏
	public static String URL_PROBLEM = "";
	private static final String PROBLEM_SERVICE = "/submitproblem.gw?";

	//首页附近分类
	public static String URL_NEARSHOPCOUNTINFO = "";
	private static final String NEARSHOPCOUNTINFO_SERVICE = "/nearshopcoutinfo.gw?";
	
	//是否已经初始化URL
	private static boolean isiniturl = false;
	/**
	 * 设置网关地址和APPID
	 * 对接口完整RUL进行赋值
	 * @param ip
	 * @param lbsappid
	 */
	public static void setWGIP(String ip ,String lbsappid){
		if(!isiniturl){
			URL_WGIP = ip;
			LBSAPPID = lbsappid;
			
			String urlstrtemp = "http://" + URL_WGIP + URL_SEVERNAME;
			
			//赋值到完整URL中
			URL_SHOP_RANKING_TYEP = urlstrtemp + SHOP_RANKING_TYEP + LBSAPPID;
			URL_SHOP_LIST = urlstrtemp + SHOP_LIST + LBSAPPID;
			URL_SHOP_IONFO = urlstrtemp + SHOP_IONFO;
			URL_CITY_LIST = urlstrtemp + CITY_LIST + LBSAPPID;
			URL_CREDITTYPES_LIST = urlstrtemp + CREDITTYPES_LIST ;
//			URL_SHOP_RANKING_TYEP = urlstrtemp + SHOP_RANKING_TYEP;
			
			URL_SEESION = urlstrtemp + SEESION_SERVICE;
			URL_MOBILECODE = urlstrtemp + MOBILECODE_SERVICE;
			URL_REGISTEPHONE = urlstrtemp + REGISTEPHONE_SERVICE;
			URL_LOGIN = urlstrtemp + LOGIN_SERVICE;
			URL_LOGOUT = urlstrtemp + LOGOUT_SERVICE;
			URL_UPLOADUSERIMG = urlstrtemp + UPLOADUSERIMG_SERVICE;
			
			URL_SHOP_LIST_OF_RANKING = urlstrtemp + URL_SHOP_LIST_OF_RANKING_SERVICE;
			URL_SHOP_INFO_OF_ID = urlstrtemp + URL_SHOP_INFO_OF_ID_SERVICE;
			URL_SHOP_COMMENT = urlstrtemp + URL_SHOP_COMMENT_SERVICE;	
			URL_SHOP_SIGN_WALL = urlstrtemp + URL_SHOP_SIGN_WALL_SERVICE;
			URL_SHOP_IMAGE_LIST = urlstrtemp + URL_SHOP_IMAGE_LIST_SERVICE;
			URL_USER_SIGN = urlstrtemp + URL_USER_SIGN_SERVICE;
			URL_USER_COMMONT = urlstrtemp + URL_USER_COMMONT_SERVICE;
			URL_SHOP_FAV = urlstrtemp + URL_SHOP_FAV_SERVICE;
			URL_SEND_IMAGE = urlstrtemp + URL_SEND_IMAGE_SERVICE;
			URL_SHOP_PRODUCT_LIST = urlstrtemp + URL_SHOP_PRODUCT_LIST_SERVICE;
			
			URL_INFOTYPE = urlstrtemp + INFOTYPE_SERVICE;	
			URL_CATEGORIES = urlstrtemp + CATEGORIES_SERVICE;	
			URL_ALLINFO = urlstrtemp + ALLINFO_SERVICE;
			URL_MYFAV = urlstrtemp + MYFAV_SERVICE;
			URL_SHOPSORTTYPES = urlstrtemp + SHOPSORTTYPES_SERVICE;
			URL_SEARCHSHOPLIST = urlstrtemp + SEARCHSHOPLIST_SERVICE;
			URL_REGIONS = urlstrtemp + REGIONS_SERVICE;
			URL_MY_SIGN_WALL = urlstrtemp + URL_MY_SIGN_WALL_SERVICE;
			URL_MY_COMMENT = urlstrtemp + URL_MY_COMMENT_SERVICE;
			URL_MY_IMAGE_LIST = urlstrtemp + URL_MY_IMAGE_LIST_SERVICE;
			URL_MY_INFO_LIST = urlstrtemp + URL_MY_INFO_LIST_SERVICE;
			URL_USERINFO = urlstrtemp + URL_USERINFO_SERVICE;
			URL_MYINFODETAIL = urlstrtemp + MYINFODETAIL_SERVICE;
			URL_CHANGEPW = urlstrtemp + CHANGEPW_SERVICE;
			URL_INFODETAIL = urlstrtemp + INFODETAIL_SERVICE;
			URL_INFOPREGET = urlstrtemp + INFOPREGET_SERVICE;
			URL_BUYINFO = urlstrtemp + BUYINFO_SERVICE;
			URL_CHANGECITY = urlstrtemp + CHANGECITY_SERVICE;
			URL_CANCELMYFAV = urlstrtemp + CANCELMYFAV_SERVICE;
			URL_INFOSMS = urlstrtemp + INFOSMS_SERVICE;
			URL_INFOPIC = urlstrtemp + INFOPIC_SERVICE;
			URL_PROBLEM = urlstrtemp + PROBLEM_SERVICE;
			URL_NEARSHOPCOUNTINFO = urlstrtemp + NEARSHOPCOUNTINFO_SERVICE;
			URL_REGISTEMAIL = urlstrtemp + REGISTEMAIL_SERVICE;
		}
		isiniturl = true;
	}
	
	//列表跳转到商家详细列表参数字段
	public static  final String KEY_SHOP_ID = "shop_id";
	//商店所在城市ID
	public static  final String KEY_SHOP_CITYID = "shop_cityid";
	
}
