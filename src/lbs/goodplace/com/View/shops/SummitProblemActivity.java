package lbs.goodplace.com.View.shops;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.msg.GetInfoSMSActivity;
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
import android.widget.ImageView;
import android.widget.Toast;

public class SummitProblemActivity extends ModuleActivity{
	public static final int LOAD_SUCCESS = 0;
	public static final int LOAD_FAILE = 1;

	//
	private Context mContext;
	private EditText mEditTextProblem;
	private ImageView mButtonSend;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.summitproblem_view, mLayout_body);
		
		mContext = SummitProblemActivity.this;
		
		mEditTextProblem = (EditText)findViewById(R.id.problemEditText);
		mButtonSend = (ImageView)findViewById(R.id.btn_ok);
		mButtonSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				requestProplem();
			}
		});
		
		setTitleTxt(getString(R.string.probleam));
	}
	
	private void requestProplem(){
		byte[] postData = JsonRequestManage.getSummintProblem(mEditTextProblem.getText().toString());
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_PROBLEM, postData, new ResultParser(),false, "geInfodproblem",
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
						Toast.makeText(mContext, mContext.getString(R.string.getprobleam), 0).show();
					}
					finish();
					break;
				
				//请求商家列表失败
				case LOAD_FAILE :
					Toast.makeText(mContext, "提交建议失败", 0).show();
					break;
					
				default :
					break;
			}
		}

	};
}
