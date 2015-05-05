package com.talk4date.android.lifecycle.sample.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.talk4date.android.lifecycle.EventListener;
import com.talk4date.android.lifecycle.EventReceiver;
import com.talk4date.android.lifecycle.FragmentLifecycle;
import com.talk4date.android.lifecycle.sample.R;
import com.talk4date.android.lifecycle.sample.activity.UserBlockingActivity;
import com.talk4date.android.lifecycle.sample.service.SendDataService;
import com.talk4date.android.lifecycle.sample.utils.MessageDialogFragment;
import com.talk4date.android.lifecycle.ui.BaseLifecycleDispatchingFragment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fragment Version of the {@link com.talk4date.android.lifecycle.sample.activity.UserBlockingActivity}.
 */
public class UserBlockingFragment extends BaseLifecycleDispatchingFragment {

	private static final String TAG_DIALOG = "DIALOG";
	private static final Logger log = LoggerFactory.getLogger(UserBlockingFragment.class);

	private MessageDialogFragment dialog;
	private EventReceiver<Void> sendDataReceiver;
	private FragmentLifecycle sessionLifecycle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sessionLifecycle = FragmentLifecycle.sessionLifecycle(this);

		dialog = (MessageDialogFragment) getFragmentManager().findFragmentByTag(TAG_DIALOG);

		sendDataReceiver = sessionLifecycle.registerListener("sendData", true, new EventListener<Void>() {
			@Override
			public void onEvent(Void event) {
				dialog.dismiss();
			}
		});
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_user_blocking, container, false);
		Button button = (Button) view.findViewById(R.id.button);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog = new MessageDialogFragment().withMessage("Sending data... please wait.");
				dialog.setCancelable(false);
				dialog.show(getFragmentManager(), TAG_DIALOG);

				SendDataService.getInstance().sendDataToServer("Hello World", sendDataReceiver);
			}
		});

		if (sessionLifecycle.isRestored() && dialog != null) {
			log.trace("lifecylce restored, dismissing dialog");
			dialog.dismiss();
		}

		return view;
	}
}
