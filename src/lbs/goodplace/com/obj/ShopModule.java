package lbs.goodplace.com.obj;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 2.3.2.3  商家具体信息
 * @author Administrator
 *
 */
public class ShopModule implements Serializable{
	public int id;
	public String name;
	public String ename;
	public String branchname;
	public long adddate;	//（自1970年1月1日起的秒数）（整形）
	public long lastdate;	//（自1970年1月1日起的秒数）（整形）
	public double lat;		//坐标经度（浮点数）(服务器提供的是Google坐标系)
	public double lng;		//坐标纬度（浮点数）(服务器提供的是Google坐标系)
	public String address;	//
	public boolean isdiscount;	//是否有打折（整形，0为否，1为是）
	public boolean ispromo;	//是否有优惠卷（整形，0为否，1为是）
	public boolean isgift;		//是否有赠送（整形，0为否，1为是）
	public boolean iscard;		//是否销售会员卡（整形，0为否，1为是）
	public boolean ishyf;		//是否导购平台合作商家（整形，0为否，1为是）
	public boolean isdsf;		//是否第三方积分合作商家（整形，0为否，1为是）
	public int categoryid;	//商家所属行业分类ID
	public String categoryname;//商家所属行业分类名称
	public int cityid;		//所在城市ID（
	public String defaultpicurl;	//商店图标URL（商家默认缩略图的URL，字符串）
	public String avgprice;	//商家人均消费价格
	public String pricetext;	//均消费价格内容
	public int regionid;	//所在商圈ID
	public String regionname;	//所在商圈名称
	public int score;		//用户对商家总体评分
	public int score1;
	public int score2;
	public int score3;
	public int score4;
	public String scoretext;	//用户对商家评分描述
	
	public String writeup;		//商家描述（字符串， HTML内容格式）
	public String phoneno;		//电话号码1
	public String phoneno2;	//电话号码2
	public String trafficinfo; //交通信息
	
	public ArrayList<ShopOtherbranchsInfo> otherbranchsList = new ArrayList<ShopOtherbranchsInfo>(); //其他分店
	public ShopInfosituationInfo infosituation = new ShopInfosituationInfo(); //商家发布资讯的情况
	public ArrayList<Recommendtags> recommendtags= new ArrayList<Recommendtags>(); //推荐列表
	public Commentsituation commentsituation = new Commentsituation(); //评论
	public Signsituation signsituation = new Signsituation(); //签到
}
