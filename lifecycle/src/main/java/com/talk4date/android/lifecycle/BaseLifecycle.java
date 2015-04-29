package com.talk4date.android.lifecycle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Abstract base class for implementing lifecycles.
 * Implements event dispatching and listener registration.
 */
public abstract class BaseLifecycle implements Lifecycle {

	/**
	 * Listeners that get notified when the active state changes.
	 */
	private List<ActiveChangeListener> activeChangeListeners = new LinkedList<>();

	/**
	 * Listeners that get notified when the the lifecycle is destroyed.
	 */
	private List<OnDestroyListener> onDestroyListeners = new LinkedList<>();

	/**
	 * All LifecycleEventDispatchers by tag.
	 */
	Map<String, LifecycleEventDispatcher<?>> eventDispatchers = new HashMap<>();

	/**
	 * If the lifecycle is currently active.
	 */
	private boolean active = false;

	/**
	 * If the lifecycle is destroyed.
	 */
	private boolean destroyed = false;

	/**
	 * Set the lifecycle active / inactive.
	 */
	protected void setActive(boolean active) {
		if (this.active != active) {
			this.active = active;
			for (ActiveChangeListener listener : activeChangeListeners) {
				listener.onActiveChange(active);
			}
		}
	}

	/**
	 * Sets all event listeners to null.
	 * Use this to mark event listeners as invalid.
	 * They need to be re-registered again with registerListener later.
	 *
	 * Subclasses must call this when event listeners get invalid.
	 */
	protected void invalidateEventListeners() {
		for (LifecycleEventDispatcher<?> dispatcher : eventDispatchers.values()) {
			dispatcher.setListener(null);
		}
	}

	/**
	 * Subclasses must call this when the lifecycle is destroyed.
	 * This automatically invalidates all listeners, so no extra call to invalidateEventListeners is needed.
	 */
	protected void destroy() {
		this.destroyed = true;
		invalidateEventListeners();
		for (OnDestroyListener listener : onDestroyListeners) {
			listener.onDestroy();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> EventReceiver<T> registerListener(String tag, boolean storeWhileInactive, EventListener<T> listener) {
		LifecycleEventDispatcher<T> eventDispatcher = (LifecycleEventDispatcher<T>) eventDispatchers.get(tag);
		if (eventDispatcher == null) {
			eventDispatcher = new LifecycleEventDispatcher<>(this, storeWhileInactive);
			eventDispatchers.put(tag, eventDispatcher);
		}

		eventDispatcher.setListener(listener);
		return eventDispatcher;
	}

	@Override
	public void addActiveChangeListener(ActiveChangeListener listener) {
		this.activeChangeListeners.add(listener);
	}

	@Override
	public void removeActiveChangeListener(ActiveChangeListener listener) {
		this.activeChangeListeners.remove(listener);
	}

	@Override
	public void addOnDestroyListener(OnDestroyListener listener) {
		this.onDestroyListeners.add(listener);
	}

	@Override
	public void removeOnDestroyListener(OnDestroyListener listener) {
		this.onDestroyListeners.remove(listener);
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public boolean isDestroyed() {
		return destroyed;
	}
}
