package lbs.goodplace.com.View.main;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.City.CityListActivity;
import lbs.goodplace.com.View.shops.SearchShopsActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.NetState;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.obj.CityModule;
import lbs.goodplace.com.obj.CredittypeModule;
import lbs.goodplace.com.obj.GatewayListModule;
import lbs.goodplace.com.obj.GatewayModule;
import lbs.goodplace.com.obj.parser.CredittypesParser;
import lbs.goodplace.com.obj.parser.GatwayParser;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

/**
 * 积分类型（主界面上方选择）
 * @author Administrator
 *
 */
public class CredittypeActivity extends ModuleActivity {
	public static final int LOAD_SUCCESS = 0;
	public static final int LOAD_FAILE = 1;

	//
	private Context mContext;
	private List<CredittypeModule> mListCredittype = new ArrayList<CredittypeModule>();
	private CredittypeListAdapter mCredittypeListAdapter;
	//控件
	private ListView mListViewCredittype;
	//缓存
	private String CACHE_NAME = "CredittypeActivity";
	private boolean mIsNeddCache = true;	//是否需要读取缓存
	private NetState mNetState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.credittype_list_view, mLayout_body);
		mContext = CredittypeActivity.this;
		
		mNetState = new NetState(mContext);
		initData();
		
		mListViewCredittype = (ListView)findViewById(R.id.credittype_listview);
		mCredittypeListAdapter = new CredittypeListAdapter(mContext,mListCredittype);
		mListViewCredittype.setAdapter(mCredittypeListAdapter);
		mListViewCredittype.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,SearchShopsActivity.class);
				intent.putExtra(SearchShopsActivity.KEY_CREDITTYPE,mListCredittype.get(arg2).getId());
				intent.putExtra(SearchShopsActivity.KEY_ISSHOW_REGIONS,false);
				startActivity(intent);
			}
		});
		
		setTitleText(R.string.credittype);
	}
	
	private void initData(){
//		CredittypeModule cm1 = new CredittypeModule();
//		cm1.setIconurl("http://4.su.bdimg.com/icon/10747.png");
//		cm1.setName("好去处积分");
//		
//		CredittypeModule cm2 = new CredittypeModule();
//		cm2.setIconurl("http://3.su.bdimg.com/icon/2562.png");
//		cm2.setName("联通积分");
//		
//		mListCredittype.add(cm1);
//		mListCredittype.add(cm2);
		
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		
		byte[] postData = JsonRequestManage.getAllcredittypesList();
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_CREDITTYPES_LIST, postData, new CredittypesParser(), mIsNeddCache, CACHE_NAME,
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mHandler.sendMessage(msg);// .sendMessageDelayed(msg, 2000);
							Log.i("zjj", "请求成功");
						} else {
							mHandler.sendEmptyMessage(LOAD_SUCCESS);
							Log.i("zjj", "请求失败");
						}
						
					}
			
				});
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case LOAD_SUCCESS :
					if(msg.obj!=null){
						mListCredittype.clear();
						mListCredittype.addAll((List<CredittypeModule>)msg.obj);
						mCredittypeListAdapter.notifyDataSetChanged();
						
						Log.i("zjj", "citylist success" + mListCredittype.get(0).getName());
//						GatewayListModule gatewayListModule = (GatewayListModule)msg.obj;
//						GatewayModule gwModule = gatewayListModule.gatewayList.get(0);
//						
//						GoodPlaceContants.setWGIP(gwModule.getIp() + ":" + gwModule.getPort(), gatewayListModule.mLbsappid);
						
						Log.i("zjj", "URL_CREDITTYPES_LIST URL:" + GoodPlaceContants.URL_CREDITTYPES_LIST);
					}
					break;
					
				default :
					Log.i("zjj", "fales");
					break;
			}
		}
	};
}
