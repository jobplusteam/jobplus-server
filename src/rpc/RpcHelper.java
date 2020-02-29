package rpc;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Item;
import entity.Item.ItemBuilder;

public class RpcHelper {
	// Writes a JSONArray to http response.
	public static void writeJsonArray(HttpServletResponse response, JSONArray array) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
		response.getWriter().print(array);
	}

	// Writes a JSONObject to http response.
	public static void writeJsonObject(HttpServletResponse response, JSONObject obj) throws IOException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json");
		response.getWriter().print(obj);

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
	
	public static Item parseSavedJob (JSONObject savedJob) throws JSONException {
		ItemBuilder builder = new ItemBuilder();
		builder.setId(savedJob.getString("item_id"));	
		return builder.build();
	}

}
