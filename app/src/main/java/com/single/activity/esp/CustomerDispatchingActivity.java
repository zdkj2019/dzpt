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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.single.activity.FrameActivity;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.gtdzpt.R;
import com.single.utils.Config;

/**
 * 厂商派工
 * 
 * @author wlj
 */
@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class CustomerDispatchingActivity extends FrameActivity {

	private TextView et_bzr,et_lxdh,et_gzxx;
	private TextView tv_wxaz,tv_pgdh,tv_servicepeople;
	private Button confirm,cancel;
	private List<Map<String, String>> data;
	private String pgdx;
//	private Integer index;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_customerdispatching);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("queryUser");
			}
		});
	}

	@Override
	protected void initVariable() {
		
//		index = 0;
		et_bzr = (TextView) findViewById(R.id.et_bzr);
		et_lxdh = (TextView) findViewById(R.id.et_lxdh);
		tv_wxaz = (TextView) findViewById(R.id.tv_wxaz);
		tv_pgdh = (TextView) findViewById(R.id.tv_pgdh);
		et_gzxx = (TextView) findViewById(R.id.et_gzxx);
		tv_servicepeople = (TextView) findViewById(R.id.tv_servicepeople);
		
		confirm = (Button) findViewById(R.id.include_botto).findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(R.id.cancel);
		
		data = new ArrayList<Map<String, String>>();
		
	}

	@Override
	protected void initView() {
		
		title.setText(DataCache.getinition().getTitle());
		
		Map<String, String> itemmap = ServiceReportCache.getData().get(
				ServiceReportCache.getIndex());
		int wx = Integer.parseInt(itemmap.get("kzzd5"))-1;
		if(wx != 0 && wx != 1 && wx != 2 && wx != 3){wx = 3;}
		tv_pgdh.setText(itemmap.get("oddnumber"));
		et_bzr.setText(itemmap.get("faultuser"));
		et_lxdh.setText(itemmap.get("usertel"));
		tv_wxaz.setText(wxaz.get(wx));
		et_gzxx.setText(itemmap.get("gzxx"));

		pgdx = itemmap.get("dispatchinguser");
		tv_servicepeople.setText(pgdx);// 显示派公对象
		tv_servicepeople.setTag(itemmap.get("pgdx"));// 派公对象编码
		
		//khbm,ds,kdzh,cfsj,khxxdz
		((TextView) findViewById(R.id.tv_suxq)).setText(itemmap.get("khbm"));
		((TextView) findViewById(R.id.tv_supq)).setText(itemmap.get("ds"));
		((TextView) findViewById(R.id.tv_kdzh)).setText(itemmap.get("kdzh"));
		((TextView) findViewById(R.id.tv_yuyuetime)).setText(itemmap.get("cfsj"));
		((TextView) findViewById(R.id.tv_xxdz)).setText(itemmap.get("khxxdz"));
		
	}

	@Override
	protected void initListeners() {
		//
		OnClickListener backonClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		
		tv_servicepeople.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				defined2(tv_servicepeople, "派工对象", data, "员工编码", "姓名");
			}
		});
		
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (isNotNull(et_bzr) && isNotNull(et_lxdh)
						&& isNotNull(tv_wxaz)&& isNotNull(tv_servicepeople)) {
					dialogShowMessage("确认提交 ？",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									showProgressDialog();
									Config.getExecutorService().execute(
											new Runnable() {

												@Override
												public void run() {

													getWebService("submit");
												}
											});

								}
							}, null);
				} else {
					dialogShowMessage_P("请填写完整", null);
				}
			}
		});
		
	}

	String flag;

	@Override
	protected void getWebService(String s) {

		if ("queryUser".equals(s)) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_RY", DataCache.getinition().getUserId(),
						"uf_json_getdata",this);
				Log.e("_PAD_RY", jsonObject.toString());
				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {
					
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");

					for (int i = 0; i < jsonArray.length(); i++) {
						Map<String, String> item = new HashMap<String, String>();

						JSONObject temp = jsonArray.getJSONObject(i);
						String id = temp.getString("ygbh");
						String name = temp.getString("xm");
						item.put("id", id);
						item.put("name", name);
						data.add(item);
					}

					Message msg = new Message();
					msg.what = 2;// 成功
					handler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = -2;// 失败
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}

		}
		
		if (s.equals("submit")) {// 提交
			try {
				String sql = "update shgl_ywgl_fwbgszb set " + "djzt= -1,"
				+"pgdx='"+ tv_servicepeople.getTag()
				+ "',slsj=sysdate where zbh='" + tv_pgdh.getText()
				+ "'";
				Log.e("CustomerDispatchingActivity", sql.toString());

				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_RZ", sql, DataCache.getinition().getUserId(),
						"uf_json_setdata",this);
				flag = jsonObject.getString("flag");

				if (Integer.parseInt(flag) > 0) {
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
				Message localMessage1 = new Message();
				localMessage1.what = 0;// 网络连接出错，你检查你的网络设置
				handler.sendMessage(localMessage1);
			}

		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case -2:
				dialogShowMessage_P("获取数据失败，请重新获取",  new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
						skipActivity2(CustomerDispatchinglist.class);
					}
				});
				break;
			case -1:
				dialogShowMessage_P("网络可能有问题，请重试！！",  new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
						skipActivity2(CustomerDispatchinglist.class);
					}
				});
				break;
			case 1:
				dialogShowMessage_P("派工成功",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});

				break;
			case 0:
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;

			case 2:
				nameToId();
				break;
			}
			progressDialog.dismiss();
		}
	};
	/**
	 * 根据 派工对象 得到 派工对象 的id
	 */
	private void nameToId() {
		pgdx = tv_servicepeople.getText().toString();
		if (pgdx != null && !"".equals(pgdx)) {
			for (int i = 0; i < data.size(); i++) {
				if (pgdx.equals(data.get(i).get("name"))) {
				
					tv_servicepeople.setTag(data.get(i).get("id"));
					break;
				}
			}
		}

	}
	@Override
	public void onBackPressed() {
		skipActivity2(MainActivity.class);
	}

}
