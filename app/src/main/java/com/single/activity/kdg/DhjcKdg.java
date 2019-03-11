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
/**
 * 快递柜-到货检查
 * @author zdkj
 *
 */
public class DhjcKdg extends FrameActivity {

	private Button confirm,cancel;
	private String flag,zbh,type="1",message,ztzt;
	private TextView tv_fwgcs;
	private ImageView iv_telphone;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_dhjc);
		initVariable();
		initView();
		initListeners();
		if("（暂停）".equals(ztzt)){
			confirm.setOnClickListener(null);
			dialogShowMessage("该工单处于暂停状态，不能提交！", null, null);
		}
	}

	@Override
	protected void initVariable() {

		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		tv_fwgcs = (TextView) findViewById(R.id.tv_fwgcs);
		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);

	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());

		final Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		ztzt = itemmap.get("ztzt").toString();
		((TextView) findViewById(R.id.tv_zbh)).setText(zbh);
		((TextView) findViewById(R.id.tv_axdh)).setText(itemmap.get("axdh").toString());
		((TextView) findViewById(R.id.tv_xqmc)).setText(itemmap.get("xqmc").toString());
		((TextView) findViewById(R.id.tv_xxdz)).setText(itemmap.get("xxdz").toString());
		((TextView) findViewById(R.id.tv_ywlx)).setText(itemmap.get("ywlx").toString());
		((TextView) findViewById(R.id.tv_sblx)).setText(itemmap.get("sblx").toString());
		((TextView) findViewById(R.id.tv_gzxx)).setText(itemmap.get("gzxx").toString());
		((TextView) findViewById(R.id.tv_ds)).setText(itemmap.get("ds").toString());
		((TextView) findViewById(R.id.tv_qx)).setText(itemmap.get("ssqx").toString());
		((TextView) findViewById(R.id.tv_zgsl)).setText(itemmap.get("zgsl").toString());
		((TextView) findViewById(R.id.tv_fgsl)).setText(itemmap.get("fgsl").toString());
		((TextView) findViewById(R.id.tv_fhrq)).setText(itemmap.get("fhrq").toString());
		((TextView) findViewById(R.id.tv_dhrq)).setText(itemmap.get("dhrq").toString());
		((TextView) findViewById(R.id.tv_sbyjjcsj)).setText(itemmap.get("jcrq").toString());
		
		((TextView) findViewById(R.id.tv_fwgcs)).setText(itemmap.get("xm").toString());
		iv_telphone.setOnClickListener(new OnClickListener() {
			
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
//					dialogShowMessage("根据平台星级评定标准，拒单扣2分，确认拒绝接单？",new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface face,int paramAnonymous2Int) {
//							showProgressDialog();
//							Config.getExecutorService().execute(new Runnable() {
//								
//								@Override
//								public void run() {
//									type = "2";
//									getWebService("submit");
//								}
//							});
//						}
//					} ,null);
					onBackPressed();
					break;
				case R.id.confirm:
					showProgressDialog();
					Config.getExecutorService().execute(new Runnable() {
						
						@Override
						public void run() {
							type = "1";
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
	}

	@Override
	protected void getWebService(String s) {
		
		if (s.equals("submit")) {// 提交
			try {
				message = "提交成功";
				String sql = "update KDG_PGZB set djzt = '2.6',pgzbsj = sysdate,dhry = '"+DataCache.getinition().getUserId()+"' where zbh = '"+zbh+"'";
				JSONObject json = callWebserviceImp.getWebServerInfo(
						"c#_PAD_ESP_ZC", sql, "0000", "1",
						"uf_json_setdata2", getApplicationContext());
				flag =json.getString("flag");
				if (Integer.parseInt(flag) > 0) {
					Message msg = new Message();
					msg.what = Constant.SUCCESS;
					handler.sendMessage(msg);
				}else{
					flag =json.getString("msg");
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

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constant.FAIL:
				dialogShowMessage_P("失败，请检查后重试...错误标识：" + flag, null);
				break;
			case Constant.SUCCESS:
				dialogShowMessage_P(message,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface face,int paramAnonymous2Int) {
						onBackPressed();
					}
				});
				break;
			case Constant.NETWORK_ERROR:
				dialogShowMessage_P(Constant.NETWORK_ERROR_STR, null);
				break;
			}
			if(progressDialog!=null){
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