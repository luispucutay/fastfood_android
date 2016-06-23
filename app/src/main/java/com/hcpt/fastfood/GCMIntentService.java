package com.hcpt.fastfood;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.hcpt.fastfood.activity.MainActivity;
import com.hcpt.fastfood.activity.SplashActivity;
import com.hcpt.fastfood.utility.StringUtility;

/**
 * {@link IntentService} responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {
	private final String TAG = "GCMIntentService";

	public static final String KEY_MSG = "message";

	private static int NOTIFICATION_ID = 0;

	public GCMIntentService() {
		super(GcmManager.SENDER_ID);
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		Log.e(TAG, "onError: " + arg1);
	}

	@Override
	protected void onMessage(Context context, Intent data) {
		Log.e(TAG, "onMessage: " + data);
		sendNotification(context, data);
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(1000);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.e(TAG, "onRegistered: " + registrationId);
		new GcmManager(context).setRegistrationId(context, registrationId);
		if (!StringUtility.isEmpty(registrationId)) {
		}
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		Log.e(TAG, "onUnregistered:  " + arg1);
	}

	// Put the GCM message into a notification and post it.
	private void sendNotification(Context context, Intent data) {

		String msg = data.getStringExtra(KEY_MSG);

		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(context, SplashActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_logo_splash)
				.setContentTitle(context.getString(R.string.app_name))
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
				.setContentText(msg).setAutoCancel(true);
		mBuilder.setContentIntent(contentIntent);
		NOTIFICATION_ID = NOTIFICATION_ID + 1;
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
}
