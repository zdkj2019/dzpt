package com.single.activity.kc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.single.activity.FrameActivity;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.gtdzpt.R;
import com.single.utils.Config;
import com.single.utils.DataUtil;

/**
 * 库存查询展示
 * 
 * @author hs
 */
@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class KccxShowActivity extends FrameActivity {

	private TextView tv_1,tv_2,tv_3,tv_4,tv_5,tv_6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kcxxshow);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {

		tv_1 = (TextView) findViewById(R.id.tv_1);
		tv_2= (TextView) findViewById(R.id.tv_2);
		tv_3= (TextView) findViewById(R.id.tv_3);
		tv_4= (TextView) findViewById(R.id.tv_4);
		tv_5= (TextView) findViewById(R.id.tv_5);
		tv_6= (TextView) findViewById(R.id.tv_6);
	}

	@Override
	protected void initView() {

		title.setText(DataCache.getinition().getTitle());
		Map<String, Object> itemmap = ServiceReportCache.getObjectdata().get(
				ServiceReportCache.getIndex());

		tv_1.setText(itemmap.get("jgmc").toString());
		tv_2.setText(itemmap.get("jgmc_1").toString());
		tv_3.setText(itemmap.get("hpmc").toString());
		tv_4.setText(itemmap.get("jldw").toString());
		tv_5.setText(itemmap.get("sqjc").toString());
		tv_6.setText(itemmap.get("dqkc").toString());
	}

	@Override
	protected void initListeners() {
		OnClickListener backonClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.bt_topback:
					onBackPressed();
					break;
				}
				
			}
		};
		topBack.setOnClickListener(backonClickListener);
	}

	@Override
	protected void getWebService(String s) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
