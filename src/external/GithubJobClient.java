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
	
	public JSONArray search(String description, String location, boolean full_time) {
		if (description != null) {
			try {
				description = URLEncoder.encode(description, "UTF-8");
			}
			catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(description).append(location).append(full_time).toString();
		//append
		
		String url = HOST + PATH + "?" + queryBuilder.toString();
		
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
	
	public JSONArray nearby(double lat, double lon) {
		String query = String.format("lat=%s&long=%s", lat, lon);
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
		return new JSONArray();
		
	}
	public static void main(String[] args) {
		GithubJobClient client = new GithubJobClient();
		JSONArray jobs = client.search("java", null, false);
		try {
			for(int i = 0; i <jobs.length(); ++i) {
				JSONObject job = jobs.getJSONObject(i);
				System.out.println(job.toString(2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}


