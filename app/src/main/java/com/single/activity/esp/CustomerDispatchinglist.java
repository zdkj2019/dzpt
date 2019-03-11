package com.single.activity.esp;

import java.util.ArrayList;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.single.activity.FrameActivity;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.utils.Config;
import com.single.gtdzpt.R;

/**
 * 厂商派工
 * @author wlj
 */
@SuppressLint("HandlerLeak")
public class CustomerDispatchinglist extends FrameActivity {

	private String flag;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, String>> data;
	private String[] from;
	private int[] to;
	private int query_djzt = -2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_dispatchinginformationreceiving);
		initVariable();
		initView();
		initListeners();
		if (!backboolean) {
			showProgressDialog();
		}
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("query");
			}
		});

	}

	@Override
	protected void initVariable() {

		listView = (ListView) findViewById(R.id.listView);
		data = new ArrayList<Map<String, String>>();

		from = new String[] { "khbm", "oddnumber", "timemy", "datemy" };
		to = new int[] { R.id.yytmy, R.id.pgdhmy, R.id.timemy, R.id.datemy };
		
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;

				}
			}
		};

		topBack.setOnClickListener(onClickListener);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int arg2,
					long id) {

				ServiceReportCache.setIndex(arg2);
				if (arg2 >= 0) {

					Intent intent = new Intent(
							CustomerDispatchinglist.this,
							CustomerDispatchingActivity.class);
					startActivity(intent);
					finish();
				}

			}
		});

	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s) && !backboolean) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SHGL_PGDCZ", "2000-01-01*3000-01-01*"
								+ query_djzt + "*"
								+ DataCache.getinition().getUserId() + "*"
								+ DataCache.getinition().getUserId(),
						"uf_json_getdata",this);
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
/**
 * 
 */
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
/**khbm,ds,kdzh,cfsj,khxxdz
 *"ccgl_wlsjb_khbm":"324医院家属楼",
 *"shgl_ywgl_fwbgszb_cfsj":"2013-12-18 11:55:10"
 *"shgl_ywgl_fwbgszb_kdzh":"30015855",
 *"shgl_ywgl_fwbgszb_khxxdz":"详细地址",
 *"main_jcsj_khjgb_ds":"观音桥"}]
 */
						String timeff = temp
								.getString("shgl_ywgl_fwbgszb_bzsj");
						timeff = timeff.substring(2);
						item.put("no", String.valueOf(i + 1));
						item.put("oddnumber",// 派工单号
								temp.getString("shgl_ywgl_fwbgszb_zbh"));
						item.put("faultuser",
								temp.getString("shgl_ywgl_fwbgszb_bzr"));
						item.put("usertel",
								temp.getString("shgl_ywgl_fwbgszb_bzrlxdh"));
						item.put("kzzd5",
								temp.getString("shgl_ywgl_fwbgszb_kzzd5"));
						item.put("gzxx",
								temp.getString("shgl_ywgl_fwbgszb_gzxx"));
						item.put("dispatchinguser",
								temp.getString("ccgl_ygb_xm"));
						item.put("pgdx",
								temp.getString("shgl_ywgl_fwbgszb_pgdx"));
						item.put("khbm",
								temp.getString("ccgl_wlsjb_khbm"));
						item.put("ds",
								temp.getString("main_jcsj_khjgb_ds"));
						item.put("kdzh",
								temp.getString("shgl_ywgl_fwbgszb_kdzh"));
						item.put("cfsj",
								temp.getString("shgl_ywgl_fwbgszb_cfsj"));
						item.put("khxxdz",
								temp.getString("shgl_ywgl_fwbgszb_khxxdz"));
						
						
//						item.put("customername",// 营业厅
//								temp.getString("ccgl_wlsjb_khbm"));
						
						
//						item.put("faulttime",// 时间
//								temp.getString("shgl_ywgl_fwbgszb_bzsj"));
//						item.put("khdh", temp.getString("ccgl_wlsjb_dh"));
//						item.put("dispatchingtime",
//								temp.getString("shgl_ywgl_fwbgszb_pgsj"));
//						item.put("state",
//								temp.getString("shgl_ywgl_fwbgszb_djzt"));
//						item.put("clfs",
//								temp.getString("shgl_ywgl_fwbgszb_clfs"));
//						item.put("clr", temp.getString("shgl_ywgl_fwbgszb_czy"));
//						item.put("sgdh",
//								temp.getString("shgl_ywgl_fwbgszb_sgdh"));
//						item.put("bz", temp.getString("shgl_ywgl_fwbgszb_bz"));
//						item.put("dz", temp.getString("ccgl_wlsjb_dz"));
//						item.put("pgbm", temp.getString("zzjgb_pz_pgbm"));
//						item.put("ydbm",
//								temp.getString("shgl_ywgl_fwbgssbb_ydbm"));
//						item.put("sbbm",
//								temp.getString("shgl_ywgl_fwbgssbb_sbbm"));
//						item.put("sblx",
//								temp.getString("main_jcsj_sblbb_sblbmc"));

						item.put("timemy",// 时间
								mdateformat(1, timeff));

						item.put("datemy",// 年月日
								mdateformat(0, timeff));
						data.add(item);
					}
					ServiceReportCache.setData(data);
					Message msg = new Message();
					msg.what = SUCCESSFUL;// 成功
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = FAIL;// 失败
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}

		} else {
			// 直接加载
			Message msg = new Message();
			msg.what = SUCCESSFUL;// 成功
			handler.sendMessage(msg);
		}

	}

	@Override
	public void onBackPressed() {
		skipActivity2(MainActivity.class);
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;

			case SUCCESSFUL:
				adapter = new SimpleAdapter(
						CustomerDispatchinglist.this,
						ServiceReportCache.getData(),
						R.layout.listview_dispatchinginformationreceiving_item,
						from, to);
				listView.setAdapter(adapter);
				break;

			case FAIL:
				dialogShowMessage_P("你查询的时间段内，没有派工单号",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								skipActivity2(MainActivity.class);
							}

						});
				break;

			}
			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};

}
