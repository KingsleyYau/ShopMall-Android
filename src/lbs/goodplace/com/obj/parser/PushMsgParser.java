package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.InfoListModule;
import lbs.goodplace.com.obj.InfoModule;
import lbs.goodplace.com.obj.MyPushMessageItem;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Push解析类
 * @author lenovo123
 *
 */
public class PushMsgParser{

	public MyPushMessageItem parser(JSONObject json) {
		if (json != null) {
			try {
				MyPushMessageItem item = new MyPushMessageItem();
				JSONObject opretbody = json.getJSONObject("body");
				
				if(!opretbody.isNull("typeid"))
					item.typeid = opretbody.getInt("typeid");
				
				if(!opretbody.isNull("id"))
					item.id = opretbody.getString("id");;
				
				if(!opretbody.isNull("aps")){
					JSONObject opret = opretbody.getJSONObject("aps");
					item.alert = opret.getString("alert");
					item.sound = opret.getString("sound");
				}
			
				return item;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
