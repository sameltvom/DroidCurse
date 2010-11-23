package org.samuel.droidcurse;

import java.util.ArrayList;


import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.AdapterView.OnItemClickListener;

public class TabsTutorial extends TabActivity {
	private NetworkConnection networkConnection;
	private ListView listViewArtists;
    private ArrayList<String> songsArrayList;
	private TabActivity thisOne;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs_main);

        networkConnection = NetworkConnection.getInstance();
        
        TabHost mTabHost;
        mTabHost = getTabHost();
   
        //tabHost.newTabSpec("albums").setIndicator("Albums",
        //        res.getDrawable(R.drawable.ic_tab_albums))
        //    .setContent(intent);
        
        mTabHost.addTab(mTabHost.newTabSpec("tab_artists").setIndicator("Artists").setContent(R.id.artists_layout));
        mTabHost.addTab(mTabHost.newTabSpec("tab_albums").setIndicator("Albums").setContent(R.id.albums_layout));
        mTabHost.addTab(mTabHost.newTabSpec("tab_songs").setIndicator("Songs").setContent(R.id.songs_layout));
        
        mTabHost.setOnTabChangedListener(tabChangedListener);
        
        mTabHost.setCurrentTab(0);
        
        
        songsArrayList = new ArrayList<String>();
        songsArrayList.add("Hejsan");
        songsArrayList.add("Hello");
        songsArrayList.add("Thohej");
        songsArrayList.add("Annars?");
        
        
        LinearLayout artistsLayout = (LinearLayout)findViewById(R.id.artists_layout); 
        	
		listViewArtists =  new ListView(this);
		listViewArtists.setOnItemClickListener(artistsItemClickListener);
		
		/*listItems = new String[songsArrayList.size()];
        songsArrayList.toArray(listItems);
        listViewSongs.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems));
        */
		
        artistsLayout.addView(listViewArtists);
        
        thisOne = this;
    }
    
    OnItemClickListener artistsItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Log.i("TabsTutorial", "Artists item clicked, arg2: "+arg2+" arg3: "+arg3);
		}
	};

   
    TabHost.OnTabChangeListener tabChangedListener = new TabHost.OnTabChangeListener() {
		
		@Override
		public void onTabChanged(String tabId) {
			Log.i("TabsTutorial", "Changing tab: "+tabId);
		
			if (tabId.equals("tab_songs")) {
				String []listItems;				
		        listItems = networkConnection.getListOfArtists();
		        listViewArtists.setAdapter(new ArrayAdapter<String>(thisOne, android.R.layout.simple_list_item_1, listItems));
			}
		}
	};
    
}