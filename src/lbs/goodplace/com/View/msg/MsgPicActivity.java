package lbs.goodplace.com.View.msg;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.main.Contants;
import lbs.goodplace.com.manage.NetState;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager;
import lbs.goodplace.com.manage.util.DownImageUitl;
import lbs.goodplace.com.manage.util.FileUtil;
import lbs.goodplace.com.manage.util.GlobalUtil;
import lbs.goodplace.com.manage.util.MyMothod;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

public class MsgPicActivity extends ModuleActivity{
	public static String KEY_URL = "KEY_URL";
	//
	private Context mContext;
	private Button mButtonDownload;
	private ImageView mImageViewPic;
	
	//
	private String mUrl = "";
	private AsyncImageManager mImgManager = null;
	
	private NetState mNetState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.msgpic_view, mLayout_body);
		mContext = MsgPicActivity.this;
		mImgManager = AsyncImageManager.getInstance();
		mNetState = new NetState(mContext);
		
		//取参数
        Bundle budle = getIntent().getExtras();
		if(budle != null ){
			if(budle.containsKey(KEY_URL)){
				this.mUrl = budle.getString(KEY_URL);
			}
			
		}
		
		mImageViewPic = (ImageView)findViewById(R.id.imageView_msgpic);
		mImageViewPic.setImageBitmap(mImgManager.loadImgFromNetwork(mUrl));
//		DownImageUitl.setIcon(mImageViewPic, 
//				mUrl, 
//				FileUtil.ICON_CACHE_PATH, String.valueOf(mUrl.hashCode()), 
//				true);
		initTitlebar();
	}
	
	private void initTitlebar(){
		setTitleText(R.string.msgpicdownload);
		
		LayoutParams p = new LayoutParams(MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size) , MyMothod.Dp2Px(this, GlobalUtil.Titlebar_button_size));
		
		mButtonDownload = new Button(this);
		mButtonDownload.setLayoutParams(p);
		mButtonDownload.setBackgroundResource(R.drawable.button_download_selector);
		mButtonDownload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!mNetState.isNetUsing()){
					ShowToast(R.string.nonet);
					return;
				}
				
				DownImageUitl.downImage(mContext, mUrl, FileUtil.ICON_CACHE_PATH, Contants.Path.IMAGE_DOWN_SAVE_PATH);
			}
		});
		setTitleRightButton(mButtonDownload);
	}
	
	
}
