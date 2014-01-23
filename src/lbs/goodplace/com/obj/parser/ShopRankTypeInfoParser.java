package lbs.goodplace.com.obj.parser;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.ShopRankTypeInfo;
import lbs.goodplace.com.obj.ShopTypeInfo;
import lbs.goodplace.com.obj.TestInfo;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 商家排行榜类型解析类
 * @author shazhuzhu
 *
 */
public class ShopRankTypeInfoParser implements IParser {

	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			try {
				ShopRankTypeInfo info = null;
				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag == 1) {
					info = new ShopRankTypeInfo();
					
					JSONArray jsonArray = json.getJSONArray("json");
					for (int i = 0; i < jsonArray.length(); i++) {
						ShopTypeInfo shopTypeInfo = new ShopTypeInfo();
					}
					
					
					
				}
				//解析json
				return info;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
