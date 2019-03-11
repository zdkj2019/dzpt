package com.single.activity.esp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.single.Parser.JSONObjectParser;
import com.single.activity.FrameActivity;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.common.Constant;
import com.single.gtdzpt.R;
import com.single.utils.Config;
import com.single.utils.DataUtil;
import com.single.utils.MyDialog;
import com.single.zxing.CaptureActivity;

/**
 * 报告填写，确认
 * 
 * @author wlj
 * 
 */
@SuppressLint("HandlerLeak")
public class ServiceReportsComplete extends FrameActivity {

	private TextView tv_xzpj, tv_ywlx, tv_fwbgsc;
	private EditText et_clgc, et_smyy;
	private Button basicconfirm, basiccancel;
	private ArrayList<Map<String, String>> data;
	private Map<String, Object> itemmap;
	private String zbh, msgStr;
	private List<Map<String, String>> data_gzbm, data_2_gzbm, data_3_gzbm,
			gzbm_2_list, gzbm_3_list, data_smyy;
	private Spinner sp_gzlb, sp_gzlbb, sp_gzzl, spinner_smyy;
	private String[] from;
	private int[] to;
	private ArrayList<String> hpsql, hpdata;
	private String xzpj_str = "", yysj, sblx, ywlx2;
	private RadioGroup rg_0;
	private CheckBox cb_sfhpj;
	private ImageView iv_telphone, iv_telphone_kh;
	private Map<String, ArrayList<String>> filemap;
	private String _PAD_EPOS_STR = "";
	private String _PAD_SFSCZZBG_STR = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_servicereport_complete);
		try {
			initVariable();
			initView();
			initListeners();
			if (!backboolean) {
				showProgressDialog();
			}
			Config.getExecutorService().execute(new Runnable() {

				@Override
				public void run() {

					getWebService("querygzlx");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void initVariable() {

		tv_ywlx = (TextView) findViewById(R.id.tv_ywlx);
		tv_fwbgsc = (TextView) findViewById(R.id.tv_fwbgsc);
		tv_xzpj = (TextView) findViewById(R.id.tv_xzpj);
		cb_sfhpj = (CheckBox) findViewById(R.id.cb_sfhpj);
		et_clgc = (EditText) findViewById(R.id.et_clgc);
		et_smyy = (EditText) findViewById(R.id.et_smyy);
		rg_0 = (RadioGroup) findViewById(R.id.rg_0);
		iv_telphone_kh = (ImageView) findViewById(R.id.iv_telphone_kh);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);

		basiccancel = (Button) findViewById(R.id.include_botton).findViewById(
				R.id.cancel);
		basicconfirm = (Button) findViewById(R.id.include_botton).findViewById(
				R.id.confirm);

		sp_gzlb = (Spinner) findViewById(R.id.sp_gzlb);
		sp_gzlbb = (Spinner) findViewById(R.id.sp_gzlbb);
		sp_gzzl = (Spinner) findViewById(R.id.sp_gzzl);
		spinner_smyy = (Spinner) findViewById(R.id.spinner_smyy);

		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };

		data = new ArrayList<Map<String, String>>();
		data_gzbm = new ArrayList<Map<String, String>>();
		data_2_gzbm = new ArrayList<Map<String, String>>();
		data_3_gzbm = new ArrayList<Map<String, String>>();
		gzbm_2_list = new ArrayList<Map<String, String>>();
		gzbm_3_list = new ArrayList<Map<String, String>>();
		filemap = new HashMap<String, ArrayList<String>>();
	}

	@Override
	protected void initView() {

		title.setText("电子服务报告");
		itemmap = ServiceReportCache.getObjectdata().get(
				ServiceReportCache.getIndex());
		zbh = itemmap.get("zbh").toString();
		sblx = itemmap.get("sblx").toString();
		yysj = itemmap.get("yysj").toString();
		ywlx2 = itemmap.get("ywlx2").toString();
		((TextView) findViewById(R.id.tv_zbh)).setText(itemmap.get("zbh")
				.toString());
		((TextView) findViewById(R.id.tv_smsf)).setText(itemmap.get("smsf")
				.toString());
		((TextView) findViewById(R.id.tv_sf)).setText(itemmap.get("sf")
				.toString());
		((TextView) findViewById(R.id.tv_ds)).setText(itemmap.get("ds")
				.toString());
		((TextView) findViewById(R.id.tv_qx)).setText(itemmap.get("sx")
				.toString());
		((TextView) findViewById(R.id.tv_wdmc)).setText(itemmap.get("xqmc")
				.toString());
		((TextView) findViewById(R.id.tv_xxdz)).setText(itemmap.get("xxdz")
				.toString());
		((TextView) findViewById(R.id.tv_jsdw)).setText("001".equals(itemmap.get(
				"cx").toString()) ? "城区" : "乡镇");// 结算位置
		((TextView) findViewById(R.id.tv_gzxx)).setText(itemmap.get("gzxx")
				.toString());
		((TextView) findViewById(R.id.tv_phone_kh)).setText(itemmap.get("lxdh")
				.toString());
		((TextView) findViewById(R.id.tv_ywlx)).setText(itemmap.get("ywlx")
				.toString());
		((TextView) findViewById(R.id.tv_rzsblx)).setText(itemmap
				.get("sblx_mc").toString());
		((TextView) findViewById(R.id.tv_fwgcs)).setText(itemmap.get("zxsxm")
				.toString());

		iv_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(itemmap.get("zxsdh").toString(),zbh);

			}
		});

		iv_telphone_kh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(itemmap.get("lxdh").toString(),zbh);

			}
		});

		tv_fwbgsc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				camera(2, filemap.get("1"));

			}
		});

		tv_xzpj.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(getApplicationContext(),
						AddParts.class);

				intent.putExtra("zbh", itemmap.get("zbh").toString());
				intent.putStringArrayListExtra("hpdata", hpdata);
				startActivityForResult(intent, 1);
			}

		});

	}

	@Override
	protected void initListeners() {

		// Button点击
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.confirm://
					try {
						// SimpleDateFormat sdf = new SimpleDateFormat(
						// "yyyy-MM-dd hh:mm:ss");
						// Date yydate = sdf.parse(yysj);
						// if (yydate.getTime() > new Date().getTime()) {
						// dialogShowMessage_P("预约时间未到，请联系服务工程师进行修改。", null);
						// return;
						// }
						if (rg_0.getCheckedRadioButtonId() == -1) {
							toastShowMessage("请选择是否二次上门！");
						} else {
							if (rg_0.getCheckedRadioButtonId() == R.id.rb_1) {
								if ("".equals(data_smyy.get(
										spinner_smyy.getSelectedItemPosition())
										.get("id"))) {
									toastShowMessage("请选择上门原因！");
									return;
								}
								if (!isNotNull(et_smyy)) {
									toastShowMessage("请录入原因说明！");
									return;
								}
								showProgressDialog();
								Config.getExecutorService().execute(
										new Runnable() {

											@Override
											public void run() {
												getWebService("submit");
											}
										});
							} else {
								boolean flag = true;
								String f = ((Map<String, String>) sp_gzlbb
										.getSelectedItem()).get("name");
								String ywxz = (String) tv_ywlx.getText();
								if ("维修".equals(ywxz)) {
									if (" ".equals(f)) {
										flag = false;
										dialogShowMessage_P("请选择故障小类", null);
										return;
									}
								}
								if (!isNotNull(et_clgc)) {
									flag = false;
									dialogShowMessage_P("请录入故障处理过程", null);
									return;
								}
								if (cb_sfhpj.isChecked() && flag) {
									if ("".equals(tv_xzpj.getText().toString())) {
										flag = false;
										dialogShowMessage_P("请新增配件", null);
										return;
									}
								}
								if (flag) {
									if ("1".equals(_PAD_SFSCZZBG_STR)) {
										if (_PAD_EPOS_STR.equals(sblx)) {
											if ("1".equals((tv_ywlx.getTag() + ""))// 巡检20160727陈总要求 巡检要上传照片
													|| "3".equals((tv_ywlx
															.getTag() + ""))// 安装
													|| "5".equals((tv_ywlx
															.getTag() + ""))) {// 撤机
												if(filemap==null||filemap.size()==0){
													dialogShowMessage_P("请拍照", null);
													return;
												}
											}
										}

									}
									sure();
								}
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
						Message localMessage1 = new Message();
						localMessage1.what = Constant.NETWORK_ERROR;// 网络连接出错，你检查你的网络设置
						handler.sendMessage(localMessage1);
					}

					break;
				default:
					onBackPressed();
					break;

				}

			}
		};

		topBack.setOnClickListener(onClickListener);
		basiccancel.setOnClickListener(onClickListener);
		basicconfirm.setOnClickListener(onClickListener);

		OnItemSelectedListener onItemSelectedListener_gzlb = new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				String select_id = data_gzbm.get(arg2).get("id");
				gzbm_2_list.clear();
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", "     ");

				map.put("name", " ");
				gzbm_2_list.add(map);
				// 选择的大类 设置中类
				for (int i = 0; i < data_2_gzbm.size(); i++) {

					String whcs_id = data_2_gzbm.get(i).get("id");
					if (whcs_id.startsWith(select_id)) {
						// 相等添加到维护厂商显示的list里
						gzbm_2_list.add(data_2_gzbm.get(i));
					}
				}

				SimpleAdapter adapter = new SimpleAdapter(
						ServiceReportsComplete.this, gzbm_2_list,
						R.layout.spinner_item, from, to);
				sp_gzzl.setAdapter(adapter);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		};

		OnItemSelectedListener onItemSelectedListener_gzzl = new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				String select_id = gzbm_2_list.get(arg2).get("id");
				gzbm_3_list.clear();
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", "     ");

				map.put("name", " ");
				gzbm_3_list.add(map);

				for (int i = 0; i < data_3_gzbm.size(); i++) {

					String whcs_id = data_3_gzbm.get(i).get("id");
					if (whcs_id.startsWith(select_id)) {
						// 相等添加到维护厂商显示的list里
						gzbm_3_list.add(data_3_gzbm.get(i));
					}
				}

				SimpleAdapter adapter = new SimpleAdapter(
						ServiceReportsComplete.this, gzbm_3_list,
						R.layout.spinner_item, from, to);
				sp_gzlbb.setAdapter(adapter);

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		};

		sp_gzlb.setOnItemSelectedListener(onItemSelectedListener_gzlb);// 故障类别
																		// 大类
		sp_gzzl.setOnItemSelectedListener(onItemSelectedListener_gzzl);// 故障类别
																		// 中类

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
			// 配件hpsql

			hpsql = data.getStringArrayListExtra("hpsql");
			hpdata = data.getStringArrayListExtra("hpdata");
			xzpj_str = "";
			try {
				if (hpdata != null) {
					for (int i = 0; i < hpdata.size(); i++) {
						String[] hps = hpdata.get(i).split(",");
						if (hps.length < 4) {
							xzpj_str = xzpj_str + hps[1] + "," + hps[2] + ","
									+ "\n";
						} else {
							xzpj_str = xzpj_str + hps[1] + "," + hps[2] + ","
									+ hps[3] + "\n";
						}
					}
					xzpj_str = xzpj_str.substring(0, xzpj_str.length() - 1);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			tv_xzpj.setText(xzpj_str);

		}
		if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
			ArrayList<String> list = data.getStringArrayListExtra("imglist");
			if (list != null) {
				loadImg(list);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void loadImg(final ArrayList<String> list) {
		try {
			if (list.size() > 0) {
				tv_fwbgsc.setText("继续选择");
				tv_fwbgsc.setBackgroundResource(R.drawable.btn_normal_yellow);
			} else {
				tv_fwbgsc.setText("选择图片");
				tv_fwbgsc.setBackgroundResource(R.drawable.btn_normal);
			}
			filemap.put("1", list);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void sure() {
		// toastShowMessage(itemmap.get("kzzd17"));

		Intent intent = new Intent(ServiceReportsComplete.this,
				ServiceReportsAppraiseActivity.class);

		intent.putExtra("zbh", itemmap.get("zbh").toString());
		intent.putExtra("sf", itemmap.get("sf").toString());
		intent.putExtra("ds", itemmap.get("ds").toString());
		intent.putExtra("qx", itemmap.get("sx").toString());
		intent.putExtra("xqmc", itemmap.get("xqmc").toString());
		
		intent.putExtra("khbm", itemmap.get("khbm").toString());
		intent.putExtra("khbmmc", itemmap.get("xqmc").toString());
		
		intent.putExtra("ds", itemmap.get("ds").toString());
		intent.putExtra("dsmc", itemmap.get("ds").toString());

		intent.putExtra("pgzt", "1");
		intent.putExtra("zdh", "");

		intent.putExtra("bzrbm", "");
		intent.putExtra("bzr", itemmap.get("khlxr").toString());

		intent.putExtra("bzrlxdh", itemmap.get("lxdh").toString());

		intent.putExtra("kzzd5", itemmap.get("ywlx").toString());
		intent.putExtra("kzzd5mc", itemmap.get("ywlx2").toString());

		intent.putExtra("khxxdz", itemmap.get("xxdz").toString());
		intent.putExtra("sfhpj", cb_sfhpj.isChecked() ? "1" : "0");

		intent.putExtra("bz", "");

		intent.putExtra("gzxx", itemmap.get("gzxx").toString());

		intent.putExtra("cljg", "");
		intent.putExtra("fwyj", "");// 未完工原因、
		intent.putExtra("kzzd17", "");
		intent.putExtra("fbsw", "");
		intent.putExtra("kzzd19", "");
		intent.putStringArrayListExtra("hpdata", hpdata);

		intent.putExtra("xzpj", tv_xzpj.getText().toString());

		intent.putExtra("gzlbid", (sp_gzlb.getSelectedItem() == null ? ""
				: ((Map<String, String>) sp_gzlb.getSelectedItem()).get("id")));
		intent.putExtra(
				"gzlbname",
				(sp_gzlb.getSelectedItem() == null ? ""
						: ((Map<String, String>) sp_gzlb.getSelectedItem())
								.get("name")));

		intent.putExtra("gzzlid", (sp_gzzl.getSelectedItem() == null ? ""
				: ((Map<String, String>) sp_gzzl.getSelectedItem()).get("id")));
		intent.putExtra(
				"gzzlname",
				(sp_gzzl.getSelectedItem() == null ? ""
						: ((Map<String, String>) sp_gzzl.getSelectedItem())
								.get("name")));

		intent.putExtra("gzlbbid", (sp_gzlbb.getSelectedItem() == null ? ""
				: ((Map<String, String>) sp_gzlbb.getSelectedItem()).get("id")));
		intent.putExtra(
				"gzlbbname",
				(sp_gzlbb.getSelectedItem() == null ? ""
						: ((Map<String, String>) sp_gzlbb.getSelectedItem())
								.get("name")));

		intent.putExtra("zdcs", "");

		intent.putExtra("mmjpxlh", "");

		intent.putExtra("sshy", "");

		intent.putExtra("sbxlh", "");

		intent.putExtra("eposcs", "");
		intent.putExtra("eposxh", "");
		intent.putExtra("sfbh", "");
		intent.putExtra("pddh", "");

		intent.putExtra("ddsj", itemmap.get("ddsj").toString());
		intent.putExtra("slsj", itemmap.get("slsj").toString());
		intent.putExtra("bzsj", itemmap.get("bzsj").toString());
		intent.putExtra("wgsj", itemmap.get("wgsj").toString());
		intent.putExtra("sfhj", hpsql == null ? "否" : "是");

		intent.putExtra("kzzd6", et_clgc.getText() + "");

		intent.putExtra("sbbm", "");

		startActivity(intent);
		// finish();
	}

	@Override
	protected void getWebService(String s) {
		//
		// if ("submit".equals(s) && !backboolean) {
		// String str1 = itemmap.get("oddnumber");
		// String cljg = "'1'";
		// String fwyj = "";;
		// String czrz3 = "'0'||chr(42)||'"
		// + DataCache.getinition().getUserId() + "'||chr(42)||'"
		// + new DataUtil().toDataString("yyyy-MM-dd HH:mm:ss") + "'";
		// try {
		// String str2 = "update shgl_ywgl_fwbgszb set "
		// // + "pgbm='"+ tv_fbdw.getTag() + "',"
		// + "khbm='"
		// + tv_supq.getTag()
		// + "',"
		// + "ds='"
		// + tv_suqy.getTag()
		// + "',"
		// + "bzr='"
		// + tv_bzr.getTag()
		// + "',"
		// // + "bzrlxdh='"+ et_lxdh.getText()+ "',"
		// + "kzzd1='"
		// + (sp_gzlb.getSelectedItem() == null ? ""
		// : ((Map<String, String>) sp_gzlb
		// .getSelectedItem()).get("id"))
		// + "',"
		// + "kzzd15='"
		// + (sp_gzzl.getSelectedItem() == null ? ""
		// : ((Map<String, String>) sp_gzzl
		// .getSelectedItem()).get("id"))
		// + "',"
		// + "kzzd16='"
		// + (sp_gzlbb.getSelectedItem() == null ? ""
		// : ((Map<String, String>) sp_gzlbb
		// .getSelectedItem()).get("id"))
		// + "',"
		// + "kzzd5='"
		// + tv_wxaz.getTag()
		// + "',"
		// + "kzzd19='"
		// + et_zsdz.getText()
		// + "',"
		// + "clfs='"
		// + 2
		// + "',"
		// + "khxxdz='"
		// + et_xxdz.getText()
		// + "',"
		// + "mmjpxlh='"
		// + tv_mmjpxlh.getText()
		// + "',"
		// + "eposcs='"
		// + tv_eposcs.getText()
		// + "',"
		// + "eposxh='"
		// + tv_xh.getText()
		// + "',"
		// + "sfbh='"
		// + tv_shbh.getText()
		// + "',"
		// + "pddh='"
		// + tv_bddh.getText()
		// + "',kzzd6='"
		// + et_clgc.getText().toString()
		// + "',"
		// // + "bz='"+ et_bz.getText()+ "',"
		// // + "gzxx='"+ et_gzxx.getText()+ "',"
		// + "cljg= "
		// + cljg
		// + ",fwyj='"
		// + fwyj
		// + "'"
		// + ","
		// + "kzzd3='"
		// + DataCache.getinition().getUserId()
		// + "',"
		// +"ywhy='"+(spinner_hy.getSelectedItem() == null ? "": ((Map<String,
		// String>) spinner_hy.getSelectedItem()).get("id"))+"',"
		// +"sbxlh='"+et_sbxlh.getText().toString()+"',"
		// +
		// "fwbg_wgsj=sysdate,wcsj=sysdate,kzzd7=sysdate,sfhf='1',djzt='4',czrz5="
		// + czrz3 + " where " + "zbh='" + str1 + "'";
		// str2 = str2 + "*sql*";
		// Log.e("dd", str2);
		// String flag = null;
		// flag = this.callWebserviceImp.getWebServerInfo(
		// "_PAD_FWBG",
		// str2,
		// DataCache.getinition().getUserId(),
		// str1 + "*" + DataCache.getinition().getUserId() + "*"
		// + "0000" + "*"// 营业厅账号
		// + "0000" // 营业厅密码
		// , "uf_json_setdata2", this).getString("flag");
		// if (Integer.parseInt(flag) > 0) {
		// Message localMessage2 = new Message();
		// localMessage2.what = 1;// 完成
		// this.handler.sendMessage(localMessage2);
		// } else {
		// Message localMessage2 = new Message();
		// localMessage2.what = 1;// 完成
		// this.handler.sendMessage(localMessage2);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// Message localMessage1 = new Message();
		// localMessage1.what = -1;// 网络连接出错，你检查你的网络设置
		// this.handler.sendMessage(localMessage1);
		// }
		// }
		// /**
		// * 网点
		// */
		// if ("wd".equals(s) && !backboolean) {
		//
		// JSONObject webServerInfo;
		// try {
		// webServerInfo = callWebserviceImp.getWebServerInfo("_PAD_WD",
		// itemmap.get("fbsw") + "*" + tv_supq.getTag(),
		// "uf_json_getdata", getApplicationContext());
		// //
		// {"tableA":[{"ccgl_wlsjb_email":"城区","ccgl_wlsjb_wlsjmc":"a小区2","ccgl_wlsjb_xxdz":"","ccgl_wlsjb_wlsjbm":"0000002"},
		// //
		// {"ccgl_wlsjb_email":"城区","ccgl_wlsjb_wlsjmc":"a小区1","ccgl_wlsjb_xxdz":"详细地址","ccgl_wlsjb_wlsjbm":"0000001"}],"flag":"2","costTime":197}
		// String flag = webServerInfo.getString("flag");
		//
		// if (Integer.parseInt(flag) > 0) {
		// JSONArray jsonArray = webServerInfo.getJSONArray("tableA");
		// data.clear();
		// for (int i = 0; i < jsonArray.length(); i++) {
		//
		// JSONObject temp = jsonArray.getJSONObject(i);
		// Map<String, String> item = new HashMap<String, String>();
		//
		// item.put("wz", temp.getString("ccgl_wlsjb_email"));
		// item.put("name", temp.getString("ccgl_wlsjb_wlsjmc"));
		// item.put("id", temp.getString("ccgl_wlsjb_wlsjbm"));
		// item.put("xxdz", temp.getString("ccgl_wlsjb_xxdz"));
		//
		// data.add(item);
		// }
		//
		// Message localMessage2 = new Message();
		// localMessage2.what = 4;// 完成
		// this.handler.sendMessage(localMessage2);
		// } else if (Integer.parseInt(flag) == 0) {
		//
		// Message localMessage2 = new Message();
		// localMessage2.what = 3;// 完成
		// this.handler.sendMessage(localMessage2);
		//
		// } else {
		// Message localMessage2 = new Message();
		// localMessage2.what = 2;// 完成
		// this.handler.sendMessage(localMessage2);
		// }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// Message localMessage1 = new Message();
		// localMessage1.what = -1;// 网络连接出错，你检查你的网络设置
		// this.handler.sendMessage(localMessage1);
		// }
		//
		// }
		//
		/**
		 * 故障类别
		 */
		if ("querygzlx".equals(s)) {

			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGZLB", sblx, "uf_json_getdata", this);
				JSONObject jsonObject1 = callWebserviceImp.getWebServerInfo(
						"_PAD_EPOS", "1", "uf_json_getdata", this);
				JSONObject jsonObject3 = callWebserviceImp.getWebServerInfo(
						"_PAD_SFSCZZBG", sblx, "uf_json_getdata", this);
				String flag1 = jsonObject1.getString("flag");
				String flag3 = jsonObject3.getString("flag");
				if (Integer.parseInt(flag1) > 0) {
					JSONArray jsonArray2 = jsonObject1.getJSONArray("tableA");
					JSONObject temp = jsonArray2.getJSONObject(0);
					_PAD_EPOS_STR = temp.getString("cs");
				}

				if (Integer.parseInt(flag3) > 0) {
					JSONArray jsonArray3 = jsonObject3.getJSONArray("tableA");
					JSONObject temp = jsonArray3.getJSONObject(0);
					_PAD_SFSCZZBG_STR = temp.getString("ssjg");
				}

				String flag2 = jsonObject.getString("flag");

				if (Integer.parseInt(flag2) > 0) {
					JSONArray jsonArray2 = jsonObject.getJSONArray("tableA");

					Map<String, String> item = new HashMap<String, String>();

					item.put("id", "     ");
					item.put("name", " ");
					data_gzbm.add(item);
					data_2_gzbm.add(item);
					data_3_gzbm.add(item);

					for (int i = 0; i < jsonArray2.length(); i++) {
						JSONObject temp = jsonArray2.getJSONObject(i);
						item = new HashMap<String, String>();
						String id = temp.getString("gzlbbm");

						item.put("id", id);
						item.put("name", temp.getString("gzlbmc"));
						if (id.length() == 2) {
							data_gzbm.add(item);
						} else if (id.length() == 4) {
							data_2_gzbm.add(item);
						} else {
							data_3_gzbm.add(item);
						}

					}

					data_smyy = new ArrayList<Map<String, String>>();
					item = new HashMap<String, String>();
					item.put("id", "");
					item.put("name", "");
					data_smyy.add(item);
					// 上门原因
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_KDG_FWBG_ECSMYY", ywlx2, "uf_json_getdata",
							this);
					String flag = jsonObject.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						JSONArray jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							item = new HashMap<String, String>();
							item.put("id", temp.getString("ccd"));
							item.put("name", temp.getString("ccdnm"));
							data_smyy.add(item);
						}
					} else {
						msgStr = "获取二次上门原因失败";
						Message msg = new Message();
						msg.what = Constant.FAIL;// 失败
						handler.sendMessage(msg);
					}

					// DataCache.getinition().setErji_GZLB(data_erji_gzbm);
					// DataCache.getinition().setGZLB(data_gzbm);
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
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

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;

			case Constant.FAIL:
				dialogShowMessage_P("失败," + msgStr, null);
				break;
			case Constant.SUBMIT_SUCCESS:
				dialogShowMessage_P("电话消单成功",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face, int in) {
								skipActivity2(MainActivity.class);
								finish();
							}
						});

				break;
			case Constant.SUCCESS:

				SimpleAdapter adapter = new SimpleAdapter(
						ServiceReportsComplete.this, data_gzbm,
						R.layout.spinner_item, from, to);

				sp_gzlb.setAdapter(adapter);

				adapter = new SimpleAdapter(ServiceReportsComplete.this,
						data_smyy, R.layout.spinner_item, from, to);
				spinner_smyy.setAdapter(adapter);

				break;
			}

			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

}
