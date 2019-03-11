package com.single.activity.w;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.single.activity.FrameActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.common.Constant;
import com.single.gtdzpt.R;
import com.single.utils.Config;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * 片区工单查询
 * 
 * @author zdkj
 *
 */
public class Pqzgjk extends FrameActivity {

	private Button confirm, cancel;
	private String flag, zbh, type = "1", message, csyybm, wwgcsyy, sfwzzm;
	private ImageView iv_telphone,iv_telphone_kh;
	private ArrayList<Map<String, String>> data_lszzjl, data_csyy;
	private ArrayList<String> list_csyy, list_csnr;
	private Spinner spinner_csyy, spinner_csnr;
	private EditText et_csnrbz, et_zzjlbz;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_w_pqzgjk);
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
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		iv_telphone_kh = (ImageView) findViewById(R.id.iv_telphone_kh);
		spinner_csyy = (Spinner) findViewById(R.id.spinner_csyy);
		spinner_csnr = (Spinner) findViewById(R.id.spinner_csnr);
		et_zzjlbz = (EditText) findViewById(R.id.et_zzjlbz);
		et_csnrbz = (EditText) findViewById(R.id.et_csnrbz);
	}

	@Override
	protected void initView() {

		title.setText("片区工单查询");

		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		((TextView) findViewById(R.id.tv_1)).setText(zbh);
		((TextView) findViewById(R.id.tv_2)).setText(itemmap.get("sx")
				.toString());
		((TextView) findViewById(R.id.tv_3)).setText(itemmap.get("qy")
				.toString());
		((TextView) findViewById(R.id.tv_4)).setText(itemmap.get("xqmc")
				.toString());
		((TextView) findViewById(R.id.tv_5)).setText(itemmap.get("xxdz")
				.toString());
		((TextView) findViewById(R.id.tv_6)).setText(itemmap.get("gzxx")
				.toString());
		((TextView) findViewById(R.id.tv_7)).setText(itemmap.get("ywlx")
				.toString());
		((TextView) findViewById(R.id.tv_8)).setText(itemmap.get("jbf")
				.toString());
		((TextView) findViewById(R.id.tv_9)).setText(itemmap.get("jbfdh")
				.toString());
		((TextView) findViewById(R.id.tv_10)).setText(itemmap.get("sfcs")
				.toString());
		((TextView) findViewById(R.id.tv_11)).setText(itemmap.get("csyy")
				.toString());
		((TextView) findViewById(R.id.tv_12)).setText(itemmap.get("csnr")
				.toString());
		((TextView) findViewById(R.id.tv_13)).setText(itemmap.get("bzsj")
				.toString());
		((TextView) findViewById(R.id.tv_14)).setText(itemmap.get("ddsj")
				.toString());
		((TextView) findViewById(R.id.tv_15)).setText(itemmap.get("scsj")
				.toString());
		((TextView) findViewById(R.id.tv_16)).setText(itemmap.get("bzrlxdh")
				.toString());
		((TextView) findViewById(R.id.tv_jjcd)).setText(itemmap.get("jjcd")
				.toString());
		
		iv_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(itemmap.get("jbfdh").toString(),zbh);
			}
		});
		
		iv_telphone_kh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(itemmap.get("bzrlxdh").toString(),zbh);
			}
		});

		sfwzzm = itemmap.get("sfwzzm").toString();
		csyybm = itemmap.get("csyybm").toString();
		wwgcsyy = itemmap.get("wwgcsyy").toString();
		if ("1".equals(sfwzzm)) {
			findViewById(R.id.ll_csyy).setVisibility(View.VISIBLE);
			findViewById(R.id.ll_csyy_content).setVisibility(View.VISIBLE);
		} else {
			findViewById(R.id.ll_csyy).setVisibility(View.GONE);
			findViewById(R.id.ll_csyy_content).setVisibility(View.GONE);
		}
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
					finish();
					break;
				case R.id.cancel:
					onBackPressed();
					finish();
					break;
				case R.id.confirm:
					if ("其他".equals((String) spinner_csnr.getSelectedItem())) {
						if (!isNotNull(et_csnrbz)) {
							dialogShowMessage_P("请录入超时内容备注", null);
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
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);

		spinner_csyy.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				list_csnr = new ArrayList<String>();
				if(position == 0){
					list_csnr.add("");
				}
				if (position == 1) {// 其他原因
					list_csnr.add("自然灾害极端天气");
					list_csnr.add("不可抗因素，快递无法投递及无法送达、公共交通不可达或中断等");
					list_csnr.add("超范围服务非合同约定的服务内容");
					list_csnr.add("其他");
				}
				if (position == 2) {// 客户原因
					list_csnr.add("网点现场无电/无网络");
					list_csnr.add("网点现场正在施工");
					list_csnr.add("客户要求更改服务时间");
					list_csnr.add("客户要求终止服务");
					list_csnr.add("系统后台无法绑定");
					list_csnr.add("非软件平台人员工作时间");
					list_csnr.add("甲方无可用备件");
					list_csnr.add("甲方调拨备件");
					list_csnr.add("甲方发货型号不匹配");
					list_csnr.add("客户资料不符无法开通");
					list_csnr.add("客户资料不齐全");
					list_csnr.add("客户网点地址与工单信息不符");
					list_csnr.add("其他");
				}
				if (position == 3) {// 正德原因
					list_csnr.add("人员未在规定时间内上门");
					list_csnr.add("跨区人员调度导致上门延迟");
					list_csnr.add("当地无可用备件需总部调拨备件");
					list_csnr.add("备件型号与现场设备不匹配");
					list_csnr.add("总部无适用备件需采购");
					list_csnr.add("其他");
				}
				Message msg = new Message();
				msg.what = Constant.NUM_7;
				handler.sendMessage(msg);

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		spinner_csnr.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String csnr = (String) spinner_csnr.getSelectedItem();
				if ("其他".equals(csnr)) {
					findViewById(R.id.ll_csyybz).setVisibility(View.VISIBLE);
					findViewById(R.id.ll_csyybz_content).setVisibility(
							View.VISIBLE);
				} else {
					findViewById(R.id.ll_csyybz).setVisibility(View.GONE);
					findViewById(R.id.ll_csyybz_content).setVisibility(
							View.GONE);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	protected void getWebService(String s) {
		if ("query".equals(s)) {
			try {
				data_lszzjl = new ArrayList<Map<String, String>>();
				data_csyy = new ArrayList<Map<String, String>>();
				list_csyy = new ArrayList<String>();
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_KDG_JKJL", zbh, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, String> item = new HashMap<String, String>();
						item.put("zbh", temp.getString("zbh"));
						item.put("sj", temp.getString("sj"));
						item.put("ry", temp.getString("ry"));
						item.put("bz", temp.getString("bz"));
						data_lszzjl.add(item);
					}

				}

				// JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
				// "_PAD_KDG_CSYYLR_CX", "", "uf_json_getdata", this);
				// flag = jsonObject.getString("flag");
				// if (Integer.parseInt(flag) > 0) {
				// JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				// for (int i = 0; i < jsonArray.length(); i++) {
				// JSONObject temp = jsonArray.getJSONObject(i);
				// Map<String, String> item = new HashMap<String, String>();
				// item.put("csyy", temp.getString("csyy"));
				// item.put("yymc", temp.getString("yymc"));
				// list_csyy.add(temp.getString("yymc"));
				// data_csyy.add(item);
				// }
				// }
				Map<String, String> item = new HashMap<String, String>();
				item.put("csyy", "");
				item.put("yymc", "");
				data_csyy.add(item);
				item = new HashMap<String, String>();
				item.put("csyy", "001");
				item.put("yymc", "其他原因");
				data_csyy.add(item);
				item = new HashMap<String, String>();
				item.put("csyy", "002");
				item.put("yymc", "客户原因");
				data_csyy.add(item);
				item = new HashMap<String, String>();
				item.put("csyy", "003");
				item.put("yymc", "正德原因");
				data_csyy.add(item);
				list_csyy.add("");
				list_csyy.add("其他原因");
				list_csyy.add("客户原因");
				list_csyy.add("正德原因");
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

		if (s.equals("submit")) {// 提交
			try {
				String typeStr = "zzzp";
				message = "保存成功";
				String str = "";
				if ("1".equals(sfwzzm)) { // 组长
					String csnr = (String) spinner_csnr.getSelectedItem();
					if ("其他".equals(csnr)) {
						csnr = et_csnrbz.getText().toString();
					}
					str = zbh
							+ "*PAM*"
							+ DataCache.getinition().getUserId()
							+ "*PAM*"
							+ ((Map<String, String>) data_csyy.get(spinner_csyy
									.getSelectedItemPosition())).get("csyy")
							+ "*PAM*" + wwgcsyy + "*PAM*" + csnr + "*PAM*"
							+ et_zzjlbz.getText().toString();
				} else {
					str = zbh + "*PAM*" + DataCache.getinition().getUserId()
							+ "*PAM*" + csyybm + "*PAM*" + "" + "*PAM*" + ""
							+ "*PAM*" + et_zzjlbz.getText().toString();
				}

				JSONObject json = this.callWebserviceImp.getWebServerInfo(
						"c#_PAD_ESP_CSYYLR_ZZJL", str, typeStr, typeStr,
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
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}
		}
	}

	private void loadLszzjl() {
		try {
			LinearLayout ll_lszzjl = (LinearLayout) findViewById(R.id.ll_lszzjl);
			for (int i = 0; i < data_lszzjl.size(); i++) {
				Map<String, String> map = data_lszzjl.get(i);
				View view = LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.listview_item_lszzjl, null);
				TextView tv_1 = (TextView) view.findViewById(R.id.tv_1);
				TextView tv_2 = (TextView) view.findViewById(R.id.tv_2);
				TextView tv_3 = (TextView) view.findViewById(R.id.tv_3);
				tv_1.setText(map.get("sj"));
				tv_2.setText(map.get("ry"));
				tv_3.setText(map.get("bz"));
				ll_lszzjl.addView(view);
			}
		} catch (Exception e) {
			e.printStackTrace();
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
			case Constant.NUM_6:
				ArrayAdapter adapter1 = new ArrayAdapter<String>(Pqzgjk.this,
						android.R.layout.simple_spinner_item, list_csyy);
				adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner_csyy.setAdapter(adapter1);
				loadLszzjl();
				break;
			case Constant.NUM_7:
				ArrayAdapter adapter2 = new ArrayAdapter<String>(Pqzgjk.this,
						android.R.layout.simple_spinner_item, list_csnr);
				adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner_csnr.setAdapter(adapter2);
				break;
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	};
}
