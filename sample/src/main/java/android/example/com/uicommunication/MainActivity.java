package android.example.com.uicommunication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import com.talk4date.android.lifecycle.ActivityLifecycle;
import com.talk4date.android.lifecycle.EventListener;
import com.talk4date.android.lifecycle.EventReceiver;
import android.example.com.uicommunication.service.RandomNumberService;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends Activity {

	private static final Logger log = LoggerFactory.getLogger(MainActivity.class);

	private static int count = 0;

	private int mycount = count++;

	TextView textView;
	Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log.trace("onCreate {}", mycount);

		setContentView(R.layout.activity_main);

		textView = (TextView) findViewById(R.id.text);
		button = (Button) findViewById(R.id.button);

		final EventReceiver<Integer> randomNumberListener = ActivityLifecycle
				.activitySessionLifecylce(this)
				.registerListener("randomNumber", true, new EventListener<Integer>() {
					@Override
					public void onEvent(Integer number) {
						log.debug("listener of activity {}", mycount);
						textView.setText("Random: " + number);
						new MessageDialogFragment().withMessage("" + number).show(getFragmentManager(), "random_tag");
					}
				});

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RandomNumberService.instance.getRandomNumber(5, randomNumberListener);
			}
		});

		if (savedInstanceState == null) {
			RandomNumberService.instance.getRandomNumber(5, randomNumberListener);
		}
	}

	// simply start a new random number

	// modal dialog

	public static class MessageDialogFragment extends DialogFragment {

		private static final String ARG_MESSAGE = "message";

		public MessageDialogFragment withMessage(String message) {
			Bundle args = getArguments();
			if (args == null) {
				args = new Bundle();
				setArguments(args);
			}

			args.putString(ARG_MESSAGE, message);
			return this;
		}

		@Override
		public android.app.Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity()).setTitle("Random").setMessage(getArguments().getString(ARG_MESSAGE)).create();
		}
	}
}
