package lbs.goodplace.com.View.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.amap.api.location.core.GeoPoint;

import lbs.goodplace.com.R;
import lbs.goodplace.com.component.ScoreStar;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.GPS.CoordinateManage;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager.AsyncImageLoadedCallBack;
import lbs.goodplace.com.manage.util.FileUtil;
import lbs.goodplace.com.obj.ShopModule;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private Context mContext = null;
	private LayoutInflater mInflater = null;
	public Handler mHandlerDelbtn;
	public boolean isDelShow = false;
//	private GeoPoint mygeppoint ;
	private DecimalFormat mdf = new DecimalFormat("0.0");

	/**
	 * 数据源
	 */
	private List<ShopModule> mDataSource = new ArrayList<ShopModule>();

	/**
	 * 默认图标
	 */
	private Bitmap mDefaultBitmap = null;

	private AsyncImageManager mImgManager = null;

	public MyAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mImgManager = AsyncImageManager.getInstance();
		initDefaultIcon();
		
//		mygeppoint = new GeoPoint((int)(GoodPlaceContants.LAT* 1e6), (int)(GoodPlaceContants.LNG * 1e6));
//		mygeppoint =  CoordinateConver.fromWgs84ToBaidu(new GeoPoint((int)(GoodPlaceContants.LAT* 1e6), (int)(GoodPlaceContants.LNG * 1e6)));
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
		mDefaultBitmap = BitmapFactory.decodeResource(mContext.getResources(),
				R.drawable.no_pic);
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

	public void updateDataSource(List<ShopModule> dataSource) {
		mDataSource = dataSource;
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (mDataSource == null || position < 0
				|| position >= mDataSource.size()) {
			return convertView;
		}
		FeatureViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.ranking_view_list_item,
					null);
			holder = new FeatureViewHolder();
			holder.mImageSwitcher = (ImageSwitcher) convertView
					.findViewById(R.id.switcher);
			holder.mName = (TextView) convertView.findViewById(R.id.name);

			holder.mPrice = (TextView) convertView.findViewById(R.id.price);
			holder.mScoreStar = (ScoreStar) convertView
					.findViewById(R.id.scoreStar);
			holder.mAdress = (TextView) convertView.findViewById(R.id.adress);
			holder.mDistance = (TextView) convertView
					.findViewById(R.id.distance);

			holder.mIconDiscount = (ImageView) convertView
					.findViewById(R.id.icon_discount);
			holder.mIconPromo = (ImageView) convertView
					.findViewById(R.id.icon_promo);
			holder.mIconGift = (ImageView) convertView
					.findViewById(R.id.icon_gift);
			holder.mIconCard = (ImageView) convertView
					.findViewById(R.id.icon_card);
			holder.mIconHyf = (ImageView) convertView
					.findViewById(R.id.icon_hyf);
			holder.mIconDsf = (ImageView) convertView
					.findViewById(R.id.icon_dsf);
			holder.mButtonDel= (Button) convertView.findViewById(R.id.button_del);
			if(isDelShow)
				holder.mButtonDel.setVisibility(View.VISIBLE);

			convertView.setTag(holder);
		} else {
			holder = (FeatureViewHolder) convertView.getTag();
		}

		ShopModule shopInfo = mDataSource.get(position);
		setIcon(position, holder.mImageSwitcher, shopInfo.defaultpicurl);

		holder.mName.setText(shopInfo.name);
		holder.mScoreStar.setStar(shopInfo.score);
		holder.mPrice.setText(shopInfo.pricetext);
		holder.mAdress.setText(shopInfo.regionname + "  "
				+ shopInfo.categoryname);

		String distanceString = getDistance(shopInfo.lat,shopInfo.lng);
		holder.mDistance.setText(distanceString);
		
//		GeoPoint shopgeppoint = CoordinateConver.fromWgs84ToBaidu(new GeoPoint((int)(shopInfo.lat* 1e6), (int)(shopInfo.lng * 1e6)));
////		Log.i("zjj", "我的LAT:" + mygeppoint.getLatitudeE6()  + ",店的LAT:" + shopgeppoint.getLatitudeE6());
//		double distance = DistanceUtil.getDistance(mygeppoint, shopgeppoint);
//		distance = distance/1000;
//		
//		holder.mDistance.setText(distance>5?">5km":mdf.format(distance) + "km" );

		setIconVisibility(shopInfo.isdiscount, holder.mIconDiscount);
		setIconVisibility(shopInfo.ispromo, holder.mIconPromo);
		setIconVisibility(shopInfo.isgift, holder.mIconGift);
//		setIconVisibility(shopInfo.iscard, holder.mIconCard);
//		setIconVisibility(shopInfo.ishyf, holder.mIconHyf);
//		setIconVisibility(shopInfo.isdsf, holder.mIconDsf);
		
		holder.mButtonDel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mHandlerDelbtn != null){
					Message msg = new Message();
					msg.arg1 = position;
					mHandlerDelbtn.sendMessage(msg);
				}
			}
		});
		
		return convertView;
	}

	public void setIconVisibility(boolean show, View view) {
		if (show) {
			view.setVisibility(View.VISIBLE);
		} else {
			view.setVisibility(View.GONE);
		}
	}

	public class FeatureViewHolder {
		public TextView mName;
		public ImageSwitcher mImageSwitcher;
		public ScoreStar mScoreStar;
		public TextView mAdress;
		public TextView mPrice;
		public TextView mDistance;
		public ImageView mIconDiscount;
		public ImageView mIconPromo;
		public ImageView mIconGift;
		public ImageView mIconCard;
		public ImageView mIconHyf;
		public ImageView mIconDsf;
		public Button mButtonDel;
	}

	/**
	 * 读取图标，然后设到imageview里
	 */
	private void setIcon(final ImageView imageView, String imgUrl,
			String imgPath, String imgName, boolean setDefaultIcon) {
		imageView.setTag(imgUrl);
		Bitmap bm = mImgManager.loadImage(imgPath, imgName, imgUrl, true,
				false, null, new AsyncImageLoadedCallBack() {
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

	private void setIcon(final int position, final ImageSwitcher switcher,
			String imgUrl) {
		// Log.i("lch", "setIcon");
		if (switcher.getTag() != null && switcher.getTag().equals(imgUrl)) {
			ImageView image = (ImageView) switcher.getCurrentView();
			Drawable drawable = image.getDrawable();
			if (drawable != null && drawable instanceof BitmapDrawable) {
				BitmapDrawable bDrawable = (BitmapDrawable) drawable;
				if (bDrawable.getBitmap() != null
						&& bDrawable.getBitmap() != mDefaultBitmap) {
					// Log.i("lch", "return");
					return;
				}
			}
		}
		switcher.setTag(imgUrl);
		switcher.getCurrentView().clearAnimation();
		switcher.getNextView().clearAnimation();
		Bitmap bm = mImgManager.loadImageForList(position,
				FileUtil.ICON_CACHE_PATH, String.valueOf(imgUrl.hashCode()),
				imgUrl, true, false, null, new AsyncImageLoadedCallBack() {
					@Override
					public void imageLoaded(Bitmap imageBitmap, String imgUrl) {
						// Log.i("lch", "加载网络图片:" + imageBitmap.toString());

						if (switcher.getTag().equals(imgUrl)) {
							Drawable drawable = ((ImageView) switcher
									.getCurrentView()).getDrawable();
							if (drawable instanceof BitmapDrawable) {
								Bitmap bm = ((BitmapDrawable) drawable)
										.getBitmap();
								// 如果图片是默认图片，设置下一个显示的图片
								if (bm == mDefaultBitmap) {
									switcher.setImageDrawable(new BitmapDrawable(
											imageBitmap));
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
			// Log.i("lch", "缓存图片：" + bm.toString());
			imageView.setImageBitmap(bm);
		} else {
			imageView.setImageBitmap(mDefaultBitmap);
		}
	}

	/**
	 * 计算2个经纬度的距离
	 * @param lat1 Google坐标系
	 * @param lng1 Google坐标系
	 * @return
	 */
	public String getDistance(double lat1, double lng1) {
		float[] results= new float[1];
		//火星坐标系计算
		Location.distanceBetween(CoordinateManage.getLatGG2HX(lat1, lng1),
				CoordinateManage.getLngGG2HX(lat1, lng1),
				GoodPlaceContants.HXLAT, GoodPlaceContants.HXLNG, results);
		DecimalFormat df = new DecimalFormat("0.0");
		float distance = results[0]/1000;
		if (distance > 5) {
			return "> 5km";
		}else {
			String distanceString = df.format(results[0]/1000) + "km";
			return distanceString;
		}
	}
	
	
}
