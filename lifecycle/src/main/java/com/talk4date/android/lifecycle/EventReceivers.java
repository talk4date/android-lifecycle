package com.talk4date.android.lifecycle;

import java.util.HashSet;
import java.util.Set;

/**
 * List of event receivers which makes sure destroyed event receivers are removed from the list,
 * and also allows simply posting of events to all contained eventreceivers.
 */
public class EventReceivers<T> implements EventReceiver.OnDestroyListener<T> {
	private Set<EventReceiver<T>> eventReceivers = new HashSet<>();

	/**
	 * Registers the given event receiver to be notified of events. If the event receiver was previously registered,
	 * this method is a no-op.
	 */
	public void registerEventReceiver(EventReceiver<T> eventReceiver) {
		if (!eventReceivers.contains(eventReceiver)) {
			eventReceivers.add(eventReceiver);
			eventReceiver.addOnDestroyListener(this);
		}
	}

	/**
	 * Removes the given event receiver, so it is no longer notified of events. If the given event receiver
	 * was not previously registered, this method is a noop.
	 */
	public void unregisterEventReceiver(EventReceiver<T> eventReceiver) {
		eventReceiver.removeOnDestroyListener(this);
		eventReceivers.remove(eventReceiver);
	}

	@Override
	public void onDestroy(EventReceiver<T> eventReceiver) {
		unregisterEventReceiver(eventReceiver);
	}

	public void postEvent(T event) {
		for (EventReceiver<T> receiver : eventReceivers) {
			receiver.postEvent(event);
		}
	}
}
