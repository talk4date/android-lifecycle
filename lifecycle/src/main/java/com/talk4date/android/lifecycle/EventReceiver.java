package com.talk4date.android.lifecycle;

/**
 * A receiver of events.
 *
 * @param <T> The type of events received by this event receiver.
 */
public interface EventReceiver<T> {

	/**
	 * Notifies the receiver about a new event.
	 * @param event The event.
	 */
	void postEvent(T event);

	/**
	 * True when the event receiver is destroyed.
	 * This means that events posted to the receiver will not be processed anymore.
	 */
	boolean isDestroyed();

	/**
	 * Adds a listener that is notified when the event receiver is destroyed.
	 * @see #isDestroyed()
	 */
	void addOnDestroyListener(OnDestroyListener listener);

	/**
	 * Removes a previously added destroy listener.
	 */
	void removeOnDestroyListener(OnDestroyListener listener);

	public static interface OnDestroyListener {

		/**
		 * Called when the event receiver is destroyed.
		 */
		void onDestroy();
	}
}