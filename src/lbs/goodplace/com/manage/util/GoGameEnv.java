package lbs.goodplace.com.manage.util;

import android.os.Environment;

/**
 * 公共常量定义
 * 
 */
public final class GoGameEnv {

	/**
	 * url相关常量定义在此处
	 * 
	 * 
	 */
	public static final class Url {

	}

	/**
	 * Market搜索用到的常量
	 * 
	 */
	public static final class Market {
		public static final String PACKAGE = "com.android.vending";

		// 用包名搜索market上的软件
		public static final String BY_PKGNAME = "market://search?q=pname:";

		// 直接使用关键字搜索market上的软件
		public static final String BY_KEYWORD = "market://search?q=";

		// 进入软件详细页面
		public static final String APP_DETAIL = "market://details?id=";

		// 浏览器版本的电子市场详情地址
		public static final String BROWSER_APP_DETAIL = "https://play.google.com/store/apps/details?id=";
	}

	/**
	 * 路径类 所有路径相关的常量都同意放在此处
	 * 
	 */
	public static final class Path {
		/**
		 * sdcard head
		 */
		public final static String SDCARD = Environment.getExternalStorageDirectory().getPath();

		// 存储路径
		public final static String GAME_DIR = SDCARD + "/GoGame";

		/**
		 * 数据库文件备份目录
		 */
		public final static String DBFILE_PATH = GAME_DIR + "/db";

		/**
		 * SharedPreferences文件备份目录
		 */
		public final static String PREFERENCESFILE_PATH = GAME_DIR + "/preferences";

		/**
		 * 日志文件备份目录
		 */
		public final static String LOG_DIR = GAME_DIR + "/log/";

		/**
		 * 图片文件目录
		 */
		public final static String COMMON_ICON_PATH = GAME_DIR + "/icon/";

		/**
		 * 屏幕截图文件夹
		 */
		public static final String SCREEN_CAPUTRE_PATH =  GAME_DIR + "/capture/";

		/***
		 * 分享功能，图片存储路径
		 */
		public static final String SHARE_IMAGE_PATH =  GAME_DIR + "/share/";

		/**
		 * 消息中心文件存储目录
		 */
		public static final String MESSAGECENTER_PATH =  GAME_DIR + "/messagecenter/";

		/**
		 * 截图存放路径
		 */
		public static final String SNAPSHOT_PATH =  GAME_DIR + "/SnapShot";

		/**
		 * 游戏社区缓存路径
		 */
		public static final String GO_GAME_CACHE_PATH = GAME_DIR + "/Cache/";
		
		/**
		 * 个人中心文件存储目录
		 */
		public static final String PERSONALCENTER_PATH =  GAME_DIR + "/personalcenter/";
		
		/**
		 * 游戏社区上传数据时，临时的图片路径
		 */
		public static final String SEND_DATA_TEMP_PATH =  GAME_DIR + "/temp/";
	}
	
	public static final String GAME_LOG_TAG = "gogame";
}
