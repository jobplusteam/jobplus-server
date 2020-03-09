package entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Item {
	private String id;
	private String title;
	private String type;
	private String createdAt;
	private String company;
	private String location;
	private String url;
	private String companyUrl;
	private String description;
	private String howToApply;


	// Construct Item using ItemBuilder
	public Item(ItemBuilder builder) {
		this.id = builder.id;
		this.title = builder.title;
		this.type = builder.type;
		this.createdAt = builder.createdAt;
		this.company = builder.company;
		this.location = builder.location;
		this.url = builder.url;
		this.companyUrl = builder.companyUrl;
		this.description = builder.description;
		this.howToApply = builder.howToApply;


	}

	public String getHowToApply() {
		return howToApply;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public String getCompany() {
		return company;
	}

	public String getLocation() {
		return location;
	}

	public String getUrl() {
		return url;
	}

	public String getCompanyUrl() {
		return companyUrl;
	}

	public String getDescription() {
		return description;
	}

	// convert Item java object to JSONObject
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("id", id);
			obj.put("title", title);
			obj.put("type", type);
			obj.put("created_at", createdAt);
			obj.put("company", company);
			obj.put("location", location);
			obj.put("url", url);
			obj.put("company_url", companyUrl);
			obj.put("description", description);
			obj.put("how_to_apply", howToApply);


		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	// User ItemBuilder to build Item, builder pattern
	public static class ItemBuilder {
		private String id;
		private String title;
		private String type;
		private String createdAt;
		private String company;
		private String location;
		private String url;
		private String companyUrl;
		private String description;
		private String howToApply;


		public ItemBuilder setId(String id) {
			this.id = id;
			return this;
		}

		public ItemBuilder setTitle(String title) {
			this.title = title;
			return this;
		}

		public ItemBuilder setType(String type) {
			this.type = type;
			return this;
		}

		public ItemBuilder setCreatedAt(String createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public ItemBuilder setCompany(String company) {
			this.company = company;
			return this;
		}

		public ItemBuilder setLocation(String location) {
			this.location = location;
			return this;
		}

		public ItemBuilder setUrl(String url) {
			this.url = url;
			return this;
		}

		public ItemBuilder setCompanyUrl(String companyUrl) {
			this.companyUrl = companyUrl;
			return this;
		}

		public ItemBuilder setDescription(String description) {
			this.description = description;
			return this;
		}
		
		
		public void setHowToApply(String howToApply) {
			this.howToApply = howToApply;
		}

		public Item build() {
			return new Item(this);
		}

	}

}
