package org.samuel.droidcurse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.LinkedList;

import android.util.Log;

public class NetworkReaderThread extends Thread {
	private Socket socket;
	private ResponseMonitor artistMonitor;
	private ResponseMonitor albumMonitor;
	private ResponseMonitor listMonitor;
	private NetworkConnection networkConnection;
	private Model model;
	
	public NetworkReaderThread(Socket socket, ResponseMonitor artistMonitor, ResponseMonitor albumMonitor, ResponseMonitor listMonitor) {
		this.socket = socket;
		this.artistMonitor = artistMonitor;
		this.albumMonitor = albumMonitor;
		this.listMonitor = listMonitor;
		this.model = Model.getInstance();
		this.networkConnection = NetworkConnection.getInstance();
	}
	
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			while (true) {
				String line = reader.readLine();
				OurLog.i("DroidCurse", "ReaderThread: Got line: "+line);
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
					OurLog.d("DroidCurse", "ReaderThread: Getting messages from artistMonitor");
					LinkedList<String> listOfArtists = artistMonitor.getMessages();
					
					// adding "all artists" item in the beginning
			        listOfArtists.addFirst("<All artists>");
					
					/*OurLog.d("DroidCurse", "ReaderThread: Getting messages from artistMonitor - finished");
					OurLog.d("DroidCurse", "ReaderThread: listOfArtists:");
					for (String s : listOfArtists) {
						OurLog.d("DroidCurse", "ReaderThread: artist: "+s);	
					}*/
					
					OurLog.d("DroidCurse", "ReaderThread: Setting model artist list");
					model.setArtistList(listOfArtists);
					OurLog.d("DroidCurse", "ReaderThread: Setting model artist list - finished");
				} if (line.startsWith("ALBUM ALBUM_ITM ")) {
					OurLog.d("DroidCurse", "ReaderThread: Getting album item");
					// an album item
					String words[] = line.split("ALBUM ALBUM_ITM ");
					String albumItem = words[1];
					albumMonitor.addMessage(albumItem);
				} else if (line.startsWith("ALBUM ALBUM_END")) {
					OurLog.d("DroidCurse", "ReaderThread: Getting messages from albumMonitor");
					LinkedList<String> listOfAlbums = albumMonitor.getMessages();
					
					// adding "all albums" item in the beginning
			        listOfAlbums.addFirst("<All albums>");
					
					/*OurLog.d("DroidCurse", "ReaderThread: Getting messages from albumMonitor - finished");
					OurLog.d("DroidCurse", "ReaderThread: listOfAlbums:");
					for (String s : listOfAlbums) {
						OurLog.d("DroidCurse", "ReaderThread: album: "+s);	
					}*/
					
					OurLog.d("DroidCurse", "ReaderThread: Setting model album list");
					model.setAlbumList(listOfAlbums);
					OurLog.d("DroidCurse", "ReaderThread: Setting model album list - finished");
				}
				
				else if (line.startsWith("LIST LIST_ITM")) {
					// a song item
					String words[] = line.split("LIST LIST_ITM ");
					String songItem = words[1];
					listMonitor.addMessage(songItem);
				} else if (line.startsWith("LIST LIST_END")) {
					// now all song items are sent
					// this call will get the messages and notify those who wait for it
					// it's not a typical mailbox, more like an observable
					OurLog.d("DroidCurse", "ReaderThread: Getting messages from listMonitor");
					LinkedList<String> listOfSongs = listMonitor.getMessages();
					/*OurLog.d("DroidCurse", "ReaderThread: Getting messages from listMonitor - finished");
					OurLog.d("DroidCurse", "ReaderThread: listOfSongs:");
					for (String s : listOfSongs) {
						OurLog.d("DroidCurse", "ReaderThread: song: "+s);	
					}*/
					OurLog.d("DroidCurse", "ReaderThread: Setting model song list");
					model.setSongList(listOfSongs);
					OurLog.d("DroidCurse", "ReaderThread: Setting model song list - finished");
				} else if (line.startsWith("INF_ARTIST")) {
					// the artist has changed
					OurLog.d("DroidCurse", "ReaderThread: Artists changed, fetching album list...");
					networkConnection.sendGetListOfAlbums();
					OurLog.d("DroidCurse", "ReaderThread: Artists changed, fetching song list...");
					networkConnection.sendGetListOfSongs();
					
					//String []listOfSongs = networkConnection.getListOfSongs();
					//OurLog.d("DroidCurse", "Fetching song list done, setting song list to model");
					//model.setSongList(listOfSongs);
					//OurLog.d("DroidCurse", "Song list finished");
				} else if (line.startsWith("INF_ALBUM")) {
					// the artist has changed
					OurLog.d("DroidCurse", "ReaderThread: Albums changed, fetching song list...");
					networkConnection.sendGetListOfSongs();
					
					//String []listOfSongs = networkConnection.getListOfSongs();
					//OurLog.d("DroidCurse", "Fetching song list done, setting song list to model");
					//model.setSongList(listOfSongs);
					//OurLog.d("DroidCurse", "Song list finished");
				} else if (line.startsWith("ALL_ARTISTS ALL_ARTISTS_OK")) {
					OurLog.d("DroidCurse", "ReaderThread: Artists changed, fetching album list...");
					// this will trigger INF_ALBUM which will request song list, sno
					// there is no need to do that here
					networkConnection.sendGetListOfAlbums();
					//OurLog.d("DroidCurse", "ReaderThread: Artists changed, fetching song list...");
					//networkConnection.sendGetListOfSongs();
					
				}
			}
		} catch (IOException e) {
			OurLog.e("DroidCurse", "Couldn't readline, probably disconnected");
		}
		OurLog.i("DroidCurse", "Exiting reader thread");
		
	}
}
