package com.talk4date.android.lifecycle.sample.service;

import android.os.Handler;
import android.os.Looper;

import com.talk4date.android.lifecycle.EventReceiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Service that delivers the time past in seconds since the service was created.
 */
public class TimingService {

	private static final Logger log = LoggerFactory.getLogger(RandomNumberService.class);

	private static TimingService instance = new TimingService();

	/**
	 * Get the singleton instance of this service.
	 */
	public static TimingService getInstance() {
		return instance;
	}

	/**
	 * Handler on the main loop.
	 */
	private Handler handler = new Handler(Looper.getMainLooper());

	/**
	 * Time when the service was created.
	 */
	private long start = System.currentTimeMillis();

	/**
	 * List with receivers that get the time every second.
	 */
	private List<EventReceiver<Long>> receivers = new LinkedList<>();

	/**
	 * Add a receiver who gets the new time in seconds every second.
	 * To unregister the receiver use {@link #removeReceiver(EventReceiver)}.
	 * @param receiver The receiver to notify with new random numbers.
	 */
	public void addReceiver(final EventReceiver<Long> receiver) {
		receivers.add(receiver);
		log.debug("added receiver - total registered now {}", receivers.size());

		// for the first new receiver we need to kick off 1 second loop
		if (receivers.size() == 1) {
			postNewRandomNumber();
		}
	}

	/**
	 * Removes a receiver previously registered with {@link #addReceiver(EventReceiver)}.
	 */
	public void removeReceiver(final EventReceiver<Long> receiver) {
		boolean removed = receivers.remove(receiver);

		if (removed) {
			log.debug("removed receiver - total registered now {}", receivers.size());
		} else {
			log.debug("no receiver removed, receiver not found - total registered now {}", receivers.size());
		}
	}

	/**
	 * Posts a new random number and schedules itself to post a new one after 1 second.
	 */
	private void postNewRandomNumber() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				log.debug("posting random number to {} registered receivers", receivers.size());
				long pastSeconds = (System.currentTimeMillis() - start) / 1000;
				for (EventReceiver<Long> receiver : receivers) {
					receiver.postEvent(pastSeconds);
				}

				// if there is still someone registered, schedule us again
				if (!receivers.isEmpty()) {
					postNewRandomNumber();
				}
			}
		}, 1000);
	}
}