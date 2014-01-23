package lbs.goodplace.com.View.main;

import android.os.Environment;

public class Contants {
	public static final int LOAD_SUCCESS = 0; //加载数据失败
	public static final int LOAD_FALSE = 1; //加载数据成功
	
	public static final String SHOP_IMAGE_LIST = "shop_image_list"; //商店图片队列
	public static final String SHOP_IMAGE_CLICK_POSITION = "shop_image_click_position"; //商店图片队列点击位置
	public static final String SHOP_PRODUCT_NAME = "shop_product_name"; //产品名称

	public static final int SCORE_ONE_SIZE = 20; // 1个星星所占分数
	
	public static final String SHOP_INFO_TEXT = "shop_info_text";
	public static final String SHOP_TRAFFIC_TEXT = "shop_traffic_text";
	
	public static final class Path {
		
		public final static String SDCARD = Environment.getExternalStorageDirectory().getPath();

		public static final String IMAGE_DOWN_SAVE_PATH = SDCARD + "/goodPlace" + "/downImage/";
		
		
		public static final String IMAGE_CACHE_PATH = SDCARD + "/goodPlace" + "/imageTemp/";
		
		public static final String IMAGE_COMPRESS_CACHE_PATH = SDCARD + "/goodPlace" + "/compressImageTemp/";
	
	
		public final static String PIC_NAME = "myCameraAvatar" + ".jpg";
		public final static String CAMERA_SAVE_PATH = IMAGE_CACHE_PATH + PIC_NAME;
		public final static String COMPRESS_SAVE_PATH = IMAGE_COMPRESS_CACHE_PATH + PIC_NAME;
		
		//用户头像
		public final static String USERPIC_NAME = "userCameraAvatar" + ".jpg";
		public final static String CAMERA_SAVE_USER_PATH = IMAGE_CACHE_PATH + USERPIC_NAME;
		public final static String COMPRESS_SAVE_USER_PATH = IMAGE_COMPRESS_CACHE_PATH + USERPIC_NAME;
	
	}
	
}
