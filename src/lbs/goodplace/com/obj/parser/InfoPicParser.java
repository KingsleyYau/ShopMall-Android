package lbs.goodplace.com.obj.parser;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.GatewayListModule;
import lbs.goodplace.com.obj.GatewayModule;
import lbs.goodplace.com.obj.InfoPicModule;
import lbs.goodplace.com.obj.RequestResultModule;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.ShopModule;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;


/**
 * 只返回成功/失败结果解析类
 * @author lenovo123
 *
 */
public class InfoPicParser implements IParser {

	@SuppressWarnings("null")
	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			InfoPicModule picModule = new InfoPicModule();
			try {
				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag == 1) {
					picModule.mURL = json.getString("infophoto");
				}
				
				//解析json
				return picModule;
			} catch (Exception e) {
				Log.i("zjj", "InfoPicParser Exception:" + e.toString());
				return picModule;
			}
		}
		return null;
	}
}
