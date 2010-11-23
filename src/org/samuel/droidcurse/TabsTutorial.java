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
	private ListView listViewSongs;
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
        //mTabHost.addTab(mTabHost.newTabSpec("tab_albums").setIndicator("Albums").setContent(R.id.albums_layout));
        mTabHost.addTab(mTabHost.newTabSpec("tab_songs").setIndicator("Songs").setContent(R.id.songs_layout));
        
        mTabHost.setOnTabChangedListener(tabChangedListener);
        
        mTabHost.setCurrentTab(0);
        
        
        
        LinearLayout artistsLayout = (LinearLayout)findViewById(R.id.artists_layout); 	
		listViewArtists =  new ListView(this);
		listViewArtists.setOnItemClickListener(artistsItemClickListener);
        artistsLayout.addView(listViewArtists);
        
        LinearLayout songsLayout = (LinearLayout)findViewById(R.id.songs_layout); 	
		listViewSongs =  new ListView(this);
		listViewSongs.setOnItemClickListener(songsItemClickListener);
        songsLayout.addView(listViewSongs);
        
        
        thisOne = this;
    }
    
    OnItemClickListener artistsItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Log.i("TabsTutorial", "Artists item clicked, arg2: "+arg2+" arg3: "+arg3);
		}
	};

	OnItemClickListener songsItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Log.i("TabsTutorial", "Songs item clicked, arg2: "+arg2+" arg3: "+arg3);
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
			} else if (tabId.equals("tab_artists")) {
				String []listItems;				
		        listItems = networkConnection.getListOfSongs();
		        listViewSongs.setAdapter(new ArrayAdapter<String>(thisOne, android.R.layout.simple_list_item_1, listItems));	
			}
		}
	};
    
}