package lbs.goodplace.com.obj.parser;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.GatewayListModule;
import lbs.goodplace.com.obj.GatewayModule;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.ShopModule;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 网关类型解析类
 * @author lenovo123
 *
 */
public class GatwayParser implements IParser {

	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			try {
				GatewayListModule gatewayListModule = null;
				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag == 1) {
					gatewayListModule = new GatewayListModule();
					gatewayListModule.mLbsappid =String.valueOf(json.getInt("lbsappid"));
					
					JSONArray gwArray = json.getJSONArray("gwlist");
					for (int i = 0; i < gwArray.length(); i++) {
						JSONObject shopJsonObject = (JSONObject) gwArray.get(i);
						GatewayModule gwModule = new GatewayModule();
						gwModule.setName(shopJsonObject.getString("gw"));
						gwModule.setIp(shopJsonObject.getString("addr"));
						gwModule.setPort(shopJsonObject.getString("port"));
						
						gatewayListModule.gatewayList.add(gwModule);
					}
				}
				//解析json
				return gatewayListModule;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
