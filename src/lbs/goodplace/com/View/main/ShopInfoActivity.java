package lbs.goodplace.com.View.main;

import java.io.File;
import java.util.ArrayList;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.contant.ImageUntil;
import lbs.goodplace.com.View.contant.Machine;
import lbs.goodplace.com.View.login.LoginActivity;
import lbs.goodplace.com.View.msg.MsgDetailActivity;
import lbs.goodplace.com.View.shops.MapActivity;
import lbs.goodplace.com.View.shops.SummitProblemActivity;
import lbs.goodplace.com.component.ScoreStar;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.NetState;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager.AsyncImageLoadedCallBack;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.manage.util.ContantsUtils;
import lbs.goodplace.com.manage.util.FileUtil;
import lbs.goodplace.com.manage.util.GlobalUtil;
import lbs.goodplace.com.manage.util.MyMothod;
import lbs.goodplace.com.obj.Commentsituation;
import lbs.goodplace.com.obj.Recommendtags;
import lbs.goodplace.com.obj.RequestResultModule;
import lbs.goodplace.com.obj.ShopModule;
import lbs.goodplace.com.obj.Signsituation;
import lbs.goodplace.com.obj.parser.ResultParser;
import lbs.goodplace.com.obj.parser.ShopInfoParser;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShopInfoActivity extends ModuleActivity implements OnClickListener{
	public static String KEY_ISFORMPUSH = "KEY_ISFORMPUSH";
	
	public Context mContext;
	private AsyncImageManager mImgManager = null;
	
	public Button mBtnBack; //返回按钮
	public ImageView mBtnForward; //转发按钮
	public Button mBtnFav; //喜欢按钮
	public Button mBtnShare; //喜欢按钮
	
	public ImageView mShopImageView; //商店图片
	public TextView mShopName; //商店名称
	public TextView mOnePersonPrice; //人均价格
	
	public ScoreStar mScoreStar; //商家评分
	
	public TextView mShopAddress; //商店地址
	public TextView mShopPhone; //商店电话
	
	public TextView mFriendRecomm; //网友推荐
	
	public TextView mReviewsCount; //点评数量
	public TextView mReviewsName; //点评用户名称
	public ScoreStar mReviewsSorce; //点评内容
	public TextView mReviewsContent; //点评内容
	public TextView mReviewsTime; //点评时间
	
	public TextView mSignCount; //签到数量
	public TextView mSignName; //签到用户名称
	public TextView mSignContent; //签到内容
	public TextView mSignTime; //签到时间
	
	public TextView mShopmsTxt; //商户资讯
	
	public RelativeLayout mShopAddressLayout; //地址布局
	public RelativeLayout mShopPhoneLayout; //电话布局
	public LinearLayout mFriendRecommLayout; //推荐布局
	public RelativeLayout mReviewsLayout; //评论布局
	public RelativeLayout mSignLayout; //签到布局
	public RelativeLayout mTrafficLayout; //交通布局
	public RelativeLayout mBranchLayout; //其他分店
	public RelativeLayout mShopMsgLayout; //商户资讯
	
	public LinearLayout mUpLoadImageLayout; //上传图片
	public LinearLayout mLeaveMessageLayout; //留言签到
	public LinearLayout mCommentAddLayout; //添加点评
	private LinearLayout mSendProblemLayout; //报错
	
	private final static int REQUEST_CODE_CAMERA = 1;
	private final static int REQUEST_CODE_ALBUM = 2;
	private final static int REQUEST_CODE_ZOOM = 3;
	
	public static final int LOAD_SHOP_INFO_SUCCESS = 0; //请求成功
	public static final int LOAD_SHOP_INFO_FAILE = 1; //请求排行版列表失败
	
	public static final int LOAD_SHOP_FAV_SUCCESS = 2; //收藏成功
	public static final int LOAD_SHOP_FAV_FAILE = 3; //收藏失败
	
	public int mShopId = -1;
	public int mShopCityId = -1;

	public ShopModule mShopModule;
	//缓存
	private String CACHE_NAME = "ShopInfoActivity";
	private boolean mIsNeddCache = true;	//是否需要读取缓存
	private NetState mNetState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.shop_info_activity, mLayout_body);
		
		mContext = this;
//		setContentView(R.layout.shop_info_activity);
		mImgManager = AsyncImageManager.getInstance();
		mNetState = new NetState(mContext);
		
//		ShopModule shopModule = (lbs.goodplace.com.obj.ShopModule) getIntent().getSerializableExtra(GoodPlaceContants.URL_SHOP_IONFO);
//		if (shopModule == null) {
//			return;
//		}
		
		Bundle extras = getIntent().getExtras();
        if(extras != null){
    		if(extras.containsKey(KEY_ISFORMPUSH)){
    			mShopId = Integer.valueOf(extras.getString(KEY_ISFORMPUSH));
    			
    			if(!GoodPlaceApp.mIsRuning){
    				Intent i = new Intent(mContext, MainActivity.class);
        			i.putExtra(MainActivity.KEY_PUSH2SHOP_ID, this.mShopId);
        			mContext.startActivity(i);
        			
        			finish();
    			}
    			
    		}
        	if(extras.containsKey(GoodPlaceContants.KEY_SHOP_ID)){
        		mShopId = extras.getInt(GoodPlaceContants.KEY_SHOP_ID,-1);
    		}
        	if(extras.containsKey(GoodPlaceContants.KEY_SHOP_CITYID)){
        		mShopCityId = extras.getInt(GoodPlaceContants.KEY_SHOP_CITYID,-1);
    		}
        }
        
        Log.i("zjj", "-----------商店详细-------------" + mShopId);
        
		if (mShopId == -1) {
			finish();
			return;
		}
		
		initView();
		requestShopInfo(mShopId);
	}
	
	public void initView(){
//		mBtnBack = (Button)findViewById(R.id.btn_back);
//		mBtnForward = (ImageView)findViewById(R.id.btn_forward);
//		mBtnFav = (ImageView)findViewById(R.id.btn_fav);
//		mBtnBack.setOnClickListener(this);
//		mBtnForward.setOnClickListener(this);
//		mBtnFav.setOnClickListener(this);
		
		
		mShopImageView  = (ImageView)findViewById(R.id.shop_icon);
		mShopImageView.setOnClickListener(this);
		mShopName  = (TextView)findViewById(R.id.shop_name);
		mOnePersonPrice = (TextView)findViewById(R.id.one_person_price);
		mScoreStar = (ScoreStar)findViewById(R.id.scoreStar1);
		mShopAddress = (TextView)findViewById(R.id.shop_address);
		mShopPhone = (TextView)findViewById(R.id.shop_phone);
		mFriendRecomm  = (TextView)findViewById(R.id.friend_recomm);
		
		//点评
		mReviewsCount = (TextView)findViewById(R.id.ReviewsCount);
		mReviewsName = (TextView)findViewById(R.id.ReviewsName);
		mReviewsSorce = (ScoreStar)findViewById(R.id.scoreStar2);
		mReviewsContent = (TextView)findViewById(R.id.ReviewsContent);
		mReviewsTime = (TextView)findViewById(R.id.ReviewsTime);
		
		//点评
		mSignCount = (TextView)findViewById(R.id.SignCount);
		mSignName = (TextView)findViewById(R.id.SignName);
		mSignContent = (TextView)findViewById(R.id.SignContent);
		mSignTime = (TextView)findViewById(R.id.SignTime);
		
		//资讯
		mShopmsTxt = (TextView)findViewById(R.id.shop_msg_txtview);
		
		mShopAddressLayout = (RelativeLayout)findViewById(R.id.ShopAddressLayout);
		mShopPhoneLayout = (RelativeLayout)findViewById(R.id.ShopPhoneLayout);
		mFriendRecommLayout = (LinearLayout)findViewById(R.id.FriendRecommLayout);
		mReviewsLayout = (RelativeLayout)findViewById(R.id.ReviewsLayout);
		mSignLayout = (RelativeLayout)findViewById(R.id.SignLayout);
		mTrafficLayout = (RelativeLayout)findViewById(R.id.TrafficLayout);
		mBranchLayout = (RelativeLayout)findViewById(R.id.BranchLayout);
		mShopMsgLayout = (RelativeLayout)findViewById(R.id.MsgLayout);
		
		mShopAddressLayout.setOnClickListener(this);
		mShopPhoneLayout.setOnClickListener(this);
		mFriendRecommLayout.setOnClickListener(this);
		mReviewsLayout.setOnClickListener(this);
		mSignLayout.setOnClickListener(this);
		mTrafficLayout.setOnClickListener(this);
		mBranchLayout.setOnClickListener(this);
		mShopMsgLayout.setOnClickListener(this);
		
		
		mUpLoadImageLayout = (LinearLayout) findViewById(R.id.upload_image);
		mLeaveMessageLayout = (LinearLayout) findViewById(R.id.leave_message_layout);
		mCommentAddLayout = (LinearLayout) findViewById(R.id.comment_add_layout);
		mSendProblemLayout = (LinearLayout) findViewById(R.id.sendproblem_layout);
		mUpLoadImageLayout.setOnClickListener(this);
		mLeaveMessageLayout.setOnClickListener(this);
		mCommentAddLayout.setOnClickListener(this);
		mSendProblemLayout.setOnClickListener(this);
		
		initTitle();
	}

	private void initTitle(){
		setTitleText(R.string.shopdetail);
		
		LayoutParams p = new LayoutParams(MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size) , MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size));
		p.setMargins(MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_LRMargins), 0, MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_LRMargins), 0);
		
		mBtnShare = new Button(this);
		mBtnShare.setLayoutParams(p);
		mBtnShare.setBackgroundResource(R.drawable.button_share_selector);
		mBtnShare.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				shareContent("", mShopModule.writeup);
			}
		});
		setTitleRightButton(mBtnShare);
		
		mBtnFav = new Button(this);
		mBtnFav.setLayoutParams(p);
		mBtnFav.setBackgroundResource(R.drawable.button_fav_selector);
		mBtnFav.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//先判断是否登录了
				if (GoodPlaceContants.USERINFO == null) {
					Intent intent = new Intent(mContext, LoginActivity.class);
					startActivity(intent);
				}else {
					requestShopFav();
				}
			}
		});
		setTitleRightButton(mBtnFav);
		
		
	}
	
	
	public void requestShopInfo(int shopId) {
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		
		byte[] postData = JsonRequestManage.getShopInfoOfIdPostData(shopId,mShopCityId);
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_SHOP_INFO_OF_ID, postData, new ShopInfoParser(),mIsNeddCache, CACHE_NAME + shopId,
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null && object instanceof ShopModule) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SHOP_INFO_SUCCESS;
							mHandler.sendMessage(msg);
							Log.i("lch", "请求成功");
						} else {
							mHandler.sendEmptyMessage(LOAD_SHOP_INFO_FAILE);
							Log.i("lch", "请求失败");
						}
						
					}
			
				});
	}
	
	/**
	 * <br>功能简述:收藏商家
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	public void requestShopFav() {
		if(!mNetState.isNetUsing()){
			ShowToast(R.string.nonet);
			return;
		}
		
		byte[] postData = JsonRequestManage.getShopFavPostData(mShopId, GoodPlaceContants.USERINFO.getId());
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_SHOP_FAV, postData, new ResultParser(),
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null && object instanceof RequestResultModule) {
							boolean isSuccess = ((RequestResultModule)object).result;
							if (isSuccess) {
								// 还在子线程操作，handler是放回主线程处理
								Message msg = new Message();
								msg.obj = object;
								msg.what = LOAD_SHOP_FAV_SUCCESS;
								mHandler.sendMessage(msg);
								Log.i("lch", "收藏成功！");
								return;
							}
						} 
						mHandler.sendEmptyMessage(LOAD_SHOP_FAV_FAILE);
						Log.i("lch", "收藏失败！");
					}
				});
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				
				//请求排行版失败
				case LOAD_SHOP_INFO_SUCCESS :
					mShopModule = (ShopModule) msg.obj;
					setViewData(mShopModule);
					break;
					
				//请求排行版失败
				case LOAD_SHOP_INFO_FAILE :
					break;
					
					
				//收藏商家成功
				case LOAD_SHOP_FAV_SUCCESS :
					Toast.makeText(mContext, "收藏成功！", Toast.LENGTH_SHORT).show();
					break;
					
				//收藏商家失败
				case LOAD_SHOP_FAV_FAILE :
					Toast.makeText(mContext, "收藏失败！", Toast.LENGTH_SHORT).show();
					break;
					
				default :
					break;
			}
		}

	};
	
	
	
	
	public void setViewData(ShopModule shopModule){
		//设置图片
		setIcon(mShopImageView, shopModule.defaultpicurl, FileUtil.ICON_CACHE_PATH, String.valueOf(shopModule.defaultpicurl.hashCode()), true);
		mShopName.setText(shopModule.name);
		mOnePersonPrice.setText("人均:￥" + shopModule.avgprice);
		mScoreStar.setStar(shopModule.score);
		mShopAddress.setText(shopModule.address);
		mShopPhone.setText(shopModule.phoneno);
		
		//网友推荐
		ArrayList<Recommendtags> recommendtags = shopModule.recommendtags;
		int recommendtagsSize = recommendtags.size();
		if (recommendtagsSize > 3) {
			recommendtagsSize = 3;
		}
		String friendRecommString = "";
		for (int i = 0; i < recommendtagsSize; i++) {
			friendRecommString = friendRecommString + recommendtags.get(i).tag + " ";
		}
		mFriendRecomm.setText(friendRecommString);
		
		//点评
		Commentsituation commentsituation = shopModule.commentsituation;
		mReviewsCount.setText("点评（共" + commentsituation.total + "条)：");
		mReviewsName.setText(commentsituation.curuser);
		mReviewsSorce.setStar(commentsituation.star);
		mReviewsContent.setText(commentsituation.curcomment);
		mReviewsTime.setText(ContantsUtils.formatData(commentsituation.time));
		
		//签名
		Signsituation signsituation = shopModule.signsituation;
		mSignCount.setText("签到留言墙（共" + signsituation.total + "人)");
		mSignName.setText(signsituation.curuser);
		mSignContent.setText(signsituation.detail);
		mSignTime.setText(ContantsUtils.formatData(signsituation.time));
		
		//资讯
		String strshopmsg = mContext.getString(R.string.shopmsg);
		strshopmsg = String.format(strshopmsg, shopModule.infosituation.infototal + "");
		mShopmsTxt.setText(strshopmsg);
	}
	
	@Override
	public void onClick(View v) {
		//点击头像
		if (v == mShopImageView) {
			Intent intent = new Intent(this, ShopImageListActivity.class);
			intent.putExtra(GoodPlaceContants.KEY_SHOP_ID, mShopId);
			startActivity(intent);
		}
		
		//返回
		else if (v == mBtnBack) {
			finish();
		}
		
		//转发
		else if (v == mBtnForward) {
			forwardShopInfo();
		}
		
//		//收藏
//		else if (v == mBtnFav) {
//			//先判断是否登录了
//			if (GoodPlaceContants.USERINFO == null) {
//				Intent intent = new Intent(this, LoginActivity.class);
//				startActivity(intent);
//			}else {
//				requestShopFav();
//			}
//		}
		
		//地址布局
		else if (v == mShopAddressLayout){
			if(mShopModule != null){
				System.gc();
				
				ShowLoadingDialog();
				
				Intent intent = new Intent(this, MapActivity.class);
				ArrayList<ShopModule> shopModuleList = new ArrayList<ShopModule>();
				shopModuleList.add(mShopModule);
				intent.putExtra(MapActivity.KEY_SHOP_LIST, shopModuleList);
				intent.putExtra(MapActivity.KEY_NEEDPAGEBAR,false);
				startActivity(intent);
				
				MapActivity.setMapLoadedHandler(mHandlerHideLoading);
			}
		}
		
		//电话布局
		else if (v == mShopPhoneLayout) {
			callPhone(mShopPhone.getText().toString());
		}
		
		//网友推荐
		else if (v == mFriendRecommLayout) {
//			Intent intent =  new Intent(this,ShopCommontActivity.class);
//			intent.putExtra(GoodPlaceContants.KEY_SHOP_ID, mShopId);
//			startActivity(intent);
		}
		
		//评论
		else if (v == mReviewsLayout) {
			Intent intent =  new Intent(this,ShopCommontActivity.class);
			intent.putExtra(GoodPlaceContants.KEY_SHOP_ID, mShopId);
			startActivity(intent);
		}
		
		//签到墙
		else if (v == mSignLayout) {
			Intent intent =  new Intent(this,ShopSignWallActivity.class);
			intent.putExtra(GoodPlaceContants.KEY_SHOP_ID, mShopId);
			startActivity(intent);
		}
		
		//上传图片
		else if (v == mUpLoadImageLayout) {
			if(!mNetState.isNetUsing()){
				ShowToast(R.string.nonet);
				return;
			}
			
			//先判断是否登录了
			if (GoodPlaceContants.USERINFO == null) {
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}else {
				doPickPhotoAction();
			}
		}
		
		
		
		//签订留言
		else if (v == mLeaveMessageLayout) {
			if(!mNetState.isNetUsing()){
				ShowToast(R.string.nonet);
				return;
			}
			
			//先判断是否登录了
			if (GoodPlaceContants.USERINFO == null) {
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}else {
				Intent intent = new Intent(this, LeaveMessageActivity.class);
				intent.putExtra(GoodPlaceContants.KEY_SHOP_ID, mShopId);
				startActivity(intent);
			}
		}
		
		//添加评论
		else if (v == mCommentAddLayout) {
			if(!mNetState.isNetUsing()){
				ShowToast(R.string.nonet);
				return;
			}
			
			//先判断是否登录了
			if (GoodPlaceContants.USERINFO == null) {
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}else {
				Intent intent = new Intent(this, CommontAddActivity.class);
				intent.putExtra(GoodPlaceContants.KEY_SHOP_ID, mShopId);
				startActivity(intent);
			}
		}
		
		
		//交通
		else if (v == mTrafficLayout) {
			if(mShopModule != null){
				Intent intent = new Intent(this, ShopTrafficActivity.class);
				intent.putExtra(Contants.SHOP_TRAFFIC_TEXT, mShopModule.trafficinfo);
				intent.putExtra(Contants.SHOP_INFO_TEXT, mShopModule.writeup);
				startActivity(intent);
			}
		}
		
		//其它分店
		else if (v == mBranchLayout) {
			finish();
		}
		
		//商户资讯
		else if (v == mShopMsgLayout) {
			if(mShopModule!= null && mShopModule.infosituation.infototal > 0){
				Intent intent = new Intent(mContext, ShopMsgActivity.class);
				intent.putExtra(ShopMsgActivity.KEY_SHOPID, mShopModule.id + "");
				intent.putExtra(ShopMsgActivity.KEY_SHOPMSGSUM, mShopModule.infosituation.infototal + "");
				mContext.startActivity(intent);
			}
		}
		
		//报错
		else if (v == mSendProblemLayout) {
			if(!mNetState.isNetUsing()){
				ShowToast(R.string.nonet);
				return;
			}
			
			Intent intent = new Intent(this, SummitProblemActivity.class);
			startActivity(intent);
		}
		
	}

	Handler mHandlerHideLoading = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			HideLoadingDialog();
		}
	};
	
	/**
	 * 读取图标，然后设到imageview里
	 */
	private void setIcon(final ImageView imageView, String imgUrl, String imgPath, String imgName,
			boolean setDefaultIcon) {
		imageView.setTag(imgUrl);
		Bitmap bm = mImgManager.loadImage(imgPath, imgName, imgUrl, true, false, null,
				new AsyncImageLoadedCallBack() {
					@Override
					public void imageLoaded(Bitmap imageBitmap, String imgUrl) {
						if (imageView.getTag().equals(imgUrl)) {
							imageView.setImageBitmap(imageBitmap);
						} else {
							imageBitmap = null;
							imgUrl = null;
						}
					}
				});
		if (bm != null) {
			imageView.setImageBitmap(bm);
		} else {
			if (setDefaultIcon) {
				imageView.setImageResource(R.drawable.no_pic_big);
			} else {
				imageView.setImageDrawable(null);
			}
		}
	}
	
	/**
	 * 拨打电话
	 * @param phoneString
	 */
	public void callPhone(String phoneString){
		Uri uri = Uri.parse("tel:" + phoneString);
		Intent it = new Intent(Intent.ACTION_DIAL, uri);
		startActivity(it);
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
				ImageUntil.compressImage(Contants.Path.CAMERA_SAVE_PATH, Contants.Path.COMPRESS_SAVE_PATH);
				openUpLoadImage();
			}
			// 相冊
			else if (requestCode == REQUEST_CODE_ALBUM) {
				String datastr = data.getData().toString();
				if (datastr.startsWith("content:")) {
					try {
						Uri originalUri = data.getData();// 得到图片的URI
						String[] imgs = { MediaStore.Images.Media.DATA };// 将图片URI转换成存储路径
						Cursor cursor = this.managedQuery(originalUri, imgs, null, null, null);
						int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						String filePath = cursor.getString(index);
						
						try {
							// 4.0以上的版本会自动关闭 (3.0.1--11;  4.0--14; 4.0.3--15)
							if (Integer.parseInt(Build.VERSION.SDK) < 11) {
								cursor.close();
							}
						} catch (Exception e) {
						}
						//
						ImageUntil.compressImage(filePath, Contants.Path.COMPRESS_SAVE_PATH);
						openUpLoadImage();
					} catch (Exception e) {}
				} else if (datastr.startsWith("file:")) {
					String filePath = datastr.substring(datastr.indexOf("file://")+7);
					ImageUntil.compressImage(filePath, Contants.Path.COMPRESS_SAVE_PATH);
					openUpLoadImage();
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public void openUpLoadImage(){
		Intent intent = new Intent(this,ShopUpLoadImageActivity.class);
		intent.putExtra(GoodPlaceContants.KEY_SHOP_ID, mShopId);
		startActivity(intent);
	}
	
	
	public void forwardShopInfo(){
	final String sendMsgString = "11111";
		new AlertDialog.Builder(mContext)
		.setTitle("发送好友")
		.setItems(new String[] {"发送短信", "发送邮件" },
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							Uri smsToUri = Uri.parse("smsto:");  
							Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);  
							intent.putExtra("sms_body", sendMsgString);  
							startActivity(intent);  
						} 
						
						else if (which == 1) {
							Intent email = new Intent(android.content.Intent.ACTION_SEND);
							email.setType("plain/text");
							//设置邮件默认标题
							email.putExtra(android.content.Intent.EXTRA_SUBJECT, "推荐个商户给你");
							//设置要默认发送的内容
							email.putExtra(android.content.Intent.EXTRA_TEXT, sendMsgString);
							//调用系统的邮件系统
							startActivity(Intent.createChooser(email, "请选择邮件发送软件"));
						}
					}
				}).setNegativeButton("取消", null).show();
	}
}
