package com.talk4date.android.lifecycle.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.talk4date.android.lifecycle.ActivityLifecycle;
import com.talk4date.android.lifecycle.EventListener;
import com.talk4date.android.lifecycle.EventReceiver;
import com.talk4date.android.lifecycle.sample.R;
import com.talk4date.android.lifecycle.sample.service.TimingService;

/**
 * Activity which shows the elapsed time since the timing service was created.
 * It uses the simple ActivityLifecycle and discards events while in backgorund.
 *
 * FIXME: There is currently no way to unregister the receiver when the lifecycle is destroyed.
 * We need a lifecycle destroy event.
 */
public class TimingActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timing);
		final TextView timeLabel = (TextView) findViewById(R.id.elapsedTime);

		ActivityLifecycle lifecycle = ActivityLifecycle.instanceLifecycle(this);
		EventReceiver<Long> timeListener = lifecycle.registerListener("time", false, new EventListener<Long>() {
			@Override
			public void onEvent(Long time) {
				timeLabel.setText(time.toString());
			}
		});

		// since we are in the short lifecycle, we need to re-register on every create
		TimingService.getInstance().addReceiver(timeListener);
	}
}