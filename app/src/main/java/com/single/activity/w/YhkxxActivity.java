package com.single.activity.w;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.single.activity.FrameActivity;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.common.Constant;
import com.single.gtdzpt.R;
import com.single.utils.Config;
import com.single.utils.IDCard;
import com.single.utils.ImageUtil;

public class YhkxxActivity extends FrameActivity {

	private Spinner spinner_yh;
	private TextView tv_yykmb, tv_yhkpz,tv_yhkggzm;
	private EditText et_khm, et_yhkh, et_fh, et_zh, et_flc;
	private String filename, zbh, flag,xb;
	private int photonum = 1,type=0;
	private LinearLayout ll_yykggzm;
	private Button confirm, cancel;
	private TextView tv_curr;
	private Map<String, ArrayList<String>> filemap;

	private List list;
	private List<Map<String, String>> list_data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_yhk);
		initVariable();
		initView();
		initListeners();
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				getWebService("getyk");
			}
		});
	}

	@Override
	protected void initVariable() {

	}

	@Override
	protected void initView() {

		spinner_yh = (Spinner) findViewById(R.id.spinner_yh);
		tv_yykmb = (TextView) findViewById(R.id.tv_yykmb);
		et_khm = (EditText) findViewById(R.id.et_khm);
		et_fh = (EditText) findViewById(R.id.et_fh);
		et_zh = (EditText) findViewById(R.id.et_zh);
		et_flc = (EditText) findViewById(R.id.et_flc);
		et_yhkh = (EditText) findViewById(R.id.et_yhkh);
		tv_yhkpz = (TextView) findViewById(R.id.tv_yhkpz);
		ll_yykggzm = (LinearLayout) findViewById(R.id.ll_yykggzm);
		tv_yhkggzm = (TextView) findViewById(R.id.tv_yhkggzm);
		filemap = new HashMap<String, ArrayList<String>>();

		confirm = (Button) findViewById(R.id.confirm);
		cancel = (Button) findViewById(R.id.cancel);

		title.setText("银行卡信息完善");
		list = new ArrayList();
		list_data = new ArrayList<Map<String, String>>();
		Intent intent = getIntent();
		type = intent.getIntExtra("type", 0);
		if(DataCache.getinition().isHasYHK()){
			ll_yykggzm.setVisibility(View.VISIBLE);
			tv_yykmb.setVisibility(View.VISIBLE);
			tv_yykmb.setText(Html.fromHtml("<u>"+"银行卡更改证明模板"+"</u>"));
		}else{
			ll_yykggzm.setVisibility(View.GONE);
			tv_yykmb.setVisibility(View.GONE);
		}
		
		et_khm.setText(intent.getStringExtra("khm"));
		et_yhkh.setText(intent.getStringExtra("yhkh"));
		xb = intent.getStringExtra("xb");
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
				case R.id.confirm:
					addYhk();
					break;
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.tv_yhkpz:
					tv_yhkpz.setTag("1");
					tv_curr = tv_yhkpz;
					ArrayList<String> list = filemap.get("1");
					camera(1, list);
					break;
				case R.id.tv_yhkggzm:
					tv_yhkggzm.setTag("2");
					tv_curr = tv_yhkggzm;
					ArrayList<String> list1 = filemap.get("2");
					camera(2, list1);
					break;
				case R.id.tv_yykmb:
					Intent intent = new Intent(YhkxxActivity.this,YHKMbActivity.class);
					intent.putExtra(xb, xb);
					startActivity(intent);
					break;
				}
			}
		};
		topBack.setOnClickListener(onClickListener);
		confirm.setOnClickListener(onClickListener);
		cancel.setOnClickListener(onClickListener);
		tv_yhkpz.setOnClickListener(onClickListener);
		tv_yhkggzm.setOnClickListener(onClickListener);
		tv_yykmb.setOnClickListener(onClickListener);

		et_yhkh.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					String yhkh = et_yhkh.getText().toString();
					String yhkh_new = "";
					if (!"".equals(yhkh)) {
						yhkh = yhkh.replace(" ", "");
						if (IDCard.isNumeric(yhkh)) {
							for (int i = 0; i < yhkh.length(); i++) {
								if (i % 4 == 0) {
									yhkh_new += " ";
								}
								yhkh_new += yhkh.charAt(i);
							}
							yhkh_new = yhkh_new.substring(1, yhkh_new.length());
							et_yhkh.setText(yhkh_new);
						} else {
							et_yhkh.setText("");
							Toast.makeText(getApplicationContext(), "银行卡号不正确",
									1).show();
						}

					}
				}

			}
		});
	}

	@Override
	protected void getWebService(String s) {
		if (s.equals("getyk")) {
			try {
				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_PAD_SJ_KHYHMC", "", "uf_json_getdata", this);
				JSONArray jsonArray = jsonObject.getJSONArray("tableA");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", obj.getString("zbh"));
					map.put("name", obj.getString("yhmc"));
					list_data.add(map);
					list.add(obj.getString("yhmc"));
				}
				Message msg = new Message();
				msg.what = 1;//
				handler.sendMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
				Message msg = new Message();
				msg.what = -1;//
				handler.sendMessage(msg);
			}
		}

		if (s.equals("yhk_back")) {
			try {
				String sql = "update shgl_ywgl_fwbgszb set kzzd4=1 where zbh='"
						+ zbh + "'";
				Log.e("dd", sql.toString());

				JSONObject jsonObject = callWebserviceImp.getWebServerInfo(
						"_RZ", sql, DataCache.getinition().getUserId(),
						"uf_json_setdata", this);
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

	private void submitData(final String khm, final String yhkh,
			final String yhmc) {
		showProgressDialog();
		Config.getExecutorService().execute(new Runnable() {
			@Override
			public void run() {
				try {
					String sql = "update CCGL_YGB set yhk = '" + yhkh
							+ "',khyh = '" + yhmc + "',kzzd3 = '" + khm
							+ "' where pym = '"
							+ DataCache.getinition().getUserId() + "'";
					if(type==1){
						sql = "update CCGL_YGB set xgkh = '" + yhkh
								+ "',xgkhyh = '" + yhmc + "',xgkhr = '" + khm
								+ "',ykkzt = '1',xgyhksj=sysdate where pym = '"
								+ DataCache.getinition().getUserId() + "'";
					}
					JSONObject object = callWebserviceImp.getWebServerInfo(
							"c#_PAD_ESP_ZC", sql, "0000", "1",
							"uf_json_setdata2", getApplicationContext());
					flag = object.getString("flag");
					if (Integer.parseInt(flag) > 0) {
                        upload();
//						Message msg = new Message();
//						msg.what = 3;// 完成
//						handler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = 0;//
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 0;//
					handler.sendMessage(msg);
				}

			}
		});

	}

	private void returnData() {
		Config.getExecutorService().execute(new Runnable() {

			@Override
			public void run() {
				try {
					String sql = "update CCGL_YGB set yhk = '',khyh = '',kzzd1 = '' where pym = '"
							+ DataCache.getinition().getUserId() + "'";
					JSONObject object = callWebserviceImp.getWebServerInfo(
							"c#_PAD_ESP_ZC", sql, "0000", "1",
							"uf_json_setdata2", getApplicationContext());
					String flag = object.getString("flag");
					if (Integer.parseInt(flag) > 0) {
						Message msg = new Message();
						msg.what = 6;// 完成
						handler.sendMessage(msg);
					} else {
						Message msg = new Message();
						msg.what = 6;//
						handler.sendMessage(msg);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 6;//
					handler.sendMessage(msg);
				}

			}
		});

	}

	private void addYhk() {
		String khm = et_khm.getText().toString();
		String yhkh = et_yhkh.getText().toString();
		String yh = spinner_yh.getSelectedItem().toString();
		String fh = et_fh.getText().toString();
		String zh = et_zh.getText().toString();
		String flc = et_flc.getText().toString();

		if ("".equals(khm)) {
			Toast.makeText(getApplicationContext(), "开户名不能为空", 1).show();
			return;
		}
		if ("".equals(yhkh)) {
			Toast.makeText(getApplicationContext(), "银行卡号不能为空", 1).show();
			return;
		}
		if ("".equals(fh)) {
			Toast.makeText(getApplicationContext(), "分行不能为空", 1).show();
			return;
		}
		if ("".equals(zh)) {
			Toast.makeText(getApplicationContext(), "支行不能为空", 1).show();
			return;
		}
		String flcmc = "".equals(flc) ? "" : flc + "分理处";
		String yhmc = yh + "银行" + fh + "分行" + zh + "支行" + flcmc;
		submitData(khm, yhkh, yhmc);
	}

	@SuppressLint("ShowToast")
	@SuppressWarnings("unused")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			ArrayList<String> list = data.getStringArrayListExtra("imglist");
			loadImg(list);
		}
		if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
			ArrayList<String> list = data.getStringArrayListExtra("imglist");
			loadImg(list);
		}
	}

	private void loadImg(final ArrayList<String> list) {
		try {
			String mxh = tv_curr.getTag().toString();
			if (list.size() > 0) {
				tv_curr.setText("继续选择");
				tv_curr.setBackgroundResource(R.drawable.btn_normal_yellow);
			} else {
				tv_curr.setText("选择图片");
				tv_curr.setBackgroundResource(R.drawable.btn_normal);
			}
			filemap.put(mxh, list);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void upload() {
		try {
			boolean flag = true;
			List<Map<String, String>> filelist = new ArrayList<Map<String, String>>();
			for (String mxh : filemap.keySet()) {
				List<String> filepathlist = filemap.get(mxh);
				for (int j = 0; j < filepathlist.size(); j++) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("mxh", mxh);
					map.put("num", j + "");
					String path = filepathlist.get(j);
					map.put("filepath", path);
					filelist.add(map);
				}
			}
			int filenum = filelist.size();
			for (int i = 0; i < filenum; i++) {
				Map<String, String> map = filelist.get(i);
				if (flag) {
					String mxh = map.get("mxh");
					String filepath = map.get("filepath");
					String num = map.get("num");
					filepath = filepath.substring(7, filepath.length());
					// 压缩图片到100K
					filepath = ImageUtil.compressAndGenImage(convertBitmap(new File(filepath),getScreenWidth()), 200, "jpg");
					File file = new File(filepath);
					// toastShowMessage("开始上传第" + (i + 1) + "/" + filenum +
					// "张图片");
					flag = uploadPic("", readJpeg(file),"uf_json_setdata");
					file.delete();
				} else {
					flag = false;
					break;
				}
			}
			if (flag) {
				Message msg = new Message();
				msg.what = 12;
				handler.sendMessage(msg);
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
					"c#_PAD_ESP_ZCMX", "0001", DataCache.getinition()
							.getUserId()+"_yhk", "0001", data1,
					"uf_json_setdata2_p11", getApplicationContext());
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
			case -1:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("网络错误,请检查网络连接是否正常！",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface face,
									int paramAnonymous2Int) {
								finish();
							}
						});
				break;
			case 0:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("失败，请检查网络连接是否正常...错误标识：" + flag, null);
				break;
			case 1:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				ArrayAdapter<Map<String, String>> yhk_adapter = new ArrayAdapter<Map<String, String>>(
						getApplicationContext(), R.layout.spinner_item_yhk,
						list);
				yhk_adapter
						.setDropDownViewResource(R.layout.spinner_dropdown_item);
				spinner_yh.setAdapter(yhk_adapter);
				break;
			case 3:

				break;
			case 4:
				returnData();
				break;
			case 5:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				if(type==0){
					dialogShowMessage_P("提交成功，银行卡信息已完善！",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									DataCache.getinition().setHasYHK(true);
									setResult(1);
									finish();
								}
							});
				}else{
					dialogShowMessage_P("提交成功，我们的工作人员会在24小时内联系您进行确认！",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
										int paramAnonymous2Int) {
									finish();
								}
							});
				}
				

				break;
			case 6:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("提交失败，上传图片失败！", null);
				break;
			case 12:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				if(type==0){
					dialogShowMessage_P("提交成功，银行卡信息已完善！",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									DataCache.getinition().setHasYHK(true);
									setResult(1);
									finish();
								}
							});
				}else{
					dialogShowMessage_P("提交成功，我们的工作人员会在24小时内联系您进行确认！",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface face,
													int paramAnonymous2Int) {
									finish();
								}
							});
				}
				break;
			case 13:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				dialogShowMessage_P("提交失败，上传图片失败！", null);
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

}
