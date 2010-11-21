package org.samuel.droidcurse;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

public class NetworkConnection {
	public static final String DEFAULT_HOST = "192.168.0.123";
	public static final int DEFAULT_PORT = 5000;
	
	private String host;
	private int port;
	private Socket socket;
	private Activity activity;
	private NetworkReaderThread networkReaderThread;
	
	
	public NetworkConnection(Activity activity) {
		this.activity = activity;
		host = DEFAULT_HOST;
		port = DEFAULT_PORT;
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
			networkReaderThread = new NetworkReaderThread(socket);
			networkReaderThread.start();
			
			return true;
		} catch (UnknownHostException e) {
			Log.e("DroidCurse", "Unknown host: "+host);
			Toast.makeText(activity.getApplicationContext(), "Unknown host", Toast.LENGTH_SHORT).show();
			return false;
		} catch (IOException e) {
			Log.e("DroidCurse", "Couldn't create socket to: "+host+":"+port);
			Toast.makeText(activity.getApplicationContext(), "Couldn't create connection", Toast.LENGTH_SHORT).show();
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
	
	
}
