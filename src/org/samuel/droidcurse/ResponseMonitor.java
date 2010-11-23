package org.samuel.droidcurse;

import java.util.LinkedList;

import android.util.Log;

/* This mailbox is used by the network reader thread. One mailbox is used for each kind of response
 * There is one mailbox for the response for the artist command, one for the songs, etc */
public class ResponseMonitor {
	private LinkedList<String> mailBox;
	private boolean done;
	
	public ResponseMonitor() {
		mailBox = new LinkedList<String>();
	}
	
	public synchronized void addMessage(String message) {
		done = false;
		mailBox.add(message);
	}
	
	// when all messages are added, tell the listener to wake up
	/*public synchronized void setReady() {
		notifyAll();
	}*/
	
	// when done, call this
	public synchronized String[] getMessages() {
		String []res = new String[mailBox.size()];
		mailBox.toArray(res);
		mailBox.clear();
		done = true;
		notifyAll();
		return res;
	}

	public synchronized void waitForResponse() {
		Log.d("DroidCurse", "Waiting for monitor ready");
		while (!done) {
			try {
				wait();
				Log.d("DroidCurse", "Woken up!");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Log.d("DroidCurse", "Waiting for monitor ready - finished");
	}
}
