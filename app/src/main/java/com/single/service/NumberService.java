package com.single.service;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.single.activity.esp.MainMenuActivity;
import com.single.activity.esp.NotificationOrderList;
import com.single.activity.kdg.ListKdg;
import com.single.activity.kdg.Zzpqgdcx;
import com.single.activity.login.LoginActivity;
import com.single.activity.w.XxglActivity;
import com.single.cache.DataCache;
import com.single.definition.Sign;
import com.single.webservice.CallWebserviceImp;
import com.single.gtdzpt.R;

@SuppressLint("HandlerLeak")
public class NumberService extends Service implements Sign {

	private CallWebserviceImp callWebserviceImp;
	private String flag ;
	private String txzd = "",zzpqMsg="",yjnr="";
	private SharedPreferences spf;
	public static final String ACTION_MAIN = "com.single.activity.esp.NumberUpdateRecevice";
	private int zyxx_num;

	// private ServiceBinder serviceBinder;
	// public class ServiceBinder extends Binder implements Notificaction {
	//
	// @Override
	// public void addNotificactions() {
	// Log.e("dd", "addNotificactions");
	// addNotificaction();
	//
	// }
	// };
	@Override
	public IBinder onBind(Intent intent) {
		Log.e("dd", "NumberService onBind");
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.e("dd", "NumberService onUnbind");
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		callWebserviceImp = new CallWebserviceImp();
		spf = getSharedPreferences("loginsp", LoginActivity.MODE_PRIVATE);
		Log.e("dd", "NumberService onCreate");
		super.onCreate();

	}

	@Override
	public void onDestroy() {
		Log.e("dd", "NumberService onDestroy");
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.e("dd", "NumberService onStartCommand");
		// 去查询数据，进行更新
		 getWebService("query");
		return super.onStartCommand(intent, flags, startId);
	}

	Calendar c = Calendar.getInstance();

	private void getWebService(String s) {

		new Thread() {

			@Override
			public void run() {

				int currentTime = c.get(Calendar.HOUR_OF_DAY);

				if (currentTime >= 8 && currentTime <= 22) {
					query();
				}

			}

		}.start();

	}
	private void query() {
		try {
			String userid = spf.getString("userId", "");
			//JSONObject jsonObject = callWebserviceImp.getWebServerInfo("_PAD_TIS", userid, "uf_json_getdata",this);
			//快递柜数量
			JSONObject jsonObject_kdg = callWebserviceImp.getWebServerInfo("_PAD_KDG_GDSLTS", userid+"*"+userid, "uf_json_getdata",this);
			//重要消息未读数量
			JSONObject jsonObject_zyxx = callWebserviceImp.getWebServerInfo("_PAD_YGXX_WDS", userid+"*"+userid, "uf_json_getdata",this);
			//即将超时提醒
			//JSONObject jsonObject_zzpq = callWebserviceImp.getWebServerInfo("_PAD_KDG_TS_CSYYWL", userid+"*"+userid+"*"+userid+"*"+userid+"*"+userid+"*"+1+"*"+1, "uf_json_getdata",this);
			//预警工单
			//JSONObject jsonObject_yj = callWebserviceImp.getWebServerInfo("_PAD_YWCX_TSYJ_ANDROID_TS", userid, "uf_json_getdata",this);
			
//			JSONArray array_yj = jsonObject_yj.getJSONArray("tableA");
//			for(int i=0;i<array_yj.length();i++){
//				JSONObject object_yj = array_yj.getJSONObject(i);
//				yjnr += object_yj.getString("yjnr")+"。";
//			}
			
//			JSONArray array_zzpq = jsonObject_zzpq.getJSONArray("tableA");
//			for(int i=0;i<array_zzpq.length();i++){
//				JSONObject object_zzpq = array_zzpq.getJSONObject(i);
//				zzpqMsg += object_zzpq.getString("nr")+"。";
//			}
			
			
			
			JSONArray array_zyxx = jsonObject_zyxx.getJSONArray("tableA");
			JSONObject object_zyxx = array_zyxx.getJSONObject(0);
			zyxx_num = object_zyxx.getInt("wds");
			DataCache.getinition().setZzxx_num(object_zyxx.getInt("wds"));
			
			//{"tableA":[{"jssl":"0","wgsl":"0"}],"flag":"1","costTime":127}
			//JSONArray jsonArray = jsonObject.getJSONArray("tableA");
			//JSONObject temp = jsonArray.getJSONObject(0);
			
//			String weijiesou = temp.getString("jssl");
//			String weiwangong = temp.getString("wgsl");
//			weijiesou = weijiesou.split("\\.")[0];
//			weiwangong = weiwangong.split("\\.")[0];
			
//			jsonArray = jsonObject_kdg.getJSONArray("tableA");
//			temp = jsonArray.getJSONObject(0);
//			
//			String weijiesou_kdg = temp.getString("wjds");
//			String weiwangong_kdg = temp.getString("wwgs");
			
			
			
//			txzd = "你有"+weijiesou+"个未接收工单，"+weiwangong+"个未完工单";
//			String txzd_kdg = "你有"+weijiesou_kdg+"个未接收工单，"+weiwangong_kdg+"个未完工单";
//			Intent myintent = new Intent(ACTION_MAIN);
//			myintent.putExtra("tishi", txzd);
//			myintent.putExtra("tishi_kdg", txzd_kdg);
//			sendBroadcast(myintent);
			
			Message msg = new Message();
			msg.what = SUCCESSFUL;// 成功
			handler.sendMessage(msg);
			
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = NETWORK_ERROR;
			handler.sendMessage(msg);
		}
		
		
		
	}
	
	private void addNotificaction() {
		// Log.e("dd", "addNotificaction");
		NotificationManager manager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// 创建一个Notification
		Notification notification = new Notification();
		// 设置显示在手机最上边的状态栏的图标
		notification.icon = R.drawable.logo;
		// 当当前的notification被放到状态栏上的时候，提示内容
		String msg = "您有"+zyxx_num+"条重要消息，请注意查收！";
		notification.tickerText = msg;
		notification.defaults = Notification.DEFAULT_ALL;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		long[] vibrate = { 0, 100, 200, 300 };
		notification.vibrate = vibrate;
		// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setStreamVolume(android.media.AudioManager.STREAM_ALARM,
				7, 0); // tempVolume:音量绝对值
		notification.audioStreamType = AudioManager.ADJUST_RAISE;
		Intent intent = new Intent(this, XxglActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		RemoteViews remoteViews = new RemoteViews(getPackageName(),
				R.layout.notification_remont);
		remoteViews.setImageViewResource(R.id.imageView1, R.drawable.logo);
		remoteViews.setTextViewText(R.id.txtnotification, msg);
		notification.contentView = remoteViews;
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);
		notification.contentIntent = pendingIntent;
		manager.notify(1, notification);

	}
	
	private void addNotificactionZzpq() {
		// Log.e("dd", "addNotificaction");
		NotificationManager manager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// 创建一个Notification
		Notification notification = new Notification();
		// 设置显示在手机最上边的状态栏的图标
		notification.icon = R.drawable.logo;
		// 当当前的notification被放到状态栏上的时候，提示内容
		String msg = zzpqMsg;
		notification.tickerText = msg;
		notification.defaults = Notification.DEFAULT_ALL;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		long[] vibrate = { 0, 100, 200, 300 };
		notification.vibrate = vibrate;
		// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setStreamVolume(android.media.AudioManager.STREAM_ALARM,
				7, 0); // tempVolume:音量绝对值
		notification.audioStreamType = AudioManager.ADJUST_RAISE;
		Intent intent = new Intent(this, Zzpqgdcx.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		RemoteViews remoteViews = new RemoteViews(getPackageName(),
				R.layout.notification_remont);
		remoteViews.setImageViewResource(R.id.imageView1, R.drawable.logo);
		remoteViews.setTextViewText(R.id.txtnotification, msg);
		notification.contentView = remoteViews;
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);
		notification.contentIntent = pendingIntent;
		manager.notify(2, notification);

	}
	
	private void addNotificactionYj() {
		// Log.e("dd", "addNotificaction");
		NotificationManager manager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// 创建一个Notification
		Notification notification = new Notification();
		// 设置显示在手机最上边的状态栏的图标
		notification.icon = R.drawable.logo;
		// 当当前的notification被放到状态栏上的时候，提示内容
		String msg = yjnr;
		notification.tickerText = msg;
		notification.defaults = Notification.DEFAULT_ALL;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		long[] vibrate = { 0, 100, 200, 300 };
		notification.vibrate = vibrate;
		// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
		AudioManager mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mAudioManager.setStreamVolume(android.media.AudioManager.STREAM_ALARM,
				7, 0); // tempVolume:音量绝对值
		notification.audioStreamType = AudioManager.ADJUST_RAISE;
		DataCache.getinition().setQueryType(206);
		DataCache.getinition().setTitle("预警工单");
		Intent intent = new Intent(this, ListKdg.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		RemoteViews remoteViews = new RemoteViews(getPackageName(),
				R.layout.notification_remont);
		remoteViews.setImageViewResource(R.id.imageView1, R.drawable.logo);
		remoteViews.setTextViewText(R.id.txtnotification, msg);
		notification.contentView = remoteViews;
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_ONE_SHOT);
		notification.contentIntent = pendingIntent;
		manager.notify(3, notification);

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case NETWORK_ERROR:
				Log.e("dd", "NumberService NETWORK_ERROR，flag:"+flag);
				
				break;

			case SUCCESSFUL:
				Log.e("dd", "NumberService SUCCESSFUL，flag:"+flag);
				if(zyxx_num>0){
					addNotificaction();
				}
//				if(!"".equals(zzpqMsg)){
//					addNotificactionZzpq();
//				}
//				if(!"".equals(yjnr)){
//					addNotificactionYj();
//				}
//				
				break;

			}
			txzd = "" ;
			
			stopSelf();
		}

	};


}
