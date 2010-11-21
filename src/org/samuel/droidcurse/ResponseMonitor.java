package org.samuel.droidcurse;

import java.util.LinkedList;

/* This mailbox is used by the network reader thread. One mailbox is used for each kind of response
 * There is one mailbox for the response for the artist command, one for the songs, etc */
public class ResponseMonitor {
	private LinkedList<String> mailBox;
	
	public ResponseMonitor() {
		mailBox = new LinkedList<String>();
	}
	
	public synchronized void addMessage(String message) {
		mailBox.add(message);
	}
	
	// when all messages are added, tell the listener to wake up
	public synchronized void setReady() {
		notifyAll();
	}
	
	public synchronized String[] getMessages() {
		while (mailBox.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String []res = new String[mailBox.size()];
		mailBox.toArray(res);
		mailBox.clear();
		notifyAll();
		return res;
	}
}
