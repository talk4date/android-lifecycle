package com.talk4date.android.lifecycle.util;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple base fragment which logs TRACE logs for all lifecycle methods.
 */
public class LifecycleLoggingFragment extends Fragment {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void onAttach(Activity activity) {
		logger.trace("onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		logger.trace("onCreate");
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		logger.trace("onCreateView");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		logger.trace("onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		logger.trace("onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		logger.trace("onViewStateRestored");
		super.onViewStateRestored(savedInstanceState);
	}

	@Override
	public void onStart() {
		logger.trace("onStart");
		super.onStart();
	}

	@Override
	public void onResume() {
		logger.trace("onResume");
		super.onResume();
	}

	@Override
	public void onPause() {
		logger.trace("onPause");
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		logger.trace("onSaveInstanceState");
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onStop() {
		logger.trace("onStop");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		logger.trace("onDestroyView");
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		logger.trace("onDestroy");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		logger.trace("onDetach");
		super.onDetach();
	}
}
