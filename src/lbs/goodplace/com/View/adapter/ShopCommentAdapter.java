package lbs.goodplace.com.View.adapter;

import java.util.List;

import lbs.goodplace.com.R;
import lbs.goodplace.com.component.ScoreStar;
import lbs.goodplace.com.manage.util.ContantsUtils;
import lbs.goodplace.com.obj.ShopCommontInfo;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ShopCommentAdapter extends BaseAdapter {
	private Context mContext = null;
	private LayoutInflater mInflater = null;

	/**
	 * 数据源
	 */
	private List<ShopCommontInfo> mDataSource;

	public ShopCommentAdapter(Context context) {
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

	public void updateDataSource(List<ShopCommontInfo> dataSource) {
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
			convertView = mInflater.inflate(R.layout.shop_comment_list_item,
					null);
			holder = new Holder();
			holder.uerName = (TextView) convertView.findViewById(R.id.user_name);
			holder.onePersonPrice = (TextView) convertView.findViewById(R.id.one_person_price);
			
			
			holder.mScoreStar = (ScoreStar)convertView.findViewById(R.id.scoreStar);
			holder.mContent = (TextView) convertView.findViewById(R.id.content);
			holder.mTime = (TextView) convertView.findViewById(R.id.post_time);
			holder.mArrowImage = (ImageView) convertView.findViewById(R.id.arrow_image);
			
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		ShopCommontInfo shopCommentInfo = mDataSource.get(position);
		holder.uerName.setText(shopCommentInfo.mUserInfo.mUsernickname);
		holder.mScoreStar.setStar(shopCommentInfo.mScore);
		holder.mContent.setText(shopCommentInfo.mReviewbody);
		holder.mTime.setText(ContantsUtils.formatData(shopCommentInfo.mPosttime));
//		holder.onePersonPrice.setText(shopCommentInfo.mReviewbody.);
		
		if (shopCommentInfo.mIsShowAllBody) {
			holder.mContent.setMaxHeight(1000);
			holder.mArrowImage.setImageResource(R.drawable.arrow_up);
		}else {
			holder.mContent.setMaxHeight(150);
			holder.mArrowImage.setImageResource(R.drawable.arrow_down);
		}
		if (shopCommentInfo.mReviewbody.length() > 200) {
			holder.mArrowImage.setVisibility(View.VISIBLE);
		}else {
			holder.mArrowImage.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	public class Holder {
		public TextView uerName;
		public TextView mContent;
		public TextView mTime;
		public TextView onePersonPrice;
		public ImageView mArrowImage;
		
		public ScoreStar mScoreStar;
	}

}
