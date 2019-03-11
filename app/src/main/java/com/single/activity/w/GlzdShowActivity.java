package com.single.activity.w;

import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.ZoomDensity;

import com.single.activity.FrameActivity;
import com.single.cache.ServiceReportCache;
import com.single.gtdzpt.R;

public class GlzdShowActivity extends FrameActivity {
	
	private WebView webview;
	private String name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appendMainBody(R.layout.activity_xxglshow);
		initVariable();
		initView();
		initListeners();
	}

	@Override
	protected void initVariable() {
		final int index = ServiceReportCache.getIndex();
		final Map<String, String> itemmap = ServiceReportCache.getData().get(index);
		webview = (WebView) findViewById(R.id.webview);
//		WebSettings settings = webview.getSettings();
//        settings.setBuiltInZoomControls(true); // 显示放大缩小 controler
//        settings.setSupportZoom(true); // 可以缩放
//        settings.setDefaultZoom(ZoomDensity.CLOSE);// 默认缩放模式
//        settings.setUseWideViewPort(true);  //为图片添加放大缩小功能
		webview.loadUrl(itemmap.get("bz"));
		name = itemmap.get("var_kzzd1");
	}

	@Override
	protected void initView() {
		title.setText(name);
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
	}

	@Override
	protected void getWebService(String s) {

	}

	@Override
	public void onBackPressed() {
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
