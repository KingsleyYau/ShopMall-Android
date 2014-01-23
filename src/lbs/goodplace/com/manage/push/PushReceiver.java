package lbs.goodplace.com.manage.push;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import org.json.JSONException;
import org.json.JSONObject;

import lbs.goodplace.com.R;
import lbs.goodplace.com.View.main.MainActivity;
import lbs.goodplace.com.View.main.ShopInfoActivity;
import lbs.goodplace.com.View.msg.MsgDetailActivity;
import lbs.goodplace.com.obj.MyPushMessageItem;
import lbs.goodplace.com.obj.parser.PushMsgParser;

import com.drcom.drpalm.Tool.service.DrServiceLog;
import com.drcom.drpalm.Tool.service.PreferenceManagement;
import com.drcom.drpalm.Tool.service.RequestParse;
import com.drcom.drpalm.objs.PushActionDefine;
import com.drcom.drpalm.objs.PushMessageItem;
import com.drcom.drpalm.objs.PushUpgradeAppItem;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class PushReceiver extends BroadcastReceiver{

	private static final int BASE_NOTIFICATION_ID = 10000;
	private static final int MAX_NOTIFICATION_COUNT = 10;
	private static int mCurNotificationId = BASE_NOTIFICATION_ID;

	public static NotificationManager mNotification;
	private ActivityManager mActivityManager;
	private String mPackageName;
	private Context mContext;
	private PreferenceManagement mPreferenceManagement;
	private ArrayBlockingQueue<Integer> mQueue = new ArrayBlockingQueue<Integer>(MAX_NOTIFICATION_COUNT);

	////////////////////////////////////////////////////////////////////////////////////
	/*
	 *  输出日志
	 */
	static final String PUSH_LOG_SAVE_PATH = "/sdcard/DrpalmPushLog/";
	static final String PUSH_LOG_NAME = "PushLog";
	static final long TIME_FOR_CLEAN_PUSH_LOG = 10*24*3600*1000;

	synchronized private void writeLog(String strLog) {
		//DrServiceLog.clearInvalidLog(PUSH_LOG_SAVE_PATH, TIME_FOR_CLEAN_PUSH_LOG);
		DrServiceLog.writeLog(PUSH_LOG_SAVE_PATH, PUSH_LOG_NAME, strLog);
	}

	@Override
	public void onReceive(Context context, Intent intent){
		mContext = context;
		mPackageName = context.getPackageName();
		mPreferenceManagement = PreferenceManagement.getInstance(context);
		mActivityManager = (ActivityManager) context.getSystemService(android.content.Context.ACTIVITY_SERVICE);
		mNotification = (NotificationManager)context.getSystemService(android.content.Context.NOTIFICATION_SERVICE);
		String stringAction = intent.getAction();
		Bundle extras = intent.getExtras();
		// 推送广播
		if(stringAction.equals(mContext.getPackageName() + PushActionDefine.PUSH_GETMESSAGE_ACTION)){	//对应DrPalmPushService 327行
//			PushMessageItem item = null;
//			if(extras.containsKey(PushActionDefine.PUSH_MESSAGE)){
//				item = (PushMessageItem)extras.getParcelable(PushActionDefine.PUSH_MESSAGE);
//			}
			String pushjsonstr = "";
			if(extras.containsKey(PushActionDefine.PUSH_MESSAGE)){
				pushjsonstr = extras.getString(PushActionDefine.PUSH_MESSAGE);
			}
			
			Log.i("zjj", "收到推送广播:" + pushjsonstr);
			
			if("" != pushjsonstr){
				String msgLog = "收到推送广播:" + pushjsonstr;
				writeLog(msgLog);

				//解释JSON
				MyPushMessageItem item = null;
				try {
					PushMsgParser pushParse = new PushMsgParser();
					item = pushParse.parser(new JSONObject(pushjsonstr));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(item !=null){
					boolean bSound = item.sound.length()>1?true:false;
					//boolean bSound = ("" != item.sound?true:false);
					boolean bVibrate = (1 == item.etype?true:false);

					// 去除旧的通知栏消息
					if(mQueue.size() >= MAX_NOTIFICATION_COUNT) {
						Integer notificationId = mQueue.poll();
						mNotification.cancel(notificationId);
					}

					mCurNotificationId = BASE_NOTIFICATION_ID + ++mCurNotificationId % MAX_NOTIFICATION_COUNT;
					showNotification(R.drawable.ic_launcher, item.alert,
							mContext.getString(R.string.push_message), item.alert,
							bVibrate, bSound, false, mCurNotificationId, item.badge,
							item.typeid,item.id);
					// 纪录新插入的消息
					mQueue.add(mCurNotificationId);
					Intent uncountIntent = new Intent(mContext.getPackageName() + PushActionDefine.UNGETCOUNT_ACTION);
					uncountIntent.putExtra(mContext.getPackageName() + PushActionDefine.UNGETCOUNT, String.valueOf(item.badge));
					context.sendBroadcast(uncountIntent);
				}
			}
		}
		// 升级广播
		else if (stringAction.equals(mContext.getPackageName() + PushActionDefine.PUSH_UPGRADEAPP_ACTION)){	//对应DrPalmPushService 335行
			PushUpgradeAppItem item = null;
			if(extras.containsKey(PushActionDefine.PUSH_UPGRADE)){
				item = (PushUpgradeAppItem)extras.getParcelable(PushActionDefine.PUSH_UPGRADE);
			}
			if(null != item){
				//转化广播
				Intent upgradeIntent = new Intent(mContext.getPackageName() + PushActionDefine.UPGRADEAPP_ACTION);
				upgradeIntent.putExtra(PushActionDefine.PUSH_UPGRADE, item);
				context.sendBroadcast(upgradeIntent);
			}
		}
	}

	/**
	 * PUSH通知框
	 * @param icon
	 * @param tickertext
	 * @param title
	 * @param content
	 * @param isVibrate
	 * @param isSound
	 * @param isAutoCancel
	 * @param notificationId
	 * @param messagecount
	 */
	public void showNotification(int icon,String tickertext,String title,String content,
    		boolean isVibrate, boolean isSound, boolean isAutoCancel, int notificationId,
    		int messagecount,int typeid,String id){
    	Notification notification= new Notification(icon,tickertext,System.currentTimeMillis());

    	notification.flags = Notification.FLAG_AUTO_CANCEL;	//点击关闭
    	if(isVibrate){
    		notification.defaults |= Notification.DEFAULT_VIBRATE;
    		long[] vibrate = {0,100,200,300};
    		notification.vibrate = vibrate;
    	}
    	if(isSound){
    		notification.defaults |= Notification.DEFAULT_SOUND;
    	}
    	Intent intent = null;
    	//************
    	//************
    	//************
    	//************点击通知启动哪个ACTIVITY
    	//************
    	//************
    	//************
    	
    	if(typeid == 1){
    		intent = new Intent(mContext, ShopInfoActivity.class);
    		intent.putExtra(ShopInfoActivity.KEY_ISFORMPUSH,id);
    	}else if(typeid == 2){
    		intent = new Intent(mContext, MsgDetailActivity.class);
    		intent.putExtra(MsgDetailActivity.KEY_ISFORMPUSH,id);
    	}else{	// if(typeid == 3)
            intent = new Intent(mContext, MainActivity.class);
//            intent.putExtra(ClassNewsListActivity.KEY_PUSHSTART,true);
    	}
        
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        Context context = mContext.getApplicationContext();
        PendingIntent pt = PendingIntent.getActivity(context, 0, intent, 0);
    	
    	notification.setLatestEventInfo(mContext,title,content,pt);

    	mNotification.notify(notificationId,notification);
    }
	
	private boolean isRunning(){
//		return false;
//		List<RunningAppProcessInfo> appProcessList = mActivityManager.getRunningAppProcesses();// .getRunningTasks(100);
//		for(RunningTaskInfo info : taskList){
//			String strActivity = info.topActivity.getClassName();
//			String topActivity = mPackageName + "." + "DrPalmActivity";
//			if(strActivity.equals(topActivity))
//				return true;
//			else
//				return false;
//		}
    	List<RunningAppProcessInfo> appProcessList = mActivityManager.getRunningAppProcesses();
    	for(ActivityManager.RunningAppProcessInfo process : appProcessList){
    		
    		Log.i("zjj", process.processName);
//    		if(mPackageName.equals(process.processName)){
////    			String drpalmActivityName = mPackageName + "." + "DrPalmActivity";
//    			return true;
//    		}
    	}
    	return false;
    }
	
//    private List<String> getAllTheLauncher()
//    {        
//    	List<String> names = null;
//    	PackageManager pkgMgt = mContext.getPackageManager();
//    	Intent it = new Intent(Intent.ACTION_MAIN);
//    	it.addCategory(Intent.CATEGORY_HOME);
//    	List<ResolveInfo> ra =pkgMgt.queryIntentActivities(it,0);
//    	if(ra.size() != 0)
//    	{
//    		names = new ArrayList<String>();
//		}
//    	for(int i=0;i< ra.size();i++)
//    	{        
//    		String packageName =  ra.get(i).activityInfo.packageName;
//    		names.add(packageName);
//    		Log.i("zjj", "**************" + packageName);
//		}
//    	return names;
//	}
//    
//	// 查询所有正在运行的应用程序信息： 包括他们所在的进程id和进程名
//	// 这儿我直接获取了系统里安装的所有应用程序，然后根据报名pkgname过滤获取所有真正运行的应用程序
//	private List<RunningAppInfo> queryAllRunningAppInfo() {
//		pm = this.getPackageManager();
//		// 查询所有已经安装的应用程序
//		List<ApplicationInfo> listAppcations = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
//		Collections.sort(listAppcations,new ApplicationInfo.DisplayNameComparator(pm));// 排序
//
//		// 保存所有正在运行的包名 以及它所在的进程信息
//		Map<String, ActivityManager.RunningAppProcessInfo> pgkProcessAppMap = new HashMap<String, ActivityManager.RunningAppProcessInfo>();
//
//		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//		// 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
//		List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
//				.getRunningAppProcesses();
//
//		for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
//			int pid = appProcess.pid; // pid
//			String processName = appProcess.processName; // 进程名
//			Log.i(TAG, "processName: " + processName + "  pid: " + pid);
//
//			String[] pkgNameList = appProcess.pkgList; // 获得运行在该进程里的所有应用程序包
//
//			// 输出所有应用程序的包名
//			for (int i = 0; i < pkgNameList.length; i++) {
//				String pkgName = pkgNameList[i];
//				Log.i(TAG, "packageName " + pkgName + " at index " + i+ " in process " + pid);
//				// 加入至map对象里
//				pgkProcessAppMap.put(pkgName, appProcess);
//			}
//		}
//		// 保存所有正在运行的应用程序信息
//		List<RunningAppInfo> runningAppInfos = new ArrayList<RunningAppInfo>(); // 保存过滤查到的AppInfo
//
//		for (ApplicationInfo app : listAppcations) {
//			// 如果该包名存在 则构造一个RunningAppInfo对象
//			if (pgkProcessAppMap.containsKey(app.packageName)) {
//				// 获得该packageName的 pid 和 processName
//				int pid = pgkProcessAppMap.get(app.packageName).pid;
//				String processName = pgkProcessAppMap.get(app.packageName).processName;
//				runningAppInfos.add(getAppInfo(app, pid, processName));
//			}
//		}
//
//		return runningAppInfos;
//
//	}
//    // 构造一个RunningAppInfo对象 ，并赋值  
//    private RunningAppInfo getAppInfo(ApplicationInfo app, int pid, String processName) {  
//        RunningAppInfo appInfo = new RunningAppInfo();  
//        appInfo.setAppLabel((String) app.loadLabel(pm));  
//        appInfo.setAppIcon(app.loadIcon(pm));  
//        appInfo.setPkgName(app.packageName);  
//  
//        appInfo.setPid(pid);  
//        appInfo.setProcessName(processName);  
//  
//        return appInfo;  
//    }  
}




