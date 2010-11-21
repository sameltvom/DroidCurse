package org.samuel.droidcurse;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

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
	
	public NetworkConnection() {
		host = DEFAULT_HOST;
		port = DEFAULT_PORT;
		artistMonitor = new ResponseMonitor();
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
			networkReaderThread = new NetworkReaderThread(socket, artistMonitor);
			networkReaderThread.start();
			
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
	
	
	public String[] getListOfSongs() {
		//return new String[]{"Bob Dylan - I want you", "Johnny cash - Get the rhythm"};
		try {
			Log.d("DroidCurse", "Waiting for artist response");
			writer.write("artist\r\n");
			writer.flush();
			String[] res = artistMonitor.getMessages();
			Log.d("DroidCurse", "Got artist response:");
			for (String s : res) {
				Log.d("DroidCurse", s);
			}
			return res;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static NetworkConnection getInstance() {
		if (singletonInstance == null) {
			singletonInstance = new NetworkConnection();
		}
		return singletonInstance;
	}

	
}
