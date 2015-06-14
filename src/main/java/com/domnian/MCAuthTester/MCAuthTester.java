package com.domnian.MCAuthTester;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MCAuthTester {

	static String aToken = null;
	static String cToken = null;
	static String authResponse;
	static String signout;
	static JsonParser parser = new JsonParser();
	static String cUUID = null;

	public static void main(String[] args) {
		doAuthenticate(args);
		doServerJoin();
	}

	public static void doAuthenticate(String[] args) {
		try {
			authResponse = post("authenticate", genAuthPayload(args[0], args[1], cToken).toString());
		} catch (Exception e) {
			//Fail Silently (Most Likely HTTP 403 Forbidden)
		}
		System.out.println("Authentication Response: " + (authResponse.equalsIgnoreCase("null") ? "FAILED" : authResponse));
		JsonObject authJson = (JsonObject)parser.parse(authResponse);
		aToken = authJson.get("accessToken").getAsString();
		cToken = authJson.get("clientToken").getAsString();
		JsonArray aProfiles = (JsonArray) authJson.get("availableProfiles");
		JsonObject sProfile = (JsonObject) authJson.get("selectedProfile");
		System.out.println("Access Token: " + (aToken != null ? aToken : "FAILED"));
		System.out.println("Client Token: " + (aToken != null ? cToken : "FAILED"));
		System.out.println("Available Profiles:");
		for ( int i = 0; i < aProfiles.size(); i++ ) {
			JsonObject profile = (JsonObject) aProfiles.get(i);
			String id = profile.get("id").getAsString();
			String name = profile.get("name").getAsString();
			System.out.println(" - " + id + "/" + name);
		}
		String id = sProfile.get("id").getAsString();
		String name = sProfile.get("name").getAsString();
		System.out.println("Selected Profile: " + id + "/" + name);
	}
	
	public static void doServerJoin() {
		System.out.println("Server Join Not Yet Implemented: Researching How To Send Handshake Packets");
	}

	public static String post(String method, String payload) throws Exception {
		URL url = new URL("https://authserver.mojang.com/" + method);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);

		connection.setRequestProperty("Content-Type", "application/json;");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestProperty("Method", "POST");

		OutputStream os = connection.getOutputStream();
		os.write(payload.getBytes("UTF-8"));
		os.close();

		InputStream is = connection.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = null;
		StringBuffer sb = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		is.close();
		return sb.toString();
	}

	public static JsonObject genAuthPayload(String username, String password,
			String cid) {
		JsonObject req = new JsonObject();
		JsonObject agent = new JsonObject();
		agent.addProperty("name", "Minecraft");
		req.add("agent", agent);
		req.addProperty("username", username);
		req.addProperty("password", password);
		if (cid != null) {
			req.addProperty("clientToken", cid);
		}
		return req;
	}

}
