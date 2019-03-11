package com.single.activity.kdg;

import java.util.Map;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.single.activity.FrameActivity;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.common.Constant;
import com.single.gtdzpt.R;
import com.single.utils.Config;
import com.single.utils.DataUtil;

/**
 * 快递柜-接单响应
 * 
 * @author zdkj
 *
 */
public class JdxyKdg extends FrameActivity {

	private Button confirm, cancel;
	private String flag, zbh, type = "1", message;
	private TextView tv_fwgcs;
	private ImageView iv_telphone,iv_telphone_bzr;
	private boolean ist16;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_jdxy);
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
		tv_fwgcs = (TextView) findViewById(R.id.tv_fwgcs);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		iv_telphone_bzr = (ImageView) findViewById(R.id.iv_telphone_bzr);
		confirm.setText("接单");
		cancel.setText("拒绝");

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		ist16 = "00000020".equals(itemmap.get("fbf").toString());
		((TextView) findViewById(R.id.tv_zbh)).setText(itemmap.get("zbh")
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
		((TextView) findViewById(R.id.tv_bzr)).setText(itemmap.get("khlxr")
				.toString());
		((TextView) findViewById(R.id.tv_jjcd)).setText(itemmap.get("jjcd")
				.toString());
		((TextView) findViewById(R.id.tv_jsdw)).setText("001".equals(itemmap.get(
				"cx").toString()) ? "城区" : "乡镇");// 结算位置
		((TextView) findViewById(R.id.tv_fwgcs)).setText(itemmap.get("zxsxm")
				.toString());
		iv_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(itemmap.get("zxsdh").toString(),zbh);
			}
		});
		
		iv_telphone_bzr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(itemmap.get("lxdh").toString(),zbh);
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
					dialogShowMessage("是否拒绝接单？",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									showProgressDialog();
									Config.getExecutorService().execute(
											new Runnable() {

												@Override
												public void run() {
													type = "2";
													getWebService("submit");
												}
											});
								}
							}, null);
					break;
				case R.id.confirm:
					dialogShowLBZ(new DialogInterface.OnClickListener() {
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
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);
	}

	@Override
	protected void getWebService(String s) {

		if (s.equals("submit")) {// 提交
			try {
				String czrz3 = "'0'||chr(42)||'" + DataCache.getinition().getUserId() + "'||chr(42)||'" + new DataUtil() .toDataString("yyyy-MM-dd HH:mm:ss") + "'";
				if("1".equals(type)){
					
					String sql = "update shgl_ywgl_fwbgszb set djzt="
							+ 3 + ",clfs=1,slsj=sysdate,czrz3=" + czrz3
							+ " where zbh='" + zbh + "'";
					JSONObject jsonObject = callWebserviceImp.getWebServerInfo("_RZ",
							sql, DataCache.getinition().getUserId(),
							"uf_json_setdata", this);
					flag = jsonObject.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						message = "提交成功";
						Message msg = new Message();
						msg.what = Constant.SUCCESS;
						handler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = Constant.FAIL;
						handler.sendMessage(msg);
					}
				}else{
					String str = zbh+"*PAM*0"+DataCache.getinition().getUserId()+new DataUtil() .toDataString("yyyy-MM-dd HH:mm:ss")+"*PAM*";
					JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
							"2#_PAD_ESP_GDJJ", str, DataCache.getinition().getUserId(),
							"uf_json_setdata2", getApplicationContext());
					flag = jsonObject.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						message = "拒绝成功";
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
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;
			case Constant.SUCCESS:
				dialogShowMessage_P(message,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
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