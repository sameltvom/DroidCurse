package org.samuel.droidcurse;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class DroidCurse extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ((Button)findViewById(R.id.connect_button)).setOnClickListener(connectButtonListener);
    }
    
    View.OnClickListener connectButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Log.i("DroidCurse", "Clicked button");
		}
	};
}