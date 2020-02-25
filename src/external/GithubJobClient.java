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
	private static final String PATH = "/positions.json";

	public List<Item> search(String description, String location, boolean full_time) {
		if (description != null) {
			try {
				description = URLEncoder.encode(description, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		StringBuilder queryBuilder = new StringBuilder();
		description = String.format("description=%s", description);
		queryBuilder.append(description).toString();

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
			JSONArray obj = new JSONArray(responseBuilder.toString());
			if (obj != null) {
				return getItemList(obj);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new ArrayList<>();
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

			itemList.add(builder.build());
		}
		return itemList;
	}

	public static void main(String[] args) {
		GithubJobClient client = new GithubJobClient();
		List<Item> jobs = client.search("java", null, false);
//		System.out.println(jobs.get(0).toJSONObject());
		for (Item job : jobs) {
			System.out.println(job.toJSONObject());
		}

	}
}
