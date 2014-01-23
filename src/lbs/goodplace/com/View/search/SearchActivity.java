package lbs.goodplace.com.View.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.categories.CategoriesActivity;
import lbs.goodplace.com.View.categories.RegionsActivity;
import lbs.goodplace.com.View.main.CredittypeActivity;
import lbs.goodplace.com.View.shops.SearchShopsActivity;

public class SearchActivity extends ModuleActivity{
	private Context mContext;
	public static int REQUESTCODE_REGIONS = 99; 
	public static int REQUESTCODE_CATEGORIES = 98; 
	
	public static final String KEY_RESULT_NAME = "RESULT_NAME";
	public static final String KEY_RESULT_ID = "RESULT_ID";
	
	//
	private String mRegionID = "";
	private String mCategoryID = "";
	private String mRegionText = "";
	private String mCategoryText = "";
	//
	private Button mButtonRegion;
	private Button mButtonCategories;
	private Button mButtonSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.search_view, mLayout_body);
		mContext = SearchActivity.this;
		
		mButtonRegion = (Button)findViewById(R.id.button_regions);
		mButtonRegion.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, RegionsActivity.class);
				i.putExtra(RegionsActivity.IS_RESULT, true);
				startActivityForResult(i, REQUESTCODE_REGIONS);
			}
		});
		
		mButtonCategories = (Button)findViewById(R.id.button_categories);
		mButtonCategories.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, CategoriesActivity.class);
				i.putExtra(CategoriesActivity.IS_RESULT, true);
				startActivityForResult(i, REQUESTCODE_CATEGORIES);
			}
		});
		
		mButtonSearch = (Button)findViewById(R.id.button_search);
		mButtonSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(mContext, SearchShopsActivity.class);
				i.putExtra(SearchShopsActivity.KEY_REGION, mRegionID);
				i.putExtra(SearchShopsActivity.KEY_CATEGORY, mCategoryID);
				i.putExtra(SearchShopsActivity.KEY_REGION_BUTTONTEXT, mRegionText);
				i.putExtra(SearchShopsActivity.KEY_CATEGORY_BUTTONTEXT, mCategoryText);
				i.putExtra(SearchShopsActivity.KEY_ISSHOW_DISTANCE,false);
				startActivity(i);
			}
		});
		
		setTitleText(R.string.searchallcategoies);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
//		if (resultCode == RESULT_OK){
			if(resultCode == REQUESTCODE_REGIONS){
				mRegionText = data.getStringExtra(KEY_RESULT_NAME);
				mButtonRegion.setText(mRegionText);
				mRegionID = data.getStringExtra(KEY_RESULT_ID);
				
			}else if(resultCode == REQUESTCODE_CATEGORIES){
				mCategoryText = data.getStringExtra(KEY_RESULT_NAME);
				mButtonCategories.setText(mCategoryText);
				mCategoryID = data.getStringExtra(KEY_RESULT_ID);
			}
//		}
	}
}
