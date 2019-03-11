package com.single.activity.kdg;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kobjects.util.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

import com.single.activity.FrameActivity;
import com.single.activity.js.FyqrCompleteActivity;
import com.single.activity.js.LiuShuiZhanglist;
import com.single.activity.main.MainActivity;
import com.single.activity.w.Pqzgjk;
import com.single.activity.w.YjgdActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.gtdzpt.R;
import com.single.utils.Config;
import com.single.utils.DataUtil;

/**
 * 快递柜-通用List
 * 
 * @author zdkj
 *
 */
public class ListKdg extends FrameActivity {

	private String flag;
	private ListView listView;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> data;
	private String[] from;
	private int[] to;
	private int queryType = DataCache.getinition().getQueryType(),
			currType = 2;
	private String cs, status = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_dispatchinginformationreceiving);
		initVariable();
		initView();
		initListeners();
		if (!backboolean) {
			showProgressDialog();
		}
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {

				getWebService("query");
			}
		});

	}

	@Override
	protected void initVariable() {

		listView = (ListView) findViewById(R.id.listView);
		
		from = new String[] { "textView1", "faultuser", "zbh", "timemy",
				"datemy", "ztzt" };
		to = new int[] { R.id.textView1, R.id.yytmy, R.id.pgdhmy, R.id.timemy,
				R.id.datemy, R.id.ztzt };

	}

	@Override
	protected void initView() {

		title.setText("工单查询");
	}

	@Override
	protected void initListeners() {
		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;

				}
			}
		};

		topBack.setOnClickListener(onClickListener);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int arg2,
					long id) {

				ServiceReportCache.setIndex(arg2);
				if (arg2 >= 0) {
					Intent intent = null;
					if (queryType == 201) {
						intent = new Intent(ListKdg.this, JdxyKdg.class);
					} else if (queryType == 203) {
						intent = new Intent(ListKdg.this, SmdwKdg.class);
					} else if (queryType == 204) {
						intent = new Intent(ListKdg.this, FwbgKdg.class);
					} else if (queryType == 2041) {
						intent = new Intent(ListKdg.this, FwbgKdg.class);
					} else if (queryType == 2042) {
						intent = new Intent(ListKdg.this, FwbgKdg.class);
					} else if (queryType == 205) {
						intent = new Intent(ListKdg.this, JqgdcxShowKdg.class);
					} else if (queryType == 208) {
						intent = new Intent(ListKdg.this, JdxyXj.class);
					} else if (queryType == 209) {
						intent = new Intent(ListKdg.this, SmdwXj.class);
					} else if (queryType == 210) {
						intent = new Intent(ListKdg.this, FwbgXj.class);
					} else if (queryType == 2101) {
						intent = new Intent(ListKdg.this, DhjcKdg.class);
					} else if (queryType == 2204) {
						intent = new Intent(ListKdg.this, ZzzpKdg.class);
					} else if (queryType == 2205) {
						intent = new Intent(ListKdg.this, ZzzpCxKdg.class);
					} else if (queryType == 2501) {
						intent = new Intent(ListKdg.this, Pqzgjk.class);
					} else if (queryType == 2601) {
						intent = new Intent(ListKdg.this,
								LiuShuiZhanglist.class);
					} else if (queryType == 206) {
						intent = new Intent(ListKdg.this, YjgdActivity.class);
					}
					startActivityForResult(intent, 1);
				}

			}
		});

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			showProgressDialog();
			Config.getExecutorService().execute(new Runnable() {

				@Override
				public void run() {

					getWebService("query");
				}
			});
		}
	}

	@Override
	protected void getWebService(String s) {

		if ("query".equals(s) && !backboolean) {
			try {
				String sqlid = "";
				data = new ArrayList<Map<String, Object>>();
				int queryType = DataCache.getinition().getQueryType();
				String userid = DataCache.getinition().getUserId();
				cs = DataCache.getinition().getUserId();
				if (queryType == 201) {
					sqlid = "_PAD_JDXY_CX1";
					cs = "2000-01-01*3000-01-01*" + userid + "*" + userid;
					currType = 2;
					DataCache.getinition().setTitle("接单响应");
				} else if (queryType == 202) {
					sqlid = "";
				} else if (queryType == 203) {
					currType = 2;
					sqlid = "_PAD_SHGL_PGDCZ";
					cs = "2000-01-01*3000-01-01*" + 3 + "*" + userid + "*"
							+ userid;
					DataCache.getinition().setTitle("上门定位");
				} else if (queryType == 204) {
					currType = 2;
					sqlid = "_PAD_FWBG_CX1";
					cs = "2018-07-01*3000-01-01*" + userid + "*" + userid;
					DataCache.getinition().setTitle("服务报告");
				} else if (queryType == 2041) {
					currType = 2;
					sqlid = "_PAD_FWBG_ECSMGD";
					cs = "2018-07-01*3000-01-01*" + userid + "*" + userid;
					DataCache.getinition().setTitle("二次上门");
				} else if (queryType == 2042) {
					currType = 2;
					sqlid = "_PAD_FWBG_CX7";
					cs = "2018-07-01*3000-01-01*" + userid + "*" + userid;
					DataCache.getinition().setTitle("服务报告修改");
				} else if (queryType == 205) {
					sqlid = "_PAD_KDG_JQGDCX";
					status = getIntent().getStringExtra("status");
					if ("1".equals(status)) { // 已完工
						cs = DataCache.getinition().getUserId() + "*6*7*8";
					} else {// 未完工
						cs = DataCache.getinition().getUserId() + "*3*4*5";
					}
				} else if (queryType == 208) {
					sqlid = "_PAD_KDG_XJ_JDXY";
				} else if (queryType == 209) {
					sqlid = "_PAD_KDG_XJ_SWDW";
				} else if (queryType == 210) {
					sqlid = "_PAD_KDG_XJ_FWBG";
				} else if (queryType == 2101) {
					sqlid = "_PAD_KDG_DHJC";
				} else if (queryType == 2204) {
					currType = 5;
					cs = DataCache.getinition().getUserId();
					sqlid = "_PAD__ESP_ZZZP";
				} else if (queryType == 2205) {
					currType = 5;
					String start = getIntent().getStringExtra("start");
					;
					String end = getIntent().getStringExtra("end");
					String status = getIntent().getStringExtra("status");
					cs = DataCache.getinition().getUserId() + "*" + start + "*"
							+ end + "*" + status + "*" + status;
					sqlid = "_PAD_ESP_GXPQ_GDMX";
				} else if (queryType == 2501) {
					currType = 5;
					int status = getIntent().getIntExtra("status", 1);
					int sj = getIntent().getIntExtra("sj", 1);
					if (status == 1) {// 已完工
						sqlid = "_PAD_KDG_ZZPQGD_YWG";
						if (sj == 1) {// 本月
							cs = userid + "*" + userid + "*" + userid + "*0*0";
						} else {// 上月
							cs = userid + "*" + userid + "*" + userid
									+ "*-1*-1";
						}

					} else {// 未完工
						sqlid = "_PAD_KDG_ZZPQGD_WWG";
						cs = userid + "*" + userid + "*" + userid;
					}

				} else if (queryType == 2601) {
					currType = 3;
					int sj = getIntent().getIntExtra("sj", 1);
					sqlid = "_PAD_JSGL_YXYSFY1";
					if (sj == 1) {// 本月
						cs = 0 + "*" + 0 + "*" + userid + "*" + 0 + "*" + 0
								+ "*" + userid;
					} else {// 上月
						cs = -1 + "*" + -1 + "*" + userid + "*" + -1 + "*" + -1
								+ "*" + userid;
					}

				} else if (queryType == 206) {
					currType = 5;
					sqlid = "_PAD_SHGL_YWCX_GDYJCX";
					cs = userid + "*" + userid + "*" + userid;
				}

				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						sqlid, cs, "uf_json_getdata", this);
				flag = jsonObject.getString("flag");

				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				if (Integer.parseInt(flag) > 0) {
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject temp = jsonArray.getJSONObject(i);
						Map<String, Object> item = new HashMap<String, Object>();
						String timeff = "";
						item.put("textView1", getListItemIcon(i));

						if (queryType == 201 || queryType == 203
								|| queryType == 204 || queryType == 2041 || queryType == 2042) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc")
									+ "   " + temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("sf", temp.getString("sf"));
							item.put("ds", temp.getString("ds"));
							item.put("fwnr", temp.getString("fwnr"));
							item.put("khbm", temp.getString("khbm"));
							item.put("sflx", temp.getString("sflx"));
							item.put("ywhy", temp.getString("ywhy"));
							item.put("sblx", temp.getString("sblx"));
							item.put("sblx_mc", temp.getString("sblx_mc"));

							item.put("sbxh", temp.getString("sbxh"));
							item.put("wxts", temp.getString("wxts"));
							item.put("khlxr", temp.getString("khlxr"));
							item.put("yysj", temp.getString("yysj"));
							item.put("ddsj", temp.getString("ddsj"));
							item.put("wgsj", temp.getString("wgsj"));
							item.put("slsj", temp.getString("slsj"));
							item.put("lxdh", temp.getString("lxdh"));
							item.put("dygdh", temp.getString("dygdh"));
							item.put("sfecgd", temp.getString("sfecgd"));
							item.put("sbewm", temp.getString("sbewm"));

							item.put("cx", temp.getString("cx"));
							item.put("fbf", temp.getString("fbf"));
							item.put("khbm", temp.getString("khbm"));
							item.put("smsf", temp.getString("smsf"));
							item.put("zxsxm", temp.getString("zxsxm"));
							item.put("zxsdh", temp.getString("zxsdh"));
						}
						if(queryType == 201 || queryType == 203){
							item.put("jjcd", temp.getString("fwjjcd"));
						}
						if (queryType == 204 || queryType == 2041 || queryType == 2042) {
							item.put("ecsmyy", temp.getString("ecsmyy"));
							item.put("yysm", temp.getString("yysm"));
							item.put("kzzd17", temp.getString("kzzd17"));
							item.put("rgf", temp.getString("rgf"));
							item.put("bjf", temp.getString("bjf"));
							item.put("fyhj", temp.getString("fyhj"));
							item.put("kzzf3", temp.getString("kzzf3"));
							item.put("kzzf4", temp.getString("kzzf4"));
							item.put("kzzf5", temp.getString("kzzf5"));
							item.put("clgc", temp.getString("clgc"));
							item.put("btgyy", temp.getString("btgyy"));
							item.put("gdnszt", temp.getString("gdnszt"));
							item.put("sftxcsyy", "2".equals(temp.getString("gdnszt"))?"1":"0");
							
						}

						if (queryType == 208 || queryType == 209
								|| queryType == 210) {
							timeff = temp.getString("shgl_xjdzb_bzsj");
							item.put("bzsj", timeff);
							item.put("hplbmc", temp.getString("hplbmc"));
							item.put("zdrq", temp.getString("shgl_xjdzb_zdrq"));
							item.put("ksrq", temp.getString("shgl_xjdzb_ksrq"));
							item.put("jsrq", temp.getString("shgl_xjdzb_jsrq"));
							item.put("dqmc", temp.getString("dqmc"));
							item.put("ds", temp.getString("ds"));
							item.put("qx", temp.getString("qx"));
							item.put("wlsjmc", temp.getString("wlsjmc"));
							item.put("khjgmc", temp.getString("khjgmc"));
							item.put("lxr", temp.getString("lxr"));
							item.put("lxdh", temp.getString("lxdh"));
							item.put("sbbh", temp.getString("sbbh"));
							item.put("fgsl", temp.getString("fgsl"));
							item.put("zgsl", temp.getString("zgsl"));
							item.put("zgllx", temp.getString("zgllx"));
							item.put("fgllx", temp.getString("fgllx"));
							item.put("zbh", temp.getString("zbh"));
							item.put("jbf", temp.getString("jbf"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("zzddh", temp.getString("zzddh"));
							item.put("faultuser", temp.getString("wlsjmc"));

							item.put("timemy", temp.getString("ywlx"));
							item.put("datemy", temp.getString("djzt2"));
						}
						if (queryType == 210) {
							item.put("fbf", temp.getString("fbf"));//
							item.put("wdbm", temp.getString("wdbm"));//
						}

						if (queryType == 2101) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("ztzt",
									"1".equals(temp.getString("ztzt")) ? "（暂停）"
											: "");
							item.put("ddh", temp.getString("ddh"));
							item.put("axdh", temp.getString("axdh"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("jbf", temp.getString("jbf"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("sblx", temp.getString("sblx"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ds", temp.getString("ds"));
							item.put("ssqx", temp.getString("ssqx"));
							item.put("zgsl", temp.getString("zgsl"));
							item.put("fgsl", temp.getString("fgsl"));
							item.put("fhrq", temp.getString("fhrq"));
							item.put("dhrq", temp.getString("dhrq"));
							item.put("jcrq", temp.getString("yjjcrq"));
							item.put("xm", temp.getString("ccgl_ygb_xm"));
							item.put("zbh", temp.getString("zbh"));
							item.put("lxdh", temp.getString("ccgl_ygb_lxdh"));
							item.put("faultuser", temp.getString("xqmc"));
						}
						if (queryType == 2204) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sf", temp.getString("sf"));
							item.put("qx", temp.getString("qx"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("bz", temp.getString("bz"));
							item.put("khbm", temp.getString("khbm"));
							item.put("sblx", temp.getString("sblx"));
							item.put("khlxr", temp.getString("lxr"));
							item.put("lxdh", temp.getString("lxdh"));
							item.put("smsf", temp.getString("smsf"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("cx", temp.getString("cx"));
							item.put("zxsxm", temp.getString("zxsxm"));
							item.put("zxsdh", temp.getString("zxsdh"));
							item.put("jjcd", temp.getString("fwjjcd"));
						}

						if (queryType == 2205) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xxdz"));

							item.put("zbh", temp.getString("zbh"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("djzt", temp.getString("djzt"));
							item.put("pgdx", temp.getString("pgdx"));
							item.put("jbflxdh", temp.getString("jbflxdh"));
							item.put("fbf", temp.getString("fbf"));
							item.put("bzrlxdh", temp.getString("bzrlxdh"));
							item.put("dqmc", temp.getString("dqmc"));
							item.put("wdmc", temp.getString("wdmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("clfs", temp.getString("clfs"));
							item.put("bz", temp.getString("bz"));
							item.put("pgsj", temp.getString("pgsj"));
							item.put("slsj", temp.getString("slsj"));
							item.put("wcsj", temp.getString("wcsj"));
						}
						if (queryType == 2501) {
							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("ywlx2", temp.getString("ywlx2"));
							item.put("zdh", temp.getString("zdh"));
							item.put("bz", temp.getString("bz"));
							item.put("jbf", temp.getString("jbf"));
							item.put("jbfdh", temp.getString("jbfdh"));
							item.put("bzrlxdh", temp.getString("bzrlxdh"));
							item.put("sfcs", temp.getString("sfcs"));
							item.put("ysbs", temp.getString("ysbs"));
							item.put("csyy", temp.getString("khcsyy"));
							item.put("csnr", temp.getString("csnr"));
							item.put("scsj", temp.getString("scsj"));
							item.put("ddsj", temp.getString("ddsj"));
							item.put("sfwzzm", temp.getString("sfwzzm"));
							item.put("csyybm", temp.getString("csyybm"));
							item.put("wwgcsyy", temp.getString("wwgcsyy"));
							item.put("wwgbz", temp.getString("wwgbz"));
							item.put("sftxcsyy", temp.getString("sftxcsyy"));
							item.put("jjcd", temp.getString("fwjjcd"));
						}

						if (queryType == 2601) {
							timeff = temp.getString("wcsj");
							item.put("bzsj", temp.getString("bzsj"));
							item.put("faultuser", temp.getString("bzr"));
							item.put("zbh", temp.getString("zbh"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("bzr", temp.getString("bzr"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("wcsj", temp.getString("wcsj"));
							item.put("pgdx", temp.getString("pgdx"));
							item.put("dqfydj", temp.getString("dqfydj"));
							item.put("jg", temp.getString("jg"));
							item.put("fy3", temp.getString("fy3"));
							item.put("fy4", temp.getString("fy4"));
							item.put("fy5", temp.getString("fy5"));
							item.put("fy6", temp.getString("fy6"));
							item.put("fy2", temp.getString("fy2"));
							item.put("tsfysm", temp.getString("tsfysm"));
							item.put("t16_jg", temp.getString("t16_jg"));
							item.put("zj", temp.getString("zj"));
						}
						if (queryType == 206) {

							timeff = temp.getString("bzsj");
							item.put("bzsj", timeff);
							item.put("faultuser", temp.getString("xqmc")
									+ "   " + temp.getString("xxdz"));
							item.put("zbh", temp.getString("zbh"));
							item.put("sx", temp.getString("sx"));
							item.put("ds", temp.getString("ds"));
							item.put("qy", temp.getString("qy"));
							item.put("djzt", temp.getString("djzt"));
							item.put("xqmc", temp.getString("xqmc"));
							item.put("xxdz", temp.getString("xxdz"));
							item.put("gzxx", temp.getString("gzxx"));
							item.put("ywlx", temp.getString("ywlx"));
							item.put("bz", temp.getString("bz"));
							item.put("khlxr", temp.getString("khlxr"));
							item.put("lxdh", temp.getString("lxdh"));
							item.put("jbf", temp.getString("jbr"));
							item.put("sjhm", temp.getString("sjhm"));
							item.put("kzzf7", temp.getString("clgc"));
							item.put("sblx", temp.getString("sblx"));
							item.put("sbxh", "");
							item.put("ywhy", "");
							item.put("sflx", "");
							item.put("kzzf1", temp.getString("sbxlh"));
							item.put("tsyjnr", temp.getString("tsyjnr"));
							item.put("hfsm", "");
							item.put("ddsj", temp.getString("ddsj"));
							item.put("wcsj", temp.getString("wcsj"));
							item.put("hfpjsj", temp.getString("hfsj"));

						}
						if (timeff == null || "".equals(timeff)) {
							item.put("timemy", "");// 时间
							item.put("datemy", "");// 年月日
						} else {
							timeff = timeff.substring(2);
							item.put("timemy", mdateformat(1, timeff));// 时间
							item.put("datemy", mdateformat(0, timeff));// 年月日
						}
						if (queryType == 2601) {
							item.put("timemy", temp.getString("zj") + "（元）");
						}
						data.add(item);
					}
					ServiceReportCache.setObjectdata(data);
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
				Message msg = new Message();
				msg.what = NETWORK_ERROR;// 网络不通
				handler.sendMessage(msg);
			}
		} else {
			Message msg = new Message();
			msg.what = FAIL;
			handler.sendMessage(msg);
		}

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("currType", currType);
		startActivity(intent);
		finish();
	}

	private class CurrAdapter extends SimpleAdapter {

		public CurrAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);

		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {
			final View view = super.getView(position, convertView, parent);
			try {
				Map<String, Object> item = data.get(position);
				String sfcs = (String) item.get("sftxcsyy");
				if ("1".equals(sfcs)) {
					view.setBackgroundResource(R.color.red);
				} else {
					view.setBackgroundResource(R.color.white);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return view;
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case NETWORK_ERROR:
				dialogShowMessage_P("网络连接出错，请检查你的网络设置", null);
				break;
			case SUCCESSFUL:
				adapter = new CurrAdapter(ListKdg.this,
						ServiceReportCache.getObjectdata(),
						R.layout.listview_dispatchinginformationreceiving_item,
						from, to);
				listView.setAdapter(adapter);
				break;
			case FAIL:
				dialogShowMessage_P("你查询的时间段内，没有数据",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								onBackPressed();
							}
						});
				break;
			}
			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};

}
