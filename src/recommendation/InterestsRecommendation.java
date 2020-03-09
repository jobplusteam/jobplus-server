package recommendation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db.MySQLConnection;
import entity.Item;
import external.GithubJobClient;

public class InterestsRecommendation {

	public List<Item> recommendItems(String userId) throws UnsupportedEncodingException {

		// Step 1, get all interests job_id from db
		MySQLConnection connection = new MySQLConnection();
		List<String> interests = connection.getInterests(userId);
		connection.close();

		// Step 2, get all related results to interests from github api
		GithubJobClient client = new GithubJobClient();
		List<Item> items = new ArrayList<>();
		for (String interest : interests) {
			// get search result for each interest
			List<Item> itemList = client.search(interest, "", "");
			// add all search results to items;
			items.addAll(itemList);
		}
		// shuffle the items
		Collections.shuffle(items);
		
		return items;
	}
}
