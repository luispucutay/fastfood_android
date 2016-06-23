package com.hcpt.fastfood.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.hcpt.fastfood.BaseActivity;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.config.Constant;
import com.hcpt.fastfood.utility.GPSTracker;

public class MapActivity extends BaseActivity {
	private WebView cweBview;
	private String fromLocation = "";
	private String destinationLocation = "";
	private Button btnTopBack;
	private TextView lblHeader;
	private GPSTracker tracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_map);
		tracker = new GPSTracker(this);
		initUI();
		getData();

	}

	private void initUI() {
		lblHeader = (TextView) findViewById(R.id.lblHeaderTtle);
		lblHeader.setText(getString(R.string.Direction));
		btnTopBack = (Button) findViewById(R.id.btnLeft);
		btnTopBack.setBackgroundResource(R.drawable.btn_back);
		btnTopBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
	}

	private void getData() {
		if (tracker.canGetLocation()) {
			setMap();
		}

	}

	private void setMap() {
		fromLocation = tracker.getLatitude() + "," + tracker.getLongitude();
		destinationLocation = Constant.MY_COMPANY_LAT + ","
				+ Constant.MY_COMPANY_LONG;

		String url = "https://maps.google.com/maps?saddr=" + fromLocation
				+ "&daddr=" + destinationLocation + "&output=classic";

		try {
			updateWebView(url);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void updateWebView(String url) {
		// TODO Auto-generated method stub
		cweBview = (WebView) findViewById(R.id.conwebView);
		cweBview.getSettings().setJavaScriptEnabled(true);
		cweBview.getSettings().setDomStorageEnabled(true);
		cweBview.setWebViewClient(new HelloWebViewClient());
		cweBview.loadUrl(url);
	}

	private class HelloWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && cweBview.canGoBack()) {
			cweBview.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);

	}
}
