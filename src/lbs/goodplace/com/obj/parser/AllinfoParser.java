package lbs.goodplace.com.obj.parser;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.manage.requestmanage.IParser;
import lbs.goodplace.com.obj.InfoListModule;
import lbs.goodplace.com.obj.InfoModule;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 资讯列表解析类
 * @author lenovo123
 *
 */
public class AllinfoParser implements IParser {

	@Override
	public Object parser(JSONObject json) {
		if (json != null) {
			try {
				InfoListModule infolist = new InfoListModule();
//				List<InfoModule> allinfolist = new ArrayList<InfoModule>();
				
				infolist.mPageInfo.mTotalrecordcount = json.getInt("totalrecordcount");
				infolist.mPageInfo.mCurpag  = json.getInt("curpage");
				infolist.mPageInfo.mPagemaxrow  = json.getInt("pagemaxrow");
				infolist.mPageInfo.mPagecount  = json.getInt("pagecount");
				
				JSONObject opret = json.getJSONObject("opret");
				int opflag = opret.getInt("opflag");
				if (opflag == 1) {
					
					if(json.has("myinfolist")){	//我的卷卷
						JSONArray gwArray = json.getJSONArray("myinfolist");
						for (int i = 0; i < gwArray.length(); i++) {
							JSONObject infoypeJsonObject = (JSONObject) gwArray.get(i);
							InfoModule itModule = new InfoModule();
							itModule.setBuytips(infoypeJsonObject.getString("buytips"));
							itModule.setCanceltime(infoypeJsonObject.getLong("canceltime"));
							itModule.setGettime(infoypeJsonObject.getLong("gettime"));
							itModule.setId(infoypeJsonObject.getString("infoid"));
							itModule.setMemberid(infoypeJsonObject.getString("memberid"));
							itModule.setStatus(infoypeJsonObject.getInt("status"));
							itModule.setInfoenddate(infoypeJsonObject.getLong("infoenddate"));
							itModule.setTitle(infoypeJsonObject.getString("infotitle"));
							itModule.setDefaultpicurl(infoypeJsonObject.getString("infophoto"));
							itModule.setShowtips(infoypeJsonObject.getString("showtips"));
							itModule.setShopname(infoypeJsonObject.getString("shopname"));
							itModule.setShowtype(infoypeJsonObject.getString("showtype"));
							itModule.setGetprice(infoypeJsonObject.getString("getprice"));
							itModule.setShopid(infoypeJsonObject.getString("shopid"));
							itModule.setInfobegindate(infoypeJsonObject.getLong("infobegindate"));
							itModule.setDesc(infoypeJsonObject.getString("infodesc"));
							itModule.setUsetime(infoypeJsonObject.getLong("usetime"));
							itModule.setMyinfoid(infoypeJsonObject.getString("myinfoid"));
							itModule.setGettype(infoypeJsonObject.getString("gettype"));
							
							infolist.mInfoModuleList.add(itModule);
						}
					}else if(json.has("infolist")){	//商家资讯
						JSONArray gwArray = json.getJSONArray("infolist");
						for (int i = 0; i < gwArray.length(); i++) {
							JSONObject infoypeJsonObject = (JSONObject) gwArray.get(i);
							InfoModule itModule = new InfoModule();
							itModule.setId(infoypeJsonObject.getString("infoid"));
							itModule.setTypeid(infoypeJsonObject.getString("infotypeid"));
							itModule.setTypename(infoypeJsonObject.getString("infotypename"));
							itModule.setBegindate(infoypeJsonObject.getLong("begindate"));
							itModule.setEnddate(infoypeJsonObject.getLong("enddate"));
							itModule.setInfobegindate(infoypeJsonObject.getLong("infobegindate"));
							itModule.setInfoenddate(infoypeJsonObject.getLong("infoenddate"));
							itModule.setTitle(infoypeJsonObject.getString("infotitle"));
//							itModule.setDesc(infoypeJsonObject.getString("infodesc"));
							itModule.setDefaultpicurl(infoypeJsonObject.getString("defaultpic"));
							itModule.setShopid(infoypeJsonObject.getString("shopid"));
							itModule.setShopname(infoypeJsonObject.getString("shopname"));
							
							infolist.mInfoModuleList.add(itModule);
						}
					}
					
				}
				//解析json
				return infolist;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
