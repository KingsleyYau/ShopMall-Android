package lbs.goodplace.com.View.categories;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.adapter.RegionsListAdapter;
import lbs.goodplace.com.View.adapter.RegionsListAdapter.CategoryViewHolder;
import lbs.goodplace.com.View.search.SearchActivity;
import lbs.goodplace.com.View.shops.SearchShopsActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.NetState;
import lbs.goodplace.com.manage.requestmanage.JsonRequestManage;
import lbs.goodplace.com.manage.requestmanage.RequestManager;
import lbs.goodplace.com.manage.requestmanage.RequestManager.IDataListener;
import lbs.goodplace.com.obj.RegionModule;
import lbs.goodplace.com.obj.parser.RegionsParser;
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
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 商圈分类
 * @author Administrator
 *
 */
public class RegionsActivity extends ModuleActivity{
	public static final int LOAD_SUCCESS = 0;
	public static final int LOAD_FAILE = 1;
	public static final String IS_RESULT = "IS_RESULT"; //是否需要选择后关闭窗体并有返回值
	
	public Context mContext;

	//
	private LinearLayout mLinearLayoutbg;
	private ListView mListViewCategoryParent = null;
	private ListView mListViewCategoryNode = null;
	
	//
	private List<RegionModule> mListCategoryParent = new ArrayList<RegionModule>();
	private List<RegionModule> mListCategoryNode = new ArrayList<RegionModule>();
	private RegionsListAdapter mAdapterCategoryParent;
	private RegionsListAdapter mAdapterCategoryNote;
	private String mCategoryParentId = "0";	//行业分类ID
	private String mCategoryid = "";	//行业ID
	private boolean isResult = false; 	//是否需要选择后关闭窗体并有返回值
	
	//缓存
	private String CACHE_NAME = "RegionsActivity";
	private boolean mIsNeddCache = true;	//是否需要读取缓存
	private NetState mNetState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.popupwindow_rank_view, mLayout_body);
		mContext = this;

		//取参数
        Bundle budle = getIntent().getExtras();
		if(budle != null ){
			if(budle.get(IS_RESULT) != null){
				isResult = true;
			}	
		}
		
		mLinearLayoutbg  = (LinearLayout)findViewById(R.id.LLayout_rankview_bg);
		mLinearLayoutbg.setBackgroundResource(R.drawable.bg1);
		
		if(mListViewCategoryParent == null){
			mListViewCategoryParent = (ListView)findViewById(R.id.listview_Province);
			mAdapterCategoryParent  = new RegionsListAdapter(mContext, mListCategoryParent);
			mListViewCategoryParent.setAdapter(mAdapterCategoryParent);
			
			mAdapterCategoryParent.mIsFoucesChangeSelector = true;
		}
		
		if(mListViewCategoryNode == null){
			mListViewCategoryNode = (ListView)findViewById(R.id.listview_City);
			mAdapterCategoryNote  = new RegionsListAdapter(mContext, mListCategoryNode);
			mListViewCategoryNode.setAdapter(mAdapterCategoryNote);
		}
		
		mListViewCategoryParent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				mAdapterCategoryParent.mFoucesIndex = arg2;
				mAdapterCategoryParent.notifyDataSetInvalidated();
				
				mCategoryParentId = mListCategoryParent.get(arg2).getId();

				if(mCategoryParentId.equals("")){
					mCategoryid = mListCategoryParent.get(arg2).getId();
					
					mListCategoryNode.clear();
					mAdapterCategoryNote.notifyDataSetChanged();	//一定要这句
					
					if(isResult){
						Intent i = new Intent();
						i.putExtra(SearchActivity.KEY_RESULT_NAME, mListCategoryParent.get(arg2).getName());
						i.putExtra(SearchActivity.KEY_RESULT_ID,mCategoryid);
						setResult(SearchActivity.REQUESTCODE_REGIONS, i);
						finish();
					}
				}else{
					getCategorytype();
				}
			}
		});
		mListViewCategoryNode.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				mCategoryModule = mListCategoryNode.get(arg2);
//				RefalshUI();
				mCategoryid = mListCategoryNode.get(arg2).getId();
				
				if(isResult){
					Intent i = new Intent();
					i.putExtra(SearchActivity.KEY_RESULT_NAME, mListCategoryNode.get(arg2).getName());
					i.putExtra(SearchActivity.KEY_RESULT_ID,mCategoryid);
					setResult(SearchActivity.REQUESTCODE_REGIONS, i);
					finish();
				}
			}
		});
		
		mNetState = new NetState(mContext);
		
		setTitleText(R.string.chooseregions);
		getCategorytype();
	}

	/**
	 * 取行业分类数据
	 */
	private void getCategorytype(){
		if(mNetState.isNetUsing()){
			mIsNeddCache = false;
		}else{
			mIsNeddCache = true;
		}
		
		byte[] postData = JsonRequestManage.getRegionsListPostData(mCategoryParentId);
		RequestManager.loadDataFromNet(mContext, GoodPlaceContants.URL_REGIONS, postData, new RegionsParser(), mIsNeddCache, CACHE_NAME+mCategoryParentId,
				new IDataListener(){

					@Override
					public void loadFinish(boolean success, Object object) {
						if (success && object != null) {
							// 还在子线程操作，handler是放回主线程处理
							Message msg = new Message();
							msg.obj = object;
							msg.what = LOAD_SUCCESS;
							mCategoriesHandler.sendMessage(msg);// .sendMessageDelayed(msg, 2000);
						} else {
							mCategoriesHandler.sendEmptyMessage(LOAD_SUCCESS);
						}
					}
			
				});
	}
	
	private Handler mCategoriesHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
				case LOAD_SUCCESS :
					if(msg.obj!=null){
						if(mCategoryParentId.equals("0")){
							mListCategoryParent.clear();
							
							//添加全部
							RegionModule rmall = new RegionModule();
							rmall.setId("");
							rmall.setName(mContext.getString(R.string.allregions));
							mListCategoryParent.add(rmall);
							
							//
							mListCategoryParent.addAll((List<RegionModule>)msg.obj);
							
							if(mListCategoryParent != null){
								Log.i("zjj", "getCategorytype 请求成功1:" + mListCategoryParent.get(0).getName());
								mAdapterCategoryParent.notifyDataSetChanged();
								
								mCategoryParentId = mListCategoryParent.get(0).getId();
								getCategorytype();
							}
						}else if(mCategoryParentId.equals("")){
							break;
						}else{
							mListCategoryNode.clear();
							
							//将父类添加到第一项中
							for(int i = 0 ; i < mListCategoryParent.size(); i++){
								if(mListCategoryParent.get(i).getId().equals(mCategoryParentId)){
//									RegionModule allcm = mListCategoryParent.get(i);
//									allcm.setName(mContext.getResources().getString(R.string.all) + allcm.getName());
//									mListCategoryNode.add(allcm);
									
									RegionModule rm = new RegionModule(); 
									rm.setId(mListCategoryParent.get(i).getId());
									rm.setLat(mListCategoryParent.get(i).getLat());
									rm.setLng(mListCategoryParent.get(i).getLng());
									rm.setName(mContext.getResources().getString(R.string.all) + mListCategoryParent.get(i).getName());
									rm.setParentid(mListCategoryParent.get(i).getId());
									mListCategoryNode.add(rm);
								}
							}
							
							mListCategoryNode.addAll((List<RegionModule>)msg.obj);
							
							if(mListCategoryNode != null){
								Log.i("zjj", "getCategorytype 请求成功2:" + mListCategoryNode.get(0).getName());
								mAdapterCategoryNote.notifyDataSetChanged();
							}
						}
					}
					break;
					
				default :
					Log.i("zjj", "getCategorytype 请求失败");
					break;
			}
		}
	};
}
