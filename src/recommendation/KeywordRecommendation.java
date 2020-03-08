package recommendation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.simple.JSONArray;

import com.monkeylearn.MonkeyLearnException;

import db.MySQLConnection;
import entity.Item;
import external.GithubJobClient;
import extractor.MonkeyLearnExtractor;

// Recommendation based on geo distance and similar categories.
public class KeywordRecommendation {

	public List<Item> recommendItems(String userId) throws MonkeyLearnException, UnsupportedEncodingException {

		// Step 1, get all saved job_id from db
		MySQLConnection connection = new MySQLConnection();
		Set<String> savedJobIds = connection.getSavedJobs(userId);
		connection.close();

		// Step 2, get all saved job_title from github api
		GithubJobClient client = new GithubJobClient();
		List<String> titles = new ArrayList<>();

		for (String savedId : savedJobIds) {
			Item job = client.getJobfromJobId(savedId);
			titles.add(job.getTitle());
		}

		// Step 3, extract 3 keywords of all texts from MonkeyLearn
		MonkeyLearnExtractor ml = new MonkeyLearnExtractor();
		String[] strArray = new String[titles.size()];
		for (int i = 0; i < titles.size(); i++) {
			strArray[i] = titles.get(i);
		}
		JSONArray jArray = ml.extract(strArray);
		List<String> strList = ml.keywords(jArray);
		StringBuilder desc = new StringBuilder();

		for (String s : strList) {
			desc.append(s + " ");
		}

		return client.search(desc.toString(), null, null);

	}
}
