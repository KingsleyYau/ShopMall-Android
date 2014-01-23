package lbs.goodplace.com.View.main;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.offlinemap.MOfflineMapStatus;

import lbs.goodplace.com.R;
import lbs.goodplace.com.controls.RefreshListView;
import lbs.goodplace.com.controls.RefreshListView.OnRefreshListener;
import lbs.goodplace.com.manage.offlinemap.IDownPercentHandler;
import lbs.goodplace.com.manage.offlinemap.OfflinemapManage;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MoreView extends LinearLayout {
	private Context mContext;
	
	private RefreshListView mListview;
	private TextView mTextViewVersioon;
	private TextView mTextViewDownloadofflinemap;
	private OfflinemapManage om ;	//离线地图下载工具
	
	public MoreView(final Context context) {
		super(context);
		this.mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.more_view, this);

		mTextViewVersioon = (TextView)findViewById(R.id.Textview_version);
		mTextViewDownloadofflinemap = (TextView)findViewById(R.id.Textview_downloadofflinemap_HK);
		
		om = new OfflinemapManage(mContext);
		om.setDownPercentHandler(mIDownPercentHandler);
		
		//取版本号
		PackageManager pm = context.getPackageManager();   
        PackageInfo pi;
		try {
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			mTextViewVersioon.setText(getResources().getString(R.string.version) + pi.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mTextViewDownloadofflinemap.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(om.getmDownloadStatus() == MOfflineMapStatus.STOP){
					if(!om.Star(getResources().getString(R.string.hongkong))){
						((MainActivity)mContext).ShowToast(R.string.nonet);
						return;
					}
				}else{
					om.Stop();
				}
				
				
			}
		});
	}
	
	//下载地图百分比
	private IDownPercentHandler mIDownPercentHandler = new IDownPercentHandler() {
		
		@Override
		public void getPercent(int p) {
			// TODO Auto-generated method stub
			mTextViewDownloadofflinemap.setText(getResources().getString(R.string.downloadofflinemap) + ":" + p + "%");
		}
	};
}
