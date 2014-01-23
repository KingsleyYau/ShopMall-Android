package lbs.goodplace.com.manage.util;

/**
 * 存放全局变量
 * @author zhaojunjie
 *
 */
public class GlobalUtil {
	
	/**
	 * 标题栏 按钮大小
	 */
	public static int Titlebar_button_size = 30;//(dp)
	
	/**
	 * 标题栏 按钮左右边距
	 */
	public static int Titlebar_button_LRMargins = 2;//(dp)
	
	/**
	 * 广播KEY 登录成功
	 */
	public static String BROADCAST_KEY_LOGINSUCCESS = "PUSH_KEY_LOGINSUCCESS";
	
	/**
	 * 广播KEY 注销成功
	 */
	public static String BROADCAST_KEY_LOGOUTSUCCESS = "BROADCAST_KEY_LOGOUTSUCCESS";
	
	/**
	 * 广播KEY 切换城市成功
	 */
	public static String BROADCAST_KEY_CHANGECITYSUCCESS = "BROADCAST_KEY_CHANGECITYSUCCESS";
	
	/**
	 * 广播KEY 切换城市但并不是所在城市
	 */
	public static String BROADCAST_KEY_CHANGECITYNOTLOCA = "BROADCAST_KEY_CHANGECITYNOTLOCA";
	
	/**
	 * SharedPreferences 名
	 */
	public static String SHAREDPERFERENCES_NAME_LOGIN = "login_userinfor";
	
	/**
	 * SharedPreferences 用户选择搜索的条件
	 */
	public static String SHAREDPERFERENCES_NAME_SEARCHCONDITION = "search_condition";
	
	/**
	 * 用户名的KEY
	 */
	public static String SHAREDPERFERENCES_KEY_USERNAME = "login_username";
	
	/**
	 * 用户名的KEY
	 */
	public static String SHAREDPERFERENCES_KEY_PW = "login_userpw";
	
	/**
	 * 搜索距离的KEY
	 */
	public static String SHAREDPERFERENCES_KEY_SEARCHDISTANCE = "search_distance";
}
