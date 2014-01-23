package lbs.goodplace.com.manage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lbs.goodplace.com.View.main.GoodPlaceApp;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

import com.drcom.drpalm.Tool.service.PreferenceManagement;

public class CrashHandler  implements UncaughtExceptionHandler {
	  public static final String TAG = "CrashHandler";   
	    public static final boolean DEBUG = true;   
	    /** 系统默认的UncaughtException处理类 */  
	    private Thread.UncaughtExceptionHandler mDefaultHandler;   
	    /** CrashHandler实例 */  
	    private static CrashHandler INSTANCE;   
	    /** 程序的Context对象 */  
	    private Context mContext;   
	    private PreferenceManagement mPreferenceManagement;
	       
	    /** 使用Properties来保存设备的信息和错误堆栈信息*/  
	 //   private Properties mDeviceCrashInfo = new Properties();   
	    private static final String VERSION_NAME = "versionName";   
	    private static final String VERSION_CODE = "versionCode";   
	    private static final String STACK_TRACE = "STACK_TRACE";   
	    /** 错误报告文件的扩展名 */  
	    private static final String CRASH_REPORTER_EXTENSION = ".txt";   
	    
	  //用来存储设备信息和异常信息  
	    private Map<String, String> infos = new HashMap<String, String>();  

	       
	    /** 保证只有一个CrashHandler实例 */  
	    private CrashHandler() {}   
	    /** 获取CrashHandler实例 ,单例模式*/  
	    public static CrashHandler getInstance() {   
	        if (INSTANCE == null) {   
	            INSTANCE = new CrashHandler();   
	        }   
	        return INSTANCE;   
	    }
	    
	    /**  
	     * 初始化,注册Context对象,  
	     * 获取系统默认的UncaughtException处理器,  
	     * 设置该CrashHandler为程序的默认处理器  
	     *   
	     * @param ctx  
	     */  
	    public void init(Context ctx) {   
	        mContext = ctx;   
	        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();   
	        Thread.setDefaultUncaughtExceptionHandler(this);   
	    }   
	    
	    /**  
	     * 当UncaughtException发生时会转入该函数来处理  
	     */  
	    @Override  
	    public void uncaughtException(Thread thread, Throwable ex) {   
	        if (!handleException(ex) && mDefaultHandler != null) {   
	            //如果用户没有处理则让系统默认的异常处理器来处理   
	            mDefaultHandler.uncaughtException(thread, ex);   
	        } else {   
	            //Sleep一会后结束程序   
	            try {   
	                Thread.sleep(3000);   
	            } catch (InterruptedException e) {   
	                Log.e(TAG, "Error : ", e);   
	            }   
	            android.os.Process.killProcess(android.os.Process.myPid());
	            System.exit(10);   
	        }   
	    }   
	    
	    /**  
	     * 自定义错误处理,收集错误信息  
	     * 发送错误报告等操作均在此完成.  
	     * 开发者可以根据自己的情况来自定义异常处理逻辑  
	     * @param ex  
	     * @return true:如果处理了该异常信息;否则返回false  
	     */  
	    private boolean handleException(Throwable ex) {   
	        if (ex == null) {   
	            return true;   
	        }   
	        final String msg = ex.getLocalizedMessage();   
	        //使用Toast来显示异常信息   
//	        new Thread() {   
//	            @Override  
//	            public void run() {   
//	                Looper.prepare();   
//	                Toast.makeText(mContext, "程序出错啦:" + msg, Toast.LENGTH_LONG)   
//	                        .show();   
//	                Looper.loop();   
//	            }   
//	  
//	        }.start();   
	        //收集设备信息   
	        collectCrashDeviceInfo(mContext);   
	        //保存错误报告文件   
	        String crashFileName = saveCrashInfoToFile(ex);   
	        return true;   
	    }     
	    
	    /**
	     * 判断存储卡是否存在
	     * 
	     * @return
	     */
	    public static boolean existSDcard()
	    {
	        if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState()))
	        {
	            return true;
	        }
	        else
	            return false;
	    }
	    
	    
	    /**  
	     * 获取错误报告文件名  
	     * @param ctx  
	     * @return  
	     */  
	    private String[] getCrashReportFiles(Context ctx) {   
	        File filesDir = ctx.getFilesDir();   
	        FilenameFilter filter = new FilenameFilter() {   
	            public boolean accept(File dir, String name) {   
	                return name.endsWith(CRASH_REPORTER_EXTENSION);   
	            }   
	        };   
	        return filesDir.list(filter);   
	    }   
	    
	    /**  
	     * 保存错误信息到文件中  
	     * @param ex  
	     * @return  
	     */  
	    private String saveCrashInfoToFile(Throwable ex) { 
	    	 StringBuffer sb = new StringBuffer();  
	         for (Map.Entry<String, String> entry : infos.entrySet()) {  
	             String key = entry.getKey();  
	             String value = entry.getValue();  
	             sb.append(key + "=" + value + "\n");  
	         }  

	    	
	        Writer info = new StringWriter(); 
	        PrintWriter printWriter = new PrintWriter(info);   
	        ex.printStackTrace(printWriter);   
	  
	        Throwable cause = ex.getCause();   
	        while (cause != null) {   
	            cause.printStackTrace(printWriter);   
	            cause = cause.getCause();   
	        }   
	  
	        String result = info.toString();  
	        sb.append(result);
	        String str = sb.toString();
	        printWriter.close();   

	        try {   
	            long timestamp = System.currentTimeMillis();   
	            Date dt = new Date(timestamp);
	            Calendar cal = Calendar.getInstance();
	            int year = cal.get(Calendar.YEAR);
	            int month = cal.get(Calendar.MONTH)+1;
	            int day = cal.get(Calendar.DAY_OF_MONTH);
	            int hour = cal.get(Calendar.HOUR_OF_DAY);
	            int min = cal.get(Calendar.MINUTE);
	            
	            
	            //byte[] buf = result.getBytes(); 
	            byte[] buf = sb.toString().getBytes();
	            DecimalFormat df = new DecimalFormat("00");
	            String fileName = "crash" +df.format(year)+df.format(month)+df.format(day)+df.format(hour)+df.format(min)+CRASH_REPORTER_EXTENSION;
	            if(existSDcard())   //有存储卡
	            {
		            //String pathName="/sdcard/DrpalmError/";  
	            	String pathName = GoodPlaceApp.GCRASHFILEPATH;
		            File path = new File(pathName);  
		            File file = new File(pathName + fileName);  
		            if( !path.exists()) {  
		                path.mkdir();  
		            }  
		            if( !file.exists()) {  
		                file.createNewFile();  
		            }  
		            FileOutputStream fos = new FileOutputStream(file); 
		            fos.write(buf);            
		            fos.close(); 
	            }
	            else
	            {
	            	FileOutputStream fos = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);  
		            fos.write(buf);            
		            fos.close(); 
	            }
	            return fileName;   
	        } catch (Exception e) {   
	            Log.e(TAG, "an error occured while writing report file...", e);   
	        }   
	        return null;   
	    }   
	    
	    
	    /**  
	     * 收集程序崩溃的设备信息  
	     *   
	     * @param ctx  
	     */  
	    public void collectCrashDeviceInfo(Context ctx) {   
	    	 try {  
	             PackageManager pm = ctx.getPackageManager();  
	             PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);  
	             if (pi != null) {  
	                 String versionName = pi.versionName == null ? "null" : pi.versionName;  
	                 String versionCode = pi.versionCode + "";  
	                 String packagename = pi.packageName == null ? "null" : pi.packageName;
	                 infos.put("versionName", versionName);  
	                 infos.put("versionCode", versionCode);  
	                 infos.put("packagename", packagename);
	             }  
	         } catch (NameNotFoundException e) {  
	             Log.e(TAG, "an error occured when collect package info", e);  
	         }  
	       //使用反射来收集设备信息.在Build类中包含各种设备信息,   
		        //例如: 系统版本号,设备生产商 等帮助调试程序的有用信息   
	         Field[] fields = Build.class.getDeclaredFields();  
	         for (Field field : fields) {  
	             try {  
	                 field.setAccessible(true);  
	                 infos.put(field.getName(), field.get(null).toString());  
	                 Log.d(TAG, field.getName() + " : " + field.get(null));  
	             } catch (Exception e) {  
	                 Log.e(TAG, "an error occured when collect crash info", e);  
	             }  
	         }  

	    }   
}
