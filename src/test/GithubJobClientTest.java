package test;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import entity.Item;
import entity.Item.ItemBuilder;
import external.GithubJobClient;

public class GithubJobClientTest {

	@Test
	public void testGetItemObject() throws JSONException {

		Item item = new ItemBuilder().setId("one").setLocation("sf").setCompany("apple").setDescription("desc")
				.setTitle("full stack engineer").setType("full time").setCompanyUrl("apple.com").build();
		JSONObject obj = new JSONObject();
		obj.put("id", "one");
		obj.put("location", "sf");
		obj.put("company", "apple");
		obj.put("description", "desc");
		obj.put("title", "full stack engineer");
		obj.put("type", "full time");

		obj.put("company_url", "apple.com");

		GithubJobClient client = new GithubJobClient();

		Assert.assertEquals(item, client.getItemObject(obj));

	}

	@Test
	public void testGetItemList() throws JSONException {

		Item one = new ItemBuilder().setId("one").setLocation("sf").build();
		Item two = new ItemBuilder().setId("two").setLocation("la").build();
		JSONObject obj_1 = new JSONObject();
		JSONObject obj_2 = new JSONObject();
		obj_1.put("id", "one");
		obj_1.put("location", "sf");
		
		obj_2.put("id", "two");
		obj_2.put("location", "la");

		JSONArray array = new JSONArray();
		array.put(obj_1);
		array.put(obj_2);
		
		List<Item> itemList = new ArrayList<>();
		itemList.add(one);
		itemList.add(two);
		
		GithubJobClient client = new GithubJobClient();
		Assert.assertEquals(itemList, client.getItemList(array));

	}
}
