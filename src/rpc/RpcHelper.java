package rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Item;
import entity.Item.ItemBuilder;

public class RpcHelper {
	// Writes a JSONArray to http response.
	public static void writeJsonArray(HttpServletRequest request, HttpServletResponse response, JSONArray array)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setContentType("application/json");
		response.getWriter().print(array);
	}

	// Writes a JSONObject to http response.
	public static void writeJsonObject(HttpServletRequest request, HttpServletResponse response, JSONObject obj)
			throws IOException {
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setContentType("application/json");
		response.getWriter().print(obj);

	}

	public static void protectEndpoint(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		JSONObject res = new JSONObject();
		response.setStatus(403);
		try {
			res.put("message", "you need login first");
			writeJsonObject(request, response, res);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// Parses a JSONObject from http request.
	public static JSONObject readJSONObject(HttpServletRequest request) {
		StringBuilder sBuilder = new StringBuilder();
		try (BufferedReader reader = request.getReader()) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				sBuilder.append(line);
			}
			return new JSONObject(sBuilder.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new JSONObject();
	}

	// add is_saved label to jobs
	public static JSONArray labeledJobs(List<Item> jobs, Set<String> savedJobs) {

		JSONArray array = new JSONArray();
		for (Item job : jobs) {
			JSONObject obj = job.toJSONObject();
			boolean isSaved = false;
			if (savedJobs.contains(job.getId())) {
				isSaved = true;
			}
			try {
				obj.put("is_saved", isSaved);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			array.put(obj);
		}
		return array;
	}

	public static Item parseSavedJob(JSONObject savedJob) throws JSONException {
		ItemBuilder builder = new ItemBuilder();
		builder.setId(savedJob.getString("item_id"));
		return builder.build();
	}

}
