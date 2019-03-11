package com.single.activity.kdg;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.single.activity.FrameActivity;
import com.single.activity.esp.AddParts;
import com.single.activity.esp.AddPartsAdd;
import com.single.activity.esp.ServiceReportsAppraiseActivity;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.common.Constant;
import com.single.gtdzpt.R;
import com.single.utils.Config;
import com.single.utils.ImageUtil;

/**
 * 快递柜-服务报告
 * 
 * @author zdkj
 *
 */
public class FwbgKdg extends FrameActivity {

	private Button confirm, cancel;
	private String flag, zbh, sbbm = "0", fbf, wdbm, sblx, xzpj_str,
			ywlx2, msgStr, djzt, ecsmyy, yysm,_PAD_EPOS_STR,_PAD_SFSCZZBG_STR,currmxh,gzdlbm,gzzlbm,gzxlbm,gdnszt;
	private int bjf,rgf,fyhj,bjf_old,currposition;
	private CheckBox cb_sfhpj;
	private Spinner spinner_gzdl, spinner_gzzl, spinner_gzxl, spinner_smyy;
	private TextView tv_xzpj, tv_fwbgsc, tv_ywlx;
	private EditText et_clgc, et_smyy,et_zsdz;
	private ArrayList<Map<String, String>> data_gzbm, data_2_gzbm, data_3_gzbm, gzbm_2_list, gzbm_3_list, data_smyy,dataYh,dataXz,dataCa;
	private String[] from,from1;
	private int[] to,to1;
	private ArrayList<String> hpdata;
	private ListView listView,listView1;
	private ImageView iv_telphone, iv_telphone_kh;
	private RadioGroup rg_0;
	private Map<String, ArrayList<String>> filemap;
	private Map<String, Object> itemmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_fwbg);
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

		confirm = (Button) findViewById(R.id.include_botton).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botton).findViewById(
				R.id.cancel);
		confirm.setText("确定");
		cancel.setText("取消");

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		tv_ywlx = (TextView) findViewById(R.id.tv_ywlx);
		tv_fwbgsc = (TextView) findViewById(R.id.tv_fwbgsc);
		tv_xzpj = (TextView) findViewById(R.id.tv_xzpj);
		cb_sfhpj = (CheckBox) findViewById(R.id.cb_sfhpj);
		et_clgc = (EditText) findViewById(R.id.et_clgc);
		et_zsdz = (EditText) findViewById(R.id.et_zsdz);
		et_smyy = (EditText) findViewById(R.id.et_smyy);
		rg_0 = (RadioGroup) findViewById(R.id.rg_0);
		iv_telphone_kh = (ImageView) findViewById(R.id.iv_telphone_kh);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		spinner_gzdl = (Spinner) findViewById(R.id.sp_gzlb);
		spinner_gzxl = (Spinner) findViewById(R.id.sp_gzlbb);
		spinner_gzzl = (Spinner) findViewById(R.id.sp_gzzl);
		spinner_smyy = (Spinner) findViewById(R.id.spinner_smyy);
		from = new String[] { "id", "name" };
		to = new int[] { R.id.bm, R.id.name };
		data_gzbm = new ArrayList<Map<String, String>>();
		data_2_gzbm = new ArrayList<Map<String, String>>();
		data_3_gzbm = new ArrayList<Map<String, String>>();
		gzbm_2_list = new ArrayList<Map<String, String>>();
		gzbm_3_list = new ArrayList<Map<String, String>>();
		filemap = new HashMap<String, ArrayList<String>>();
		hpdata = new ArrayList<String>();
		listView = (ListView) findViewById(R.id.listView);
		listView1 = (ListView) findViewById(R.id.listView1);
		from1 = new String[] { "hpmc", "sl","dj","sfhs" };
		to1 = new int[] { R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4 };

		DataCache.getinition().setFilemap(null);
		
		itemmap = ServiceReportCache.getObjectdata().get(
				ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		fbf = itemmap.get("fbf").toString();
		djzt = itemmap.get("djzt").toString();
		ywlx2 = itemmap.get("ywlx2").toString();
		zbh = itemmap.get("zbh").toString();
		sblx = itemmap.get("sblx").toString();
		ywlx2 = itemmap.get("ywlx2").toString();
		ecsmyy = itemmap.get("ecsmyy").toString();
		yysm = itemmap.get("yysm").toString();
		gzdlbm = itemmap.get("kzzf3").toString();
		gzzlbm = itemmap.get("kzzf4").toString();
		gzxlbm = itemmap.get("kzzf5").toString();
		gdnszt = itemmap.get("gdnszt").toString();
		
		rgf = "".equals(itemmap.get("rgf").toString())?0:Integer.parseInt(itemmap.get("rgf").toString());
		bjf_old = "".equals(itemmap.get("bjf").toString())?0:Integer.parseInt(itemmap.get("bjf").toString());
		fyhj = "".equals(itemmap.get("fyhj").toString())?0:Integer.parseInt(itemmap.get("fyhj").toString());
		
		
		((TextView) findViewById(R.id.tv_zbh)).setText(itemmap.get("zbh")
				.toString());
		((TextView) findViewById(R.id.tv_smsf)).setText(itemmap.get("smsf")
				.toString());
		((TextView) findViewById(R.id.tv_sf)).setText(itemmap.get("sf")
				.toString()+ecsmyy);
		((TextView) findViewById(R.id.tv_ds)).setText(itemmap.get("sx")
				.toString());
		((TextView) findViewById(R.id.tv_qx)).setText(itemmap.get("qy")
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
		((TextView) findViewById(R.id.tv_btgyy)).setText(itemmap.get("btgyy")
				.toString());
		((TextView) findViewById(R.id.tv_bz)).setText(itemmap.get("bz")
				.toString());
		((TextView) findViewById(R.id.tv_rgf)).setText(rgf+"");
		((TextView) findViewById(R.id.tv_bjf)).setText(bjf_old+"");
		((TextView) findViewById(R.id.tv_fyhj)).setText(fyhj+"");
		et_clgc.setText(itemmap.get("clgc").toString());
		if ("3.6".equals(djzt)||"4".equals(djzt)||"5".equals(djzt)) {
			rg_0.getChildAt(0).setEnabled(false);
			rg_0.getChildAt(1).setEnabled(false);
			findViewById(R.id.ll_ecsm).setVisibility(View.GONE);
			findViewById(R.id.ll_ecsm_content).setVisibility(View.GONE);
		}
		
		if("4".equals(djzt)){
			if(!"2".equals(gdnszt)){
				findViewById(R.id.ll_zp).setVisibility(View.GONE);
				findViewById(R.id.ll_zp_line).setVisibility(View.GONE);
			}
		}

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
		//
		OnClickListener backonClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.confirm:
					boolean flag = true;
					if ("3.5".equals(djzt)) {
						if (rg_0.getCheckedRadioButtonId() == -1) {
							toastShowMessage("请选择是否二次上门！");
							return;
						}
						if (rg_0.getCheckedRadioButtonId() == R.id.rb_1) {
							flag = false;
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
							Config.getExecutorService().execute(new Runnable() {

								@Override
								public void run() {
									getWebService("submitSm");
								}
							});
						}
					}
					
					if(flag){
						String f = ((Map<String, String>) spinner_gzxl
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
						if("".equals(et_clgc.getText().toString().trim())){
							flag = false;
							dialogShowMessage_P("请录入故障处理过程", null);
							return;
						}
//						if (cb_sfhpj.isChecked() && flag) {
//							if ("".equals(tv_xzpj.getText().toString())) {
//								flag = false;
//								dialogShowMessage_P("请新增配件", null);
//								return;
//							}
//						}
						
						if ("1".equals(_PAD_SFSCZZBG_STR)) {
							if (_PAD_EPOS_STR.equals(sblx)) {
								if ("1".equals(ywlx2)// 巡检20160727陈总要求 巡检要上传照片
										|| "3".equals(ywlx2)// 安装
										|| "5".equals(ywlx2)) {// 撤机
									if(filemap==null||filemap.size()==0){
										dialogShowMessage_P("请选择照片", null);
										return;
									}
								}
							}

						}
						
//						if(filemap.size()!=0){
//							Config.getExecutorService().execute(new Runnable() {
//
//								@Override
//								public void run() {
//									try {
//										ArrayList<String> list = filemap.get("1");
//										for(int i=0;i<list.size();i++){
//											String filepath = list.get(i);
//											filepath = filepath.substring(7, filepath.length());
//											ImageUtil.compressAndGenImage(ImageUtil.ratio(filepath,getScreenWidth(),getScreenHeight()),filepath, 200);
//											File file = new File(filepath);
//											boolean fileflag = uploadPic(zbh, readJpeg(file),"uf_json_setdata2_p1");
//											System.out.println("json:"+fileflag);
//										}
//									} catch (Exception e) {
//										e.printStackTrace();
//									}
//								}
//							});
//						}

						if("4".equals(djzt)||"5".equals(djzt)){
							showProgressDialog();
							Config.getExecutorService().execute(new Runnable() {

								@Override
								public void run() {
									getWebService("submitXg");
								}
							});
						}else{
							Intent intent = new Intent(getApplicationContext(),
									ServiceReportsAppraiseActivity.class);
							intent.putExtra("zbh", itemmap.get("zbh").toString());
							intent.putExtra("sf", itemmap.get("sf").toString());
							intent.putExtra("sx", itemmap.get("sx").toString());
							intent.putExtra("qy", itemmap.get("qy").toString());
							intent.putExtra("xqmc", itemmap.get("xqmc").toString());
							intent.putExtra("khxxdz", itemmap.get("xxdz").toString());
							intent.putExtra("kzzd5mc", itemmap.get("ywlx").toString());
							intent.putExtra("bz", itemmap.get("bz").toString());
							intent.putExtra("bzsj", itemmap.get("bzsj").toString());
							intent.putExtra("slsj", itemmap.get("slsj").toString());
							intent.putExtra("zsdz", et_zsdz.getText().toString());
							
							intent.putExtra("gzlbid",(spinner_gzdl.getSelectedItem() == null ? "": ((Map<String, String>) spinner_gzdl.getSelectedItem()).get("id")));
							intent.putExtra("gzlbname",(spinner_gzdl.getSelectedItem() == null ? "": ((Map<String, String>) spinner_gzdl.getSelectedItem()).get("name")));
							intent.putExtra("gzzlid",(spinner_gzzl.getSelectedItem() == null ? "": ((Map<String, String>) spinner_gzzl.getSelectedItem()).get("id")));
							intent.putExtra("gzzlname",(spinner_gzzl.getSelectedItem() == null ? "": ((Map<String, String>) spinner_gzzl.getSelectedItem()).get("name")));
							intent.putExtra("gzlbbid",(spinner_gzxl.getSelectedItem() == null ? "": ((Map<String, String>) spinner_gzxl.getSelectedItem()).get("id")));
							intent.putExtra("gzlbbname",(spinner_gzxl.getSelectedItem() == null ? "": ((Map<String, String>) spinner_gzxl.getSelectedItem()).get("name")));
							intent.putExtra("clgc", et_clgc.getText().toString());
							intent.putExtra("sfhj", hpdata.size()>1?"是":"否");
							intent.putExtra("hpdata", hpdata);
							intent.putExtra("kzzd17", itemmap.get("kzzd17").toString());
							intent.putExtra("ddsj", itemmap.get("ddsj").toString());
							intent.putExtra("djzt", djzt);
							
							DataCache.getinition().setFilemap(filemap);
							
							startActivity(intent);
						}
						
					}
					break;

				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);

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

				SimpleAdapter adapter = new SimpleAdapter(FwbgKdg.this,
						gzbm_2_list, R.layout.spinner_item, from, to);
				spinner_gzzl.setAdapter(adapter);
				
				if (gzzlbm != null) {
					for (int i = 0; i < gzbm_2_list.size(); i++) {
						map = gzbm_2_list.get(i);
						if (gzzlbm.equals(map.get("id"))) {
							spinner_gzzl.setSelection(i);
							gzzlbm = null;
							break;
						}
					}
				}
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

				SimpleAdapter adapter = new SimpleAdapter(FwbgKdg.this,
						gzbm_3_list, R.layout.spinner_item, from, to);
				spinner_gzxl.setAdapter(adapter);
				
				if (gzxlbm != null) {
					for (int i = 0; i < gzbm_3_list.size(); i++) {
						map = gzbm_3_list.get(i);
						if (gzxlbm.equals(map.get("id"))) {
							spinner_gzxl.setSelection(i);
							gzxlbm = null;
							break;
						}
					}
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		};

		spinner_gzdl.setOnItemSelectedListener(onItemSelectedListener_gzlb);// 故障类别
		spinner_gzzl.setOnItemSelectedListener(onItemSelectedListener_gzzl);// 故障类别
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
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
		if (requestCode == 3 && resultCode == 1 && data != null) {
			ArrayList<Map<String, String>> list = (ArrayList<Map<String, String>>) data
					.getSerializableExtra("list");
			Map<String, String> m1 = list.get(0);
			boolean fl = false;
			String hpmcStr = "";
			for(int i=0;i<dataXz.size();i++){
				Map<String, String> m0 = dataXz.get(i);
				if(m0.get("hpbm").equals(m1.get("hpbm"))){
					hpmcStr = m1.get("hpmc");
					fl = true;
				}
			}
			if(fl){
				toastShowMessage("已选择"+hpmcStr+",不能重复选择");
				return;
			}else{
				dataXz.add(dataXz.size() - 1, m1);
				bjf = bjf_old;
				for(int i=0;i<dataXz.size()-1;i++){
					Map<String, String> m = dataXz.get(i);
					bjf+=Integer.parseInt(m.get("sl"))*Integer.parseInt(m.get("dj"));
				}
				fyhj = bjf+rgf;
				hpdata = new ArrayList<String>();
				for(int i=0;i<dataXz.size();i++){
					Map<String, String> m = dataXz.get(i);
					int fy = Integer.parseInt(m.get("sl"))*Integer.parseInt(m.get("dj"));
					String d = m.get("hpbm")+","+m.get("hpmc")+","+m.get("sl")+","+m.get("dj")+","+fy+","+rgf+","+fyhj;
					hpdata.add(d);
				}
				Message msg = new Message();
				msg.what = Constant.NUM_6;
				handler.sendMessage(msg);
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

	@Override
	protected void getWebService(String s) {
		if (s.equals("query")) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SBGZLB", sblx, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					JSONArray jsonArray = jsonObject.getJSONArray("tableA");
					Map<String, String> item = new HashMap<String, String>();
					item.put("id", "     ");
					item.put("name", " ");
					data_gzbm.add(item);
					data_2_gzbm.add(item);
					data_3_gzbm.add(item);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
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
					jsonObject = callWebserviceImp.getWebServerInfo("_PAD_EPOS", "1", "uf_json_getdata", this);
					if (Integer.parseInt(jsonObject.getString("flag")) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						JSONObject temp = jsonArray.getJSONObject(0);
						_PAD_EPOS_STR = temp.getString("cs");
					}
					
					jsonObject = callWebserviceImp.getWebServerInfo("_PAD_SFSCZZBG", sblx, "uf_json_getdata", this);
					if (Integer.parseInt(jsonObject.getString("flag")) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						JSONObject temp = jsonArray.getJSONObject(0);
						_PAD_SFSCZZBG_STR = temp.getString("ssjg");
					}
					
					dataXz = new ArrayList<Map<String, String>>();
					dataYh = new ArrayList<Map<String, String>>();
					
					jsonObject = callWebserviceImp.getWebServerInfo("_PAD_FWBG_CX6", zbh, "uf_json_getdata", this);
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							item = new HashMap<String, String>();
							item.put("hpmc", temp.getString("hpmc"));
							item.put("sl", temp.getString("sl"));
							item.put("dj", temp.getString("dj"));
							item.put("sfhs", "");
							item.put("rgf", temp.getString("rgf"));
							item.put("fyhj", temp.getString("fyhj"));
							
							dataYh.add(item);
						}
					}
					Map<String, String> map = new HashMap<String, String>();
					map.put("hpbm", "");
					map.put("hpmc", "");
					map.put("sl", "0");
					map.put("dj", "0");
					map.put("sfhs", "");
					dataXz.add(map);
					
					dataCa = new ArrayList<Map<String, String>>();
					jsonObject = callWebserviceImp.getWebServerInfo(
							"_PAD_FWBG_CX4", "", "uf_json_getdata",
							getApplicationContext());
					flag = jsonObject.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						jsonArray = jsonObject.getJSONArray("tableA");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject temp = jsonArray.getJSONObject(i);
							item = new HashMap<String, String>();
							item.put("id", temp.getString("hpbm"));
							item.put("name", temp.getString("hpmc"));
							item.put("zbts", temp.getString("zbts"));
							//item.put("sfhs_bm", temp.getString("sfhs_bm"));
							dataCa.add(item);
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
						jsonArray = jsonObject.getJSONArray("tableA");
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
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				} else {
					msgStr = "获取故障信息失败";
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

		if (s.equals("submitXg")) {// 修改提交
			try {
				try {
					String sfhj = hpdata.size()>1?"1":"0";
					String str = zbh + "*PAM*"
							+ (spinner_gzdl.getSelectedItem() == null ? "": ((Map<String, String>) spinner_gzdl.getSelectedItem()).get("id")) + "*PAM*"
							+ (spinner_gzzl.getSelectedItem() == null ? "": ((Map<String, String>) spinner_gzzl.getSelectedItem()).get("id")) + "*PAM*"
							+ (spinner_gzxl.getSelectedItem() == null ? "": ((Map<String, String>) spinner_gzxl.getSelectedItem()).get("id")) + "*PAM*"
							+ et_clgc.getText().toString() + "*PAM*" + sfhj;
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

					str = str + xzpj_str + "*PAM*" + "" + "*PAM*"
							+ "" + "*PAM*" + "" + "*PAM*"
							+ "" + "*PAM*" + "" + "*PAM*" + ""
							+ "*PAM*" + "" + "*PAM*"
							+ "";

					JSONObject jsonobject = this.callWebserviceImp
							.getWebServerInfo("2#_PAD_FWBG_XG", str, DataCache
									.getinition().getUserId(), zbh,
									"uf_json_setdata2", this);

					this.flag = jsonobject.getString("flag");

					Log.e("flag", flag);
					int flags = Integer.parseInt(this.flag);
					if (flags > 0) {
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
						msgStr = "提交成功";
						Message msg = new Message();
						msg.what = Constant.SUBMIT_SUCCESS;
						this.handler.sendMessage(msg);
					} else {
						msgStr = "提交失败";
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
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = Constant.NETWORK_ERROR;
				handler.sendMessage(msg);
			}

		}

		if (s.equals("submitSm")) {// 二次上门提交
			try {
				String cs = zbh
						+ "*PAM*"
						+ data_smyy.get(spinner_smyy.getSelectedItemPosition())
								.get("id") + "*PAM*"
						+ et_smyy.getText().toString();
				JSONObject json = callWebserviceImp.getWebServerInfo(
						"2#_PAD_KDG_SQECSM", cs, "0000", "1",
						"uf_json_setdata2", getApplicationContext());
				flag = json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					msgStr = "提交成功";
					Message msg = new Message();
					msg.what = Constant.SUBMIT_SUCCESS;
					handler.sendMessage(msg);
				} else {
					msgStr = "提交失败";
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

	// private void upload() {
	// try {
	// boolean flag = uploadPic("", readJpeg(photo_file1),
	// "uf_json_setdata");
	// if (flag) {
	// flag = uploadPic("", readJpeg(photo_file2), "uf_json_setdata");
	// if (flag) {
	// Message msg = new Message();
	// msg.what = 12;
	// handler.sendMessage(msg);
	// } else {
	// Message msg = new Message();
	// msg.what = 13;
	// handler.sendMessage(msg);
	// }
	// } else {
	// Message msg = new Message();
	// msg.what = 13;
	// handler.sendMessage(msg);
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// Message msg = new Message();
	// msg.what = 13;
	// handler.sendMessage(msg);
	// }
	//
	// }

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

	private void loadEcsmyy() {
		try {
			rg_0.check(R.id.rb_1);
			et_smyy.setText(yysm);
			for(int i=0;i<data_smyy.size();i++){
				if(ecsmyy.equals(data_smyy.get(i).get("id"))){
					spinner_smyy.setSelection(i);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private class MyAdapter extends SimpleAdapter {

		public MyAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
			iv_close.setVisibility(View.VISIBLE);
			if (position == dataXz.size() - 1) {
				iv_close.setImageResource(R.drawable.add);
				iv_close.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Map<String, String> map = dataXz.get(position);
						ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
						list.add(map);
						Intent intent = new Intent(getApplicationContext(),
								AddPartsAdd.class);
						intent.putExtra("data", list);
						intent.putExtra("bjlb", dataCa);
						intent.putExtra("fbf", fbf);
						startActivityForResult(intent, 3);
					}
				});
			}else{
				iv_close.setImageResource(R.drawable.close);
				iv_close.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						dataXz.remove(position);
						
						if(dataXz.size()==0){
							Map<String, String> map = new HashMap<String, String>();
							map.put("hpbm", "");
							map.put("hpmc", "");
							map.put("sl", "0");
							map.put("dj", "0");
							map.put("sfhs", "");
							dataXz.add(map);
						}
						bjf = bjf_old;
						for(int i=0;i<dataXz.size()-1;i++){
							Map<String, String> m = dataXz.get(i);
							bjf+=Integer.parseInt(m.get("sl"))*Integer.parseInt(m.get("dj"));
						}
						fyhj = rgf+bjf;
						hpdata = new ArrayList<String>();
						for(int i=0;i<dataXz.size();i++){
							Map<String, String> m = dataXz.get(i);
							int fy = Integer.parseInt(m.get("sl"))*Integer.parseInt(m.get("dj"));
							String d = m.get("hpbm")+","+m.get("hpmc")+","+m.get("sl")+","+m.get("dj")+","+fy+","+rgf+","+fyhj;
							hpdata.add(d);
						}
						Message msg = new Message();
						msg.what = Constant.NUM_6;
						handler.sendMessage(msg);
					}
				});
			}
			
			return view;
		}

	}
	
	private class MyYhpjAdapter extends SimpleAdapter {

		public MyYhpjAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
			//iv_close.setVisibility(View.VISIBLE);
			iv_close.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					currmxh = dataYh.get(position).get("mxh");
					currposition = position;
					
					dialogShowMessage("是否删除该配件？",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									showProgressDialog();
									Config.getExecutorService().execute(new Runnable() {

										@Override
										public void run() {
											getWebService("delpj");
										}
									});
								}
							}, null);
				}
			});
			return view;
		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.FAIL:
				dialogShowMessage_P("失败，" + msgStr, null);
				break;
			case Constant.SUCCESS:
				SimpleAdapter adapter = new SimpleAdapter(FwbgKdg.this,
						data_gzbm, R.layout.spinner_item, from, to);
				spinner_gzdl.setAdapter(adapter);
				if(gzdlbm!=null){
					for (int i = 0; i < data_gzbm.size(); i++) {
						Map<String, String> map = data_gzbm.get(i);
						if (gzdlbm.equals(map.get("id"))) {
							spinner_gzdl.setSelection(i);
							gzdlbm=null;
							break;
						}
					}
				}
				

				adapter = new SimpleAdapter(FwbgKdg.this, data_smyy,
						R.layout.spinner_item, from, to);
				spinner_smyy.setAdapter(adapter);
				
				adapter = new MyAdapter(getApplicationContext(), dataXz,
						R.layout.listview_item_addpj, from1, to1);
				listView.setAdapter(adapter);
				
				adapter = new MyYhpjAdapter(getApplicationContext(), dataYh,
						R.layout.listview_item_addpj, from1, to1);
				listView1.setAdapter(adapter);
				
				if(dataYh.size()==0){
					findViewById(R.id.ll_yhpj_title).setVisibility(View.GONE);
					findViewById(R.id.ll_yhpj_content).setVisibility(View.GONE);
				}
				
				if ("3.6".equals(djzt)) {
					loadEcsmyy();
				}
				break;
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			case Constant.SUBMIT_SUCCESS:
				dialogShowMessage_P(msgStr,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								onBackPressed();
							}
						});
				break;
			case Constant.NUM_6:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				adapter = new MyAdapter(getApplicationContext(), dataXz,
						R.layout.listview_item_addpj, from1, to1);
				listView.setAdapter(adapter);
				((TextView) findViewById(R.id.tv_rgf)).setText(rgf+"");
				((TextView) findViewById(R.id.tv_bjf)).setText(bjf+"");
				((TextView) findViewById(R.id.tv_fyhj)).setText(fyhj+"");
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
				dialogShowMessage_P("提交失败,上传图片失败！", null);
				break;
			case 14:
				dialogShowMessage_P("请拍照！", null);
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
		intent.putExtra("currType", 2);
		startActivity(intent);
		finish();
	}

}
