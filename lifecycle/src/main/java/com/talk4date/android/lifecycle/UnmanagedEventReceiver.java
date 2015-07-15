package com.talk4date.android.lifecycle;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

/**
 * An event receiver which is not managed by a lifecycle, can be used if a event receiver is required
 * but no lifecycle is required.
 */
public abstract class UnmanagedEventReceiver<T> implements EventReceiver<T>, EventListener<T> {

	private Handler mainHandler = new Handler(Looper.getMainLooper());

	private boolean destroyed = false;

	List<OnDestroyListener<T>> listeners = new ArrayList<>();

	@Override
	public void postEvent(final T event) {
		if (isDestroyed()) {
			return;
		}

		if (Looper.myLooper() == Looper.getMainLooper()) {
			onEvent(event);
		} else {
			mainHandler.post(new Runnable() {
				@Override
				public void run() {
					onEvent(event);
				}
			});
		}
	}

	@Override
	public boolean isDestroyed() {
		return destroyed;
	}

	public void destroy() {
		this.destroyed = true;
		notifyOnDestroyListeners();
	}

	private void notifyOnDestroyListeners() {
		for (OnDestroyListener listener : listeners) {
			listener.onDestroy(this);
		}
	}

	@Override
	public void addOnDestroyListener(OnDestroyListener<T> listener) {
		listeners.add(listener);
	}

	@Override
	public void removeOnDestroyListener(OnDestroyListener<T> listener) {
		listeners.remove(listener);
	}
}
