package lbs.goodplace.com.View.main;

import java.util.List;

import lbs.goodplace.com.R;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager.AsyncImageLoadedCallBack;
import lbs.goodplace.com.manage.util.FileUtil;
import lbs.goodplace.com.obj.MainMenuItem;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainAdapter extends ArrayAdapter<MainMenuItem> {
	private List<MainMenuItem> itemList ;
	private LayoutInflater listContainer;  
//	private BuyerMainActivity context;
	private MainMenuItem info;
	private AsyncImageManager mImgManager = null;
	
	public MainAdapter(Context context, List<MainMenuItem> itemList) {
		super(context, 0, itemList);
//		this.context = (BuyerMainActivity) context;
		this.listContainer = LayoutInflater.from(context); 
		this.itemList = itemList;
		mImgManager = AsyncImageManager.getInstance();
	}

	@Override
	public int getCount() {
		return itemList.size();
	}

	@Override
	public MainMenuItem getItem(int position) {
		return itemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder = null;
		info = this.getItem(position);
		if (convertView == null) {
			convertView =  listContainer.inflate(R.layout.main_item, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
			
			holder.img = (ImageView)convertView.findViewById(R.id.main_item_imageView);
//			holder.txt = (TextView)convertView.findViewById(R.id.main_item_textView);
			holder.numTxt = (TextView)convertView.findViewById(R.id.main_item_number_txtv);
			holder.numTxt1 = (TextView)convertView.findViewById(R.id.main_item_bottomnumber_txtv1);
			holder.numTxt2 = (TextView)convertView.findViewById(R.id.main_item_bottomnumber_txtv2);
			holder.bottomlayout = (LinearLayout)convertView.findViewById(R.id.main_item_bottom);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.img.setBackgroundResource(info.getPicResId());
		if(!info.getmIconUrl().equals("")){
//			Bitmap bm = mImgManager.loadImgFromNetwork(info.getmIconUrl());
//			Log.i("zjj", "-----------mian find view icon --------:" + bm.getHeight());
//			holder.img.setImageBitmap(bm);
			
			setIcon(holder.img, info.getmIconUrl(), FileUtil.ICON_CACHE_PATH, String.valueOf(info.getmIconUrl().hashCode()), true);
		}else{
			if (info.getmBitmap() != null) {
				holder.img.setImageBitmap(info.getmBitmap());
			} else {
				holder.img.setImageBitmap(null);
			}
		}
		
//		holder.txt.setText(info.getName());
		
		if(info.getCount() > 0){
			holder.numTxt.setText(String.valueOf(info.getCount()));
		}else{
			holder.numTxt.setVisibility(View.GONE);
		}
		
		if(info.isShowBottom){
			holder.bottomlayout.setVisibility(View.VISIBLE);
			holder.numTxt1.setText(info.getBottomNum1());
			holder.numTxt2.setText(info.getBottomNum2());
		}else{
			holder.bottomlayout.setVisibility(View.GONE);
		}
		
		
		return convertView;
    }
    
    class ViewHolder{
    	ImageView img;
//    	TextView txt;
    	TextView numTxt;
    	TextView numTxt1;
    	TextView numTxt2;
    	LinearLayout bottomlayout;
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
				imageView.setImageResource(R.drawable.no_pic_big);
			} else {
				imageView.setImageDrawable(null);
			}
		}
	}
}
