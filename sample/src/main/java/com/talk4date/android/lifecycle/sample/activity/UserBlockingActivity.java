package com.talk4date.android.lifecycle.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.talk4date.android.lifecycle.ActivityLifecycle;
import com.talk4date.android.lifecycle.EventListener;
import com.talk4date.android.lifecycle.EventReceiver;
import com.talk4date.android.lifecycle.sample.R;
import com.talk4date.android.lifecycle.sample.service.SendDataService;
import com.talk4date.android.lifecycle.sample.utils.MessageDialogFragment;

/**
 * Sends a request to the service and blocks the user until there is some response.
 */
public class UserBlockingActivity extends Activity {

	private static final String TAG_DIALOG = "dialog";
	private Button button;

	private SendDataService sendDataService = SendDataService.getInstance();
	private MessageDialogFragment dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_blocking);

		ActivityLifecycle lifecycle = ActivityLifecycle.activitySessionLifecycle(this);

		this.button = (Button)findViewById(R.id.button);

		final EventReceiver<Void> sendDataListener = lifecycle
				.registerListener("sendData", true, new EventListener<Void>() {
					@Override
					public void onEvent(Void event) {
						// Dialog must not be null, otherwise something very bad happened.
						dialog.dismiss();
					}
				});

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog = new MessageDialogFragment()
						.withMessage("Sending data... please wait.");
				dialog.setCancelable(false);
				dialog.show(getFragmentManager(), TAG_DIALOG);
				sendDataService.sendDataToServer("Test", sendDataListener);
			}
		});

		dialog = (MessageDialogFragment) getFragmentManager().findFragmentByTag(TAG_DIALOG);
		if (lifecycle.isRestored() && dialog != null) {
			dialog.dismiss();
		}
	}
}
