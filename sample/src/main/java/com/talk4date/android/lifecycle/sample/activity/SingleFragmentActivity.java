package com.talk4date.android.lifecycle.sample.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.talk4date.android.lifecycle.sample.R;

/**
 * Activity that just hosts one fragment.
 */
public class SingleFragmentActivity extends FragmentActivity {

	public static final String EXTRA_FRAGMENT_CLASS = "EXTRA_FRAGMENT_CLASS";

	private static final String TAG_FRAGMENT = "FRAGMENT";
	private static final String STATE_FRAGMENT_CLASS = "FRAGMENT_CLASS";
	private static final String STATE_FRAGMENT_STATE = "FRAGMENT_STATE";

	private static enum FragmentState {
		ADDED,
		DETACHED,
		REMOVED
	}

	private FragmentState fragmentState;
	private String fragmentClassName;
	private Fragment fragment;

	private ViewGroup buttonContainer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_single_fragment);
		this.buttonContainer = (ViewGroup) findViewById(R.id.buttons);

		if (savedInstanceState == null) {
			this.fragmentClassName = getIntent().getExtras().getString(EXTRA_FRAGMENT_CLASS);
			addFragment();
		} else {
			this.fragmentState = FragmentState.values()[savedInstanceState.getInt(STATE_FRAGMENT_STATE)];
			this.fragmentClassName = savedInstanceState.getString(STATE_FRAGMENT_CLASS);
			this.fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
			bindView();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_FRAGMENT_STATE, this.fragmentState.ordinal());
		outState.putString(STATE_FRAGMENT_CLASS, this.fragmentClassName);
	}

	private void bindView() {
		this.buttonContainer.removeAllViews();

		switch (fragmentState) {
			case ADDED:
				addButton("Remove", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						removeFragment();
					}
				});
				addButton("Detach", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						detachFragment();
					}
				});
				break;
			case REMOVED:
				addButton("Add", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						addFragment();
					}
				});
				break;
			case DETACHED:
				addButton("Attach", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						attachFragment();
					}
				});
				break;
		}
	}

	private void addButton(String text, View.OnClickListener listener) {
		Button button = new Button(this);
		button.setText(text);
		button.setOnClickListener(listener);
		button.setLayoutParams(new LinearLayout.LayoutParams(
				ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, 1f));
		this.buttonContainer.addView(button);
	}

	/**
	 * Re-attaches the fragment to the container.
	 */
	private void attachFragment() {
		this.fragmentState = FragmentState.ADDED;
		getSupportFragmentManager().beginTransaction().attach(fragment).commit();
		bindView();
	}

	/**
	 * Detaches the fragment from the container.
	 */
	private void detachFragment() {
		this.fragmentState = FragmentState.DETACHED;
		getSupportFragmentManager().beginTransaction().detach(fragment).commit();
		bindView();
	}

	/**
	 * Removes the fragment from the container.
	 */
	private void removeFragment() {
		this.fragmentState = FragmentState.REMOVED;
		getSupportFragmentManager().beginTransaction().remove(fragment).commit();
		bindView();
	}

	/**
	 * Creates a new fragment instance and adds it.
	 */
	@SuppressWarnings("unchecked")
	private void addFragment() {
		this.fragmentState = FragmentState.ADDED;

		try {
			Class<? extends Fragment> clazz = (Class<? extends Fragment>) Class.forName(fragmentClassName);
			this.fragment = clazz.newInstance();
		} catch (ClassNotFoundException|InstantiationException|IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, TAG_FRAGMENT).commit();
		bindView();
	}
}
