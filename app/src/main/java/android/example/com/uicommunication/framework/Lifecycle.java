package android.example.com.uicommunication.framework;

/**
 * A lifecycle for events.
 */
public interface Lifecycle {

	/**
	 * If the lifecycle is currently active.
	 */
	boolean isActive();

	/**
	 * Adds a listener to the lifecycle that is invoked when the active state changes.
	 * @param listener The listener to invoke when the active state changes.
	 */
	void addActiveChangeListener(Runnable listener);

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
}
