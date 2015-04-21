package com.talk4date.android.lifecycle.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.talk4date.android.lifecycle.ActivityLifecycle;
import com.talk4date.android.lifecycle.EventListener;
import com.talk4date.android.lifecycle.EventReceiver;
import com.talk4date.android.lifecycle.sample.R;
import com.talk4date.android.lifecycle.sample.service.CachingCalculatorService;

/**
 * Activity that demonstrates calls to a service which caches its results.
 *
 * Practically the returned results from the service would be large and can't be
 * reasonably persisted in instance state.
 *
 * Therefore the activity should request fresh result from the service when it is recreated.
 */
public class CachingCalculatorActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_caching_calculator);
		final TextView resultView = (TextView) findViewById(R.id.result);

		ActivityLifecycle lifecycle = ActivityLifecycle.activityLifecycle(this);
		EventReceiver<Integer> resultListener = lifecycle.registerListener("result", true, new EventListener<Integer>() {
			@Override
			public void onEvent(Integer result) {
				resultView.setText(String.valueOf(result));
			}
		});

		// TODO:
		// Currently when the screen is rotated before the service caches the result,
		// we calculate the result twice.
		//
		// Where should we best solve this?
		// I'd tend to say that this is the beer of the service.
		// It can also track which requests are currently in progress and just wait for the answer.
		CachingCalculatorService.getInstance().add(16, 35, resultListener);
	}
}
