package com.single.activity.kdg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.single.activity.FrameActivity;
import com.single.activity.esp.BaiDuGpsActivity;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.common.Constant;
import com.single.gtdzpt.R;
import com.single.utils.Config;
import com.single.utils.DataUtil;

/**
 * 快递柜-上门定位
 * 
 * @author zdkj
 *
 */
public class SmdwKdg extends FrameActivity {

	private TextView tv_time, tv_jd, tv_wd, tv_dz;
	private EditText et_clgc;
	private Spinner sp_gzlb, sp_gzzl, sp_gzlbb, spinner_smfs;
	private String[] from;
	private int[] to;
	private List<Map<String, String>> data_gzbm, data_2_gzbm, data_3_gzbm,
			gzbm_2_list, gzbm_3_list, data_smfs;
	private Button confirm, cancel;
	private String flag, zbh, message, slsj, yysj, ywlx2, zzdh;
	private BDLocation location;
	private LocationClient mLocClient;
	private BDLocationListener myListener = new MyLocationListener();
	private ImageView iv_telphone;
	private boolean hasDw = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_smdw);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				getWebService("querygzlx");

			}
		});

	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("确定");
		cancel.setText("取消");
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_jd = (TextView) findViewById(R.id.tv_jd);
		tv_wd = (TextView) findViewById(R.id.tv_wd);
		tv_dz = (TextView) findViewById(R.id.tv_dz);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		et_clgc = (EditText) findViewById(R.id.et_clgc);
		sp_gzlb = (Spinner) findViewById(R.id.sp_gzlb);
		sp_gzzl = (Spinner) findViewById(R.id.sp_gzzl);
		sp_gzlbb = (Spinner) findViewById(R.id.sp_gzlbb);
		spinner_smfs = (Spinner) findViewById(R.id.spinner_smfs);
		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };
		data_gzbm = new ArrayList<Map<String, String>>();
		data_2_gzbm = new ArrayList<Map<String, String>>();
		data_3_gzbm = new ArrayList<Map<String, String>>();
		gzbm_2_list = new ArrayList<Map<String, String>>();
		gzbm_3_list = new ArrayList<Map<String, String>>();

		data_smfs = new ArrayList<Map<String, String>>();
		Map<String, String> item = new HashMap<String, String>();
		item.put("id", "");
		item.put("name", "");
		data_smfs.add(item);
		item = new HashMap<String, String>();
		item.put("id", "1");
		item.put("name", "人工上门");
		data_smfs.add(item);
		item = new HashMap<String, String>();
		item.put("id", "2");
		item.put("name", "电话完工");
		data_smfs.add(item);
		SimpleAdapter adapter = new SimpleAdapter(SmdwKdg.this, data_smfs,
				R.layout.spinner_item, from, to);
		spinner_smfs.setAdapter(adapter);

		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		slsj = itemmap.get("slsj").toString();
		yysj = itemmap.get("yysj").toString();

		((TextView) findViewById(R.id.tv_rzsblx)).setText(itemmap
				.get("sblx_mc").toString());
		((TextView) findViewById(R.id.tv_bzr)).setText(itemmap.get("khlxr")
				.toString());
		((TextView) findViewById(R.id.tv_wdmc)).setText(itemmap.get("xqmc")
				.toString());
		((TextView) findViewById(R.id.tv_xxdz)).setText(itemmap.get("xxdz")
				.toString());
		((TextView) findViewById(R.id.tv_gzxx)).setText(itemmap.get("gzxx")
				.toString());
		((TextView) findViewById(R.id.tv_lxdh)).setText(itemmap.get("lxdh")
				.toString());
		((TextView) findViewById(R.id.tv_bz)).setText(itemmap.get("bz")
				.toString());
		((TextView) findViewById(R.id.tv_jjcd)).setText(itemmap.get("jjcd")
				.toString());

		iv_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(itemmap.get("lxdh").toString(),zbh);

			}
		});

		/*
		 * 此处需要注意：LocationClient类必须在主线程中声明。需要Context类型的参数。
		 * Context需要时全进程有效的context,推荐用getApplicationConext获取全进程有效的context
		 */
		mLocClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocClient.registerLocationListener(myListener); // 注册监听函数

		setLocationClientOption();
	}

	@Override
	protected void initListeners() {
		//
		OnClickListener backonClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.confirm:
					try {
						Date now = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd hh:mm");
						Date sldate = sdf.parse(slsj);
						long time = now.getTime() - sldate.getTime();
						if (time < 15 * 60 * 1000) {
							dialogShowMessage_P("对不起，规定时间未到，暂时无法提交！", null);
							return;
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					String smfsbm = data_smfs.get(
							spinner_smfs.getSelectedItemPosition()).get("id");
					if ("".equals(smfsbm)) {
						toastShowMessage("请选择上门方式");
						return;
					}
					if ("2".equals(smfsbm)) {// 电话完工
						if ("     ".equals(gzbm_3_list.get(
								sp_gzlbb.getSelectedItemPosition()).get("id"))) {
							toastShowMessage("请选择故障类别！");
							return;
						}
						if (!isNotNull(et_clgc)) {
							toastShowMessage("请录入故障处理过程！");
							return;
						}
						showProgressDialog();
						Config.getExecutorService().execute(new Runnable() {

							@Override
							public void run() {
								getWebService("submitdhwg");
							}
						});
					} else {
						boolean flag = true;
						// 不满15分钟不能提交
						Date now = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss");
						try {
							if (flag) {
								dialogShowMessage("确认到达 ？",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface face,
													int paramAnonymous2Int) {
												showProgressDialog();
												Config.getExecutorService()
														.execute(
																new Runnable() {
																	@Override
																	public void run() {

																		getWebService("submit");
																	}
																});

											}
										}, null);
							}

						} catch (Exception e) {
							Message msg = new Message();
							msg.what = Constant.FAIL;// 失败
							handler.sendMessage(msg);
							e.printStackTrace();
						}
					}

					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);

		spinner_smfs.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 2) {
					findViewById(R.id.ll_gzxx).setVisibility(View.VISIBLE);
					findViewById(R.id.ll_gzxx_content).setVisibility(
							View.VISIBLE);
				} else {
					findViewById(R.id.ll_gzxx).setVisibility(View.GONE);
					findViewById(R.id.ll_gzxx_content).setVisibility(View.GONE);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		OnItemSelectedListener onItemSelectedListener_gzlb = new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				String select_id = data_gzbm.get(arg2).get("id");
				gzbm_2_list.clear();
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", "     ");

				map.put("name", " ");
				gzbm_2_list.add(map);
				// 选择的大类 设置中类
				for (int i = 0; i < data_2_gzbm.size(); i++) {

					String whcs_id = data_2_gzbm.get(i).get("id");
					if (whcs_id.startsWith(select_id)) {
						// 相等添加到维护厂商显示的list里
						gzbm_2_list.add(data_2_gzbm.get(i));
					}
				}

				SimpleAdapter adapter = new SimpleAdapter(SmdwKdg.this,
						gzbm_2_list, R.layout.spinner_item, from, to);
				sp_gzzl.setAdapter(adapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		};

		OnItemSelectedListener onItemSelectedListener_gzzl = new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				String select_id = gzbm_2_list.get(arg2).get("id");
				gzbm_3_list.clear();
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", "     ");

				map.put("name", " ");
				gzbm_3_list.add(map);

				for (int i = 0; i < data_3_gzbm.size(); i++) {

					String whcs_id = data_3_gzbm.get(i).get("id");
					if (whcs_id.startsWith(select_id)) {
						// 相等添加到维护厂商显示的list里
						gzbm_3_list.add(data_3_gzbm.get(i));
					}
				}

				SimpleAdapter adapter = new SimpleAdapter(SmdwKdg.this,
						gzbm_3_list, R.layout.spinner_item, from, to);
				sp_gzlbb.setAdapter(adapter);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		};
		sp_gzlb.setOnItemSelectedListener(onItemSelectedListener_gzlb);// 故障类别
		// 大类
		sp_gzzl.setOnItemSelectedListener(onItemSelectedListener_gzzl);// 故障类别
	}

	@Override
	protected void getWebService(String s) {
		if ("querygzlx".equals(s)) {

			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGZLB", "", "uf_json_getdata", this);
				String flag2 = jsonObject.getString("flag");

				if (Integer.parseInt(flag2) > 0) {
					JSONArray jsonArray2 = jsonObject.getJSONArray("tableA");

					Map<String, String> item = new HashMap<String, String>();

					item.put("id", "     ");
					item.put("name", " ");
					data_gzbm.add(item);
					data_2_gzbm.add(item);
					data_3_gzbm.add(item);

					for (int i = 0; i < jsonArray2.length(); i++) {
						JSONObject temp = jsonArray2.getJSONObject(i);
						item = new HashMap<String, String>();
						String id = temp.getString("gzlbbm");

						item.put("id", id);
						item.put("name", temp.getString("gzlbmc"));
						if (id.length() == 2) {
							data_gzbm.add(item);
						} else if (id.length() == 4) {
							data_2_gzbm.add(item);
						} else {
							data_3_gzbm.add(item);
						}

					}
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = Constant.FAIL;// 失败
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				e.printStackTrace();

				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}
		
		if (s.equals("submitdhjl")) {// 提交
			try {
				String str = zbh;
				this.callWebserviceImp.getWebServerInfo(
						"2#_PAD_KDG_BDDHJL", str, "", "", "uf_json_setdata2",
						this);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if ("submit".equals(s)) {
			String czrz3 = "'0'||chr(42)||'"
					+ DataCache.getinition().getUserId() + "'||chr(42)||'"
					+ DataUtil.toDataString("yyyy-MM-dd HH:mm:ss") + "'";
			String sql = "update shgl_ywgl_fwbgszb set kzzd12='"
					+ tv_jd.getText() + "," + tv_wd.getText() + "',dwdz='"
					+ tv_dz.getText() + "',ddsj=sysdate,djzt=3.5,czrz35="
					+ czrz3 + " where zbh='" + zbh + "'";
			JSONObject jsonObject;
			try {
				jsonObject = callWebserviceImp.getWebServerInfo("_RZ", sql,
						DataCache.getinition().getUserId(), "uf_json_setdata",
						this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message msg = new Message();
					msg.what = Constant.SUBMIT_SUCCESS;// 成功
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = Constant.FAIL;// 失败
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if (s.equals("submitdhwg")) {// 提交
			try {
				String str = zbh + "*PAM*";
				str += ((Map<String, String>) sp_gzlb.getSelectedItem())
						.get("id");
				str += "*PAM*";
				str += ((Map<String, String>) sp_gzzl.getSelectedItem())
						.get("id");
				str += "*PAM*";
				str += ((Map<String, String>) sp_gzlbb.getSelectedItem())
						.get("id");
				str += "*PAM*";
				str += et_clgc.getText().toString();
				str += "*PAM*";
				str += "";
				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"2#_PAD_KDG_DHWG", str, "", "", "uf_json_setdata2",
						this);
				flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message msg = new Message();
					msg.what = Constant.SUBMIT_SUCCESS;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = Constant.FAIL;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}
	}

	/*
	 * BDLocationListener接口有2个方法需要实现： 1.接收异步返回的定位结果，参数是BDLocation类型参数。
	 * 2.接收异步返回的POI查询结果，参数是BDLocation类型参数。
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation locations) {
			if (locations == null) {
				return;
			} else {
				location = locations;
				mLocClient.stop();
				Message msg = new Message();
				msg.what = Constant.NUM_7;// 成功
				handler.sendMessage(msg);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {

		}

		@Override
		public void onConnectHotSpotMessage(String arg0, int arg1) {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * 设置定位参数包括：定位模式（单次定位，定时定位），返回坐标类型，是否打开GPS等等。
	 */
	private void setLocationClientOption() {

		final LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPriority(LocationClientOption.GpsFirst);
		option.setAddrType("all");
		mLocClient.setLocOption(option);
		mLocClient.start();
	}

	@Override
	protected void onDestroy() {
		mLocClient.stop();
		super.onDestroy();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.FAIL:
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;
			case Constant.SUCCESS:
				SimpleAdapter adapter = new SimpleAdapter(SmdwKdg.this,
						data_gzbm, R.layout.spinner_item, from, to);

				sp_gzlb.setAdapter(adapter);
				break;
			case Constant.SUBMIT_SUCCESS:
				dialogShowMessage_P("提交成功",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								onBackPressed();
							}

						});
				break;

			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case Constant.NUM_7:
				tv_time.setText(location.getTime());
				tv_jd.setText("" + location.getLongitude());
				tv_wd.setText("" + location.getLatitude());
				tv_dz.setText("" + location.getAddrStr());
				hasDw = true;
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("currType", 1);
		startActivity(intent);
		finish();
	}

}