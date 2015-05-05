package com.talk4date.android.lifecycle.callbacks;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Abstract implementation of {@link com.talk4date.android.lifecycle.callbacks.FragmentLifecycleCallbacks}.
 * 
 * All methods do nothing by default. Subclasses do not need to call through to super.
 */
public class AbstractFragmentLifecycleCallbacks implements FragmentLifecycleCallbacks {
	
	@Override
	public void onFragmentAttach(Fragment fragment, Activity activity) {}

	@Override
	public void onFragmentCreate(Fragment fragment, Bundle savedInstanceState) {}

	@Override
	public void onFragmentCreateView(Fragment fragment, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {}

	@Override
	public void onFragmentViewCreated(Fragment fragment, View view, Bundle savedInstanceState) {}

	@Override
	public void onFragmentActivityCreated(Fragment fragment, Bundle savedInstanceState) {}

	@Override
	public void onFragmentViewStateRestored(Fragment fragment, Bundle savedInstanceState) {}

	@Override
	public void onFragmentStart(Fragment fragment) {}

	@Override
	public void onFragmentResume(Fragment fragment) {}

	@Override
	public void onFragmentPause(Fragment fragment) {}

	@Override
	public void onFragmentSaveInstanceState(Fragment fragment, Bundle outState) {}

	@Override
	public void onFragmentStop(Fragment fragment) {}

	@Override
	public void onFragmentDestroyView(Fragment fragment) {}

	@Override
	public void onFragmentDestroy(Fragment fragment) {}

	@Override
	public void onFragmentDetach(Fragment fragment) {}
}
