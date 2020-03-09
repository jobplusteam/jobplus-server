package external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Item;
import entity.Item.ItemBuilder;

public class GithubJobClient {
	private static final String HOST = "https://jobs.github.com";
	private static final String PATH = "/positions";

	public List<Item> search(String description, String location, String full_time)
			throws UnsupportedEncodingException {

		// encode keyword with space
		if (description != null) {
			description = URLEncoder.encode(description, "UTF-8");
		}

		if (location != null) {
			location = URLEncoder.encode(location, "UTF-8");
		}

		// Construct url
		String query = String.format("description=%s&location=%s&full_time=%s", description, location, full_time);
		String url = HOST + PATH + ".json?" + query;

		return getItemsFromGithub(url);
	}
	
	public List<Item> nearby(double lat, double lon) {
		String query = String.format("lat=%s&long=%s", lat, lon);
		String url = HOST + PATH + ".json?" + query;

		return getItemsFromGithub(url);

	}

	public List<Item> getItemsFromGithub(String url) {
		StringBuilder responseBuilder = new StringBuilder();
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.connect();

			int responseCode = connection.getResponseCode();
			System.out.println("Sending requests to url: " + url);
			System.out.println("Response code: " + responseCode);

			if (responseCode != 200) {
				return new ArrayList<>();
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
			JSONArray array = new JSONArray(responseBuilder.toString());
			if (array != null) {
				return getItemList(array);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
	}

	

	public Item getJobfromJobId(String jobId) {
		String url = HOST + PATH + "/" + jobId + ".json";

		StringBuilder responseBuilder = new StringBuilder();
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.connect();

			int responseCode = connection.getResponseCode();
			System.out.println("Sending requests to url: " + url);
			System.out.println("Response code: " + responseCode);

			if (responseCode != 200) {
				return null;
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
			if (obj != null) {
				return getItemObject(obj);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;

	}

	// Convert JSONObject to a item object
	private Item getItemObject(JSONObject job) throws JSONException {

		ItemBuilder builder = new ItemBuilder();
		if (!job.isNull("id")) {
			builder.setId(job.getString("id"));
		}

		if (!job.isNull("title")) {
			builder.setTitle(job.getString("title"));
		}

		if (!job.isNull("type")) {
			builder.setType(job.getString("type"));
		}

		if (!job.isNull("created_at")) {
			builder.setCreatedAt(job.getString("created_at"));
		}

		if (!job.isNull("company")) {
			builder.setCompany(job.getString("company"));
		}
		if (!job.isNull("url")) {
			builder.setUrl(job.getString("url"));
		}
		if (!job.isNull("company_url")) {
			builder.setCompanyUrl(job.getString("company_url"));
		}

		if (!job.isNull("description")) {
			builder.setDescription(job.getString("description"));
		}
		
		if (!job.isNull("how_to_apply")) {
			builder.setHowToApply(job.getString("how_to_apply"));
		}

		return builder.build();
	}

	// Convert JSONArray to a list of item objects.
	private List<Item> getItemList(JSONArray jobs) throws JSONException {
		List<Item> itemList = new ArrayList<>();
		for (int i = 0; i < jobs.length(); ++i) {
			JSONObject job = jobs.getJSONObject(i);

			ItemBuilder builder = new ItemBuilder();
			if (!job.isNull("id")) {
				builder.setId(job.getString("id"));
			}

			if (!job.isNull("title")) {
				builder.setTitle(job.getString("title"));
			}

			if (!job.isNull("type")) {
				builder.setType(job.getString("type"));
			}

			if (!job.isNull("created_at")) {
				builder.setCreatedAt(job.getString("created_at"));
			}

			if (!job.isNull("company")) {
				builder.setCompany(job.getString("company"));
			}
			if (!job.isNull("url")) {
				builder.setUrl(job.getString("url"));
			}
			if (!job.isNull("company_url")) {
				builder.setCompanyUrl(job.getString("company_url"));
			}

			if (!job.isNull("description")) {
				builder.setDescription(job.getString("description"));
			}
			
			if (!job.isNull("how_to_apply")) {
				builder.setHowToApply(job.getString("how_to_apply"));
			}

			itemList.add(builder.build());
		}
		return itemList;
	}

}
