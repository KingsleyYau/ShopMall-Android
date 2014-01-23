package lbs.goodplace.com.View.msg;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.obj.RequestResultModule;
import lbs.goodplace.com.obj.parser.ResultParser;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GetInfoSMSActivity  extends ModuleActivity{
	public static String KEY_INFOID = "KEY_INFOID";
	public static final int LOAD_SUCCESS = 0;
	public static final int LOAD_FAILE = 1;

	private String mInfoid;
	//
	private Context mContext;
	private EditText mEditTextNum;
	private Button mButtonSend;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.getinfosms_view, mLayout_body);
		
		mContext = GetInfoSMSActivity.this;
		
		Bundle extras = getIntent().getExtras();
        if(extras != null){
        	if(extras.containsKey(KEY_INFOID)){
        		mInfoid = extras.getString(KEY_INFOID);
    		}
        }
		
		mEditTextNum = (EditText)findViewById(R.id.Txtview_username);
		
		initTitleBar();
	}
	
	/**
	 * 初始化Titlebar
	 */
	private void initTitleBar(){
		setTitleText(R.string.getmsgdetail);
		
//		LayoutParams p = new LayoutParams(MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size) , MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size));
		
		mButtonSend = new Button(this);
//		mButtonSend.setLayoutParams(p);
		mButtonSend.setText(getString(R.string.download));
		mButtonSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				requestDetail();
			}
		});
		setTitleRightButton(mButtonSend);
	}
	
	private void requestDetail(){
		byte[] postData = JsonRequestManage.getInfoSMS(mInfoid,mEditTextNum.getText().toString());
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_INFOSMS, postData, new ResultParser(),false, "geInfodsms",
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
					RequestResultModule rrModule = (RequestResultModule)msg.obj;
					if(rrModule.result){
						Toast.makeText(mContext, mContext.getString(R.string.getinfosmssucceed), 0).show();
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
