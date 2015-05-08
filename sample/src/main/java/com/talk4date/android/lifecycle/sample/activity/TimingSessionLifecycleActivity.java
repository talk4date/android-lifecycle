package com.talk4date.android.lifecycle.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.talk4date.android.lifecycle.ActivityLifecycle;
import com.talk4date.android.lifecycle.EventListener;
import com.talk4date.android.lifecycle.EventReceiver;
import com.talk4date.android.lifecycle.sample.R;
import com.talk4date.android.lifecycle.sample.service.TimingService;

/**
 * Activity which shows the elapsed time since the timing service was created.
 * It uses the session lifecycle and drops events while paused.
 */
public class TimingSessionLifecycleActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timing);
		final TextView timeLabel = (TextView) findViewById(R.id.elapsedTime);

		ActivityLifecycle lifecycle = ActivityLifecycle.sessionLifecycle(this);
		EventReceiver<Long> timeListener = lifecycle.registerListener("time", false, new EventListener<Long>() {
			@Override
			public void onEvent(Long time) {
				timeLabel.setText(time.toString());
			}
		});

		// When the activity lifecycle was just created for this activity instance and we have not been restored,
		// we have to register our receiver.
		if (lifecycle.isNewOrRestored()) {
			TimingService.getInstance().addReceiver(timeListener);
		}
	}
}