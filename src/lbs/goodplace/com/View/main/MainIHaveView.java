package lbs.goodplace.com.View.main;

import java.util.ArrayList;
import java.util.List;

import lbs.goodplace.com.R;
import lbs.goodplace.com.controls.RefreshListView;
import lbs.goodplace.com.controls.RefreshListView.OnRefreshListener;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

public class MainIHaveView extends LinearLayout {
	private Context mContext;
	
	private RefreshListView mListview;
	
	public MainIHaveView(final Context context) {
		super(context);
		this.mContext = context;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.mainihave_view, this);

		mListview = (RefreshListView)findViewById(R.id.ihaveListview);
		mListview.setCacheColorHint(Color.WHITE) ;
		//一定要SetAdapter先会有“下拉刷新”果个头(set个空的Adapter都得)
		mListview.setAdapter(new ArrayAdapter<String>(mContext, android.R.layout.simple_expandable_list_item_1,getData())); 
		mListview.setonRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefreshData() {
				initData();
				
			}

			@Override
			public void loadNextPageData() {
				// TODO Auto-generated method stub
				
			}
        });
//		setListviewHead();
	}

	/**
	 * 控制列表头
	 */
	private void setListviewHead(){
		//列表头
		if (mListview.getCount() > 1) {		//1是因为有HEADVIEW
			mListview.hideHeadView();
		} else {
			mListview.setHeadViewVisible();
		}
	}
	
	/**
	 * 初始化 按钮数据
	 */
	private void initData() {
		
	}
	
	
	
	private List<String> getData(){ 
        
        List<String> data = new ArrayList<String>(); 
        data.add("测试数据1"); 
        data.add("测试数据2"); 
        data.add("测试数据3"); 
        data.add("测试数据4"); 
          
        return data; 
    } 

}
