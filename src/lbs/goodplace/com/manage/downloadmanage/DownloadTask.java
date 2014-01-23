package lbs.goodplace.com.manage.downloadmanage;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 下载文件,工具类,每1秒发送一次下载进度,msg.what=1;obj=percentage;
 * 
 * @author xpf
 * 
 */
public class DownloadTask extends Thread {
	public static final int DOWN = 1;		//部分下载成功
	public static final int NOTDOWN = -1;	//下载失败
	public static final int STOP = 2;		//下载被停止
	
	private int blockSize, downloadSizeMore;
	private int threadNum;
	private int fileSize, downloadedSize;
	private String urlStr, fileName;
	private String dowloadDir;
	private Handler handler;
	private boolean stop = false;
	
//	private List<FileDownloadThread> listThreads = new ArrayList<FileDownloadThread>();
	private FileDownloadThread[] fds;

	/**
	 * 
	 * @param handler
	 *            每1秒发送一次下载进度
	 * @param urlStr
	 *            下载的url
	 * @param threadNum
	 *            开启的进程数,默认为3
	 * @param fileName
	 *            保存的文件名,if null save with the last string after '/'
	 */
	public DownloadTask(Handler handler, String urlStr, int threadNum, String fileName, String savaDirector) {
		this.urlStr = urlStr;
		if (threadNum <= 0) {
			this.threadNum = 1;
		} else {
			this.threadNum = threadNum;
		}
		this.handler = handler;

		// 下载目录
		if ("".equals(savaDirector) || null == savaDirector) {
			dowloadDir = Environment.getExternalStorageDirectory() + "/Ebabydownload/";
		} else {
			dowloadDir = Environment.getExternalStorageDirectory() + "/" + savaDirector + "/";
		}
		File file = new File(dowloadDir);
		// 创建下载目录
		if (!file.exists()) {
			file.mkdirs();
		}
		// 去掉文件名中的"/"
		if ("".equals(fileName) || null == fileName) {
			// 如果下载文件名为空则获取Url尾为文件名
			this.fileName = urlStr.substring(urlStr.lastIndexOf("/") + 1, urlStr.length());
		} else {
			this.fileName = fileName;
		}
		this.fileName = dowloadDir + this.fileName;
		Log.i("xpf", "filename-"+this.fileName);
	}

	public void StopDownload(){
		stop = true;
		
		for(int i = 0 ; i < threadNum;i++){
			fds[i].StopDownload();
		}
		
		Message msg = handler.obtainMessage();
		msg.what = STOP;
		msg.obj = "dowload stoped";
		handler.sendMessage(msg);
	}
	
	@Override
	public void run() {
//		FileDownloadThread[] fds = new FileDownloadThread[threadNum];
		fds = new FileDownloadThread[threadNum];
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			// 获取下载文件的总大小
			fileSize = conn.getContentLength();
			Log.i("xpf", "filelenght="+fileSize);
			// 计算每个线程要下载的数据量
			blockSize = fileSize / threadNum;
			// 解决整除后百分比计算误差
			downloadSizeMore = (fileSize % threadNum);
			File file = new File(fileName);
			for (int i = 0; i < threadNum-1; i++) {
				// 启动线程，分别下载自己需要下载的部分
				FileDownloadThread fdt = new FileDownloadThread(url, file, i * blockSize, (i + 1) * blockSize );
				fdt.setName("Thread" + i);
				fdt.start();
				fds[i] = fdt;
			}
			
			//下载带除后多出来的部分
			FileDownloadThread fdt = new FileDownloadThread(url, file,(threadNum-1) * blockSize, ((threadNum-1) + 1) * blockSize +downloadSizeMore);
			fdt.setName("Thread" + (threadNum-1));
			fdt.start();
			fds[threadNum-1] = fdt;
			
			boolean finished = false;
			if (fileSize <= 0) {
				Message msg = handler.obtainMessage();
				msg.what = NOTDOWN;
				msg.obj = "dowload failed";
				handler.sendMessage(msg);
				finished = true;
			}
			while (!finished && !stop) {
				// 先把整除的余数搞定
				downloadedSize = downloadSizeMore;
				finished = true;
				for (int i = 0; i < fds.length; i++) {
					downloadedSize += fds[i].getDownloadSize();
					if (!fds[i].isFinished()) {
						finished = false;
					}
				}
				// 通知handler去更新视图组件
				Message msg = handler.obtainMessage();
				msg.what = DOWN;
				
				msg.obj = (Double.valueOf((downloadedSize * 1.0 / fileSize * 100))).intValue();
				Log.i("xpf", "downloadedSize="+downloadedSize+"  fileSize="+fileSize+" p="+Double.valueOf((downloadedSize * 1.0 / fileSize * 100)));
				if (downloadedSize == fileSize) {
					finished = true;
					msg.obj=100;
				}
				handler.sendMessage(msg);
			
				// 休息1秒后再读取下载进度
				sleep(1000);
			}
		} catch (Exception e) {
			Log.i("xpf", "url eeror");
			Message msg = handler.obtainMessage();
			msg.what = NOTDOWN;
			msg.obj = "dowload failed";
			handler.sendMessage(msg);
		}

	}

}
