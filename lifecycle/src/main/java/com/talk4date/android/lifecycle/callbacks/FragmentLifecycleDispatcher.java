package com.talk4date.android.lifecycle.callbacks;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;

/**
 * Registry for fragment lifecycle callbacks.
 */
public class FragmentLifecycleDispatcher implements FragmentLifecycleCallbacks {

	private static FragmentLifecycleDispatcher instance = new FragmentLifecycleDispatcher();

	private LinkedList<FragmentLifecycleCallbacks> callbacks = new LinkedList<>();

	private FragmentLifecycleDispatcher() {}

	/**
	 * Get the singleton fragment lifecycle dispatcher.
	 */
	public static FragmentLifecycleDispatcher get() {
		return instance;
	}

	/**
	 * Adds fragment lifecycle callbacks which are notified about all fragment lifecycle events.
	 */
	public synchronized void addFragmentLifecycleCallbacks(FragmentLifecycleCallbacks callbacks) {
		this.callbacks.add(callbacks);
	}

	/**
	 * Removes previously added lifecycle callbacks.
	 */
	public synchronized void removeFragmentLifecycleCallbacks(FragmentLifecycleCallbacks callbacks) {
		this.callbacks.remove(callbacks);
	}

	@Override
	public void onFragmentAttach(Fragment fragment, Activity activity) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentAttach(fragment, activity);
		}
	}

	@Override
	public void onFragmentCreate(Fragment fragment, Bundle savedInstanceState) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentCreate(fragment, savedInstanceState);
		}
	}

	@Override
	public void onFragmentCreateView(Fragment fragment, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentCreateView(fragment, inflater, container, savedInstanceState);
		}
	}

	@Override
	public void onFragmentViewCreated(Fragment fragment, View view, Bundle savedInstanceState) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentViewCreated(fragment, view, savedInstanceState);
		}
	}

	@Override
	public void onFragmentActivityCreated(Fragment fragment, Bundle savedInstanceState) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentActivityCreated(fragment, savedInstanceState);
		}
	}

	@Override
	public void onFragmentViewStateRestored(Fragment fragment, Bundle savedInstanceState) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentViewStateRestored(fragment, savedInstanceState);
		}
	}

	@Override
	public void onFragmentStart(Fragment fragment) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentStart(fragment);
		}
	}

	@Override
	public void onFragmentResume(Fragment fragment) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentResume(fragment);
		}
	}

	@Override
	public void onFragmentPause(Fragment fragment) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentPause(fragment);
		}
	}

	@Override
	public void onFragmentSaveInstanceState(Fragment fragment, Bundle outState) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentSaveInstanceState(fragment, outState);
		}
	}

	@Override
	public void onFragmentStop(Fragment fragment) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentStop(fragment);
		}
	}

	@Override
	public void onFragmentDestroyView(Fragment fragment) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentDestroyView(fragment);
		}
	}

	@Override
	public void onFragmentDestroy(Fragment fragment) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentDestroy(fragment);
		}
	}

	@Override
	public void onFragmentDetach(Fragment fragment) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentDetach(fragment);
		}
	}
}
