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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

/**
 * 信息接受
 * 
 * @author wlj
 */
@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class InformationReceivingActivity extends FrameActivity {

	private TextView tv_pgdh, tv_bzr, tv_gzxx;
	private Button confirm, cancel;
	private ImageView iv_telphone;
	private String tel, zbh;
	private boolean ist16 = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_informationreceiving);
		initVariable();
		initView();
		initListeners();

	}

	@Override
	protected void initVariable() {

		tv_pgdh = (TextView) findViewById(R.id.tv_pgdh);
		tv_bzr = (TextView) findViewById(R.id.tv_bzr);
		tv_gzxx = (TextView) findViewById(R.id.tv_gzxx);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("接单");
		cancel.setText("拒绝");

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

		Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(
				ServiceReportCache.getIndex());

		ist16 = "00000020".equals(itemmap.get("fbf").toString());
		zbh = itemmap.get("zbh").toString();
		((TextView) findViewById(R.id.tv_pgdh)).setText(itemmap.get("zbh")
				.toString());
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
		((TextView) findViewById(R.id.tv_xxdz)).setText(itemmap.get("xxdz")
				.toString());
		((TextView) findViewById(R.id.tv_bz)).setText(itemmap.get("bz")
				.toString());
		((TextView) findViewById(R.id.tv_gzxx)).setText(itemmap.get("gzxx")
				.toString());
		((TextView) findViewById(R.id.tv_bzsj)).setText(itemmap.get("bzsj")
				.toString());
		((TextView) findViewById(R.id.tv_ywlx)).setText(itemmap.get("ywlx")
				.toString());
		((TextView) findViewById(R.id.tv_jsdw)).setText("001".equals(itemmap
				.get("cx").toString()) ? "城区" : "乡镇");// 结算位置
		((TextView) findViewById(R.id.tv_fwgcs)).setText(itemmap.get("zxsxm")
				.toString());
		tel = itemmap.get("zxsdh").toString();
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
					Intent intent = new Intent(
							InformationReceivingActivity.this,
							InformationReceivingRefuseActivity.class);
					startActivity(intent);
					// dialogShowMessage("确认拒绝接单？",
					// new DialogInterface.OnClickListener() {
					// public void onClick(DialogInterface face,
					// int paramAnonymous2Int) {
					// refuseList();
					// }
					// },null);
					break;
				case R.id.iv_telphone:
					Call(tel, zbh);
					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		iv_telphone.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogShowLBZ(new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface face,
							int paramAnonymous2Int) {
						showProgressDialog();
						Config.getExecutorService().execute(new Runnable() {

							@Override
							public void run() {

								getWebService("submit");
							}
						});
					}
				});
			}
		});

	}

	String flag;

	@Override
	protected void getWebService(String s) {

		if (s.equals("submit")) {// 提交
			try {

				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_JDCF_CX", tv_pgdh.getText() + "",
						"uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray array = jsonObject.getJSONArray("tableA");
					jsonObject = (JSONObject) array.get(0);
					if ("2".equals(jsonObject.get("djzt"))) {
						String czrz3 = "'0'||chr(42)||'"
								+ DataCache.getinition().getUserId()
								+ "'||chr(42)||'"
								+ new DataUtil()
										.toDataString("yyyy-MM-dd HH:mm:ss")
								+ "'";
						Log.e("dd", czrz3);
						String djzt = "3";
						String sql = "update shgl_ywgl_fwbgszb set djzt="
								+ djzt + ",clfs=1,slsj=sysdate,czrz3=" + czrz3
								+ " where zbh='" + tv_pgdh.getText() + "'";
						jsonObject = callWebserviceImp.getWebServerInfo("_RZ",
								sql, DataCache.getinition().getUserId(),
								"uf_json_setdata", this);
						flag = jsonObject.getString("flag");

						if (Integer.parseInt(flag) > 0) {
							Message msg = new Message();
							msg.what = Constant.SUBMIT_SUCCESS;
							handler.sendMessage(msg);
						} else {
							Message msg = new Message();
							msg.what = Constant.FAIL;// 失败
							handler.sendMessage(msg);
						}
					} else {
						Message msg = new Message();
						msg.what = Constant.NUM_7;
						handler.sendMessage(msg);
					}

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

	private void refuseList() {
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					String czrz3 = "0*"
							+ DataCache.getinition().getUserId()
							+ "*"
							+ new DataUtil()
									.toDataString("yyyy-MM-dd HH:mm:ss") + "";
					String sql = "update shgl_ywgl_fwbgszb set djzt='1.5',chjdrz = '"
							+ czrz3
							+ "'"
							+ " where zbh='"
							+ tv_pgdh.getText()
							+ "'";
					JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
							"_RZ", sql, DataCache.getinition().getUserId(),
							"uf_json_setdata", getApplicationContext());
					flag = jsonObject.getString("flag");

					if (Integer.parseInt(flag) > 0) {
						Message msg = new Message();
						msg.what = Constant.NUM_6;
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
		});
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;
			case Constant.SUBMIT_SUCCESS:
				String message = "接收成功!";
				if (ist16) {
					message = message + "（特别提示：该工单若按T16标准完成，可获得额外奖励。）";
				}
				dialogShowMessage_P(message,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});

				break;
			case Constant.FAIL:
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;
			case Constant.NUM_6:
				dialogShowMessage_P("拒绝成功！",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			case Constant.NUM_7:
				dialogShowMessage_P("工单已接收！",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}

		}
	};

	@Override
	public void onBackPressed() {
		skipActivity2(MainActivity.class);
		finish();
	}

}
