package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.CredittypeModule;
import lbs.goodplace.com.obj.GatewayListModule;
import lbs.goodplace.com.obj.GatewayModule;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.ShopModule;
import lbs.goodplace.com.obj.SortModule;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 排序方式类型解析类
 * @author lenovo123
 *
 */
public class SorttypesParser implements IParser {

	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			try {
				List<SortModule> sorttypelist = new ArrayList<SortModule>();
				
				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag == 1) {
					
					JSONArray gwArray = json.getJSONArray("shopsorttypes");
					for (int i = 0; i < gwArray.length(); i++) {
						JSONObject shopJsonObject = (JSONObject) gwArray.get(i);
						SortModule sortModule = new SortModule();
						sortModule.setName(shopJsonObject.getString("sortname"));
						sortModule.setId(String.valueOf(shopJsonObject.getInt("sortid")));
						
						sorttypelist.add(sortModule);
					}
				}
				//解析json
				return sorttypelist;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
