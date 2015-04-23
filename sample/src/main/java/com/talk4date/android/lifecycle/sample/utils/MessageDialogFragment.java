package com.talk4date.android.lifecycle.sample.utils;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.talk4date.android.lifecycle.sample.activity.SingleRandomNumberActivity;

/**
 * Simple dialog fragment that displays a message.
 */
public class MessageDialogFragment extends DialogFragment {

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
		return new AlertDialog.Builder(getActivity())
				.setMessage(getArguments().getString(ARG_MESSAGE)).create();
	}
}
