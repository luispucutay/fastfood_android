package com.hcpt.fastfood.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.hcpt.fastfood.BaseActivity;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.config.GlobalValue;
import com.hcpt.fastfood.object.PromotionObject;
import com.hcpt.fastfood.utility.DateUtil;

public class PromotionDetailActivity extends BaseActivity {

	private Button btnTopBack, btnRefresh, btnPromotion;
	private TextView lblHeader;

	private TextView lblTitle, lblCategory, lblStartDate, lblEndDate,
			lblContent;
	private ImageView imgImgage;

	private PromotionObject promotionObject;
	private ProgressBar progess;

	public static final String MY_PREFS_NAME = "LucasApp";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_promotion_detail);
		promotionObject = GlobalValue.getInstance().currentPromotion;
		initUI();
		initControl();
	}

	private void initControl() {
		lblHeader.setText(getString(R.string.lbl_promotion));
		btnTopBack.setBackgroundResource(R.drawable.btn_back);
		btnRefresh.setBackgroundResource(R.drawable.ic_action_refresh);
		btnRefresh.setVisibility(View.GONE);
		btnTopBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void initUI() {
		lblHeader = (TextView) findViewById(R.id.lblHeaderTtle);
		btnTopBack = (Button) findViewById(R.id.btnLeft);
		btnRefresh = (Button) findViewById(R.id.btnRight);
		btnRefresh.setVisibility(View.VISIBLE);
		btnPromotion = (Button) findViewById(R.id.btnPromotion);
		btnPromotion.setVisibility(View.GONE);

		lblTitle = (TextView) findViewById(R.id.lblTitle);
		lblCategory = (TextView) findViewById(R.id.lblCategory);
		lblStartDate = (TextView) findViewById(R.id.lblStartDate);
		lblEndDate = (TextView) findViewById(R.id.lblEndDate);
		lblContent = (TextView) findViewById(R.id.lblContent);
		progess = (ProgressBar) findViewById(R.id.progess);
		imgImgage = (ImageView) findViewById(R.id.imgImgage);

		// Set value
		lblTitle.setText(promotionObject.getTitle());
		// Load Image
		AQuery aq = new AQuery(this);
		aq.id(imgImgage)
				.progress(progess)
				.image(promotionObject.getImage(), true, true, 0,
						R.drawable.no_image, new BitmapAjaxCallback() {
							@SuppressLint("NewApi")
							@Override
							public void callback(String url, ImageView iv,
									Bitmap bm, AjaxStatus status) {
								if (bm != null) {
									imgImgage.setImageBitmap(bm);
								} else {
									imgImgage
											.setBackgroundResource(R.drawable.no_image);
								}
							}
						});
		lblCategory.setText(promotionObject.getCategoryName());
		lblStartDate.setText(DateUtil.convertTimeStampToDate(
				promotionObject.getStartDate(), "MMM,dd yyyy"));
		lblEndDate.setText(DateUtil.convertTimeStampToDate(
				promotionObject.getEndDate(), "MMM,dd yyyy"));
		lblContent.setText(promotionObject.getDescription());
	}
}
