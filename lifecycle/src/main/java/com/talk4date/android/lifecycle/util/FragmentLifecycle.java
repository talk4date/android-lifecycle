package com.talk4date.android.lifecycle.util;

import android.app.Fragment;
import android.os.Bundle;

import com.talk4date.android.lifecycle.ActivityBasedLifecycle;
import com.talk4date.android.lifecycle.callbacks.AbstractFragmentLifecycleCallbacks;
import com.talk4date.android.lifecycle.callbacks.FragmentLifecycleCallbacks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A lifecycle of a fragment.
 * It is active only when the fragment is resumed.
 *
 * The lifecycle can either be retained across configuration changes (FragmentSession)
 * or die directly together with the fragment on every configuration change.
 *
 * The lifecycle will always be destroyed when the fragment is finished.
 */
public class FragmentLifecycle extends ActivityBasedLifecycle {

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
	 * Existing fragment lifecycles for all fragment ids.
	 *
	 * TODO: we still maintain old lifecycles. We need to properly destroy the lifecycle when it comes to an end.
	 */
	private static Map<String, FragmentLifecycle> idToSessionLifecycle = new HashMap<>();


	public static FragmentLifecycle fragmentSessionLifecycle(Fragment fragment) {
		String id = fragmentToId.get(fragment);
		if (id == null) {
			throw new IllegalStateException("Fragment was not registered. " +
					"Are you sure that the FragmentLifecycle callbacks are registered correctly?");
		}

		FragmentLifecycle lifecycle = idToSessionLifecycle.get(fragmentToId.get(fragment));
		if (lifecycle == null) {
			throw new IllegalStateException("Lifecycle was null, caused probably a bug in FragmentLifecycle.");
		}

		return lifecycle;
	}

	/**
	 * Global listener that drives the fragment lifecycles.
	 */
	private static class FragmentLifecycleListener extends AbstractFragmentLifecycleCallbacks {

		private static final Logger log = LoggerFactory.getLogger(FragmentLifecycleListener.class);

		private static final String STATE_LIFECYCLE_ID = "STATE_TALK4DATE_LIFECYCLE_ID";

		@Override
		public void onFragmentCreate(Fragment fragment, Bundle savedInstanceState) {
			// new fragment -> new lifecycle
			if (savedInstanceState == null) {
				String id = newFragmentId();
				fragmentToId.put(fragment, id);
				idToSessionLifecycle.put(id, new FragmentLifecycle());
			// existing fragment id -> existing lifecycle
			} else {
				String id = savedInstanceState.getString(STATE_LIFECYCLE_ID);
				if (id == null) {
					throw new IllegalArgumentException("Missing fragment id in instance state of fragment.");
				}

				FragmentLifecycle lifecycle;

				// existing lifecycle
				if (idToSessionLifecycle.containsKey(id)) {
					lifecycle = idToSessionLifecycle.get(id);
					lifecycle.restored = false;
				// restored lifecycle
				} else {
					lifecycle = new FragmentLifecycle();
					lifecycle.restored = true;
					idToSessionLifecycle.put(id, lifecycle);
				}

				lifecycle.newLifecycle = false;
			}

			if (log.isDebugEnabled()) {
				log.debug("Current fragment to id mappings: {}", fragmentToId.size());
				log.debug("Current session lifecycles: {}", idToSessionLifecycle.size());
			}
		}

		@Override
		public void onFragmentResume(Fragment fragment) {
			fragmentSessionLifecycle(fragment).setActive(true);
		}

		@Override
		public void onFragmentPause(Fragment fragment) {
			fragmentSessionLifecycle(fragment).setActive(false);
		}

		@Override
		public void onFragmentDestroy(Fragment fragment) {
			fragmentSessionLifecycle(fragment).invalidateEventListeners();
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
