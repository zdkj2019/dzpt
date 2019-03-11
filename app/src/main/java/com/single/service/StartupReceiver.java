package com.single.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * 开机启动广播拦截启动一个service
 * @author Cheng
 *
 */
public class StartupReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// 启动一个Service
		Intent serviceIntent = new Intent(context, PatrolService.class);
		
		context.startService(serviceIntent);
		
//		while (true) {
//
//			Log.e("dd", "开机       启动");
//			Intent activityIntent = new Intent(context, LoginActivity.class);
//			// 要想在Service中启动Activity，必须设置如下标志
//			activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(activityIntent);
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//
//				e.printStackTrace();
//			}
//		}

	}
}
