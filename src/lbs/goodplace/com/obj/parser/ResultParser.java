package lbs.goodplace.com.obj.parser;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.GatewayListModule;
import lbs.goodplace.com.obj.GatewayModule;
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
public class ResultParser implements IParser {

	@SuppressWarnings("null")
	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			RequestResultModule resultModule = new RequestResultModule();
			try {
				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag == 1) {
					resultModule.setResult(true);
				}else{
					resultModule.setResult(false);
					resultModule.setErrorcode(opret.getInt("code"));
				}
				//解析json
				return resultModule;
			} catch (Exception e) {
				Log.i("zjj", "ResultParser Exception:" + e.toString());
				
				resultModule.setResult(false);
				resultModule.setErrorcode(199001);
				return resultModule;
			}
		}
		return null;
	}
}
