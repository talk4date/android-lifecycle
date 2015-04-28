package com.talk4date.android.lifecycle.sample;

import android.app.Application;

import com.talk4date.android.lifecycle.callbacks.FragmentLifecycleDispatcher;
import com.talk4date.android.lifecycle.util.FragmentLifecycleLoggingCallbacks;

/**
 * Basic sample application which registers global lifecycles.
 */
public class SampleApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		FragmentLifecycleDispatcher.get().addFragmentLifecycleCallbacks(new FragmentLifecycleLoggingCallbacks());
	}
}
