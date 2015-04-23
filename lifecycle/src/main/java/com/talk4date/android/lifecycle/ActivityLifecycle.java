package com.talk4date.android.lifecycle;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A lifecycle of an activity.
 * It is inactive when the activity is paused.
 *
 * The lifecycle can either be retained across configuration changes (ActivitySession)
 * or die directly together with the activity on every configuration change.
 *
 * The lifecycle internally is represented by a worker fragment.
 */
public class ActivityLifecycle extends BaseLifecycle {

	private static final Logger log = LoggerFactory.getLogger(ActivityLifecycle.class);

	private static final String TAG_ACTIVITY_SESSION_LIFECYCLE_FRAGMENT = "ACTIVITY_SESSION_LIFECYCLE_FRAGMENT";
	private static final String TAG_ACTIVITY_LIFECYCLE_FRAGMENT = "ACTIVITY_LIFECYCLE_FRAGMENT";

	/**
	 * @see #isRestored()
	 */
	private boolean restored = false;

	/**
	 * Flag indicating if this lifecycle was created for the current activity.
	 */
	private boolean newLifecycle = true;

	/**
	 * Private constructor. Use #sessionLifecylce to obtain instances.
	 */
	private ActivityLifecycle() {}

	/**
	 * Get the session lifecycle for the given activity.
	 * The returned lifecycle will be kept alive across configuration changes.
	 *
	 * @param activity The activity for which to get the activity session lifecycle.
	 */
	public static ActivityLifecycle activitySessionLifecycle(Activity activity) {
		return lifecycle(activity, true, TAG_ACTIVITY_SESSION_LIFECYCLE_FRAGMENT);
	}

	/**
	 * Get the simple lifecycle for the given activity.
	 * The returned lifecycle will be destroyed on configuration changes.
	 *
	 * @param activity The activity for which to get the activity lifecycle.
	 */
	public static ActivityLifecycle activityLifecycle(Activity activity) {
		return lifecycle(activity, false, TAG_ACTIVITY_LIFECYCLE_FRAGMENT);
	}

	private static ActivityLifecycle lifecycle(Activity activity, boolean retain, String tag) {
		ActivityLifecycleFragment fragment = (ActivityLifecycleFragment)
				activity.getFragmentManager().findFragmentByTag(tag);

		if (fragment == null) {
			fragment = new ActivityLifecycleFragment().withRetention(retain);
			fragment.restoredFromFragmentState = false;
			fragment.lifecycle.newLifecycle = true;
			activity.getFragmentManager().beginTransaction()
					.add(fragment, tag)
					.commit();
		} else if (retain && !fragment.configured) {
			// fragment already existed, but wasn't configured for this activity yet

			// fragment existed, lifecycle must be old
			fragment.lifecycle.newLifecycle = false;

			if (fragment.restoredFromFragmentState) {
				fragment.restoredFromFragmentState = false;
				fragment.lifecycle.restored = true;
			} else {
				fragment.lifecycle.restored = false;
			}
		}

		fragment.configured = true;
		log.trace("lifecycle fragment {}", fragment);
		return fragment.lifecycle;
	}


	/**
	 * Indicates if the lifecycle was restored - the session was resumed although the process was destroyed and recreated.
	 * If this is true, we might have lost events, because we store events only within the process.
	 */
	public boolean isRestored() {
		return restored;
	}

	/**
	 * Lifecycle was just created for the currently active activity instance.
	 */
	public boolean isNew() {
		return newLifecycle;
	}

	/**
	 * This method allows callers to have one method to check if callback objects might have been lost.
	 * @return true if either {@link #isNew()} or {@link #isRestored()} is true.
	 */
	public boolean isNewOrRestored() {
		return isNew() || isRestored();
	}

	/**
	 * Fragment that holds and controls the activity lifecycle.
	 */
	public static class ActivityLifecycleFragment extends Fragment {

		private static final Logger log = LoggerFactory.getLogger(ActivityLifecycle.class);

		ActivityLifecycle lifecycle = new ActivityLifecycle();

		/**
		 * Whether the fragment was restored by the system or created ourselves.
		 */
		private boolean restoredFromFragmentState = true;

		/**
		 * Flag indicating if the fragment has been configured for this activity instance.
		 */
		private boolean configured = false;

		/**
		 * Configure if the lifecycle fragment should be retained across configuration changes.
		 */
		public ActivityLifecycleFragment withRetention(boolean retention) {
			setRetainInstance(retention);
			return this;
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			log.trace("onAttach -> activating");
			lifecycle.setActive(true);
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			log.trace("onCreate -> activating");
			lifecycle.setActive(true);
		}

		@Override
		public void onPause() {
			super.onPause();
			log.trace("onPause -> deactivating");
			lifecycle.setActive(false);
		}

		@Override
		public void onResume() {
			super.onResume();
			log.trace("onResume -> activating");
			lifecycle.setActive(true);
		}

		@Override
		public void onDetach() {
			super.onDetach();
			log.trace("detach -> invalidating event listeners");
			lifecycle.invalidateEventListeners();
			configured = false;
		}

		@Override
		public String toString() {
			return "ActivityLifecycleFragment { \n"
					+ "retained: " + getRetainInstance() + ", "
					+ "restoredFromFragmentState: " + restoredFromFragmentState + ", "
					+ "configured: " + configured + " }";
		}
	}
}
