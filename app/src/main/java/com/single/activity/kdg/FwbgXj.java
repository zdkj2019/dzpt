package com.single.activity.kdg;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.camera2.params.RggbChannelVector;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView.OnEditorActionListener;

import com.single.activity.FrameActivity;
import com.single.activity.esp.AddParts;
import com.single.activity.esp.ServiceReportsComplete;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.common.Constant;
import com.single.gtdzpt.R;
import com.single.utils.Config;
import com.single.utils.ImageUtil;
import com.single.zxing.CaptureActivity;

/**
 * 巡检-服务报告
 * 
 * @author zdkj
 *
 */
@SuppressLint("ResourceAsColor")
public class FwbgXj extends FrameActivity {

	private Button confirm, cancel;
	private TextView tv_pz_1, tv_pz_2, tv_pz_3, tv_pz_4, tv_pz_5, tv_pz_6,
			tv_pz_7, tv_pz_8, tv_pz_9, tv_pz_10, tv_pz_11, tv_pz_12, tv_pz_13;
	private EditText et_gzid, et_xjkssj, et_xjjssj;
	private Spinner spinner_fgsl;
	private String flag, zbh, message, sbbm = "0", fbf, wdbm, gzid;
	private String[] from;
	private int[] to;
	private LinearLayout ll_gdxx_p, ll_gdxx, ll_ggjcx_p, ll_ggjcx, ll_pz_p,
			ll_pz, ll_sngjcx_p, ll_sngjcx, ll_hwgjcx_p, ll_hwgjcx, ll_s_p,
			ll_s, ll_pz_13, ll_wbggjcx_p, ll_wbggjcx, ll_wbtjggaq_p,
			ll_wbtjggaq, ll_spjc_p, ll_spjc, ll_sx, ll_xxdz,ll_jssj_p,ll_jssj;
	private ImageView image_1, image_2, image_3, image_4, image_5, image_6,
			image_7, image_8, image_9, image_10, image_11, image_12, image_13;
	private EditText et_1, et_2, et_3, et_4, et_5, et_6, et_7, et_8, et_9,
			et_10, et_11, et_12, et_13;
	private int photonum = 1;
	private String filename, kdg_type;
	private File photo_file1 = null, photo_file2 = null, photo_file3 = null,
			photo_file4 = null, photo_file5 = null, photo_file6 = null,
			photo_file7 = null, photo_file8 = null, photo_file9 = null,
			photo_file10 = null, photo_file11 = null, photo_file12 = null,
			photo_file13 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_xj_fwbg);
		initVariable();
		initView();
		initListeners();

		AlertDialog.Builder builder = new Builder(this);
		builder.setCancelable(false);
		builder.setMessage("请选择快递柜类型");
		builder.setTitle("提示");
		builder.setPositiveButton("室内柜", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface face, int paramAnonymous2Int) {
				kdg_type = "001";
				loadPzSng();
				ll_pz_13.setVisibility(View.GONE);
				ll_hwgjcx_p.setVisibility(View.GONE);
			}
		});
		// 中间的按钮
		builder.setNeutralButton("半室内", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				kdg_type = "003";
				loadPzSng();
				ll_pz_13.setVisibility(View.GONE);
				ll_hwgjcx_p.setVisibility(View.GONE);
			}
		});

		builder.setNegativeButton("室外柜", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface face, int paramAnonymous2Int) {
				kdg_type = "002";
				loadPzHwg();

				ll_sngjcx_p.setVisibility(View.GONE);
			}
		});
		builder.create().show();
	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("确定");
		cancel.setText("取消");

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

		et_gzid = (EditText) findViewById(R.id.et_gzid);
		et_xjkssj = (EditText) findViewById(R.id.et_xjkssj);
		et_xjjssj = (EditText) findViewById(R.id.et_xjjssj);
		spinner_fgsl = (Spinner) findViewById(R.id.spinner_fgsl);

		ll_xxdz = (LinearLayout) findViewById(R.id.ll_xxdz);
		ll_gdxx_p = (LinearLayout) findViewById(R.id.ll_gdxx_p);
		ll_gdxx = (LinearLayout) findViewById(R.id.ll_gdxx);
		ll_ggjcx_p = (LinearLayout) findViewById(R.id.ll_ggjcx_p);
		ll_ggjcx = (LinearLayout) findViewById(R.id.ll_ggjcx);
		ll_pz_p = (LinearLayout) findViewById(R.id.ll_pz_p);
		ll_pz = (LinearLayout) findViewById(R.id.ll_pz);
		
		ll_jssj_p = (LinearLayout) findViewById(R.id.ll_jssj_p);
		ll_jssj = (LinearLayout) findViewById(R.id.ll_jssj);

		ll_sngjcx_p = (LinearLayout) findViewById(R.id.ll_sngjcx_p);
		ll_sngjcx = (LinearLayout) findViewById(R.id.ll_sngjcx);
		ll_hwgjcx_p = (LinearLayout) findViewById(R.id.ll_hwgjcx_p);
		ll_hwgjcx = (LinearLayout) findViewById(R.id.ll_hwgjcx);

		ll_wbggjcx_p = (LinearLayout) findViewById(R.id.ll_wbggjcx_p);
		ll_wbggjcx = (LinearLayout) findViewById(R.id.ll_wbggjcx);
		ll_wbtjggaq_p = (LinearLayout) findViewById(R.id.ll_wbtjggaq_p);
		ll_wbtjggaq = (LinearLayout) findViewById(R.id.ll_wbtjggaq);
		ll_spjc_p = (LinearLayout) findViewById(R.id.ll_spjc_p);
		ll_spjc = (LinearLayout) findViewById(R.id.ll_spjc);
		ll_sx = (LinearLayout) findViewById(R.id.ll_sx);

		ll_s_p = (LinearLayout) findViewById(R.id.ll_s_p);
		ll_s = (LinearLayout) findViewById(R.id.ll_s);

		ll_pz_13 = (LinearLayout) findViewById(R.id.ll_pz_13);

		tv_pz_1 = (TextView) findViewById(R.id.tv_pz_1);
		tv_pz_2 = (TextView) findViewById(R.id.tv_pz_2);
		tv_pz_3 = (TextView) findViewById(R.id.tv_pz_3);
		tv_pz_4 = (TextView) findViewById(R.id.tv_pz_4);
		tv_pz_5 = (TextView) findViewById(R.id.tv_pz_5);
		tv_pz_6 = (TextView) findViewById(R.id.tv_pz_6);
		tv_pz_7 = (TextView) findViewById(R.id.tv_pz_7);
		tv_pz_8 = (TextView) findViewById(R.id.tv_pz_8);
		tv_pz_9 = (TextView) findViewById(R.id.tv_pz_9);
		tv_pz_10 = (TextView) findViewById(R.id.tv_pz_10);
		tv_pz_11 = (TextView) findViewById(R.id.tv_pz_11);
		tv_pz_12 = (TextView) findViewById(R.id.tv_pz_12);
		tv_pz_13 = (TextView) findViewById(R.id.tv_pz_13);

		image_1 = (ImageView) findViewById(R.id.image_1);
		image_2 = (ImageView) findViewById(R.id.image_2);
		image_3 = (ImageView) findViewById(R.id.image_3);
		image_4 = (ImageView) findViewById(R.id.image_4);
		image_5 = (ImageView) findViewById(R.id.image_5);
		image_6 = (ImageView) findViewById(R.id.image_6);
		image_7 = (ImageView) findViewById(R.id.image_7);
		image_8 = (ImageView) findViewById(R.id.image_8);
		image_9 = (ImageView) findViewById(R.id.image_9);
		image_10 = (ImageView) findViewById(R.id.image_10);
		image_11 = (ImageView) findViewById(R.id.image_11);
		image_12 = (ImageView) findViewById(R.id.image_12);
		image_13 = (ImageView) findViewById(R.id.image_13);
		et_1 = (EditText) findViewById(R.id.et_1);
		et_2 = (EditText) findViewById(R.id.et_2);
		et_3 = (EditText) findViewById(R.id.et_3);
		et_4 = (EditText) findViewById(R.id.et_4);
		et_5 = (EditText) findViewById(R.id.et_5);
		et_6 = (EditText) findViewById(R.id.et_6);
		et_7 = (EditText) findViewById(R.id.et_7);
		et_8 = (EditText) findViewById(R.id.et_8);
		et_9 = (EditText) findViewById(R.id.et_9);
		et_10 = (EditText) findViewById(R.id.et_10);
		et_11 = (EditText) findViewById(R.id.et_11);
		et_12 = (EditText) findViewById(R.id.et_12);
		et_13 = (EditText) findViewById(R.id.et_13);

		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };

		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata()
				.get(ServiceReportCache.getIndex());

		addXxdz(itemmap.get("xxdz").toString());
		addWbggjcx();
		addWbtjggaq();
		addSpjc();
		addS();
		addSngjcx();
		addHwgjcx();
		addGgjcx();

		zbh = itemmap.get("zbh").toString();
		sbbm = itemmap.get("sbbh").toString();
		fbf = itemmap.get("fbf").toString();
		wdbm = itemmap.get("wdbm").toString();
		gzid = itemmap.get("zzddh").toString();
		((TextView) findViewById(R.id.tv_zbh)).setText(zbh);
		((TextView) findViewById(R.id.tv_sf)).setText(itemmap.get("dqmc")
				.toString());
		((TextView) findViewById(R.id.tv_ds)).setText(itemmap.get("ds")
				.toString());
		((TextView) findViewById(R.id.tv_qx)).setText(itemmap.get("khjgmc")
				.toString());
		((TextView) findViewById(R.id.tv_xqmc)).setText(itemmap.get("wlsjmc")
				.toString());
		((TextView) findViewById(R.id.tv_xjr)).setText(DataCache.getinition()
				.getUsername());

		List list = new ArrayList<String>();
		list.add("1");
		list.add("2");
		list.add("3");
		list.add("4");
		list.add("5");
		list.add("6");
		list.add("7");
		list.add("8");
		list.add("9");
		list.add("10");
		ArrayAdapter adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_fgsl.setAdapter(adapter);

	}

	@Override
	protected void initListeners() {
		//
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.ll_gdxx_p:
					if (ll_gdxx.getVisibility() == View.GONE) {
						ll_gdxx.setVisibility(View.VISIBLE);
					} else {
						ll_gdxx.setVisibility(View.GONE);
					}
					break;
				case R.id.ll_ggjcx_p:
					if (ll_ggjcx.getVisibility() == View.GONE) {
						ll_ggjcx.setVisibility(View.VISIBLE);
					} else {
						ll_ggjcx.setVisibility(View.GONE);
					}
					break;
				case R.id.ll_pz_p:
					if (ll_pz.getVisibility() == View.GONE) {
						ll_pz.setVisibility(View.VISIBLE);
					} else {
						ll_pz.setVisibility(View.GONE);
					}
					break;
				case R.id.ll_sngjcx_p:
					if (ll_sngjcx.getVisibility() == View.GONE) {
						ll_sngjcx.setVisibility(View.VISIBLE);
					} else {
						ll_sngjcx.setVisibility(View.GONE);
					}
					break;
				case R.id.ll_hwgjcx_p:
					if (ll_hwgjcx.getVisibility() == View.GONE) {
						ll_hwgjcx.setVisibility(View.VISIBLE);
					} else {
						ll_hwgjcx.setVisibility(View.GONE);
					}
					break;
				case R.id.ll_wbggjcx_p:
					if (ll_wbggjcx.getVisibility() == View.GONE) {
						ll_wbggjcx.setVisibility(View.VISIBLE);
					} else {
						ll_wbggjcx.setVisibility(View.GONE);
					}
					break;
				case R.id.ll_spjc_p:
					if (ll_spjc.getVisibility() == View.GONE) {
						ll_spjc.setVisibility(View.VISIBLE);
					} else {
						ll_spjc.setVisibility(View.GONE);
					}
					break;
				case R.id.ll_wbtjggaq_p:
					if (ll_wbtjggaq.getVisibility() == View.GONE) {
						ll_wbtjggaq.setVisibility(View.VISIBLE);
					} else {
						ll_wbtjggaq.setVisibility(View.GONE);
					}
					break;
				case R.id.ll_s_p:
					if (ll_s.getVisibility() == View.GONE) {
						ll_s.setVisibility(View.VISIBLE);
					} else {
						ll_s.setVisibility(View.GONE);
					}
					break;
				case R.id.ll_jssj_p:
					if (ll_jssj.getVisibility() == View.GONE) {
						ll_jssj.setVisibility(View.VISIBLE);
					} else {
						ll_jssj.setVisibility(View.GONE);
					}
					break;
				case R.id.et_xjkssj:
					showTimeSelector(et_xjkssj);
					break;
				case R.id.et_xjjssj:
					showTimeSelector(et_xjjssj);
					break;
				case R.id.confirm:

					if (isNotNull(et_gzid)) {
						if (!gzid.trim().equals(et_gzid.getText().toString())) {
							toastShowMessage("柜子ID不正确，请录入正确的ID");
							return;
						}
					} else {
						toastShowMessage("请录入柜子ID");
						return;
					}

					for (int i = 0; i < ll_xxdz.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_xxdz.getChildAt(i);
						if (ll.getChildAt(1) instanceof LinearLayout) {
							ll = (LinearLayout) ll.getChildAt(1);
							if (ll.getChildAt(0) instanceof RadioGroup) {
								RadioGroup rg = (RadioGroup) ll.getChildAt(0);
								if (rg.getCheckedRadioButtonId() == -1) {
									dialogShowMessage_P("请选择实际放置地与任务的地点是否一致",
											null);
									return;
								}
							} else if (ll.getChildAt(0) instanceof EditText) {
								EditText et = (EditText) ll.getChildAt(0);
								if (!isNotNull(et)) {
									dialogShowMessage_P("请录入详细地址", null);
									return;
								}
							}

						}
					}

					if (!isNotNull(et_xjkssj)) {
						toastShowMessage("请录入巡检开始时间");
						return;
					}

					for (int i = 0; i < ll_sx.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_sx.getChildAt(i);
						EditText et = (EditText) ll.findViewById(R.id.et_sx);
						if (!isNotNull(et)) {
							toastShowMessage("请录入锁芯编号");
							return;
						}
						RadioGroup rg = (RadioGroup) ll.findViewById(R.id.rg_0);
						if (rg.getCheckedRadioButtonId() == -1) {
							dialogShowMessage_P("请选择钥匙编号", null);
							return;
						}
					}

					for (int i = 0; i < ll_wbggjcx.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_wbggjcx
								.getChildAt(i);
						RadioGroup rg = (RadioGroup) ll.findViewById(R.id.rg_0);
						if (rg.getCheckedRadioButtonId() == -1) {
							dialogShowMessage_P("外部公共检查项-各项信息不能为空", null);
							return;
						}
					}

					for (int i = 0; i < ll_wbtjggaq.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_wbtjggaq
								.getChildAt(i);
						if (ll.getChildAt(1) instanceof LinearLayout) {
							ll = (LinearLayout) ll.getChildAt(1);
							if (ll.getChildAt(0) instanceof RadioGroup) {
								RadioGroup rg = (RadioGroup) ll.getChildAt(0);
								if (rg.getCheckedRadioButtonId() == -1) {
									dialogShowMessage_P(
											"外部条件公共安全-各项信息不能为空，请选择", null);
									return;
								}
							} else if (ll.getChildAt(0) instanceof EditText) {
//								EditText et = (EditText) ll.getChildAt(0);
//								String tag = et.getTag().toString();
//								if (!isNotNull(et)) {
//									dialogShowMessage_P("外部条件公共安全-" + tag
//											+ "不能为空，请录入", null);
//									return;
//								}
							}
						}
					}

					for (int i = 0; i < ll_spjc.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_spjc.getChildAt(i);
						if (ll.getChildAt(1) instanceof LinearLayout) {
							ll = (LinearLayout) ll.getChildAt(1);
							if (ll.getChildAt(0) instanceof RadioGroup) {
								RadioGroup rg = (RadioGroup) ll.getChildAt(0);
								if (rg.getCheckedRadioButtonId() == -1) {
									dialogShowMessage_P("水平检查-各项信息不能为空，请选择",
											null);
									return;
								}
							} else if (ll.getChildAt(0) instanceof EditText) {
								EditText et = (EditText) ll.getChildAt(0);
								String tag = et.getTag().toString();
								boolean f = false;
								if (tag.indexOf("需整改内容和要求") == -1
										&& tag.indexOf("未加装原因") == -1) {
									f = true;
								}
								if (!isNotNull(et) && f) {
									dialogShowMessage_P("水平检查-" + tag
											+ "不能为空，请录入", null);
									return;
								}
							}

						}
					}

					if ("001".equals(kdg_type) || "003".equals(kdg_type)) {
						for (int i = 0; i < ll_sngjcx.getChildCount(); i++) {
							LinearLayout ll = (LinearLayout) ll_sngjcx
									.getChildAt(i);
							if (ll.getChildAt(1) instanceof LinearLayout) {
								ll = (LinearLayout) ll.getChildAt(1);
								if (ll.getChildAt(0) instanceof RadioGroup) {
									RadioGroup rg = (RadioGroup) ll
											.getChildAt(0);
									if (rg.getCheckedRadioButtonId() == -1) {
										dialogShowMessage_P(
												"室内检查项-各项信息不能为空，请选择", null);
										return;
									}
								} else if (ll.getChildAt(0) instanceof EditText) {
									EditText et = (EditText) ll.getChildAt(0);
									String tag = et.getTag().toString();
									boolean f = false;
									if (tag.indexOf("需整改内容和要求") == -1
											&& tag.indexOf("未加装原因") == -1) {
										f = true;
									}
									if (!isNotNull(et) && f) {
										dialogShowMessage_P("室内检查项-" + tag
												+ "不能为空，请录入", null);
										return;
									}
								}

							}
						}
					} else {
						for (int i = 0; i < ll_hwgjcx.getChildCount(); i++) {
							LinearLayout ll = (LinearLayout) ll_hwgjcx
									.getChildAt(i);
							if (ll.getChildAt(1) instanceof LinearLayout) {
								ll = (LinearLayout) ll.getChildAt(1);
								if (ll.getChildAt(0) instanceof RadioGroup) {
									RadioGroup rg = (RadioGroup) ll
											.getChildAt(0);
									if (rg.getCheckedRadioButtonId() == -1) {
										dialogShowMessage_P(
												"户外检查项-各项信息不能为空，请选择", null);
										return;
									}
								} else if (ll.getChildAt(0) instanceof EditText) {
									EditText et = (EditText) ll.getChildAt(0);
									String tag = et.getTag().toString();
									boolean f = false;
									if (tag.indexOf("需整改内容和要求") == -1
											&& tag.indexOf("未加固说明") == -1
											&& tag.indexOf("不稳固说明") == -1) {
										f = true;
									}
									if (!isNotNull(et) && f) {
										dialogShowMessage_P("户外检查项-" + tag
												+ "不能为空，请录入", null);
										return;
									}
								}

							}
						}
					}

					for (int i = 0; i < ll_s.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_s.getChildAt(i);
						if (ll.getChildAt(1) instanceof LinearLayout) {
							ll = (LinearLayout) ll.getChildAt(1);
							if (ll.getChildAt(0) instanceof RadioGroup) {
								RadioGroup rg = (RadioGroup) ll.getChildAt(0);
								if (rg.getCheckedRadioButtonId() == -1) {
									dialogShowMessage_P("电子锁整改-各项信息不能为空，请选择",
											null);
									return;
								}
							}
						}

					}

					for (int i = 0; i < ll_ggjcx.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_ggjcx.getChildAt(i);
						if (ll.getChildAt(1) instanceof LinearLayout) {
							ll = (LinearLayout) ll.getChildAt(1);
							if (ll.getChildAt(0) instanceof RadioGroup) {
								RadioGroup rg = (RadioGroup) ll.getChildAt(0);
								if (rg.getCheckedRadioButtonId() == -1) {
									dialogShowMessage_P("公共检查项-各项信息不能为空，请选择",
											null);
									return;
								}
							} else if (ll.getChildAt(0) instanceof EditText) {
								EditText et = (EditText) ll.getChildAt(0);
								String tag = et.getTag().toString();
								boolean f = false;
								if (tag.indexOf("需整改内容和要求") == -1
										&& tag.indexOf("未升级说明") == -1
										&& tag.indexOf("备注") == -1) {
									f = true;
								}
								if (!isNotNull(et) && f) {
									dialogShowMessage_P("公共检查项-" + tag
											+ "不能为空，请录入", null);
									return;
								}
							}

						}
					}
					
					if (!isNotNull(et_xjjssj)) {
						toastShowMessage("巡检结束-巡检结束时间不能为空，请录入");
						return;
					}
					
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {

						@Override
						public void run() {
							// upload();
							getWebService("submit");
						}
					});
					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);
		ll_gdxx_p.setOnClickListener(onClickListener);
		ll_ggjcx_p.setOnClickListener(onClickListener);
		ll_pz_p.setOnClickListener(onClickListener);
		ll_sngjcx_p.setOnClickListener(onClickListener);
		ll_hwgjcx_p.setOnClickListener(onClickListener);
		ll_s_p.setOnClickListener(onClickListener);

		ll_wbggjcx_p.setOnClickListener(onClickListener);
		ll_wbtjggaq_p.setOnClickListener(onClickListener);
		ll_spjc_p.setOnClickListener(onClickListener);
		ll_jssj_p.setOnClickListener(onClickListener);
		et_xjkssj.setOnClickListener(onClickListener);
		et_xjjssj.setOnClickListener(onClickListener);

		et_gzid.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					if (!gzid.trim().equals(et_gzid.getText().toString())) {
						toastShowMessage("柜子ID不正确，请录入正确的ID");
					}
				}
			}
		});

		spinner_fgsl.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				addSx(position + 1);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		et_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photonum = 1;
				camera();
			}
		});

		et_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photonum = 2;
				camera();
			}
		});

		et_3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photonum = 3;
				camera();
			}
		});

		et_4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photonum = 4;
				camera();
			}
		});

		et_5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photonum = 5;
				camera();
			}
		});

		et_6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photonum = 6;
				camera();
			}
		});

		et_7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photonum = 7;
				camera();
			}
		});

		et_8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photonum = 8;
				camera();
			}
		});

		et_9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photonum = 9;
				camera();
			}
		});

		et_10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photonum = 10;
				camera();
			}
		});

		et_11.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photonum = 11;
				camera();
			}
		});

		et_12.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photonum = 12;
				camera();
			}
		});

		et_13.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				photonum = 13;
				camera();
			}
		});

	}

	private void camera() {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
			toastShowMessage("没有储存卡，不能拍照");
			return;
		}
		filename = String.valueOf(System.currentTimeMillis()).trim()
				.substring(4);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), filename + ".jpg")));
		startActivityForResult(intent, 1);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			// 读pic
			String filepath = Environment.getExternalStorageDirectory()
					.toString() + "/" + filename + ".jpg";
			try {
				File file = new File(filepath);
				if (file == null) {
					Toast.makeText(getApplicationContext(), "图片不存在！", 1).show();
					return;
				}
				// 压缩图片到100K
				ImageUtil.compressAndGenImage(convertBitmap(file, 480),
						filepath, 200);
				if (photonum == 1) {
					photo_file1 = new File(filepath);
					image_1.setImageBitmap(convertBitmap(photo_file1, 150));
					image_1.setVisibility(View.VISIBLE);
					et_1.setText("重新拍照");
				} else if (photonum == 2) {
					photo_file2 = new File(filepath);
					image_2.setImageBitmap(convertBitmap(photo_file2, 150));
					image_2.setVisibility(View.VISIBLE);
					et_2.setText("重新拍照");
				} else if (photonum == 3) {
					photo_file3 = new File(filepath);
					image_3.setImageBitmap(convertBitmap(photo_file3, 150));
					image_3.setVisibility(View.VISIBLE);
					et_3.setText("重新拍照");
				} else if (photonum == 4) {
					photo_file4 = new File(filepath);
					image_4.setImageBitmap(convertBitmap(photo_file4, 150));
					image_4.setVisibility(View.VISIBLE);
					et_4.setText("重新拍照");
				} else if (photonum == 5) {
					photo_file5 = new File(filepath);
					image_5.setImageBitmap(convertBitmap(photo_file5, 150));
					image_5.setVisibility(View.VISIBLE);
					et_5.setText("重新拍照");
				} else if (photonum == 6) {
					photo_file6 = new File(filepath);
					image_6.setImageBitmap(convertBitmap(photo_file6, 150));
					image_6.setVisibility(View.VISIBLE);
					et_6.setText("重新拍照");
				} else if (photonum == 7) {
					photo_file7 = new File(filepath);
					image_7.setImageBitmap(convertBitmap(photo_file7, 150));
					image_7.setVisibility(View.VISIBLE);
					et_7.setText("重新拍照");
				} else if (photonum == 8) {
					photo_file8 = new File(filepath);
					image_8.setImageBitmap(convertBitmap(photo_file8, 150));
					image_8.setVisibility(View.VISIBLE);
					et_8.setText("重新拍照");
				} else if (photonum == 9) {
					photo_file9 = new File(filepath);
					image_9.setImageBitmap(convertBitmap(photo_file9, 150));
					image_9.setVisibility(View.VISIBLE);
					et_9.setText("重新拍照");
				} else if (photonum == 10) {
					photo_file10 = new File(filepath);
					image_10.setImageBitmap(convertBitmap(photo_file10, 150));
					image_10.setVisibility(View.VISIBLE);
					et_10.setText("重新拍照");
				} else if (photonum == 11) {
					photo_file11 = new File(filepath);
					image_11.setImageBitmap(convertBitmap(photo_file11, 150));
					image_11.setVisibility(View.VISIBLE);
					et_11.setText("重新拍照");
				} else if (photonum == 12) {
					photo_file12 = new File(filepath);
					image_12.setImageBitmap(convertBitmap(photo_file12, 150));
					image_12.setVisibility(View.VISIBLE);
					et_12.setText("重新拍照");
				} else if (photonum == 13) {
					photo_file13 = new File(filepath);
					image_13.setImageBitmap(convertBitmap(photo_file13, 150));
					image_13.setVisibility(View.VISIBLE);
					et_13.setText("重新拍照");
				}
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "压缩图片尺寸失败，请调低像素后重新拍照",
						1).show();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "压缩图片大小失败，请调低像素后重新拍照",
						1).show();
			}

		}

	}

	@Override
	protected void getWebService(String s) {

		if (s.equals("submit")) {// 提交
			try {
				message = "提交成功！";
				flag = "1";
				if (Integer.parseInt(flag) > 0) {

					String cs = "";

					for (int i = 0; i < ll_xxdz.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_xxdz.getChildAt(i);
						if (ll.getChildAt(1) instanceof LinearLayout) {
							ll = (LinearLayout) ll.getChildAt(1);
							if (ll.getChildAt(0) instanceof RadioGroup) {
								RadioGroup rg = (RadioGroup) ll.getChildAt(0);
								if (rg.getCheckedRadioButtonId() == R.id.rb_1) {
									cs += "1";
								} else if (rg.getCheckedRadioButtonId() == R.id.rb_2) {
									cs += "2";
								}
								cs += "*PAM*";
							} else if (ll.getChildAt(0) instanceof EditText) {
								EditText et = (EditText) ll.getChildAt(0);
								cs += et.getText().toString();
								cs += "*PAM*";
							}

						}
					}

					cs += et_xjkssj.getText().toString();
					cs += "*PAM*";
					cs += et_xjjssj.getText().toString();
					cs += "*PAM*";

					int num = spinner_fgsl.getSelectedItemPosition() + 1;
					cs += num;
					cs += "*PAM*";

					for (int i = 0; i < ll_sx.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_sx.getChildAt(i);
						EditText et = (EditText) ll.findViewById(R.id.et_sx);
						cs += et.getText().toString();
						cs += "*PAM*";
						RadioGroup rg = (RadioGroup) ll.findViewById(R.id.rg_0);
						if (rg.getCheckedRadioButtonId() == R.id.rb_1) {
							cs += "1";
						} else if (rg.getCheckedRadioButtonId() == R.id.rb_2) {
							cs += "2";
						}
						cs += "*PAM*";
					}

					for (int i = 0; i < ll_wbggjcx.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_wbggjcx
								.getChildAt(i);
						if (ll.getChildAt(1) instanceof LinearLayout) {
							ll = (LinearLayout) ll.getChildAt(1);
							RadioGroup rg = (RadioGroup) ll.getChildAt(0);
							if (rg.getCheckedRadioButtonId() == R.id.rb_1) {
								cs += "1";
							} else if (rg.getCheckedRadioButtonId() == R.id.rb_2) {
								cs += "2";
							}
							cs += "*PAM*";
						}

					}

					for (int i = 0; i < ll_wbtjggaq.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_wbtjggaq
								.getChildAt(i);
						if (ll.getChildAt(1) instanceof LinearLayout) {
							ll = (LinearLayout) ll.getChildAt(1);
							if (ll.getChildAt(0) instanceof RadioGroup) {
								RadioGroup rg = (RadioGroup) ll.getChildAt(0);
								if (rg.getCheckedRadioButtonId() == R.id.rb_1) {
									cs += "1";
								} else if (rg.getCheckedRadioButtonId() == R.id.rb_2) {
									cs += "2";
								} else if (rg.getCheckedRadioButtonId() == R.id.rb_3) {
									cs += "3";
								}
								cs += "*PAM*";
							} else if (ll.getChildAt(0) instanceof EditText) {
								EditText et = (EditText) ll.getChildAt(0);
								cs += et.getText().toString();
								cs += "*PAM*";
							}

						}
					}

					for (int i = 0; i < ll_spjc.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_spjc.getChildAt(i);
						if (ll.getChildAt(1) instanceof LinearLayout) {
							ll = (LinearLayout) ll.getChildAt(1);
							if (ll.getChildAt(0) instanceof RadioGroup) {
								RadioGroup rg = (RadioGroup) ll.getChildAt(0);
								if (rg.getCheckedRadioButtonId() == R.id.rb_1) {
									cs += "1";
								} else if (rg.getCheckedRadioButtonId() == R.id.rb_2) {
									cs += "2";
								}
								cs += "*PAM*";
							} else if (ll.getChildAt(0) instanceof EditText) {
								EditText et = (EditText) ll.getChildAt(0);
								cs += et.getText().toString();
								cs += "*PAM*";
							}

						}

					}

					if ("001".equals(kdg_type) || "003".equals(kdg_type)) {
						for (int i = 0; i < ll_sngjcx.getChildCount(); i++) {
							LinearLayout ll = (LinearLayout) ll_sngjcx
									.getChildAt(i);
							if (ll.getChildAt(1) instanceof LinearLayout) {
								ll = (LinearLayout) ll.getChildAt(1);
								if (ll.getChildAt(0) instanceof RadioGroup) {
									RadioGroup rg = (RadioGroup) ll
											.getChildAt(0);
									if (rg.getCheckedRadioButtonId() == R.id.rb_1) {
										cs += "1";
									} else if (rg.getCheckedRadioButtonId() == R.id.rb_2) {
										cs += "2";
									} else if (rg.getCheckedRadioButtonId() == R.id.rb_3) {
										cs += "3";
									}
									cs += "*PAM*";
								} else if (ll.getChildAt(0) instanceof CheckBox) {
									for (int m = 0; m < ll.getChildCount(); m++) {
										CheckBox sb = (CheckBox) ll
												.getChildAt(m);
										if (sb.isChecked()) {
											cs += "1";
										} else {
											cs += "0";
										}
										cs += "*PAM*";
									}
								} else if (ll.getChildAt(0) instanceof EditText) {
									EditText et = (EditText) ll.getChildAt(0);
									cs += et.getText().toString();
									cs += "*PAM*";
								}

							}

						}
					} else {
						for (int i = 0; i < ll_hwgjcx.getChildCount(); i++) {
							LinearLayout ll = (LinearLayout) ll_hwgjcx
									.getChildAt(i);
							if (ll.getChildAt(1) instanceof LinearLayout) {
								ll = (LinearLayout) ll.getChildAt(1);
								if (ll.getChildAt(0) instanceof RadioGroup) {
									RadioGroup rg = (RadioGroup) ll
											.getChildAt(0);
									if (rg.getCheckedRadioButtonId() == R.id.rb_1) {
										cs += "1";
									} else if (rg.getCheckedRadioButtonId() == R.id.rb_2) {
										cs += "2";
									} else if (rg.getCheckedRadioButtonId() == R.id.rb_3) {
										cs += "3";
									}
									cs += "*PAM*";
								} else if (ll.getChildAt(0) instanceof EditText) {
									EditText et = (EditText) ll.getChildAt(0);
									cs += et.getText().toString();
									cs += "*PAM*";
								}

							}

						}
					}

					for (int i = 0; i < ll_s.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_s.getChildAt(i);
						if (ll.getChildAt(1) instanceof LinearLayout) {
							ll = (LinearLayout) ll.getChildAt(1);
							if (ll.getChildAt(0) instanceof RadioGroup) {
								RadioGroup rg = (RadioGroup) ll.getChildAt(0);
								if (rg.getCheckedRadioButtonId() == R.id.rb_1) {
									cs += "1";
								} else if (rg.getCheckedRadioButtonId() == R.id.rb_2) {
									cs += "2";
								} else if (rg.getCheckedRadioButtonId() == R.id.rb_3) {
									cs += "3";
								}
								cs += "*PAM*";
							} else {
								EditText et = (EditText) ll.getChildAt(0);
								cs += et.getText().toString();
								cs += "*PAM*";
							}

						}
					}

					for (int i = 0; i < ll_ggjcx.getChildCount(); i++) {
						LinearLayout ll = (LinearLayout) ll_ggjcx.getChildAt(i);
						if (ll.getChildAt(1) instanceof LinearLayout) {
							ll = (LinearLayout) ll.getChildAt(1);
							if (ll.getChildAt(0) instanceof RadioGroup) {
								RadioGroup rg = (RadioGroup) ll.getChildAt(0);
								if (rg.getCheckedRadioButtonId() == R.id.rb_1) {
									cs += "1";
								} else if (rg.getCheckedRadioButtonId() == R.id.rb_2) {
									cs += "2";
								}
								cs += "*PAM*";
							} else {
								EditText et = (EditText) ll.getChildAt(0);
								cs += et.getText().toString();
								cs += "*PAM*";
							}

						}

					}
					cs = cs.substring(0, cs.length() - 5);

					// 再提交服务报告
					String typeStr = "fwbg";
					String str = zbh + "*PAM*"
							+ DataCache.getinition().getUserId() + "*PAM*"
							+ kdg_type + "*PAM*" + cs;
					JSONObject json = this.callWebserviceImp.getWebServerInfo(
							"c#_PAD_KDG_XJ_ALL", str, typeStr, typeStr,
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

	private void addSx(int num) {
		ll_sx.removeAllViews();
		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_sx, null);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_name.setText("主柜主机柜门");
		ll_sx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_sx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_name.setText("主柜维修条顶部");
		ll_sx.addView(view);

		for (int i = 1; i < num + 1; i++) {
			view = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.include_xj_sx, null);
			tv_name = (TextView) view.findViewById(R.id.tv_name);
			tv_name.setText("副柜左" + i + "维修条顶部");
			ll_sx.addView(view);
		}
	}

	// 详细地址
	private void addXxdz(String xxdz) {
		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		RadioButton rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		RadioButton rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("实际放置地与任务的地点是否一致");
		ll_xxdz.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		EditText et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setText(xxdz);
		et_val.setHint("请录入详细地址");
		et_val.setHintTextColor(R.color.gray);
		tv_name.setText("详细地址");
		ll_xxdz.addView(view);
	}

	// 外部公共检查项
	@SuppressLint("ResourceAsColor")
	private void addWbggjcx() {
		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		RadioButton rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		RadioButton rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("干净");
		rb_2.setText("脏");
		tv_name.setText("屏幕卫生处理");
		ll_wbggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("有");
		rb_2.setText("无");
		tv_name.setText("外观是否有刮痕（如有，需拍照）");
		ll_wbggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("贴纸是否完好（如否，需拍贴纸前后照）");
		ll_wbggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("有");
		rb_2.setText("无");
		tv_name.setText("当地是否有贴纸");
		ll_wbggjcx.addView(view);

		ll_wbggjcx.setVisibility(View.GONE);
	}

	// 外部条件公共安全
	private void addWbtjggaq() {
		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		EditText et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("接地线确认-火零（V）");
		tv_name.setText("接地线确认-火零（V）（需拍火零电压照）");
		ll_wbtjggaq.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("接地线确认-火地（V）");
		tv_name.setText("接地线确认-火地（V）（需拍火地电压照）");
		ll_wbtjggaq.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("接地线确认-零地（V）");
		tv_name.setText("接地线确认-零地（V）（需拍零地电压照）");
		ll_wbtjggaq.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_type_2, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		RadioButton rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		RadioButton rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		RadioButton rb_3 = (RadioButton) view.findViewById(R.id.rb_3);
		rb_1.setText("合格");
		rb_2.setText("不合格");
		rb_3.setText("不能检测");
		tv_name.setText("接地线确认");
		ll_wbtjggaq.addView(view);
		ll_wbtjggaq.setVisibility(View.GONE);
	}

	// 水平检查
	private void addSpjc() {
		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		RadioButton rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		RadioButton rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("水平");
		rb_2.setText("倾斜");
		tv_name.setText("柜体水平（如倾斜，需拍调水平前后对比照）");
		ll_spjc.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("是否需安排上门整改");
		ll_spjc.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		EditText et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("需整改内容和要求");
		tv_name.setText("需整改内容和要求");
		ll_spjc.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("调平");
		rb_2.setText("未调平");
		tv_name.setText("底角调平（如未调平，需拍照）");
		ll_spjc.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("是否需安排上门整改");
		ll_spjc.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("需整改内容和要求");
		tv_name.setText("需整改内容和要求");
		ll_spjc.addView(view);

		ll_spjc.setVisibility(View.GONE);
	}

	@SuppressLint("InflateParams")
	private void addS() {

		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_type_2, null);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		RadioButton rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		RadioButton rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		RadioButton rb_3 = (RadioButton) view.findViewById(R.id.rb_3);
		rb_1.setText("龙源");
		rb_2.setText("圳佳安");
		rb_3.setText("混合");
		tv_name.setText("主柜电子锁类型（需拍照）");
		ll_s.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_type_2, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_3 = (RadioButton) view.findViewById(R.id.rb_3);
		rb_1.setText("龙源");
		rb_2.setText("圳佳安");
		rb_3.setText("混合");
		tv_name.setText("副柜电子锁类型");
		ll_s.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("是否整改了所有的圳佳安电子锁（如是圳佳安，需拍1张照）");
		ll_s.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		EditText et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("未整改的圳佳安电子锁的箱号");
		tv_name.setText("未整改的圳佳安电子锁的箱号");
		ll_s.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("未整改原因");
		tv_name.setText("未整改原因");
		ll_s.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("龙源锁整改是否到位");
		ll_s.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("好");
		rb_2.setText("坏");
		tv_name.setText("门锁（需拍各机柜门状态照多张）");
		ll_s.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("好");
		rb_2.setText("坏");
		tv_name.setText("门锁勾");
		ll_s.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("红外条整改是否到位");
		ll_s.addView(view);

		ll_s.setVisibility(View.GONE);

	}

	@SuppressLint("InflateParams")
	private void addSngjcx() {

		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		RadioButton rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		RadioButton rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("室内柜是否存在倾倒风险（需拍正、左、右、后面照）");
		ll_sngjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("各机柜柜顶间的横向是否水平");
		ll_sngjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("各机柜柜顶间的纵向是否水平");
		ll_sngjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("各机柜的正面是否在一个垂直面上");
		ll_sngjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("是否需安排上门整改");
		ll_sngjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		EditText et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("需整改内容和要求");
		tv_name.setText("需整改内容和要求");
		ll_sngjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_checkbox, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_name.setText("风险说明（如有，需拍倾倒风险照）");
		ll_sngjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("机柜顶部两柜之间，是否安装了最新规格的连接片（需拍柜顶含所有连接片照）");
		ll_sngjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("连接片是否都已上紧");
		ll_sngjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("机柜顶部两柜之间的连接片数");
		et_val.setHint("请录入机柜顶部两柜之间的连接片数");
		et_val.setHintTextColor(R.color.gray);
		tv_name.setText("机柜顶部两柜之间的连接片数");
		ll_sngjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("机柜顶部两柜之间的螺丝数");
		et_val.setHint("请录入机柜顶部两柜之间的螺丝数");
		et_val.setHintTextColor(R.color.gray);
		tv_name.setText("机柜顶部两柜之间的螺丝数");
		ll_sngjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("是否需安排上门整改");
		ll_sngjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("需整改内容和要求");
		tv_name.setText("需整改内容和要求");
		ll_sngjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("未加装原因");
		tv_name.setText("未加装原因");
		ll_sngjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("有");
		rb_2.setText("无");
		tv_name.setText("室内柜链接带照片");
		ll_sngjcx.addView(view);

		ll_sngjcx.setVisibility(View.GONE);

	}

	@SuppressLint("InflateParams")
	private void addHwgjcx() {

		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_type_2, null);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		RadioButton rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		RadioButton rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		RadioButton rb_3 = (RadioButton) view.findViewById(R.id.rb_3);
		rb_1.setText("靠墙");
		rb_2.setText("不靠墙");
		rb_3.setText("靠半墙");
		tv_name.setText("户外柜安装方式（如靠墙，需拍拉卷尺参考照）");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("户外柜地面固定是否稳固（如不稳，需拍平台风险照）");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		EditText et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("不稳固说明");
		tv_name.setText("不稳固说明");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("是否需安排上门整改");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("需整改内容和要求");
		tv_name.setText("需整改内容和要求");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("前部是否有对地加固L型固定件（需拍正、左、右、后对地加固细节照共4张）");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("左部是否有对地加固L型固定件");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("右部是否有对地加固L型固定件");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("后部是否有对地加固L型固定件");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("后部L型固定件个数（个）");
		et_val.setHint("请录入后部L型固定件个数（个）");
		et_val.setHintTextColor(R.color.gray);
		tv_name.setText("后部L型固定件个数（个）");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("是否需安排上门整改");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("需整改内容和要求");
		tv_name.setText("需整改内容和要求");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("对地加固的膨胀螺丝的总个数");
		et_val.setHint("请录入对地加固的膨胀螺丝的总个数");
		et_val.setHintTextColor(R.color.gray);
		tv_name.setText("户外柜对地加固的膨胀螺丝的总个数");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("紧固");
		rb_2.setText("否");
		tv_name.setText("所有的膨胀螺丝是否紧固，是否已拉紧");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("牢固无风险");
		rb_2.setText("不牢固有风险");
		tv_name.setText("地基平台是否牢固，有无塌方风险");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("是否需安排上门整改");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("需整改内容和要求");
		tv_name.setText("需整改内容和要求");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("未加固说明");
		tv_name.setText("未加固说明");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("是否需安排上门整改");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("需整改内容和要求");
		tv_name.setText("需整改内容和要求");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("符合");
		rb_2.setText("不符合");
		tv_name.setText("室外机膨胀螺丝检测和加固及其照片（照片需能反馈所有螺丝、加固前、加固后、未能加固注意原因）");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("所有柜体之间连接螺栓总数（个）");
		et_val.setHint("请录入所有柜体之间连接螺栓总数（个）");
		et_val.setHintTextColor(R.color.gray);
		tv_name.setText("户外柜主副柜之间所有柜体之间连接螺栓总数（个）（一个柜间连接螺丝照一张）");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("户外柜主副柜之间所有螺栓是否都已上紧");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("雨棚是否漏水（需拍每一个雨棚的带垫片的螺栓照）");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("每个雨棚都紧固连接");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("所有雨棚螺栓总数（个）");
		et_val.setHint("请录入所有雨棚螺栓总数（个）");
		et_val.setHintTextColor(R.color.gray);
		tv_name.setText("所有雨棚螺栓总数（个）");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("雨棚螺栓装了的弹簧垫圈数（个）");
		et_val.setHint("请录入雨棚螺栓装了的弹簧垫圈数（个）");
		et_val.setHintTextColor(R.color.gray);
		tv_name.setText("雨棚螺栓装了的弹簧垫圈数（个）");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("雨棚螺栓装了的平垫圈数（个）");
		et_val.setHint("请录入雨棚螺栓装了的平垫圈数（个）");
		et_val.setHintTextColor(R.color.gray);
		tv_name.setText("雨棚螺栓装了的平垫圈数（个）");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("合格");
		rb_2.setText("不合格");
		tv_name.setText("平垫直径必须为30mm");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("是否需安排上门整改");
		ll_hwgjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("需整改内容和要求");
		tv_name.setText("需整改内容和要求");
		ll_hwgjcx.addView(view);

		ll_hwgjcx.setVisibility(View.GONE);

	}

	@SuppressLint("InflateParams")
	private void addGgjcx() {

		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
		RadioButton rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		RadioButton rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("良好");
		rb_2.setText("差");
		tv_name.setText("部品性能测试");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("有");
		rb_2.setText("无");
		tv_name.setText("风扇声音");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("烫手");
		rb_2.setText("低");
		tv_name.setText("主机温度");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("正常");
		rb_2.setText("不正常");
		tv_name.setText("检查主机串口");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("灵敏");
		rb_2.setText("不灵敏");
		tv_name.setText("触摸屏灵敏度");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("触摸屏是否升级（需拍触摸屏版本照共1张）");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		EditText et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("未升级说明");
		tv_name.setText("未升级说明");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("是否需安排上门换触摸屏");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("强");
		rb_2.setText("弱");
		tv_name.setText("路由器检查（需拍路由器状态图照共3张）");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("可看到屏幕和几乎全部柜面");
		rb_2.setText("偏移");
		tv_name.setText("监控摄像位置");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("清晰");
		rb_2.setText("模糊");
		tv_name.setText("摄像头（画面）（需拍左右摄像头监控屏幕照）");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("有盲区");
		rb_2.setText("无盲区");
		tv_name.setText("摄像头（画面）");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("已维修");
		rb_2.setText("未解决");
		tv_name.setText("故障箱门修复（物品检测、箱门开启、限位链修复）");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("正常");
		rb_2.setText("不正常");
		tv_name.setText("扫描枪检测");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("正常");
		rb_2.setText("不正常");
		tv_name.setText("身份证识别器检测");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("已处理");
		rb_2.setText("未处理");
		tv_name.setText("线束处理");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("是否提交巡检照片（需拍巡检报告每页照共4张）");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("共提交了多少张");
		et_val.setHintTextColor(R.color.gray);
		et_val.setHint("请录入共提交了多少张");
		tv_name.setText("共提交了多少张");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("是");
		rb_2.setText("否");
		tv_name.setText("显示屏照片（高清全屏）（需拍显示屏终端号和版本号照共1张）");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("有");
		rb_2.setText("无");
		tv_name.setText("摄像头（画面）（高清全屏）");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("有");
		rb_2.setText("无");
		tv_name.setText("柜子整体照片（前 ）");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("有");
		rb_2.setText("无");
		tv_name.setText("柜子整体照片（后）");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("有");
		rb_2.setText("无");
		tv_name.setText("柜子整体照片（左）");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_aqfx, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		rb_1 = (RadioButton) view.findViewById(R.id.rb_1);
		rb_2 = (RadioButton) view.findViewById(R.id.rb_2);
		rb_1.setText("有");
		rb_2.setText("无");
		tv_name.setText("柜子整体照片（右）");
		ll_ggjcx.addView(view);

		view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.include_xj_text, null);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		et_val = (EditText) view.findViewById(R.id.et_val);
		et_val.setTag("备注");
		tv_name.setText("备注");
		ll_ggjcx.addView(view);

		ll_ggjcx.setVisibility(View.GONE);
	}

	private void loadPzHwg() {
		tv_pz_1.setText("屏幕照（要求能看清终端号和版本号）");
		tv_pz_2.setText("监控画面（要求看得见所有箱子及箱号）");
		tv_pz_3.setText("触摸屏版本照（要求看得清全部序列号）");
		tv_pz_4.setText("设备状态照（全屏）");
		tv_pz_5.setText("正面照（要求能看见柜子的整个正面、雨棚（装饰板要安装）、摄像头以及地脚螺丝）");
		tv_pz_6.setText("左侧照（要看得清膨胀螺丝）");
		tv_pz_7.setText("右侧照（要看得清膨胀螺丝）");
		tv_pz_8.setText("后面照（加装了固定螺丝的要能看得清螺丝）");
		tv_pz_9.setText("两柜之间固定螺丝照（两柜之间共4颗螺丝）");
		tv_pz_10.setText("钥匙照（特指无钥匙部分、锁芯编码）");
		tv_pz_11.setText("人机自拍照（能反馈人的衣着、设备入网的状态）");
		tv_pz_12.setText("照片（巡检手写表格）");
		tv_pz_13.setText("雨棚固定螺丝照片（每片雨棚3颗螺丝）");
	}

	private void loadPzSng() {
		tv_pz_1.setText("屏幕照（要求能看清终端号和版本号）");
		tv_pz_2.setText("监控画面（要求看得见所有箱子及箱号）");
		tv_pz_3.setText("触摸屏版本照（要求看得清全部序列号）");
		tv_pz_4.setText("设备状态照（全屏）");
		tv_pz_5.setText("正面照（要求能看见柜子的整个正面、、摄像头）");
		tv_pz_6.setText("左侧照");
		tv_pz_7.setText("右侧照");
		tv_pz_8.setText("后面照");
		tv_pz_9.setText("两柜之间连接片照");
		tv_pz_10.setText("钥匙照（特指无钥匙部分、锁芯编码）");
		tv_pz_11.setText("人机自拍照（能反馈人的衣着、设备入网的状态）");
		tv_pz_12.setText("照片（巡检手写表格）");
	}

	private void upload() {
		if (photo_file1 == null || photo_file2 == null || photo_file3 == null
				|| photo_file4 == null || photo_file5 == null
				|| photo_file6 == null || photo_file7 == null
				|| photo_file8 == null || photo_file9 == null
				|| photo_file10 == null || photo_file11 == null
				|| photo_file12 == null) {
			if ("002".equals(kdg_type)) {
				if (photo_file13 == null) {
					Message msg = new Message();
					msg.what = 14;
					handler.sendMessage(msg);
					return;
				}
			}
			Message msg = new Message();
			msg.what = 14;
			handler.sendMessage(msg);
			return;
		}
		try {
			boolean flag = uploadPic("1", readJpeg(photo_file1),
					"uf_json_setdata");
			if (flag) {
				flag = uploadPic("2", readJpeg(photo_file2), "uf_json_setdata");
				if (flag) {
					flag = uploadPic("3", readJpeg(photo_file3),
							"uf_json_setdata");
					if (flag) {
						flag = uploadPic("4", readJpeg(photo_file4),
								"uf_json_setdata");
						if (flag) {
							flag = uploadPic("5", readJpeg(photo_file5),
									"uf_json_setdata");
							if (flag) {
								flag = uploadPic("6", readJpeg(photo_file6),
										"uf_json_setdata");
								if (flag) {
									flag = uploadPic("7",
											readJpeg(photo_file7),
											"uf_json_setdata");
									if (flag) {
										flag = uploadPic("8",
												readJpeg(photo_file8),
												"uf_json_setdata");
										if (flag) {
											flag = uploadPic("9",
													readJpeg(photo_file9),
													"uf_json_setdata");
											if (flag) {
												flag = uploadPic("10",
														readJpeg(photo_file10),
														"uf_json_setdata");
												if (flag) {
													flag = uploadPic(
															"11",
															readJpeg(photo_file11),
															"uf_json_setdata");
													if (flag) {
														flag = uploadPic(
																"12",
																readJpeg(photo_file12),
																"uf_json_setdata");
														if (flag) {
															if ("002"
																	.equals(kdg_type)) {
																flag = uploadPic(
																		"13",
																		readJpeg(photo_file13),
																		"uf_json_setdata");
																if (flag) {
																	Message msg = new Message();
																	msg.what = 12;
																	handler.sendMessage(msg);
																} else {
																	Message msg = new Message();
																	msg.what = 13;
																	handler.sendMessage(msg);
																}
															} else {
																Message msg = new Message();
																msg.what = 12;
																handler.sendMessage(msg);
															}
														} else {
															Message msg = new Message();
															msg.what = 13;
															handler.sendMessage(msg);
														}
													} else {
														Message msg = new Message();
														msg.what = 13;
														handler.sendMessage(msg);
													}
												} else {
													Message msg = new Message();
													msg.what = 13;
													handler.sendMessage(msg);
												}
											} else {
												Message msg = new Message();
												msg.what = 13;
												handler.sendMessage(msg);
											}
										} else {
											Message msg = new Message();
											msg.what = 13;
											handler.sendMessage(msg);
										}
									} else {
										Message msg = new Message();
										msg.what = 13;
										handler.sendMessage(msg);
									}
								} else {
									Message msg = new Message();
									msg.what = 13;
									handler.sendMessage(msg);
								}
							} else {
								Message msg = new Message();
								msg.what = 13;
								handler.sendMessage(msg);
							}
						} else {
							Message msg = new Message();
							msg.what = 13;
							handler.sendMessage(msg);
						}
					} else {
						Message msg = new Message();
						msg.what = 13;
						handler.sendMessage(msg);
					}
				} else {
					Message msg = new Message();
					msg.what = 13;
					handler.sendMessage(msg);
				}
			} else {
				Message msg = new Message();
				msg.what = 13;
				handler.sendMessage(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message msg = new Message();
			msg.what = 13;
			handler.sendMessage(msg);
		}

	}

	private boolean uploadPic(final String orderNumbers, final byte[] data1,
			final String methed) throws Exception {

		if (data1 != null && orderNumbers != null) {
			JSONObject json = callWebserviceImp.getWebServerInfo2_pic(
					"c#_PAD_KDG_XJ_CZP", "0001", zbh + "" + orderNumbers,
					"0001", data1, "uf_json_setdata2_p11",
					getApplicationContext());
			String flag = json.getString("flag");
			if ("1".equals(flag)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.FAIL:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;
			case Constant.SUCCESS:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P(message,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			case Constant.NETWORK_ERROR:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case Constant.NUM_6:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				break;
			case Constant.NUM_7:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}

				break;
			case Constant.NUM_8:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				break;
			case Constant.NUM_9:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				message = "设备编码" + sbbm + ",未查询到该设备,请确认二维码是否正确！";
				dialogShowMessage_P(message,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
							}
						});
				break;
			case 11:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("仓位权限错误，请联系管理员", null);
				break;
			case 12:
				Config.getExecutorService().execute(new Runnable() {

					@Override
					public void run() {
						getWebService("submit");
					}
				});
				break;
			case 13:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("提交失败,上传图片失败！", null);
				break;
			case 14:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("请拍照！", null);
				break;
			}

		}
	};

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("currType", 2);
		startActivity(intent);
		finish();
	}

}
