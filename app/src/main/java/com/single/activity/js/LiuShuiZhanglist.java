package com.single.activity.js;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.single.activity.FrameActivity;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.common.Constant;
import com.single.utils.Config;
import com.single.gtdzpt.R;

/**
 * 流水列表
 * 
 * @author
 */
@SuppressLint("HandlerLeak")
public class LiuShuiZhanglist extends FrameActivity {

	private Button confirm, cancel;
	private String zbh,msgStr;
	private RadioGroup rg_1;
	private EditText et_yyfk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_lsz);
		initVariable();
		initView();
		initListeners();

	}

	@Override
	protected void initVariable() {
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("提交");

		rg_1 = (RadioGroup) findViewById(R.id.rg_1);
		et_yyfk = (EditText) findViewById(R.id.et_yyfk);
	}

	@Override
	protected void initView() {
		title.setText(DataCache.getinition().getTitle());
		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		((TextView) findViewById(R.id.tv_1)).setText(zbh);
		((TextView) findViewById(R.id.tv_2)).setText(itemmap.get("ywlx")
				.toString());
		((TextView) findViewById(R.id.tv_3)).setText(itemmap.get("bzr")
				.toString());
		((TextView) findViewById(R.id.tv_4)).setText(itemmap.get("gzxx")
				.toString());
		((TextView) findViewById(R.id.tv_5)).setText(itemmap.get("bzsj")
				.toString());
		((TextView) findViewById(R.id.tv_6)).setText(itemmap.get("wcsj")
				.toString());
		((TextView) findViewById(R.id.tv_7)).setText(itemmap.get("pgdx")
				.toString());
		((TextView) findViewById(R.id.tv_8)).setText(itemmap.get("dqfydj")
				.toString());
		((TextView) findViewById(R.id.tv_9)).setText(itemmap.get("jg")
				.toString() + "（元）");
		((TextView) findViewById(R.id.tv_10)).setText(itemmap.get("fy3")
				.toString() + "（元）");
		((TextView) findViewById(R.id.tv_11)).setText(itemmap.get("fy4")
				.toString() + "（元）");
		((TextView) findViewById(R.id.tv_12)).setText(itemmap.get("fy5")
				.toString() + "（元）");
		((TextView) findViewById(R.id.tv_13)).setText(itemmap.get("fy2")
				.toString() + "（元）");
		((TextView) findViewById(R.id.tv_14)).setText(itemmap.get("tsfysm")
				.toString());
		((TextView) findViewById(R.id.tv_15)).setText(itemmap.get("t16_jg")
				.toString() + "（元）");
		((TextView) findViewById(R.id.tv_16)).setText(itemmap.get("zj")
				.toString() + "（元）");
		((TextView) findViewById(R.id.tv_17)).setText(itemmap.get("fy6")
				.toString() + "（元）");
		if ("0".equals(itemmap.get("t16_jg").toString())) {
			findViewById(R.id.ll_jl).setVisibility(View.GONE);
			findViewById(R.id.ll_jl_line).setVisibility(View.GONE);
		}
	}

	@Override
	protected void initListeners() {

		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					finish();
					break;
				case R.id.cancel:
					onBackPressed();
					finish();
					break;
				case R.id.confirm:
					if (rg_1.getCheckedRadioButtonId() == R.id.rb_1_s) {
						if (!isNotNull(et_yyfk)) {
							dialogShowMessage_P("请录入异议原因", null);
							return;
						}
					}
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {
							getWebService("submit");

						}
					});
					break;

				}
			}
		};
		topBack.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);
	}

	@Override
	protected void getWebService(String s) {
		if ("submit".equals(s)) {
			try {
				int sfyy = rg_1.getCheckedRadioButtonId() == R.id.rb_1_s ? 1
						: 2;
				String str = zbh + "*PAM*" + sfyy + "*PAM*"
						+ et_yyfk.getText().toString();
				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_JSGL_TSFY_FHSQ", str, "ysk", "ysk",
						"uf_json_setdata2", this);
				String flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
					msgStr = json.getString("msg");
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

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;

			case Constant.SUCCESS:
				dialogShowMessage_P("提交成功",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;

			case Constant.FAIL:
				dialogShowMessage_P("提交失败，"+msgStr, null);
				break;

			}
			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};

}
