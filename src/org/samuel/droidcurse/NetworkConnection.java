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

	private String m_host;
	private int m_port;
	private Socket socket;
	private Activity m_activity;
	
	public NetworkConnection(Activity activity) {
		m_activity = activity;
		m_host = DEFAULT_HOST;
		m_port = DEFAULT_PORT;
	}

	public void setHost(String host) {
		m_host = host;
	}

	public void setPort(int port) {
		m_port = port;
	}

	public void connect() {
		try {
			socket = new Socket(m_host, m_port);
			Toast.makeText(m_activity.getApplicationContext(), "Connected to: "+m_host, Toast.LENGTH_SHORT).show();
		} catch (UnknownHostException e) {
			Log.e("DroidCurse", "Unknown host");
			Toast.makeText(m_activity.getApplicationContext(), "Unknown host", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			Log.e("DroidCurse", "Couldn't create socket");
			Toast.makeText(m_activity.getApplicationContext(), "Couldn't create connection", Toast.LENGTH_SHORT).show();
		}
	}
	
	
}
