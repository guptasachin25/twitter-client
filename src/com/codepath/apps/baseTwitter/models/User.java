package com.codepath.apps.baseTwitter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	public String getUserName() {
		return userName;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public String getTagLine() {
		return tagLine;
	}

	public Integer getFollowers() {
		return followers;
	}

	public Integer getFollowing() {
		return following;
	}

	private String userName;
	private String screenName;
	private String profileImage;
	private String tagLine;
	private Integer followers;
	private Integer following;
	

	public User(String userName, String screenName, String tagLine, String profileImage, Integer followers, Integer following) {
		this.userName = userName;
		this.screenName = screenName;
		this.profileImage = profileImage;
		this.followers = followers;
		this.following = following;
		this.tagLine = tagLine;
	}
	
	public static User fromJson(JSONObject jsonResponse) {
		try {
			return new User(jsonResponse.getString("name"), 
					jsonResponse.getString("screen_name"),
					jsonResponse.getString("description"),
					jsonResponse.getString("profile_image_url"),
					jsonResponse.getInt("followers_count"),
					jsonResponse.getInt("friends_count"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
