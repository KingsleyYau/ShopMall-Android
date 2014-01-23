package lbs.goodplace.com.View.adapter;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.R;
import lbs.goodplace.com.component.ScoreStar;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager.AsyncImageLoadedCallBack;
import lbs.goodplace.com.manage.util.ContantsUtils;
import lbs.goodplace.com.manage.util.FileUtil;
import lbs.goodplace.com.obj.InfoModule;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoListAdapter extends BaseAdapter {
	private Context mContext = null;
	private LayoutInflater mInflater = null;
	
	/**
	 * 数据源
	 */
	private List<InfoModule> mDataSource = new ArrayList<InfoModule>();
	
	/**
	 * 默认图标
	 */
	private Bitmap mDefaultBitmap = null;

	private AsyncImageManager mImgManager = null;


	public InfoListAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mImgManager = AsyncImageManager.getInstance();
		initDefaultIcon();
	}

	/**
	 * 设置列表展现的默认图标
	 */
	public void setDefaultIcon(Drawable drawable) {
		if (drawable != null && drawable instanceof BitmapDrawable) {
			mDefaultBitmap = ((BitmapDrawable) drawable).getBitmap();
		}
	}
	
	/**
	 * 设置列表展现的默认图标
	 */
	public void initDefaultIcon() {
		mDefaultBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_icon);
	}
	

	@Override
	public int getCount() {
		if (mDataSource == null) {
			return 0;
		}
		return mDataSource.size();
	}

	@Override
	public Object getItem(int position) {
		try {
			return mDataSource.get(position);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	
	public void updateDataSource(List<InfoModule> dataSource) {
		mDataSource = dataSource;
		notifyDataSetChanged();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (mDataSource == null || position < 0 || position >= mDataSource.size()) {
			return convertView;
		}
		FeatureViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.info_view_list_item, null);
			holder = new FeatureViewHolder();
			holder.mImageSwitcher = (ImageSwitcher)convertView.findViewById(R.id.switcher);
			
			holder.mName = (TextView)convertView.findViewById(R.id.shopname);
			holder.mTitle = (TextView)convertView.findViewById(R.id.infotitle);
			holder.mInfoenddate = (TextView)convertView.findViewById(R.id.infoenddate);
			
			convertView.setTag(holder);
		} else {
			holder = (FeatureViewHolder) convertView.getTag();
		}
		
		InfoModule shopInfo = mDataSource.get(position);
		setIcon(position, holder.mImageSwitcher, shopInfo.defaultpicurl);
		
		holder.mName.setText(shopInfo.getShopname());
		holder.mTitle.setText(shopInfo.getTitle());
		String timestr= mContext.getString(R.string.msgusetime);
		timestr = String.format(timestr,ContantsUtils.formatDataYYYYMMDD(shopInfo.getInfobegindate()) + "",
				ContantsUtils.formatDataYYYYMMDD(shopInfo.getInfoenddate()) + "");
		holder.mInfoenddate.setText(timestr);
		
		return convertView;
	}

	public void setIconVisibility(boolean show, View view){
		if (show) {
			view.setVisibility(View.VISIBLE);
		}else {
			view.setVisibility(View.GONE);
		}
	}
	
	
	public class FeatureViewHolder {
		public ImageSwitcher mImageSwitcher;
		public TextView mName;
		public TextView mTitle;
		public TextView mInfoenddate;
	}
	
	
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
				imageView.setImageResource(android.R.drawable.sym_def_app_icon);
			} else {
				imageView.setImageDrawable(null);
			}
		}
	}

	private void setIcon(final int position, final ImageSwitcher switcher, String imgUrl) {
//		Log.i("lch", "setIcon");
		if (switcher.getTag() != null && switcher.getTag().equals(imgUrl)) {
			ImageView image = (ImageView) switcher.getCurrentView();
			Drawable drawable = image.getDrawable();
			if (drawable != null && drawable instanceof BitmapDrawable) {
				BitmapDrawable bDrawable = (BitmapDrawable) drawable;
				if (bDrawable.getBitmap() != null && bDrawable.getBitmap() != mDefaultBitmap) {
//					Log.i("lch", "return");
					return;
				}
			}
		}
		switcher.setTag(imgUrl);
		switcher.getCurrentView().clearAnimation();
		switcher.getNextView().clearAnimation();
		Bitmap bm = mImgManager.loadImageForList(position, FileUtil.ICON_CACHE_PATH, String.valueOf(imgUrl.hashCode()), imgUrl, true, false,
				null, new AsyncImageLoadedCallBack() {
					@Override
					public void imageLoaded(Bitmap imageBitmap, String imgUrl) {
//						Log.i("lch", "加载网络图片:" + imageBitmap.toString());
						
						if (switcher.getTag().equals(imgUrl)) {
							Drawable drawable = ((ImageView) switcher.getCurrentView()).getDrawable();
							if (drawable instanceof BitmapDrawable) {
								Bitmap bm = ((BitmapDrawable) drawable).getBitmap();
								//如果图片是默认图片，设置下一个显示的图片
								if (bm == mDefaultBitmap) {
									switcher.setImageDrawable(new BitmapDrawable(imageBitmap));
								}
							}
						} else {
							imageBitmap = null;
							imgUrl = null;
						}
					}
				});
		ImageView imageView = (ImageView) switcher.getCurrentView();
		if (bm != null) {
//			Log.i("lch", "缓存图片：" + bm.toString());
			imageView.setImageBitmap(bm);
		} else {
			imageView.setImageBitmap(mDefaultBitmap);
		}
	}
	
}
