package com.talk4date.android.lifecycle.callbacks;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Registry for fragment lifecycle callbacks.
 */
public class FragmentLifecycleDispatcher implements FragmentLifecycleCallbacks {

	private static FragmentLifecycleDispatcher instance = new FragmentLifecycleDispatcher();

	/**
	 * Callbacks that get notified for every fragment.
	 */
	private LinkedList<FragmentLifecycleCallbacks> callbacks = new LinkedList<>();

	/**
	 * Callbacks that only get notified for the fragment they registered.
	 */
	private HashMap<Fragment, LinkedList<FragmentLifecycleCallbacks>> byFragmentCallbacks = new HashMap<>();

	private FragmentLifecycleDispatcher() {}

	/**
	 * Get the singleton fragment lifecycle dispatcher.
	 */
	public static FragmentLifecycleDispatcher get() {
		return instance;
	}

	/**
	 * Adds fragment lifecycle callbacks that get notified for the given fragment.
	 * This callbacks are automatically removed when the fragment is destroyed.
	 * Therefore it is not necessary to manually remove them.
	 *
	 * @param fragment The fragment for which to add the callbacks.
	 * @param callbacks The callbacks to trigger.
	 */
	public synchronized void addFragmentLifecycleCallbacks(Fragment fragment, FragmentLifecycleCallbacks callbacks) {
		LinkedList<FragmentLifecycleCallbacks> callbacksForFragment = byFragmentCallbacks.get(fragment);
		if (callbacksForFragment == null) {
			callbacksForFragment = new LinkedList<>();
			byFragmentCallbacks.put(fragment, callbacksForFragment);
		}
		callbacksForFragment.add(callbacks);
	}

	/**
	 * Removes previously registered callbacks for a specific fragment.
	 * @see #addFragmentLifecycleCallbacks(Fragment, FragmentLifecycleCallbacks)
	 */
	public synchronized void removeFragmentLifecycleCallbacks(Fragment fragment, FragmentLifecycleCallbacks callbacks) {
		List<FragmentLifecycleCallbacks> callbacksForFragment = byFragmentCallbacks.get(fragment);
		if (callbacksForFragment != null) {
			callbacksForFragment.remove(callbacks);
		}
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

		List<FragmentLifecycleCallbacks> callbacksForFragment = byFragmentCallbacks.get(fragment);
		if (callbacksForFragment != null) {
			for (FragmentLifecycleCallbacks callback : callbacksForFragment) {
				callback.onFragmentAttach(fragment, activity);
			}
		}
	}

	@Override
	public void onFragmentCreate(Fragment fragment, Bundle savedInstanceState) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentCreate(fragment, savedInstanceState);
		}

		List<FragmentLifecycleCallbacks> callbacksForFragment = byFragmentCallbacks.get(fragment);
		if (callbacksForFragment != null) {
			for (FragmentLifecycleCallbacks callback : callbacksForFragment) {
				callback.onFragmentCreate(fragment, savedInstanceState);
			}
		}
	}

	@Override
	public void onFragmentViewCreated(Fragment fragment, View view, Bundle savedInstanceState) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentViewCreated(fragment, view, savedInstanceState);
		}

		List<FragmentLifecycleCallbacks> callbacksForFragment = byFragmentCallbacks.get(fragment);
		if (callbacksForFragment != null) {
			for (FragmentLifecycleCallbacks callback : callbacksForFragment) {
				callback.onFragmentViewCreated(fragment, view, savedInstanceState);
			}
		}
	}

	@Override
	public void onFragmentActivityCreated(Fragment fragment, Bundle savedInstanceState) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentActivityCreated(fragment, savedInstanceState);
		}

		List<FragmentLifecycleCallbacks> callbacksForFragment = byFragmentCallbacks.get(fragment);
		if (callbacksForFragment != null) {
			for (FragmentLifecycleCallbacks callback : callbacksForFragment) {
				callback.onFragmentActivityCreated(fragment, savedInstanceState);
			}
		}
	}

	@Override
	public void onFragmentViewStateRestored(Fragment fragment, Bundle savedInstanceState) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentViewStateRestored(fragment, savedInstanceState);
		}

		List<FragmentLifecycleCallbacks> callbacksForFragment = byFragmentCallbacks.get(fragment);
		if (callbacksForFragment != null) {
			for (FragmentLifecycleCallbacks callback : callbacksForFragment) {
				callback.onFragmentViewStateRestored(fragment, savedInstanceState);
			}
		}
	}

	@Override
	public void onFragmentStart(Fragment fragment) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentStart(fragment);
		}

		List<FragmentLifecycleCallbacks> callbacksForFragment = byFragmentCallbacks.get(fragment);
		if (callbacksForFragment != null) {
			for (FragmentLifecycleCallbacks callback : callbacksForFragment) {
				callback.onFragmentStart(fragment);
			}
		}
	}

	@Override
	public void onFragmentResume(Fragment fragment) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentResume(fragment);
		}

		List<FragmentLifecycleCallbacks> callbacksForFragment = byFragmentCallbacks.get(fragment);
		if (callbacksForFragment != null) {
			for (FragmentLifecycleCallbacks callback : callbacksForFragment) {
				callback.onFragmentResume(fragment);
			}
		}
	}

	@Override
	public void onFragmentPause(Fragment fragment) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentPause(fragment);
		}

		List<FragmentLifecycleCallbacks> callbacksForFragment = byFragmentCallbacks.get(fragment);
		if (callbacksForFragment != null) {
			for (FragmentLifecycleCallbacks callback : callbacksForFragment) {
				callback.onFragmentPause(fragment);
			}
		}
	}

	@Override
	public void onFragmentSaveInstanceState(Fragment fragment, Bundle outState) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentSaveInstanceState(fragment, outState);
		}

		List<FragmentLifecycleCallbacks> callbacksForFragment = byFragmentCallbacks.get(fragment);
		if (callbacksForFragment != null) {
			for (FragmentLifecycleCallbacks callback : callbacksForFragment) {
				callback.onFragmentSaveInstanceState(fragment, outState);
			}
		}
	}

	@Override
	public void onFragmentStop(Fragment fragment) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentStop(fragment);
		}

		List<FragmentLifecycleCallbacks> callbacksForFragment = byFragmentCallbacks.get(fragment);
		if (callbacksForFragment != null) {
			for (FragmentLifecycleCallbacks callback : callbacksForFragment) {
				callback.onFragmentStop(fragment);
			}
		}
	}

	@Override
	public void onFragmentDestroyView(Fragment fragment) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentDestroyView(fragment);
		}

		List<FragmentLifecycleCallbacks> callbacksForFragment = byFragmentCallbacks.get(fragment);
		if (callbacksForFragment != null) {
			for (FragmentLifecycleCallbacks callback : callbacksForFragment) {
				callback.onFragmentDestroyView(fragment);
			}
		}
	}

	@Override
	public void onFragmentDestroy(Fragment fragment) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentDestroy(fragment);
		}

		List<FragmentLifecycleCallbacks> callbacksForFragment = byFragmentCallbacks.get(fragment);
		if (callbacksForFragment != null) {
			for (FragmentLifecycleCallbacks callback : callbacksForFragment) {
				callback.onFragmentDestroy(fragment);
			}
		}

		byFragmentCallbacks.remove(fragment);
	}

	@Override
	public void onFragmentDetach(Fragment fragment) {
		for (FragmentLifecycleCallbacks callback : this.callbacks) {
			callback.onFragmentDetach(fragment);
		}

		List<FragmentLifecycleCallbacks> callbacksForFragment = byFragmentCallbacks.get(fragment);
		if (callbacksForFragment != null) {
			for (FragmentLifecycleCallbacks callback : callbacksForFragment) {
				callback.onFragmentDetach(fragment);
			}
		}
	}
}
