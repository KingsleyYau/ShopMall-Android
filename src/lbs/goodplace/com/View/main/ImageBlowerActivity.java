package lbs.goodplace.com.View.main;

import java.util.ArrayList;

import lbs.goodplace.com.R;
import lbs.goodplace.com.obj.ShopImageInfo;
import android.app.Activity;
import android.os.Bundle;

import com.lee.beautifulgirl.imageBrows.ImageBrowserView;

public class ImageBlowerActivity extends Activity {
	private ImageBrowserView mBrowserView; // 图片浏览器控件

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_browser_main_layout);

		mBrowserView = (ImageBrowserView) findViewById(R.id.image_browser_main_view);
		ArrayList<ShopImageInfo> mDataSource = getIntent().getParcelableArrayListExtra(Contants.SHOP_IMAGE_LIST);
		ShopImageInfo info = mDataSource.get(mDataSource.size() - 1); //取消最后一个load对象
		if (info.mIsProgressBar == 1) {
			mDataSource.remove(info);
		}
		
		// 点击位置
		int pos = getIntent().getIntExtra(Contants.SHOP_IMAGE_CLICK_POSITION, 0);
		if (mDataSource.size() < 1) {
			finish();
		} else {
			mBrowserView.setAnimationBitmap(mDataSource.get(pos), mDataSource);
		}
	}


	@Override
	protected void onDestroy() {
		mBrowserView.recyle();
		super.onDestroy();
	}
}
