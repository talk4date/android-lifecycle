package com.talk4date.android.lifecycle.sample.service;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.talk4date.android.lifecycle.EventReceiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Service that does math in the background and caches the results internally.
 */
public class CachingCalculatorService {

	private static final Logger log = LoggerFactory.getLogger(RandomNumberService.class);

	private static CachingCalculatorService instance = new CachingCalculatorService();

	/**
	 * Get the singleton instance of this service.
	 */
	public static CachingCalculatorService getInstance() {
		return instance;
	}

	/**
	 * Handler on the main loop.
	 */
	private Handler handler = new Handler(Looper.getMainLooper());

	/**
	 * Nested map with cached addition results (a -> b -> result)
	 */
	private Map<Integer, Map<Integer, Integer>> addCache = new HashMap<>();

	/**
	 * Caches an add result.
	 *
	 * @param a The first number.
	 * @param b The second number.
	 * @param result The result.
	 */
	private void cacheAddResult(int a, int b, int result) {
		Map<Integer, Integer> bCache = addCache.get(a);

		if (bCache == null) {
			bCache = new HashMap<>();
		}

		bCache.put(b, result);
		addCache.put(a, bCache);
	}

	/**
	 * Gets a cached result form an addition.
	 * @param a The first number.
	 * @param b the second number.
	 * @return The cached result or null if the result isn't cached yet.
	 */
	private @Nullable Integer getCachedAddResult(int a, int b) {
		Map<Integer, Integer> bCache = addCache.get(a);
		if (bCache == null) {
			return null;
		} else {
			return bCache.get(b);
		}
	}

	/**
	 * Adds number the supplied numbers and returns the result to the receiver when finished.
	 * Must be called on the main thread.
	 * It will take 5 minutes until the result is returned.
	 *
	 * @param a The first number.
	 * @param b The second number.
	 * @param resultReceiver The receiver which gets the result when the calculation is finished.
	 */
	public void add(final int a, final int b, final EventReceiver<Integer> resultReceiver) {
		Integer cachedResult = getCachedAddResult(a, b);
		if (cachedResult != null) {
			resultReceiver.postEvent(cachedResult);
		} else {
			handler.postDelayed(new Runnable() {
				@Override
				public void run() {
					int result = a + b;
					cacheAddResult(a, b, result);
					resultReceiver.postEvent(result);
				}
			}, 5000);
		}
	}
}