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
		done = false;
	}
	
	public synchronized void addMessage(String message) {
		done = false;
		OurLog.d("DroidCurse", "Monitor: Adding message: "+message);
		mailBox.add(message);
	}
	
	// when done, call this
	public synchronized LinkedList<String> getMessages() {
		LinkedList<String> tmp = new LinkedList<String>(mailBox);
		mailBox.clear();
		done = true;
		notifyAll();
		return tmp;
	}

	public synchronized void waitForResponse() {
		OurLog.d("DroidCurse", "Monitor: Waiting for monitor ready");
		while (!done) {
			try {
				wait();
				OurLog.d("DroidCurse", "Monitor: Woken up!");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		OurLog.d("DroidCurse", "Monitor: Waiting for monitor ready - finished");
		done = false;
	}
}
