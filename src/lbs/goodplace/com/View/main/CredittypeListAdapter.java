package lbs.goodplace.com.View.main;

import java.util.List;

import lbs.goodplace.com.R;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager.AsyncImageLoadedCallBack;
import lbs.goodplace.com.manage.util.FileUtil;
import lbs.goodplace.com.obj.CityModule;
import lbs.goodplace.com.obj.CredittypeModule;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CredittypeListAdapter extends ArrayAdapter<CredittypeModule>{
		private Context mContext;
		private List<CredittypeModule> mList ;
		private LayoutInflater listContainer;  
		private CredittypeModule info;
		/**
		 * 默认图标
		 */
		private Bitmap mDefaultBitmap = null;
		private AsyncImageManager mImgManager = null;
		
		public CredittypeListAdapter(Context context, List<CredittypeModule> list) {
			super(context, 0, list);
			this.listContainer = LayoutInflater.from(context); 
			this.mList = list;
			this.mContext = context;
			mImgManager = AsyncImageManager.getInstance();
			initDefaultIcon();
		}
		
		@Override
		public int getCount() {
			return mList.size();
		}
		@Override
		public CredittypeModule getItem(int position) {
			return mList.get(position);
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CityViewHolder holder = null;
			info = this.getItem(position);
			if (convertView == null) {
				convertView =  listContainer.inflate(R.layout.credittype_list_item, null);
				holder = new CityViewHolder();
				convertView.setTag(holder);
				
				holder.name = (TextView) convertView.findViewById(R.id.Txtview_credittype_name);
				holder.mImageSwitcher = (ImageSwitcher)convertView.findViewById(R.id.switcher);
			
			} else {
				holder = (CityViewHolder) convertView.getTag();
			}
			holder.name.setText(info.getName());
			setIcon(position, holder.mImageSwitcher, info.getIconurl(),
					FileUtil.ICON_CACHE_PATH,
					String.valueOf(info.getIconurl().hashCode()));
			return convertView;
		}
		
		public class CityViewHolder{
			public ImageSwitcher mImageSwitcher;
			public TextView name;
		}
		
		/**
		 * 设置列表展现的默认图标
		 */
		public void initDefaultIcon() {
			mDefaultBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.default_icon);
		}
		
		private void setIcon(final int position, final ImageSwitcher switcher, String imgUrl,
				String imgPath, String imgName) {
//			Log.i("lch", "setIcon");
			if (switcher.getTag() != null && switcher.getTag().equals(imgUrl)) {
				ImageView image = (ImageView) switcher.getCurrentView();
				Drawable drawable = image.getDrawable();
				if (drawable != null && drawable instanceof BitmapDrawable) {
					BitmapDrawable bDrawable = (BitmapDrawable) drawable;
					if (bDrawable.getBitmap() != null && bDrawable.getBitmap() != mDefaultBitmap) {
//						Log.i("lch", "return");
						return;
					}
				}
			}
			switcher.setTag(imgUrl);
			switcher.getCurrentView().clearAnimation();
			switcher.getNextView().clearAnimation();
			Bitmap bm = mImgManager.loadImageForList(position, imgPath, imgName, imgUrl, true, false,
					null, new AsyncImageLoadedCallBack() {
						@Override
						public void imageLoaded(Bitmap imageBitmap, String imgUrl) {
//							Log.i("lch", "加载网络图片:" + imageBitmap.toString());
							
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
//				Log.i("lch", "缓存图片：" + bm.toString());
				imageView.setImageBitmap(bm);
			} else {
				imageView.setImageBitmap(mDefaultBitmap);
			}
		}
		
}
