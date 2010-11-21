package org.samuel.droidcurse;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ArtistsActivity extends ListActivity {
	private String[] listItems;
	
	private NetworkConnection networkConnection;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DroidCurse", "ArtistsActivity - onCreate");
    }
    
    @Override
	protected void onStart() {
		super.onStart();
		Log.d("DroidCurse", "ArtistsActivity - onStart");
		networkConnection = NetworkConnection.getInstance();
        setContentView(R.layout.songs);
        fillData();
    }


	private void fillData() {
    	listItems = networkConnection.getListOfArtists();
    	setListAdapter(new ArrayAdapter(this, R.layout.list_row, listItems));
    }
	
	@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        /*Intent i = new Intent(this, NoteEdit.class);
        i.putExtra(NotesDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);*/
        Log.d("DroidCurse", "Clicked, position: "+position+" id: "+id);
        //networkConnection.setArtist(position);
        networkConnection.setArtist(position);
    }

}
