package com.single.activity.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jauker.widget.BadgeView;
import com.single.activity.FrameActivity;
import com.single.activity.esp.ArrivalConfirmationActivity;
import com.single.activity.esp.BaiDuGpsActivity;
import com.single.activity.esp.InformationReceivingActivity;
import com.single.activity.esp.InformationReceivingRefuseList;
import com.single.activity.esp.InformationReceivinglist;
import com.single.activity.esp.InformationReceivinglist_Dzqd;
import com.single.activity.esp.PGDCaseActivity;
import com.single.activity.esp.ServiceReportsComplete;
import com.single.activity.esp.ServiceReportslist;
import com.single.activity.esp.SheBeiPDActivity;
import com.single.activity.esp.XjListActivity;
import com.single.activity.esp.YytReportedActivity;
import com.single.activity.esp.ZZServiceReportslist;
import com.single.activity.js.FyqrActivity;
import com.single.activity.js.LiuShuiZhanglist;
import com.single.activity.js.YingShouKuanList;
import com.single.activity.kc.KccxActivity;
import com.single.activity.kc.WbdbckActivity;
import com.single.activity.kc.WbdbrkListActivity;
import com.single.activity.kdg.FwbgKdg;
import com.single.activity.kdg.JdxyKdg;
import com.single.activity.kdg.JqgdcxKdg;
import com.single.activity.kdg.ListKdg;
import com.single.activity.kdg.SmdwKdg;
import com.single.activity.kdg.Zzpqgdcx;
import com.single.activity.kdg.ZzzpCxTj;
import com.single.activity.kdg.ZzzpKdg;
import com.single.activity.login.LoginActivity;
import com.single.activity.w.ChangePasswordActivity;
import com.single.activity.w.Fwzxscx;
import com.single.activity.w.GzpActivity;
import com.single.activity.w.GzpLrActivity;
import com.single.activity.w.HelpActivity;
import com.single.activity.w.JszlActivity;
import com.single.activity.w.QyxxActivity;
import com.single.activity.w.XxglActivity;
import com.single.activity.w.YHKInfoActivity;
import com.single.activity.w.YhkxxActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.common.Constant;
import com.single.dodowaterfall.MarqueeTextView;
import com.single.gtdzpt.R;
import com.single.service.CsyyService;
import com.single.service.JdtxService;
import com.single.service.NumberService;
import com.single.service.PatrolService;
import com.single.utils.Config;

@SuppressLint("ResourceAsColor")
public class MainActivity extends FrameActivity implements OnClickListener {

	private LinearLayout linear_sy_top, tab_bottom_esp, tab_bottom_kdg,
			tab_bottom_kc, tab_bottom_js, tab_bottom_w;
	private ImageView sy_top_1, sy_top_2;
	private int num = 1;
	private List<String> menu;
	private TextView tishi, tv_esp, tv_kdg, tv_kc, tv_js, tv_w, tv_ry;
	private ImageView img_esp, img_kdg, img_kc, img_js, img_w;
	private BadgeView badgeView;
	private MarqueeTextView tv_marquee;
	private String ts_num_msg_esp = "", ts_num_msg_kdg = "";
	private int currType = 1; // 当前类型：1.esp 2.快递柜 3.结算 4.库存
	private Long mExitTime = -2000l;

	private List<Map<String, Object>> esp_list, kdg_list, kc_list, js_list,
			w_list;
	private GridView gridview;
	private List<Map<String, Object>> data_list;
	private SimpleAdapter sim_adapter;
	private String[] from;
	private int[] to;

	private ListView listview;
	private SimpleAdapter adapter_listview;
	private List<Map<String, Object>> data_listview;
	private String[] from_listview;
	private int[] to_listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initListeners();
		changeBackground();
		runChangeService();

		Intent intent = new Intent(getApplicationContext(), NumberService.class);
		// 设置一个PendingIntent对象，发送广播
		PendingIntent pi = PendingIntent.getService(this, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				SystemClock.elapsedRealtime(), 30 * 60 * 1000, pi);

		intent = new Intent(getApplicationContext(), JdtxService.class);
		// 设置一个PendingIntent对象，发送广播
		pi = PendingIntent.getService(this, 0, intent, 0);
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				SystemClock.elapsedRealtime(), 60 * 1000, pi);

		// 预警提醒
		intent = new Intent(getApplicationContext(), PatrolService.class);
		pi = PendingIntent.getService(this, 0, intent, 0);
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(),
				12 * 60 * 60 * 1000, pi);
		// 工单超时提醒
		intent = new Intent(getApplicationContext(), CsyyService.class);
		pi = PendingIntent.getService(this, 0, intent, 0);
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(), 30 * 60 * 1000,
				pi);

		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("query");
			}
		});
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("wwgd");
			}
		});

	}

	@Override
	protected void initView() {
		badgeView = new BadgeView(getApplicationContext());
		tishi = (TextView) findViewById(R.id.tishi);
		tv_ry = (TextView) findViewById(R.id.tv_ry);
		linear_sy_top = (LinearLayout) findViewById(R.id.linear_sy_top);
		tab_bottom_esp = (LinearLayout) findViewById(R.id.tab_bottom_esp);
		tab_bottom_kdg = (LinearLayout) findViewById(R.id.tab_bottom_kdg);
		tab_bottom_kc = (LinearLayout) findViewById(R.id.tab_bottom_kc);
		tab_bottom_js = (LinearLayout) findViewById(R.id.tab_bottom_js);
		tab_bottom_w = (LinearLayout) findViewById(R.id.tab_bottom_w);
		tv_marquee = (MarqueeTextView) findViewById(R.id.tv_marquee);
		sy_top_1 = (ImageView) findViewById(R.id.sy_top_1);
		sy_top_2 = (ImageView) findViewById(R.id.sy_top_2);
		img_esp = (ImageView) tab_bottom_esp.findViewById(R.id.img_esp);
		tv_esp = (TextView) tab_bottom_esp.findViewById(R.id.tv_esp);
		img_kdg = (ImageView) tab_bottom_kdg.findViewById(R.id.img_kdg);
		tv_kdg = (TextView) tab_bottom_kdg.findViewById(R.id.tv_kdg);
		img_kc = (ImageView) tab_bottom_kc.findViewById(R.id.img_kc);
		tv_kc = (TextView) tab_bottom_kc.findViewById(R.id.tv_kc);
		img_js = (ImageView) tab_bottom_js.findViewById(R.id.img_js);
		tv_js = (TextView) tab_bottom_js.findViewById(R.id.tv_js);
		img_w = (ImageView) tab_bottom_w.findViewById(R.id.img_w);
		tv_w = (TextView) tab_bottom_w.findViewById(R.id.tv_w);

		gridview = (GridView) findViewById(R.id.gridview);
		from = new String[] { "id", "img", "name", "type" };
		to = new int[] { R.id.menu_id, R.id.menu_img, R.id.menu_name,
				R.id.menu_type };

		listview = (ListView) findViewById(R.id.listview);

		from_listview = new String[] { "textView1", "faultuser", "zbh",
				"timemy", "datemy", "ztzt" };
		to_listview = new int[] { R.id.textView1, R.id.yytmy, R.id.pgdhmy,
				R.id.timemy, R.id.datemy, R.id.ztzt };

		initMenus();

		tv_ry.setText(DataCache.getinition().getUsername());
		Intent intent = getIntent();
		currType = intent.getIntExtra("currType", 1);
		

	}

	@Override
	protected void initListeners() {

		sy_top_1.setOnClickListener(this);
		sy_top_2.setOnClickListener(this);
		linear_sy_top.setOnClickListener(this);
		tv_marquee.setOnClickListener(this);
		tab_bottom_kdg.setOnClickListener(this);
		tab_bottom_esp.setOnClickListener(this);
		tab_bottom_kc.setOnClickListener(this);
		tab_bottom_js.setOnClickListener(this);
		tab_bottom_w.setOnClickListener(this);

		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tv = (TextView) view.findViewById(R.id.menu_id);
				String menu_type = tv.getText().toString();
				turnTo(menu_type);
			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					Map<String, Object> map = data_listview.get(position);
					String djzt = (String) map.get("djzt");
					String ywlx = (String) map.get("ywlx");
					Intent intent = null;
					//if ("维修".equals(ywlx)||"巡检".equals(ywlx)) {
						if ("1".equals(djzt) || "1.5".equals(djzt)) {
							DataCache.getinition().setTitle("组长转派");
							intent = new Intent(MainActivity.this,
									ZzzpKdg.class);
						} else if ("2".equals(djzt)) {
							DataCache.getinition().setTitle("接单响应");
							intent = new Intent(MainActivity.this,
									JdxyKdg.class);
						} else if ("3".equals(djzt)) {
							DataCache.getinition().setTitle("上门定位");
							intent = new Intent(MainActivity.this,
									SmdwKdg.class);
						} else if ("3.5".equals(djzt)) {
							DataCache.getinition().setTitle("服务报告");
							intent = new Intent(MainActivity.this,
									FwbgKdg.class);
						} else if ("3.6".equals(djzt)) {
							DataCache.getinition().setTitle("二次上门");
							intent = new Intent(MainActivity.this,
									FwbgKdg.class);
						} else if ("5".equals(djzt)) {
							// DataCache.getinition().setTitle("二次上门");
							// intent = new Intent(MainActivity.this,
							// FwbgKdgWxEcsm.class);
						}
					//}
					ServiceReportCache.setObjectdata(data_listview);
					ServiceReportCache.setIndex(position);
					startActivityForResult(intent, 1);
				} catch (Exception e) {
				}

			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		listview = (ListView) findViewById(R.id.listview);
		loadMenus();
	}

	private void runChangeService() {
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(5000);
					if (num < 3) {
						num = num + 1;
					} else {
						num = 1;
					}
					Message msg = new Message();
					msg.what = 5;
					handler.sendMessage(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sy_top_1:
			num = 1;
			changeBackground();
			break;
		case R.id.sy_top_2:
			num = 2;
			changeBackground();
			break;
		case R.id.linear_sy_top:
			gotoOtherPage();
			break;
		case R.id.tv_marquee:
			dialogShowMessage_P(tv_marquee.getText().toString(), null);
			break;
		case R.id.tab_bottom_esp:
			currType = 1;
			setMenus();
			break;
		case R.id.tab_bottom_kdg:
			currType = 2;
			setMenus();
			break;
		case R.id.tab_bottom_js:
			currType = 3;
			setMenus();
			break;
		case R.id.tab_bottom_kc:
			currType = 4;
			setMenus();
			break;
		case R.id.tab_bottom_w:
			currType = 5;
			setMenus();
			break;
		default:
			break;
		}

	}

	private void initMenus() {
		esp_list = new ArrayList<Map<String, Object>>();
		Map<String, Object> esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_yytbz");
		esp_map.put("name", "故障申报");
		esp_map.put("img", R.drawable.menu_esp_fwbg);
		esp_list.add(esp_map);
		esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_gdqd");
		esp_map.put("name", "电子抢单");
		esp_map.put("img", R.drawable.menu_esp_fwbg);
		esp_list.add(esp_map);
		esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_jdxy");
		esp_map.put("name", "接单响应");
		esp_map.put("img", R.drawable.menu_esp_jdxy);
		esp_list.add(esp_map);
		esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_jjjd");
		esp_map.put("name", "拒绝接单");
		esp_map.put("img", R.drawable.menu_esp_jjjd);
		esp_list.add(esp_map);
		esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_smddqr");
		esp_map.put("name", "上门定位");
		esp_map.put("img", R.drawable.menu_esp_smdw);
		esp_list.add(esp_map);
		esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_fwbg");
		esp_map.put("name", "服务报告");
		esp_map.put("img", R.drawable.menu_esp_fwbg);
		esp_list.add(esp_map);
		esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_zzfwbg");
		esp_map.put("name", "纸质报告");
		esp_map.put("img", R.drawable.menu_esp_zzbgsc);
		esp_list.add(esp_map);

		esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_sblr");
		esp_map.put("name", "设备录入");
		esp_map.put("img", R.drawable.menu_esp_xxlr);
		esp_list.add(esp_map);
		esp_map = new HashMap<String, Object>();
		esp_map.put("id", "m_pad_fwzxscx");
		esp_map.put("name", "服务咨询师查询");
		esp_map.put("img", R.drawable.menu_esp_fwzxs);
		esp_list.add(esp_map);

		kdg_list = new ArrayList<Map<String, Object>>();
		Map<String, Object> kdg_map = new HashMap<String, Object>();
		kdg_map.put("id", "m_pad_jdxy");
		kdg_map.put("name", "接单响应");
		kdg_map.put("img", R.drawable.menu_esp_jdxy);
		kdg_list.add(kdg_map);
		kdg_map = new HashMap<String, Object>();
		kdg_map.put("id", "m_pad_smddqr");
		kdg_map.put("name", "上门定位");
		kdg_map.put("img", R.drawable.menu_esp_jjjd);
		kdg_list.add(kdg_map);
		kdg_map = new HashMap<String, Object>();
		kdg_map.put("id", "m_pad_fwbg");
		kdg_map.put("name", "服务报告");
		kdg_map.put("img", R.drawable.menu_esp_fwbg);
		kdg_list.add(kdg_map);
		kdg_map = new HashMap<String, Object>();
		kdg_map.put("id", "m_pad_fwbg_xg");
		kdg_map.put("name", "服务报告修改");
		kdg_map.put("img", R.drawable.menu_esp_fwbg);
		kdg_list.add(kdg_map);
		kdg_map = new HashMap<String, Object>();
		kdg_map.put("id", "m_pad_ecsm");
		kdg_map.put("name", "二次上门");
		kdg_map.put("img", R.drawable.menu_esp_zzbgsc);
		kdg_list.add(kdg_map);

		kc_list = new ArrayList<Map<String, Object>>();
		Map<String, Object> kc_map = new HashMap<String, Object>();
		kc_map.put("id", "m_pad_ckdqkc");
		kc_map.put("name", "当前库存");
		kc_map.put("img", R.drawable.menu_kc_dqkccx);
		kc_list.add(kc_map);
		kc_map = new HashMap<String, Object>();
		kc_map.put("id", "m_pad_wbdbrk");
		kc_map.put("name", "外部调拨入库");
		kc_map.put("img", R.drawable.menu_kc_wbdbrk);
		kc_list.add(kc_map);
		kc_map = new HashMap<String, Object>();
		kc_map.put("id", "m_pad_wbdbck");
		kc_map.put("name", "外部调拨出库");
		kc_map.put("img", R.drawable.menu_kc_wbdbck);
		kc_list.add(kc_map);

		js_list = new ArrayList<Map<String, Object>>();
		Map<String, Object> js_map = new HashMap<String, Object>();
		js_map.put("id", "m_pad_dqyskcx");
		js_map.put("name", "应收款");
		js_map.put("img", R.drawable.menu_js_dqyskcx);
		js_list.add(js_map);
		// js_map = new HashMap<String, Object>();
		// js_map.put("id", "m_pad_yslszcx");
		// js_map.put("name", "流水账");
		// js_map.put("img", R.drawable.menu_js_yslszcx);
		// js_list.add(js_map);
		// js_map = new HashMap<String, Object>();
		// js_map.put("id", "m_pad_srqr");
		// js_map.put("name", "费用确认");
		// js_map.put("img", R.drawable.menu_js_fyqr);
		// js_list.add(js_map);

		w_list = new ArrayList<Map<String, Object>>();
		Map<String, Object> w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_qyjjg");
		w_map.put("name", "区域及价格");
		w_map.put("img", R.drawable.menu_w_qyyjg);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_gxpq_gdcx");
		w_map.put("name", "片区工单查询");
		w_map.put("img", R.drawable.menu_kdg_dhjc);
		w_list.add(w_map);
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_pgdcx");
		w_map.put("name", "近期工单查询");
		w_map.put("img", R.drawable.menu_esp_jqgdcx);
		w_list.add(w_map);
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_esp_zzzp");
		w_map.put("name", "组长转派");
		w_map.put("img", R.drawable.menu_kdg_dhjc);
		w_list.add(w_map);
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_tsyj");
		w_map.put("name", "预警工单");
		w_map.put("img", R.drawable.menu_w_help);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_yhkxx");
		w_map.put("name", "银行卡信息");
		w_map.put("img", R.drawable.menu_w_yhk);
		w_list.add(w_map);
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_zyxx");
		w_map.put("name", "重要消息");
		w_map.put("img", R.drawable.menu_w_zyxx);
		w_list.add(w_map);
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_ryxx");
		w_map.put("name", "人员信息");
		w_map.put("img", R.drawable.menu_w_gzz);
		w_list.add(w_map);
		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_ryxxlr");
		w_map.put("name", "人员信息录入");
		w_map.put("img", R.drawable.menu_w_gzzlr);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_xgmm");
		w_map.put("name", "修改密码");
		w_map.put("img", R.drawable.menu_w_xgmm);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_help");
		w_map.put("name", "帮助");
		w_map.put("img", R.drawable.menu_w_help);
		w_list.add(w_map);

		w_map = new HashMap<String, Object>();
		w_map.put("id", "m_pad_jszl");
		w_map.put("name", "技术资料");
		w_map.put("img", R.drawable.menu_w_jszl);
		w_list.add(w_map);
	}

	private void loadMenus() {

		menu = DataCache.getinition().getMenu();
		if (menu == null || menu.isEmpty()) {
			toastShowMessage("基础数据过时，重新登录！！");
			skipActivity(LoginActivity.class);
			finish();
			return;
		}
		setMenus();
	}

	private void setMenus() {
		if (currType == 1) {
			Config.getExecutorService().execute(new Runnable() {

				@Override
				public void run() {

					getWebService("query");
				}
			});
			tishi.setText(ts_num_msg_esp);
			tv_esp.setTextColor(getResources().getColor(R.color.blue));
			tv_kdg.setTextColor(getResources().getColor(R.color.qianhui));
			tv_js.setTextColor(getResources().getColor(R.color.qianhui));
			tv_kc.setTextColor(getResources().getColor(R.color.qianhui));
			tv_w.setTextColor(getResources().getColor(R.color.qianhui));
			img_esp.setBackgroundResource(R.drawable.btn_esp_up);
			img_kdg.setBackgroundResource(R.drawable.btn_kdg_down);
			img_js.setBackgroundResource(R.drawable.btn_js_down);
			img_kc.setBackgroundResource(R.drawable.btn_kc_down);
			img_w.setBackgroundResource(R.drawable.btn_w_down);
			loadEspMenus();
		} else if (currType == 2) {
			tishi.setText(ts_num_msg_kdg);
			tv_kdg.setTextColor(getResources().getColor(R.color.blue));
			tv_esp.setTextColor(getResources().getColor(R.color.qianhui));
			tv_js.setTextColor(getResources().getColor(R.color.qianhui));
			tv_kc.setTextColor(getResources().getColor(R.color.qianhui));
			tv_w.setTextColor(getResources().getColor(R.color.qianhui));
			img_esp.setBackgroundResource(R.drawable.btn_esp_down);
			img_kdg.setBackgroundResource(R.drawable.btn_kdg_up);
			img_js.setBackgroundResource(R.drawable.btn_js_down);
			img_kc.setBackgroundResource(R.drawable.btn_kc_down);
			img_w.setBackgroundResource(R.drawable.btn_w_down);
			loadKdgMenus();
		} else if (currType == 3) {
			tishi.setText("");
			tv_js.setTextColor(getResources().getColor(R.color.blue));
			tv_kc.setTextColor(getResources().getColor(R.color.qianhui));
			tv_kdg.setTextColor(getResources().getColor(R.color.qianhui));
			tv_esp.setTextColor(getResources().getColor(R.color.qianhui));
			tv_w.setTextColor(getResources().getColor(R.color.qianhui));
			img_js.setBackgroundResource(R.drawable.btn_js_up);
			img_kc.setBackgroundResource(R.drawable.btn_kc_down);
			img_esp.setBackgroundResource(R.drawable.btn_esp_down);
			img_kdg.setBackgroundResource(R.drawable.btn_kdg_down);
			img_w.setBackgroundResource(R.drawable.btn_w_down);
			loadJsMenus();
		} else if (currType == 4) {
			tishi.setText("");
			tv_js.setTextColor(getResources().getColor(R.color.qianhui));
			tv_kc.setTextColor(getResources().getColor(R.color.blue));
			tv_kdg.setTextColor(getResources().getColor(R.color.qianhui));
			tv_esp.setTextColor(getResources().getColor(R.color.qianhui));
			tv_w.setTextColor(getResources().getColor(R.color.qianhui));
			img_js.setBackgroundResource(R.drawable.btn_js_down);
			img_kc.setBackgroundResource(R.drawable.btn_kc_up);
			img_esp.setBackgroundResource(R.drawable.btn_esp_down);
			img_kdg.setBackgroundResource(R.drawable.btn_kdg_down);
			img_w.setBackgroundResource(R.drawable.btn_w_down);
			loadKcMenus();
		} else if (currType == 5) {
			tishi.setText("");
			badgeView.setTargetView(null);
			DataCache.getinition().setZzxx_num(0);
			tv_js.setTextColor(getResources().getColor(R.color.qianhui));
			tv_kc.setTextColor(getResources().getColor(R.color.qianhui));
			tv_kdg.setTextColor(getResources().getColor(R.color.qianhui));
			tv_esp.setTextColor(getResources().getColor(R.color.qianhui));
			tv_w.setTextColor(getResources().getColor(R.color.blue));
			img_js.setBackgroundResource(R.drawable.btn_js_down);
			img_kc.setBackgroundResource(R.drawable.btn_kc_down);
			img_esp.setBackgroundResource(R.drawable.btn_esp_down);
			img_kdg.setBackgroundResource(R.drawable.btn_kdg_down);
			img_w.setBackgroundResource(R.drawable.btn_w_up);
			loadWMenus();
		}
	}

	private void loadEspMenus() {
		gridview.setVisibility(View.GONE);
		listview.setVisibility(View.VISIBLE);
		// data_list = new ArrayList<Map<String, Object>>();
		//
		// for (int i = 0; i < esp_list.size(); i++) {
		// Map<String, Object> map = esp_list.get(i);
		// for (int k = 0; k < menu.size(); k++) {
		// if (map.get("id").equals(menu.get(k))) {
		// // map.put("type", "esp");
		// data_list.add(map);
		// break;
		// }
		// }
		// }
		//
		// sim_adapter = new CurrAdapter(this, data_list, R.layout.include_menu,
		// from, to);
		// // 配置适配器
		// gridview.setAdapter(sim_adapter);
	}

	private void loadKdgMenus() {
		gridview.setVisibility(View.VISIBLE);
		listview.setVisibility(View.GONE);
		data_list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < kdg_list.size(); i++) {
			Map<String, Object> map = kdg_list.get(i);
			for (int k = 0; k < menu.size(); k++) {
				if (map.get("id").equals(menu.get(k))) {
					// map.put("type", "kdg");
					data_list.add(map);
					break;
				}
			}
		}

		sim_adapter = new CurrAdapter(this, data_list, R.layout.include_menu,
				from, to);
		// 配置适配器
		gridview.setAdapter(sim_adapter);
	}

	private void loadKcMenus() {
		gridview.setVisibility(View.VISIBLE);
		listview.setVisibility(View.GONE);
		data_list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < kc_list.size(); i++) {
			Map<String, Object> map = kc_list.get(i);
			for (int k = 0; k < menu.size(); k++) {
				if (map.get("id").equals(menu.get(k))) {
					// map.put("type", "kc");
					data_list.add(map);
					break;
				}
			}
		}

		sim_adapter = new CurrAdapter(this, data_list, R.layout.include_menu,
				from, to);
		// 配置适配器
		gridview.setAdapter(sim_adapter);
	}

	private void loadJsMenus() {
		gridview.setVisibility(View.VISIBLE);
		listview.setVisibility(View.GONE);
		data_list = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < js_list.size(); i++) {
			Map<String, Object> map = js_list.get(i);
			for (int k = 0; k < menu.size(); k++) {
				if (map.get("id").equals(menu.get(k))) {
					// map.put("type", "js");
					data_list.add(map);
					break;
				}
			}
		}

		sim_adapter = new CurrAdapter(this, data_list, R.layout.include_menu,
				from, to);
		// 配置适配器
		gridview.setAdapter(sim_adapter);
	}

	private void loadWMenus() {
		gridview.setVisibility(View.VISIBLE);
		listview.setVisibility(View.GONE);
		menu.add("m_pad_qyjjg");
		menu.add("m_pad_yhkxx");
		// menu.add("m_pad_ryxx");
		// menu.add("m_pad_ryxxlr");

		// menu.add("m_pad_help");
		// menu.add("m_pad_jszl");

		data_list = new ArrayList<Map<String, Object>>();
		int zyxx_num = 0;
		for (int i = 0; i < w_list.size(); i++) {
			Map<String, Object> map = w_list.get(i);
			for (int k = 0; k < menu.size(); k++) {
				if (map.get("id").equals(menu.get(k))) {
					if ("m_pad_zyxx".equals(map.get("id"))) {
						zyxx_num = i;
					}
					data_list.add(map);
					break;
				}
			}
		}

		sim_adapter = new CurrAdapter(this, data_list, R.layout.include_menu,
				from, to);
		// 配置适配器
		gridview.setAdapter(sim_adapter);

//		 View v = (View) gridview.getAdapter().getView(zyxx_num, null, null);
//		 TextView tv = (TextView) v.findViewById(R.id.menu_name);
//		 badgeView.setTargetView(tv);
//		 badgeView.setBadgeCount(1);
	}

	private void turnTo(String menu_type) {

		if ("m_pad_yytbz".equals(menu_type)) {
			skipActivity(YytReportedActivity.class);
			DataCache.getinition().setTitle("故障申报");
			return;
		} else if ("m_pad_gdqd".equals(menu_type)) {
			skipActivity(InformationReceivinglist_Dzqd.class);
			DataCache.getinition().setTitle("电子抢单");
			return;
		} else if ("m_pad_jdxy".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(201);
			DataCache.getinition().setTitle("接单响应");
			return;
		} else if ("m_pad_jjjd".equals(menu_type)) {
			skipActivity(InformationReceivingRefuseList.class);
			DataCache.getinition().setTitle("拒绝接单");
			return;
		} else if ("m_pad_smddqr".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(203);
			DataCache.getinition().setTitle("上门定位");
			return;
		} else if ("m_pad_fwbg".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(204);
			DataCache.getinition().setTitle("电子服务报告");
			return;
		} else if ("m_pad_fwbg_xg".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2042);
			DataCache.getinition().setTitle("服务报告修改");
			return;
		} else if ("m_pad_ecsm".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2041);
			DataCache.getinition().setTitle("二次上门");
			return;
		} else if ("m_pad_zzfwbg".equals(menu_type)) {
			skipActivity(ZZServiceReportslist.class);
			DataCache.getinition().setTitle("纸质服务报告");
			return;
		} else if ("m_pad_pgdcx".equals(menu_type)) {
			Intent intent = new Intent(getApplicationContext(),
					PGDCaseActivity.class);
			intent.putExtra("time", "2013-6-28*2013-7-13");
			startActivity(intent);
			DataCache.getinition().setTitle("近期工单查询");
			return;
		} else if ("m_pad_sblr".equals(menu_type)) {
			skipActivity(SheBeiPDActivity.class);
			DataCache.getinition().setTitle("设备信息录入");
			return;
		} else if ("m_pad_fwzxscx".equals(menu_type)) {
			skipActivity(Fwzxscx.class);
			DataCache.getinition().setTitle("服务咨询师查询");
			return;
		} else if ("m_kdg_jdxy_kuaidigui".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(201);
			DataCache.getinition().setTitle("快递柜-接单响应");
			return;
		} else if ("m_kdg_jjgd_kuaidigui".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(202);
			DataCache.getinition().setTitle("快递柜-拒绝接单");
			return;
		} else if ("m_kdg_ddqr_kuaidigui".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(203);
			DataCache.getinition().setTitle("快递柜-上门定位");
			return;
		} else if ("m_kdg_fwbg_kuaidigui".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(204);
			DataCache.getinition().setTitle("快递柜-服务报告");
			return;
		} else if ("m_pad_jqgdcx_kuaidigui".equals(menu_type)) {
			skipActivity(JqgdcxKdg.class);
			DataCache.getinition().setTitle("快递柜-近期工单查询");
			return;
		} else if ("".equals(menu_type)) {
			skipActivity(XjListActivity.class);
			DataCache.getinition().setTitle("巡检");
			return;
		} else if ("m_zh_xjjiedan".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(208);
			DataCache.getinition().setTitle("巡检接单");
			return;
		} else if ("m_zh_xjdingwei".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(209);
			DataCache.getinition().setTitle("巡检定位");
			return;
		} else if ("m_zh_sjxj".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(210);
			DataCache.getinition().setTitle("巡检服务报告");
			return;
		} else if ("m_kdg_dhjc_kuaidigui".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2101);
			DataCache.getinition().setTitle("到货检查");
			return;
		} else if ("m_pad_esp_zzzp".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(2204);
			DataCache.getinition().setTitle("组长转派");
			return;
		} else if ("m_pad_gxpq_gdcx".equals(menu_type)) {
			skipActivity(Zzpqgdcx.class);
			DataCache.getinition().setTitle("片区工单查询");
			return;

		} else if ("m_pad_dqyskcx".equals(menu_type)) {
			skipActivity(YingShouKuanList.class);
			DataCache.getinition().setTitle("结算-当前应收款");
			return;
		} else if ("m_pad_yslszcx".equals(menu_type)) {
			skipActivity(LiuShuiZhanglist.class);
			DataCache.getinition().setTitle("结算-应收流水账");
			return;
		} else if ("m_pad_srqr".equals(menu_type)) {
			skipActivity(FyqrActivity.class);
			DataCache.getinition().setTitle("结算-费用确认");
			return;
		} else if ("m_pad_ckdqkc".equals(menu_type)) {
			skipActivity(KccxActivity.class);
			DataCache.getinition().setTitle("库存-当前库存查询");
			return;
		} else if ("m_pad_wbdbrk".equals(menu_type)) {
			skipActivity(WbdbrkListActivity.class);
			DataCache.getinition().setTitle("库存-外部调拨入库");
			return;
		} else if ("m_pad_wbdbck".equals(menu_type)) {
			skipActivity(WbdbckActivity.class);
			DataCache.getinition().setTitle("库存-外部调拨出库");
			return;
		} else if ("m_pad_qyjjg".equals(menu_type)) {
			skipActivity(QyxxActivity.class);
			DataCache.getinition().setTitle("区域信息");
			return;
		} else if ("m_pad_yhkxx".equals(menu_type)) {
			skipActivity(YHKInfoActivity.class);
			DataCache.getinition().setTitle("银行卡信息");
			return;
		} else if ("m_pad_zyxx".equals(menu_type)) {
			skipActivity(XxglActivity.class);
			DataCache.getinition().setTitle("重要消息");
			return;
		} else if ("m_pad_xgmm".equals(menu_type)) {
			skipActivity(ChangePasswordActivity.class);
			DataCache.getinition().setTitle("修改密码");
			return;
		} else if ("m_pad_ryxx".equals(menu_type)) {
			skipActivity(GzpActivity.class);
			DataCache.getinition().setTitle("工作证");
			return;
		} else if ("m_pad_ryxxlr".equals(menu_type)) {
			skipActivity(GzpLrActivity.class);
			DataCache.getinition().setTitle("工作证录入");
			return;
		} else if ("m_pad_help".equals(menu_type)) {
			skipActivity(HelpActivity.class);
			DataCache.getinition().setTitle("帮助");
			return;
		} else if ("m_pad_jszl".equals(menu_type)) {
			skipActivity(JszlActivity.class);
			DataCache.getinition().setTitle("技术资料");
			return;
		} else if ("m_pad_tsyj".equals(menu_type)) {
			skipActivity(ListKdg.class);
			DataCache.getinition().setQueryType(206);
			DataCache.getinition().setTitle("预警工单");
			return;
		}

	}

	private void gotoOtherPage() {
		// String url = "http://www.baidu.com";
		// switch (num) {
		// case 1:
		// url = "http://www.baidu.com";
		// break;
		// case 2:
		// url = "http://www.sina.com";
		// break;
		// case 3:
		// url = "http://china.nba.com/boxscore/";
		// break;
		// default:
		// break;
		// }
		// Uri uri = Uri.parse(url);
		// Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		// startActivity(intent);
	}

	private void changeBackground() {
		sy_top_1.setBackgroundResource(R.drawable.btn_sy_point_up);
		sy_top_2.setBackgroundResource(R.drawable.btn_sy_point_up);
		switch (num) {
		case 1:
			sy_top_1.setBackgroundResource(R.drawable.btn_sy_point_down);
			linear_sy_top.setBackgroundResource(R.drawable.a);
			break;
		case 2:
			sy_top_2.setBackgroundResource(R.drawable.btn_sy_point_down);
			linear_sy_top.setBackgroundResource(R.drawable.b);
			break;
		default:
			break;
		}

	}

	@Override
	protected void initVariable() {

	}

	protected void getWebService(String s) {
		if ("query".equals(s)) {
			try {
				String sqlid = "_PAD_SHGL_GDALL_WWG";
				String cs = DataCache.getinition().getUserId();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						sqlid, cs, "uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				data_listview = new ArrayList<Map<String, Object>>();
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						String timeff = "";
						item.put("textView1", getListItemIcon(i));
						timeff = temp.getString("bzsj");
						item.put("bzsj", timeff);
						item.put("faultuser", temp.getString("xqmc") + "   "
								+ temp.getString("xxdz"));
						item.put("zbh", temp.getString("zbh"));
						item.put("sx", temp.getString("sx"));
						item.put("qy", temp.getString("qy"));
						item.put("djzt", temp.getString("djzt"));
						item.put("xqmc", temp.getString("xqmc"));
						item.put("xxdz", temp.getString("xxdz"));
						item.put("gzxx", temp.getString("gzxx"));
						item.put("ywlx", temp.getString("ywlx"));
						item.put("ywlx2", temp.getString("ywlx2"));
						item.put("zdh", temp.getString("zdh"));
						item.put("bz", temp.getString("bz"));
						item.put("sf", temp.getString("sf"));
						item.put("ds", temp.getString("ds"));
						item.put("fwnr", temp.getString("fwnr"));
						item.put("khbm", temp.getString("khbm"));
						item.put("sflx", temp.getString("sflx"));
						item.put("ywhy", temp.getString("ywhy"));
						item.put("sblx", temp.getString("sblx"));
						item.put("sblx_mc", temp.getString("sblx_mc"));

						item.put("sbxh", temp.getString("sbxh"));
						item.put("wxts", temp.getString("wxts"));
						item.put("khlxr", temp.getString("khlxr"));
						item.put("yysj", temp.getString("yysj"));
						item.put("ddsj", temp.getString("ddsj"));
						item.put("wgsj", temp.getString("wgsj"));
						item.put("slsj", temp.getString("slsj"));
						item.put("lxdh", temp.getString("lxdh"));
						item.put("dygdh", temp.getString("dygdh"));
						item.put("sfecgd", temp.getString("sfecgd"));
						item.put("sbewm", temp.getString("sbewm"));

						item.put("cx", temp.getString("cx"));
						item.put("fbf", temp.getString("fbf"));
						item.put("smsf", temp.getString("smsf"));
						item.put("zxsxm", temp.getString("zxsxm"));
						item.put("zxsdh", temp.getString("zxsdh"));
						
						item.put("btgyy", temp.getString("btgyy"));
						item.put("gdnszt", temp.getString("gdnszt"));// 0未审 1已通过 2未通过，修改后又变成未审状态
						item.put("rgf", temp.getString("rgf"));
						item.put("bjf", temp.getString("bjf"));
						item.put("fyhj", temp.getString("fyhj"));
						item.put("kzzf3", temp.getString("kzzf3"));
						item.put("kzzf4", temp.getString("kzzf4"));
						item.put("kzzf5", temp.getString("kzzf5"));
						item.put("clgc", temp.getString("clgc"));

						item.put("ecsmyy", temp.getString("ecsmyy"));
						item.put("yysm", temp.getString("yysm"));
						item.put("kzzd17", temp.getString("kzzd17"));
						item.put("jjcd", temp.getString("fwjjcd"));

						item.put("timemy", temp.getString("ywlx"));
						item.put("datemy", temp.getString("djzt2"));
						data_listview.add(item);
					}
					
				} 
				ServiceReportCache.setObjectdata(data_listview);
				Message msg = new Message();
				msg.what = Constant.SUCCESS;// 成功
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if (s.equals("wwgd")) {// 提交
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_YGXX_WDS", DataCache.getinition().getUserId()
								+ "*" + DataCache.getinition().getUserId(),
						"uf_json_getdata", this);
				String flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					JSONObject temp = jsonArray.getJSONObject(0);
					DataCache.getinition().setZzxx_num(temp.getInt("wds"));
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_TIS", DataCache.getinition().getUserId(),
							"uf_json_getdata", this);
					jsonArray = jsonObject.getJSONArray("tableA");
					temp = jsonArray.getJSONObject(0);
					String weijiesou = temp.getString("jssl");
					String weiwangong = temp.getString("wgsl");
					weijiesou = weijiesou.split("\\.")[0];
					weiwangong = weiwangong.split("\\.")[0];
					ts_num_msg_esp = "你有"+weijiesou+"个未接收工单，"+weiwangong+"个未完工单";
				} else {
					DataCache.getinition().setZzxx_num(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
				DataCache.getinition().setZzxx_num(0);
			}
			Message msg = new Message();
			msg.what = Constant.NUM_8;
			handler.sendMessage(msg);
		}
	}

	private class UiUpdateRecevice extends BroadcastReceiver {
		// 更新带完工数量，和带接收数量
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(NumberService.ACTION_MAIN)) {
				//ts_num_msg_kdg = intent.getStringExtra("tishi_kdg");
				ts_num_msg_esp = intent.getStringExtra("tishi");
				tishi.setText(ts_num_msg_esp);
//				if (currType == 1) {
//					tishi.setText(ts_num_msg_esp);
//				} else if (currType == 2) {
//					tishi.setText(ts_num_msg_kdg);
//				}
			}
		}
	}

	UiUpdateRecevice uur;

	@SuppressLint("NewApi")
	private void showLoginMsg(final Map<String, String> map) {

		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_popupwin, null);
		WebView webview = (WebView) view.findViewById(R.id.webview);
		webview.loadUrl(map.get("bz").toString());
		AlertDialog.Builder builder = new Builder(this);
		builder.setCancelable(false);
		builder.setView(view);
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface face, int paramAnonymous2Int) {

				if ("0".equals(map.get("xxzt"))) {
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {
							try {
								String sql = "insert into esp_sjxx_zjb (rybm,xxbm,lrsj,xxzt) values ("
										+ "'"
										+ DataCache.getinition().getUserId()
										+ "',"
										+ "'"
										+ map.get("zbh")
										+ "',"
										+ "sysdate," + "'1')";
								callWebserviceImp.getWebServerInfo("_RZ", sql,
										DataCache.getinition().getUserId(),
										"uf_json_setdata",
										getApplicationContext());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
				map.put("xxzt", "1");
				DataCache.getinition().setLogin_show_map(map);
			}
		});
		builder.create().show();
	}

	@Override
	protected void onStart() {
		uur = new UiUpdateRecevice();
		IntentFilter intentFilter = new IntentFilter(NumberService.ACTION_MAIN);
		intentFilter.setPriority(1000);

		registerReceiver(uur, intentFilter);

		super.onStart();
	}

	@Override
	protected void onStop() {
		unregisterReceiver(uur);
		super.onStop();
	}

	/**
	 * 重写回退键
	 */
	@Override
	public void onBackPressed() {
		if ((System.currentTimeMillis() - mExitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else {
			Intent intent = new Intent(getApplicationContext(),
					LoginActivity.class);
			startActivity(intent);
			finish();
		}
	}

	private class CurrAdapter extends SimpleAdapter {

		public CurrAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			final View view = super.getView(position, convertView, parent);
			LinearLayout l = (LinearLayout) view.findViewById(R.id.ll_menu);
			if (position % 4 == 0) {
				l.setBackgroundResource(R.drawable.menu_esp_yellow);
			} else if (position % 4 == 1) {
				l.setBackgroundResource(R.drawable.menu_esp_red);
			} else if (position % 4 == 2) {
				l.setBackgroundResource(R.drawable.menu_esp_green);
			} else if (position % 4 == 3) {
				l.setBackgroundResource(R.drawable.menu_esp_blue);
			}
			return view;
		}

	}

	private class CurrKdgAdapter extends SimpleAdapter {

		public CurrKdgAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			final View view = super.getView(position, convertView, parent);
			LinearLayout l = (LinearLayout) view.findViewById(R.id.ll_menu);
			if (position % 4 == 0) {
				l.setBackgroundResource(R.drawable.menu_kdg_yellow);
			} else if (position % 4 == 1) {
				l.setBackgroundResource(R.drawable.menu_kdg_red);
			} else if (position % 4 == 2) {
				l.setBackgroundResource(R.drawable.menu_kdg_green);
			} else if (position % 4 == 3) {
				l.setBackgroundResource(R.drawable.menu_kdg_blue);
			}
			return view;
		}

	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
			switch (msg.what) {
			case NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;

			case FAIL:
				dialogShowMessage_P("获取数据失败", null);
				break;
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;
			case Constant.SUCCESS:
				adapter_listview = new SimpleAdapter(MainActivity.this,
						ServiceReportCache.getObjectdata(),
						R.layout.listview_dispatchinginformationreceiving_item,
						from_listview, to_listview);
				listview.setAdapter(adapter_listview);
				adapter_listview.notifyDataSetChanged();
				break;
			case Constant.FAIL:
				listview.setAdapter(null);
				if (adapter_listview != null) {
					adapter_listview.notifyDataSetChanged();
				}
				break;
			case 5:
				changeBackground();
				break;
			case Constant.NUM_8:
				if (DataCache.getinition().getZzxx_num() > 0) {
					badgeView.setTargetView(tab_bottom_w
							.findViewById(R.id.tab_bottom_w_bv));
					badgeView.setBadgeCount(DataCache.getinition()
							.getZzxx_num());
				}
				tishi.setText(ts_num_msg_esp);
				tv_marquee.setText(DataCache.getinition().getTsxx());

				if (!DataCache.getinition().isHasYHK()) {
					dialogShowMessage("银行卡信息未完善，建议立即完善（您也可以在人员基础信息界面中完善）。去完善？",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									Intent intent = new Intent(
											getApplicationContext(),
											YhkxxActivity.class);
									startActivityForResult(intent, 1);
								}
							}, null);
				}

				// 显示提示信息
				Map<String, String> map = DataCache.getinition()
						.getLogin_show_map();
				// map = new HashMap<String, String>();
				// map.put("bz",
				// "http://115.29.147.137:8083/esp/yk/20160627.html?v=1");
				// map.put("xxzt", "1");
				// showLoginMsg(map);
				if (map != null && "0".equals(map.get("xxzt"))) {
					showLoginMsg(map);
				}
				break;
			}

		};
	};

}