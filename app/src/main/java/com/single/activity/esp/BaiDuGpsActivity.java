package com.single.activity.esp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.single.activity.FrameActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.common.Constant;
import com.single.gtdzpt.R;
import com.single.utils.Config;
import com.single.utils.DataUtil;

@SuppressLint({ "SimpleDateFormat", "HandlerLeak" })
public class BaiDuGpsActivity extends FrameActivity {
	private TextView tv_jd, tv_wd, tv_dz, tv_time;
	private ImageView iv_telphone;
	private Button confirm, cancel;
	private String zbh,flag;
	private BDLocation location;
	private String slsj, yysj,msgStr;

	private EditText et_clgc, et_bz;
	private Spinner sp_gzlb, sp_gzzl, sp_gzlbb, spinner_smfs;
	private String[] from;
	private int[] to;
	private List<Map<String, String>> data_gzbm, data_2_gzbm, data_3_gzbm,
			gzbm_2_list, gzbm_3_list, data_smfs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_showgps);
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

	public LocationClient mLocClient;
	public BDLocationListener myListener = new MyLocationListener();

	@Override
	protected void initVariable() {
		tv_jd = (TextView) findViewById(R.id.tv_jd);
		tv_wd = (TextView) findViewById(R.id.tv_wd);
		tv_dz = (TextView) findViewById(R.id.tv_dz);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		tv_time = (TextView) findViewById(R.id.tv_time);
		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);
		et_bz = (EditText) findViewById(R.id.et_bz);
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
		SimpleAdapter adapter = new SimpleAdapter(BaiDuGpsActivity.this,
				data_smfs, R.layout.spinner_item, from, to);
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
	protected void initView() {
		title.setText("工程师到达确认");
		getyytzb();// 营业厅在服务器中的坐标
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.confirm:
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

							// Date pgdate = sdf.parse(slsj);
							// Date yydate = sdf.parse(yysj);
							// if (yydate.getTime() > new Date().getTime()) {
							// dialogShowMessage_P("预约时间未到，请联系服务工程师进行修改。",
							// null);
							// return;
							// }
							// long tv_time = now.getTime() - pgdate.getTime();
							// long gd_time = 15 * 60 * 1000;
							// if (tv_time > gd_time) {
							// // 计算距离 方圆200m内才能定位
							// // double from_lat = Double
							// // .parseDouble(tv_jd.getText()
							// // + "");
							// // double from_lng = Double
							// // .parseDouble(tv_wd.getText()
							// // + "");
							// // double to_lat = Double.parseDouble(xxx + "");
							// // double to_lng = Double.parseDouble(yyy + "");
							// // double s = GPS.getDistance(from_lat,
							// // from_lng,
							// // to_lat, to_lng);
							// // if (s > 200) {
							// // flag = false;
							// // dialogShowMessage_P("请到达后定位！", null);
							// // } else {
							// // flag = true;
							// // }
							// flag = true;
							// } else {
							// dialogShowMessage_P("时间未到，不能定位！", null);
							// return;
							// }

							if (flag) {
								dialogShowMessage("确认到达 ？",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface face,
													int paramAnonymous2Int) {
												showProgressDialog();
												// sure = true;
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

				case R.id.cancel:
					onBackPressed();
					break;

				case R.id.bt_topback:
					onBackPressed();
					break;
				//
				// case R.id.bt_toplogout:
				// skipActivity(LoginActivity.class);
				// break;
				}
			}
		};
		confirm.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		topBack.setOnClickListener(onClickListener);

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

				SimpleAdapter adapter = new SimpleAdapter(
						BaiDuGpsActivity.this, gzbm_2_list,
						R.layout.spinner_item, from, to);
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

				SimpleAdapter adapter = new SimpleAdapter(
						BaiDuGpsActivity.this, gzbm_3_list,
						R.layout.spinner_item, from, to);
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
		/**
		 * 故障类别
		 */
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
					
					
					// DataCache.getinition().setErji_GZLB(data_erji_gzbm);
					// DataCache.getinition().setGZLB(data_gzbm);
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
					msgStr = "获取故障信息失败";
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

		if ("submit".equals(s)) {
			String czrz3 = "'0'||chr(42)||'"
					+ DataCache.getinition().getUserId() + "'||chr(42)||'"
					+ DataUtil.toDataString("yyyy-MM-dd HH:mm:ss") + "'";
			String sql = "update shgl_ywgl_fwbgszb set kzzd12='"
					+ tv_jd.getText() + "," + tv_wd.getText()
					+ "',dwdz='" + tv_dz.getText()
					+ "',ddsj=sysdate,djzt=3.5,czrz35=" + czrz3
					+ " where zbh='" + zbh + "'";
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
					msgStr = "提交信息失败";
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
				str += et_bz.getText().toString();
				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"2#_PAD_KDG_DHWG", str, "", "", "uf_json_setdata2",
						this);
				flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message msg = new Message();
					msg.what = Constant.SUBMIT_SUCCESS;
					handler.sendMessage(msg);
				} else {
					msgStr = "提交信息失败";
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
			}

			location = locations;
			Message msg = new Message();
			msg.what = 7;// 成功
			handler.sendMessage(msg);

		}

		public void onReceivePoi(BDLocation poiLocation) {

		}

		@Override
		public void onConnectHotSpotMessage(String arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	/**
	 * 服务器上营业厅的坐标
	 */
	private void getyytzb() {

		// if ("".equals(intent.getStringExtra("khbm"))) {
		// toastShowMessage("取得坐标为空");
		// return;
		// } else {
		// xzbf = parseDouble(intent.getStringExtra("xxx"));
		// yzbf = parseDouble(intent.getStringExtra("yyy"));
		// }
	}

	public Double parseDouble(String s) {
		if (s != null && !"".equals(s))
			return Double.parseDouble(s);
		return 3333.3;
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {

			case Constant.NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;

			case Constant.SUCCESS:
				SimpleAdapter adapter = new SimpleAdapter(
						BaiDuGpsActivity.this, data_gzbm,
						R.layout.spinner_item, from, to);

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

			case Constant.FAIL:
				dialogShowMessage_P("失败，"+msgStr, null);
				break;

			case 7:
				tv_time.setText(location.getTime());
				tv_jd.setText("" + location.getLongitude());
				tv_wd.setText("" + location.getLatitude());
				tv_dz.setText("" + location.getAddrStr());
				// if (xzbf == 0.0 && yzbf == 0.0) {
				// tv_dz.setText("服务端经纬度为0");
				//
				// } else {
				// // 则小于0在范围内
				// Double jd = Math.abs(xzbf - location.getLongitude()) -
				// 0.00516;// 经度差-范围
				// Double wd = Math.abs(yzbf - location.getLatitude()) -
				// 0.004603;// 维度差-范围
				//
				// if (jd <= 0.0 && wd <= 0.0) {
				// daoda = "定位到达";
				// tv_dz.setText(daoda);
				// } else {
				// daoda = "定位未到达";
				// tv_dz.setText(daoda);
				// }
				// }
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}

	};

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

}
