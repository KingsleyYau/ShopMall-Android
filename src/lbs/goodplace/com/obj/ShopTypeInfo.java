package lbs.goodplace.com.obj;

/**
 * 商家排行榜
 * (2.3.1.8 )
 * @author Administrator
 *
 */
public class ShopTypeInfo {
	private String categoryid;	//行业分类ID
	private String categoryfavicon;	//行业分类的图标URL地址
	private String categoryname;	//行业分类名称
	
	
	/**
	 * 排行榜
	 * @author Administrator
	 *
	 */
	public class Carrank{
		private String id;		//排行榜ID
		private String iconurl;	//排行榜的图标URL
		private String name;	//排行榜名称
	}
}
