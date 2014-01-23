package lbs.goodplace.com.View.adapter;

import java.util.ArrayList;

import lbs.goodplace.com.R;
import lbs.goodplace.com.obj.ShopRankingChildInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RankListAdapter extends BaseAdapter {
	private Context mContext = null;
	private LayoutInflater mInflater = null;
	private ArrayList<ShopRankingChildInfo> mDataSource = new ArrayList<ShopRankingChildInfo>();

	public RankListAdapter(Context context) {
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
	
	
	public void updateDataSource(ArrayList<ShopRankingChildInfo> dataSource) {
		mDataSource = dataSource;
		notifyDataSetChanged();
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (mDataSource == null || position < 0 || position >= mDataSource.size()) {
			return convertView;
		}
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.rank_list_item, null);
			
			holder = new ViewHolder();
			holder.mTitleName = (TextView)convertView.findViewById(R.id.titleName);
			holder.mChildName = (TextView)convertView.findViewById(R.id.childName);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ShopRankingChildInfo info = mDataSource.get(position);
		
//		if (info.mIsFarter) {
//			holder.mTitleName.setText(info.mName);
//			holder.mTitleName.setVisibility(View.VISIBLE);
//			holder.mChildName.setVisibility(View.GONE);
//		}else {
			holder.mChildName.setText(info.mName);
			holder.mChildName.setVisibility(View.VISIBLE);
			holder.mTitleName.setVisibility(View.GONE);
//		}
		return convertView;
	}
	
	public class ViewHolder {
		public TextView mTitleName;
		public TextView mChildName;

	}
}
