package lbs.goodplace.com.View.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.contant.ImageUntil;
import lbs.goodplace.com.View.contant.Machine;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.manage.uploadpicmanage.FileUploader;
import lbs.goodplace.com.manage.uploadpicmanage.FormFile;
import lbs.goodplace.com.manage.util.ErrorCodeUtils;
import lbs.goodplace.com.obj.RequestResultModule;
import lbs.goodplace.com.obj.parser.ResultParser;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 签到留言
 * 
 * @author shazhuzhu
 * 
 */
public class LeaveMessageActivity extends Activity implements OnClickListener {
	public Context mContext;
	public Button mBtnBack;
	public EditText mMessageEditText;
	public TextView mTextCount;
	public ImageView mPhoneImage;
	public RatingBar mScoreRatingBar;
	public ImageView mOkBtn;
	

	private final static int REQUEST_CODE_CAMERA = 1;
	private final static int REQUEST_CODE_ALBUM = 2;
	private final static int REQUEST_CODE_ZOOM = 3;

	public int mShopId = -1;
	public int mPicType = -1;
	private final static int PIC_TYPE_CAMERA = 0;
	private final static int PIC_TYPE_GARRY = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.leave_message_activity);
		
		mShopId = getIntent().getIntExtra(GoodPlaceContants.KEY_SHOP_ID, -1);
		if (mShopId == -1) {
			finish();
			return;
		}
		
		initView();
	}

	public void initView() {
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
		
		mMessageEditText = (EditText) findViewById(R.id.message_edit);
		mTextCount = (TextView) findViewById(R.id.text_count);
		
		mScoreRatingBar = (RatingBar) findViewById(R.id.scoreRatingBar);
		
		mPhoneImage = (ImageView) findViewById(R.id.phone_image);
		mPhoneImage.setOnClickListener(this);
		
		mMessageEditText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() <= 140) {
					mTextCount.setText((140 - s.length()) + "");
				}
			}
		});
		
		mOkBtn = (ImageView)findViewById(R.id.btn_ok);
		mOkBtn.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		if (v == mBtnBack) {
			finish();
		}
		
		else if (v == mPhoneImage) {
			doPickPhotoAction();
		}
		
		//确定提交按钮
		else if (v == mOkBtn) {
			String messageString = mMessageEditText.getText().toString();
			if (TextUtils.isEmpty(messageString)) {
				Toast.makeText(mContext, "请输入留言信息！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			if (messageString.length() < 6) {
				Toast.makeText(mContext, "输入内容不得小于6个字符！", Toast.LENGTH_SHORT).show();
				return;
			}
			
			int score = (int)mScoreRatingBar.getRating() * Contants.SCORE_ONE_SIZE; //1个星星20分
			if (score == 0) {
				Toast.makeText(mContext, "请输入评分！", Toast.LENGTH_SHORT).show();
				return;
			}
			requestUserSign(score, messageString);
		}
	}

	private void doPickPhotoAction() {
		String camera = "拍照";
		String album = "相册";
		new AlertDialog.Builder(mContext)
				.setTitle("选择相片")
				.setItems(new String[] { camera, album },
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									startCamera();
								} else if (which == 1) {
									startAlbum();
								}
							}
						}).setNegativeButton("取消", null).show();
	}

	public void startCamera() {
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (Machine.isSDCardExist()) {
				File file = new File(Contants.Path.IMAGE_CACHE_PATH);
				if (!file.exists()) {
					file.mkdirs();
				}
				File picFile = new File(Contants.Path.CAMERA_SAVE_PATH);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
			}
			startActivityForResult(intent, REQUEST_CODE_CAMERA);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startAlbum() {
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			startActivityForResult(intent, REQUEST_CODE_ALBUM);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			// 拍照
			if (requestCode == REQUEST_CODE_CAMERA) {
				// 裁剪
				// Uri uri = Uri.fromFile(new
				// File(Contants.Path.IMAGE_CACHE_PATH, USERCAMERA_NAME));
				// startPhotoZoom(uri);

				ImageUntil.compressImage(Contants.Path.CAMERA_SAVE_PATH, Contants.Path.COMPRESS_SAVE_PATH);
				mPhoneImage.setImageBitmap(BitmapFactory.decodeFile(Contants.Path.COMPRESS_SAVE_PATH));
				mPicType = PIC_TYPE_CAMERA;
			}

			// 相冊
			else if (requestCode == REQUEST_CODE_ALBUM) {
				// if (data == null) {
				// return;
				// }
				// startPhotoZoom(data.getData());

				try {
					Uri originalUri = data.getData();// 得到图片的URI
					String[] imgs = { MediaStore.Images.Media.DATA };// 将图片URI转换成存储路径
					Cursor cursor = this.managedQuery(originalUri, imgs, null,null, null);
					int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String filePath = cursor.getString(index);
					ImageUntil.compressImage(filePath,Contants.Path.COMPRESS_SAVE_PATH);
					mPhoneImage.setImageBitmap(BitmapFactory.decodeFile(Contants.Path.COMPRESS_SAVE_PATH));
					mPicType = PIC_TYPE_GARRY;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// 图片压缩
			else if (requestCode == REQUEST_CODE_ZOOM) {
				if (data == null) {
					return;
				}
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap photo = extras.getParcelable("data");
					mPhoneImage.setImageBitmap(photo);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1); // aspectX aspectY 是宽高的比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 360);// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputY", 360);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, REQUEST_CODE_ZOOM);
	}
	
	
	
	/**
	 * 上传用户头像
	 */
	private void requestUserSign(int score, String messageString){		
		List formFiles = null;
		if (mPicType != -1) {
			//读取文件
			File file = new File(Contants.Path.COMPRESS_SAVE_PATH);
			
			FileInputStream fileStream = null;
			try {
				fileStream = new FileInputStream(file);
			} 
			catch (FileNotFoundException ex) {
				Toast.makeText(mContext, "读取图片文件失败，请重试!", Toast.LENGTH_SHORT).show();
			}

			formFiles = new ArrayList<FormFile>();	//<--存放要上传的文件(可多个)
			FormFile formFile = new FormFile(fileStream,file.getName(),"attachedimg","image/pjpeg");
			formFiles.add(formFile);
			
		}
		//请求参数
		Map<String, Object> postData = JsonRequestManage.getUserSignPostData(mShopId, GoodPlaceContants.USERINFO.getId(), score, messageString);
		
		//上传
		FileUploader fu = new FileUploader();
		fu.UploadPic(GoodPlaceContants.URL_USER_SIGN, formFiles, postData, mContext, new ResultParser(),
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
								Log.i("lch", "签到成功！");
								return;
							}else {
								Message msg = new Message();
								msg.arg1 = rm.getErrorcode();
								msg.what = Contants.LOAD_FALSE;
								mHandler.sendMessage(msg);
								Log.i("lch", "签到失败！");
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
					Toast.makeText(mContext, "签到成功！", Toast.LENGTH_SHORT).show();
					LeaveMessageActivity.this.finish();
					break;
					
				//收藏商家失败
				case Contants.LOAD_FALSE :
					String errorstr = ErrorCodeUtils.getErrorStr(mContext,msg.arg1 );
					Toast.makeText(mContext, errorstr, Toast.LENGTH_SHORT).show();
//					Toast.makeText(mContext, "签到失败！", Toast.LENGTH_SHORT).show();
					break;
					
				default :
					break;
			}
		}
	};
}