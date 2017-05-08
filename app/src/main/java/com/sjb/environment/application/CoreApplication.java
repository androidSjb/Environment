package com.sjb.environment.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.igexin.sdk.PushManager;
import com.pgyersdk.crash.PgyCrashManager;
import com.sjb.environment.service.IntentService;
import com.sjb.environment.service.PushService;
import com.sjb.environment.util.CrashHandler;
import org.xutils.x;
import java.util.ArrayList;
import java.util.List;

/**
 * 全局上下文
 *
 * @author zhangchi
 *
 */
public class CoreApplication extends Application {

//	static private Application ins;
	public static Context applicationContext;
	private static CoreApplication instance;
	public static boolean isActivityReStart = true;
	public static boolean isFragmentReStart = true;
	private static List<Activity> activities = new ArrayList<>();

	@Override
	public void onCreate() {

		super.onCreate();
		PgyCrashManager.register(this);
		SDKInitializer.initialize(getApplicationContext());
		applicationContext = getApplicationContext();
		instance = this;

		x.Ext.init(this);

		PushManager.getInstance().initialize(this.getApplicationContext(), PushService.class);
		PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), IntentService.class);
		CrashHandler handler = CrashHandler.getInstance();
		handler.init(getApplicationContext());
		Thread.setDefaultUncaughtExceptionHandler(handler);
	}

	public static boolean isActivityExist(String activityNow){
		for (Activity activity : activities) {
			if(activity.toString().contains(activityNow)){
				return true;
			}
		}
		return false;
	}

	public static void addActivity(Activity activity){
		activities.add(activity);
	}

	public static void removeActivity(Activity activity){
		activities.remove(activity);
	}

	public static void exitSystem(){
		try {
			for (Activity activity : activities) {
				if (activity != null)
					activity.finish();
			}
			activities.clear();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	public static CoreApplication getInstance() {
		return instance;
	}
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
//		MultiDex.install(this);
	}

}
