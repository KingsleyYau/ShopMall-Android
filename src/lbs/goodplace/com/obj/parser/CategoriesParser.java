package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.CategoryModule;
import lbs.goodplace.com.obj.CredittypeModule;
import lbs.goodplace.com.obj.GatewayListModule;
import lbs.goodplace.com.obj.GatewayModule;
import lbs.goodplace.com.obj.InfotypeModule;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.ShopModule;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 行业分类解析类
 * @author lenovo123
 *
 */
public class CategoriesParser implements IParser {

	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			try {
				List<CategoryModule> categorieslist = new ArrayList<CategoryModule>();
				
				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag == 1) {
					
					JSONArray gwArray = json.getJSONArray("categories");
					for (int i = 0; i < gwArray.length(); i++) {
						JSONObject infoypeJsonObject = (JSONObject) gwArray.get(i);
						CategoryModule itModule = new CategoryModule();
						itModule.setId(infoypeJsonObject.getString("categoryid"));
						itModule.setParentid(infoypeJsonObject.getString("parentid"));
						itModule.setName(infoypeJsonObject.getString("categoryname"));
						itModule.setFaviconurl(infoypeJsonObject.getString("categoryfavicon"));
						if(infoypeJsonObject.has("dsfshopcount"))
							itModule.setDsfshopcount(infoypeJsonObject.getString("dsfshopcount"));
						if(infoypeJsonObject.has("hyfshopcount"))
							itModule.setHyfshopcount(infoypeJsonObject.getString("hyfshopcount"));
						
						categorieslist.add(itModule);
					}
				}
				//解析json
				return categorieslist;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
