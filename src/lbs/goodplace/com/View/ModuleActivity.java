package lbs.goodplace.com.View;

import lbs.goodplace.com.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public abstract class ModuleActivity extends Activity implements OnTouchListener {
	//
	private float moveX = 0f;
	private float moveY = 0f;
	private float mClickX0 = -1;
	private float mClickY0 = -1;
	private boolean isShowTitlebarRightButton = false;
	private View.OnClickListener mBackOnclickListener;
	
	// 控件
	private LinearLayout mLayout_title;
//	private LinearLayout mLayout_Toobar;
	private LinearLayout mLayout_title_right;
	private LinearLayout mLayout_title_left;
	private RelativeLayout mRelativeLayoutTitle;
	private RelativeLayout mRelativeLayoutSearch;
	private Button Button_back;
	private Button Button_Right;
	private TextView mTextview_title;
	private ProgressBar mProgressBar;
	private ProgressDialog progressDlg; 
	public LinearLayout mLayout_body;
	private LinearLayout mLayout_title_Right;
//	private LinearLayout mLayout_toolbar_Left;
//	private LinearLayout mLayout_toolbar_Right;
	private EditText mEditTextSearch;
	private Button mButtonSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.module_view);

		mLayout_title = (LinearLayout) findViewById(R.id.Layout_title);
//		mLayout_Toobar = (LinearLayout) findViewById(R.id.Layout_toolbar);
		mLayout_title_right = (LinearLayout) findViewById(R.id.Layoutright_title_btn);
		mLayout_title_left = (LinearLayout) findViewById(R.id.Layoutleft_title_btn);
		Button_back = (Button) findViewById(R.id.title_button_left);
//		Button_Right = (Button) findViewById(R.id.title_button_right);
//		ImageView_Logo = (ImageView) findViewById(R.id.title_imageView);
		mProgressBar = (ProgressBar) findViewById(R.id.title_progressBar);
		mLayout_body = (LinearLayout) findViewById(R.id.Layout_body);
		mLayout_title_Right = (LinearLayout) findViewById(R.id.Layoutright_title);
		mTextview_title = (TextView) findViewById(R.id.title_txtView);
//		mLayout_toolbar_Left = (LinearLayout) findViewById(R.id.Layout_toolbar_Left);
//		mLayout_toolbar_Right = (LinearLayout) findViewById(R.id.Layout_toolbar_Right);
		mRelativeLayoutTitle = (RelativeLayout)findViewById(R.id.RLayout_Title);
		mRelativeLayoutSearch = (RelativeLayout)findViewById(R.id.RLayout_Search);
		mRelativeLayoutSearch.setVisibility(View.GONE);
		mButtonSearch = (Button)findViewById(R.id.button_searchbtn);
		mEditTextSearch = (EditText)findViewById(R.id.editText_search);
		mEditTextSearch.addTextChangedListener(new TextWatcher() {             
		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) { 
		    } 
		      
		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count, int after) { 
		    } 
		      
		    @Override
		    public void afterTextChanged(Editable s) { 
		    	if(mEditTextSearch.getText().toString().length() > 0){
					mButtonSearch.setVisibility(View.VISIBLE);
				}else{
					mButtonSearch.setVisibility(View.GONE);
				}
		    } 
		}); 


		Button_back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mLayout_body.setOnTouchListener(this);
		// setTitleLogo();
		// setTitlebg();
	}

	/**
	 * 设置Title图标
	 */
	public void setTitleTxt(String text){
		mTextview_title.setText(text);
	}
	
	/**
	 * 设置Title图标
	 */
	public void setTitleText(int res){
		mTextview_title.setText(res);
	}
	
	public void hideBackButton(){
		Button_back.setVisibility(View.GONE);
	}

	/**
	 * 隐藏标题
	 */
	public void hideTitle() {
		mLayout_title.setVisibility(View.GONE);
	}

//	/**
//	 * 隐藏工具栏
//	 */
//	public void hideToolbar() {
//		mLayout_Toobar.setVisibility(View.GONE);
//	}
//	
//	/**
//	 * 设置工具栏背景色
//	 * @param color Color.?
//	 */
//	public void setToolbarBgColor(int color){
//		mLayout_Toobar.setBackgroundColor(color);
//	}
	
	/**
	 * 设置Title背景色
	 * @param color Color.?
	 */
	public void setTitlebarBgColor(int color){
		mLayout_title.setBackgroundColor(color);
	}

	/**
	 * 返回按钮设置事件
	 * 
	 * @param l
	 */
	public void SetBackBtnOnClickListener(View.OnClickListener l) {
		Button_back.setOnClickListener(l);
		mBackOnclickListener = l;
	}

	/**
	 * 显示Loading
	 */
	public void showProgressBar() {
		mProgressBar.setVisibility(View.VISIBLE);
//		if(Button_Right != null)
//			Button_Right.setVisibility(View.GONE);
		
		mLayout_title_right.setVisibility(View.GONE);
	}

	/**
	 * 隐藏Loading
	 */
	public void hideProgressBar() {
		mProgressBar.setVisibility(View.GONE);
//		if(Button_Right != null && isShowTitlebarRightButton)
//			Button_Right.setVisibility(View.VISIBLE);
		
		if(isShowTitlebarRightButton)
			mLayout_title_right.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 设置itleBar右边的按钮
	 * @param b
	 */
	public void setTitleRightButton(Button b){
//		Button_Right = b;
//		mLayout_title_Right.addView(Button_Right);
//		isShowTitlebarRightButton = true;
		
		mLayout_title_Right.addView(b);
		isShowTitlebarRightButton = true;
	}
	
	/**
	 * 设置titleBar左边的按钮
	 * @param b
	 */
	public void setTitleLeftButton(Button b){
		mLayout_title_left.removeAllViews();
		mLayout_title_left.addView(b);
	}
	
//	/**
//	 * 显示TitleBar右边的按钮
//	 */
//	public void showTitleRightButton(){
//		Button_Right.setVisibility(View.VISIBLE);
//		isShowTitlebarRightButton = true;
//	}
	
	/**
	 * 显示TitleBar右边的按钮
	 */
	public void hideTitleRightButton(){
		Button_Right.setVisibility(View.GONE);
		isShowTitlebarRightButton = false;
	}
	
//	/**
//	 * 显示TitleBar右边的按钮点击事件
//	 * @param l
//	 */
//	public void setTitleRightButtonOnClickListener(View.OnClickListener l){
//		Button_Right.setOnClickListener(l);
//	}

	/**
	 * setbody
	 * 
	 * @param v
	 */
	public void setBodyView(View v) {
		mLayout_body.addView(v);
	}

	/**
	 * 底部工具条(左边)添加按钮
	 * 
	 * @param b
	 */
//	public void ToolbarAddLeftButton(View b) {
//		mLayout_toolbar_Left.addView(b);
//	}

	/**
	 * 底部工具条(右边)添加按钮
	 * 
	 * @param b
	 */
//	public void ToolbarAddRightButton(View b) {
//		mLayout_toolbar_Right.addView(b);
//	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mClickX0 = event.getRawX();
			mClickY0 = event.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			moveX = event.getX() - mClickX0;
			moveY = event.getY() - mClickY0;
			if(moveX >200){
//				Log.i("zjj", "2 right" + moveX);
				//向右滑关闭
				if(mBackOnclickListener != null)
					mBackOnclickListener.onClick(Button_back);
				else
					finish();
			}
			break;
		}
		return true;
	}
	
	/**
	 * 截取返回按钮事件
	 */
	public boolean onKeyDown(int keyCoder, KeyEvent event) {
		if (keyCoder == KeyEvent.KEYCODE_BACK) {
			if(mBackOnclickListener != null)
				mBackOnclickListener.onClick(Button_back);
			else
				finish();
			return false;
		}

		return false;
	}
	
	/**
	 * 弹出LOADING对话框
	 */
	public void ShowLoadingDialog() {
		progressDlg = new ProgressDialog(ModuleActivity.this);
		progressDlg.setMessage("请稍等...");
		progressDlg.setCancelable(true);
		progressDlg.show();
	}
	
	/**
	 * 隐藏LOADING对话框
	 */
	public void HideLoadingDialog(){
		try{
			if(progressDlg != null){
				progressDlg.dismiss();
				progressDlg = null;
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 设置搜索条是否可视
	 * @param visibility
	 */
	public void SetSearchBarVisibility(boolean visibility){
		if(visibility){
			mRelativeLayoutTitle.setVisibility(View.GONE);
			mRelativeLayoutSearch.setVisibility(View.VISIBLE);
		}else{
			mRelativeLayoutTitle.setVisibility(View.VISIBLE);
			mRelativeLayoutSearch.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 搜索按钮触发事件
	 * @param l
	 */
	public void SetSearchListener(View.OnClickListener l){
		mButtonSearch.setOnClickListener(l);
	}
	
	/**
	 * 取搜索内容
	 * @return
	 */
	public String getSearchKeyword(){
		return mEditTextSearch.getText().toString();
	}
	
	/**
	 * 设置中间文字点击事件
	 * @param l
	 */
	public void SetTitleTxtOnClickListener(View.OnClickListener l){
		mTextview_title.setOnClickListener(l);
	}
	
	/**
	 * Toast提示
	 * @param strid
	 */
	public void ShowToast(int strid){
		Toast.makeText(ModuleActivity.this, getString(strid), Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 分享
	 * @param context
	 * @param subject
	 * @param content
	 */
	public void shareContent(String subject, String content) {
		
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share) + content);
		startActivity(Intent.createChooser(intent, "Share"));
	}
}
