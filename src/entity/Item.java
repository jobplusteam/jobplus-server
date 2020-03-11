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

	@Override
	public String toString() {
		return "Item [id=" + id + ", title=" + title + ", type=" + type + ", createdAt=" + createdAt + ", company="
				+ company + ", location=" + location + ", url=" + url + ", companyUrl=" + companyUrl + ", description="
				+ description + ", howToApply=" + howToApply + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((company == null) ? 0 : company.hashCode());
		result = prime * result + ((companyUrl == null) ? 0 : companyUrl.hashCode());
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((howToApply == null) ? 0 : howToApply.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (company == null) {
			if (other.company != null)
				return false;
		} else if (!company.equals(other.company))
			return false;
		if (companyUrl == null) {
			if (other.companyUrl != null)
				return false;
		} else if (!companyUrl.equals(other.companyUrl))
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (howToApply == null) {
			if (other.howToApply != null)
				return false;
		} else if (!howToApply.equals(other.howToApply))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
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
