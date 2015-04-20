package com.talk4date.android.lifecycle.sample.service;

import com.talk4date.android.lifecycle.EventReceiver;
import android.os.Handler;
import android.os.Looper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Service to get random numbers in an async way.
 */
public class RandomNumberService {

	private static final Logger log = LoggerFactory.getLogger(RandomNumberService.class);

	private static RandomNumberService instance = new RandomNumberService();

	/**
	 * Get the singleton instance of this service.
	 */
	public static RandomNumberService getInstance() {
		return instance;
	}

	private Random random = new Random();

	/**
	 * Handler on the main loop.
	 */
	private Handler handler = new Handler(Looper.getMainLooper());

	/**
	 * Counter to generate unique ids.
	 * @see #newUniqueId()
	 */
	private int lastUniqueId = 0;

	/**
	 * List with receivers that get a new random number every second.
	 */
	private List<EventReceiver<Integer>> everySecondReceivers = new LinkedList<>();

	/**
	 * Get a new random number after the given delay in seconds.
	 * @param delayInSeconds The delay after which to get the random number
	 * @param receiver The listener to invoke with the new random number
	 */
	public void getOneRandomNumber(int delayInSeconds, final EventReceiver<Integer> receiver) {
		final int id = newUniqueId();
		log.debug("starting random number with id {}", id);

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				log.debug("finished random number with id ", id);
				receiver.postEvent(random.nextInt());
			}
		}, delayInSeconds * 1000);
	}

	/**
	 * Posts a new random number to the given event receiver every second.
	 * To unregister the receiver use {@link #removeEverySecondReceiver(EventReceiver)}.
	 * @param receiver The receiver to notify with new random numbers.
	 */
	public void addEverySecondReceiver(final EventReceiver<Integer> receiver) {
		everySecondReceivers.add(receiver);

		// for the first new receiver we need to kick off 1 second loop
		if (everySecondReceivers.size() == 1) {
			postNewRandomNumber();
		}
	}

	/**
	 * Removes a receiver previously registered with {@link #addEverySecondReceiver(EventReceiver)}.
	 */
	public void removeEverySecondReceiver(final EventReceiver<Integer> receiver) {
		everySecondReceivers.remove(receiver);
	}

	/**
	 * Posts a new random number and schedules itself to post a new one after 1 second.
	 */
	private void postNewRandomNumber() {
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				log.debug("posting random number to {} registered receivers", everySecondReceivers.size());
				int number = random.nextInt();
				for (EventReceiver<Integer> receiver : everySecondReceivers) {
					receiver.postEvent(number);
				}

				// if there is still someone registered, schedule us again
				if (!everySecondReceivers.isEmpty()) {
					postNewRandomNumber();
				}
			}
		}, 1000);
	}

	/**
	 * Creates a new unique id.
	 * @return a new unique id.
	 */
	private int newUniqueId() {
		return ++ lastUniqueId;
	}
}
