package org.samuel.droidcurse;

import android.util.Log;

public class OurLog {

	private static boolean debug = false;

	public static void d(String tag, String message){
		if (debug) {
			Log.d(tag, message);
		}
	}

	public static void i(String tag, String message){
		if (debug) {
			Log.i(tag, message);
		}
	}

	public static void e(String tag, String message){
		if (debug) {
			Log.e(tag, message);
		}
	}
}
