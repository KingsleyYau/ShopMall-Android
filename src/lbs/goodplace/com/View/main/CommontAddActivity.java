package lbs.goodplace.com.View.main;

import lbs.goodplace.com.R;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.manage.util.ErrorCodeUtils;
import lbs.goodplace.com.obj.RequestResultModule;
import lbs.goodplace.com.obj.parser.ResultParser;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 用户评论界面
 * @author shazhuzhu
 *
 */
public class CommontAddActivity extends Activity implements OnClickListener{
	private static int CONTENT_MAX = 70;
	
	public Context mContext;
	public int mShopId;
	public Button mBtnBack;
	
	public RatingBar mScore; //总体评分
	public RatingBar mScore1; //口味
	public RatingBar mScore2; //环境
	public RatingBar mScore3; //服务
	public RatingBar mScore4; //其他评分
	
	public EditText mPriceEditText; //价格
	
	public TextView mTextCount; //剩下输入字符
	public EditText mMessageEditText; //评论
	
	public ImageView mBtnOk; //提交按钮
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.comment_add_activity);
		mContext = this;
		

		mShopId = getIntent().getIntExtra(GoodPlaceContants.KEY_SHOP_ID, -1);
		if (mShopId == -1) {
			finish();
			return;
		}
		
		initView();
	}

	public void initView(){
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		
		mScore = (RatingBar)findViewById(R.id.score);
		mScore1 = (RatingBar)findViewById(R.id.score1);
		mScore2 = (RatingBar)findViewById(R.id.score2);
		mScore3 = (RatingBar)findViewById(R.id.score3);
		mScore4 = (RatingBar)findViewById(R.id.score4);

		mPriceEditText = (EditText)findViewById(R.id.priceEditText);
		mTextCount = (TextView) findViewById(R.id.text_count);
		mMessageEditText = (EditText)findViewById(R.id.messageEditText);
		mMessageEditText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() <= CONTENT_MAX) {
					mTextCount.setText((CONTENT_MAX - s.length()) + "");
				}
			}
		});

		mBtnOk = (ImageView)findViewById(R.id.btn_ok);
		mBtnOk.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		if (v == mBtnBack) {
			finish();
		}
		
		else if (v == mBtnOk) {
			String priceString = "0";
//			String priceString = mPriceEditText.getText().toString(); 
//			if (TextUtils.isEmpty(priceString)) {
//				Toast.makeText(mContext, "请输入人均价格！", Toast.LENGTH_SHORT).show();
//				return;
//			}
			
			String messageString = mMessageEditText.getText().toString();
			if (TextUtils.isEmpty(messageString)) {
				Toast.makeText(mContext, "请输入留言信息！", Toast.LENGTH_SHORT).show();
				return;
			}
				
			if (messageString.length() < 6) {
				Toast.makeText(mContext, "输入内容不得小于6个字符！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			int score = (int)mScore.getRating() * Contants.SCORE_ONE_SIZE; //1个星星20分;
			int score1 = (int)mScore1.getRating() * Contants.SCORE_ONE_SIZE; //1个星星20分;
			int score2 = (int)mScore2.getRating() * Contants.SCORE_ONE_SIZE; //1个星星20分;
			int score3 = (int)mScore3.getRating() * Contants.SCORE_ONE_SIZE; //1个星星20分;
			int score4 = (int)mScore4.getRating() * Contants.SCORE_ONE_SIZE; //1个星星20分;
			if (score == 0 ) {
				Toast.makeText(mContext, "请输入总体评分！", Toast.LENGTH_SHORT).show();
				return;
			}
			if (score1 == 0 ) {
				Toast.makeText(mContext, "请输入口味评分！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (score2 == 0 ) {
				Toast.makeText(mContext, "请输入环境评分！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (score3 == 0 ) {
				Toast.makeText(mContext, "请输入服务评分！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (score4 == 0 ) {
				Toast.makeText(mContext, "请输入其它评分！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			requestSendCommont(score, score1, score2, score3, score4, priceString, messageString);
		}
	}
	
	/**
	 * <br>功能简述:上传信息
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	public void requestSendCommont(int score, int score1, int score2, int score3, int score4, String price, String message ) {
		byte[] postData = JsonRequestManage.getSendCommontPostData(mShopId, GoodPlaceContants.USERINFO.getId(), score, score1, score2, score3, score4, price, message);
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_USER_COMMONT, postData, new ResultParser(),
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null && object instanceof RequestResultModule) {
							RequestResultModule rm = (RequestResultModule)object;
							boolean isSuccess = rm.result;
							if (isSuccess) {
								// 还在子线程操作，handler是放回主线程处理
								Message msg = new Message();
								msg.obj = object;
								msg.what = Contants.LOAD_SUCCESS;
								mHandler.sendMessage(msg);
								Log.i("lch", "评论成功！");
								return;
							}else {
								Message msg = new Message();
								msg.arg1 = rm.getErrorcode();
								msg.what = Contants.LOAD_FALSE;
								mHandler.sendMessage(msg);
								Log.i("lch", "评论失败！");
							}
						} 
					}
				});
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				//收藏商家成功
				case Contants.LOAD_SUCCESS :
					Toast.makeText(mContext, "评论成功！", Toast.LENGTH_SHORT).show();
					CommontAddActivity.this.finish();
					break;
					
				//收藏商家失败
				case Contants.LOAD_FALSE :
					String errorstr = ErrorCodeUtils.getErrorStr(mContext,msg.arg1 );
					Toast.makeText(mContext, errorstr, Toast.LENGTH_SHORT).show();				
//					Toast.makeText(mContext, "评论失败！", Toast.LENGTH_SHORT).show();
					break;
					
				default :
					break;
			}
		}

	};
}
