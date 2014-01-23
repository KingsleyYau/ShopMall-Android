package lbs.goodplace.com.manage.util;

/**
 * IPreferencesIds
 */
public interface IPreferencesIds {
	public static final String GAME_SHAREPREFERENCES_FILE = "gogame";
	
	public static final String GO_GAME_DATA_PREFERENCES = "goGameDataPreferences";

	//如果恢复默认时需要清除某个sharepreference请将它加入这里
	public static final String[] NEED_CLEAR_PREFERENCES = {};
	//保存userId
	public static final String GO_GAME_USERID = "goGameUserIdPreferences";
	//保存是否已经登录
	public static final String GO_GAME_FACEBOOK_LOGIN = "goGameFaceBookLogin";
	
	
}
