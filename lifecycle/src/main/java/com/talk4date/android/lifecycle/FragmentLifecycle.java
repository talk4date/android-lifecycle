package com.talk4date.android.lifecycle;

import android.app.Fragment;
import android.os.Bundle;

import com.talk4date.android.lifecycle.callbacks.AbstractFragmentLifecycleCallbacks;
import com.talk4date.android.lifecycle.callbacks.FragmentLifecycleCallbacks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static com.talk4date.android.lifecycle.FragmentLifecycleType.*;

/**
 * A lifecycle of a fragment.
 *
 * There are two types of lifecycles: the instance and the session lifecycles.
 * For a detailed description see {@link #sessionLifecycle(android.app.Fragment)} and
 * {@link #instanceLifecycle(android.app.Fragment)}.
 */
public class FragmentLifecycle extends ActivityBasedLifecycle {

	private static final Logger log = LoggerFactory.getLogger(FragmentLifecycle.class);

	/**
	 * An id that is unique for the current process.
	 * Used to avoid id clashes through app restarts.
	 */
	private static String processUniqueId = UUID.randomUUID().toString();

	/**
	 * The last used fragment id inside the process.
	 */
	private static int lastId = 0;

	/**
	 * Get a new globally unique fragment id.
	 */
	private static String newFragmentId() {
		lastId ++;
		return processUniqueId + lastId;
	}

	/**
	 * We assign all fragments an id.
	 * The fragment either gets the id from instance state or when created it gets a new id.
	 */
	private static Map<Fragment, String> fragmentToId = new HashMap<>();

	/**
	 * All existing fragment lifecycles.
	 * Mapped type -> id -> lifecycle.
	 *
	 * The current implementation eagerly creates and manages the session and instance lifecycle.
	 * We could probably safe some memory when we only track the current fragment states and create lifecycles on demand.
	 */
	private static EnumMap<FragmentLifecycleType, Map<String, FragmentLifecycle>> lifecycles;

	static {
		lifecycles = new EnumMap<>(FragmentLifecycleType.class);
		for (FragmentLifecycleType type : FragmentLifecycleType.values()) {
			lifecycles.put(type, new HashMap<String, FragmentLifecycle>());
		}
	}

	/**
	 * The instance lifecycle is directly tied to the fragment instance and destroyed when the instance is destroyed.
	 * It is only active when the fragment is in resumed state.
	 *
	 * @param fragment The fragment for which to get the lifecycle.
	 */
	public static FragmentLifecycle instanceLifecycle(Fragment fragment) {
		logStatistics();
		return lifecycle(INSTANCE, fragment);
	}

	/**
	 * The session lifecycle is retained across configuration changes and app kills.
	 * It is only active when the fragment is in resumed state.
	 *
	 * When the fragments hosting activity is destroyed, the lifecycle is destroyed too.
	 *
	 * When the fragment is actively removed from the activities fragment manager, the lifecycle is destroyed too.
	 * However when it's state is saved like for example FragmentStatePagerAdapter does,
	 * it will be restored later when a fragment with the same state is recreated.
	 *
	 * @param fragment The fragment for which to get the lifecycle.
	 */
	public static FragmentLifecycle sessionLifecycle(Fragment fragment) {
		logStatistics();
		return lifecycle(SESSION, fragment);
	}

	/**
	 * Get the lifecycle for the given type and fragment.
	 */
	private static FragmentLifecycle lifecycle(FragmentLifecycleType type, Fragment fragment) {
		String id = fragmentToId.get(fragment);
		if (id == null) {
			throw new IllegalStateException("Fragment was not registered. " +
					"Are you sure that the FragmentLifecycle callbacks are registered correctly?");
		}

		FragmentLifecycle lifecycle = lifecycles.get(type).get(fragmentToId.get(fragment));
		if (lifecycle == null) {
			throw new IllegalStateException("Lifecycle was null, caused probably a bug in FragmentLifecycle.");
		}

		return lifecycle;
	}

	private static void logStatistics() {
		if (log.isDebugEnabled()) {
			log.debug("Current fragment to id mappings: {}", fragmentToId.size());
			log.debug("Current session lifecycles: {}", lifecycles.get(SESSION).size());
			log.debug("Current instance lifecycles: {}", lifecycles.get(INSTANCE).size());
		}
	}

	/**
	 * Global listener that drives the fragment lifecycles.
	 */
	private static class FragmentLifecycleListener extends AbstractFragmentLifecycleCallbacks {

		private static final Logger log = LoggerFactory.getLogger(FragmentLifecycleListener.class);

		private static final String STATE_LIFECYCLE_ID = "STATE_TALK4DATE_LIFECYCLE_ID";

		@Override
		public void onFragmentCreate(Fragment fragment, Bundle savedInstanceState) {
			Map<String, FragmentLifecycle> sessionLifecycles = lifecycles.get(SESSION);
			Map<String, FragmentLifecycle> instanceLifecycles = lifecycles.get(INSTANCE);

			// new fragment -> new lifecycles
			if (savedInstanceState == null) {
				String id = newFragmentId();
				fragmentToId.put(fragment, id);

				sessionLifecycles.put(id, new FragmentLifecycle());
				instanceLifecycles.put(id, new FragmentLifecycle());
			// existing fragment id
			} else {
				String id = savedInstanceState.getString(STATE_LIFECYCLE_ID);
				if (id == null) {
					throw new IllegalArgumentException("Missing fragment id in instance state of fragment.");
				}
				fragmentToId.put(fragment, id);

				// instance lifecycle is always new
				instanceLifecycles.put(id, new FragmentLifecycle());

				// session lifecycle might still exist
				FragmentLifecycle sessionLifecycle;

				if (sessionLifecycles.containsKey(id)) {
					// existing lifecycle
					sessionLifecycle = sessionLifecycles.get(id);
					sessionLifecycle.restored = false;
				} else {
					// restored lifecycle
					sessionLifecycle = new FragmentLifecycle();
					sessionLifecycle.restored = true;
					sessionLifecycles.put(id, sessionLifecycle);
				}

				sessionLifecycle.newLifecycle = false;
			}
		}

		@Override
		public void onFragmentResume(Fragment fragment) {
			lifecycle(SESSION, fragment).setActive(true);
			lifecycle(INSTANCE, fragment).setActive(true);
		}

		@Override
		public void onFragmentPause(Fragment fragment) {
			lifecycle(SESSION, fragment).setActive(false);
			lifecycle(INSTANCE, fragment).setActive(false);
		}

		@Override
		public void onFragmentDestroy(Fragment fragment) {
			String id = fragmentToId.get(fragment);

			// instance lifecycles are always destroyed
			lifecycles.get(INSTANCE).remove(id);

			// session lifecycles are either destroyed or listeners got invalid
			FragmentLifecycle sessionLifecycle = lifecycle(SESSION, fragment);

			if (fragment.isRemoving() || fragment.getActivity().isFinishing()) {
				lifecycles.get(SESSION).remove(id);
				sessionLifecycle.destroy();
			} else {
				sessionLifecycle.invalidateEventListeners();
			}

			// remove the fragment to id mapping
			fragmentToId.remove(fragment);
		}

		@Override
		public void onFragmentSaveInstanceState(Fragment fragment, Bundle outState) {
			outState.putString(STATE_LIFECYCLE_ID, fragmentToId.get(fragment));
		}
	}

	// listener registration

	private static FragmentLifecycleListener listener = new FragmentLifecycleListener();

	/**
	 * Provides the callbacks that need to be registered once in the application.
	 */
	public static FragmentLifecycleCallbacks getCallbacks() {
		return listener;
	}
}
