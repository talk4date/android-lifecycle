package com.talk4date.android.lifecycle.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.talk4date.android.lifecycle.EventListener;
import com.talk4date.android.lifecycle.EventReceiver;
import com.talk4date.android.lifecycle.sample.R;
import com.talk4date.android.lifecycle.sample.service.RandomNumberService;
import com.talk4date.android.lifecycle.sample.utils.MessageDialogFragment;
import com.talk4date.android.lifecycle.ui.BaseLifecycleDispatchingFragment;
import com.talk4date.android.lifecycle.FragmentLifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Same demo like in {@link com.talk4date.android.lifecycle.sample.activity.SingleRandomNumberActivity} but in a fragment.
 */
public class SingleRandomNumberFragment extends BaseLifecycleDispatchingFragment {

	private static final Logger log = LoggerFactory.getLogger(SingleRandomNumberFragment.class);

	private static final String INSTANCE_STATE_LAST_RESULT = "lastResult";

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

	private EventReceiver<Integer> randomNumberReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_STATE_LAST_RESULT)) {
			lastResult = savedInstanceState.getInt(INSTANCE_STATE_LAST_RESULT);
		}

		FragmentLifecycle lifecycle = FragmentLifecycle.fragmentSessionLifecycle(this);

		randomNumberReceiver = lifecycle.registerListener("randomNumber", true, new EventListener<Integer>() {
			@Override
			public void onEvent(Integer number) {
				lastResult = number;
				bindView();
				new MessageDialogFragment().withMessage(String.valueOf(number)).show(getFragmentManager(), "randomDialog");
			}
		});

		if (lifecycle.isNewOrRestored() && lastResult == null) {
			RandomNumberService.getInstance().getOneRandomNumber(5, randomNumberReceiver);
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_single_random_number, container, false);
		this.textView = (TextView) view.findViewById(R.id.text);
		this.button = (Button) view.findViewById(R.id.button);

		this.button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				randomNumberService.getOneRandomNumber(5, randomNumberReceiver);
			}
		});

		bindView();
		return view;
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
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (lastResult != null) {
			outState.putInt(INSTANCE_STATE_LAST_RESULT, lastResult);
		}
	}
}
