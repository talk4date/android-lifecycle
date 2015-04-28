package com.talk4date.android.lifecycle.sample.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.PersistableBundle;

/**
 * Activity that just hosts one fragment.
 */
public class FragmentHostingActivity extends Activity {

	public static final String EXTRA_FRAGMENT_CLASS = "EXTRA_FRAGMENT_CLASS";

	@Override
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			try {
				String className = getIntent().getExtras().getString(EXTRA_FRAGMENT_CLASS);
				Class<? extends Fragment> clazz = (Class<? extends Fragment>) Class.forName(className);
				Fragment fragment = clazz.newInstance();

				getFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
			} catch (ClassNotFoundException|InstantiationException|IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
