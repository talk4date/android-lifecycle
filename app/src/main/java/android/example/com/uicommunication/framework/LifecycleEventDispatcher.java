package android.example.com.uicommunication.framework;

import android.example.com.uicommunication.framework.EventListener;
import android.example.com.uicommunication.framework.EventReceiver;
import android.example.com.uicommunication.framework.Lifecycle;
import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An event dispatcher that dispatches events to a listener when its lifecycle is active.
 * @param <T> The type of event for this dispatcher.
 */
public class LifecycleEventDispatcher<T> implements EventReceiver<T> {

	private static final Logger log = LoggerFactory.getLogger(LifecycleEventDispatcher.class);

	/**
	 * The lifecycle in which this event receiver forwards events to the listener.
	 */
	private Lifecycle lifecycle;

	/**
	 * The current listener for this LifecycleEventReceiver.
	 */
	@Nullable
	private EventListener<T> listener;

	/**
	 * If events should be stored when the lifecycle is inactive.
	 * If true the events will be forwarded to the listener as soon as the lifecycle becomes active again.
	 */
	private boolean storeWhileInactive;

	/**
	 * Events that have been stored for later dispatch.
	 */
	private List<T> pendingEvents = new ArrayList<>();

	/**
	 * Create a new lifecycle event receiver.
	 *
	 * @param lifecycle The lifecycle for this receiver.
	 * @param storeWhileInactive
	 */
	public LifecycleEventDispatcher(Lifecycle lifecycle, boolean storeWhileInactive) {
		this.lifecycle = lifecycle;
		this.storeWhileInactive = storeWhileInactive;

		lifecycle.addActiveChangeListener(new Runnable() {
			@Override
			public void run() {
				dispatchPendingIfReady();
			}
		});
	}

	/**
	 * Sets the listener for this event dispatcher.
	 * If there are pending events in the queue they are immediately
	 * dispatched if the lifecycle is active.
	 *
	 * @param listener The new event listener.
	 */
	public void setListener(@Nullable EventListener<T> listener) {
		this.listener = listener;
		dispatchPendingIfReady();
	}

	/**
	 * Checks if the event listener is currently ready to receive an event.
	 * This means that the lifecycle is active and we have a listener.
	 */
	private boolean readyForEvent() {
		return lifecycle.isActive() && listener != null;
	}

	/**
	 * Dispatches the given event to the listener.
	 * @param event The event to dispatch.
	 */
	private void dispatchEvent(T event) {
		if (listener == null) {
			throw new IllegalStateException("Tried to dispatch an event while the listener is null");
		}
		listener.onEvent(event);
	}

	/**
	 * Dispatches pending events when the listener is ready to receive events.
	 */
	private void dispatchPendingIfReady() {
		if (readyForEvent()) {
			if (log.isDebugEnabled() && !pendingEvents.isEmpty()) {
				log.debug("dispatching {} pending events", pendingEvents.size());
			}
			Iterator<T> it = pendingEvents.iterator();
			while (it.hasNext()) {
				dispatchEvent(it.next());
				it.remove();
			}
		}
	}

	@Override
	public void postEvent(T event) {
		log.trace("post event {}", event);

		if (readyForEvent()) {
			log.debug("directly dispatching event {}", event);
			dispatchEvent(event);
		} else {
			if (storeWhileInactive) {
				log.debug("storing event for later dispatch {}", event);
				pendingEvents.add(event);
			} else {
				log.debug("discarding event silently {}", event);
			}
		}
	}
}