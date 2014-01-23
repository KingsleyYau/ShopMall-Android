package lbs.goodplace.com.View.login;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.manage.util.Encryption;
import lbs.goodplace.com.manage.util.ErrorCodeUtils;
import lbs.goodplace.com.manage.util.GlobalUtil;
import lbs.goodplace.com.obj.RequestResultModule;
import lbs.goodplace.com.obj.parser.ResultParser;
import android.content.Context;
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

public class EditUserinfoAcitivity extends ModuleActivity{
	public static final int LOAD_SUCCESS = 0;
	public static final int LOAD_FAILE = 1;
	
	public Context mContext;
	
	private Button mButtonSummit;
	private EditText mEditTextOldpw;
	private EditText mEditTextNewpw;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.edituserinfo_view, mLayout_body);
		
		mContext = EditUserinfoAcitivity.this;
		
		mEditTextOldpw = (EditText)findViewById(R.id.Txtview_oldpw);
		mEditTextNewpw = (EditText)findViewById(R.id.Txtview_newpw);
		
		initTitilebar();
	}

	private void initTitilebar(){
//		LayoutParams p = new LayoutParams(MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size) , MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size));
		
		mButtonSummit = new Button(this);
//		mButtonSummit.setLayoutParams(p);
		mButtonSummit.setText(getResources().getString(R.string.edit));
//		mButtonSummit.setBackgroundResource(R.drawable.button_logout_selector);
		mButtonSummit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getEditPw();
			}
		});
		setTitleRightButton(mButtonSummit);
	}
	
	/**
	 * 修改密码
	 */
	private void getEditPw(){
		ShowLoadingDialog();
		
		byte[] postData = JsonRequestManage.getEditPw(
				Encryption.toMd5(mEditTextOldpw.getText().toString()) ,
				mEditTextNewpw.getText().toString());
		
		
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_CHANGEPW, postData, new ResultParser(), false, "getCHANGEPW",
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mEditpwHandler.sendMessage(msg);// .sendMessageDelayed(msg, 2000);
						} else {
							mEditpwHandler.sendEmptyMessage(LOAD_SUCCESS);
						}
					}
			
				});
	}
	
	private Handler mEditpwHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			HideLoadingDialog();
			
			switch (msg.what) {
				case LOAD_SUCCESS :
					if(msg.obj!=null){
						RequestResultModule rrm = (RequestResultModule)msg.obj;
						if(rrm.getResult()){
							Log.i("zjj", "getEditPw success" + rrm.getResult());
							SaveNewpw();
							
							Toast.makeText(mContext, mContext.getResources().getString(R.string.changepwsuccess), Toast.LENGTH_LONG).show();
							//要开线程关闭，否则会报错
							mButtonSummit.postDelayed(new Runnable() {
								
								@Override
								public void run() {
//									// TODO Auto-generated method stub
									finish();
								}
							}, 300);
							
							
						}else{
							Toast.makeText(mContext, ErrorCodeUtils.getErrorStr(mContext, rrm.getErrorcode()), Toast.LENGTH_LONG).show();
						}
					}
					break;
					
				default :
					
					Toast.makeText(mContext, mContext.getResources().getString(R.string.changepwfaile), Toast.LENGTH_LONG).show();
					Log.i("zjj", "getEditPw faile");
					break;
			}
		}

	};
	
	/**
	 * 登录成功保存帐号密码
	 */
	private void SaveNewpw(){
		SharedPreferences userInfo = getSharedPreferences(GlobalUtil.SHAREDPERFERENCES_NAME_LOGIN, 0);  
        userInfo.edit().putString(GlobalUtil.SHAREDPERFERENCES_KEY_PW, mEditTextNewpw.getText().toString()).commit();  
	}
}
