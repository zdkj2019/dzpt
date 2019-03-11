package com.single.activity.kdg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.single.activity.FrameActivity;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.common.Constant;
import com.single.gtdzpt.R;
import com.single.utils.Config;
import com.single.utils.DataUtil;

public class ZzzpKdg extends FrameActivity {

	private Button confirm, cancel;
	private String flag, zbh, type = "1", message, ywlx, khbm, lxrdh, jdrdh,
			pqid, qxid;
	private Spinner spinner_pq, spinner_qx, spinner_jdry;
	private ImageView iv_telphone, iv_lxr_telphone,iv_telphone_bzr;
	private TextView tv_phone;
	private String[] from;
	private int[] to;
	private ArrayList<Map<String, String>> data_ry, data_pq, data_qx;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_zzzp);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				getWebService("getpq");
			}
		});

	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		iv_lxr_telphone = (ImageView) findViewById(R.id.iv_lxr_telphone);
		iv_telphone_bzr = (ImageView) findViewById(R.id.iv_telphone_bzr);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		confirm.setText("接单");
		cancel.setText("转派");

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		spinner_jdry = (Spinner) findViewById(R.id.spinner_jdry);
		spinner_pq = (Spinner) findViewById(R.id.spinner_pq);
		spinner_qx = (Spinner) findViewById(R.id.spinner_qx);

		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };

		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		ywlx = itemmap.get("ywlx").toString();
		khbm = itemmap.get("khbm").toString();
		lxrdh = itemmap.get("lxdh").toString();
		((TextView) findViewById(R.id.tv_zbh)).setText(zbh);
		((TextView) findViewById(R.id.tv_smsf)).setText(itemmap.get("smsf")
				.toString());
		((TextView) findViewById(R.id.tv_sf)).setText(itemmap.get("sf")
				.toString());
		((TextView) findViewById(R.id.tv_ds)).setText(itemmap.get("sx")
				.toString());
		((TextView) findViewById(R.id.tv_qx)).setText(itemmap.get("qy")
				.toString());
		((TextView) findViewById(R.id.tv_wdmc)).setText(itemmap.get("xqmc")
				.toString());
		((TextView) findViewById(R.id.tv_jswz)).setText("001".equals(itemmap.get(
				"cx").toString()) ? "城区" : "乡镇");
		((TextView) findViewById(R.id.tv_xxdz)).setText(itemmap.get("xxdz")
				.toString());
		((TextView) findViewById(R.id.tv_gzxx)).setText(itemmap.get("gzxx")
				.toString());
		((TextView) findViewById(R.id.tv_bzsj)).setText(itemmap.get("bzsj")
				.toString());
		((TextView) findViewById(R.id.tv_ywlx)).setText(itemmap.get("ywlx")
				.toString());
		((TextView) findViewById(R.id.tv_bzr)).setText(itemmap.get("khlxr")
				.toString());
		((TextView) findViewById(R.id.tv_jjcd)).setText(itemmap.get("jjcd")
				.toString());
		((TextView) findViewById(R.id.tv_lxr_phone)).setText(itemmap.get(
				"zxsxm").toString());

		iv_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(tv_phone.getText().toString(),zbh);
			}
		});
		iv_telphone_bzr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(itemmap.get("lxdh").toString(),zbh);
			}
		});
		iv_lxr_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(itemmap.get("zxsdh").toString(),zbh);
			}
		});
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
					String pqbm = data_pq.get(
							spinner_pq.getSelectedItemPosition()).get("id");
					if ("".equals(pqbm)) {
						toastShowMessage("请选择片区");
						return;
					}
					String qxbm = data_qx.get(
							spinner_qx.getSelectedItemPosition()).get("id");
					if ("".equals(qxbm)) {
						toastShowMessage("请选择区县");
						return;
					}
					String jdrybm = data_ry.get(
							spinner_jdry.getSelectedItemPosition()).get("id");
					if ("".equals(jdrybm)) {
						toastShowMessage("请选择接单人员");
						return;
					}
					dialogShowMessage_P("是否确定转派？",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface face,
								int paramAnonymous2Int) {
							showProgressDialog();
							Config.getExecutorService().execute(new Runnable() {

								@Override
								public void run() {
									type = "1";
									getWebService("submit");
								}
							});
						}
					});
					break;
				case R.id.confirm:
					
					dialogShowLBZ(new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface face, int paramAnonymous2Int) {
							showProgressDialog();
							Config.getExecutorService().execute(new Runnable() {
			
								@Override
								public void run() {
									type = "2";
									getWebService("submit");
								}
							});
						}
					});
					
					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);

		spinner_pq.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				pqid = data_pq.get(position).get("id");
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("getqx");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner_qx.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				qxid = data_qx.get(position).get("id");
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("getry");
					}
				});
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner_jdry.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				tv_phone.setText(data_ry.get(position).get("lxdh"));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	protected void getWebService(String s) {
		if (s.equals("getpq")) {
			try {
				data_pq = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_pq.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_ZCZP_PQ", DataCache.getinition().getUserId()+"*"+khbm,
						"uf_json_getdata", this);
				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("pqbm"));
						item.put("name", temp.getString("pqmc"));
						data_pq.add(item);
					}
				}
				Message msg = new Message();
				msg.what = Constant.NUM_6;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if (s.equals("getqx")) {
			try {
				data_qx = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				data_qx.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_ZCZP_QX", pqid+"*"+khbm, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("qxbm"));
						item.put("name", temp.getString("dqmc"));
						data_qx.add(item);
					}
				}
				Message msg = new Message();
				msg.what = Constant.NUM_7;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if (s.equals("getry")) {
			try {
				data_ry = new ArrayList<Map<String, String>>();
				Map<String, String> item = new HashMap<String, String>();
				item.put("id", "");
				item.put("name", "");
				item.put("lxdh", "");
				data_ry.add(item);
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_ZCZP_RY", qxid, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						item = new HashMap<String, String>();
						item.put("id", temp.getString("userid"));
						item.put("name", temp.getString("xm"));
						item.put("lxdh", temp.getString("lxdh"));
						data_ry.add(item);
					}

				}
				Message msg = new Message();
				msg.what = Constant.NUM_8;
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}

		if (s.equals("submit")) {// 提交
			try {
				if ("1".equals(type)) {
					String typeStr = "zzzp";
					message = "转派成功";
					String str = zbh
							+ "*PAM*"
							+ data_ry.get(
									spinner_jdry.getSelectedItemPosition())
									.get("id") + "*PAM*"
							+ DataCache.getinition().getUserId();
					JSONObject json = this.callWebserviceImp.getWebServerInfo(
							"c#_PAD_ESP_ZZZP_WEB", str, typeStr, typeStr,
							"uf_json_setdata2", this);
					flag = json.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						Message msg = new Message();
						msg.what = Constant.SUCCESS;
						handler.sendMessage(msg);
					} else {
						flag = json.getString("msg");
						Message msg = new Message();
						msg.what = Constant.FAIL;
						handler.sendMessage(msg);
					}
				} else {
					message = "接单成功";
					String czrz3 = "'0'||chr(42)||'"
							+ DataCache.getinition().getUserId()
							+ "'||chr(42)||'"
							+ new DataUtil()
									.toDataString("yyyy-MM-dd HH:mm:ss")
							+ "'";
					Log.e("dd", czrz3);
					String djzt = "3";
					String sql = "";
					sql = "update shgl_ywgl_fwbgszb set djzt=" + djzt
							+ ",clfs=1,slsj=sysdate,czrz3=" + czrz3
							+ " where zbh='" + zbh + "'";
					JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
							"_RZ", sql, DataCache.getinition()
									.getUserId(), "uf_json_setdata",
							this);
					flag = jsonObject.getString("flag");

					if (Integer.parseInt(flag) > 0) {
						Message msg = new Message();
						msg.what = Constant.SUCCESS;
						handler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = Constant.FAIL;// 失败
						handler.sendMessage(msg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.FAIL:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;
			case Constant.SUCCESS:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P(message,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			case Constant.NETWORK_ERROR:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case 4:
				dialogShowMessage_P("工单已接收！",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			case Constant.NUM_6:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				SimpleAdapter adapter1 = new SimpleAdapter(ZzzpKdg.this,
						data_pq, R.layout.spinner_item, from, to);
				spinner_pq.setAdapter(adapter1);
				break;
			case Constant.NUM_7:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				SimpleAdapter adapter2 = new SimpleAdapter(ZzzpKdg.this,
						data_qx, R.layout.spinner_item, from, to);
				spinner_qx.setAdapter(adapter2);
				break;
			case Constant.NUM_8:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				SimpleAdapter adapter3 = new SimpleAdapter(ZzzpKdg.this,
						data_ry, R.layout.spinner_item, from, to);
				spinner_jdry.setAdapter(adapter3);
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("querygzlx");

					}
				});
				break;
			}
		}
	};

}
