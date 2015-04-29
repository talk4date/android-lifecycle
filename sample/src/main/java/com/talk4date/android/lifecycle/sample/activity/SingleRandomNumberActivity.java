package com.talk4date.android.lifecycle.sample.activity;

import android.app.Activity;

import com.talk4date.android.lifecycle.sample.service.RandomNumberService;
import com.talk4date.android.lifecycle.ActivityLifecycle;
import com.talk4date.android.lifecycle.EventListener;
import com.talk4date.android.lifecycle.EventReceiver;

import com.talk4date.android.lifecycle.sample.R;
import com.talk4date.android.lifecycle.sample.utils.MessageDialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example that gets and displays random numbers from a random number service.
 *
 * The first random number is immediately fetched when the activity starts.
 * When the Activity is rotated no initial new random number will be fetched.
 *
 * More random numbers can be fetched with click on the button.
 * For every click one new random number will be fetched.
 * This also works correctly across rotation changes.
 *
 * We display a message dialog fragment with the random number in addition to update the TextView.
 * This also works in background which wouldn't without ActivityLifecycle
 * because it's forbidden to create fragment transactions after onSaveInstanceState.
 *
 * Since all listeners registered at an ActivityLifecycle are guaranteed to execute only when
 * the activity is resumed this works flawlessly in this example.
 */
public class SingleRandomNumberActivity extends Activity {

	private static final Logger log = LoggerFactory.getLogger(SingleRandomNumberActivity.class);

	private static final String INSTANCE_STATE_LAST_RESULT = "lastResult";

	/**
	 * Total number of activities created (only for debug output).
	 */
	private static int lastActivityId = 0;

	/**
	 * Assign our activity instance an id (only for debug output)
	 */
	private int id = ++lastActivityId;

	/**
	 * The random number service from which we fetch our data.
	 */
	private RandomNumberService randomNumberService = RandomNumberService.getInstance();

	/**
	 * The text view on the screen on which we display the number.
	 */
	private TextView textView;

	/**
	 * Button to create new numbers.
	 */
	private Button button;

	/**
	 * The last result we received, which will be displayed in the UI.
	 */
	private Integer lastResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log.trace("onCreate activity {}", id);

		// Get the activity session lifecycle for this activity.
		ActivityLifecycle lifecycle = ActivityLifecycle.sessionLifecycle(this);

		setContentView(R.layout.activity_single_random_number);

		this.textView = (TextView) findViewById(R.id.text);
		this.button = (Button) findViewById(R.id.button);

		// Register a listener with the lifecycle and get back the EventReceiver for it.
		// All events will only be executed when the activity is resumed.
		final EventReceiver<Integer> randomNumberListener = lifecycle
			.registerListener("randomNumber", true, new EventListener<Integer>() {
				@Override
				public void onEvent(Integer number) {
					// Since the ActivityLifecycle manages the listener
					// we can do all view related tasks without taking care.
					log.debug("listener of activity {} executing", id);

					lastResult = number;
					bindView();

					// We can even create a fragment transaction without further handling.
					// The lifecycle makes sure that this will only be executed while resumed.
					new MessageDialogFragment().withMessage(String.valueOf(number)).show(getFragmentManager(), "randomDialog");
				}
			});

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				randomNumberService.getOneRandomNumber(5, randomNumberListener);
			}
		});

		if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_STATE_LAST_RESULT)) {
			lastResult = savedInstanceState.getInt(INSTANCE_STATE_LAST_RESULT);
		}

		// We only kick off to get the first random number when the lifecycle
		// is new or restored  and we don't have a result yet.
		if (lifecycle.isNewOrRestored() && lastResult == null) {
			randomNumberService.getOneRandomNumber(5, randomNumberListener);
		}
		bindView();
	}

	/**
	 * Binds the view with the lastResult.
	 */
	private void bindView() {
		if (lastResult == null) {
			return;
		}
		// We can directly access the textView.
		// The lifecycle makes sure that this is only executed on the active instance.
		textView.setText("Random: " + lastResult);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (lastResult != null) {
			outState.putInt(INSTANCE_STATE_LAST_RESULT, lastResult);
		}
	}
}
