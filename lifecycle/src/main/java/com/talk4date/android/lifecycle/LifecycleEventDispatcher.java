package com.talk4date.android.lifecycle;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * An event dispatcher that dispatches events to a listener when its lifecycle is active.
 * @param <T> The type of event for this dispatcher.
 */
public class LifecycleEventDispatcher<T> implements EventReceiver<T>, Lifecycle.OnDestroyListener, Lifecycle.ActiveChangeListener {

	private static final Logger log = LoggerFactory.getLogger(LifecycleEventDispatcher.class);

	/**
	 * Handler used to dispatch events on the main thread.
	 */
	private final Handler mainHandler = new Handler(Looper.getMainLooper());

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
	 * List of all on destroy listeners.
	 */
	private List<OnDestroyListener<T>> onDestroyListeners = new LinkedList<>();

	/**
	 * If the lifecycle has been destroyed.
	 */
	private boolean destroyed = false;

	/**
	 * Create a new lifecycle event receiver.
	 *
	 * @param lifecycle The lifecycle for this receiver.
	 * @param storeWhileInactive If events should be stored when the lifecycle is inactive.
	 */
	public LifecycleEventDispatcher(Lifecycle lifecycle, boolean storeWhileInactive) {
		this.lifecycle = lifecycle;
		this.storeWhileInactive = storeWhileInactive;

		lifecycle.addActiveChangeListener(this);
		lifecycle.addOnDestroyListener(this);
	}

	/**
	 * Lifecycle on active change listener.
	 */
	@Override
	public void onActiveChange(boolean active) {
		dispatchPendingIfReady();
	}

	/**
	 * Lifecycle on destroy listener.
	 * Propagate to our listeners that we are destroyed.
	 */
	@Override
	public void onDestroy() {
		this.destroyed = true;

		for (OnDestroyListener<T> listener : onDestroyListeners) {
			listener.onDestroy(this);
		}

		// Long running processes might still have references to us and keep us and alive.
		// Therefore we give up everything not needed to free all resources as soon as possible.
		this.lifecycle = null;
		this.listener = null;
		this.pendingEvents = null;
		this.onDestroyListeners.clear();
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
	 * Dispatches the given event to the listener. Must be called on the UI thread.
	 * @param event The event to dispatch.
	 */
	private void dispatchEvent(final T event) {
		if (listener == null) {
			throw new IllegalStateException("Tried to dispatch an event while the listener is null");
		}
		listener.onEvent(event);
	}

	/**
	 * Dispatches pending events when the listener is ready to receive events.
	 * Must be called on the UI thread.
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
	public void postEvent(final T event) {
		log.trace("post event {}", event);
		if (Looper.myLooper() == Looper.getMainLooper()) {
			postEventSync(event);
		} else {
			mainHandler.post(new Runnable() {
				@Override
				public void run() {
					postEventSync(event);
				}
			});
		}
	}

	/**
	 * Posts event on the current thread, basically a wrapper used by {@link #postEvent(Object)} to ensure we are
	 * on the UI thread.
	 */
	private void postEventSync(T event) {
		if (destroyed) {
			log.debug("lifecylce already destroyed, discarding event silently");
		} else if (readyForEvent()) {
			log.debug("directly dispatching event {}", event);
			dispatchEvent(event);
		} else {
			if (storeWhileInactive) {
				log.debug("lifeycle not ready for event, storing event for later dispatch {}", event);
				pendingEvents.add(event);
			} else {
				log.debug("lifeycle not ready for event, discarding event silently {}", event);
			}
		}
	}

	@Override
	public boolean isDestroyed() {
		return destroyed;
	}

	@Override
	public void addOnDestroyListener(OnDestroyListener<T> listener) {
		this.onDestroyListeners.add(listener);
	}

	@Override
	public void removeOnDestroyListener(OnDestroyListener<T> listener) {
		this.onDestroyListeners.remove(listener);
	}
}