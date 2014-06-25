package com.codepath.apps.baseTwitter.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.codepath.apps.baseTwitter.HomeTimelineActivity;

public class Tweet {
	String id;
	String username;
	String name;
	String body;
	Date timestamp;
	String userImage;
	String handle;

	SimpleDateFormat formatter = new SimpleDateFormat(
			"EEE MMM dd HH:mm:ss Z yyyy");

	public Tweet(String id, String username, String name, String body,
			String timestamp, String userImage, String handle) {
		this.username = username;
		this.name = name;
		this.body = body;
		try {
			this.timestamp = formatter.parse(timestamp);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.userImage = userImage;
		this.id = id;
		this.handle = handle;
	}

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

	public String getBody() {
		return body;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getHandle() {
		return handle;
	}

	public String getTimeFromNow() {
		Date currentDate = new Date();
		long seconds = (currentDate.getTime() - timestamp.getTime()) / 1000;
		if (seconds < 60L) {
			return String.valueOf(seconds) + 's';
		} else if (seconds < 3600) {
			return String.valueOf(seconds / 60) + 'm';
		} else if (seconds < 3600 * 24) {
			return String.valueOf(seconds / 3600) + 'h';
		} else {
			return String.valueOf(seconds / 86400) + 'd';
		}
	}

	public String toString() {
		return getBody();
	}

	public static List<Tweet> fromJsonArray(JSONArray response) {
		List<Tweet> tweets = new ArrayList<Tweet>();

		int length = response.length();
		for (int i = 0; i < length; i++) {
			try {
				JSONObject tweet = response.getJSONObject(i);
				String body = tweet.getString("text");
				String id = tweet.getString("id_str");
				String timestamp = tweet.getString("created_at");
				JSONObject userObject = tweet.getJSONObject("user");
				String username = userObject.getString("name");
				String profile_image_url = userObject
						.getString("profile_image_url");
				String handle = userObject.getString("screen_name");
				tweets.add(new Tweet(id, username, null, body, timestamp,
						profile_image_url, handle));

				// To keep for pagination.
				HomeTimelineActivity.CURRENT_MIN_TWEET_ID = Math.min(
						HomeTimelineActivity.CURRENT_MIN_TWEET_ID, tweet.getLong("id"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return tweets;
	}

	public String getUserProfileImage() {
		return userImage;
	}

}
