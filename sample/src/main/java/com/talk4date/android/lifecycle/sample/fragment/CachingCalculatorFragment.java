package com.talk4date.android.lifecycle.sample.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talk4date.android.lifecycle.EventListener;
import com.talk4date.android.lifecycle.EventReceiver;
import com.talk4date.android.lifecycle.FragmentLifecycle;
import com.talk4date.android.lifecycle.sample.R;
import com.talk4date.android.lifecycle.sample.service.CachingCalculatorService;
import com.talk4date.android.lifecycle.ui.BaseLifecycleDispatchingFragment;

/**
 * Fragment version of the {@link com.talk4date.android.lifecycle.sample.activity.CachingCalculatorActivity}.
 */
public class CachingCalculatorFragment extends BaseLifecycleDispatchingFragment {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_caching_calculator, container, false);
		final TextView textView = (TextView) view.findViewById(R.id.result);

		FragmentLifecycle instanceLifecycle = FragmentLifecycle.instanceLifecycle(this);
		EventReceiver<Integer> addReceiver = instanceLifecycle.registerListener("add", true, new EventListener<Integer>() {
			@Override
			public void onEvent(Integer event) {
				textView.setText(event.toString());
			}
		});

		CachingCalculatorService.getInstance().add(16, 35, addReceiver);

		return view;
	}
}
