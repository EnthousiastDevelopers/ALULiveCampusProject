package com.tolotranet.livecampus.Transp;


import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Transp_FileOperations {

	//Have to keep permissions for writing and reading external storage
	public static void StoreData(String input) throws IOException {
		File Root = Environment.getExternalStorageDirectory();
		File Dir = new File(Root.getAbsoluteFile() + "/Android-CampusLive");
		if (!Dir.exists()) {
			Dir.mkdir();
		}
		File file = new File(Dir, "Transp.txt");
		FileOutputStream fileoutputstream = new FileOutputStream(file);
		fileoutputstream.write(input.getBytes());
		fileoutputstream.close();
		Log.d("hello", "writing complete");
	}

	public static String ReadData() throws IOException {
		File Root = Environment.getExternalStorageDirectory();
		File Dir = new File(Root.getAbsoluteFile() + "/Android-CampusLive");

		if (!Dir.exists()) {
			Log.d("hello", "reading complete. file doesn't exist");
			return "";

		}

		File newfile = new File(Dir, "Transp.txt");
		FileInputStream fileinputstream = new FileInputStream(newfile);
		InputStreamReader inputreader = new InputStreamReader(fileinputstream);
		BufferedReader buffreader = new BufferedReader(inputreader);
		StringBuffer stringbuffer = new StringBuffer();
		String ReadMessage;
		while (((ReadMessage = buffreader.readLine()) != null)) {
			stringbuffer.append(ReadMessage + "\n");
		}
		inputreader.close();
		String stringRes = stringbuffer.toString();
		//Log.d("hello", stringRes);
		return stringbuffer.toString();

	}

}
