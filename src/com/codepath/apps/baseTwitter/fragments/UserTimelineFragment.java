package com.codepath.apps.baseTwitter.fragments;

import org.json.JSONArray;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.baseTwitter.TwitterApplication;
import com.codepath.apps.baseTwitter.TwitterRestClient;
import com.codepath.apps.baseTwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class UserTimelineFragment extends TweetFragment {
	private String screenName = null;
	TwitterRestClient client = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		screenName = getArguments().getString("screen_name");
		populateData(null);
	}
	
	public static UserTimelineFragment getInstance(String screenName) {
		UserTimelineFragment fragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name", screenName);
        fragment.setArguments(args);
		return fragment;
	}


	@Override
	void populateData(RequestParams params) {
		if(params == null) {
			params = new RequestParams();
		}
		params.put("screen_name", screenName);
		
		client = TwitterApplication.getRestClient();
		if (client != null) {
			client.downloadUserTimeline(params, new JsonHttpResponseHandler() {
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
