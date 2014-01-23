package lbs.goodplace.com.View.main;

import java.io.File;
import java.util.ArrayList;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.adapter.ShopImageListAdapter;
import lbs.goodplace.com.View.contant.ImageUntil;
import lbs.goodplace.com.View.contant.Machine;
import lbs.goodplace.com.View.login.LoginActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.NetState;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.manage.util.GlobalUtil;
import lbs.goodplace.com.manage.util.MyMothod;
import lbs.goodplace.com.obj.ShopImageInfo;
import lbs.goodplace.com.obj.ShopImageListInfo;
import lbs.goodplace.com.obj.parser.ShopImageListParser;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class ShopImageListActivity extends ModuleActivity implements View.OnClickListener{
	public static String KEY_MYSIGN = "KEY_MYSIGN";
	
	public Context mContext;
	public Button mBtnBack; //返回按钮
	public Button mBtnUpLoadPic; //返回按钮
	
	private final static int REQUEST_CODE_CAMERA = 1;
	private final static int REQUEST_CODE_ALBUM = 2;
	private final static int REQUEST_CODE_ZOOM = 3;

	private GridView mGridView;
	public ShopImageListAdapter mAdapter;
	ArrayList<ShopImageInfo> mDataSource = new ArrayList<ShopImageInfo>();
	ArrayList<ShopImageInfo> mDataSourceTemp = new ArrayList<ShopImageInfo>();
	public int mCurPage = 1;
	public int mNextPage = 1;
	public int mTotalPage = 2;
	private boolean mIsMysign = false;	//是否查看我的留言
	
	public int mShopId = -1;
	
	public boolean isShowLastPage = true; //是否提示最后一页
	
	//缓存
	private String CACHE_NAME = "ShopImageListActivity";
	private boolean mIsNeddCache = true;	//是否需要读取缓存
	private NetState mNetState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.shop_info_image_list_view, mLayout_body);
//		setContentView(R.layout.shop_info_image_list_view);
		mContext = this;
		mNetState = new NetState(mContext);
		
		//取参数
        Bundle budle = getIntent().getExtras();
		if(budle != null ){
			if(budle.get(GoodPlaceContants.KEY_SHOP_ID) != null){
				this.mShopId = budle.getInt(GoodPlaceContants.KEY_SHOP_ID);
			}
			
			if(budle.get(KEY_MYSIGN) != null){
				this.mIsMysign = budle.getBoolean(KEY_MYSIGN);
			}
		}
		
		if (mShopId == -1 && !mIsMysign) {
			finish();
			return;
		}
		initView();
//		loadData(mCurPage);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		mCurPage = 1;
		mNextPage = 1;
		mDataSource.clear();
		mDataSourceTemp.clear();
		loadData(mCurPage);
	}
	
	
	public void initView(){
//		mBtnBack = (Button)findViewById(R.id.btn_back);
//		mBtnBack.setOnClickListener(this);
//		
//		mBtnUpLoadPic = (Button)findViewById(R.id.upload_pic);
//		mBtnUpLoadPic.setOnClickListener(this);
		
		mGridView = (GridView) findViewById(R.id.gridView);
		mAdapter = new ShopImageListAdapter(mContext);
		mGridView.setAdapter(mAdapter);
		mAdapter.updateDataSource(mDataSource);
		
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				for (int i = 0; i < mDataSource.size(); i++) {
//					Log.i("lch3", i + ":" + mDataSource.get(i).mIsProgressBar);
//				}
				Intent intent = new Intent(mContext, ImageBlowerActivity.class);
				intent.putExtra(Contants.SHOP_IMAGE_LIST, mDataSource);
				intent.putExtra(Contants.SHOP_IMAGE_CLICK_POSITION , position);
				mContext.startActivity(intent);
			}
		});
		
		mGridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				// 如果是滑到底部，加载下一页
				case OnScrollListener.SCROLL_STATE_IDLE: {
					if (view.getLastVisiblePosition() >= (view.getCount() - 1)) {
						if (mCurPage >= mTotalPage) {
							if (isShowLastPage) {
//								Toast.makeText(mContext, "最后一页", Toast.LENGTH_SHORT).show();
								isShowLastPage = false;
							}
						}else {
							loadData(mNextPage);
						}
					}

					// 列表停止滚动时
					// 找出列表可见的第一项和最后一项
					int start = view.getFirstVisiblePosition();
					int end = view.getLastVisiblePosition();
					// 如果有添加HeaderView，要减去
					ListView lisView = null;
					if (view instanceof ListView) {
						lisView = (ListView) view;
					}
					if (lisView != null) {
						int headViewCount = lisView.getHeaderViewsCount();
						start -= headViewCount;
						end -= headViewCount;
					}
					if (end >= view.getCount()) {
						end = view.getCount() - 1;
					}
					// 对图片控制器进行位置限制设置
					AsyncImageManager.getInstance().setLimitPosition(start, end);
					// 然后解锁通知加载
					AsyncImageManager.getInstance().unlock();
				}
					break;
				case OnScrollListener.SCROLL_STATE_FLING: {
					// 列表在滚动，图片控制器加锁
					AsyncImageManager.getInstance().lock();
				}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL: {
					// 列表在滚动，图片控制器加锁
					AsyncImageManager.getInstance().lock();
				}
					break;
				default:
					break;
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
			}
		});
		
		initTitle();
	}
	
	private void initTitle(){
		if(mIsMysign){
			setTitleText(R.string.myalbum);
		}else{
			setTitleText(R.string.album);
			
			LayoutParams p = new LayoutParams(MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size) , MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size));
			
			mBtnUpLoadPic = new Button(this);
			mBtnUpLoadPic.setLayoutParams(p);
			mBtnUpLoadPic.setBackgroundResource(R.drawable.upload_camera_selector);
			mBtnUpLoadPic.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(!mNetState.isNetUsing()){
						ShowToast(R.string.nonet);
						return;
					}
					
					//先判断是否登录了
					if (GoodPlaceContants.USERINFO == null) {
						Intent intent = new Intent(mContext, LoginActivity.class);
						startActivity(intent);
					}else {
						doPickPhotoAction();
					}
				}
			});
			setTitleRightButton(mBtnUpLoadPic);
		}
		
	}
	
	public void loadData(int curpage) {
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		
		byte[] postData = null;
		String url = "";
		if(mIsMysign){
			postData = JsonRequestManage.getMyImageListPostData(curpage);
			url = GoodPlaceContants.URL_MY_IMAGE_LIST;
		}else{
			postData = JsonRequestManage.getShopImageListPostData(mShopId, curpage);
			url = GoodPlaceContants.URL_SHOP_IMAGE_LIST;
		}
		
		RequestManager.loadDataFromNet(mContext, url, postData, new ShopImageListParser(),mIsNeddCache,CACHE_NAME + mShopId + curpage,
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null && object instanceof ShopImageListInfo) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = Contants.LOAD_SUCCESS;
							mHandler.sendMessage(msg);
						} else {
							mHandler.sendEmptyMessage(Contants.LOAD_FALSE);
						}
						
					}
			
				});
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case Contants.LOAD_SUCCESS :
					if (mNextPage == 1) {
						mDataSource.clear();
					}
				
					mCurPage = mNextPage;
					mNextPage = mCurPage + 1;
					
					ShopImageListInfo shopImageListInfo = (ShopImageListInfo)msg.obj;
					
					ArrayList<ShopImageInfo> shopImageList = shopImageListInfo.mShopImageList;
					
					Log.i("zjj", "商店图片列表大小:" + shopImageList.size());
					if(shopImageList.size() == 0){
						mGridView.setVisibility(View.GONE);
						return;
					}
					
					if (shopImageList != null && shopImageList.size() > 0) {
						mTotalPage = shopImageListInfo.mPageInfo.mPagecount;
						mDataSourceTemp.addAll(shopImageList);
						mAdapter.notifyDataSetChanged();
					}	
					
					mDataSource.clear();
					mDataSource.addAll(mDataSourceTemp);
					
					ShopImageInfo info = new ShopImageInfo();
					
					if (mNextPage <= mTotalPage) {
						info.mIsProgressBar = 1;
						mDataSource.add(info);
					}
					
					mAdapter.notifyDataSetChanged();
					break;
					
				case Contants.LOAD_FALSE :
					Toast.makeText(mContext, "请求失败", 0).show();
//					if (mNextPage == 1) {
//						mDataSourceTemp.clear();
//					}
//					
//					mCurPage = mNextPage;
//					mNextPage = mCurPage + 1;
//					
//					addData();
//					mAdapter.notifyDataSetChanged();
					break;
					
				default :
					break;
			}
		}

	};

	
	
	
//	public void addData() {
//		String url1 = "http://postimg1.mop.com/2010/12/28/1293523608798423.jpg";
//		String url2 = "http://www.661998.com/uploads/allimg/120914/0S5501044-3.jpg";
//		String url3 = "http://www.661998.com/uploads/allimg/120914/0S5501044-3.jpg";
//		String url4 = "http://comic.people.com.cn/NMediaFile/2012/1120/MAIN201211201515000185687571866.jpg";
//		String url5 = "http://www.daozhou.net/yule/attach/20121125/1353777374ADjL.jpg";
//		String url6 = "http://game.people.com.cn/NMediaFile/2012/1121/MAIN201211211428000205195914208.jpg";
//		String url7 = "http://ww4.sinaimg.cn/mw600/a7656986jw1dt8mo0xi6qj.jpg";
//		String url8 = "http://ww2.sinaimg.cn/large/9a992288jw1dtc7le2r0qj.jpg";
//		String url9 = "http://www.qianyan001.com/img1/images/mm/qcmn/1-111126225321.jpg";
//		String url10 = "http://www.yjz9.com/uploadfile/2012/1023/20121023121216627.jpg";
//		
//		String[] urlList = {url1,url2,url3,url4,url5,url6,url7,url8,url9,url10};
//		
//		
//		for (int i = 0; i < 10; i++) {
//			ShopImageInfo info = new ShopImageInfo();
//			info.mThumburl = urlList[i];
//			info.mFullurl = urlList[i];
//			info.mImgname = "食物" + i;
//			info.mUploaduser = "小猪" + i;
//			info.mStar = 35;
//			info.mPrice = 50 + i;
//			info.mUploadtime = System.currentTimeMillis();
//			mDataSourceTemp.add(info);
//		}
//		
//		mDataSource.clear();
//		mDataSource.addAll(mDataSourceTemp);
//		
//		ShopImageInfo info = new ShopImageInfo();
//		if (mNextPage <= mTotalPage) {
//			info.mIsProgressBar = 1;
////			Log.i("lch2", "22222222");
//			mDataSource.add(info);
//		}
//	}
//	
//	
//	public void log(String string) {
//		Log.i("lch1", string);
//	}




	@Override
	public void onClick(View v) {
		if (v == mBtnBack) {
			finish();
		}
		
//		else if (v == mBtnUpLoadPic) {
//			//先判断是否登录了
//			if (GoodPlaceContants.USERINFO == null) {
//				Intent intent = new Intent(this, LoginActivity.class);
//				startActivity(intent);
//			}else {
//				doPickPhotoAction();
//			}
//		}
		
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
}
