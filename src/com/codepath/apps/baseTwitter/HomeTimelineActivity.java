package com.codepath.apps.baseTwitter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.baseTwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.baseTwitter.listeners.EndlessScrollListener;
import com.codepath.apps.baseTwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HomeTimelineActivity extends Activity {
	TwitterRestClient client = null;
	ArrayAdapter<Tweet> adapter;
	List<Tweet> tweets;
	public static Long CURRENT_MIN_TWEET_ID = Long.MAX_VALUE;

	ListView lvTweets;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_timeline);
		client = TwitterApplication.getRestClient();
		tweets = new ArrayList<Tweet>();
		tweets.clear();
		CURRENT_MIN_TWEET_ID = Long.MAX_VALUE;
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		adapter = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(adapter);
		adapter.clear();
		loadTweets(null);

		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				RequestParams params = new RequestParams();
				params.put("max_id",
						String.valueOf(HomeTimelineActivity.CURRENT_MIN_TWEET_ID));
				loadTweets(params);
			}
		});
	}

	/**
	 * Function to connect with Tweets API and download tweets.
	 */
	private void loadTweets(RequestParams params) {
		if (client != null) {
			Log.d("DEBUG", "Clients not null");
			client.downloadTweets(params, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONArray response) {
					//Toast.makeText(getApplicationContext(), "Got all tweets",
					//		Toast.LENGTH_SHORT).show();
					List<Tweet> twits = Tweet.fromJsonArray(response);
					adapter.addAll(twits);
					Log.d("DEBUG", response.toString());
				}

				@Override
				public void onFailure(Throwable e, JSONArray errorResponse) {
					//Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
					Log.d("DEBUG", errorResponse.toString());
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.compose_tweet, menu);
		return true;
	}

	public void onComposeAction(MenuItem mi) {
		Intent intent = new Intent(this, ComposeActivity.class);
		startActivity(intent);
	}
}
