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
	
	public NetworkReaderThread(Socket socket, ResponseMonitor artistMonitor, ResponseMonitor listMonitor) {
		this.socket = socket;
		this.artistMonitor = artistMonitor;
		this.listMonitor = listMonitor;
	}
	
	public void run() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			while (true) {
				String line = reader.readLine();
				Log.i("DroidCurse", "Got line: "+line);
				if (line.startsWith("ARTIST ARTIST_ITM ")) {
					String words[] = line.split("ARTIST ARTIST_ITM ");
					String artistItem = words[1];
					artistMonitor.addMessage(artistItem);
				} else if (line.startsWith("ARTIST ARTIST_END")) {
					artistMonitor.setReady();
				} else if (line.startsWith("LIST LIST_ITM")) {
					String words[] = line.split("LIST LIST_ITM ");
					String songItem = words[1];
					listMonitor.addMessage(songItem);
				} else if (line.startsWith("LIST LIST_END")) {
					listMonitor.setReady();
				}
			}
		} catch (IOException e) {
			Log.e("DroidCurse", "Couldn't readline, probably disconnected");
		}
		Log.i("DroidCurse", "Exiting reader thread");
		
	}
}
