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

	private static final String TAG_ACTIVITY_SESSION_LIFECYCLE_FRAGMENT = "ACTIVITY_SESSION_LIFECYCLE_FRAGMENT";
	private static final String TAG_ACTIVITY_LIFECYCLE_FRAGMENT = "ACTIVITY_LIFECYCLE_FRAGMENT";

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
		ActivitySessionLifecycleFragment fragment = (ActivitySessionLifecycleFragment)
				activity.getFragmentManager().findFragmentByTag(tag);

		if (fragment == null) {
			fragment = new ActivitySessionLifecycleFragment().withRetention(retain);
			activity.getFragmentManager().beginTransaction()
					.add(fragment, tag)
					.commit();
		}

		return fragment.lifecycle;
	}

	/**
	 * Fragment that holds and controls the ActivitySessionLifecycle
	 */
	public static class ActivitySessionLifecycleFragment extends Fragment {

		private static final Logger log = LoggerFactory.getLogger(ActivityLifecycle.class);

		ActivityLifecycle lifecycle = new ActivityLifecycle();

		/**
		 * If the lifecycle fragment should be retained across configuration changes.
		 */
		private boolean retain = false;

		/**
		 * Configure if the lifecycle fragment should be retained across configuration changes.
		 */
		public ActivitySessionLifecycleFragment withRetention(boolean retention) {
			this.retain = retention;
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
			setRetainInstance(retain);
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
		}
	}
}
