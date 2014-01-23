package lbs.goodplace.com.manage.requestmanage;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lbs.goodplace.com.View.main.CredittypeActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.util.Encryption;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * 韩国版本接口请求json管理类
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  licanhui
 * @date  [2013-3-5]
 */
public class JsonRequestManage {

	/**
	 * <br>功能简述:通过排行榜查询商家
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param catrankid
	 * @param curpage
	 * @param pagemaxrow
	 * @return
	 */
	public static byte[] getShopListOfRankPostData(int catrankid,int curpage, int pagemaxrow) {
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("cityid", GoodPlaceContants.CITY_ID);
			map.put("catrankid ", catrankid);
			map.put("curpage", curpage);
			map.put("pagemaxrow", pagemaxrow);
			
			Log.i("lch", "请求参数:" + map.toString());
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	

	/**
	 * <br>功能简述:通过排行榜查询商家
	 * <br>功能详细描述:
	 * <br>注意:
	 * @param catrankid
	 * @param curpage
	 * @param pagemaxrow
	 * @return
	 */
	public static byte[] getShopInfoOfIdPostData(int shopid,int shopCityId) {
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			if(shopCityId != -1)
				map.put("cityid", shopCityId);
			else
				map.put("cityid", GoodPlaceContants.CITY_ID);
			map.put("shopid", shopid);
			Log.i("lch", "请求参数:" + map.toString());
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 收藏商家
	 * @param shopid
	 * @param userid
	 * @return
	 */
	public static byte[] getShopFavPostData(int shopid, String userid) {
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("cityid", GoodPlaceContants.CITY_ID);
			map.put("shopid", shopid);
			map.put("userid", userid);
			Log.i("lch", "请求参数:" + map.toString());
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 2.3.7.6 我的卷卷列表查询
	 * @param infotype
	 * @param keyword
	 * @param categoryid
	 * @param credittypeid
	 * @param range
	 * @param shopid
	 * @param curpage
	 * @param pagemaxrow
	 * @return
	 */
	public static byte[] getMyInfolist(int status,int curpage,int pagemaxrow){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("userid", GoodPlaceContants.USERINFO ==null? "-1":GoodPlaceContants.USERINFO.getId());
			map.put("status", status);
			map.put("curpage", curpage);
			map.put("pagemaxrow", pagemaxrow);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 卷卷详细
	 * @param infoid
	 * @return
	 */
	public static byte[] getMyinfoDeatil(String myinfoid){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("userid", GoodPlaceContants.USERINFO ==null? "-1":GoodPlaceContants.USERINFO.getId());
			map.put("myinfoid", myinfoid);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	public static byte[] getShopProductNameListPostData(int shopid,int curpage) {
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("shopid", shopid);
			map.put("curpage", curpage);
			map.put("pagemaxrow", 20);
			
			Log.i("lch", "请求参数:" + map.toString());
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	public static byte[] getSendCommontPostData(int shopid, String userid, int score, int score1, int score2, int score3, int score4, String price, String message ) {
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("cityid", GoodPlaceContants.CITY_ID);
			map.put("shopid", shopid);
			map.put("userid", userid);
			map.put("score", score);
			map.put("score1", score1);
			map.put("score2", score2);
			map.put("score3", score3);
			map.put("score4", score4);
			map.put("reviewbody", message);
			map.put("price", price);
			
			Log.i("lch", "请求参数:" + map.toString());
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 我的点评
	 * @param curpage
	 * @return
	 */
	public static byte[] getMyCommentPostData(int curpage) {
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("userid", GoodPlaceContants.USERINFO ==null? "-1":GoodPlaceContants.USERINFO.getId());
			map.put("curpage", curpage);
			map.put("pagemaxrow", 20);
			Log.i("lch", "请求参数:" + map.toString());
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	public static byte[] getShopCommentPostData(int shopid,int curpage) {
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("cityid", GoodPlaceContants.CITY_ID);
			map.put("shopid", shopid);
			map.put("curpage", curpage);
			map.put("pagemaxrow", 20);
			Log.i("lch", "请求参数:" + map.toString());
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}

	
	public static byte[] getShopSignWallPostData(int shopid,int curpage) {
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("cityid", GoodPlaceContants.CITY_ID);
			map.put("shopid", shopid);
			map.put("curpage", curpage);
			map.put("pagemaxrow", 20);
			Log.i("lch", "请求参数:" + map.toString());
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 我的留言
	 * @param curpage
	 * @return
	 */
	public static byte[] getMySignWallPostData(int curpage) {
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("userid", GoodPlaceContants.USERINFO ==null? "-1":GoodPlaceContants.USERINFO.getId());
			map.put("curpage", curpage);
			map.put("pagemaxrow", 20);
			Log.i("lch", "请求参数:" + map.toString());
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	
	public static byte[] getShopImageListPostData(int shopid,int curpage) {
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("cityid", GoodPlaceContants.CITY_ID);
			map.put("shopid", shopid);
			map.put("curpage", curpage);
			map.put("pagemaxrow", 20);
			Log.i("lch", "请求参数:" + map.toString());
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 我的图片
	 * @param shopid
	 * @param curpage
	 * @return
	 */
	public static byte[] getMyImageListPostData(int curpage) {
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("userid", GoodPlaceContants.USERINFO ==null? "-1":GoodPlaceContants.USERINFO.getId());
			map.put("curpage", curpage);
			map.put("pagemaxrow", 20);
			Log.i("lch", "请求参数:" + map.toString());
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	
	/**
	 * 签到
	 * @return
	 */
	public static Map<String, Object> getUserSignPostData(int shopid, String userid, int score, String signbody){
		Map<String, Object> map = null;
		try {
			map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("shopid", shopid);
			map.put("userid", userid);
			map.put("score", score);
			map.put("signbody", signbody);
			Log.i("lch", "请求参数:" + map.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 上传图片
	 * @return
	 */
	public static Map<String, Object> getSendShopImagePostData(int shopid, String userid,String imgtype, String imgname, int star, String price){
		Map<String, Object> map = null;
		try {
			map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("shopid", shopid);
			map.put("userid", userid);
			map.put("imgtype", imgtype);
			map.put("imgname", imgname);
			map.put("star", star);
			map.put("price", price);
			Log.i("lch", "请求参数:" + map.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 取网关
	 * @return
	 */
	public static byte[] getWG(){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 取城市列表
	 * @return
	 */
	public static byte[] getCityList(){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			map.put("lbsappid", GoodPlaceContants.LBSAPPID);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 取积分类型信息
	 * @return
	 */
	public static byte[] getAllcredittypesList(){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 取排序方式类型信息
	 * @return
	 */
	public static byte[] getSortList(){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	
	
	/**
	 * 取资讯类型信息
	 * @return
	 */
	public static byte[] getInfotypeListPostData(){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 取积分类型信息
	 * @return
	 */
	public static byte[] getRankingListPostData(){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 取行业分类信息
	 * @return
	 */
	public static byte[] getCategoriesListPostData(String parentid){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("parentid", parentid);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 取商圈分类信息
	 * @return
	 */
	public static byte[] getRegionsListPostData(String parentid){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("cityid", GoodPlaceContants.SESSEIONMOUDLE.getCity().getId());
			if(!parentid.equals("") && !parentid.equals("0"))
				map.put("parentid", parentid);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 取签到信息
	 * @return
	 */
	public static byte[] getSession(){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("devicetoken", GoodPlaceContants.DEVICETOKEN);
			map.put("lng", GoodPlaceContants.LNG);
			map.put("lat", GoodPlaceContants.LAT);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 取验证码
	 * @return
	 */
	public static byte[] getMobileCode(String phonenumber){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("phonenumber", phonenumber);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 手机注册
	 * @return
	 */
	public static byte[] getResgiterPhone(String mobilecode,String phonenumber,String username,String password){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("checkcode", mobilecode);
			map.put("userid", phonenumber);
			map.put("username", username);
			map.put("password", password);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 邮箱注册
	 * @return
	 */
	public static byte[] getResgiterMail(String email,String username,String password){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("userid", "");
			map.put("username", email);
			map.put("password", password);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 修改密码
	 * @return
	 */
	public static byte[] getEditPw(String oldpw,String newpw){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("userid", GoodPlaceContants.USERINFO ==null? "-1":GoodPlaceContants.USERINFO.getId());
			map.put("oldpassword", oldpw);
			map.put("newpassword", newpw);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 登录
	 * @return
	 */
	public static byte[] getLogin(String username,String password){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("userid", username);
			map.put("pwd", Encryption.toMd5(password));
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 注销
	 * @return
	 */
	public static byte[] getLogout(){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("userid", GoodPlaceContants.USERINFO ==null? "-1":GoodPlaceContants.USERINFO.getId());
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 上传用户头像
	 * @return
	 */
	public static Map<String, Object> getUploadmemberimg(String userid){
		Map<String, Object> map = null;
		try {
			map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("userid", userid);
//			map.put("ufile", filebody);
//			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 资讯列表
	 * @param infotype 资讯类型ID，可通过2.3.1.7接口获取（整形，可选，没有该参数或为空则表示全部）
	 * @param keyword 资讯内容或商家名称的关键词（字符串，可选，没有该参数或为空则表示全部）
	 * @param categoryid 行业ID（整形，可选，没有该参数或为空则表示全部）
	 * @param credittypeid 积分类型ID（整形，可选，没有该参数或为空则表示全部
	 * @param range 距离（整形，可选，单位为米，但需与lat及lon同时使用）
	 * @param shopid 商家ID
	 * @param curpage 当次获取结果的第几页（整形，资讯列表结果以分页返回，表示当次获取结果的第几页，空则默认为第1页）
	 * @param pagemaxrow 每页所含最大记录数
	 * @return
	 */
	public static byte[] getInfolist(String infotype,String keyword,
			String categoryid,String credittypeid,String range,String shopid ,int curpage,int pagemaxrow){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("cityid", GoodPlaceContants.SESSEIONMOUDLE.getCity().getId());
			
			if(!infotype.equals(""))
				map.put("infotype", infotype);
			if(!keyword.equals(""))
				map.put("keyword", keyword);
			if(!categoryid.equals("") && !categoryid.equals("0"))
				map.put("categoryid", categoryid);
			if(!credittypeid.equals(""))
				map.put("credittypeid", credittypeid);
			if(!range.equals("") && !range.equals("0")){
//				map.put("lng", GoodPlaceContants.SESSEIONMOUDLE.getCity().getLng());
//				map.put("lat", GoodPlaceContants.SESSEIONMOUDLE.getCity().getLat());
				map.put("lng",GoodPlaceContants.LNG);
				map.put("lat",GoodPlaceContants.LAT);
				map.put("range", range);
			}
			if(!shopid.equals("")){
				map.put("shopid", shopid);
			}
			map.put("curpage", curpage);
			map.put("pagemaxrow", pagemaxrow);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 资讯详细
	 * @param infoid
	 * @return
	 */
	public static byte[] getInfoDeatil(String infoid){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("infoid", infoid);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 2.3.6.4 资讯兑换或购买预判
	 * @param infoid
	 * @return
	 */
	public static byte[] getInfoPreget(String infoid){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("infoid", infoid);
			map.put("userid", GoodPlaceContants.USERINFO ==null? "-1":GoodPlaceContants.USERINFO.getId());
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 2.3.6.5 资讯兑换或购买接口
	 * @param infoid
	 * @return
	 */
	public static byte[] getBuyInfo(String infoid, String number){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("userid", GoodPlaceContants.USERINFO ==null? "-1":GoodPlaceContants.USERINFO.getId());
			map.put("infoid", infoid);
			map.put("phonenumber", number);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 发送资讯到手机
	 * @param infoid
	 * @return
	 */
	public static byte[] getInfoSMS(String infoid, String number){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("userid", GoodPlaceContants.USERINFO ==null? "-1":GoodPlaceContants.USERINFO.getId());
			map.put("infoid", infoid);
			map.put("phonenumber", number);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 取资讯图片下载地址
	 * @param infoid
	 * @return
	 */
	public static byte[] getInfoPicDownload(String infoid){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("userid", GoodPlaceContants.USERINFO ==null? "-1":GoodPlaceContants.USERINFO.getId());
			map.put("infoid", infoid);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 取用户收藏商店列表
	 * @return
	 */
	public static byte[] getMyfavshops(int curpage,int pagemaxrow){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("userid", GoodPlaceContants.USERINFO ==null? "-1":GoodPlaceContants.USERINFO.getId());
			map.put("curpage", curpage);
			map.put("pagemaxrow", pagemaxrow);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 取消收藏
	 * @return
	 */
	public static byte[] getCancelMyfavshop(String shopid){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("userid", GoodPlaceContants.USERINFO ==null? "-1":GoodPlaceContants.USERINFO.getId());
			map.put("shopid", shopid);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 取用户信息
	 * @return
	 */
	public static byte[] getUserinfo(){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("userid", GoodPlaceContants.USERINFO ==null? "-1":GoodPlaceContants.USERINFO.getId());
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 搜索商店
	 * @param regionid 商圈ID（整形，可选，没有该参数或为空则表示全部）
	 * @param keyword 商家名称或地址包含的关键词（字符串，可选，没有该参数或为空则表示全部）
	 * @param categoryid 行业ID（整形，可选，没有该参数或为空则表示全部）
	 * @param credittypeid 积分类型ID（整形，可选，没有该参数或为空则表示全部
	 * @param range 距离（整形，可选，单位为米，但需与lat及lon同时使用）
	 * @param minavgprice 最低人均消费价格（整形，可选，没有该参数或为空则表示全部）
	 * @param maxavgprice 最高人均消费价格（整形，可选，没有该参数或为空则表示全部）
	 * @param ifdiscount 是否有打折（整形，可选，1表示是，0表示否，没有该参数或为空则表示全部）
	 * @param ifpromo 是否有优惠卷（整形，可选，1表示是，0表示否，没有该参数或为空则表示全部）
	 * @param ifgift 是否有赠送（整形，可选，1表示是，0表示否，没有该参数或为空则表示全部）
	 * @param sortid 商家列表排序ID（整形，注：商家排行榜通过2.3.2.2获取）
	 * @param curpage 当次获取结果的第几页
	 * @param pagemaxrow 每页所含最大记录数
	 * @return
	 */
	public static byte[] getSearchshopslist(
			String regionid,
			String keyword,
			String categoryid,
			String credittypeid,
			String range,
			String minavgprice,
			String maxavgprice,
			String ifdiscount,
			String ifpromo,
			String ifgift,
			String sortid,
			int curpage,
			int pagemaxrow
			){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("cityid", GoodPlaceContants.SESSEIONMOUDLE.getCity().getId());
			
			if(!regionid.equals(""))
				map.put("regionid", regionid);
			if(!keyword.equals(""))
				map.put("keyword", keyword);
			if(!categoryid.equals("") && !categoryid.equals("0"))
				map.put("categoryid", categoryid);
			if(!credittypeid.equals(""))
				map.put("credittypeid", credittypeid);
			if(!range.equals("") && !range.equals("0")){
//				map.put("lng", GoodPlaceContants.SESSEIONMOUDLE.getCity().getLng());
//				map.put("lat", GoodPlaceContants.SESSEIONMOUDLE.getCity().getLat());
				map.put("lng",GoodPlaceContants.LNG);
				map.put("lat",GoodPlaceContants.LAT);
				map.put("range", range);
			}
			if(!minavgprice.equals(""))
				map.put("minavgprice", minavgprice);
			if(!maxavgprice.equals(""))
				map.put("maxavgprice", maxavgprice);
			if(!ifdiscount.equals(""))
				map.put("ifdiscount", ifdiscount);
			if(!ifpromo.equals(""))
				map.put("ifpromo", ifpromo);
			if(!ifgift.equals(""))
				map.put("ifgift", ifgift);
			if(!sortid.equals(""))
				map.put("sortid", sortid);
			map.put("curpage", curpage);
			map.put("pagemaxrow", pagemaxrow);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 切换城市
	 * @return
	 */
	public static byte[] getChangeCity(String cityid){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("cityid", cityid);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 提交错误
	 * @return
	 */
	public static byte[] getSummintProblem(String problem){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
			map.put("problem", problem);
			map.put("suggestion", problem);
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	/**
	 * 首页附近分类
	 * @return
	 */
	public static byte[] getNearshopcoutinfoPostData(){
		byte[] postData = null;
		try {
			Map<String, Object> map= new HashMap<String, Object>();
			setHead(map);
			map.put("sessionkey", GoodPlaceContants.SESSION_KEY);
//			map.put("lng", GoodPlaceContants.SESSEIONMOUDLE.getCity().getLng());
//			map.put("lat", GoodPlaceContants.SESSEIONMOUDLE.getCity().getLat());
			
			map.put("lng",GoodPlaceContants.LNG);
			map.put("lat",GoodPlaceContants.LAT);
			
//			Log.i("zjj", "首页附近分类 请求内容:" + "lng:" + GoodPlaceContants.SESSEIONMOUDLE.getCity().getLng()
//					+ "lat:" + GoodPlaceContants.SESSEIONMOUDLE.getCity().getLat());
			Log.i("zjj", "首页附近分类 请求内容:" 
					+ "lat:" + GoodPlaceContants.LAT
					+ "lng:" + GoodPlaceContants.LNG);
			
			postData = mapToByteArray(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return postData;
	}
	
	
	
	public static void setHead(Map map) {
		if (map != null) {
			map.put("lbsappid", GoodPlaceContants.LBSAPPID);
//			map.put("sessionkey", "1");
		}
	}
	

	private static byte[] mapToByteArray(Map map) {
		byte[] ret = null;
		List<NameValuePair> formparams = getListParams(map);
		return nameValuePairsToByteArray(formparams);
	}

	
	
	/**
	 * 此处可以作成静态工具方法
	 * @param map
	 * @return
	 */
	private static List<NameValuePair> getListParams(Map map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();

		Set keys = map.keySet();

		for (Object key : keys) {
			Object value = map.get(key);
			if (value.getClass().isArray()) {
				String[] values = (String[]) value;
				for (int i = 0; i < values.length; i++) {
					formparams.add(new BasicNameValuePair((String) key, values[i]));
				}
			} else {
				formparams.add(new BasicNameValuePair((String) key, value.toString()));
			}

		}
		return formparams;
	}

	private static byte[] nameValuePairsToByteArray(List<NameValuePair> nameValuePairs) {
		byte[] ret = null;
		try {
			UrlEncodedFormEntity resultEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
			ret = EntityUtils.toByteArray(resultEntity);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

}
