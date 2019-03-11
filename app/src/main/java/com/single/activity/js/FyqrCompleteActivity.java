package com.single.activity.js;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.single.activity.FrameActivity;
import com.single.cache.DataCache;
import com.single.common.Constant;
import com.single.gtdzpt.R;
import com.single.utils.Config;

public class FyqrCompleteActivity extends FrameActivity {

	private Button confirm, cancel;
	private String flag, zbh;
	private JSONObject json = null;
	private boolean showjl = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_fyqrcomplete);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				getWebService("query");
			}
		});
	}

	@Override
	protected void initVariable() {
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);

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
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.confirm:
					onBackPressed();
					break;
				case R.id.bt_topback:
					onBackPressed();
					break;

				}
			}
		};

		topBack.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
	}

	@Override
	protected void getWebService(String s) {
		if ("query".equals(s)) {
			try {
				String userid = DataCache.getinition().getUserId();
				int sj = getIntent().getIntExtra("sj", 0);
				String sqlid = "_PAD_JSGL_YXYSFY2";
				String cs = "";
				if (sj == 1) {// 本月
					cs = 0 + "*" + 0 + "*" + userid + "*" + 0 + "*" + 0 + "*"
							+ userid;
				} else {// 上月
					cs = -1 + "*" + -1 + "*" + userid + "*" + -1 + "*" + -1
							+ "*" + userid;
				}
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						sqlid, cs, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");

				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					json = jsonArray.getJSONObject(0);

					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = Constant.FAIL;
					handler.sendMessage(msg);
				}
				
			} catch (Exception e) {
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
				dialogShowMessage_P("你查询的时间段内，没有数据",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								onBackPressed();
							}
						});
				break;
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;
			case Constant.SUCCESS:
				try {
					((TextView) findViewById(R.id.tv_1)).setText(json.get(
							"pgdx").toString());
					((TextView) findViewById(R.id.tv_2)).setText(json.get("jg")
							.toString() + "（元）");
					((TextView) findViewById(R.id.tv_3)).setText(json.get(
							"tsfy").toString()
							+ "（元）");
					((TextView) findViewById(R.id.tv_4)).setText(json.get(
							"t16_jg").toString()
							+ "（元）");
					((TextView) findViewById(R.id.tv_5)).setText(json.get("zj")
							.toString() + "（元）");
					if("0".equals(json.get("t16_jg").toString())){
						findViewById(R.id.ll_jl).setVisibility(View.GONE);
						findViewById(R.id.ll_jl_line).setVisibility(View.GONE);
					}
				} catch (Exception e) {

				}

				break;

			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};
}
