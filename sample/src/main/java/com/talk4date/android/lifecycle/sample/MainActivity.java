package com.talk4date.android.lifecycle.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talk4date.android.lifecycle.sample.R;
import com.talk4date.android.lifecycle.sample.activity.CachingCalculatorActivity;
import com.talk4date.android.lifecycle.sample.activity.SingleRandomNumberActivity;
import com.talk4date.android.lifecycle.sample.activity.TimingActivity;
import com.talk4date.android.lifecycle.sample.activity.TimingSessionLifecycleActivity;
import com.talk4date.android.lifecycle.sample.activity.UserBlockingActivity;
import com.talk4date.android.lifecycle.sample.fragment.FragmentAttachment;
import com.talk4date.android.lifecycle.sample.service.CachingCalculatorService;

/**
 * Activity that lists all examples.
 */
public class MainActivity extends Activity {

	private static Example[] examples = new Example[] {
		new Example(
			"Single Random Number",
			"Fetches a random number on start and a new one every time the button is pressed",
			SingleRandomNumberActivity.class
		),

		new Example(
				"Timing Service Short Lifecycle",
				"Updates a label with the time past since a service was created. " +
						"Uses the short Acitvity Lifecylce.",
				TimingActivity.class
		),

		new Example(
			"Timing Service Session Lifecycle",
			"Updates a label with the time past since a service was created. " +
					"Uses the session ActivityLifecycle.",
			TimingSessionLifecycleActivity.class
		),

		new Example(
			"Caching Calculator",
			"After a long running calculation updates a label. " +
					"The result of the calculation is cached in the service.",
			CachingCalculatorActivity.class
		),

		new Example(
			"User Blocking Sender",
			"Send some data to a fake server and block the user until it is finished.",
			UserBlockingActivity.class
		),

		new Example(
			"Fragment Exploration",
			"Tests out adding, removing and re-creating fragments.",
			FragmentAttachment.class
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

		public Example(String label, String description, Class<? extends Activity> activity) {
			this.title = label;
			this.activity = activity;
			this.description = description;
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
			Intent launchActivity = new Intent(v.getContext(), example.activity);
			v.getContext().startActivity(launchActivity);
		}
	}
}
