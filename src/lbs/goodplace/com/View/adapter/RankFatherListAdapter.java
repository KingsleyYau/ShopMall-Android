package lbs.goodplace.com.View.adapter;

import java.util.ArrayList;

import lbs.goodplace.com.R;
import lbs.goodplace.com.obj.ShopRankingChildInfo;
import lbs.goodplace.com.obj.ShopRankingInfo;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RankFatherListAdapter extends BaseAdapter {
	private Context mContext = null;
	private LayoutInflater mInflater = null;
	private ArrayList<ShopRankingInfo> mDataSource = new ArrayList<ShopRankingInfo>();

	public RankFatherListAdapter(Context context) {
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
	
	
	public void updateDataSource(ArrayList<ShopRankingInfo> dataSource) {
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
			convertView = mInflater.inflate(R.layout.rank_father_list_item, null);
			
			holder = new ViewHolder();
			holder.mTitleName = (TextView)convertView.findViewById(R.id.name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ShopRankingInfo info = mDataSource.get(position);
		holder.mTitleName.setText(info.mName + mContext.getResources().getString(R.string.rank));
		
		Log.i("lch", "position:" + position);
		Log.i("lch", "mIsCheck:" + info.mIsCheck);
		Log.i("lch", "--------");
		if (info.mIsCheck) {
			holder.mTitleName.setTextColor(mContext.getResources().getColor(R.color.red1));
		}else {
			holder.mTitleName.setTextColor(mContext.getResources().getColor(R.color.black));

		}
		return convertView;
	}
	
	public class ViewHolder {
		public TextView mTitleName;

	}
}
