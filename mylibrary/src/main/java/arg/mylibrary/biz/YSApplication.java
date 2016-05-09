package arg.mylibrary.biz;

import android.app.Application;
import android.content.Context;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.LinkedList;
import java.util.List;

import arg.mylibrary.ui.base.AbstractBaseActivity;

public class YSApplication extends Application implements Thread.UncaughtExceptionHandler {
	private static Context ctx;

	/** 获取全局的上下文 */
	public static Context getContext() {
		return ctx;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		ctx = getApplicationContext();
	}
	/** 记录所有打开的Activity，用于退出 */
	private List<AbstractBaseActivity> activitys = null;

	// 添加Activity到容器中
	public void addActivity(AbstractBaseActivity activity) {
		if (activitys == null) {
			activitys = new LinkedList<AbstractBaseActivity>();
		}

		if (activitys != null && activitys.size() > 0) {
			if (!activitys.contains(activity)) {
				activitys.add(activity);
			}
		} else {
			activitys.add(activity);
		}
	}

	public void removeActivity(AbstractBaseActivity activity) {
		if (activitys != null && activitys.size() > 0) {
			if (activitys.contains(activity)) {
				activitys.remove(activity);
			}
		}
	}
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}
	/**
	 * 获取所有Activity
	 * 
	 * @return
	 */
	public List<AbstractBaseActivity> getActivitys() {
		return activitys;
	}

	// 遍历所有Activity并finish
	public void exit() {
		if (activitys != null && activitys.size() > 0) {
			for (AbstractBaseActivity activity : activitys) {
				if (activity != null) {
					activity.finish();
				}
			}
		}
		System.exit(0);
	}

	// 程序以外终止：重启程序：
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		/*// 重启程序：
		Intent intent = new Intent(this, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		Log.e("hy", "uncaughtException, thread: " + thread + " name: " + thread.getName() + " id: " + thread.getId()
				+ "exception: " + ex);
		// 退出应用：
		exit();*/
	}
}
