package org.samuel.droidcurse;

import java.util.ArrayList;
import java.util.LinkedList;


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

public class MusicBrowser extends TabActivity {
	private Model model;
	//private NetworkConnection networkConnection;
	private ListView listViewArtists;
	private ListView listViewAlbums;
	private ListView listViewSongs;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs_main);

        model = Model.getInstance();
        //networkConnection = NetworkConnection.getInstance();
        
        TabHost mTabHost;
        mTabHost = getTabHost();
   
        //tabHost.newTabSpec("albums").setIndicator("Albums",
        //        res.getDrawable(R.drawable.ic_tab_albums))
        //    .setContent(intent);
        
        mTabHost.addTab(mTabHost.newTabSpec("tab_artists").setIndicator("Artists").setContent(R.id.artists_layout));
        mTabHost.addTab(mTabHost.newTabSpec("tab_albums").setIndicator("Albums").setContent(R.id.albums_layout));
        mTabHost.addTab(mTabHost.newTabSpec("tab_songs").setIndicator("Songs").setContent(R.id.songs_layout));
        
        mTabHost.setOnTabChangedListener(tabChangedListener);
        
        LinearLayout artistsLayout = (LinearLayout)findViewById(R.id.artists_layout); 	
		listViewArtists =  new ListView(this);
		listViewArtists.setOnItemClickListener(artistsItemClickListener);
        artistsLayout.addView(listViewArtists);
        
        LinearLayout albumsLayout = (LinearLayout)findViewById(R.id.albums_layout); 	
		listViewAlbums =  new ListView(this);
		listViewAlbums.setOnItemClickListener(albumsItemClickListener);
        albumsLayout.addView(listViewAlbums);
        
        LinearLayout songsLayout = (LinearLayout)findViewById(R.id.songs_layout); 	
		listViewSongs =  new ListView(this);
		listViewSongs.setOnItemClickListener(songsItemClickListener);
        songsLayout.addView(listViewSongs);
      
        
        // switch to artists tab
        mTabHost.setCurrentTab(0);
    
        // first fill tab so it's not empty
        fillArtistsTab();   
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		NetworkConnection networkConnection = NetworkConnection.getInstance();
		
		networkConnection.disconnect();
    }
    
    OnItemClickListener artistsItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			OurLog.i("DroidCurse", "Artists item clicked, arg2: "+arg2+" arg3: "+arg3);
			if (arg2 == 0) {
				model.changeAllArtists();
			} else{
				model.changeArtist(arg2-1);
			}
			
			// update the album list as well
			model.changeAllAlbums();
		}
	};
	
	OnItemClickListener albumsItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			OurLog.i("DroidCurse", "Albums item clicked, arg2: "+arg2+" arg3: "+arg3);
			if (arg2 == 0) {
				model.changeAllAlbums();
			} else{
				model.changeAlbum(arg2-1);
			}
		}
	};


	OnItemClickListener songsItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			OurLog.i("DroidCurse", "Songs item clicked, arg2: "+arg2+" arg3: "+arg3);
			model.changeSong(arg2);
		}
	};

	
    TabHost.OnTabChangeListener tabChangedListener = new TabHost.OnTabChangeListener() {	
		@Override
		public void onTabChanged(String tabId) {
			OurLog.i("DroidCurse", "MusicBrowser: Changing tab: "+tabId);
		
			if (tabId.equals("tab_songs")) {
				fillSongsTab();
			} else if (tabId.equals("tab_albums")) {
				fillAlbumsTab();
			} else if (tabId.equals("tab_artists")) {
				fillArtistsTab();
			}
		}
	};
	
	private void fillSongsTab() {
		LinkedList<String> listItems;
		OurLog.d("DroidCurse", "MusicBrowser: model.getListOfSongs()");
        listItems = model.getListOfSongs();
		OurLog.d("DroidCurse", "MusicBrowser: model.getListOfSongs() - finished");
		listViewSongs.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems));
	}
	
	private void fillAlbumsTab() {
		LinkedList<String> listItems;
		OurLog.d("DroidCurse", "MusicBrowser: model.getListOfAlbums()");
        listItems = model.getListOfAlbums();
		OurLog.d("DroidCurse", "MusicBrowser: model.getListOfSongs() - finished");
		listViewAlbums.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems));
	}
	
	private void fillArtistsTab() {
		LinkedList<String> listItems;	
		OurLog.d("DroidCurse", "MusicBrowser: model.getListOfArtists()");
        listItems = model.getListOfArtists();
        OurLog.d("DroidCurse", "MusicBrowser: model.getListOfArtists() - finished");
        listViewArtists.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems));
	}
    
}