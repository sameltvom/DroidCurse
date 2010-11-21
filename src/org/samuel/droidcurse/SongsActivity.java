package org.samuel.droidcurse;


import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class SongsActivity extends ListActivity {
	String[] listItems = {"exploring", "android", 
            "list", "activities"};

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.songs);
        fillData();
    }
    
    private void fillData() {
    	setListAdapter(new ArrayAdapter(this, R.layout.list_row, listItems));
    }

}
