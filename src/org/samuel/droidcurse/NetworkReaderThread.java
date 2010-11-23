package org.samuel.droidcurse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import android.util.Log;

public class NetworkReaderThread extends Thread {
	private Socket socket;
	private ResponseMonitor artistMonitor;
	private ResponseMonitor listMonitor;
	private NetworkConnection networkConnection;
	private Model model;
	
	public NetworkReaderThread(Socket socket, ResponseMonitor artistMonitor, ResponseMonitor listMonitor) {
		this.socket = socket;
		this.artistMonitor = artistMonitor;
		this.listMonitor = listMonitor;
		this.model = Model.getInstance();
		this.networkConnection = NetworkConnection.getInstance();
	}
	
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			while (true) {
				String line = reader.readLine();
				Log.i("DroidCurse", "ReaderThread: Got line: "+line);
				if (line.startsWith("ARTIST ARTIST_ITM ")) {
					// an artist item
					String words[] = line.split("ARTIST ARTIST_ITM ");
					String artistItem = words[1];
					artistMonitor.addMessage(artistItem);
				} else if (line.startsWith("ARTIST ARTIST_END")) {
					// now all artist items are sent
					//artistMonitor.setReady();
					// this call will get the messages and notify those who wait for it
					// it's not a typical mailbox, more like an observable
					Log.d("DroidCurse", "ReaderThread: Getting messages from artistMonitor");
					String[] listOfArtists = artistMonitor.getMessages();
					Log.d("DroidCurse", "ReaderThread: Getting messages from artistMonitor - finished");
					Log.d("DroidCurse", "ReaderThread: Setting model artist list");
					model.setArtistList(listOfArtists);
					Log.d("DroidCurse", "ReaderThread: Setting model artist list - finished");
				} else if (line.startsWith("LIST LIST_ITM")) {
					// a song item
					String words[] = line.split("LIST LIST_ITM ");
					String songItem = words[1];
					listMonitor.addMessage(songItem);
				} else if (line.startsWith("LIST LIST_END")) {
					// now all song items are sent
					// this call will get the messages and notify those who wait for it
					// it's not a typical mailbox, more like an observable
					Log.d("DroidCurse", "ReaderThread: Getting messages from listMonitor");
					String[] listOfSongs = listMonitor.getMessages();
					Log.d("DroidCurse", "ReaderThread: Getting messages from listMonitor - finished");
					Log.d("DroidCurse", "ReaderThread: Setting model song list");
					model.setSongList(listOfSongs);
					Log.d("DroidCurse", "ReaderThread: Setting model song list - finished");
				} else if (line.startsWith("INF_ARTIST")) {
					// the artist has changed
					Log.d("DroidCurse", "ReaderThread: Artists changed, fetching song list...");
					networkConnection.sendGetListOfSongs();
					//String []listOfSongs = networkConnection.getListOfSongs();
					//Log.d("DroidCurse", "Fetching song list done, setting song list to model");
					//model.setSongList(listOfSongs);
					//Log.d("DroidCurse", "Song list finished");
				}
			}
		} catch (IOException e) {
			Log.e("DroidCurse", "Couldn't readline, probably disconnected");
		}
		Log.i("DroidCurse", "Exiting reader thread");
		
	}
}
