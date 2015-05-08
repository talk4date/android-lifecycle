package com.talk4date.android.lifecycle.callbacks;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Callbacks for all fragment lifecycle events.
 */
public interface FragmentLifecycleCallbacks {

	void onFragmentAttach(Fragment fragment, Activity activity);

	void onFragmentCreate(Fragment fragment, Bundle savedInstanceState);

	void onFragmentCreateView(Fragment fragment, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

	void onFragmentViewCreated(Fragment fragment, View view, Bundle savedInstanceState);

	void onFragmentActivityCreated(Fragment fragment, Bundle savedInstanceState);

	void onFragmentViewStateRestored(Fragment fragment, Bundle savedInstanceState);

	void onFragmentStart(Fragment fragment);

	void onFragmentResume(Fragment fragment);

	void onFragmentPause(Fragment fragment);

	void onFragmentSaveInstanceState(Fragment fragment, Bundle outState);

	void onFragmentStop(Fragment fragment);

	void onFragmentDestroyView(Fragment fragment);

	void onFragmentDestroy(Fragment fragment);

	void onFragmentDetach(Fragment fragment);
}