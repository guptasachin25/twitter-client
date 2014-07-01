package com.codepath.apps.baseTwitter.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.baseTwitter.TwitterApplication;
import com.codepath.apps.baseTwitter.TwitterRestClient;
import com.codepath.apps.baseTwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CurrentUserTimelineFragment extends TweetFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		populateData(null);
	}

	TwitterRestClient client = null;

	@Override
	void populateData(RequestParams params) {
		client = TwitterApplication.getRestClient();
		if (client != null) {
			client.downloadCurrentUserTimeline(params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONArray response) {
					addAll(Tweet.fromJsonArray(response));
				}
				
				@Override
				public void onFailure(Throwable e, JSONArray errorResponse) {
					Log.d("DEBUG", errorResponse.toString());
				}
			});
		}
	}

}
