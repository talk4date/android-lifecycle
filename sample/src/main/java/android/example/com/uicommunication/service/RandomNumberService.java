package android.example.com.uicommunication.service;

import com.talk4date.android.lifecycle.EventReceiver;
import android.os.Handler;
import android.os.Looper;

import java.util.Random;

/**
 * Service to get random numbers in an aysync way.
 */
public class RandomNumberService {

	public static RandomNumberService instance = new RandomNumberService();

	private Handler handler = new Handler(Looper.getMainLooper());

	private RandomNumberService() {}

	private int lastId = 0;

	/**
	 * Get a new random number after the given delay in seconds.
	 * @param delayInSeconds The delay after which to get the random number
	 * @param listener The listener to invoke with the new random number
	 */
	public void getRandomNumber(int delayInSeconds, final EventReceiver<Integer> listener) {
		final int id = ++lastId;
		System.out.println("starting random number with id " + id);

		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				System.out.println("finished random number with id " + id);
				listener.postEvent(new Random().nextInt());
			}
		}, delayInSeconds * 1000);
	}
}
