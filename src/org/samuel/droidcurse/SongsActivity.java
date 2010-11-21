package org.samuel.droidcurse;


import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class SongsActivity extends ListActivity {
	String[] listItems = {"exploring", "android", 
            "list", "activities"};
	private NetworkConnection networkConnection;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        networkConnection = NetworkConnection.getInstance();
        setContentView(R.layout.songs);
        fillData();
    }
    
    private void fillData() {
    	listItems = networkConnection.getListOfSongs();
    	setListAdapter(new ArrayAdapter(this, R.layout.list_row, listItems));
    }

}
