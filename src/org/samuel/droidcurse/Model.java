package org.samuel.droidcurse;

import java.util.LinkedList;

public class Model {
	private static Model model;
	/* The actual lists of songs, albums and artists transferred to the app */
	private LinkedList<String> listOfSongs;
	private LinkedList<String> listOfAlbums;
	private LinkedList<String> listOfArtists;
	
	/* Monitors that temporarily holds the songs, albums and artists,
	 * they are cleared after each corresponding END command,
	 * see NetworkReaderThread */
	private ResponseMonitor artistMonitor;
	private ResponseMonitor albumMonitor;
	private ResponseMonitor listMonitor;
	
	public Model() {
		listOfArtists = new LinkedList<String>();
		listOfAlbums = new LinkedList<String>();
		listOfSongs = new LinkedList<String>();
		
		artistMonitor = new ResponseMonitor();
		albumMonitor = new ResponseMonitor();
		listMonitor = new ResponseMonitor();
	}
	
	public static Model getInstance() {
		if (model == null) {
			model = new Model();
		}
		return model;
	}

	synchronized public LinkedList<String> getListOfArtists() {
		return listOfArtists;
	}

	synchronized public void setArtistList(LinkedList<String> listOfArtists) {
		this.listOfArtists = listOfArtists;
	}

	synchronized public LinkedList<String> getListOfAlbums() {
		return listOfAlbums;
	}

	synchronized public void setAlbumList(LinkedList<String> listOfAlbums) {
		this.listOfAlbums = listOfAlbums;
	}
	
	synchronized public void setSongList(LinkedList<String> listOfSongs) {
		this.listOfSongs = listOfSongs;
	}
	
	synchronized public LinkedList<String> getListOfSongs() {
		return listOfSongs;
	}

	synchronized ResponseMonitor getArtistMonitor() {
		return artistMonitor;
	}
	
	synchronized ResponseMonitor getAlbumMonitor() {
		return albumMonitor;
	}
	
	synchronized ResponseMonitor getListMonitor() {
		return listMonitor;
	}
}
