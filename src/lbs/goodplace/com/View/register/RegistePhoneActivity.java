package lbs.goodplace.com.View.register;

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
import android.widget.Toast;
import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.main.GoodPlaceApp;
import lbs.goodplace.com.View.main.MainActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.manage.util.ErrorCodeUtils;
import lbs.goodplace.com.obj.GatewayListModule;
import lbs.goodplace.com.obj.GatewayModule;
import lbs.goodplace.com.obj.RequestResultModule;
import lbs.goodplace.com.obj.parser.ResultParser;
import lbs.goodplace.com.obj.parser.SessionParser;

/**
 * 手机号注册
 * @author Administrator
 *
 */
public class RegistePhoneActivity extends ModuleActivity{
	public static final int LOAD_SUCCESS = 0;
	public static final int LOAD_FAILE = 1;
	
	//控件
	private Context mContext;
	private Button mButtonRegisteMail;
	private Button mButtonGetcode;
	private Button mButtonRegiste;
	private EditText mEditTextPhonenum;
	private EditText mEditTextCode;
	private EditText mEditTextUsername;
	private EditText mEditTextPw;
	private EditText mEditTextPw2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.registerphone_view, mLayout_body);
		
		mContext = RegistePhoneActivity.this;
		mButtonRegisteMail = (Button)findViewById(R.id.Btn_mail);
		mButtonRegisteMail.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, RegisteMailActivity.class);
				startActivity(i);
				finish();
			}
		});
		
		mEditTextPhonenum = (EditText)findViewById(R.id.registerphone_phonenum);
		mEditTextCode = (EditText)findViewById(R.id.registerphone_code);
		mEditTextUsername = (EditText)findViewById(R.id.registerphone_username);
		mEditTextPw = (EditText)findViewById(R.id.registerphone_pw);
		mEditTextPw2 = (EditText)findViewById(R.id.registerphone_pw2);
		mButtonGetcode = (Button)findViewById(R.id.Button_getcode);
		mButtonGetcode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getMobileCode();
			}
		});
		mButtonRegiste = (Button)findViewById(R.id.Button_registe);
		mButtonRegiste.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getRegiste();
			}
		});
	}

	/**
	 * 取验证码
	 */
	private void getMobileCode(){
		long temptime = System.currentTimeMillis() - GoodPlaceApp.GET_REGISTRCODE_TIME ;
		if(temptime > 30000){
			GoodPlaceApp.GET_REGISTRCODE_TIME = System.currentTimeMillis();
		}else{
			long seconds = (30000 - temptime) / 1000;
			
			String msg = getString(R.string.getcodemsg1);
			msg = String.format(msg, seconds);
			Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
			return;
		}
		
		
		byte[] postData = JsonRequestManage.getMobileCode(mEditTextPhonenum.getText().toString());
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_MOBILECODE, postData, new ResultParser(), false, "getmobilecode",
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mHandler.sendMessage(msg);// .sendMessageDelayed(msg, 2000);
						} else {
							mHandler.sendEmptyMessage(LOAD_SUCCESS);
						}
					}
			
				});
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			HideLoadingDialog();
			
			switch (msg.what) {
				case LOAD_SUCCESS :
					if(msg.obj!=null){
						RequestResultModule rrm = (RequestResultModule)msg.obj;
						if(!rrm.getResult()){
							Toast.makeText(mContext, ErrorCodeUtils.getErrorStr(mContext, rrm.getErrorcode()), Toast.LENGTH_LONG).show();
						}else{
							Toast.makeText(mContext, getString(R.string.getcodemsg2), Toast.LENGTH_LONG).show();
						}
						Log.i("zjj", "getMobileCode success" + rrm.getResult());
					}
					break;
					
				default :
					Log.i("zjj", "getMobileCode faile");
					break;
			}
		}

	};
	
	/**
	 * 手机注册
	 */
	private void getRegiste(){
		if(!mEditTextPw.getText().toString().equals(mEditTextPw2.getText().toString())){
			Toast.makeText(mContext, getString(R.string.pwnotfit), Toast.LENGTH_SHORT).show();
			return;
		}
		
		byte[] postData = JsonRequestManage.getResgiterPhone(
				mEditTextCode.getText().toString(),
				mEditTextPhonenum.getText().toString(),
				mEditTextUsername.getText().toString(),
				mEditTextPw.getText().toString());
		
		
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_REGISTEPHONE, postData, new ResultParser(), false, "getregistephone",
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mRegisteHandler.sendMessage(msg);// .sendMessageDelayed(msg, 2000);
						} else {
							mRegisteHandler.sendEmptyMessage(-1);
						}
					}
			
				});
	}
	
	private Handler mRegisteHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case LOAD_SUCCESS :
					if(msg.obj!=null){
						RequestResultModule rrm = (RequestResultModule)msg.obj;
						if(rrm.result){
							Toast.makeText(mContext, mContext.getResources().getString(R.string.registesuccess), Toast.LENGTH_LONG).show();
							Log.i("zjj", "getRegiste success" + rrm.getResult());
						}else{
							Toast.makeText(mContext, ErrorCodeUtils.getErrorStr(mContext, rrm.getErrorcode()), Toast.LENGTH_LONG).show();
						}
					}
					break;
					
				default :
					HideLoadingDialog();
					if(msg.obj!=null){
						Toast.makeText(mContext, getString(R.string.registefaile), Toast.LENGTH_LONG).show();
						Log.i("zjj", "getMobileCode faile");
					}
					break;
			}
		}

	};
	
}
