package lbs.goodplace.com.View.myinfo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.City.CityListActivity;
import lbs.goodplace.com.View.adapter.InfoListAdapter;
import lbs.goodplace.com.View.msg.MsgDetailActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.controls.RefreshListView;
import lbs.goodplace.com.controls.RefreshListView.OnRefreshListener;
import lbs.goodplace.com.manage.NetState;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.obj.InfoListModule;
import lbs.goodplace.com.obj.InfoModule;
import lbs.goodplace.com.obj.InfotypeModule;
import lbs.goodplace.com.obj.parser.AllinfoParser;

/**
 * 我的卷卷
 * @author Administrator
 *
 */
public class MyInfoActivity extends ModuleActivity{
	private static final int INFOSTATUS_CANUSE = 1;
	private static final int INFOSTATUS_USED = 2;
	private static final int INFOSTATUS_CANTUSE = 3;
	
	private static final int LOAD_SUCCESS = 0;
	private static final int LOAD_FAILE = 1;
	
	private int PAGEMAX = 10;	//每页条数
	
	private Context mContext;
	
	//控件
	private Button mBtn1;
	private Button mBtn2;
	private Button mBtn3;
	private RefreshListView mListView;
	
	//变量
	private int mStatus = INFOSTATUS_CANUSE;	//卷使用状态
	private InfoListAdapter mMyAdapter;
	private ArrayList<InfoModule> mDataSource;
	private int mCurPage = 1;
	private int mNextPage = 1;
	
	//缓存
	private String CACHE_NAME = "MyInfoActivity";
	private boolean mIsNeddCache = true;	//是否需要读取缓存
	private NetState mNetState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.myinfo_list_view, mLayout_body);
		
		mContext = MyInfoActivity.this;
		mNetState = new NetState(mContext);
		
		mBtn1 = (Button) findViewById(R.id.btn1);
		mBtn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mStatus = INFOSTATUS_CANUSE;
				refalshlist();
			}
		});
		mBtn2 = (Button) findViewById(R.id.btn2);
		mBtn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mStatus = INFOSTATUS_USED;
				refalshlist();
			}
		});
		mBtn3 = (Button) findViewById(R.id.btn3);
		mBtn3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mStatus = INFOSTATUS_CANTUSE;
				refalshlist();
			}
		});
		setTitleText(R.string.myinfo);
		initListView();
		requestRankingList();
	}
	
	public void initListView() {
		mListView = (RefreshListView) findViewById(R.id.myinfo_listview);
		// 初始化adapter
		mMyAdapter = new InfoListAdapter(mContext);
		mListView.setAdapter(mMyAdapter);

		mDataSource = new ArrayList<InfoModule>();
		mMyAdapter.updateDataSource(mDataSource);
		mListView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefreshData() {
				mCurPage = 1;
				mNextPage = 1;
				requestRankingList();
			}

			@Override
			public void loadNextPageData() {
				requestRankingList();
			}
        });
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (position - 1 >= 0 && mDataSource != null && mDataSource.size() > 0) {
					if(position- 1 < mDataSource.size()){
						Intent intent = new Intent(mContext, MyinfoDetailActivity.class);
						intent.putExtra(MyinfoDetailActivity.KEY_MYINFOID, mDataSource.get(position- 1).getMyinfoid());			//-1是因为有下拉的头
						mContext.startActivity(intent);
					}
				}
			}
		});
	
	}
	
	/**
	 * 刷新列表
	 */
	private void refalshlist(){
		mListView.hideListViewFooter();
		mCurPage = 1;
		mNextPage = 1;
		requestRankingList();
	}
	
	/**
	 * <br>功能简述:请求我的资讯列表
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	private void requestRankingList(){
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		
		byte[] postData = JsonRequestManage.getMyInfolist(mStatus,
				mNextPage,PAGEMAX);
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_MY_INFO_LIST, postData, new AllinfoParser(),mIsNeddCache, CACHE_NAME+mStatus+"_"+mNextPage,
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mHandler.sendMessage(msg);
							Log.i("lch", "请求成功");
						} else {
							mHandler.sendEmptyMessage(LOAD_FAILE);
							Log.i("lch", "请求失败");
						}
					}
				});
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				//请求商家列表成功
				case LOAD_SUCCESS :
					if (mNextPage == 1) {
						mListView.onRefreshComplete();
						mDataSource.clear();
					}
					
					mCurPage = mNextPage;
					mNextPage = mNextPage + 1;
					
					InfoListModule infolist = (InfoListModule) msg.obj;
					
					if (infolist.mInfoModuleList != null && infolist.mInfoModuleList.size() > 0) {
						mListView.setPageData(infolist.mPageInfo.mPagecount , mCurPage);
						mDataSource.addAll(infolist.mInfoModuleList);
					}else{
						mListView.hideListViewFooter();
					}
					
					mListView.setLoadMoreFinsh();
					mMyAdapter.notifyDataSetChanged();
					
					break;
				
				//请求商家列表失败
				case LOAD_FAILE :
					mListView.setLoadMoreFinsh();
					Toast.makeText(mContext, "请求商家列表失败", 0).show();
					break;
					
				default :
					mListView.setLoadMoreFinsh();
					break;
			}
		}

	};
}
