package lbs.goodplace.com.View.main;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import lbs.goodplace.com.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * <br>类描述:交通信息详细信息类
 * <br>功能详细描述:
 * 
 * @author  licanhui
 * @date  [2013-4-26]
 */
public class ShopTrafficActivity extends Activity implements OnClickListener{
	public  ImageView mBtnBack;
	public  LinearLayout mTrafficLayout; //交通内容布局
//	public  TextView mTrafficTextView; //交通内容
	public  TextView mShopInfoTextView; //商家详情
	
	public  WebView mTrafficeWebView; //交通webview

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_traffic_view);
//		mWebView.loadDataWithBaseURL("about:blank", notice1, "text/html", "utf-8", null);  
		
		initView();
		setData();
	}

	
	public void initView(){
		mBtnBack = (ImageView) findViewById(R.id.btnBack);
		mBtnBack.setOnClickListener(this);
		mTrafficLayout = (LinearLayout) findViewById(R.id.traffic_layout);
//		mTrafficTextView = (TextView) findViewById(R.id.traffic_text);
		
		mTrafficeWebView = (WebView) findViewById(R.id.traffic_webview);
		mTrafficeWebView.setBackgroundColor(0);
		mShopInfoTextView = (TextView) findViewById(R.id.shopinfo_text);
	}
	
	private String fmtString(String str){  
	    String notice = "";  
	    try{  
	        notice = URLEncoder.encode(str, "utf-8");  
	    }catch(UnsupportedEncodingException ex){  
	          
	    }  
	    return notice;  
	} 
	
	public void setData(){
		String trafficString = getIntent().getStringExtra(Contants.SHOP_TRAFFIC_TEXT);
		if (!TextUtils.isEmpty(trafficString) && !trafficString.equals("null")) {
//			mTrafficTextView.setText((Html.fromHtml(trafficString)));
			mTrafficeWebView.loadDataWithBaseURL("about:blank", trafficString, "text/html", "utf-8", null);  

		}else {
			mTrafficeWebView.loadDataWithBaseURL("about:blank", getResources().getString(R.string.tip_no_traffic), "text/html", "utf-8", null);  

//			mTrafficLayout.setVisibility(View.GONE);
		}
		
		
		String shopInfoString = getIntent().getStringExtra(Contants.SHOP_INFO_TEXT);
		if (!TextUtils.isEmpty(shopInfoString)) {
			mShopInfoTextView.setText((Html.fromHtml(shopInfoString)));
		}
		
	}


	@Override
	public void onClick(View v) {
		if (v == mBtnBack) {
			finish();
		}
		
	}
	
}
