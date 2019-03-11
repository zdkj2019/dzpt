package com.single.activity.kdg;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
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
import com.single.gtdzpt.R;

/**
 * 快递柜-组长转派查询-数据展示
 * 
 * @author zdkj
 *
 */
public class ZzzpCxKdg extends FrameActivity {

	private Button confirm, cancel;
	private ImageView iv_telphone, iv_lxr_telphone;
	private String tel1, tel2,zbh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_zzzp_show);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {

		iv_telphone = (ImageView) findViewById(R.id.iv_telphone);
		iv_lxr_telphone = (ImageView) findViewById(R.id.iv_lxr_telphone);

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

		Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(
				ServiceReportCache.getIndex());

		zbh = itemmap.get("zbh").toString();
		((TextView) findViewById(R.id.tv_zbh)).setText(zbh);
		((TextView) findViewById(R.id.tv_ywlx)).setText(itemmap.get("ywlx")
				.toString());
		((TextView) findViewById(R.id.tv_djzt)).setText(itemmap.get("djzt")
				.toString());
		((TextView) findViewById(R.id.tv_pgdx)).setText(itemmap.get("pgdx")
				.toString());
		((TextView) findViewById(R.id.tv_jbflxdh)).setText(itemmap.get(
				"jbflxdh").toString());
		((TextView) findViewById(R.id.tv_fbf)).setText(itemmap.get("fbf")
				.toString());
		((TextView) findViewById(R.id.tv_bzrlxdh)).setText(itemmap.get(
				"bzrlxdh").toString());
		((TextView) findViewById(R.id.tv_dqmc)).setText(itemmap.get("dqmc")
				.toString());
		((TextView) findViewById(R.id.tv_wdmc)).setText(itemmap.get("wdmc")
				.toString());
		((TextView) findViewById(R.id.tv_xxdz)).setText(itemmap.get("xxdz")
				.toString());
		((TextView) findViewById(R.id.tv_gzxx)).setText(itemmap.get("gzxx")
				.toString());
		((TextView) findViewById(R.id.tv_clfs)).setText(itemmap.get("clfs")
				.toString());
		((TextView) findViewById(R.id.tv_bz)).setText(itemmap.get("bz")
				.toString());
		((TextView) findViewById(R.id.tv_bzsj)).setText(itemmap.get("bzsj")
				.toString());
		((TextView) findViewById(R.id.tv_pgsj)).setText(itemmap.get("pgsj")
				.toString());
		((TextView) findViewById(R.id.tv_slsj)).setText(itemmap.get("slsj")
				.toString());
		((TextView) findViewById(R.id.tv_wcsj)).setText(itemmap.get("wcsj")
				.toString());

		tel1 = itemmap.get("bzrlxdh").toString();
		tel2 = itemmap.get("jbflxdh").toString();
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

		iv_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(tel1,zbh);
			}
		});

		iv_lxr_telphone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Call(tel2,zbh);
			}
		});
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
