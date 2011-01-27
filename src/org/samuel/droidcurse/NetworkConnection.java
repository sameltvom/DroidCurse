package org.samuel.droidcurse;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import android.os.Handler;
import android.os.Message;

public class NetworkConnection {
	private static NetworkConnection singletonInstance;
	
	//private String[] hostList = new String[]{"192.168.0.100", "192.168.0.123"};
	private LinkedList<String> hostList; 
	
	// Host: dave
	public static final String DEFAULT_HOST = "192.168.0.100";
	
	public static final int DEFAULT_PORT = 5000;
	
	private String host;
	private int port;
	private Socket socket;
	private NetworkReaderThread networkReaderThread;	
	private BufferedWriter writer;
	
	public NetworkConnection() {
		host = DEFAULT_HOST;
		port = DEFAULT_PORT;
		
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

	public void connect(Handler connectionHandler) {	
		Message message = Message.obtain();
		
		try {
			socket = new Socket(host, port);
			// it should respond quick
			//socket.setSoTimeout(6000);
			writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			networkReaderThread = new NetworkReaderThread(socket);
			networkReaderThread.start();
			
			message.arg1 = 1;
		} catch (UnknownHostException e) {
			OurLog.e("DroidCurse", "Unknown host: "+host);
			message.arg1 = 0;
		} catch (IOException e) {
			OurLog.e("DroidCurse", "Couldn't create socket to: "+host+":"+port);
			message.arg1 = 0;
		}
		
		// let droid curse know how it went
		connectionHandler.sendMessage(message);
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
