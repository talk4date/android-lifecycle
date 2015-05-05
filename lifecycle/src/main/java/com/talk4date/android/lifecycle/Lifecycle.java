package com.talk4date.android.lifecycle;

/**
 * A lifecycle for events.
 */
public interface Lifecycle {

	/**
	 * If the lifecycle is currently active.
	 */
	boolean isActive();

	/**
	 * True when the lifecycle is destroyed.
	 */
	boolean isDestroyed();

	/**
	 * Adds a listener to the lifecycle that is invoked when the active state changes.
	 */
	void addActiveChangeListener(ActiveChangeListener listener);

	/**
	 * Remove a previously added active change listener.
	 */
	void removeActiveChangeListener(ActiveChangeListener listener);

	/**
	 * Adds a listener to the lifecycle that is invoked when the lifecycle is destroyed.
	 */
	void addOnDestroyListener(OnDestroyListener listener);

	/**
	 * Removes a previously added on destroy listener.
	 */
	void removeOnDestroyListener(OnDestroyListener listener);

	/**
	 * Configures an event receiver in this lifecycle with the given listener.
	 * Events posted to the created EventReceiver will only be forwarded to the listener when the lifecycle is active.
	 *
	 * Multiple registrations with the same tag in the same lifecycle will cause the listener to be updated.
	 *
	 * @param tag A tag that identifies the listener
	 * @param storeWhileInactive If events should be stored and dispatched later when the lifecycle is inactive.
	 * @param listener The listener to invoke with the events from the event receiver.
	 * @return The event receiver.
	 */
	<T> EventReceiver<T> registerListener(String tag, boolean storeWhileInactive, EventListener<T> listener);

	public static interface ActiveChangeListener {

		/**
		 * Called when the active state of the lifecycle changes.
		 * @param active The new active state.
		 */
		void onActiveChange(boolean active);
	}

	public static interface OnDestroyListener {

		/**
		 * Called when the lifecycle is destroyed.
		 */
		void onDestroy();
	}
}
