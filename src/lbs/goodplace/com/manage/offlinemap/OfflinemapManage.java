package lbs.goodplace.com.manage.offlinemap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipException;

import lbs.goodplace.com.manage.downloadmanage.DownloadTask;
import lbs.goodplace.com.manage.zip.ZipUtils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.amap.api.maps.MapsInitializer;
import com.amap.api.offlinemap.DownCity;
import com.amap.api.offlinemap.MOfflineMapStatus;
import com.amap.api.offlinemap.OfflineMapManager;
import com.amap.api.offlinemap.OfflineMapManager.OfflineMapDownloadListener;

public class OfflinemapManage implements
		OfflineMapDownloadListener {
	private String ZIPNAME = "GangAo.zip";
	private String ZipPath = "";	//ZIP文件不能放在地图缓存的目录下
	private String MapPath = "";	//
	
	private Context mContext;
	private OfflineMapManager mOffline = null;
	private int mDownloadStatus = MOfflineMapStatus.STOP;
	private IDownPercentHandler mDownPercentHandler;
	
	private DownloadTask mdownloadTask;
	
	public OfflinemapManage(Context c){
		mContext = c;
		mOffline = new OfflineMapManager(c, this);
		//设置应用单独的地图存储目录，在下载离线地图或初始化地图时设置
//		MapsInitializer.sdcardDir = Environment.getExternalStorageDirectory() + "/amap/mini_mapv3/vmap/";//getSdCacheDir(c);
		ZipPath = Environment.getExternalStorageDirectory() +"/";
		MapPath = Environment.getExternalStorageDirectory() + "/amap/mini_mapv3/vmap/";
		
		Log.i("zjj", "离线地图保存目录:" + ZipPath);
	}

	/**
	 * 开始下载
	 * @param cityname http://mapdownload.autonavi.com/mobilemap/mapdatav3/12Q4/GangAo.zip
	 * @return 下载是否成功
	 */
	public boolean Star(String cityname){
//		boolean start = mOffline.downloadByCityName("香港");//cityname);
		
		
//		boolean start = mOffline.downloadByCityCode("020");
//		Log.i("zjj", "下载地图:" + start);
		
		
		
//		return start;
		
//		ArrayList<DownCity> dcs =  mOffline.getOfflineCityList();
//		for(int i = 0;i < dcs.size(); i++){
//			
//			Log.i("zjj", "下载地图:" + dcs.get(i).getCity() + ",url" + dcs.get(i).getDurl());
//		}
//		
//		return true;
		
		download(mContext,"http://mapdownload.autonavi.com/mobilemap/mapdatav3/12Q4/GangAo.zip");
		return true;
	}
	
	/**
	 * 暂停下载
	 */
	public void Stop(){
//		mOffline.pause();
		mdownloadTask.StopDownload();
		mdownloadTask = null;
		mDownloadStatus = MOfflineMapStatus.STOP;
	}

	/**
	 * 取下载状态(对应 MOfflineMapStatus.xxx 的值)
	 * @return
	 */
	public int getmDownloadStatus() {
		return mDownloadStatus;
	}
	
	public void setDownPercentHandler(IDownPercentHandler h){
		mDownPercentHandler = h;
	}
	
	/**
	 * 获取map 缓存和读取目录
	 * @param context
	 * @return
	 */
	private  String getSdCacheDir(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			java.io.File fExternalStorageDirectory = Environment
					.getExternalStorageDirectory();
			java.io.File autonaviDir = new java.io.File(
					fExternalStorageDirectory, "companyname");
			boolean result = false;
			if (!autonaviDir.exists()) {
				result = autonaviDir.mkdir();
			}
			java.io.File minimapDir = new java.io.File(autonaviDir,
					"objectname");
			if (!minimapDir.exists()) {
				result = minimapDir.mkdir();
			}
			return minimapDir.toString() + "/";
		} else {
			return null;
		}
	}
	
	@Override
	public void onDownload(int status, int completeCode) {
		// TODO Auto-generated method stub
		
		mDownloadStatus = status;
		if(mDownPercentHandler != null){
			mDownPercentHandler.getPercent(completeCode);
		}
	}
	
	/**
	 * 
	 * @param path
	 *            下载的目标地址
	 * @param dir
	 *            存放的目标地址
	 * @param callBack
	 *            下载成功回调
	 */
	private void download(final Context context, final String url) {
		if(mdownloadTask == null)
			mdownloadTask = new DownloadTask(handler, url, 3, null, ZipPath);
		
		mdownloadTask.start();
		mDownloadStatus = MOfflineMapStatus.LOADING;
	}
	
	/**
	 * 处理下载中的信息
	 */
	private Handler handler = new Handler() {
		int size = 0;
		
		@Override
		// 信息
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DownloadTask.DOWN:
				// int size = msg.getData().getInt("size");
				size =(Integer) msg.obj;
				
				Log.i("zjj", "离线地图下载百分比:" + size);
				if(mDownPercentHandler != null){
					mDownPercentHandler.getPercent(size);
				}
				
				if (size == 100) {
					Log.i("zjj", "资源包下载完成,下一步是解压");
					// 解压
					if (UnzipResourceWithZip()) {
						// 回调
						Log.i("zjj", "资源包解压完成");
					}
				}
				break;

			case DownloadTask.NOTDOWN:
				break;
			}

		}
	};

	/**
	 * 解压
	 * 
	 * @param zipName
	 * @param isForce
	 * @return
	 */
	public boolean UnzipResourceWithZip() {
		if (ZIPNAME.lastIndexOf(".zip") > -1) {
//			String resourceDirectory = MapsInitializer.sdcardDir;
			String mResourceZipFileName = ZipPath + ZIPNAME;
			
			Log.i("zjj", "解压资源包zipName:" + mResourceZipFileName);

			File zipFile = new File(mResourceZipFileName);
			try {
				ZipUtils.upZipFile(zipFile, MapPath);
				// deleteAllOtherFile(new File(zipFile));
				// zipFile.delete();
				Log.i("zjj", "解压资源包成功");
				return true;
			} catch (ZipException e) {
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
}
