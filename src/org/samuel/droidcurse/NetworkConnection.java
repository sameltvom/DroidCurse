package org.samuel.droidcurse;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;

import android.util.Log;

public class NetworkConnection {
	private static NetworkConnection singletonInstance;
	
	//private String[] hostList = new String[]{"192.168.0.100", "192.168.0.123"};
	private LinkedList<String> hostList; 
	
	// Host: trudy
	//public static final String DEFAULT_HOST = "192.168.0.123";
	
	// Host: dave
	public static final String DEFAULT_HOST = "192.168.0.100";
	
	public static final int DEFAULT_PORT = 5000;
	
	private String host;
	private int port;
	private Socket socket;
	private NetworkReaderThread networkReaderThread;	
	private BufferedWriter writer;
	
	
	
	private Model model;
	
	public NetworkConnection() {
		host = DEFAULT_HOST;
		port = DEFAULT_PORT;
		
		model = Model.getInstance();
		hostList = new LinkedList<String>();
		hostList.add("192.168.0.100");
		hostList.add("192.168.0.123");
		
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean connect() {
		try {
			socket = new Socket(host, port);
			// it should respond quick
			//socket.setSoTimeout(6000);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			networkReaderThread = new NetworkReaderThread(socket, model);
			networkReaderThread.start();
	
			/* Get list of artists and add them to the model */
			OurLog.d("DroidCurse", "Network: Sending getListofArtists");
			sendGetListOfArtists();
			OurLog.d("DroidCurse", "Network: Sending getListofArtists - finished");
			OurLog.d("DroidCurse", "Network: Waiting for response from artistMonitor...");
			model.getArtistMonitor().waitForResponse();
			OurLog.d("DroidCurse", "Network: Waiting for response from artistMonitor - finished");
			
			OurLog.d("DroidCurse", "Network: Sending getListofAlbums");
			sendGetListOfAlbums();
			OurLog.d("DroidCurse", "Network: Sending getListofAlbums - finished");
			OurLog.d("DroidCurse", "Network: Waiting for response from albumMonitor...");
			model.getAlbumMonitor().waitForResponse();
			OurLog.d("DroidCurse", "Network: Waiting for response from albumMonitor - finished");
			
			
			/* Get list of song and add them to the model */
			OurLog.d("DroidCurse", "Network: Sending getListofSongs");
			sendGetListOfSongs();
			OurLog.d("DroidCurse", "Network: Sending getListofSongs - finished");
			OurLog.d("DroidCurse", "Network: Waiting for response from listMonitor...");
			model.getListMonitor().waitForResponse();
			OurLog.d("DroidCurse", "Network: Waiting for response from listMonitor - finished");
			
			return true;
		} catch (UnknownHostException e) {
			OurLog.e("DroidCurse", "Unknown host: "+host);
			return false;
		} catch (IOException e) {
			OurLog.e("DroidCurse", "Couldn't create socket to: "+host+":"+port);
			return false;
		}
	}

	public void disconnect() {
		try {
			quit();
			OurLog.d("DroidCurse", "Network: Closing socket");
			socket.close();
			OurLog.d("DroidCurse", "Network: Closing socket - finished");
		} catch (IOException e) {
			OurLog.e("DroidCurse", "Couldn't close socket");
			e.printStackTrace();
		} catch (NullPointerException e) {
			OurLog.e("DroidCurse", "Socket already closed");
			e.printStackTrace();
		}
	}
	
	
	public static NetworkConnection getInstance() {
		if (singletonInstance == null) {
			singletonInstance = new NetworkConnection();
		}
		return singletonInstance;
	}
	
	public void quit() {
		OurLog.d("DroidCurse", "Quiting");
		try {
			writer.write("quit\r\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OurLog.d("DroidCurse", "Quiting - finished");
		
	}
	
	// TODO: Do this in a seperate thread
	public void playSong(int position) {
		OurLog.d("DroidCurse", "Playing song: "+position);
		try {
			writer.write("play "+position+"\r\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OurLog.d("DroidCurse", "Playing song done");
	}

	void sendGetListOfArtists() {
		try {
			OurLog.d("DroidCurse", "Sending \"artist\" command");
			writer.write("artist\r\n");
			writer.flush();
		} catch (IOException e) {
			OurLog.e("DroidCurse", "Couldn't get list of artists");
			e.printStackTrace();
		}
	}
	
	void sendGetListOfAlbums() {
		try {
			OurLog.d("DroidCurse", "Sending \"album\" command");
			writer.write("album\r\n");
			writer.flush();
		} catch (IOException e) {
			OurLog.e("DroidCurse", "Couldn't get list of albums");
			e.printStackTrace();
		}
	}
	
	void sendGetListOfSongs() {
		try {
			OurLog.d("DroidCurse", "Sending \"list\" command");
			writer.write("list\r\n");
			writer.flush();
		} catch (IOException e) {
			OurLog.e("DroidCurse", "Couldn't get list of songs");
			e.printStackTrace();
		}
	}

	public void setArtist(int position) {
		OurLog.d("DroidCurse", "Setting artist: "+position);
		try {
			writer.write("set artist "+position+"\r\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setAllArtists() {
		OurLog.d("DroidCurse", "Setting all artists");
		try {
			writer.write("all artists\r\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setAllAlbums() {
		OurLog.d("DroidCurse", "Setting all albums");
		try {
			writer.write("all albums\r\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setAlbum(int albumId) {
		OurLog.d("DroidCurse", "Setting album: "+albumId);
		try {
			writer.write("set album "+albumId+"\r\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public LinkedList<String> getListOfHosts() {
		return hostList;
	}
}
