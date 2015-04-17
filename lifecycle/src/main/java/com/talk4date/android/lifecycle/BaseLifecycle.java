package com.talk4date.android.lifecycle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Abstract base class for implementing lifecycles.
 */
public abstract class BaseLifecycle implements Lifecycle {

	/**
	 * Listeners that get notified when the active state changes.
	 */
	private List<Runnable> activeChangeListeners = new LinkedList<>();

	/**
	 * All LifecycleEventDispatchers by tag.
	 */
	Map<String, LifecycleEventDispatcher<?>> eventDispatchers = new HashMap<>();

	/**
	 * If the lifecycle is currently active.
	 */
	private boolean active = false;

	/**
	 * Notifies all active change listeners about a change.
	 */
	private void notifyActiveChangeListeners() {
		for (Runnable listener : activeChangeListeners) {
			listener.run();
		}
	}

	/**
	 * Set the lifecycle active / inactive.
	 */
	protected void setActive(boolean active) {
		if (this.active != active) {
			this.active = active;
			notifyActiveChangeListeners();
		}
	}

	@Override
	public void addActiveChangeListener(Runnable listener) {
		this.activeChangeListeners.add(listener);
	}

	@Override
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets all event listeners to null.
	 * Use this to mark event listeners as invalid.
	 * The need to be re-registered again with registerListener later.
	 */
	protected void invalidateEventListeners() {
		for (LifecycleEventDispatcher<?> dispatcher : eventDispatchers.values()) {
			dispatcher.setListener(null);
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
}
