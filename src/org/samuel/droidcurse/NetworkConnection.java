package org.samuel.droidcurse;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class NetworkConnection {
	private static NetworkConnection singletonInstance;
	
	public static final String DEFAULT_HOST = "192.168.0.123";
	public static final int DEFAULT_PORT = 5000;
	
	private String host;
	private int port;
	private Socket socket;
	private NetworkReaderThread networkReaderThread;	
	private BufferedWriter writer;
	
	private ResponseMonitor artistMonitor;
	private ResponseMonitor listMonitor;
	
	private Model model;
	
	public NetworkConnection() {
		host = DEFAULT_HOST;
		port = DEFAULT_PORT;
		artistMonitor = new ResponseMonitor();
		listMonitor = new ResponseMonitor();
		model = Model.getInstance();
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
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			networkReaderThread = new NetworkReaderThread(socket, artistMonitor, listMonitor);
			networkReaderThread.start();
	
			/* Get list of artists and add them to the model */
			Log.d("DroidCurse", "Waiting for artist response");
			writer.write("artist\r\n");
			writer.flush();
			String[] artistList = artistMonitor.getMessages();
			Log.d("DroidCurse", "Got artist response:");
			for (String s : artistList) {
				Log.d("DroidCurse", s);
			}
			Log.d("DroidCurse", "Setting artist: "+0);
			model.setArtistList(artistList);
		
			/* Get list of song and add them to the model */
			Log.d("DroidCurse", "Waiting for list response");
			writer.write("list\r\n");
			writer.flush();
			String[] songList = listMonitor.getMessages();
			Log.d("DroidCurse", "Got list response:");
			for (String s : songList) {
				Log.d("DroidCurse", s);
			}
			model.setSongList(songList);
			
			return true;
		} catch (UnknownHostException e) {
			Log.e("DroidCurse", "Unknown host: "+host);
			return false;
		} catch (IOException e) {
			Log.e("DroidCurse", "Couldn't create socket to: "+host+":"+port);
			return false;
		}
	}

	public void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
			Log.e("DroidCurse", "Couldn't close socket");
			e.printStackTrace();
		} catch (NullPointerException e) {
			Log.e("DroidCurse", "Socket already closed");
			e.printStackTrace();
		}
	}
	
	
	public static NetworkConnection getInstance() {
		if (singletonInstance == null) {
			singletonInstance = new NetworkConnection();
		}
		return singletonInstance;
	}
	
	
	// TODO: Do this in a seperate thread
	public void setArtist(int position) {
		Log.d("DroidCurse", "Setting artist: "+position);
		try {
			writer.write("set artist "+position+"\r\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("DroidCurse", "Setting artist done");
	}

	// TODO: Do this in a seperate thread
	public void playSong(int position) {
		Log.d("DroidCurse", "Playing song: "+position);
		try {
			writer.write("play "+position+"\r\n");
			writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("DroidCurse", "Playing song done");
	}

	
}
