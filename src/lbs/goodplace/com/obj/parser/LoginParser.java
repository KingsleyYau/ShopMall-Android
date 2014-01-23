package lbs.goodplace.com.obj.parser;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.GatewayListModule;
import lbs.goodplace.com.obj.GatewayModule;
import lbs.goodplace.com.obj.LoginModule;
import lbs.goodplace.com.obj.RequestResultModule;
import lbs.goodplace.com.obj.ShopListModule;
import lbs.goodplace.com.obj.ShopModule;
import lbs.goodplace.com.obj.UserInfoModule;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 登录解析类
 * @author lenovo123
 *
 */
public class LoginParser implements IParser {

	@SuppressWarnings("null")
	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			try {
				LoginModule loginModule  = new LoginModule();
				
				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag == 1) {
					loginModule.mRequestResult.setResult(true);
					
					JSONObject userjson = json.getJSONObject("userinfo");
					
					loginModule.mUserInfo.setPhotocount(userjson.getInt("photocount"));
					loginModule.mUserInfo.setLevel(userjson.getString("level"));
					loginModule.mUserInfo.setFavoshopcount(userjson.getInt("favoshopcount"));
					loginModule.mUserInfo.setNickname(userjson.getString("nickname"));
					loginModule.mUserInfo.setFavoshopinfocount(userjson.getInt("favoshopinfocount"));
					loginModule.mUserInfo.setId(userjson.getString("userid"));
					loginModule.mUserInfo.setCheckincount(userjson.getInt("checkincount"));
					loginModule.mUserInfo.setSpscore(userjson.getInt("spscore"));
					loginModule.mUserInfo.setOtherscore(userjson.getInt("otherscore"));
					loginModule.mUserInfo.setReviewcount(userjson.getInt("reviewcount"));
					if(userjson.has("myinfocount"))
						loginModule.mUserInfo.setMyinfocount(userjson.getInt("myinfocount"));
					loginModule.mUserInfo.setAvatarurl(userjson.getString("avatar"));
				}else{
					loginModule.mRequestResult.setResult(false);
					loginModule.mRequestResult.setErrorcode(opret.getInt("code"));
				}
				//解析json
				return loginModule;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
