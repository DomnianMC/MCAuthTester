package com.domnian.MCAuthTester;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MCAuthTester {

	static String aToken = null;
	static String cToken = null;
	static String authResponse;
	static JsonParser parser = new JsonParser();

	public static void main(String[] args) {
		try {
			authResponse = post("authenticate", genAuthPayload(args[0], args[1], cToken).toString());
		} catch (Exception e) {
			//Fail Silently (Most Likely HTTP 403 Forbidden)
		}
		System.out.println("Authentication Response: " + (authResponse.equalsIgnoreCase("null") ? "FAILED" : authResponse));
		JsonObject authJson = (JsonObject)parser.parse(authResponse);
		aToken = authJson.get("accessToken").getAsString();
		cToken = authJson.get("clientToken").getAsString();
		System.out.println("accessToken: " + (aToken != null ? aToken : "FAILED"));
		System.out.println("clientToken: " + (aToken != null ? cToken : "FAILED"));
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
		JsonObject reqparam = new JsonObject();
		reqparam.addProperty("username", username);
		reqparam.addProperty("password", password);
		if (cid != null) {
			reqparam.addProperty("clientToken", cid);
		}
		return reqparam;
	}

}
