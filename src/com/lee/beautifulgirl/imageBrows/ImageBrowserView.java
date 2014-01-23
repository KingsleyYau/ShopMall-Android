package com.lee.beautifulgirl.imageBrows;

import java.util.ArrayList;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ImageBrowserProgressBar;
import lbs.goodplace.com.View.main.Contants;
import lbs.goodplace.com.component.ScoreStar;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager.AsyncImageLoadedCallBack;
import lbs.goodplace.com.manage.util.ContantsUtils;
import lbs.goodplace.com.manage.util.DownImageUitl;
import lbs.goodplace.com.manage.util.FileUtil;
import lbs.goodplace.com.obj.ShopImageInfo;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lee.beautifulgirl.imageBrows.ImageScroller.ImageScrollerListener;

/**
 * 
 * <br>
 * 类描述: <br>
 * 功能详细描述:
 * 
 * @author dingzijian
 * @date [2012-8-28]
 */
public class ImageBrowserView extends RelativeLayout implements
		OnClickListener, ImageScrollerListener {

	private static final int MOVE_RIGHT = 0x001;
	private static final int MOVE_LEFT = 0x002;
	// 与下标对应，不能改为其他值
	public static final int UPDATE_FIRST_SCALEVIEW = 0;
	public static final int UPDATE_MIDDLE_SCALEVIEW = 1;
	public static final int UPDATE_LAST_SCALEVIEW = 2;

	public static final int UPDATE_CURRENT_IMG_INDEX = 0x003;

	private LinearLayout mImageScrollerLayout; // 加载Scroller的布局
	private TextView mPicCountText; // 显示图片张图的text
	
	private ImageView mBtnBack; // 返回按钮
	private ImageView mBtnDownImage; //下载图片按钮
	private TextView mFootName; //食物名称
	private ScoreStar mScoreStar; //评分
	
	private TextView mUserName; //上传用户名
	private TextView mTimeText; // 时间
	private TextView mPriceText; // 价格

	
	

	private UIHandler mHandler;
	private ImageScroller mImageScroller; // 图片滑动的容器
	private Context mContext;
	private RelativeLayout mTopLayout;
	private RelativeLayout mBottomLayout;

	private int mImgCount;
	private ArrayList<ShopImageInfo> mLoadList = new ArrayList<ShopImageInfo>(3); // 当前三个scaleview的数据对象集合
	private ArrayList<ShopImageInfo> mAllDataList; // 当前浏览相册的数据对象集合
	private ImageBrowserScaleView mFirstScaleView; // 第一张视图，下标为0
	private ImageBrowserScaleView mMiddleScaleView; // 第二张视图，下标为1
	private ImageBrowserScaleView mLastScaleView; // 第三张视图，下标为2

	private ShopImageInfo mSelectedFile; // 相册中选中的图片的bean

	private AsyncImageManager mImageManager;

	public ImageBrowserView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mImageManager = AsyncImageManager.getInstance();
	}

	@Override
	protected void onFinishInflate() {
		init();
		super.onFinishInflate();
	}

	private void init() {
		mImageScrollerLayout = (LinearLayout) findViewById(R.id.image_browser_gallery);
		mPicCountText = (TextView) findViewById(R.id.image_browser_pic_count);
		
		mBtnBack = (ImageView) findViewById(R.id.btn_back);
		mBtnDownImage = (ImageView) findViewById(R.id.btn_down_image);
		mFootName = (TextView) findViewById(R.id.foot_name);
		mScoreStar = (ScoreStar) findViewById(R.id.scoreStar);
		mUserName = (TextView) findViewById(R.id.user_name);
		mTimeText = (TextView) findViewById(R.id.time);
		mPriceText = (TextView) findViewById(R.id.price);
		
		mBottomLayout = (RelativeLayout) findViewById(R.id.image_browser_bottom_layout);
		mTopLayout = (RelativeLayout) findViewById(R.id.image_browser_top_layout);

		mBtnBack.setOnClickListener(this);
		mBtnDownImage.setOnClickListener(this);
		mImageScroller = new ImageScroller(mContext);
		mImageScroller.setOnImageScrollerListener(this);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		mImageScroller.setLayoutParams(params);
		mImageScrollerLayout.addView(mImageScroller);

		mHandler = new UIHandler();
		hideLayout();
	}

	/**
	 * 
	 * <br>
	 * 类描述: 更新UI视图的handler <br>
	 * 功能详细描述: 负责接收后台线程加载图片后发送过来的图片。
	 * 
	 * @author dingzijian
	 * @date [2012-8-28]
	 */
	class UIHandler extends Handler {

		@Override
		public synchronized void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE_CURRENT_IMG_INDEX:
				int imgIndex = msg.arg1;
				if (imgIndex >= mAllDataList.size()) {
					break;
				}
				setImageBaseData((imgIndex + 1), mAllDataList.get(imgIndex));  //设置基本信息
				break;
				
			default:
				break;
			}
			super.handleMessage(msg);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//返回
		case R.id.btn_back:
			((Activity)mContext).finish();
			break;
		
		//下载图片
		case R.id.btn_down_image:
			downLoadImage();
			break;
			
		//显示上下提示	
		case R.id.image_browser_item:
			if (isLayoutShow()) {
				hideLayout();
			} else {
				showLayout();
			}
			break;

		default:
			break;
		}
	}

	private ImageBrowserScaleView initScaleView() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		FrameLayout imageLayout = (FrameLayout) inflater.inflate(
				R.layout.image_browser_item_view, null);
		ImageBrowserScaleView imageView = (ImageBrowserScaleView) imageLayout
				.findViewById(R.id.image_browser_item);
		imageView.setOnClickListener(this);
		imageView.setClickable(true);
		ImageBrowserProgressBar progressBar = (ImageBrowserProgressBar) imageLayout
				.findViewById(R.id.image_browser_loading_view);
		progressBar.setBackgroundResource(android.R.color.transparent);
		imageView.setLoadView(progressBar);
		TextView textView = (TextView) imageLayout
				.findViewById(R.id.imagebrower_image_damage);
		imageView.setDamageView(textView);
		mImageScroller.addScreenView(imageLayout);
		return imageView;
	}

	// 检测屏幕的滑动情况
	@Override
	public void onScreenChanged(final int newScreen, final int oldScreen) {
		int fingWay = fingWay();
		boolean isRight = fingWay == MOVE_RIGHT;
		switch (newScreen) {
		case 0:
			if (isRight) {
				loadNextImg(0, 1);
			} else {
				loadLastImg(0, 2);
			}
			break;
		case 1:
			if (isRight) {
				loadNextImg(1, 2);
			} else {
				loadLastImg(1, 0);
			}
			break;
		case 2:
			if (isRight) {
				loadNextImg(2, 0);
			} else {
				loadLastImg(2, 1);
			}
			break;
		default:
			break;
		}
	}

	// 检测屏幕的滑动是否完成
	@Override
	public void onScrollFinish(final int currentScreen) {
		loadCurrentImg(currentScreen);
	}

	private void loadNextImg(int currentPosition, int nextPositon) {
		if (mLoadList.isEmpty() || mAllDataList == null) {
			return;
		}
		ShopImageInfo infoBean;
		int currentIndex = getFolderIndex(mLoadList.get(currentPosition));
		Message msg = mHandler.obtainMessage(UPDATE_CURRENT_IMG_INDEX);
		msg.arg1 = currentIndex;
		mHandler.sendMessage(msg);
		if (mImageScroller.getChildCount() < 3) {
			return;
		}

		int nextIndex = getFolderIndex(mLoadList.get(nextPositon));
		int nextPicIndex = currentIndex + 1;
		if (nextPicIndex > mAllDataList.size() - 1) {
			nextPicIndex = 0;
		}
		infoBean = mAllDataList.get(nextPicIndex);
		if (nextPicIndex == nextIndex) {
			return;
		}
		mLoadList.set(nextPositon, infoBean);

		ImageBrowserScaleView scaleView = updateScaleViewByPosition(nextPositon);
		if (scaleView != null) {
			if (!scaleView.getTag().equals(infoBean.mFullurl)) {
				scaleView.showLoadView();
				scaleView.setImageBitmap(null);
				loadImage(scaleView, infoBean.mFullurl,nextPicIndex, true);
			}
		}
	}

	/**
	 * <br>
	 * 功能简述:启动后台线程预加载下一张图片 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param currentPosition
	 * @param lastPositon
	 */
	private void loadLastImg(int currentPosition, int lastPositon) {
		if (mLoadList.isEmpty() || mAllDataList == null) {
			return;
		}
		// 更新图片名称。
		ShopImageInfo infoBean;
		int currentIndex = getFolderIndex(mLoadList.get(currentPosition));
		Message msg = mHandler.obtainMessage(UPDATE_CURRENT_IMG_INDEX);
		msg.arg1 = currentIndex;
		mHandler.sendMessage(msg);
		// 图片小于等于3张不需要预加载。
		if (mImageScroller.getChildCount() < 3) {
			return;
		}
		int lastIndex = getFolderIndex(mLoadList.get(lastPositon));
		int lastPicIndex = currentIndex - 1;
		if (lastPicIndex < 0) {
			lastPicIndex = mAllDataList.size() - 1;
		}
		infoBean = mAllDataList.get(lastPicIndex);
		if (lastPicIndex == lastIndex) {
			return;
		}
		mLoadList.set(lastPositon, infoBean);

		ImageBrowserScaleView scaleView = updateScaleViewByPosition(lastPositon);
		if (scaleView != null) {
			if (!scaleView.getTag().equals(infoBean.mFullurl)) {
				scaleView.showLoadView();
				scaleView.setImageBitmap(null);
				loadImage(scaleView, infoBean.mFullurl, lastPicIndex, true);
			}
		}
	}

	/**
	 * <br>
	 * 功能简述: 启动后台线程加载当前浏览的清晰图片。 <br>
	 * 功能详细描述: <br>
	 * 注意:
	 * 
	 * @param currentPosition
	 */
	private void loadCurrentImg(int currentPosition) {
		if (mLoadList == null) {
			return;
		} else if (mLoadList.isEmpty()) {
			return;
		}
		ShopImageInfo infoBean = mLoadList.get(currentPosition);
		ImageBrowserScaleView scaleView = (ImageBrowserScaleView) mImageScroller.getCurrentScaleView();
		if (scaleView.mFloderIndex >= mAllDataList.size()) {
			scaleView.mFloderIndex = 0;
		}

		if (scaleView != null) {
			if (!scaleView.getTag().equals(infoBean.mFullurl)) {
				loadImage(scaleView, infoBean.mFullurl, scaleView.mFloderIndex, true);
			}
		}
	}

	/**
	 * <br>
	 * 功能简述: 为图片浏览器注入数据 <br>
	 * 功能详细描述: <br>
	 * 注意:，1张个2张图片需要特殊处理。
	 * 
	 * @param info
	 * @param infoBeans
	 */
	public void setData(ShopImageInfo info, ArrayList<ShopImageInfo> infoBeans) {
		if (infoBeans == null || infoBeans.isEmpty()) {
			return;
		}
		if (mLoadList != null && !mLoadList.isEmpty()) {
			mLoadList.clear();
		}
		mImgCount = infoBeans.size();
		mAllDataList = infoBeans;
		int size = infoBeans.size();
		int i = getFolderIndex(info);
		switch (size) {
		case 1:
			mLoadList.add(info);
			setImageBaseData(getFolderIndex(mLoadList.get(0)) + 1, info);
			loadImage(mFirstScaleView, info.mFullurl, i, true);
			break;
			
		case 2:
			if (i == 0) {
				mLoadList.add(infoBeans.get(i));
				mLoadList.add(infoBeans.get(i + 1));
				setImageBaseData(1, info); //设置基本信息
				loadImage(mFirstScaleView, info.mFullurl, i, true);
			} else if (i == 1) {
				mLoadList.add(infoBeans.get(i - 1));
				mLoadList.add(infoBeans.get(i));
				setImageBaseData(2, info); //设置基本信息
				loadImage(mMiddleScaleView, info.mFullurl, i, true);
			}
			break;
		
		default:
			if (i < 0) {
				return;
			}
			if (i == 0) {
				mLoadList.add(infoBeans.get(size - 1));
				mLoadList.add(infoBeans.get(i));
				mLoadList.add(infoBeans.get(i + 1));
			} else if (i == size - 1) {
				mLoadList.add(infoBeans.get(i - 1));
				mLoadList.add(infoBeans.get(i));
				mLoadList.add(infoBeans.get(0));
			} else {
				mLoadList.add(infoBeans.get(i - 1));
				mLoadList.add(infoBeans.get(i));
				mLoadList.add(infoBeans.get(i + 1));
			}
			for (int j = 0; j < mLoadList.size(); j++) {
				ShopImageInfo infoBean = mLoadList.get(j);
				switch (j) {
				case 0:
					mFirstScaleView.showLoadView();
					loadImage(mFirstScaleView, infoBean.mFullurl, i, true);
					break;
				case 1:
					setImageBaseData(i + 1, infoBean); //设置基本信息
					
					mMiddleScaleView.showLoadView();
					loadImage(mMiddleScaleView, infoBean.mFullurl, i, true);
					break;
				case 2:
					mLastScaleView.showLoadView();
					loadImage(mLastScaleView, infoBean.mFullurl, i, true);
					break;
				default:
					break;
				}
			}
			break;
		}
	}

	private int getFolderIndex(ShopImageInfo info) {
		if (mAllDataList != null) {
			return mAllDataList.indexOf(info);
		}
		return -1;
	}


	private void hideLayout() {
		mBottomLayout.setVisibility(View.GONE);
		mTopLayout.setVisibility(View.GONE);
	}

	private void showLayout() {
		mBottomLayout.setVisibility(View.VISIBLE);
		mTopLayout.setVisibility(View.VISIBLE);
	}

	private boolean isLayoutShow() {
		if (mBottomLayout.isShown() && mTopLayout.isShown()) {
			return true;
		}
		return false;
	}

	private int fingWay() {
		int newScreen = mImageScroller.getCurrentScreen();
		int oldScreen = mImageScroller.getLastScreen();
		if (newScreen - oldScreen == 1 || newScreen - oldScreen == -2) {
			return MOVE_RIGHT;
		} else {
			return MOVE_LEFT;
		}
	}

	public ShopImageInfo getImgInfo(int index) {
		return mAllDataList.get(index);
	}

	public void recyle() {
		mImageScroller.removeAllViews();
		if (mFirstScaleView != null) {
			mFirstScaleView.recyle();
		}
		if (mMiddleScaleView != null) {
			mMiddleScaleView.recyle();
		}
		if (mLastScaleView != null) {
			mLastScaleView.recyle();
		}
		mImageScroller.recyle();
		mHandler = null;
	}

	public void setAnimationBitmap(ShopImageInfo fileInfo, ArrayList<ShopImageInfo> fileInfos) {
		int which = fileInfos.size();
		int index = fileInfos.indexOf(fileInfo);
		mSelectedFile = fileInfo;
		switch (which) {
		case 1:
			mImageScroller.getScreenScroller().setCurrentScreen(0);
			mFirstScaleView = initScaleView();
			break;
		case 2:
			if (index == 0) {
				mImageScroller.getScreenScroller().setCurrentScreen(0);
			}
			if (index == 1) {
				mImageScroller.getScreenScroller().setCurrentScreen(1);
			}
			mFirstScaleView = initScaleView();
			mMiddleScaleView = initScaleView();
			break;
		default:
			mFirstScaleView = initScaleView();
			mMiddleScaleView = initScaleView();
			mLastScaleView = initScaleView();
			mImageScroller.getScreenScroller().setCurrentScreen(1);
			break;
		}
		mAllDataList = fileInfos;
		setData(mSelectedFile, mAllDataList);
	}

	public ImageBrowserScaleView updateScaleViewByPosition(int position) {
		switch (position) {
		case UPDATE_FIRST_SCALEVIEW:
			return mFirstScaleView;
		case UPDATE_MIDDLE_SCALEVIEW:
			return mMiddleScaleView;
		case UPDATE_LAST_SCALEVIEW:
			return mLastScaleView;
		default:
			break;
		}
		return null;

	}

	public void loadImage(final ImageBrowserScaleView imageView, String imgUrl, final int picIndex, final boolean isCurrent) {
		imageView.setTag(imgUrl);
		if (imgUrl.equals("")) {
			imageView.showDamageView();
			return;
		}
		imageView.mFloderIndex = picIndex;

		Bitmap bm = mImageManager.loadImage(FileUtil.ICON_CACHE_PATH, String.valueOf(imgUrl.hashCode()),
				imgUrl, true, false, null, new AsyncImageLoadedCallBack() {
					@Override
					public void imageLoaded(Bitmap imageBitmap, String imgUrl) {
						if (imageView.getTag().equals(imgUrl)) {
							Options options = new Options();
							options.outHeight = imageBitmap.getHeight();
							options.outWidth = imageBitmap.getWidth();
							imageView.mIsCurrent = isCurrent;
							imageView.hideLoadView();
							imageView.setBitmap(imageBitmap, options);
						} else {
							imageBitmap = null;
							imgUrl = null;
						}
					}
				});
		if (bm != null) {
			Options options = new Options();
			options.outHeight = bm.getHeight();
			options.outWidth = bm.getWidth();
			imageView.mIsCurrent = isCurrent;
			imageView.hideLoadView();
			imageView.setBitmap(bm, options);
		}
	}
	
	
	public void setImageBaseData(int position, ShopImageInfo info){
		if (info == null) {
			return;
		}
		
		mPicCountText.setText(position + "/" + mImgCount);
		
		mFootName.setText(info.mImgname);
		mScoreStar.setStar(info.mStar);
		mUserName.setText(info.mUploaduser);
		mTimeText.setText(ContantsUtils.formatData(info.mUploadtime));
		if (info.mPrice != 0) {
			mPriceText.setText("￥" + info.mPrice);
		}else {
			mPriceText.setText("");
		}
	}
	
	
	/**
	 * <br>功能简述:下载图片
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	public void downLoadImage(){
		int index = mImageScroller.getCurrentScaleView().mFloderIndex;
		ShopImageInfo info = getImgInfo(index);
		if (info != null) {
			String imgUrl = info.mFullurl; //图片下载路径
			DownImageUitl.downImage(mContext, imgUrl, FileUtil.ICON_CACHE_PATH, Contants.Path.IMAGE_DOWN_SAVE_PATH);
			
//			String fileName = String.valueOf(imgUrl.hashCode());
//			final File downFile = new File(FileUtil.ICON_CACHE_PATH + fileName);
//			final File saveFile = new File(Contants.Path.IMAGE_DOWN_SAVE_PATH + fileName + ".jpg");
//			
//			Bitmap bm = mImageManager.loadImage(FileUtil.ICON_CACHE_PATH, fileName,
//					imgUrl, true, false, null, new AsyncImageLoadedCallBack() {
//						@Override
//						public void imageLoaded(Bitmap imageBitmap, String imgUrl) {
//							boolean success = FileUtil.copyFile(downFile, saveFile, false);
//							if (success) {
//								Toast.makeText(mContext, "下载图片成功，保存在：" + saveFile, Toast.LENGTH_SHORT).show();
//							}else {
//								Toast.makeText(mContext, "下载图片失败！", Toast.LENGTH_SHORT).show();
//
//							}
//						
//						}
//					});
//			if (bm != null) {
//				boolean success = FileUtil.copyFile(downFile, saveFile, false);
//				if (success) {
//					Toast.makeText(mContext, "下载图片成功，保存在：" + saveFile, Toast.LENGTH_SHORT).show();
//				}else {
//					Toast.makeText(mContext, "下载图片失败！", Toast.LENGTH_SHORT).show();
//
//				}
//			}
		}
	}
}
