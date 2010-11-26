package org.samuel.droidcurse;

import java.util.LinkedList;

public class Model {
	private static Model model;
	private LinkedList<String> listOfSongs;
	private LinkedList<String> listOfAlbums;
	private LinkedList<String> listOfArtists;
	
	public Model() {
		listOfArtists = new LinkedList<String>();
		listOfAlbums = new LinkedList<String>();
		listOfSongs = new LinkedList<String>();
		
		/*listOfArtists.add("0 - Bob Dylan");
		listOfArtists.add("1 - Swen Andersson");
		listOfArtists.add("2 - Gullbritt");
		
		listOfSongs.add("0 - Bob Dylan - Heavy rain");
		listOfSongs.add("1 - Bob Dylan - Idiot wind");
		listOfSongs.add("2 - Bob Dylan - I want you");
		listOfSongs.add("3 - Swen Andersson - Rosor");
		listOfSongs.add("4 - Swen Andersson - Damer");
		listOfSongs.add("5 - Gullbritt - Gamla guvvar");*/
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
	

	synchronized public void changeArtist(int artistId) {
		// TODO: Add a message to a mailbox that later is being consumed by another thread
		NetworkConnection networkConnection = NetworkConnection.getInstance();
		networkConnection.setArtist(artistId);
	}

	synchronized public void changeAllArtists() {
		// TODO: Add a message to a mailbox that later is being consumed by another thread
		NetworkConnection networkConnection = NetworkConnection.getInstance();
		networkConnection.setAllArtists();
	}
	
	synchronized public void changeAllAlbums() {
		// TODO: Add a message to a mailbox that later is being consumed by another thread
		NetworkConnection networkConnection = NetworkConnection.getInstance();
		networkConnection.setAllAlbums();
	}

	synchronized public void changeAlbum(int albumId) {
		// TODO: Add a message to a mailbox that later is being consumed by another thread
		NetworkConnection networkConnection = NetworkConnection.getInstance();
		networkConnection.setAlbum(albumId);
	}
	
	synchronized public void changeSong(int songId) {
		// TODO: Add a message to a mailbox that later is being consumed by another thread
		NetworkConnection networkConnection = NetworkConnection.getInstance();
		networkConnection.playSong(songId);
	}

	

	

}
