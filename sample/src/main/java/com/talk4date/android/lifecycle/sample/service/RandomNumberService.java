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
	 * Creates a new unique id.
	 * @return a new unique id.
	 */
	private int newUniqueId() {
		return ++ lastUniqueId;
	}
}
