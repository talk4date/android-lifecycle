package com.talk4date.android.lifecycle;

import java.util.ArrayList;
import java.util.List;

/**
 * List of event receivers which makes sure destroyed event receivers are removed from the list,
 * and also allows simply posting of events to all contained eventreceivers.
 */
public class EventReceivers<T> implements EventReceiver.OnDestroyListener<T> {
	private List<EventReceiver<T>> eventReceivers = new ArrayList<>();

	public void registerEventReceiver(EventReceiver<T> eventReceiver) {
		eventReceivers.add(eventReceiver);
		eventReceiver.addOnDestroyListener(this);
	}

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
