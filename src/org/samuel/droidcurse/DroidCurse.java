package org.samuel.droidcurse;

import android.app.Activity;
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
    }
    
    View.OnClickListener connectButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.i("DroidCurse", "Clicked button");
			networkConnection.setHost(networkConnection.DEFAULT_HOST);
			networkConnection.setPort(networkConnection.DEFAULT_PORT);
			networkConnection.connect();
		}
	};
}