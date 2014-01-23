package com.lee.beautifulgirl.imageBrows;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.untils.toolsproject.scroller.DrawUtils;
/**
 * 
 * <br>类描述: 用于展现图片的自定义控件。
 * <br>功能详细描述:
 * 
 * @author  dingzijian
 * @date  [2012-9-21]
 */
public class ImageBrowserScaleView extends ImageView {
	private Context mContext;
	protected boolean mIsCurrent; //是否当前展示给用户的照片。
	private ScaleTouchListener mListener;
	private boolean mIsOverEdge;  //图片放大移动时，是否到达了边界。
	public int mFloderIndex; //当前图片在相册中的下标。
	private View mLoadView; //显示加载中的view
	private TextView mDamageView; //图片损坏显示的view
	public int index;
	public ImageBrowserScaleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}
/**
 * 
 * <br>类描述:
 * <br>功能详细描述:
 * 
 * @author  dingzijian
 * @date  [2012-9-26]
 */
	class ScaleTouchListener {
		private Matrix mMatrix = new Matrix(); //控制图片大小与旋转的举证。
		private Matrix mSavedMatrix = new Matrix();
		private Matrix mOriMatrix = new Matrix(); //原始的矩阵。
		private DisplayMetrics mDisplayMetrics;
		private Bitmap mBitmap;
		private float minScaleR; // 最小缩放比例
		private float mOriMinScaleR;
		private static final float MAX_SCALE = 14f; // 最大缩放比例
		public static final int NONE = 0; // 初始状态
		public static final int DRAG = 1; // 拖动
		public static final int ZOOM = 2; // 缩放
		private int mMode = NONE;
		private PointF mPrev = new PointF();
		private PointF mMid = new PointF();
		private float mDist = 1f;
		private Options mOptions;

		public ScaleTouchListener(Bitmap bm, Options options) {
			mDisplayMetrics = new DisplayMetrics();
			((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
			setMatrix(bm, options);
		}

		private boolean onTouch(MotionEvent event) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			// 主点按下
				case MotionEvent.ACTION_DOWN :
					if (!mOriMatrix.equals(mMatrix)) {
						mSavedMatrix.set(mMatrix);
						mMode = DRAG;
						mPrev.set(event.getX(), event.getY());
					}
					break;
				// 副点按下
				case MotionEvent.ACTION_POINTER_DOWN :
					mDist = spacing(event);
					// 如果连续两点距离大于10，则判定为多点模式
					if (spacing(event) > 10f) {
						mSavedMatrix.set(mMatrix);
						midPoint(mMid, event);
						mMode = ZOOM;
					}
					break;
				case MotionEvent.ACTION_UP :
				case MotionEvent.ACTION_POINTER_UP :
					mMode = NONE;
					break;
				case MotionEvent.ACTION_MOVE :
					if (mMode == DRAG && event.getPointerCount() == 1 && !mOriMatrix.equals(mMatrix)) {
						mMatrix.set(mSavedMatrix);
						float x = event.getX() - mPrev.x;
						float y = event.getY() - mPrev.y;
						mMatrix.postTranslate(x, y);
					} else if (mMode == ZOOM) {
						float newDist = spacing(event);
						if (newDist > 10f) {
							mMatrix.set(mSavedMatrix);
							float tScale = newDist / mDist;
							mMatrix.postScale(tScale, tScale, mMid.x, mMid.y);
						}
					}
					break;
			}
			setImageMatrix(mMatrix);
			mIsOverEdge = checkView();
			return true;
		}

		/**
		 * 限制最大最小缩放比例，自动居中
		 */
		private boolean checkView() {
			float p[] = new float[9];
			mMatrix.getValues(p);
			if (mMode == ZOOM) {
				if (p[0] < minScaleR) {

					mMatrix.setScale(minScaleR, minScaleR);
				}
				if (p[0] > MAX_SCALE) {
					mMatrix.set(mSavedMatrix);
				}
			}
			return center();
		}

		/**
		 * 最小缩放比例，最大为100%
		 */
		private void minZoom() {
			if (mBitmap == null) {
				return;
			}
			int w = mBitmap.getWidth();
			int h = mBitmap.getHeight();
			if (w <= 0 || h <= 0) {
				return;
			}
			minScaleR = Math.min((float) mDisplayMetrics.widthPixels / (float) w, (float) mDisplayMetrics.heightPixels
					/ (float) h);
			if (minScaleR > 1 && mIsCurrent
					&& (mOptions.outWidth < mDisplayMetrics.widthPixels && mOptions.outHeight < mDisplayMetrics.heightPixels)) {
				minScaleR = 1;
			}
			if (!mIsCurrent && mOriMinScaleR > 1) {
				minScaleR = Math.min(mOptions.outWidth / w, mOptions.outHeight / h);
			}
			mMatrix.postScale(minScaleR, minScaleR);
		}

		private boolean center() {
			if (mBitmap == null) {
				return false;
			}
			return center(true, true);
		}

		/**
		 * 横向、纵向居中
		 */
		protected boolean center(boolean horizontal, boolean vertical) {
			boolean ret = false;
			Matrix m = new Matrix();
			m.set(mMatrix);
			RectF rect = new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
			m.mapRect(rect);

			int height = (int) rect.height();
			int width = (int) rect.width();

			int deltaX = 0, deltaY = 0;

			if (vertical) {
				// 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
				int screenHeight = mDisplayMetrics.heightPixels;
				if (height < screenHeight) {
					deltaY = (int) ((screenHeight - height) / 2 - rect.top);
				} else if (rect.top > 0) {
					deltaY = (int) -rect.top;
				} else if (rect.bottom < screenHeight) {
					deltaY = (int) (screenHeight - rect.bottom);
				}
			}

			if (horizontal) {
				int screenWidth = mDisplayMetrics.widthPixels;
				if (width < screenWidth) {
					deltaX = (int) ((screenWidth - width) / 2 - rect.left);
					ret = true;
				} else if (rect.left > 0) {
					ret = true;
					deltaX = (int) -rect.left;
				} else if (rect.right < screenWidth) {
					ret = true;
					deltaX = (int) (screenWidth - rect.right);
				}
			}
			mMatrix.postTranslate(deltaX, deltaY);
			return ret;
		}

		/**
		 * 两点的距离
		 */
		private float spacing(MotionEvent event) {
			if (event.getPointerCount() == 2) {
				float x = event.getX(0) - event.getX(1);
				float y = event.getY(0) - event.getY(1);
				return FloatMath.sqrt(x * x + y * y);
			}
			return -1;
		}

		/**
		 * 两点的中点
		 */
		private void midPoint(PointF point, MotionEvent event) {
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		}

		private void resetImg() {
			mMatrix.set(mOriMatrix);
			setImageMatrix(mMatrix);
			invalidate();
		}

		private void rotateImage() {
			resetImg();
			mMatrix.postRotate(90, DrawUtils.sWidthPixels / 2, DrawUtils.sHeightPixels / 2); // degree为:正数表示向右(顺时针),负数表示向左(逆时针)
			mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(),
					mMatrix, true);
			setBitmap(mBitmap, null);
		}

		private void setMatrix(Bitmap bm, Options options) {
			mBitmap = bm;
			if (options != null) {
				mOptions = options;
				mOriMinScaleR = Math.min((float) DrawUtils.sWidthPixels / (float) options.outWidth,
						(float) DrawUtils.sHeightPixels / (float) options.outHeight);
			}
			mMatrix.reset();
			minZoom();
			center();
			mOriMatrix.reset();
			mOriMatrix.set(mMatrix);
			setImageMatrix(mMatrix);
		}

	}

	public void setBitmap(Bitmap bm, Options options) {
		BitmapDrawable bitmapDrawable = (BitmapDrawable) ImageBrowserScaleView.this.getDrawable();
		setImageBitmap(bm);
		// 释放内存中原来的图片
		if (bitmapDrawable != null) {
			Bitmap oldBitmap = bitmapDrawable.getBitmap();
			if (oldBitmap != null) {
				if (!oldBitmap.isRecycled()) {
					// oldBitmap.recycle();
					oldBitmap = null;
				}
			}
		}
		bitmapDrawable = null;
		if (mListener == null) {
			mListener = new ScaleTouchListener(bm, options);
		} else {
			mListener.setMatrix(bm, options);
		}
	}

	public void rotateImage() {
		mListener.rotateImage();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (null == mListener) {
			return;
		}
		((Activity)mContext).getWindowManager().getDefaultDisplay().getMetrics(mListener.mDisplayMetrics);
		mListener.resetImg();
		setBitmap(mListener.mBitmap, null);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	public int getMode() {
		if (mListener != null) {
			return mListener.mMode;
		}
		return ScaleTouchListener.NONE;
	}

	public boolean onScale(MotionEvent event) {
		if (mListener != null) {
			return mListener.onTouch(event);
		}
		return false;
	}

	public boolean isOverEdge() {
		return mIsOverEdge;
	}

	public void resetImg() {
		mListener.resetImg();
	}

	public void setLoadView(View mLoadView) {
		this.mLoadView = mLoadView;
	}

	public void showLoadView() {
		if (mLoadView != null) {
			mLoadView.setVisibility(View.VISIBLE);
		}
	}

	public void hideLoadView() {
		if (mLoadView != null) {
			mLoadView.setVisibility(View.GONE);
		}
	}

	public void showDamageView() {
		if (mDamageView != null) {
			mDamageView.setVisibility(View.VISIBLE);
		}
	}

	public void hideDamageView() {
		if (mDamageView != null) {
			mDamageView.setVisibility(View.GONE);
		}
	}

	public void setDamageView(TextView textView) {
		mDamageView = textView;
	}

	public boolean isScaled() {
		if (mListener == null || mListener.mMatrix == null || mListener.mOriMatrix == null) {
			return false;
		}
		if (!mListener.mMatrix.equals(mListener.mOriMatrix)) {
			return true;
		}
		return false;
	}

	public void recyle() {
		setImageBitmap(null);
		mListener = null;
		mLoadView = null;
	}
}
