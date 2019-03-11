package com.single.activity.kdg;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.single.activity.FrameActivity;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.gtdzpt.R;
import com.single.utils.DataUtil;

/**
 * 快递柜-组长转派查询-筛选条件
 * 
 * @author zdkj
 *
 */
public class ZzzpCxTj extends FrameActivity {

	private Button confirm, cancel;
	private TextView tv_start, tv_end;
	private RadioGroup rg_1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// 默认焦点不进入输入框，避免显示输入法
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		appendMainBody(R.layout.activity_kdg_zzzpcx);
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
		rg_1 = (RadioGroup) findViewById(R.id.rg_1);
		tv_start = (TextView) findViewById(R.id.tv_start);
		tv_end = (TextView) findViewById(R.id.tv_end);
		
		tv_start.setText(DataUtil.toDataString("yyyy-MM-dd HH:mm:ss"));
		tv_end.setText(DataUtil.toDataString("yyyy-MM-dd HH:mm:ss"));
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
					if(!isNotNull(tv_start)){
						toastShowMessage("请选择开始时间");
						return;
					}
					if(!isNotNull(tv_end)){
						toastShowMessage("请选择结束时间");
						return;
					}
					String zt = "";
					if(rg_1.getCheckedRadioButtonId() == R.id.rb_1_s){
						zt = "2";
					}else if(rg_1.getCheckedRadioButtonId() == R.id.rb_1_f){
						zt = "1";
					}else{
						zt = "3";
					}
					DataCache.getinition().setQueryType(2205);
					DataCache.getinition().setTitle("组长片区工单查询");
					Intent intent = new Intent(ZzzpCxTj.this, ListKdg.class);
					intent.putExtra("status",zt);
					intent.putExtra("start",tv_start.getText().toString());
					intent.putExtra("end",tv_end.getText().toString());
					startActivity(intent);
					break;
				default:
					break;
				}

			}
		};

		topBack.setOnClickListener(backonClickListener);
		cancel.setOnClickListener(backonClickListener);
		confirm.setOnClickListener(backonClickListener);

		tv_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dateDialog(tv_start);
			}
		});

		tv_end.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dateDialog(tv_end);
			}
		});
	}

	@Override
	protected void getWebService(String s) {

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
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
