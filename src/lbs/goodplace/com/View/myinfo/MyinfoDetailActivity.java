package lbs.goodplace.com.View.myinfo;

import java.util.ArrayList;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.main.Contants;
import lbs.goodplace.com.View.main.GoodPlaceApp;
import lbs.goodplace.com.View.main.MainActivity;
import lbs.goodplace.com.View.main.ShopInfoActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.NetState;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.manage.util.ContantsUtils;
import lbs.goodplace.com.manage.util.DownImageUitl;
import lbs.goodplace.com.manage.util.FileUtil;
import lbs.goodplace.com.obj.InfoListModule;
import lbs.goodplace.com.obj.InfoModule;
import lbs.goodplace.com.obj.InfoPicModule;
import lbs.goodplace.com.obj.ShopModule;
import lbs.goodplace.com.obj.parser.AllinfoParser;
import lbs.goodplace.com.obj.parser.InfoPicParser;
import lbs.goodplace.com.obj.parser.MyinfoDetailParser;
import lbs.goodplace.com.obj.parser.infoDetailParser;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyinfoDetailActivity extends ModuleActivity{
	public static String KEY_MYINFOID = "KEY_MYINFOID";
	
	public static final int LOAD_SUCCESS = 0;
	public static final int LOAD_FAILE = 1;

	private String mMyinfoid;
	private InfoModule mInfoModule;
//	private AsyncImageManager mImgManager = null;
	private String mPicUrl = "";	//图片下载地址
	//
	private Context mContext;
	private ImageView mImageViewQR;
	private ImageView mImageViewDetail;
	private TextView mTextViewTips;
	private TextView mTextViewDetail;
	private TextView mTextViewTime;
	private TextView mTextViewShowtips;
	private TextView mTextViewVericode;
	private Button mButtonDownload;
	
	//缓存
	private String CACHE_NAME = "MyinfoDetailActivity";
	private boolean mIsNeddCache = true;	//是否需要读取缓存
	private NetState mNetState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.msgdetail_view, mLayout_body);
		mContext = MyinfoDetailActivity.this;
//		mImgManager = AsyncImageManager.getInstance();
		mNetState = new NetState(mContext);
		
		Bundle extras = getIntent().getExtras();
        if(extras != null){
        	if(extras.containsKey(KEY_MYINFOID)){
        		mMyinfoid = extras.getString(KEY_MYINFOID);
    		}
        }
		
        mImageViewQR = (ImageView)findViewById(R.id.imageView_qrcode);
		mImageViewDetail = (ImageView)findViewById(R.id.imageView_detail);
//		mImageViewDetail.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
////				String imgUrl = mInfoModule.getDefaultpicurl(); //图片下载路径
////				DownImageUitl.downImage(mContext, imgUrl, FileUtil.ICON_CACHE_PATH, Contants.Path.IMAGE_DOWN_SAVE_PATH);
//				if(!mNetState.isNetUsing()){
//					ShowToast(R.string.nonet);
//					return;
//				}
//				
//				Intent i = new Intent(mContext, MsgPicActivity.class);
//				i.putExtra(MsgPicActivity.KEY_URL, mPicUrl);
//				startActivity(i);
//			}
//		});
		
		mTextViewVericode = (TextView)findViewById(R.id.Textview_vericode);
		mTextViewTips = (TextView)findViewById(R.id.txtView_msgtips);
		mTextViewDetail = (TextView)findViewById(R.id.Textview_detail);
		mTextViewTime = (TextView)findViewById(R.id.Textview_time);
		mTextViewShowtips = (TextView)findViewById(R.id.txtView_showtips);
		mTextViewShowtips.setVisibility(View.GONE);
		mButtonDownload = (Button)findViewById(R.id.Button_download);
		mButtonDownload.setVisibility(View.GONE);
//		mButtonDownload.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(!mNetState.isNetUsing()){
//					ShowToast(R.string.nonet);
//					return;
//				}
//				
//				if(GoodPlaceContants.USERINFO == null){
//					Toast.makeText(mContext, mContext.getString(R.string.plzlogin), Toast.LENGTH_LONG).show();
//					return;
//				}
//				
////				Intent i = new Intent(mContext, GetInfoSMSActivity.class);
//				Intent i = new Intent(mContext, InfoPregetActivity.class);
//				i.putExtra(InfoPregetActivity.KEY_INFOID, mInfoid);
//				startActivity(i);
//			}
//		});
		
		setTitleText(R.string.myinfodetail);
		requestDetail();
	}
	
	private void requestDetail(){
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		
		byte[] postData = JsonRequestManage.getMyinfoDeatil(mMyinfoid);
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_MYINFODETAIL, postData, new MyinfoDetailParser(),mIsNeddCache, CACHE_NAME,
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mHandler.sendMessage(msg);
							Log.i("lch", "请求成功");
						} else {
							mHandler.sendEmptyMessage(LOAD_FAILE);
							Log.i("lch", "请求失败");
						}
					}
				});
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				//请求商家列表成功
				case LOAD_SUCCESS :
					mInfoModule = (InfoModule)msg.obj;
					
					String timestr= mContext.getString(R.string.msgusetime);
					timestr = String.format(timestr,ContantsUtils.formatData(mInfoModule.getInfobegindate()) + "",ContantsUtils.formatData(mInfoModule.getInfoenddate()) + "");
					mTextViewTime.setText(timestr);
					mTextViewTips.setText(mInfoModule.getShowtips());
					mTextViewDetail.setText(mInfoModule.getDesc());
					DownImageUitl.setIcon(mImageViewDetail, 
							mInfoModule.getDefaultpicurl(), 
							FileUtil.ICON_CACHE_PATH, String.valueOf(mInfoModule.getDefaultpicurl().hashCode()), 
							true);
					
					if(!mInfoModule.getBarimgurl().equals("")){
						mImageViewQR.setVisibility(View.VISIBLE);
						DownImageUitl.setIcon(mImageViewQR, 
								mInfoModule.getBarimgurl(), 
								FileUtil.ICON_CACHE_PATH, String.valueOf(mInfoModule.getBarimgurl().hashCode()), 
								true);
					}
					
					if(!mInfoModule.getVericode().equals("")){
						mTextViewVericode.setText(mInfoModule.getVericode());
						mTextViewVericode.setVisibility(View.VISIBLE);
					}
					
					break;
				
				//请求商家列表失败
				case LOAD_FAILE :
					Toast.makeText(mContext, "请求资讯详细失败", 0).show();
					break;
					
				default :
					break;
			}
		}

	};
	
//	private void requestDownloadPic(){
//		byte[] postData = JsonRequestManage.getInfoPicDownload(mInfoid);
//		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_INFOPIC, postData, new InfoPicParser(),false, "geInfopic",
//				new IDataListener(){
//
//					@Override
//					public void loadFinish(boolean success, Object object) {
//						if (success && object != null) {
//							// 还在子线程操作，handler是放回主线程处理
//							Message msg = new Message();
//							msg.obj = object;
//							msg.what = LOAD_SUCCESS;
//							mDownloadPicHandler.sendMessage(msg);
//							Log.i("lch", "请求成功");
//						} else {
//							mDownloadPicHandler.sendEmptyMessage(LOAD_FAILE);
//							Log.i("lch", "请求失败");
//						}
//					}
//				});
//	}
//	
//	private Handler mDownloadPicHandler = new Handler() {
//
//		@Override
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//				//请求商家列表成功
//				case LOAD_SUCCESS :
//					InfoPicModule mInfoModule = (InfoPicModule)msg.obj;
//					mPicUrl = mInfoModule.mURL;
//					
//					break;
//				
//				//请求商家列表失败
//				case LOAD_FAILE :
//					Toast.makeText(mContext, "请求资讯图片下载地细失败", 0).show();
//					break;
//					
//				default :
//					break;
//			}
//		}
//
//	};
}
