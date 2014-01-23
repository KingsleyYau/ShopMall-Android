package lbs.goodplace.com.manage.util;

import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.obj.LoginModule;
import lbs.goodplace.com.obj.RequestResultModule;
import lbs.goodplace.com.obj.UserInfoModule;
import lbs.goodplace.com.obj.parser.LoginParser;
import lbs.goodplace.com.obj.parser.ResultParser;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;

public class LoginUtils {
	private static final int LOAD_SUCCESS = 0;
	private static final int LOAD_FAILE = 1;
	
	public static void Login(final Context context, String username, String password, final Handler handler){
		byte[] postData = JsonRequestManage.getLogin(
				username,
				password);
		RequestManager.loadDataFromNet(context, GoodPlaceContants.URL_LOGIN, postData, new LoginParser(), false, "getlogin",
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							if(handler!= null){
								Message msg = new Message();
								msg.obj = object;
								msg.what = LOAD_SUCCESS;
								handler.sendMessage(msg);// .sendMessageDelayed(msg, 2000);
							}
							
							LoginModule loginmodule = (LoginModule)object;
							
							if(loginmodule.mRequestResult.result){
								GoodPlaceContants.USERINFO = loginmodule.mUserInfo;
								Intent intent = new Intent(GlobalUtil.BROADCAST_KEY_LOGINSUCCESS);
								context.sendBroadcast(intent);
							}
						} else {
							if(handler!= null){
								handler.sendEmptyMessage(LOAD_FAILE);
							}
						}
					}
			
				});
	}
	
	public static void Logout(final Context context, final Handler handler){
		byte[] postData = JsonRequestManage.getLogout();
		RequestManager.loadDataFromNet(context, GoodPlaceContants.URL_LOGOUT, postData, new ResultParser(), false, "getlogput",
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							if(handler!= null){
								Message msg = new Message();
								msg.obj = object;
								msg.what = LOAD_SUCCESS;
								handler.sendMessage(msg);// .sendMessageDelayed(msg, 2000);
							}
							
							RequestResultModule rm = (RequestResultModule)object;
							if(rm.result){
								GoodPlaceContants.USERINFO = null;
								
								//清空密码
								SharedPreferences userInfo = context.getSharedPreferences(GlobalUtil.SHAREDPERFERENCES_NAME_LOGIN, 0);  
						        userInfo.edit().putString(GlobalUtil.SHAREDPERFERENCES_KEY_PW, "").commit();  
								
								Intent intent = new Intent(GlobalUtil.BROADCAST_KEY_LOGOUTSUCCESS);
								context.sendBroadcast(intent);
							}
							
						} else {
							handler.sendEmptyMessage(LOAD_FAILE);
						}
					}
			
				});
	}
}
