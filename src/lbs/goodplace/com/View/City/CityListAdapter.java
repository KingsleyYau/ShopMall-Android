package lbs.goodplace.com.View.City;

import java.util.List;

import lbs.goodplace.com.R;
import lbs.goodplace.com.obj.CityModule;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class CityListAdapter extends ArrayAdapter<CityModule>{
		private List<CityModule> mList ;
		private LayoutInflater listContainer;  
		private CityModule info;
		public boolean mIsinitfirstindexbg = true;
		
		private TextView name_temp;
		
		public CityListAdapter(Context context, List<CityModule> list) {
			super(context, 0, list);
			this.listContainer = LayoutInflater.from(context); 
			this.mList = list;
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public int getCount() {
			return mList.size();
		}
		@Override
		public CityModule getItem(int position) {
			return mList.get(position);
		}
		@Override
		public long getItemId(int position) {
			return 0;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CityViewHolder holder = null;
			info = this.getItem(position);
			if (convertView == null) {
				convertView =  listContainer.inflate(R.layout.city_list_item, null);
				holder = new CityViewHolder();
				convertView.setTag(holder);
				
				holder.name = (TextView) convertView.findViewById(R.id.textview_cityname);
				holder.mLinearLayoutbg = (LinearLayout)convertView.findViewById(R.id.LinearLayout_back);
				if(position == 0 && mIsinitfirstindexbg){
					holder.mLinearLayoutbg.setBackgroundResource(R.drawable.list_parent_click_selector);
					mIsinitfirstindexbg =false;
				}
			
			} else {
				holder = (CityViewHolder) convertView.getTag();
			}
			holder.name.setText(info.getName());
			
			return convertView;
		}
		
		public class CityViewHolder{
			public LinearLayout mLinearLayoutbg;
			public TextView name;
		}
}
