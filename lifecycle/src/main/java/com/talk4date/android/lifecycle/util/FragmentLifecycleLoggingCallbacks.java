package com.talk4date.android.lifecycle.util;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talk4date.android.lifecycle.callbacks.FragmentLifecycleCallbacks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple lifecycle callbacks implementation which logs all events on trace level.
 */
public class FragmentLifecycleLoggingCallbacks implements FragmentLifecycleCallbacks {

	private static final Logger logger = LoggerFactory.getLogger(FragmentLifecycleLoggingCallbacks.class);

	private void log(Fragment fragment, String event) {
		if (logger.isTraceEnabled()) {
			logger.trace("{}: {}", fragment, event);
		}
	}

	@Override
	public void onFragmentAttach(Fragment fragment, Activity activity) {
		log(fragment, "onFragmentAttach");
	}

	@Override
	public void onFragmentCreate(Fragment fragment, Bundle savedInstanceState) {
		log(fragment, "onFragmentCreate");
	}

	@Override
	public void onFragmentCreateView(Fragment fragment, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		log(fragment, "onFragmentCreateView");
	}

	@Override
	public void onFragmentViewCreated(Fragment fragment, View view, Bundle savedInstanceState) {
		log(fragment, "onFragmentViewCreated");
	}

	@Override
	public void onFragmentActivityCreated(Fragment fragment, Bundle savedInstanceState) {
		log(fragment, "onFragmentActivityCreated");
	}

	@Override
	public void onFragmentViewStateRestored(Fragment fragment, Bundle savedInstanceState) {
		log(fragment, "onFragmentViewStateRestored");
	}

	@Override
	public void onFragmentStart(Fragment fragment) {
		log(fragment, "onFragmentStart");
	}

	@Override
	public void onFragmentResume(Fragment fragment) {
		log(fragment, "onFragmentResume");
	}

	@Override
	public void onFragmentPause(Fragment fragment) {
		log(fragment, "onFragmentPause");
	}

	@Override
	public void onFragmentSaveInstanceState(Fragment fragment, Bundle outState) {
		log(fragment, "onFragmentSaveInstanceState");
	}

	@Override
	public void onFragmentStop(Fragment fragment) {
		log(fragment, "onFragmentStop");
	}

	@Override
	public void onFragmentDestroyView(Fragment fragment) {
		log(fragment, "onFragmentDestroyView");
	}

	@Override
	public void onFragmentDestroy(Fragment fragment) {
		log(fragment, "onFragmentDestroy");
	}

	@Override
	public void onFragmentDetach(Fragment fragment) {
		log(fragment, "onFragmentDetach");
	}
}