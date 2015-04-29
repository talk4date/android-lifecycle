package com.talk4date.android.lifecycle.sample;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talk4date.android.lifecycle.sample.activity.CachingCalculatorActivity;
import com.talk4date.android.lifecycle.sample.activity.SingleFragmentActivity;
import com.talk4date.android.lifecycle.sample.activity.SingleRandomNumberActivity;
import com.talk4date.android.lifecycle.sample.activity.TimingActivity;
import com.talk4date.android.lifecycle.sample.activity.TimingSessionLifecycleActivity;
import com.talk4date.android.lifecycle.sample.activity.UserBlockingActivity;
import com.talk4date.android.lifecycle.sample.fragment.CachingCalculatorFragment;
import com.talk4date.android.lifecycle.sample.fragment.FragmentAttachment;
import com.talk4date.android.lifecycle.sample.fragment.SingleRandomNumberFragment;

/**
 * Activity that lists all examples.
 */
public class MainActivity extends Activity {

	private static Example[] examples = new Example[] {
		new Example(
			"Activity: Single Random Number",
			"Fetches a random number on start and a new one every time the button is pressed",
			SingleRandomNumberActivity.class
		),

		new Example(
			"Activity:Timing Service Short Lifecycle",
			"Updates a label with the time past since a service was created. " +
					"Uses the short Acitvity Lifecylce.",
			TimingActivity.class
		),

		new Example(
			"Activity: Timing Service Session Lifecycle",
			"Updates a label with the time past since a service was created. " +
					"Uses the session ActivityLifecycle.",
			TimingSessionLifecycleActivity.class
		),

		new Example(
			"Activity: Caching Calculator",
			"After a long running calculation updates a label. " +
					"The result of the calculation is cached in the service.",
			CachingCalculatorActivity.class
		),

		new Example(
			"Activity: User Blocking Sender",
			"Send some data to a fake server and block the user until it is finished.",
			UserBlockingActivity.class
		),

		new Example(
			"Activity: Fragment Exploration",
			"Tests out adding, removing and re-creating fragments.",
			FragmentAttachment.class
		),

		new Example(
			"Fragment: Single Random Number",
			"Fetches a random number on start and a new one every time the button is pressed",
			SingleRandomNumberFragment.class
		),

		new Example(
			"Fragment: Caching Calculator",
			"After a long running calculation updates a label. " +
					"The result of the calculation is cached in the service.",
			CachingCalculatorFragment.class
		)
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.exampleList);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(new ExampleAdapter());
	}

	public static class Example {

		public final String title;
		public final String description;
		public final Class<? extends Activity> activity;
		public final Class<? extends Fragment> fragment;

		@SuppressWarnings("unchecked")
		public Example(String label, String description, Class<?> fragmentOrActivity) {
			this.title = label;
			this.description = description;

			if (Fragment.class.isAssignableFrom(fragmentOrActivity)) {
				this.fragment = (Class<? extends Fragment>) fragmentOrActivity;
				this.activity = null;
			} else {
				this.activity = (Class<? extends Activity>) fragmentOrActivity;
				this.fragment = null;
			}
		}
	}

	private class ExampleAdapter extends RecyclerView.Adapter<ExampleViewHolder> {

		@Override
		public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View layout = LayoutInflater
					.from(parent.getContext())
					.inflate(R.layout.example_list_entry, parent, false);

			return new ExampleViewHolder(layout);
		}

		@Override
		public void onBindViewHolder(ExampleViewHolder viewHolder, int i) {
			viewHolder.bind(examples[i]);
		}

		@Override
		public int getItemCount() {
			return examples.length;
		}
	}

	private static class ExampleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private TextView title;
		private TextView description;

		private Example example;

		public ExampleViewHolder(View view) {
			super(view);
			this.title = (TextView) view.findViewById(R.id.title);
			this.description = (TextView) view.findViewById(R.id.description);
			view.setOnClickListener(this);
		}

		public void bind(Example example) {
			this.example = example;
			title.setText(example.title);
			description.setText(example.description);
		}

		@Override
		public void onClick(View v) {
			Intent launchExampleIntent = null;

			if (example.activity != null) {
				launchExampleIntent = new Intent(v.getContext(), example.activity);
			} else if (example.fragment != null) {
				launchExampleIntent = new Intent(v.getContext(), SingleFragmentActivity.class);
				launchExampleIntent.putExtra(SingleFragmentActivity.EXTRA_FRAGMENT_CLASS, example.fragment.getCanonicalName());
			}

			v.getContext().startActivity(launchExampleIntent);
		}
	}
}
