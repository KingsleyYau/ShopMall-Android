package lbs.goodplace.com.manage.imagemanage;

import java.util.HashMap;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.util.Log;

/**
 * 
 * @author zhouxuewen
 *
 */
public class ThreadPoolManager {

	private static HashMap<String, ThreadPoolManager> sThreadPoolManagerhHashMap = null;

	private final static int DEFAULT_COREPOOL_SIZE = 4;
	private final static int DEFAULT_MAXIMUMPOOL_SIZE = 4;
	private final static long DEFAULT_KEEPALIVE_TIME = 0;
	private final static TimeUnit DEFAULT_TIMEUNIT = TimeUnit.SECONDS;
	private ThreadPoolExecutor mWorkThreadPool = null; // 线程池
	private ScheduledExecutorService mScheduledExecutorService = null; // 调度线程池
	private Thread mScheduledThread = null; // 调度Runnable
	private Queue<Runnable> mWaitTasksQueue = null; // 等待任务队列
	private RejectedExecutionHandler mRejectedExecutionHandler = null; // 任务被拒绝执行的处理器
	private Object mLock = new Object();

	private ThreadPoolManager() {
		this(DEFAULT_COREPOOL_SIZE, DEFAULT_MAXIMUMPOOL_SIZE, DEFAULT_KEEPALIVE_TIME,
				DEFAULT_TIMEUNIT, false);
	}

	private ThreadPoolManager(int corePoolSize, int maximumPoolSize, long keepAliveTime,
			TimeUnit unit, boolean isPriority) {
		mWaitTasksQueue = new ConcurrentLinkedQueue<Runnable>();
		mScheduledThread = new ScheduledThread();
		mScheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		mScheduledExecutorService.scheduleAtFixedRate(mScheduledThread, 0, 1000,
				TimeUnit.MILLISECONDS);
		initRejectedExecutionHandler();
		BlockingQueue<Runnable> queue = isPriority
				? new PriorityBlockingQueue<Runnable>(16)
				: new LinkedBlockingQueue<Runnable>(16);
		mWorkThreadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
				unit, queue, mRejectedExecutionHandler);
	}

	public synchronized static ThreadPoolManager getInstance(String threadPoolManagerName) {
		ThreadPoolManager threadPoolManager = null;
		if (threadPoolManagerName != null && !"".equals(threadPoolManagerName.trim())) {
			if (null == sThreadPoolManagerhHashMap) {
				sThreadPoolManagerhHashMap = new HashMap<String, ThreadPoolManager>();
			}
			threadPoolManager = sThreadPoolManagerhHashMap.get(threadPoolManagerName);
			if (null == threadPoolManager) {
				threadPoolManager = new ThreadPoolManager();
				sThreadPoolManagerhHashMap.put(threadPoolManagerName, threadPoolManager);
			}
		}
		return threadPoolManager;
	}

	public synchronized static void buildInstance(String threadPoolManagerName, int corePoolSize,
			int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
		buildInstance(threadPoolManagerName, corePoolSize, maximumPoolSize, keepAliveTime, unit,
				false);
	}

	public synchronized static void buildInstance(String threadPoolManagerName, int corePoolSize,
			int maximumPoolSize, long keepAliveTime, TimeUnit unit, boolean isPriority) {
		if (threadPoolManagerName == null || "".equals(threadPoolManagerName.trim())
				|| corePoolSize < 0 || maximumPoolSize <= 0 || maximumPoolSize < corePoolSize
				|| keepAliveTime < 0) {
			return;
		} else {
			if (null == sThreadPoolManagerhHashMap) {
				sThreadPoolManagerhHashMap = new HashMap<String, ThreadPoolManager>();
			}
			ThreadPoolManager threadPoolManager = new ThreadPoolManager(corePoolSize,
					maximumPoolSize, keepAliveTime, unit, isPriority);
			sThreadPoolManagerhHashMap.put(threadPoolManagerName, threadPoolManager);
		}
	}

	/**
	 * 初始化调度Runable
	 */
	private class ScheduledThread extends Thread {
		@Override
		public void run() {
			synchronized (mLock) {
				if (hasMoreWaitTask()) {
					Runnable runnable = mWaitTasksQueue.poll();
					if (runnable != null) {
						execute(runnable);
					}
				}
			}
		}
	}

	/**
	 * 初始化任务被拒绝执行的处理器的方法
	 */
	private void initRejectedExecutionHandler() {
		mRejectedExecutionHandler = new RejectedExecutionHandler() {
			@Override
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				// 把被拒绝的任务重新放入到等待队列中
				synchronized (mLock) {
					mWaitTasksQueue.offer(r);
				}
			}
		};
	}

	/**
	 * 是否还有等待任务的方法
	 * 
	 * @return
	 */
	public boolean hasMoreWaitTask() {
		boolean result = false;
		if (mWaitTasksQueue != null && !mWaitTasksQueue.isEmpty()) {
			result = true;
		}
		return result;
	}

	/**
	 * 执行任务的方法
	 * 
	 * @param task
	 */
	public void execute(Runnable task) {
		if (mWorkThreadPool != null && task != null) {
			mWorkThreadPool.execute(task);
			// Log.i("getView",
			// "mWorkThreadPool.getTaskCount():"+mWorkThreadPool.getTaskCount());
		}
	}

	/**
	 * 取消任务
	 * 
	 * @param task
	 */
	public void cancel(Runnable task) {
		if (task != null) {
			synchronized (mLock) {
				if (mWaitTasksQueue != null && mWaitTasksQueue.contains(task)) {
					mWaitTasksQueue.remove(task);
				}
			}
			if (mWorkThreadPool != null) {
				mWorkThreadPool.remove(task);
			}
		}
	}

	public void removeAllTask() {
		// 如果取task过程中task队列数量改变了会抛异常
		try {
			if (null != mWorkThreadPool && !mWorkThreadPool.isShutdown()) {
				BlockingQueue<Runnable> tasks = mWorkThreadPool.getQueue();
				for (Runnable task : tasks) {
					mWorkThreadPool.remove(task);
				}
			}
		} catch (Throwable e) {
			Log.e("ThreadPoolManager", "removeAllTask " + e.getMessage());
		}
	}

	public boolean isShutdown() {
		boolean result = true;
		if (null != mWorkThreadPool) {
			result = mWorkThreadPool.isShutdown();
		}
		return result;
	}

	/**
	 * 清理方法
	 */
	private void cleanUp() {
		if (mWorkThreadPool != null) {
			if (!mWorkThreadPool.isShutdown()) {
				try {
					mWorkThreadPool.shutdownNow();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			mWorkThreadPool = null;
		}
		mRejectedExecutionHandler = null;
		if (mScheduledExecutorService != null) {
			if (!mScheduledExecutorService.isShutdown()) {
				try {
					mScheduledExecutorService.shutdownNow();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			mScheduledExecutorService = null;
		}
		mScheduledThread = null;
		synchronized (mLock) {
			if (mWaitTasksQueue != null) {
				mWaitTasksQueue.clear();
				mWaitTasksQueue = null;
			}
		}
	}

	/**
	 * 销毁的方法
	 */
	public static synchronized void destory() {
		if (sThreadPoolManagerhHashMap != null) {
			Set<String> keySet = sThreadPoolManagerhHashMap.keySet();
			if (keySet != null && keySet.size() > 0) {
				ThreadPoolManager threadPoolManager = null;
				for (String key : keySet) {
					threadPoolManager = sThreadPoolManagerhHashMap.get(key);
					if (threadPoolManager != null) {
						threadPoolManager.cleanUp();
					}
				}
			}
			sThreadPoolManagerhHashMap.clear();
			sThreadPoolManagerhHashMap = null;
		}
	}
}
