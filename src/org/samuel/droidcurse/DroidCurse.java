package org.samuel.droidcurse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
	String[] hostList;
	private String selectedHost;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        networkConnection = NetworkConnection.getInstance();
        
        ((Button)findViewById(R.id.connect_button)).setOnClickListener(connectButtonListener);
        
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
			Log.d("DroidCurse", "Spinner item chosen: "+arg2);
			selectedHost = hostList[arg2]; 
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// do nothing
		}
	};
    
    View.OnClickListener connectButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.i("DroidCurse", "Clicked connect button");
			
			if (selectedHost != null) {
				networkConnection.setHost(selectedHost);
			} else {
				// the first should be "chosen" by default
				networkConnection.setHost(hostList[0]);
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
}