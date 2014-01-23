package lbs.goodplace.com.View.adapter;

import java.util.List;

import lbs.goodplace.com.R;
import lbs.goodplace.com.component.ScoreStar;
import lbs.goodplace.com.manage.util.ContantsUtils;
import lbs.goodplace.com.obj.ShopSignInfo;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopSignAdapter extends BaseAdapter {
	private Context mContext = null;
	private LayoutInflater mInflater = null;

	/**
	 * 数据源
	 */
	private List<ShopSignInfo> mDataSource;

	public ShopSignAdapter(Context context) {
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

	public void updateDataSource(List<ShopSignInfo> dataSource) {
		mDataSource = dataSource;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (mDataSource == null || position < 0
				|| position >= mDataSource.size()) {
			return convertView;
		}
		Holder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.shop_sign_list_item,
					null);
			holder = new Holder();
			holder.uerName = (TextView) convertView.findViewById(R.id.user_name);
			holder.mScoreStar = (ScoreStar)convertView.findViewById(R.id.scoreStar);
			holder.mContent = (TextView) convertView.findViewById(R.id.content);
			holder.mTime = (TextView) convertView.findViewById(R.id.post_time);
			holder.mArrowImage = (ImageView) convertView.findViewById(R.id.arrow_image);
			
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		ShopSignInfo shopCommentInfo = mDataSource.get(position);
		holder.uerName.setText(shopCommentInfo.mUserInfo.mUsernickname);
		holder.mScoreStar.setStar(shopCommentInfo.mScore);
		holder.mContent.setText(shopCommentInfo.mSignbody);
		holder.mTime.setText(ContantsUtils.formatData(shopCommentInfo.mSigntime));
		
		
		return convertView;
	}

	public class Holder {
		public TextView uerName;
		public TextView mContent;
		public TextView mTime;
		
		public ImageView mArrowImage;
		
		public ScoreStar mScoreStar;
	}

}
