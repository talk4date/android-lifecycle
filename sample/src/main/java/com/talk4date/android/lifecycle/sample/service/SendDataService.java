package com.talk4date.android.lifecycle.sample.service;

import android.os.Handler;
import android.os.Looper;

import com.talk4date.android.lifecycle.EventReceiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A service which simulates sending of data to a server.
 */
public class SendDataService {

	private static final Logger log = LoggerFactory.getLogger(SendDataService.class);

	private static SendDataService instance = new SendDataService();

	/**
	 * Handler on the main loop.
	 */
	private Handler handler = new Handler(Looper.getMainLooper());

	private SendDataService() {
	}

	public static SendDataService getInstance() {
		return instance;
	}

	/**
	 * Simulates sending some data to the server.
	 * Triggers the event receiver once the response from the server came back.
	 *
	 * @param data Some data to send to the server. Just used for debug logging in this example.
	 * @param receiver The receiver to trigger with the response.
	 */
	public void sendDataToServer(String data, final EventReceiver<Void> receiver) {
		// Simulate network latency.
		log.debug("Sending data to server {}", data);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				receiver.postEvent(null);
			}
		}, 5 * 1000);
	}
}
