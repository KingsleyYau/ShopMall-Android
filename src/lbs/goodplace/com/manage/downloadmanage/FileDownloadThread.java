package lbs.goodplace.com.manage.downloadmanage;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

import android.util.Log;

public class FileDownloadThread extends Thread {
	private static final int BUFFER_SIZE = 1024;
	private URL url;
	private File file;
	private int startPosition;
	private int endPosition;
	private int curPosition;
	// 用于标识当前线程是否下载完成
	private boolean finished = false;
	private int downloadSize;//,tatol=0;
	//停止下载
	private boolean stop = false;

	public FileDownloadThread(URL url, File file, int startPosition, int endPosition) {
		this.url = url;
		this.file = file;
		this.startPosition = startPosition;
		this.curPosition = startPosition;
		this.endPosition = endPosition;
		this.downloadSize = 0;
	}

	@Override
	public void run() {
		BufferedInputStream bis = null;
		RandomAccessFile fos = null;
		byte[] buf = new byte[BUFFER_SIZE];
		URLConnection con = null;
		try {
			con = url.openConnection();
			con.setAllowUserInteraction(true);
			// 设置当前线程下载的起点，终点
			Log.i("xpf", "startPosition=" + startPosition + " endPosition" + endPosition);
			con.setRequestProperty("Range", "bytes=" + startPosition + "-" + endPosition);
			// 使用java中的RandomAccessFile对文件进行随机读写操作
			fos = new RandomAccessFile(file, "rw");
			Log.i("xpf", "file.length=" + file.length());
			// 设置开始写文件的位置
			fos.seek(startPosition);
			bis = new BufferedInputStream(con.getInputStream());
			// 开始循环以流的形式读写文件
			while (curPosition < endPosition) {
				//停止
				if(stop)
					break;
				
				int len = bis.read(buf, 0, BUFFER_SIZE);
				if (len == -1) {
					break;
				}
				if (curPosition + len < endPosition) {
					fos.write(buf, 0, len);
					curPosition = curPosition + len;
					downloadSize += len;
				} else {
					fos.write(buf, 0, endPosition - curPosition);
					this.finished = true;
					downloadSize += endPosition - curPosition ;
					curPosition = endPosition;
					break;
				}
			}

			// 下载完成设为true
			this.finished = true;
			bis.close();
			fos.close();
		} catch (IOException e) {
			Log.d(getName() + "Error:", e.getMessage());
		}
	}

	public boolean isFinished() {
		return finished;
	}

	public int getDownloadSize() {
		return downloadSize;
	}
	
	public void StopDownload(){
		stop = true;
	}
}
