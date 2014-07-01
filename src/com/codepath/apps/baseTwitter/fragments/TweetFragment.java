package com.codepath.apps.baseTwitter.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.apps.baseTwitter.HomeTimelineActivity;
import com.codepath.apps.baseTwitter.R;
import com.codepath.apps.baseTwitter.adapters.TweetArrayAdapter;
import com.codepath.apps.baseTwitter.listeners.EndlessScrollListener;
import com.codepath.apps.baseTwitter.models.Tweet;
import com.loopj.android.http.RequestParams;

public abstract class TweetFragment extends Fragment {
	private ArrayAdapter<Tweet> adapter;
	private List<Tweet> tweets;
	private ListView lvTweets;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tweets = new ArrayList<Tweet>();
		adapter = new TweetArrayAdapter(getActivity(), tweets);
		tweets.clear();
		adapter.clear();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.listview_fragment, container, false);
		lvTweets = (ListView) view.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(adapter);
		setOnScrollListener();
		//setImageProfileClickListener();
		return view;
	}
	
	private void setOnScrollListener() {
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				RequestParams params = new RequestParams();
				params.put("max_id",
						String.valueOf(HomeTimelineActivity.CURRENT_MIN_TWEET_ID));
				populateData(params);
			}
		});
	}
	
	public void addAll(List<Tweet> tweets) {
		adapter.addAll(tweets);
	}
	
	abstract void populateData(RequestParams params);
}
