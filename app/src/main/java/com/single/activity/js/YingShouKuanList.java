package com.single.activity.js;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.single.activity.FrameActivity;
import com.single.activity.kdg.ListKdg;
import com.single.activity.kdg.Zzpqgdcx;
import com.single.activity.main.MainActivity;
import com.single.cache.DataCache;
import com.single.cache.ServiceReportCache;
import com.single.utils.Config;
import com.single.gtdzpt.R;

/**
 * 应收款list
 * 
 * @author
 */
@SuppressLint("HandlerLeak")
public class YingShouKuanList extends FrameActivity {

	private Button confirm,cancel;
	private RadioGroup rg_1,rg_2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_yigshoukuan);
		initVariable();
		initView();
		initListeners();

	}

	@Override
	protected void initVariable() {
		title.setText(DataCache.getinition().getTitle());
		rg_1 = (RadioGroup) findViewById(R.id.rg_1);
		rg_2 = (RadioGroup) findViewById(R.id.rg_2);
	}

	@Override
	protected void initView() {
		confirm = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.confirm);
		cancel = (Button) findViewById(R.id.include_botto).findViewById(
				R.id.cancel);
		confirm.setText("确定");
		cancel.setText("取消");
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
				case R.id.cancel:
					onBackPressed();
					break;
				case R.id.confirm:
					int sj = rg_1.getCheckedRadioButtonId()==R.id.rb_1_s?1:2;
					int nr = rg_2.getCheckedRadioButtonId()==R.id.rb_2_s?1:2;
					if(nr==1){ //总收入
						Intent intent = new Intent(YingShouKuanList.this, FyqrCompleteActivity.class);
						intent.putExtra("sj", rg_1.getCheckedRadioButtonId()==R.id.rb_1_s?1:2);
						startActivity(intent);
					}else{//明细收入
						DataCache.getinition().setQueryType(2601);
						Intent intent = new Intent(YingShouKuanList.this, ListKdg.class);
						intent.putExtra("sj", rg_1.getCheckedRadioButtonId()==R.id.rb_1_s?1:2);
						startActivity(intent);
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

	}

	@Override
	protected void getWebService(String s) {

	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.putExtra("currType", 3);
		startActivity(intent);
		finish();
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
				
				break;

			case FAIL:
				
				break;

			}
			if (!backboolean) {
				progressDialog.dismiss();
			}
		}

	};

}
