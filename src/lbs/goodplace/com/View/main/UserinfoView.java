package lbs.goodplace.com.View.main;

import java.io.File;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.contant.ImageUntil;
import lbs.goodplace.com.View.contant.Machine;
import lbs.goodplace.com.View.fav.MyFavshopsActivity;
import lbs.goodplace.com.View.login.EditUserinfoAcitivity;
import lbs.goodplace.com.View.login.LoginActivity;
import lbs.goodplace.com.View.myinfo.MyInfoActivity;
import lbs.goodplace.com.controls.GoodPlaceContants;
import lbs.goodplace.com.manage.NetState;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager;
import lbs.goodplace.com.manage.imagemanage.AsyncImageManager.AsyncImageLoadedCallBack;
import lbs.goodplace.com.manage.util.FileUtil;
import lbs.goodplace.com.manage.util.GlobalUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 我有（用户信息）
 * @author Administrator
 *
 */
public class UserinfoView extends LinearLayout {
	public static final int LOAD_SUCCESS = 0;
	public static final int LOAD_FAILE = 1;
	public final static int REQUEST_CODE_CAMERA = 1;
	public final static int REQUEST_CODE_ALBUM = 2;
	//
	private Context mContext;
	private AsyncImageManager mImgManager = null;
	private NetState mNetState;
	
	//
	private RelativeLayout mRLayoutUserinfo;
	private ImageView mImageViewUser;
	private TextView mTextViewUsername;
	private TextView mTextViewSpscorenum;
	private TextView mTextViewOtherscorenum;
	private TextView mTextViewFavnum;
	private TextView mTextViewPicnum;
	private TextView mTextViewReviewnum;
	private TextView mTextViewCheckinnum;
	private TextView mTextViewMyinfonum;
	private Button mButtonLogin;
	private LinearLayout mLinearLayoutMyfav;
	private LinearLayout mLinearLayoutMysign;
	private LinearLayout mLinearLayoutMyreviews;
	private LinearLayout mLinearLayoutMypic;
	private LinearLayout mLinearLayoutMyinfo;
	private Button mButtonEdit;
	
	public UserinfoView(final Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.userinfo_view, this);

		mContext = context;
		mImgManager = AsyncImageManager.getInstance();
		mNetState = new NetState(mContext);
		
		mRLayoutUserinfo = (RelativeLayout)findViewById(R.id.RLayout_userinfo);
		mTextViewUsername = (TextView)findViewById(R.id.textView_usernickname);
		mTextViewSpscorenum = (TextView)findViewById(R.id.Txtview_spscore);
		mTextViewOtherscorenum = (TextView)findViewById(R.id.Txtview_otherscore);
		mTextViewFavnum = (TextView)findViewById(R.id.Txtview_favnum);
		mTextViewPicnum = (TextView)findViewById(R.id.Txtview_picturenum);
		mTextViewReviewnum = (TextView)findViewById(R.id.Txtview_reviewnum);
		mTextViewCheckinnum = (TextView)findViewById(R.id.Txtview_checkinnum);
		mTextViewMyinfonum = (TextView)findViewById(R.id.Txtview_myinfonum);
		mLinearLayoutMyfav = (LinearLayout)findViewById(R.id.LLayout_myfav);
		mLinearLayoutMyfav.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//先判断是否登录了
				if (GoodPlaceContants.USERINFO == null) {
					showLoginMsg();
					return;
				}
				
				//
				Intent i = new Intent(mContext, MyFavshopsActivity.class);
				mContext.startActivity(i);
			}
		});
		
		mLinearLayoutMypic = (LinearLayout)findViewById(R.id.LLayout_mypic);
		mLinearLayoutMypic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//先判断是否登录了
				if (GoodPlaceContants.USERINFO == null) {
					showLoginMsg();
					return;
				}
				
				//
				System.gc();
				
				Intent i = new Intent(mContext, ShopImageListActivity.class);
				i.putExtra(ShopSignWallActivity.KEY_MYSIGN, true);
				mContext.startActivity(i);
			}
		});
		
		mLinearLayoutMyreviews = (LinearLayout)findViewById(R.id.LLayout_myreviews);
		mLinearLayoutMyreviews.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//先判断是否登录了
				if (GoodPlaceContants.USERINFO == null) {
					showLoginMsg();
					return;
				}
				
				//
				Intent i = new Intent(mContext, ShopCommontActivity.class);
				i.putExtra(ShopSignWallActivity.KEY_MYSIGN, true);
				mContext.startActivity(i);
			}
		});
		
		mLinearLayoutMysign = (LinearLayout)findViewById(R.id.LLayout_mysign);
		mLinearLayoutMysign.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//先判断是否登录了
				if (GoodPlaceContants.USERINFO == null) {
					showLoginMsg();
					return;
				}
				
				//
				Intent i = new Intent(mContext, ShopSignWallActivity.class);
				i.putExtra(ShopSignWallActivity.KEY_MYSIGN, true);
				mContext.startActivity(i);
			}
		});
		
		mLinearLayoutMyinfo = (LinearLayout)findViewById(R.id.LLayout_myinfo);
		mLinearLayoutMyinfo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//先判断是否登录了
				if (GoodPlaceContants.USERINFO == null) {
					showLoginMsg();
					return;
				}
				
				//
				Intent i = new Intent(mContext, MyInfoActivity.class);
				mContext.startActivity(i);
			}
		}); 
		
		mImageViewUser = (ImageView)findViewById(R.id.imageView_user);
		mImageViewUser.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doPickPhotoAction() ;
			}
		});
		
		mButtonLogin = (Button)findViewById(R.id.Btn_login);
		mButtonLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, LoginActivity.class);
				mContext.startActivity(intent);
			}
		});
		
		mButtonEdit = (Button)findViewById(R.id.button_edit);
		mButtonEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!mNetState.isNetUsing()){
					((MainActivity)mContext).ShowToast(R.string.nonet);
					return;
				}
				
				Intent intent = new Intent(mContext, EditUserinfoAcitivity.class);
				mContext.startActivity(intent);
			}
		});
		
		mRLayoutUserinfo.setVisibility(View.GONE);
		ClearUI();
	}
	
	

    
    /**
     * 弹出选择头像对话框
     */
	private void doPickPhotoAction() {
		if(!mNetState.isNetUsing()){
			((MainActivity)mContext).ShowToast(R.string.nonet);
			return;
		}
		
		String camera = "拍照";
		String album = "相册";
		new AlertDialog.Builder(mContext)
				.setTitle("选择相片")
				.setItems(new String[] { camera, album },
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 0) {
									startCamera();
								} else if (which == 1) {
									startAlbum();
								}
							}
						}).setNegativeButton("取消", null).show();
	}

	public void startCamera() {
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			if (Machine.isSDCardExist()) {
				File file = new File(Contants.Path.IMAGE_CACHE_PATH);
				if (!file.exists()) {
					file.mkdirs();
				}
				File picFile = new File(Contants.Path.CAMERA_SAVE_USER_PATH);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
			}
			((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_CAMERA);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startAlbum() {
		try {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
			intent.setType("image/*");
			((Activity) mContext).startActivityForResult(intent, REQUEST_CODE_ALBUM);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 刷新用户头像
	 */
	public void ReflasUI(){
		if(GoodPlaceContants.USERINFO != null){
			mRLayoutUserinfo.setVisibility(View.VISIBLE);
			mButtonLogin.setVisibility(View.GONE);
		
			mTextViewUsername.setText(GoodPlaceContants.USERINFO.getNickname()); 
			mTextViewSpscorenum.setText(GoodPlaceContants.USERINFO.getSpscore()+""); 
			mTextViewOtherscorenum.setText(GoodPlaceContants.USERINFO.getOtherscore()+"");
			mTextViewFavnum.setText(GoodPlaceContants.USERINFO.getFavoshopcount()+"");
			mTextViewPicnum.setText(GoodPlaceContants.USERINFO.getPhotocount()+"");
			mTextViewReviewnum.setText(GoodPlaceContants.USERINFO.getReviewcount()+"");
			mTextViewCheckinnum.setText(GoodPlaceContants.USERINFO.getCheckincount()+"");
			mTextViewMyinfonum.setText(GoodPlaceContants.USERINFO.getMyinfocount()+"");
//			mLinearLayoutMyfav.setClickable(true);
//			mLinearLayoutMysign.setClickable(true);
//			mLinearLayoutMyreviews.setClickable(true);
//			mLinearLayoutMypic.setClickable(true);
//			mLinearLayoutMyinfo.setClickable(true);
			
//			mImageViewUser.setImageBitmap(mImgManager.loadImgFromNetwork(GoodPlaceContants.USERINFO.getAvatarurl()));
			setIcon(mImageViewUser, 
					GoodPlaceContants.USERINFO.getAvatarurl(), 
					FileUtil.ICON_CACHE_PATH, String.valueOf(GoodPlaceContants.USERINFO.getAvatarurl().hashCode()), 
					true);
			
		}
		
	}
	
	/**
	 * 清空界面信息
	 */
	public void ClearUI(){
		mRLayoutUserinfo.setVisibility(View.GONE);
		mButtonLogin.setVisibility(View.VISIBLE);
		
		mTextViewSpscorenum.setText(""); 
		mTextViewOtherscorenum.setText(""); 
		mTextViewUsername.setText(""); 
		mTextViewFavnum.setText("");
		mTextViewPicnum.setText("");
		mTextViewReviewnum.setText("");
		mTextViewCheckinnum.setText("");
		mTextViewMyinfonum.setText("");
		
//		mLinearLayoutMyfav.setClickable(false);
//		mLinearLayoutMysign.setClickable(false);
//		mLinearLayoutMyreviews.setClickable(false);
//		mLinearLayoutMypic.setClickable(false);
//		mLinearLayoutMyinfo.setClickable(false);
		
//		mImageViewUser.setImageBitmap(mImgManager.loadImgFromNetwork(GoodPlaceContants.USERINFO.getAvatarurl()));
	}
	
	/**
	 * 设置用户重新选择的头像
	 */
	public void SetUserImg(){
		String imagePathString = Contants.Path.COMPRESS_SAVE_USER_PATH;
		File file = new File(imagePathString);
		if (file.exists()) {
			mImageViewUser.setImageBitmap(BitmapFactory.decodeFile(imagePathString));
		}
	}
	
	
	/**
	 * 读取图标，然后设到imageview里
	 */
	private void setIcon(final ImageView imageView, String imgUrl, String imgPath, String imgName,
			boolean setDefaultIcon) {
		imageView.setTag(imgUrl);
		Bitmap bm = mImgManager.loadImage(imgPath, imgName, imgUrl, true, false, null,
				new AsyncImageLoadedCallBack() {
					@Override
					public void imageLoaded(Bitmap imageBitmap, String imgUrl) {
						if (imageView.getTag().equals(imgUrl)) {
							imageView.setImageBitmap(imageBitmap);
						} else {
							imageBitmap = null;
							imgUrl = null;
						}
					}
				});
		if (bm != null) {
			imageView.setImageBitmap(bm);
		} else {
			if (setDefaultIcon) {
				imageView.setImageResource(R.drawable.no_pic_big);
			} else {
				imageView.setImageDrawable(null);
			}
		}
	}
	
	/**
	 * 先登录提示
	 */
	private void showLoginMsg(){
		Toast.makeText(mContext, mContext.getString(R.string.plzlogin), Toast.LENGTH_SHORT).show();
	}
}
