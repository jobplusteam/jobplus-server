package external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GithubJobClient {
	private static final String HOST = "https://jobs.github.com";
	private static final String PATH = "/positions.json?";
	private static final String DEFAULT_DESCRIPTION = "job";
	
	public JSONArray search(double lat, double lon, String description, String location, boolean full_time) {
		if (description == null) {
			description = DEFAULT_DESCRIPTION;
		}
		try {
			description = URLEncoder.encode(descripton, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String query = String.format("latlon=%s,%s", lat, lon, description, location, full_time);
		String url = HOST + PATH + "?" + query;
		StringBuilder responseBuilder = new StringBuilder();
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			
			int responseCode = connection.getResponseCode();
			System.out.println("Sending requests to url: " + url);
			System.out.println("Response code: " + responseCode);
			
			if (responseCode != 200) {
				return new JSONArray();
			}
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null) {
				responseBuilder.append(line);
			}
			reader.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			JSONObject obj = new JSONObject(responseBuilder.toString());
			if (!obj.isNull("_embedded")) {
				JSONObject embedded = obj.getJSONObject("_embedded");
				return embedded.getJSONArray("jobs");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return new JSONArray();
	}
	public static void main(String[] args) {
		GithubJobClient client = new GithubJobClient();
		JSONObject jobs = client.search(lat, lon, description, location, full_time)
	}
}


