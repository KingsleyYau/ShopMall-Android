package lbs.goodplace.com.View.main;

import lbs.goodplace.com.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class IndexActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index_view);
		mHandler.sendEmptyMessageDelayed(0, 300);
	}
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Intent intent = new Intent(IndexActivity.this, MainActivity.class);
			startActivity(intent);
			IndexActivity.this.finish();
		}
	};
	
}
