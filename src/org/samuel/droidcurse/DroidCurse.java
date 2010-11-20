package org.samuel.droidcurse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DroidCurse extends Activity {
	private NetworkConnection networkConnection;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        networkConnection = new NetworkConnection(this);
        
        ((Button)findViewById(R.id.connect_button)).setOnClickListener(connectButtonListener);
        ((Button)findViewById(R.id.disconnect_button)).setOnClickListener(disconnectButtonListener);
    }
    
    View.OnClickListener connectButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.i("DroidCurse", "Clicked connect button");
			networkConnection.setHost(networkConnection.DEFAULT_HOST);
			networkConnection.setPort(networkConnection.DEFAULT_PORT);
			boolean connectOk = networkConnection.connect();
			
			if (connectOk) {
				Intent i = new Intent(DroidCurse.this, MusicBrowser.class);
				/* Give the speed as an argument */
				//i.putExtra("speed", Morse.UNIT_TIME);
		        startActivity(i);
			}
		}
	};
	
	View.OnClickListener disconnectButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.i("DroidCurse", "Clicked disconnect button");
			networkConnection.disconnect();
		}
	};
}