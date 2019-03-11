package com.single.activity.esp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.single.Parser.JSONObjectParser;
import com.single.activity.FrameActivity;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.gtdzpt.R;
import com.single.utils.Config;
import com.single.utils.DataUtil;

/**
 * 营业厅报障
 * 
 * @author cheng
 */
@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class YytReportedActivity_20141220 extends FrameActivity {

	private SharedPreferences spf;
	private Editor spfe;
	private CheckBox efb_fbrdx,efb_khdx;
	private EditText  efb_smsf,efb_smyq,efb_fbr,efb_fbrsj,
	efb_khxm,efb_khsj,efb_khdz,efb_bwnr,efb_bz;
	private TextView efb_sssqx,efb_dyxz,efb_khsspq,efb_khwd,efb_rzsblx,efb_yylxxm,efb_fbdw, efb_ywlx,
	eefb_fbdw, eefb_ywlx,eefb_rzsblx,eefb_yylxxm,eefb_khwd,eefb_khsspq,eefb_sssqx;
	private Button confirm, cancel;
	private JSONObjectParser jsonObjectParser;
	String flag;
	private ArrayList<Map<String, String>> data_fbdw; // 发包单位缓存数据
	private ArrayList<Map<String, String>> data_khsspq; // 所属片区缓存数据
	private ArrayList<Map<String, String>> data_fylx; // 费用类型缓存数据
	private ArrayList<Map<String, String>> data_khwd; // 所属网点缓存数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_yytreported);
		initVariable();
		initView();
		initListeners();
		if (!backboolean) {
			showProgressDialog();
		}
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("xq");
			}
		});

	}

	

	@Override
	protected void initVariable() {

		efb_fbdw = (TextView) this.findViewById(R.id.efb_fbdw);
		efb_ywlx = (TextView) this.findViewById(R.id.efb_ywlx);
		efb_rzsblx = (TextView) this.findViewById(R.id.efb_rzsblx);
		efb_yylxxm = (TextView) this.findViewById(R.id.efb_yylxxm);
		efb_smsf = (EditText) this.findViewById(R.id.efb_smsf);
		efb_smyq = (EditText) this.findViewById(R.id.efb_smyq);
		efb_fbr = (EditText) this.findViewById(R.id.efb_fbr);
		efb_fbrsj = (EditText) this.findViewById(R.id.efb_fbrsj);
		efb_fbrdx = (CheckBox) this.findViewById(R.id.efb_fbrdx);
		efb_khwd = (TextView) this.findViewById(R.id.efb_khwd);
		efb_khsspq = (TextView) this.findViewById(R.id.efb_khsspq);
		efb_sssqx = (TextView) this.findViewById(R.id.efb_sssqx);
		efb_dyxz = (TextView) this.findViewById(R.id.efb_dyxz);
		efb_khxm = (EditText) this.findViewById(R.id.efb_khxm);
		efb_khsj = (EditText) this.findViewById(R.id.efb_khsj);
		efb_khdz = (EditText) this.findViewById(R.id.efb_khdz);
		efb_khdx = (CheckBox) this.findViewById(R.id.efb_khdx);
		efb_bwnr = (EditText) this.findViewById(R.id.efb_bwnr);
		efb_bz = (EditText) this.findViewById(R.id.efb_bz);

		eefb_fbdw = (TextView) this.findViewById(R.id.eefb_fbdw);
		eefb_ywlx = (TextView) this.findViewById(R.id.eefb_ywlx);
		eefb_rzsblx = (TextView) this.findViewById(R.id.eefb_rzsblx);
		eefb_yylxxm = (TextView) this.findViewById(R.id.eefb_yylxxm);
		eefb_khwd = (TextView) this.findViewById(R.id.eefb_khwd);
		eefb_khsspq = (TextView) this.findViewById(R.id.eefb_khsspq);
		eefb_sssqx = (TextView) this.findViewById(R.id.eefb_sssqx);
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);

		data_fbdw = new ArrayList<Map<String, String>>();
		data_khsspq = new ArrayList<Map<String, String>>();
		data_fylx = new ArrayList<Map<String, String>>();
		data_khwd = new ArrayList<Map<String, String>>();
		jsonObjectParser = new JSONObjectParser(this);
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

		spf = getPreferences(MODE_PRIVATE);
		// 设置默认选择
		efb_fbr.setText(spf.getString("bzr", ""));
		efb_fbrsj.setText(spf.getString("lxdh", ""));

		spfe = spf.edit();

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

		efb_ywlx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				defined(efb_ywlx, "维修安装", wxaz);
			}
		});

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isNotNull(efb_fbdw) && isNotNull(efb_ywlx)
						&& isNotNull(efb_rzsblx) && isNotNull(efb_yylxxm)
						&& isNotNull(efb_fbr) && isNotNull(efb_fbrsj)&& isNotNull(efb_khwd)
						&& isNotNull(efb_khsspq)&& isNotNull(efb_khsj)
						&& isNotNull(efb_khdz)&& isNotNull(efb_bwnr)) {
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

		//所属片区
		efb_khsspq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(YytReportedActivity_20141220.this,
						ChooseXQ.class);
				intent.putExtra("data_khsspq", data_khsspq);
				startActivityForResult(intent, 11);
			}
		});

		//发包单位
		efb_fbdw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(YytReportedActivity_20141220.this,
						ChooseFBF.class);
				intent.putExtra("data_fbdw", data_fbdw);
				startActivityForResult(intent, 12);
			}
		});
		
		//设备认证类型
		efb_rzsblx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(YytReportedActivity_20141220.this,
						ChooseSblx.class);
				String fbfid = eefb_fbdw.getTag()==null?"":eefb_fbdw.getTag().toString();
				intent.putExtra("data_fbdw", data_fbdw);
				intent.putExtra("fbfid", fbfid);
				startActivityForResult(intent, 13);
			}
		});
		
		//费用类型
		efb_yylxxm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(YytReportedActivity_20141220.this,
						ChooseFylx.class);
				String fbfid = eefb_fbdw.getTag()==null?"":eefb_fbdw.getTag().toString();
				String ywlxid = (wxaz.indexOf(efb_ywlx.getText()) + 1)+"";
				String sblxid = eefb_rzsblx.getTag()==null?"":eefb_rzsblx.getTag().toString();
				intent.putExtra("data_fylx", data_fylx);
				intent.putExtra("fbfid", fbfid);
				intent.putExtra("ywlxid", ywlxid);
				intent.putExtra("sblxid", sblxid);
				startActivityForResult(intent, 14);
			}
		});
		
		//客户网点
		efb_khwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(YytReportedActivity_20141220.this,
						ChooseKhwd.class);
				String fbfid = eefb_fbdw.getTag()==null?"":eefb_fbdw.getTag().toString();
				String pqid = eefb_khsspq.getTag()==null?"":eefb_khsspq.getTag().toString();
				intent.putExtra("data_khwd", data_khwd);
				intent.putExtra("fbfid", fbfid);
				intent.putExtra("pqid", pqid);
				startActivityForResult(intent, 15);
			}
		});

//		tv_yuyuetime.setOnClickListener(new DateTimeOnClick());
//		tv_yidongbz.setOnClickListener(new DateTimeOnClickYD());

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 11 && resultCode == 11 && data != null) {

			efb_khsspq.setText(data.getStringExtra("name"));
			eefb_khsspq.setTag(data.getStringExtra("id"));
			efb_sssqx.setText(data.getStringExtra("qymc"));
			eefb_sssqx.setTag(data.getStringExtra("qybm"));
			
			//客户网点
			efb_khwd.setText("");
			eefb_khwd.setTag("");
			efb_dyxz.setText("");
		}else if(requestCode == 12 && resultCode == 12 && data != null) {
			efb_fbdw.setText(data.getStringExtra("name"));
			eefb_fbdw.setTag(data.getStringExtra("id"));
			
			//设备认证设为空
			efb_rzsblx.setText("");
			eefb_rzsblx.setTag("");
			
			//费用类型
			efb_yylxxm.setText("");
			eefb_yylxxm.setTag("");
			
			//客户网点
			efb_khwd.setText("");
			eefb_khwd.setTag("");
			efb_dyxz.setText("");
			
		}else if(requestCode == 13 && resultCode == 13 && data != null) {
			efb_rzsblx.setText(data.getStringExtra("name"));
			eefb_rzsblx.setTag(data.getStringExtra("id"));
			//费用类型
			efb_yylxxm.setText("");
			eefb_yylxxm.setTag("");
		}else if(requestCode == 14 && resultCode == 14 && data != null) {
			efb_yylxxm.setText(data.getStringExtra("name"));
			eefb_yylxxm.setTag(data.getStringExtra("id"));
		}else if(requestCode == 15 && resultCode == 15 && data != null) {
			efb_khwd.setText(data.getStringExtra("name"));
			eefb_khwd.setTag(data.getStringExtra("id"));
			efb_dyxz.setText(data.getStringExtra("email"));
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	

	@Override
	protected void getWebService(String s) {

		if ("xq".equals(s)) {

			data_fbdw = new ArrayList<Map<String, String>>();
			data_khsspq = new ArrayList<Map<String, String>>();
			data_fylx = new ArrayList<Map<String, String>>();
			data_khwd = new ArrayList<Map<String, String>>();

			final JSONObject jsonObject_fbdw;
			final JSONObject jsonObject_sspq;
			final JSONObject jsonObject_fylx;
			final JSONObject jsonObject_khwd;

			String readFile_fbdw = null;
			String readFile_sspq = null;
			String readFile_fylx = null;
			String readFile_khwd = null;
			try {
				// _PAD_QX_FBF 发包单位
				// _PAD_PQSZ 所属片区
				//readFile_fbdw = Config.readFile("_PAD_QX_FBF", this);
				if (readFile_fbdw == null) {// _PAD_XQ_KD_LC
					jsonObject_fbdw = callWebserviceImp.getWebServerInfo(
							"_PAD_QX_FBF", DataCache.getinition().getUserId(),
							"uf_json_getdata", this);

					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {

							Config.writeFile("_PAD_QX_FBF",
									jsonObject_fbdw.toString(),
									getApplicationContext());
						}
					});
				} else {
					jsonObject_fbdw = new JSONObject(readFile_fbdw);
				}
				
				//readFile_sspq = Config.readFile("_PAD_PQSZ", this);
				if (readFile_sspq == null) {// _PAD_XQ_KD_LC
					jsonObject_sspq = callWebserviceImp.getWebServerInfo(
							"_PAD_PQSZ", "1", "uf_json_getdata", this);

					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {

							Config.writeFile("_PAD_PQSZ",
									jsonObject_sspq.toString(),
									getApplicationContext());
						}
					});
				} else {
					jsonObject_sspq = new JSONObject(readFile_sspq);
				}
				
				//费用类型
				//readFile_fylx = Config.readFile("_PAD_FYXMLX", this);
				if (readFile_fylx == null) {// _PAD_XQ_KD_LC
					jsonObject_fylx = callWebserviceImp.getWebServerInfo(
							"_PAD_FYXMLX", "1", "uf_json_getdata", this);

					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {

							Config.writeFile("_PAD_FYXMLX",
									jsonObject_fylx.toString(),
									getApplicationContext());
						}
					});
				} else {
					jsonObject_fylx = new JSONObject(readFile_fylx);
				}
				
				//客户网点
				//readFile_khwd = Config.readFile("_PAD_WD", this);
				if (readFile_khwd == null) {// _PAD_XQ_KD_LC
					jsonObject_khwd = callWebserviceImp.getWebServerInfo(
							"_PAD_WD", "1", "uf_json_getdata", this);

					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {

							Config.writeFile("_PAD_WD",
									jsonObject_khwd.toString(),
									getApplicationContext());
						}
					});
				} else {
					jsonObject_khwd = new JSONObject(readFile_khwd);
				}
				
				try {
					data_fbdw = jsonObjectParser.FBFParser(jsonObject_fbdw);
					data_khsspq = jsonObjectParser.xqParser(jsonObject_sspq);
					data_fylx = jsonObjectParser.FylxParser(jsonObject_fylx);
					data_khwd = jsonObjectParser.KhwdParser(jsonObject_khwd);
					Message msg = new Message();
					msg.what = 2;// 解析出错
					handler.sendMessage(msg);
				} catch (JSONException e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = -3;// 解析出错
					handler.sendMessage(msg);
				}

			} catch (Exception e) {
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		}

		if (s.equals("submit")) {// 提交
			try {// khbm,ds,kdzh,cfsj,khxxdz
				int random = (int) Math.round(Math.random()*10000);
				random = random<1000?random+1000:random>10000?random-1000:random;
				int ywlxid = wxaz.indexOf(efb_ywlx.getText()) + 1;
				String khwd = (String) eefb_khwd.getTag();
				String sql = "insert into shgl_ywgl_fwbgszb (zbh,djzt,pgbm,kzzd5,jdgs,fwgcs,sf,kzzd18,kzzd10,kzzd11,kzzd13,khbm,ds,bzr,sgdh,bzrlxdh,kzzd14,khxxdz,gzxx,bz,lxdh,kzzd17,bzsj,czy,czsj,czrz1) "
						+ "values (" + "'%s'," + "'" + "1"
						+ "','" + eefb_fbdw.getTag() 
						+ "','" + ywlxid + "','"
						+ eefb_rzsblx.getTag()
						+ "','"
						+ efb_smsf.getText()
						+ "','"
						+ eefb_yylxxm.getTag()
						+ "','"
						+ efb_smyq.getText()
						+ "','"
						+ efb_fbr.getText()
						+ "','"
						+ efb_fbrsj.getText()
						+ "','"
						+ (efb_fbrdx.isChecked()==true?"0":"1")
						+ "','"
						+ eefb_khsspq.getTag()
						+ "','"
						+ eefb_sssqx.getTag()
						+ "','"
						+ eefb_khwd.getTag()
						+ "','"
						+ efb_khxm.getText()
						+ "','"
						+ efb_khsj.getText()
						+ "','"
						+ (efb_khdx.isChecked()==true?"0":"1")
						+ "','"
						+ efb_khdz.getText()
						+ "','"
						+ efb_bwnr.getText()
						+ "','"
						+ efb_bz.getText()
						+ "','"
						+"2"
						+ "','"
						+random
						+ "',sysdate,'"
						+DataCache.getinition().getUserId()
						+ "',sysdate,'"
						+ "0*"+DataCache.getinition().getUserId()+"*"+new DataUtil().toDataString("yyyy-MM-dd HH:mm:ss") 
						+ "')";
				Log.e("YytReportedActivity", sql);
				flag = callWebserviceImp.getWebServerInfo("c#_PAD_BZLR", sql,
						DataCache.getinition().getUserId(), eefb_fbdw.getTag() + "*" + eefb_rzsblx.getTag(),
						"uf_json_setdata2", this).getString("flag");
				Log.e("YytReportedActivity", flag);
				if (Integer.parseInt(flag) > 0) {
					Message localMessage2 = new Message();
					localMessage2.what = 1;// 完成
					handler.sendMessage(localMessage2);

				} else {
					Message localMessage3 = new Message();
					localMessage3.what = 0;// 服务报告完成失败，请检查后重试...
					handler.sendMessage(localMessage3);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Message localMessage1 = new Message();
				localMessage1.what = 0;// 网络连接出错，你检查你的网络设置
				handler.sendMessage(localMessage1);
			}
			// 保存填写记录
			spfe.putString("bzr", efb_fbr.getText().toString().trim());
			spfe.putString("lxdh", efb_fbrsj.getText().toString().trim());

			spfe.commit();

		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case -3:
				dialogShowMessage_P("小区数据解析失败", null);
				break;
			case -2:
				dialogShowMessage_P("小区数据获取失败", null);
				break;
			case -1:
				dialogShowMessage_P("获取基础数据失败", null);
				break;
			case 1:
				dialogShowMessage_P("报障提交完成",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});

				break;
			case 0:
				dialogShowMessage_P("报障失败，请检查后重试...错误标识：" + flag, null);
				break;

			case 2:
				// 维护厂商

				break;
			}

			progressDialog.dismiss();

		}
	};

//	private final class DateTimeOnClick implements OnClickListener {
//		public void onClick(View v) {
//			DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
//					YytReportedActivity.this);
//			dateTimePicKDialog.dateTimePicKDialog(tv_yuyuetime, 0);
//		}
//	}
//
//	private final class DateTimeOnClickYD implements OnClickListener {
//		public void onClick(View v) {
//			DateTimePickerDialog dateTimePicKDialog = new DateTimePickerDialog(
//					YytReportedActivity.this);
//			dateTimePicKDialog.dateTimePicKDialog(tv_yidongbz, 0);
//		}
//	}

	@Override
	public void onBackPressed() {
		skipActivity2(MainActivity.class);
	}

}
