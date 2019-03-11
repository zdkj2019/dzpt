package com.single.activity.esp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.single.gtdzpt.R;
import com.single.activity.FrameActivity;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.utils.Config;

/**
 * 考核数据分析
 * 
 * @author cheng
 */
@SuppressLint("HandlerLeak")
public class KHchooseActivity extends FrameActivity {

	private String flag, time;

	private List<Map<String, String>> data2;
	private String[] from;
	private int[] to;
	private SimpleAdapter adapter;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// appendMainBody(R.layout.listview_item);
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
		time = getIntent().getStringExtra("time");
		// System.out.println("timetimetimetimetime:"+time);
		// data = new ArrayList<List<String>>();
		data2 = new ArrayList<Map<String, String>>();
		listView = (ListView) findViewById(R.id.listView);
		from = new String[] { "khxl", "pgbm", "wcl", "clz", "wg", "wcs", "cs" };
		to = new int[] { R.id.text_khxl, R.id.text_pgbm, R.id.weichuli,
				R.id.chulizhong, R.id.wangong, R.id.chaoshi, R.id.weichaoshi };

		// tablelayout = (TableLayout) findViewById(R.id.tablelayout);
	}

	@Override
	protected void initView() {
		title.setText(DataCache.getinition().getTitle());

	}

	@Override
	protected void initListeners() {

		// listView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int arg2,
		// long id) {
		// ServiceReportCache.setIndex(arg2);
		// if (arg2 >= 0) {
		// Intent intent = new Intent(getApplicationContext(),
		// KHmingxiActivity.class);
		//
		// startActivity(intent);
		// finish();
		// }
		//
		// }
		// });

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

	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s) && !backboolean) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_WSQ_SHGL_JSCJ_PGFWDJZT", time + "*"
								+ DataCache.getinition().getUserId() + "*"
								+ DataCache.getinition().getUserId(),
						"uf_json_getdata",this);

				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");

				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						// List<String> item = new ArrayList<String>();

						/*
						 * "wg":"1.000000000000000000", "khxl":"020101",
						 * "khdl":"02", "clz":"0", "wcl":"3.000000000000000000",
						 * "pgbm":"10101", "khzl":"0201", "wcs":"0", "cs":"0"
						 * },{ "wg":"0", "khxl":"020101", "khdl":"02",
						 * "clz":"0", "wcl":"1.000000000000000000",
						 * "pgbm":"10102", "khzl":"0201", "wcs":"0",
						 * "cs":"0"}],"flag":"2"}
						 */
						Map<String, String> item2 = new HashMap<String, String>();
						item2.put("khxl", temp.getString("khxl"));// 机构1
						item2.put("pgbm", temp.getString("pgbm"));// 维护厂商2
						item2.put("wcl", toInt(temp.getString("wcl")));// 未处理3
						item2.put("clz", toInt(temp.getString("clz")));// 处理中4
						item2.put("wg", toInt(temp.getString("wg")));// 完工5
						item2.put("wcs", toInt(temp.getString("wcs")));// 未超时6
						item2.put("cs", toInt(temp.getString("cs")));// 超时7

						data2.add(item2);

						// //这里顺序不能乱
						// item.add(temp.getString("khxl"));// 机构1
						// item.add(temp.getString("pgbm"));// 维护厂商2
						// item.add(toInt(temp.getString("wcl")));// 未处理3
						// item.add(toInt(temp.getString("clz")));// 处理中4
						// item.add(toInt(temp.getString("wg")));// 完工5
						// item.add(toInt(temp.getString("wcs")));// 未超时6
						// item.add(toInt(temp.getString("cs")));// 超时7
						//
						// data.add(item);

					}
					ServiceReportCache.setData(data2);
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

	private String toInt(String s) {

		if (!s.equals(null) && !"".equals(s)) {
			String[] strings = s.split("\\.");
			return strings[0];
		}
		return null;
	}

	TextView tabtext;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;

			case SUCCESSFUL:

				// for (int i = 0; i < data.size(); i++) {
				// List<String> list = data.get(i);
				// TableRow tableRow = new TableRow(getApplicationContext());
				// for (int j = 0; j < list.size(); j++) {
				// LinearLayout d = new LinearLayout(getApplicationContext());
				// TableRow.LayoutParams lp = new
				// TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,
				// LayoutParams.WRAP_CONTENT);
				// lp.setMargins(1,1,1,1);
				// tabtext = new TextView(getApplicationContext());
				// tabtext.setLayoutParams(lp);
				// tabtext.setBackgroundColor(Color.WHITE);
				// tabtext.setText(list.get(j));
				// tableRow.addView(tabtext);
				// }
				// tablelayout.addView(tableRow);
				// }

				adapter = new SimpleAdapter(getApplicationContext(),
						ServiceReportCache.getData(), R.layout.istview_item,
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

	@Override
	public void onBackPressed() {
		skipActivity2(MainActivity.class);
	}

}
