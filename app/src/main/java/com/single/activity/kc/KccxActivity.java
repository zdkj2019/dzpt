package com.single.activity.kc;

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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.single.activity.FrameActivity;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.utils.Config;
import com.single.gtdzpt.R;

/**
 * 当前库存查询
 * 
 * @author
 */
@SuppressLint("HandlerLeak")
public class KccxActivity extends FrameActivity {

	private String flag;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> data;
	private String[] from;
	private int[] to;

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
		data = new ArrayList<Map<String, Object>>();

		from = new String[] { "textView1", "faultuser", "zbh","sl"};
		to = new int[] { R.id.textView1, R.id.faultuser, R.id.zbh, R.id.sl};

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

					Intent intent = new Intent(KccxActivity.this,
							KccxShowActivity.class);
					startActivity(intent);
				}

			}
		});
	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s) && !backboolean) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_HPKF_DQBGZ", DataCache.getinition().getUserId(),
						"uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						item.put("num", "");
						item.put("textView1", getListItemIcon(i));
						item.put("sqjc", temp.getString("sqjc"));// 上期结存
						item.put("jgmc_1", temp.getString("jcsj_sczzjg_jgmc_1"));// 仓位名称
						item.put("dqkc", temp.getString("dqkc"));// 当前库存
						item.put("hpmc", temp.getString("hpgl_jcsj_hpxxb_hpmc"));// 货品名称
						item.put("jgmc", temp.getString("jcsj_sczzjg_jgmc"));// 仓位名称
						item.put("jldw", temp.getString("hpgl_jcsj_hpxxb_jldw"));// 单位
						
						item.put("faultuser",temp.getString("jcsj_sczzjg_jgmc_1"));
						item.put("zbh",temp.getString("hpgl_jcsj_hpxxb_hpmc"));
						item.put("sl",temp.getString("dqkc")+"（"+temp.getString("hpgl_jcsj_hpxxb_jldw")+"）");
						
						data.add(item);
					}
					ServiceReportCache.setObjectdata(data);
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
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("currType", 4);
		startActivity(intent);
		finish();
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
				adapter = new SimpleAdapter(KccxActivity.this,
						ServiceReportCache.getObjectdata(),
						R.layout.listview_item_kcxx,
						from, to);
				listView.setAdapter(adapter);
				break;

			case FAIL:
				dialogShowMessage_P("你查询的时间段内，没有数据",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								onBackPressed();
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
