package com.talk4date.android.lifecycle.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talk4date.android.lifecycle.EventListener;
import com.talk4date.android.lifecycle.EventReceiver;
import com.talk4date.android.lifecycle.FragmentLifecycle;
import com.talk4date.android.lifecycle.Lifecycle;
import com.talk4date.android.lifecycle.sample.R;
import com.talk4date.android.lifecycle.sample.service.TimingService;
import com.talk4date.android.lifecycle.ui.BaseLifecycleDispatchingFragment;

/**
 * The timing example on a fragment base.
 */
public class TimingSessionLifecycleFragment extends BaseLifecycleDispatchingFragment {

	private TextView label;
	private TimingService timingService = TimingService.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FragmentLifecycle sessionLifecycle = FragmentLifecycle.sessionLifecycle(this);

		final EventReceiver<Long> timeReceiver = sessionLifecycle.registerListener("timing", false, new EventListener<Long>() {
			@Override
			public void onEvent(Long time) {
				label.setText(String.valueOf(time));
			}
		});

		if (sessionLifecycle.isNewOrRestored()) {
			timingService.addReceiver(timeReceiver);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_timing, container, false);
		label = (TextView) view.findViewById(R.id.elapsedTime);
		return view;
	}
}
