package lbs.goodplace.com.View.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lbs.goodplace.com.R;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.manage.uploadpicmanage.FileUploader;
import lbs.goodplace.com.manage.uploadpicmanage.FormFile;
import lbs.goodplace.com.manage.util.ErrorCodeUtils;
import lbs.goodplace.com.obj.RequestResultModule;
import lbs.goodplace.com.obj.ShopProductInfo;
import lbs.goodplace.com.obj.parser.ResultParser;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

/**
 * 上传图片
 * 
 * @author shazhuzhu
 * 
 */
public class ShopUpLoadImageActivity extends Activity implements OnClickListener {
	public Context mContext;
	public Button mBtnBack;
	public ImageView mLoadImage; // 上传图片
	public Button mBtnProduct; // 产品按钮
	public Button mBtnAmbience; // 氛围按钮
	public Button mBtnOhter; // 其他按钮
	public Button mBtnSelectProduct; //选择菜名按钮

	public EditText mNameEdit; // 名称
	public RatingBar mSorce; // 评分
	public EditText mPriceEditT; // 价格

	public LinearLayout mPriceLayout;
	
	public Button mBtnLoad; // 上传按钮
	
	public static final String TYPE_PRODUCE = "产品";
	public static final String TYPE_AMBIENCE = "氛围";
	public static final String TYPE_OTHER = "其它";
	
	public String mType = TYPE_PRODUCE;

	public int mShopId = -1;
	
	public static final int SELECT_PRODUCT_NAME = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_load_image_view);
		mContext = this;
		
		mShopId = getIntent().getIntExtra(GoodPlaceContants.KEY_SHOP_ID, -1);
		if (mShopId == -1) {
			finish();
			return;
		}
		
		initImageView();
		initView();
	}
	
	public void initImageView(){
		mLoadImage = (ImageView) findViewById(R.id.load_image);
		String imagePathString = Contants.Path.COMPRESS_SAVE_PATH;
		File file = new File(imagePathString);
		if (file.exists()) {
			mLoadImage.setImageBitmap(BitmapFactory.decodeFile(imagePathString));
		}
	}

	public void initView() {
		mBtnBack = (Button) findViewById(R.id.btn_back);
		
		mBtnProduct = (Button) findViewById(R.id.btn_product);
		mBtnProduct.setBackgroundResource(R.drawable.btn_submit_pressed);
		
		mBtnAmbience = (Button) findViewById(R.id.btn_ambience);
		mBtnOhter = (Button) findViewById(R.id.btn_other);
		mNameEdit = (EditText) findViewById(R.id.name_edite);
		mSorce = (RatingBar) findViewById(R.id.score);
		mPriceEditT = (EditText) findViewById(R.id.price_edit);
		mBtnLoad = (Button) findViewById(R.id.btn_load);
		
		mBtnSelectProduct = (Button) findViewById(R.id.select_product);
		
		mBtnBack.setOnClickListener(this);
		mBtnProduct.setOnClickListener(this);
		mBtnAmbience.setOnClickListener(this);
		mBtnOhter.setOnClickListener(this);
		mBtnLoad.setOnClickListener(this);
		mBtnSelectProduct.setOnClickListener(this);
		
		mPriceLayout = (LinearLayout) findViewById(R.id.price_layout);

	}


	@Override
	public void onClick(View v) {
		if (v == mBtnBack) {
			finish();
		}
		
		else if (v == mBtnProduct) {
			mType = TYPE_PRODUCE;
			mPriceLayout.setVisibility(View.VISIBLE);
			mBtnProduct.setBackgroundResource(R.drawable.btn_submit_pressed);
			mBtnAmbience.setBackgroundResource(R.drawable.button_shop_selector);
			mBtnOhter.setBackgroundResource(R.drawable.button_shop_selector);
			mBtnSelectProduct.setVisibility(View.VISIBLE);
			mNameEdit.setText("");
			mNameEdit.setEnabled(false);
		}

		else if (v == mBtnAmbience) {
			mType = TYPE_AMBIENCE;
			mPriceLayout.setVisibility(View.GONE);
			mBtnProduct.setBackgroundResource(R.drawable.button_shop_selector);
			mBtnAmbience.setBackgroundResource(R.drawable.btn_submit_pressed);
			mBtnOhter.setBackgroundResource(R.drawable.button_shop_selector);
			mBtnSelectProduct.setVisibility(View.GONE);
			mNameEdit.setText("");
			mNameEdit.setEnabled(true);
		}

		else if (v == mBtnOhter) {
			mType = TYPE_OTHER;
			mPriceLayout.setVisibility(View.GONE);
			mBtnProduct.setBackgroundResource(R.drawable.button_shop_selector);
			mBtnAmbience.setBackgroundResource(R.drawable.button_shop_selector);
			mBtnOhter.setBackgroundResource(R.drawable.btn_submit_pressed);
			mBtnSelectProduct.setVisibility(View.GONE);
			mNameEdit.setText("");
			mNameEdit.setEnabled(true);
		}

		//打开菜单列表
		else if (v == mBtnSelectProduct){
			Intent intent = new Intent(this, ShopProductListActivity.class);
			intent.putExtra(GoodPlaceContants.KEY_SHOP_ID, mShopId);
			startActivityForResult(intent, SELECT_PRODUCT_NAME);
		}
		
		//上传按钮
		else if (v == mBtnLoad) {
			String nameString = mNameEdit.getText().toString();
			if (TextUtils.isEmpty(nameString)) {
				Toast.makeText(mContext, "请输入图片名称！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			int sorce = (int)mSorce.getRating() * Contants.SCORE_ONE_SIZE;
			if (sorce <= 0) {
				Toast.makeText(mContext, "请输入评分！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			String priceString = mPriceEditT.getText().toString();
			if (TextUtils.isEmpty(priceString)) {
				priceString = "0";
				if (mType == TYPE_PRODUCE) {
					Toast.makeText(mContext, "请输入价格！", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			
			requestSendImage(mType, nameString, sorce, priceString);
		}
	}

	
	
	/**
	 * 上传用户头像
	 */
	private void requestSendImage(String imgType, String imageName, int score, String price){		
		//读取文件
		File file = new File(Contants.Path.COMPRESS_SAVE_PATH);
		
		FileInputStream fileStream = null;
		try {
			fileStream = new FileInputStream(file);
		} 
		catch (FileNotFoundException ex) {
			Toast.makeText(mContext, "读取图片文件失败，请重试!", Toast.LENGTH_SHORT).show();
		}

		List formFiles = new ArrayList<FormFile>();	//<--存放要上传的文件(可多个)
		FormFile formFile = new FormFile(fileStream,file.getName(),"shopimg","image/pjpeg");
		formFiles.add(formFile);
		//请求参数
		Map<String, Object> postData = JsonRequestManage.getSendShopImagePostData(mShopId, GoodPlaceContants.USERINFO.getId(), imgType, imageName, score, price);
		
		//上传
		FileUploader fu = new FileUploader();
		fu.UploadPic(GoodPlaceContants.URL_SEND_IMAGE, formFiles, postData, mContext, new ResultParser(),
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
								Log.i("lch", "上传成功！");
								return;
							}else{
								Message msg = new Message();
								msg.arg1 = rm.getErrorcode();
								msg.what = Contants.LOAD_FALSE;
								mHandler.sendMessage(msg);
								Log.i("lch", "上传失败！");
								
							}
						} 
						
					}
			
				});
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				//成功
				case Contants.LOAD_SUCCESS :
					Toast.makeText(mContext, "上传成功！", Toast.LENGTH_SHORT).show();
					ShopUpLoadImageActivity.this.finish();
					break;
					
				//失败
				case Contants.LOAD_FALSE :
					String errorstr = ErrorCodeUtils.getErrorStr(mContext,msg.arg1 );
					Toast.makeText(mContext, errorstr, Toast.LENGTH_SHORT).show();
//					Toast.makeText(mContext, "上传失败！", Toast.LENGTH_SHORT).show();
					break;
					
				default :
					break;
			}
		}
	};
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SELECT_PRODUCT_NAME && resultCode == RESULT_OK) {
			Object object = data.getSerializableExtra(Contants.SHOP_PRODUCT_NAME);
			if (object != null && object instanceof ShopProductInfo) {
				ShopProductInfo info = (ShopProductInfo)object;
				mNameEdit.setText(info.mProname);
			}
		}
	};
}
