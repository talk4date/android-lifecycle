package com.talk4date.android.lifecycle.sample.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.talk4date.android.lifecycle.Lifecycle;
import com.talk4date.android.lifecycle.sample.R;
import com.talk4date.android.lifecycle.util.LifecycleLoggingFragment;

/**
 * Fragment attach / reattach demo.
 */
public class FragmentAttachment extends Activity {

	private Fragment nonRetainedFragment;

	private Button nonRetainedNew;
	private Button nonRetainedAdd;
	private Button nonRetainedRemove;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_attachment);

		nonRetainedNew = (Button) findViewById(R.id.nonRetainedNew);
		nonRetainedAdd = (Button) findViewById(R.id.nonRetainedAdd);
		nonRetainedRemove = (Button) findViewById(R.id.nonRetainedRemove);

		nonRetainedNew.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				nonRetainedFragment = new SimpleFragment();
			}
		});

		nonRetainedAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction().add(R.id.mainLayout, nonRetainedFragment, "NonRetained").commit();
			}
		});

		nonRetainedRemove.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getFragmentManager().beginTransaction().remove(nonRetainedFragment).commit();
			}
		});
	}

	public static class SimpleFragment extends LifecycleLoggingFragment {

		@Nullable
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			TextView textView = new TextView(getActivity());
			textView.setText("I'm the fragment");
			return textView;
		}
	}
}
