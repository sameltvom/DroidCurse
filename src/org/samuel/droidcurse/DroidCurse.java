package org.samuel.droidcurse;


import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SlidingDrawer;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class DroidCurse extends Activity {
	private NetworkConnection networkConnection;
	LinkedList<String> hostList;
	private String selectedHost;

	private static final int SETTINGS_ID = Menu.FIRST;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		networkConnection = NetworkConnection.getInstance();

		((Button)findViewById(R.id.connect_button)).setOnClickListener(connectButtonListener);   
	}

	@Override
	protected void onStart() {
		super.onStart();

		// in onStart so it will be run after being restarted after EditHostsActivity
		hostList = networkConnection.getListOfHosts();

		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hostList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(spinnerListener);

	}

	OnItemSelectedListener spinnerListener = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			OurLog.d("DroidCurse", "Spinner item chosen: "+arg2);
			selectedHost = hostList.get(arg2); 
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// do nothing
		}
	};

	View.OnClickListener connectButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			OurLog.i("DroidCurse", "Clicked connect button");

			if (selectedHost != null) {
				networkConnection.setHost(selectedHost);
			} else {
				// the first should be "chosen" by default
				networkConnection.setHost(hostList.getFirst());
			}
			//networkConnection.setHost(NetworkConnection.DEFAULT_HOST);
			networkConnection.setPort(NetworkConnection.DEFAULT_PORT);
			boolean connectOk = networkConnection.connect();

			if (connectOk) {
				Intent i = new Intent(DroidCurse.this, MusicBrowser.class);
				startActivity(i);
			} else {
				Toast.makeText(getApplicationContext(), "Couldn't connect", Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, SETTINGS_ID, 0, R.string.menu_hosts);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		OurLog.i("DroidCurse", "Menu selected");

		switch (item.getItemId()) {
		case SETTINGS_ID:
			OurLog.i("DroidCurse", "Menu selected - settings id");
			Intent i = new Intent(this, EditHostsActivity.class);
			startActivity(i);
			/* Give the speed as an argument */
			//i.putExtra("speed", Morse.UNIT_TIME);
			//startActivityForResult(i, ACTIVITY_CREATE);
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}


}