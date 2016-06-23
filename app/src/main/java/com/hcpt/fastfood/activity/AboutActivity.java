package com.hcpt.fastfood.activity;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hcpt.fastfood.BaseActivity;
import com.hcpt.fastfood.R;
import com.hcpt.fastfood.config.Constant;
import com.hcpt.fastfood.location.BestLocationListener;
import com.hcpt.fastfood.location.BestLocationProvider;
import com.hcpt.fastfood.location.BestLocationProvider.LocationType;
import com.hcpt.fastfood.modelmanager.ModelManager;
import com.hcpt.fastfood.modelmanager.ModelManagerListener;

public class AboutActivity extends BaseActivity implements OnClickListener {

	private Button btnTopBack;
	private TextView lblHeader, lblWebSite;
	private MapView mMapView;
	private GoogleMap googleMap;
	private BestLocationProvider mBestLocationProvider;
	private BestLocationListener mBestLocationListener;
	private TextView btnFeedback, btnGoHere;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initUI();
		initControl();
		initGoogleMap(savedInstanceState);
	}

	private void initGoogleMap(Bundle savedInstanceState) {
		mMapView.onCreate(savedInstanceState);
		mMapView.onResume();
		try {
			MapsInitializer.initialize(getApplicationContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
		googleMap = mMapView.getMap();
	}

	private void initUI() {
		lblHeader = (TextView) findViewById(R.id.lblHeaderTtle);
		lblHeader.setText(getString(R.string.lbl_about));
		btnTopBack = (Button) findViewById(R.id.btnLeft);
		btnTopBack.setBackgroundResource(R.drawable.btn_back);
		btnTopBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		mMapView = (MapView) findViewById(R.id.mapView);
		btnFeedback = (TextView) findViewById(R.id.btnFeedback);
		btnGoHere = (TextView) findViewById(R.id.btnGohere);
        lblWebSite = (TextView) findViewById(R.id.lblWebsite);
	}

	private void initControl() {
		btnFeedback.setOnClickListener(this);
		btnGoHere.setOnClickListener(this);
        lblWebSite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(AboutActivity.this.getResources().getString(R.string.about_link)));
                startActivity(i);
            }
        });
	}

	@Override
	public void onClick(View v) {
		if (v == btnFeedback) {
			showFeedbackDialog();
			return;
		}
		if (v == btnGoHere) {
			gotoActivity(MapActivity.class);
			return;
		}
	}

	private void showFeedbackDialog() {
		Display display = self.getWindowManager().getDefaultDisplay();
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_layout_feedback);
		dialog.setCancelable(false);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = (display.getWidth() * 4) / 5;
		dialog.getWindow().setAttributes(lp);

		final EditText txtEmail = (EditText) dialog.findViewById(R.id.txtEmail);
		final EditText txtContent = (EditText) dialog
				.findViewById(R.id.txtContent);
		TextView lblClose = (TextView) dialog.findViewById(R.id.lblCancel);
		TextView lblSend = (TextView) dialog.findViewById(R.id.lblSend);

		lblClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		lblSend.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String email = txtEmail.getText().toString();
				String content = txtContent.getText().toString();
				if (email.isEmpty() || content.isEmpty()) {
					Toast.makeText(self,
							getString(R.string.error_input_receipted_info),
							Toast.LENGTH_LONG).show();
				} else {
					sendFeedback(email, content);
				}
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void sendFeedback(String email, String feedback) {
		ModelManager.sendFeedBack(self, email, feedback, true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						Log.e(TAG, "json response :" + json);
						Toast.makeText(self,
								self.getString(R.string.send_feedback_success),
								Toast.LENGTH_LONG).show();
					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub
						Toast.makeText(self,
								self.getString(R.string.error_send_feedback),
								Toast.LENGTH_LONG).show();
					}
				});
	}

	@Override
	public void onResume() {
		initLocation();
		mBestLocationProvider
				.startLocationUpdatesWithListener(mBestLocationListener);
		super.onResume();
		mMapView.onResume();
		initCompanyLocation();
	}

	private void initCompanyLocation() {
		MarkerOptions marker = new MarkerOptions().position(new LatLng(
				Constant.MY_COMPANY_LAT, Constant.MY_COMPANY_LONG));
		// Changing marker icon
		marker.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
		// adding marker
		googleMap.addMarker(marker);
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(Constant.MY_COMPANY_LAT,
						Constant.MY_COMPANY_LONG)).zoom(16).build();
		googleMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
	}

	@Override
	public void onPause() {
		initLocation();
		mBestLocationProvider.stopLocationUpdates();
		super.onPause();
		mMapView.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mMapView.onLowMemory();
	}

	private void initLocation() {
		if (mBestLocationListener == null) {
			mBestLocationListener = new BestLocationListener() {

				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {
					Log.i(TAG, "onStatusChanged PROVIDER:" + provider
							+ " STATUS:" + String.valueOf(status));
				}

				@Override
				public void onProviderEnabled(String provider) {
					Log.i(TAG, "onProviderEnabled PROVIDER:" + provider);
				}

				@Override
				public void onProviderDisabled(String provider) {
					Log.i(TAG, "onProviderDisabled PROVIDER:" + provider);
				}

				@Override
				public void onLocationUpdateTimeoutExceeded(LocationType type) {
					Log.w(TAG, "onLocationUpdateTimeoutExceeded PROVIDER:"
							+ type);
				}

				@Override
				public void onLocationUpdate(Location location,
						LocationType type, boolean isFresh) {
					Log.i(TAG, "onLocationUpdate TYPE:" + type + " Location:"
							+ mBestLocationProvider.locationToString(location));

					// create marker
					MarkerOptions marker = new MarkerOptions().position(
							new LatLng(location.getLatitude(), location
									.getLongitude())).title(
							getResources().getString(R.string.you_are_here));

					// Changing marker icon
					marker.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
				}
			};

			if (mBestLocationProvider == null) {
				mBestLocationProvider = new BestLocationProvider(this, true,
						true, 10000, 1000, 2, 0);
			}
		}
	}
}
