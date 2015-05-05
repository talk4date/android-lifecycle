package com.talk4date.android.lifecycle;

/**
 * Base class for FragmentLifecycle and Activity Lifecycle.
 */
public class ActivityBasedLifecycle extends BaseLifecycle {

	/**
	 * @see #isRestored()
	 */
	protected boolean restored = false;

	/**
	 * @see #isNew()
	 */
	protected boolean newLifecycle = true;

	/**
	 * Indicates if the lifecycle was restored.
	 * If true the lifecycle was recreated because the process was destroyed and recreated.
	 * In this case we might have have lost or missed events.
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
}
