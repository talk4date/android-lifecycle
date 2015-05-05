package com.talk4date.android.lifecycle.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talk4date.android.lifecycle.callbacks.FragmentLifecycleDispatcher;

/**
 * Base Fragment that dispatches all events to the {@link com.talk4date.android.lifecycle.callbacks.FragmentLifecycleDispatcher}.
 */
public class BaseLifecycleDispatchingFragment extends Fragment {

	private FragmentLifecycleDispatcher dispatcher = FragmentLifecycleDispatcher.get();

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		dispatcher.onFragmentAttach(this, activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dispatcher.onFragmentCreate(this, savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		dispatcher.onFragmentCreateView(this, inflater, container, savedInstanceState);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		dispatcher.onFragmentViewCreated(this, view, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		dispatcher.onFragmentActivityCreated(this, savedInstanceState);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		dispatcher.onFragmentViewStateRestored(this, savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
		dispatcher.onFragmentStart(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		dispatcher.onFragmentResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		dispatcher.onFragmentPause(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		dispatcher.onFragmentSaveInstanceState(this, outState);
	}

	@Override
	public void onStop() {
		super.onStop();
		dispatcher.onFragmentStop(this);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		dispatcher.onFragmentDestroyView(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dispatcher.onFragmentDestroy(this);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		dispatcher.onFragmentDetach(this);
	}
}