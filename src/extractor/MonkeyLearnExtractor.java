package extractor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.monkeylearn.ExtraParam;
import com.monkeylearn.MonkeyLearn;
import com.monkeylearn.MonkeyLearnException;
import com.monkeylearn.MonkeyLearnResponse;

public class MonkeyLearnExtractor {
	private static final String API_KEY = "2e42c630d27db05387bed783197877527e4d7ffa";
	private static final String MODEL_ID = "ex_YCya9nrn";

	public JSONArray extract(String[] data) throws MonkeyLearnException {
		MonkeyLearn ml = new MonkeyLearn(API_KEY);
		ExtraParam[] extraParams = { new ExtraParam("max_keywords", "3") };
		MonkeyLearnResponse res = ml.extractors.extract(MODEL_ID, data, extraParams);

		System.out.println(res.arrayResult);

		return res.arrayResult;

	}

	public List<String> keywords(JSONArray jArray) {
		
		Map<String, Integer> allKeywords = new HashMap<>();
		
		for (Object arr: jArray) {
			JSONArray keyArray = (JSONArray)arr;
			for (Object obj: keyArray) {
				JSONObject key = (JSONObject)obj;
				String keyword = (String)key.get("keyword");
				allKeywords.put(keyword, allKeywords.getOrDefault(keyword, 0) + 1);
			}
		}
		List<Entry<String, Integer>> keywordList = new ArrayList<>(allKeywords.entrySet());
		Collections.sort(keywordList, (Entry<String, Integer> e1, Entry<String, Integer> e2) -> {
			return Integer.compare(e2.getValue(), e1.getValue());
		});
		List<String> res = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			res.add(keywordList.get(i).getKey());
		}
		
		return res;
	}

	public static void main(String[] args) {
		MonkeyLearnExtractor ml = new MonkeyLearnExtractor();
		String[] data = { "lifu yao",
				"Elon Musk has shared a photo of the spacesuit designed by SpaceX. This is the second image shared of the new design and the first to feature the spacesuit’s full-body look." };
		try {
			JSONArray jArray = ml.extract(data);
			List<String> res = ml.keywords(jArray);
			for (String s: res) {
				System.out.println(s);
			}
		} catch (MonkeyLearnException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}