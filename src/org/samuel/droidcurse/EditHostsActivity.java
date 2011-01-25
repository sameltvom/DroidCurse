package org.samuel.droidcurse;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditHostsActivity extends Activity {
	private NetworkConnection networkConnection;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_hosts);
	
		networkConnection = NetworkConnection.getInstance();
		
		((Button)findViewById(R.id.add_host_button)).setOnClickListener(buttonListener);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private View.OnClickListener buttonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			EditText hostEditText = (EditText)findViewById(R.id.host_edittext);
			networkConnection.getListOfHosts().add(hostEditText.getText().toString());
		}
	};
	
}
