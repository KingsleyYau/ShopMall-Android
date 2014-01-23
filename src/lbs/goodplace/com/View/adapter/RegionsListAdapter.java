package lbs.goodplace.com.View.adapter;

import java.util.List;

import lbs.goodplace.com.R;
import lbs.goodplace.com.obj.RegionModule;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


public class RegionsListAdapter extends ArrayAdapter<RegionModule>{
		private List<RegionModule> mList ;
		private LayoutInflater listContainer;  
		private RegionModule info;
		public boolean mIsFoucesChangeSelector = false;	//点中时是否要改变背景Selector
		public int mFoucesIndex = 0;	//要加上被选中状态的index
		private Context mContext;
		
		private TextView name_temp;
		
		public RegionsListAdapter(Context context, List<RegionModule> list) {
			super(context, 0, list);
			this.listContainer = LayoutInflater.from(context); 
			this.mList = list;
			this.mContext = context;
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public int getCount() {
			return mList.size();
		}
		@Override
		public RegionModule getItem(int position) {
			return mList.get(position);
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CategoryViewHolder holder = null;
			info = this.getItem(position);
			if (convertView == null) {
				convertView =  listContainer.inflate(R.layout.city_list_item, null);
				holder = new CategoryViewHolder();
				convertView.setTag(holder);
				
				holder.name = (TextView) convertView.findViewById(R.id.textview_cityname);
				holder.mLinearLayoutbg = (LinearLayout)convertView.findViewById(R.id.LinearLayout_back);
			
			} else {
				holder = (CategoryViewHolder) convertView.getTag();
			}
			
			holder.mLinearLayoutbg.setBackgroundResource(R.drawable.list_parent_selector);
			holder.name.setTextColor(this.mContext.getResources().getColor(R.color.black));
			if(mIsFoucesChangeSelector){
				if(position == mFoucesIndex){
					holder.mLinearLayoutbg.setBackgroundResource(R.drawable.list_parent_click_selector);
					holder.name.setTextColor(this.mContext.getResources().getColor(R.color.red));
				}
			}
			holder.name.setText(info.getName());
			
			return convertView;
		}
		
		public class CategoryViewHolder{
			public LinearLayout mLinearLayoutbg;
			public TextView name;
		}
}
