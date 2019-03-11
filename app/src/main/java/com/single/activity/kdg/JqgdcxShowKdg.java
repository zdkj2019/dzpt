package com.single.activity.kdg;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.single.activity.FrameActivity;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.gtdzpt.R;
/**
 * 快递柜-近期工单查询-数据展示
 * @author zdkj
 *
 */
public class JqgdcxShowKdg extends FrameActivity {

	private Button confirm,cancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_jqgdcxshow);
		initVariable();
		initView();
		initListeners();
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

		Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(ServiceReportCache.getIndex());

		String zbh = itemmap.get("zbh").toString();
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
		((TextView) findViewById(R.id.tv_sbyjjcsj)).setText(itemmap.get("jcsj").toString());

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
					onBackPressed();
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
		
	
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

}
