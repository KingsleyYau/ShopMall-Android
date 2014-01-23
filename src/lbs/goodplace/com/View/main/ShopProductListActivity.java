package lbs.goodplace.com.View.main;

import java.util.ArrayList;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.controls.RefreshListView;
import lbs.goodplace.com.controls.RefreshListView.OnRefreshListener;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.obj.RequestResultModule;
import lbs.goodplace.com.obj.ShopProductInfo;
import lbs.goodplace.com.obj.ShopProductListInfo;
import lbs.goodplace.com.obj.parser.ShopProductListParser;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ShopProductListActivity extends ModuleActivity{
	public Context mContext;
	public RefreshListView mListView;
	public MyAdapter mAdapter;
	public ArrayList<ShopProductInfo> mShopProductList = new ArrayList<ShopProductInfo>();
	public int mShopId = -1;
	
	public int mCurPage = 1;
	public int mNextPage = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(R.layout.shopslist_view, mLayout_body);
		mContext = this;

		mShopId = getIntent().getIntExtra(GoodPlaceContants.KEY_SHOP_ID, -1);
		if (mShopId == -1) {
			finish();
			return;
		}
		
		setTitleText(R.string.select_shop_image_name);
		
		initView();
		requestShopProductList(mCurPage);
	}
	
	public void initView(){
		mListView = (RefreshListView) findViewById(R.id.listview);
		mAdapter = new MyAdapter(mContext);
		mListView.setAdapter(mAdapter);
		mAdapter.updateDataSource(mShopProductList);
		
		mListView.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefreshData() {
				mCurPage = 1;
				mNextPage = 1;
				requestShopProductList(mNextPage);
			}

			@Override
			public void loadNextPageData() {
				requestShopProductList(mNextPage);
			}
        });
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if (mShopProductList != null && position <= mShopProductList.size()) {
					Intent intent = new Intent();
					intent.putExtra(Contants.SHOP_PRODUCT_NAME, mShopProductList.get(position - 1));
					setResult(RESULT_OK, intent);
					ShopProductListActivity.this.finish();
				}
			}
		});
	}
	
	
	
	

	/**
	 * <br>功能简述:收藏商家
	 * <br>功能详细描述:
	 * <br>注意:
	 */
	public void requestShopProductList(int curpage) {
		byte[] postData = JsonRequestManage.getShopProductNameListPostData(mShopId, curpage);
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_SHOP_PRODUCT_LIST, postData, new ShopProductListParser(),
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null && object instanceof ShopProductListInfo) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = Contants.LOAD_SUCCESS;
							mHandler.sendMessage(msg);
							Log.i("lch", "获取产品列表成功！");
							return;
						} 
						mHandler.sendEmptyMessage(Contants.LOAD_FALSE);
						Log.i("lch", "获取产品列表失败！");
					}
				});
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				//成功
				case Contants.LOAD_SUCCESS :
					if (mNextPage == 1) {
						mListView.onRefreshComplete();
						mShopProductList.clear();
					}
					
					mCurPage = mNextPage;
					mNextPage = mNextPage + 1;
					
					ShopProductListInfo shopProductListInfo = (ShopProductListInfo) msg.obj;
					ArrayList<ShopProductInfo> shopProductList = shopProductListInfo.mShopProductList;
					
					if(shopProductList.size() == 0){
						mListView.setVisibility(View.GONE);
						return;
					}
					
					if (shopProductList != null && shopProductList.size() > 0) {
						mListView.setPageData(shopProductListInfo.mPageInfo.mPagecount , mCurPage);
						mListView.setLoadMoreFinsh();
						mShopProductList.addAll(shopProductList);
						mAdapter.notifyDataSetChanged();
					}
					break;
					
				//失败
				case Contants.LOAD_FALSE :
					Toast.makeText(mContext, "获取产品列表失败！", Toast.LENGTH_SHORT).show();
					break;
					
				default :
					break;
			}
		}

	};
	
	
	
	
	
	public class MyAdapter extends BaseAdapter {
		private Context mContext = null;
		private LayoutInflater mInflater = null;
		
		/**
		 * 数据源
		 */
		private ArrayList<ShopProductInfo> mDataSource = new ArrayList<ShopProductInfo>();

		public MyAdapter(Context context) {
			mContext = context;
			mInflater = LayoutInflater.from(context);
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
		
		
		public void updateDataSource(ArrayList<ShopProductInfo> dataSource) {
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
				convertView = mInflater.inflate(R.layout.shop_product_list_item, null);
				holder = new FeatureViewHolder();
				holder.mName = (TextView)convertView.findViewById(R.id.name);
				convertView.setTag(holder);
			} else {
				holder = (FeatureViewHolder) convertView.getTag();
			}
			ShopProductInfo info = mDataSource.get(position);
			holder.mName.setText(info.mProname);
			
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
			public TextView mName;
		}
	}
}
