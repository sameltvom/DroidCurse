package org.samuel.droidcurse;

public class Model {
	private static Model model;
	private String[] listOfSongs;
	private String[] listOfArtists;
	
	public Model() {
		listOfArtists = new String[]{"0 - Bob Dylan", "1 - Swen Andersson", "2 - Gullbritt"};
		listOfSongs = new String[]{"0 - Bob Dylan - Heavy rain", "1 - Bob Dylan - Idiot wind",
									"2 - Bob Dylan - I want you", "3 - Swen Andersson - Rosor",
									"4 - Swen Andersson - Damer", "5 - Gullbritt - Gamla guvvar"};
	}
	
	public static Model getInstance() {
		if (model == null) {
			model = new Model();
		}
		return model;
	}


	synchronized public String[] getListOfSongs() {
		return listOfSongs;
	}
	
	synchronized public String[] getListOfArtists() {
		return listOfArtists;
	}

	synchronized public void setArtistList(String[] listOfArtists) {
		this.listOfArtists = listOfArtists;
	}

	synchronized public void setSongList(String[] listOfSongs) {
		this.listOfSongs = listOfSongs;
	}

	synchronized public void changeArtist(int artistId) {
		// TODO: Add a message to a mailbox that later is being consumed by another thread
		NetworkConnection networkConnection = NetworkConnection.getInstance();
		networkConnection.setArtist(artistId);
	}

	synchronized public void changeSong(int songId) {
		// TODO: Add a message to a mailbox that later is being consumed by another thread
		NetworkConnection networkConnection = NetworkConnection.getInstance();
		networkConnection.playSong(songId);
	}

}
