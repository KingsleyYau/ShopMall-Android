package lbs.goodplace.com.View.msg;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.ModuleActivity;
import lbs.goodplace.com.View.login.EditUserinfoAcitivity;
import lbs.goodplace.com.View.main.MainActivity;
import lbs.goodplace.com.View.myinfo.MyInfoActivity;
import lbs.goodplace.com.manage.NetState;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

/**
 * 资讯购买或兑换处理结果
 * @author Administrator
 *
 */
public class GetInfoSucceedAcitvity extends ModuleActivity{
	private Button mButtonBack;
	private Button mButtonGotoMyinfolist;
	
	private Context mContext;
	private NetState mNetState;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.getinfosucceed_view, mLayout_body);
		
		mContext = GetInfoSucceedAcitvity.this;
		mNetState = new NetState(mContext);
		
		mButtonBack = (Button)findViewById(R.id.button_back);
		mButtonBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mButtonGotoMyinfolist = (Button)findViewById(R.id.button_tomyinfolist);
		mButtonGotoMyinfolist.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!mNetState.isNetUsing()){
					((MainActivity)mContext).ShowToast(R.string.nonet);
					return;
				}
				
				Intent intent = new Intent(mContext, MyInfoActivity.class);
				mContext.startActivity(intent);
				
				finish();
			}
		});
	}
}
