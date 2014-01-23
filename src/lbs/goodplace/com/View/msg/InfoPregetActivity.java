package lbs.goodplace.com.View.msg;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.NetState;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.manage.util.ContantsUtils;
import lbs.goodplace.com.manage.util.ErrorCodeUtils;
import lbs.goodplace.com.obj.InfoModule;
import lbs.goodplace.com.obj.RequestResultModule;
import lbs.goodplace.com.obj.parser.ResultParser;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 2.3.6.4 资讯兑换或购买预判接口
 * @author Administrator
 *
 */
public class InfoPregetActivity extends ModuleActivity{
	public static String KEY_INFOID = "KEY_INFOID";
//	public static String KEY_ISFORMPUSH = "KEY_ISFORMPUSH";
	
	public static final int LOAD_SUCCESS = 0;
	public static final int LOAD_FAILE = 1;

	private String mInfoid;
	private InfoModule mInfoModule;
	
	//
	private Context mContext;
	private TextView mTextViewBuytips;
	private TextView mTextViewShopname;
	private TextView mTextViewInfotitle;
	private TextView mTextViewInfodes;
	private TextView mTextViewTime;
	private TextView mTextViewShowtips;
	private EditText mEditTextNum;
	private Button mButtonDownload;
	
	//缓存
	private String CACHE_NAME = "InfoPregetActivity";
	private boolean mIsNeddCache = true;	//是否需要读取缓存
	private NetState mNetState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.infopreget_view, mLayout_body);
		mContext = InfoPregetActivity.this;
//		mImgManager = AsyncImageManager.getInstance();
		mNetState = new NetState(mContext);
		
		Bundle extras = getIntent().getExtras();
        if(extras != null){
//    		if(extras.containsKey(KEY_ISFORMPUSH)){
//    			mInfoid = extras.getString(KEY_ISFORMPUSH);
//    			
//    			if(!GoodPlaceApp.mIsRuning){
//	    			Intent i = new Intent(mContext, MainActivity.class);
//	    			i.putExtra(MainActivity.KEY_PUSH2INFO_ID, this.mInfoid);
//	    			mContext.startActivity(i);
//	    			
//	    			finish();
//    			}
//    		}
        	if(extras.containsKey(KEY_INFOID)){
        		mInfoid = extras.getString(KEY_INFOID);
    		}
        }
		
        mTextViewBuytips = (TextView)findViewById(R.id.txtView_buytips);
        mTextViewShopname = (TextView)findViewById(R.id.Textview_shopname);
        mTextViewInfotitle = (TextView)findViewById(R.id.Textview_infotitle);
        mTextViewInfodes = (TextView)findViewById(R.id.Textview_infodes);
		mTextViewTime = (TextView)findViewById(R.id.Textview_time);
		mTextViewShowtips = (TextView)findViewById(R.id.txtView_showtips);
		mEditTextNum = (EditText)findViewById(R.id.Txtview_username);
		mButtonDownload = (Button)findViewById(R.id.Button_download);
		mButtonDownload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!mNetState.isNetUsing()){
					ShowToast(R.string.nonet);
					return;
				}
				
				if(GoodPlaceContants.USERINFO == null){
					Toast.makeText(mContext, mContext.getString(R.string.plzlogin), Toast.LENGTH_LONG).show();
					return;
				}
				
				requestBuy();
			}
		});
		
		setTitleText(R.string.msgdetail);
		requestDetail();
	}
	
	private void requestDetail(){
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		
		byte[] postData = JsonRequestManage.getInfoPreget(mInfoid);
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_INFOPREGET, postData, new infoDetailParser(),mIsNeddCache, CACHE_NAME,
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
					
					if(mInfoModule.getResult()){
						mTextViewBuytips.setText(mInfoModule.getBuytips());
						mTextViewShopname.setText(mInfoModule.getShopname());
						mTextViewInfotitle.setText(mInfoModule.getTitle());
						mTextViewInfodes.setText(mInfoModule.getDesc());
						String timestr= mContext.getString(R.string.msgusetime);
						timestr = String.format(timestr,ContantsUtils.formatData(mInfoModule.getInfobegindate()) + "",ContantsUtils.formatData(mInfoModule.getInfoenddate()) + "");
						mTextViewTime.setText(timestr);
						mTextViewShowtips.setText(mInfoModule.getShowtips());
						mButtonDownload.setVisibility(View.VISIBLE);
						
						if(mInfoModule.getShowtype().equals("2")){
							mEditTextNum.setVisibility(View.VISIBLE);
						}
					}else{
						Toast.makeText(mContext, ErrorCodeUtils.getErrorStr(mContext, mInfoModule.getErrorcode()), Toast.LENGTH_LONG).show();
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
	
	private void requestBuy(){
		byte[] postData = JsonRequestManage.getBuyInfo(mInfoid,mEditTextNum.getText().toString());
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_BUYINFO, postData, new ResultParser(),false, "buyinfo",
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mHandlerBuyinfo.sendMessage(msg);
							Log.i("lch", "请求成功");
						} else {
							mHandlerBuyinfo.sendEmptyMessage(LOAD_FAILE);
							Log.i("lch", "请求失败");
						}
					}
				});
	}
	
	private Handler mHandlerBuyinfo = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				//请求商家列表成功
				case LOAD_SUCCESS :
					RequestResultModule rrModule = (RequestResultModule)msg.obj;
					if(rrModule.result){
//						Toast.makeText(mContext, mContext.getString(R.string.getinfosmssucceed), 0).show();
						Intent i = new Intent(mContext, GetInfoSucceedAcitvity.class);
						startActivity(i);
					}
					finish();
					break;
				
				//请求商家列表失败
				case LOAD_FAILE :
					Toast.makeText(mContext, "请求资讯短信失败", 0).show();
					break;
					
				default :
					break;
			}
		}

	};
}
