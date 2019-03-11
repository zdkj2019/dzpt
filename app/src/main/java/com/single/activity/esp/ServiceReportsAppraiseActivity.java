package com.single.activity.esp;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.single.activity.FrameActivity;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.common.Constant;
import com.single.definition.Sign;
import com.single.gtdzpt.R;
import com.single.utils.Config;
import com.single.utils.DataUtil;
import com.single.utils.ImageUtil;
import com.single.utils.MyDialog;

/**
 * 评价
 * 
 * @author asus
 * 
 */
@SuppressLint({ "HandlerLeak" })
public class ServiceReportsAppraiseActivity extends FrameActivity implements
		Sign {
	private Button cancel;
	private Button confirm;
	private EditText et_mima, et_qt, et_fwtdpj_yy, et_yzyxpj_yy;
	private String flag, msgStr,djzt,zbh;
	private Intent intent;
	private Map<Integer, LinearLayout> mmap;
	private RadioGroup rg_eglx, rg_myd;
	private RadioButton radioButton1;
	private RadioButton radioButton2;
	// private RadioButton radioButton3;
	private RadioButton radioButton4;
	private RadioButton radioButton5;
	private CheckBox radioButton6;
	private LinearLayout radioRL1;
	private LinearLayout radioRL2;
	// private RelativeLayout radioRL3;
	private LinearLayout radioRL4;
	private LinearLayout radioRL5;
	private LinearLayout namepass, ll_ycwg, ll_qt;
	private int index;
	private LinearLayout jbxx_bt, jzfwbg_bt, zyxx_bt;
	private ImageView jbxx_imageview, jzfwbg_imageview, jzfwbg_imageView1,
			zyxx_imageview, zyxx_imageView1, jbxx_imageView1;
	private LinearLayout jbxx_xxi, zyxx_xxi, jzfwbg_xxi;
	private TextView jbxx_TextView1, jzfwbg_TextView1, zyxx_TextView1;
	private LayoutInflater inflater;
	private View basic_i_xx, service_r_xx, zyxx_i_xx;
	private boolean basicstate, servicestate, zyxxstate;
	private Drawable _bg_down_ico, _jbxx_down_ico, _fx_down_ico;
	private MyDialog myDialog;
	private String rand = "";
	private CheckBox checkbox_bzsj, checkbox_slsj, checkbox_ddsj,
			checkbox_wgsj, checkbox_sfhj, checkbox_gzlb;
	private Spinner spinner_ycwg;
	private Map<String, ArrayList<String>> filemap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		appendMainBody(R.layout.activity_servicereport_o);
		initVariable();
		initView();
		initListeners();
	}

	protected void getWebService(String paramString) {

		if ("submit".equals(paramString)) {
			if("3.6".equals(djzt)||"3.5".equals(djzt)){//服务报告提交

				boolean f = true;
				int sfhj = "是".equals(intent.getStringExtra("sfhj")) ? 1 : 0;
				if (sfhj == 1) {
					try {
						JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
								"_PAD_FWBG_CWCX", DataCache.getinition()
										.getUserId(), "uf_json_getdata", this);
						flag = jsonObject.getString("flag");
						if (Integer.parseInt(flag) > 0) {
							JSONArray jsonArray = jsonObject.getJSONArray("tableA");
							JSONObject object = jsonArray.getJSONObject(0);
							String sl = object.getString("sl");
							if (!"1".equals(sl)) {
								msgStr = "仓位权限错误，请联系管理员";
								f = false;
								Message msg = new Message();
								msg.what = Constant.FAIL;
								handler.sendMessage(msg);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (f) {
					Object tag = radioButton4.getTag();
					String yzm = "".equals(et_mima.getText().toString()) ? "0"
							: et_mima.getText().toString();

					String ycwg_id = spinner_ycwg.getSelectedItemPosition() + 1
							+ "";
					String ycwg_text = "".equals(et_qt.getText().toString()) ? "0"
							: et_qt.getText().toString();
					String fwtdpj_id = "";
					if(rg_myd.getCheckedRadioButtonId() == R.id.rb_fwtdpj_1){
						fwtdpj_id = "1";
					}else if(rg_myd.getCheckedRadioButtonId() == R.id.rb_fwtdpj_2){
						fwtdpj_id = "2";
					}else if(rg_myd.getCheckedRadioButtonId() == R.id.rb_fwtdpj_3){
						fwtdpj_id = "3";
					}else if(rg_myd.getCheckedRadioButtonId() == R.id.rb_fwtdpj_4){
						fwtdpj_id = "4";
					}else if(rg_myd.getCheckedRadioButtonId() == R.id.rb_fwtdpj_5){
						fwtdpj_id = "5";
					}
					String fwtdpj_text = et_fwtdpj_yy.getText().toString();
					String isyzm = rg_eglx.getCheckedRadioButtonId() == R.id.rb_yzm ? "1"
							: "2";
//					// 服务态度评价 衣着言行评价都满意就满意
//					if (!("1".equals(fwtdpj_id) && "1".equals(yzyxpj_id))) {
//						index = 3;
//					}
//					String fwpj = index + 1 + "";
					try {

						String str = zbh + "*PAM*"
								+ intent.getStringExtra("gzlbid") + "*PAM*"
								+ intent.getStringExtra("gzzlid") + "*PAM*"
								+ intent.getStringExtra("gzlbbid") + "*PAM*"
								+ intent.getStringExtra("clgc") + "*PAM*" + sfhj;

						ArrayList<String> hpdata = intent
								.getStringArrayListExtra("hpdata");
						String xzpj_str = "*PAM*";
						try {
							if (hpdata.size()>1) {
								for (int i = 0; i < hpdata.size()-1; i++) {
									String[] hps = hpdata.get(i).split(",");
									if (hps.length < 4) {
										xzpj_str = xzpj_str + hps[0] + "#@#"+ hps[2]+ "#@#"+ hps[3]+ "#@#"+ hps[4]+ "#@#"+ hps[5]+ "#@#"+ hps[6];
									} else {
										xzpj_str = xzpj_str + hps[0] + "#@#"+ hps[2]+ "#@#"+ hps[3]+ "#@#"+ hps[4]+ "#@#"+ hps[5]+ "#@#"+ hps[6];
									}
									xzpj_str = xzpj_str + "#^#";
								}
								xzpj_str = xzpj_str.substring(0,
										xzpj_str.length() - 3);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						str = str + xzpj_str + "*PAM*" + fwtdpj_id + "*PAM*"
								+ fwtdpj_text + "*PAM*" + "" + "*PAM*"
								+ "" + "*PAM*" + isyzm + "*PAM*" + ycwg_id
								+ "*PAM*" + ycwg_text + "*PAM*"
								+ intent.getStringExtra("zsdz");

						JSONObject jsonobject = this.callWebserviceImp
								.getWebServerInfo("2#_PAD_FWBG", str, DataCache
										.getinition().getUserId(), zbh,
										"uf_json_setdata2", this);

						this.flag = jsonobject.getString("flag");

						Log.e("flag", flag);
						int flags = Integer.parseInt(this.flag);
						if (flags > 0) {

							str = "";
							str = "update CCGL_WLSJB set xxdz = '"
									+ intent.getStringExtra("khxxdz")
									+ "' where jzmc = '"
									+ intent.getStringExtra("fbsw")
									+ "' AND WLSJBM = '"
									+ intent.getStringExtra("bzrbm") + "'";
							JSONObject jsonObject = callWebserviceImp
									.getWebServerInfo("_RZ", str, DataCache
											.getinition().getUserId(),
											"uf_json_setdata", this);
							flag = jsonObject.getString("flag");
							if (Integer.parseInt(flag) > 0) {
								if(filemap.size()!=0){
									Config.getExecutorService().execute(new Runnable() {

										@Override
										public void run() {
											try {
												ArrayList<String> list = filemap.get("1");
												for(int i=0;i<list.size();i++){
													String filepath = list.get(i);
													filepath = filepath.substring(7, filepath.length());
													ImageUtil.compressAndGenImage(ImageUtil.ratio(filepath,getScreenWidth(),getScreenHeight()),filepath, 200);
													File file = new File(filepath);
													boolean fileflag = uploadPic(zbh, readJpeg(file),"uf_json_setdata2_p1");
													System.out.println("json:"+fileflag);
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									});
								}
								Message msg = new Message();
								msg.what = Constant.SUBMIT_SUCCESS;
								this.handler.sendMessage(msg);
							} else {
								Message msg = new Message();
								msg.what = Constant.SUBMIT_SUCCESS;
								handler.sendMessage(msg);
							}

						} else {
							msgStr = jsonobject.getString("msg");
							Message msg = new Message();
							msg.what = Constant.FAIL;
							this.handler.sendMessage(msg);
						}

						return;
					} catch (Exception e) {
						e.printStackTrace();
						Message msg = new Message();
						msg.what = Constant.NETWORK_ERROR;// 网络连接出错，你检查你的网络设置
						this.handler.sendMessage(msg);
					}
				}
			}else if("4".equals(djzt)||"5".equals(djzt)){//服务报告提交

				boolean f = true;
				int sfhj = "是".equals(intent.getStringExtra("sfhj")) ? 1 : 0;
				if (sfhj == 1) {
					try {
						JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
								"_PAD_FWBG_CWCX", DataCache.getinition()
										.getUserId(), "uf_json_getdata", this);
						flag = jsonObject.getString("flag");
						if (Integer.parseInt(flag) > 0) {
							JSONArray jsonArray = jsonObject.getJSONArray("tableA");
							JSONObject object = jsonArray.getJSONObject(0);
							String sl = object.getString("sl");
							if (!"1".equals(sl)) {
								msgStr = "仓位权限错误，请联系管理员";
								f = false;
								Message msg = new Message();
								msg.what = Constant.FAIL;
								handler.sendMessage(msg);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (f) {
					Object tag = radioButton4.getTag();
					String yzm = "".equals(et_mima.getText().toString()) ? "0"
							: et_mima.getText().toString();

					String ycwg_id = spinner_ycwg.getSelectedItemPosition() + 1
							+ "";
					String ycwg_text = "".equals(et_qt.getText().toString()) ? "0"
							: et_qt.getText().toString();
					String fwtdpj_id = "";
					if(rg_myd.getCheckedRadioButtonId() == R.id.rb_fwtdpj_1){
						fwtdpj_id = "1";
					}else if(rg_myd.getCheckedRadioButtonId() == R.id.rb_fwtdpj_2){
						fwtdpj_id = "2";
					}else if(rg_myd.getCheckedRadioButtonId() == R.id.rb_fwtdpj_3){
						fwtdpj_id = "3";
					}else if(rg_myd.getCheckedRadioButtonId() == R.id.rb_fwtdpj_4){
						fwtdpj_id = "4";
					}else if(rg_myd.getCheckedRadioButtonId() == R.id.rb_fwtdpj_5){
						fwtdpj_id = "5";
					}
					String fwtdpj_text = et_fwtdpj_yy.getText().toString();
					String isyzm = rg_eglx.getCheckedRadioButtonId() == R.id.rb_yzm ? "1"
							: "2";
//					// 服务态度评价 衣着言行评价都满意就满意
//					if (!("1".equals(fwtdpj_id) && "1".equals(yzyxpj_id))) {
//						index = 3;
//					}
//					String fwpj = index + 1 + "";
					try {

						String str = zbh + "*PAM*"
								+ intent.getStringExtra("gzlbid") + "*PAM*"
								+ intent.getStringExtra("gzzlid") + "*PAM*"
								+ intent.getStringExtra("gzlbbid") + "*PAM*"
								+ intent.getStringExtra("clgc") + "*PAM*" + sfhj;

						ArrayList<String> hpdata = intent
								.getStringArrayListExtra("hpdata");
						String xzpj_str = "*PAM*";
						try {
							if (hpdata.size()>1) {
								for (int i = 0; i < hpdata.size()-1; i++) {
									String[] hps = hpdata.get(i).split(",");
									if (hps.length < 4) {
										xzpj_str = xzpj_str + hps[0] + "#@#"+ hps[2]+ "#@#"+ hps[3]+ "#@#"+ hps[4]+ "#@#"+ hps[5]+ "#@#"+ hps[6];
									} else {
										xzpj_str = xzpj_str + hps[0] + "#@#"+ hps[2]+ "#@#"+ hps[3]+ "#@#"+ hps[4]+ "#@#"+ hps[5]+ "#@#"+ hps[6];
									}
									xzpj_str = xzpj_str + "#^#";
								}
								xzpj_str = xzpj_str.substring(0,
										xzpj_str.length() - 3);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						str = str + xzpj_str + "*PAM*" + fwtdpj_id + "*PAM*"
								+ fwtdpj_text + "*PAM*" + "" + "*PAM*"
								+ "" + "*PAM*" + isyzm + "*PAM*" + ycwg_id
								+ "*PAM*" + ycwg_text + "*PAM*"
								+ intent.getStringExtra("zsdz");

						JSONObject jsonobject = this.callWebserviceImp
								.getWebServerInfo("2#_PAD_FWBG_XG", str, DataCache
										.getinition().getUserId(), zbh,
										"uf_json_setdata2", this);

						this.flag = jsonobject.getString("flag");

						Log.e("flag", flag);
						int flags = Integer.parseInt(this.flag);
						if (flags > 0) {

							str = "";
							str = "update CCGL_WLSJB set xxdz = '"
									+ intent.getStringExtra("khxxdz")
									+ "' where jzmc = '"
									+ intent.getStringExtra("fbsw")
									+ "' AND WLSJBM = '"
									+ intent.getStringExtra("bzrbm") + "'";
							JSONObject jsonObject = callWebserviceImp
									.getWebServerInfo("_RZ", str, DataCache
											.getinition().getUserId(),
											"uf_json_setdata", this);
							flag = jsonObject.getString("flag");
							if (Integer.parseInt(flag) > 0) {
								if(filemap.size()!=0){
									Config.getExecutorService().execute(new Runnable() {

										@Override
										public void run() {
											try {
												ArrayList<String> list = filemap.get("1");
												for(int i=0;i<list.size();i++){
													String filepath = list.get(i);
													filepath = filepath.substring(7, filepath.length());
													ImageUtil.compressAndGenImage(ImageUtil.ratio(filepath,getScreenWidth(),getScreenHeight()),filepath, 200);
													File file = new File(filepath);
													boolean fileflag = uploadPic(zbh, readJpeg(file),"uf_json_setdata2_p1");
													System.out.println("json:"+fileflag);
												}
											} catch (Exception e) {
												e.printStackTrace();
											}
										}
									});
								}
								Message msg = new Message();
								msg.what = Constant.SUBMIT_SUCCESS;
								this.handler.sendMessage(msg);
							} else {
								Message msg = new Message();
								msg.what = Constant.SUBMIT_SUCCESS;
								handler.sendMessage(msg);
							}

						} else {
							msgStr = jsonobject.getString("msg");
							Message msg = new Message();
							msg.what = Constant.FAIL;
							this.handler.sendMessage(msg);
						}

						return;
					} catch (Exception e) {
						e.printStackTrace();
						Message msg = new Message();
						msg.what = Constant.NETWORK_ERROR;// 网络连接出错，你检查你的网络设置
						this.handler.sendMessage(msg);
					}
				}
			}
			
		}
	}
	
	private boolean uploadPic(final String orderNumbers, final byte[] data1,
			final String methed) throws Exception {

		if (data1 != null && orderNumbers != null) {
			JSONObject json = callWebserviceImp.getWebServerInfo2(
					"_FWBG_1", orderNumbers, data1, methed, this);
			String flag = json.getString("flag");
			if ("1".equals(flag)) {
				return true;
			}
		}
		return false;
	}

	protected void initListeners() {

		View.OnClickListener local2 = new View.OnClickListener() {

			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.confirm:// 确认
					String ddsj = intent.getStringExtra("ddsj");
					// 不满15分钟不能提交
					Date now = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd hh:mm");
					Date pgdate;
//					try {
//						pgdate = sdf.parse(ddsj);
//						long time = now.getTime() - pgdate.getTime();
//						long gd_time = 10 * 60 * 1000;
//						if (time < gd_time) {
//							dialogShowMessage_P("对不起，规定时间未到，暂时无法提交！", null);
//							return;
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//						dialogShowMessage_P("对不起，规定时间未到，暂时无法提交！", null);
//						return;
//					}
					if (!(checkbox_bzsj.isChecked()
							&& checkbox_slsj.isChecked()
							&& checkbox_ddsj.isChecked()
							&& checkbox_wgsj.isChecked()
							&& checkbox_sfhj.isChecked() && checkbox_gzlb
							.isChecked())) {
						dialogShowMessage_P("请确认重要信息！", null);
						return;
					} else {
						if (rg_eglx.getCheckedRadioButtonId() == R.id.rb_yzm) { // 验证码完工
							if (isNotNull(et_mima)) {
								if (!rand.equals((et_mima.getText().toString()
										.trim()))) {
									dialogShowMessage_P("对不起,验证码错误！", null);
									return;
								} else {
									if (radioButton6.isChecked()) {
										showProgressDialog();
										Config.getExecutorService().execute(
												new Runnable() {

													@Override
													public void run() {
														getWebService("submit");
													}
												});
									} else {
										dialogShowMessage_P(
												"对不起，您必须确认您对上述信息已经认真核实，确认无误。",
												null);
										return;
									}
								}
							} else {
								dialogShowMessage_P("验证码不能为空，请录入验证码！", null);
								return;
							}
						} else {
							if (spinner_ycwg.getSelectedItemPosition() == 5) {
								if (!isNotNull(et_qt)) {
									dialogShowMessage_P("请录入非验证码消单备注！", null);
									return;
								}
							}
							if (radioButton6.isChecked()) {
								showProgressDialog();
								Config.getExecutorService().execute(
										new Runnable() {

											@Override
											public void run() {
												getWebService("submit");
											}
										});
							} else {
								dialogShowMessage_P(
										"对不起，您必须确认您对上述信息已经认真核实，确认无误。", null);
								return;
							}
						}
					}

					break;
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.bt_topback:
					onBackPressed();
					break;
				}
			}
		};
		this.topBack.setOnClickListener(local2);
		this.confirm.setOnClickListener(local2);
		this.cancel.setOnClickListener(local2);

		final LinearLayout[] arrayRL = { radioRL1, radioRL2, radioRL4, radioRL5 };
		final ArrayList<RadioButton> arrayRadioButton = new ArrayList<RadioButton>();
		arrayRadioButton.add(radioButton1);
		arrayRadioButton.add(radioButton2);
		arrayRadioButton.add(radioButton5);
		// arrayRadioButton.add(radneioButton3);
		arrayRadioButton.add(radioButton4);

		final ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("维修及时性不满意");
		arrayList.add("对维修人员工作态度不满意");
		arrayList.add("对现场维修质量部满意");

		View.OnClickListener radioOnClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				int i = arrayRL.length;
				for (int j = 0;; j++) {
					if (j >= i) {
						RadioButton localRadioButton = (RadioButton) findViewById(v
								.getId());

						localRadioButton.getText().toString();
						mmap.get(Integer.valueOf(v.getId()))
								.setBackgroundColor(Color.parseColor("#CDE7F8"));
						localRadioButton.setChecked(true);
						index = arrayRadioButton.indexOf(localRadioButton);
						radioButton4.setTag("");
						if (index >= 0 && index <= 1) {
							dialogShowMessage_P("感谢您对我们本次服务工作的支持", null);
						}
						if (index == 3) {

							myDialog.redioDialog_fwbg(radioButton4, "不满意原因",
									arrayList);
						}
						return;
					}
					arrayRadioButton.get(j).setChecked(false);
					arrayRL[j].setBackgroundResource(R.drawable.edittext_bg);
				}

			}

		};

		radioButton1.setOnClickListener(radioOnClickListener);
		radioButton2.setOnClickListener(radioOnClickListener);
		// radioButton3.setOnClickListener(radioOnClickListener);
		radioButton4.setOnClickListener(radioOnClickListener);
		radioButton5.setOnClickListener(radioOnClickListener);

		final Drawable _bg_ico = getResources().getDrawable(
				R.drawable.servicereport_bg_ico);
		final Drawable _jbxx_ico = getResources().getDrawable(
				R.drawable.servicereport_jbxx_ico);
		final Drawable _fx_ico = getResources().getDrawable(
				R.drawable.servicereport_fx_ico);
		final Drawable _dzfwbg_down_ico = getResources().getDrawable(
				R.drawable.servicereport_dzfwbg_down_ico);
		final Drawable _dzfwbg_ico = getResources().getDrawable(
				R.drawable.servicereport_dzfwbg_ico);

		OnClickListener myonClickListener = new OnClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {

				switch (v.getId()) {

				case R.id.jbxx_bt:// 基本信息

					if (basicstate) {

						jbxx_xxi.addView(basic_i_xx);
						jbxx_bt.setBackgroundDrawable(_bg_down_ico);
						jbxx_imageView1.setImageDrawable(_jbxx_down_ico);
						jbxx_TextView1.setTextColor(Color.WHITE);
						jbxx_imageview.setImageDrawable(_fx_down_ico);
						basicstate = false;
					} else {

						jbxx_xxi.removeAllViews();
						jbxx_bt.setBackgroundDrawable(_bg_ico);
						jbxx_imageView1.setImageDrawable(_jbxx_ico);
						jbxx_TextView1.setTextColor(Color.BLACK);
						jbxx_imageview.setImageDrawable(_fx_ico);
						basicstate = true;
					}

					break;
				case R.id.zyxx_bt:// 重要信息

					if (zyxxstate) {

						zyxx_xxi.addView(zyxx_i_xx);
						zyxx_bt.setBackgroundDrawable(_bg_down_ico);
						zyxx_imageView1.setImageDrawable(_jbxx_down_ico);
						zyxx_TextView1.setTextColor(Color.WHITE);
						zyxx_imageview.setImageDrawable(_fx_down_ico);
						zyxxstate = false;
					} else {

						zyxx_xxi.removeAllViews();
						zyxx_bt.setBackgroundDrawable(_bg_ico);
						zyxx_imageView1.setImageDrawable(_jbxx_ico);
						zyxx_TextView1.setTextColor(Color.BLACK);
						zyxx_imageview.setImageDrawable(_fx_ico);
						zyxxstate = true;
					}

					break;
				case R.id.jzfwbg_bt:// 电子服务报告

					if (servicestate) {
						jzfwbg_xxi.addView(service_r_xx);
						jzfwbg_bt.setBackgroundDrawable(_bg_down_ico);
						jzfwbg_imageView1.setImageDrawable(_dzfwbg_down_ico);
						jzfwbg_TextView1.setTextColor(Color.WHITE);
						jzfwbg_imageview.setImageDrawable(_fx_down_ico);
						servicestate = false;
					} else {

						jzfwbg_xxi.removeAllViews();
						jzfwbg_bt.setBackgroundDrawable(_bg_ico);
						jzfwbg_imageView1.setImageDrawable(_dzfwbg_ico);
						jzfwbg_TextView1.setTextColor(Color.BLACK);
						jzfwbg_imageview.setImageDrawable(_fx_ico);
						servicestate = true;
					}
					break;

				}
			}
		};

		jbxx_bt.setOnClickListener(myonClickListener);
		jzfwbg_bt.setOnClickListener(myonClickListener);
		zyxx_bt.setOnClickListener(myonClickListener);

		rg_eglx.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_yzm:
					namepass.setVisibility(View.VISIBLE);
					ll_ycwg.setVisibility(View.GONE);
					break;
				case R.id.rb_yc:
					namepass.setVisibility(View.GONE);
					ll_ycwg.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}

			}
		});

		spinner_ycwg.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 5) {
					ll_qt.setVisibility(View.VISIBLE);
				} else {
					ll_qt.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
	}

	@SuppressLint({ "UseSparseArrays" })
	protected void initVariable() {
		title.setText("服务报告评价");
		myDialog = new MyDialog(this);
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		basic_i_xx = inflater
				.inflate(R.layout.activity_servicereport_bas, null);
		zyxx_i_xx = inflater
				.inflate(R.layout.activity_servicereport_zyxx, null);
		service_r_xx = inflater.inflate(R.layout.activity_servicereport_appr,
				null);

		radioButton1 = ((RadioButton) service_r_xx
				.findViewById(R.id.radioButton1));
		radioButton2 = ((RadioButton) service_r_xx
				.findViewById(R.id.radioButton2));
		// radioButton3 = ((RadioButton) service_r_xx
		// .findViewById(R.id.radioButton3));
		radioButton4 = ((RadioButton) service_r_xx
				.findViewById(R.id.radioButton4));
		radioButton5 = ((RadioButton) service_r_xx
				.findViewById(R.id.radioButton5));
		radioButton6 = ((CheckBox) service_r_xx.findViewById(R.id.RadioButton6));
		radioRL1 = ((LinearLayout) service_r_xx.findViewById(R.id.radioRL1));
		radioRL2 = ((LinearLayout) service_r_xx.findViewById(R.id.radioRL2));
		// radioRL3 = ((RelativeLayout)
		// service_r_xx.findViewById(R.id.radioRL3));
		radioRL4 = ((LinearLayout) service_r_xx.findViewById(R.id.radioRL4));
		radioRL5 = ((LinearLayout) service_r_xx.findViewById(R.id.radioRL5));
		confirm = ((Button) service_r_xx.findViewById(R.id.incl).findViewById(
				R.id.confirm));
		cancel = ((Button) service_r_xx.findViewById(R.id.incl).findViewById(
				R.id.cancel));
		et_mima = (EditText) service_r_xx.findViewById(R.id.et_mima);
		et_qt = (EditText) service_r_xx.findViewById(R.id.et_qt);
		et_fwtdpj_yy = (EditText) service_r_xx.findViewById(R.id.et_fwtdpj_yy);
		rg_eglx = (RadioGroup) service_r_xx.findViewById(R.id.rg_eglx);
		rg_myd = (RadioGroup) service_r_xx.findViewById(R.id.rg_myd);
		namepass = (LinearLayout) service_r_xx.findViewById(R.id.namepass);
		ll_ycwg = (LinearLayout) service_r_xx.findViewById(R.id.ll_ycwg);
		ll_qt = (LinearLayout) service_r_xx.findViewById(R.id.ll_qt);

		spinner_ycwg = (Spinner) service_r_xx.findViewById(R.id.spinner_ycwg);
		String[] from = new String[] { "id", "name" };
		int[] to = new int[] { R.id.bm, R.id.name };
		List<Map<String, String>> list_data = new ArrayList<Map<String, String>>();
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", "1");
		map.put("name", "客户不在现场");
		list_data.add(map);
		map = new HashMap<String, String>();
		map.put("id", "2");
		map.put("name", "客户丢失验证码");
		list_data.add(map);
		map = new HashMap<String, String>();
		map.put("id", "3");
		map.put("name", "客户未收到验证码");
		list_data.add(map);
		map = new HashMap<String, String>();
		map.put("id", "4");
		map.put("name", "手机网络原因导致无法操作完工");
		list_data.add(map);
		map = new HashMap<String, String>();
		map.put("id", "5");
		map.put("name", "客户不愿意提供验证码");
		list_data.add(map);
		map = new HashMap<String, String>();
		map.put("id", "6");
		map.put("name", "其他");
		list_data.add(map);
		SimpleAdapter adapter = new SimpleAdapter(
				ServiceReportsAppraiseActivity.this, list_data,
				R.layout.spinner_item, from, to);
		spinner_ycwg.setAdapter(adapter);

		checkbox_bzsj = (CheckBox) zyxx_i_xx.findViewById(R.id.checkbox_bzsj);
		checkbox_slsj = (CheckBox) zyxx_i_xx.findViewById(R.id.checkbox_slsj);
		checkbox_ddsj = (CheckBox) zyxx_i_xx.findViewById(R.id.checkbox_ddsj);
		checkbox_wgsj = (CheckBox) zyxx_i_xx.findViewById(R.id.checkbox_wgsj);
		checkbox_sfhj = (CheckBox) zyxx_i_xx.findViewById(R.id.checkbox_sfhj);
		checkbox_gzlb = (CheckBox) zyxx_i_xx.findViewById(R.id.checkbox_gzlb);

		// et_pjnr = ((EditText) service_r_xx.findViewById(R.id.et_pjnr));
		mmap = new HashMap<Integer, LinearLayout>();
		mmap.put(Integer.valueOf(R.id.radioButton1), radioRL1);
		mmap.put(Integer.valueOf(R.id.radioButton2), radioRL2);
		// mmap.put(Integer.valueOf(R.id.radioButton3), radioRL3);
		mmap.put(Integer.valueOf(R.id.radioButton4), radioRL4);
		mmap.put(Integer.valueOf(R.id.radioButton5), radioRL5);

		// 主界面上的
		jbxx_bt = (LinearLayout) findViewById(R.id.jbxx_bt);
		jzfwbg_bt = (LinearLayout) findViewById(R.id.jzfwbg_bt);
		zyxx_bt = (LinearLayout) findViewById(R.id.zyxx_bt);

		jbxx_imageView1 = (ImageView) findViewById(R.id.jbxx_imageView1);
		jzfwbg_imageView1 = (ImageView) findViewById(R.id.jzfwbg_imageView1);
		zyxx_imageView1 = (ImageView) findViewById(R.id.zyxx_imageView1);

		jbxx_TextView1 = (TextView) findViewById(R.id.jbxx_TextView1);
		jzfwbg_TextView1 = (TextView) findViewById(R.id.jzfwbg_TextView1);
		zyxx_TextView1 = (TextView) findViewById(R.id.zyxx_TextView1);

		jbxx_imageview = (ImageView) findViewById(R.id.jbxx_imageview);
		jzfwbg_imageview = (ImageView) findViewById(R.id.jzfwbg_imageview);
		zyxx_imageview = (ImageView) findViewById(R.id.zyxx_imageview);

		jbxx_xxi = (LinearLayout) findViewById(R.id.jbxx_xxi);
		jzfwbg_xxi = (LinearLayout) findViewById(R.id.jzfwbg_xxi);
		zyxx_xxi = (LinearLayout) findViewById(R.id.zyxx_xxi);

		basicstate = servicestate = zyxxstate = true;

		_bg_down_ico = getResources().getDrawable(
				R.drawable.servicereport_bg_down_ico);
		_jbxx_down_ico = getResources().getDrawable(
				R.drawable.servicereport_jbxx_down_ico);
		_fx_down_ico = getResources().getDrawable(
				R.drawable.servicereport_fx_down_ico);

	}

	protected void initView() {

		this.intent = getIntent();
		
		filemap = DataCache.getinition().getFilemap();
		
		String fwyj = intent.getStringExtra("fwyj");
		rand = intent.getStringExtra("kzzd17");
		djzt = intent.getStringExtra("djzt");
		zbh = intent.getStringExtra("zbh");

		((TextView) basic_i_xx.findViewById(R.id.text_mxh)).setText(intent
				.getStringExtra("zbh"));
		// ((TextView)
		// basic_i_xx.findViewById(R.id.text_fbdw)).setText(intent.getStringExtra("pgbmmc"));
		((TextView) basic_i_xx.findViewById(R.id.text_supq)).setText(intent
				.getStringExtra("sx"));
		((TextView) basic_i_xx.findViewById(R.id.text_suqy)).setText(intent
				.getStringExtra("qy"));
		((TextView) basic_i_xx.findViewById(R.id.text_fy1)).setText(intent
				.getStringExtra("xqmc"));
		((TextView) basic_i_xx.findViewById(R.id.text_sf)).setText(intent
				.getStringExtra("sf"));
		((TextView) basic_i_xx.findViewById(R.id.text_lxdh)).setText(intent
				.getStringExtra("bzrlxdh"));
		((TextView) basic_i_xx.findViewById(R.id.text_gzdl)).setText(intent
				.getStringExtra("gzlbname"));
		((TextView) basic_i_xx.findViewById(R.id.text_gzxl)).setText(intent
				.getStringExtra("gzlbbname"));
		((TextView) basic_i_xx.findViewById(R.id.text_wxaz)).setText(intent
				.getStringExtra("kzzd5mc"));
		((TextView) basic_i_xx.findViewById(R.id.text_clfs)).setText("上门");
		((TextView) basic_i_xx.findViewById(R.id.text_xxdz)).setText(intent
				.getStringExtra("khxxdz"));
		((TextView) basic_i_xx.findViewById(R.id.text_zsdz)).setText(intent
				.getStringExtra("kzzd19"));
		((TextView) basic_i_xx.findViewById(R.id.text_bz)).setText(intent
				.getStringExtra("bz"));
		((TextView) basic_i_xx.findViewById(R.id.text_jkje)).setText(intent
				.getStringExtra("gzxx"));
		((TextView) basic_i_xx.findViewById(R.id.text_cljg)).setText(intent
				.getStringExtra("cljg"));

		// ((TextView) basic_i_xx.findViewById(R.id.text_sbbm)).setText(intent
		// .getStringExtra("sbbm"));
		// ((TextView) basic_i_xx.findViewById(R.id.text_clgc)).setText(intent
		// .getStringExtra("kzzd6"));
		// ((TextView) basic_i_xx.findViewById(R.id.text_xzpj)).setText(intent
		// .getStringExtra("xzpj"));

		((TextView) zyxx_i_xx.findViewById(R.id.text_ddsj)).setText(intent
				.getStringExtra("ddsj"));
		((TextView) zyxx_i_xx.findViewById(R.id.text_slsj)).setText(intent
				.getStringExtra("slsj"));
		((TextView) zyxx_i_xx.findViewById(R.id.text_bzsj)).setText(intent
				.getStringExtra("bzsj"));
		((TextView) zyxx_i_xx.findViewById(R.id.text_wgsj))
				.setText(new DataUtil().toDataString("yyyy-MM-dd HH:mm:ss"));
		((TextView) zyxx_i_xx.findViewById(R.id.text_sfhj)).setText(intent
				.getStringExtra("sfhj"));
		((TextView) zyxx_i_xx.findViewById(R.id.text_gzlb)).setText(intent
				.getStringExtra("gzlbbname"));

		((TextView) basic_i_xx.findViewById(R.id.text_mmjpxlh)).setText(intent
				.getStringExtra("mmjpxlh"));
		((TextView) basic_i_xx.findViewById(R.id.text_eposcs)).setText(intent
				.getStringExtra("eposcs"));
		((TextView) basic_i_xx.findViewById(R.id.text_eposxh)).setText(intent
				.getStringExtra("eposxh"));
		((TextView) basic_i_xx.findViewById(R.id.text_sfbh)).setText(intent
				.getStringExtra("sfbh"));
		((TextView) basic_i_xx.findViewById(R.id.text_pddh)).setText(intent
				.getStringExtra("pddh"));
		((TextView) basic_i_xx.findViewById(R.id.text_gzzl)).setText(intent
				.getStringExtra("gzzlname"));

		if (fwyj != null && !"null".equals(fwyj)) {
			((TextView) basic_i_xx.findViewById(R.id.text_fwyj)).setText(fwyj);
		} else {

		}

		jbxx_xxi.addView(basic_i_xx);
		jbxx_bt.setBackgroundDrawable(_bg_down_ico);
		jbxx_imageView1.setImageDrawable(_jbxx_down_ico);
		jbxx_TextView1.setTextColor(Color.WHITE);
		jbxx_imageview.setImageDrawable(_fx_down_ico);
		basicstate = false;

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message paramAnonymousMessage) {
			super.handleMessage(paramAnonymousMessage);
			switch (paramAnonymousMessage.what) {

			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case Constant.SUBMIT_SUCCESS:
				dialogShowMessage_P("服务报告提交成功",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face, int in) {
								skipActivity2(MainActivity.class);
								finish();
							}
						});

				break;
			case Constant.FAIL:
				dialogShowMessage_P("失败，" + msgStr, null);
				break;
			}

			if (progressDialog != null) {
				progressDialog.dismiss();
			}

		}
	};

	private String generateSql(String paramString1, String paramString2,
			List<String> paramList) {
		if ((paramList != null) && (paramList.size() > 0)) {
			StringBuilder localStringBuilder = new StringBuilder();
			Iterator localIterator = paramList.iterator();
			while (true) {
				if (!localIterator.hasNext()) {
					String str = localStringBuilder.substring(0, -5
							+ localStringBuilder.length());
					return paramString1 + "*sql*" + paramString2 + "*sql*"
							+ str;
				}
				localStringBuilder.append((String) localIterator.next());
			}
		}
		return paramString1 + "*sql*" + paramString2;
	}
}