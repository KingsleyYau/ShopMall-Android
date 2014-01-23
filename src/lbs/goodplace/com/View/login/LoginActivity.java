package lbs.goodplace.com.View.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import lbs.goodplace.com.View.register.RegistePhoneActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.manage.util.ErrorCodeUtils;
import lbs.goodplace.com.manage.util.GlobalUtil;
import lbs.goodplace.com.manage.util.LoginUtils;
import lbs.goodplace.com.obj.LoginModule;
import lbs.goodplace.com.obj.RequestResultModule;
import lbs.goodplace.com.obj.UserInfoModule;
import lbs.goodplace.com.obj.parser.LoginParser;
import lbs.goodplace.com.obj.parser.ResultParser;

public class LoginActivity extends ModuleActivity{
	public static final int LOAD_SUCCESS = 0;
	public static final int LOAD_FAILE = 1;
	
	public Context mContext;
	
	private Button mButtonRegister;
	private Button mButtonLogin;
	private EditText mEditTextUsername;
	private EditText mEditTextPw;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.login_view, mLayout_body);
		
		mContext = LoginActivity.this;
		
		mEditTextUsername = (EditText)findViewById(R.id.Txtview_username);
		mEditTextPw = (EditText)findViewById(R.id.Txtview_password);
		mButtonLogin = (Button)findViewById(R.id.Button_login);
		mButtonLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getLogin();
			}
		});
		
		initTitlebar();
		LoadUserinfo();
	}
	
	private void initTitlebar(){
		mButtonRegister = new Button(mContext);
		mButtonRegister.setText(R.string.register);
		mButtonRegister.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, RegistePhoneActivity.class);
				startActivity(i);
			}
		});
		
		
		setTitleRightButton(mButtonRegister);
	}
	
	/**
	 * 登录
	 */
	private void getLogin(){
		LoginUtils.Login(mContext,
				mEditTextUsername.getText().toString(),
				mEditTextPw.getText().toString(),
				mHandler
				);
	}
	
	private Handler mHandler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			HideLoadingDialog();
			
			switch (msg.what) {
				case LOAD_SUCCESS :
					if(msg.obj!=null){
//						UserInfoModule userinfo = (UserInfoModule)msg.obj;
//						GoodPlaceContants.USERINFO = userinfo;
						
						LoginModule loginmodule = (LoginModule)msg.obj;
						if(loginmodule.mRequestResult.getResult()){
							
							Toast.makeText(mContext, mContext.getResources().getString(R.string.loginsuccess), Toast.LENGTH_LONG).show();
							SaveUserinfo();
							//要开线程关闭，否则会报错
							mButtonLogin.postDelayed(new Runnable() {
								
								@Override
								public void run() {
//									// TODO Auto-generated method stub
									finish();
								}
							}, 300);
						}else{
							Toast.makeText(mContext, ErrorCodeUtils.getErrorStr(mContext, loginmodule.mRequestResult.getErrorcode()), Toast.LENGTH_LONG).show();
//							Toast.makeText(mContext, loginmodule.mRequestResult.getErrorcode() + "", Toast.LENGTH_LONG).show();
						}
					}
					break;
					
				default :
					if(msg.obj!=null){
						Toast.makeText(mContext, getString(R.string.loginfaile), Toast.LENGTH_LONG).show();
						Log.i("zjj", "getMobileCode faile");
					}
					break;
			}
		}

	};
	
	/**
	 * 登录成功保存帐号密码
	 */
	private void SaveUserinfo(){
		SharedPreferences userInfo = getSharedPreferences(GlobalUtil.SHAREDPERFERENCES_NAME_LOGIN, 0);  
        userInfo.edit().putString(GlobalUtil.SHAREDPERFERENCES_KEY_USERNAME, mEditTextUsername.getText().toString()).commit();  
        userInfo.edit().putString(GlobalUtil.SHAREDPERFERENCES_KEY_PW, mEditTextPw.getText().toString()).commit();  
	}
	
	/**
	 * 取帐号密码
	 */
	private void LoadUserinfo(){
		SharedPreferences userInfo = getSharedPreferences(GlobalUtil.SHAREDPERFERENCES_NAME_LOGIN, 0);  
        String username = userInfo.getString(GlobalUtil.SHAREDPERFERENCES_KEY_USERNAME, "");  
        String pass = userInfo.getString(GlobalUtil.SHAREDPERFERENCES_KEY_PW, "");  
        
        mEditTextUsername.setText(username);
        mEditTextPw.setText(pass);
	}
}
