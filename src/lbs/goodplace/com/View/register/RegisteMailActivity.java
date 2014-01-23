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
import lbs.goodplace.com.View.main.MainActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.manage.util.ErrorCodeUtils;
import lbs.goodplace.com.obj.RequestResultModule;
import lbs.goodplace.com.obj.parser.ResultParser;

/**
 * 邮箱注册
 * @author Administrator
 *
 */
public class RegisteMailActivity extends ModuleActivity{
	public static final int LOAD_SUCCESS = 0;
	public static final int LOAD_FAILE = 1;
	
	private Context mContext;
	//
	private Button mButtonRegistePhone;
	private EditText mEditTextMail;
	private EditText mEditTextUsername;
	private EditText mEditTextPw;
	private EditText mEditTextPw2;
	private Button mButtonRegiste;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.registermail_view, mLayout_body);
		
		mContext = RegisteMailActivity.this;
		mButtonRegistePhone = (Button)findViewById(R.id.Btn_phone);
		mButtonRegistePhone.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, RegistePhoneActivity.class);
				startActivity(i);
				finish();
			}
		});
		
		mEditTextMail = (EditText)findViewById(R.id.registermail_mail);
		mEditTextUsername = (EditText)findViewById(R.id.registermail_username);
		mEditTextPw = (EditText)findViewById(R.id.registermail_pw1);
		mEditTextPw2 = (EditText)findViewById(R.id.registermail_pw2);
		mButtonRegiste = (Button)findViewById(R.id.Button_summit);
		mButtonRegiste.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getRegiste();
			}
		});
	}
	
	/**
	 * 注册
	 */
	private void getRegiste(){
		if(mEditTextMail.getText().toString().indexOf("@")<0 ||
				mEditTextMail.getText().toString().indexOf(".com")<0){
			Toast.makeText(mContext, getString(R.string.notmail), Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(!mEditTextPw.getText().toString().equals(mEditTextPw2.getText().toString())){
			Toast.makeText(mContext, getString(R.string.pwnotfit), Toast.LENGTH_SHORT).show();
			return;
		}
		
		byte[] postData = JsonRequestManage.getResgiterMail(
				mEditTextMail.getText().toString(),
				mEditTextUsername.getText().toString(),
				mEditTextPw.getText().toString());
		
		
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_REGISTEMAIL, postData, new ResultParser(), false, "getregistemail",
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
