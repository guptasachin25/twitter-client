package com.codepath.apps.baseTwitter;

import com.codepath.apps.baseTwitter.fragments.HomeTimelineFragment;
import com.codepath.apps.baseTwitter.fragments.MentionsFragment;
import com.codepath.apps.baseTwitter.listeners.FragmentTabListener;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class HomeTimelineActivity extends FragmentActivity {
	public static Long CURRENT_MIN_TWEET_ID = Long.MAX_VALUE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_timeline);
		CURRENT_MIN_TWEET_ID = Long.MAX_VALUE;
		setupNavigationTabs();
	}
	
	private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
		    .newTab()
		    .setText("Home")
		    .setIcon(R.drawable.ic_home)
		    .setTag("HomeTimelineFragment")
		    .setTabListener(new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this,
                        "first", HomeTimelineFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
		    .newTab()
		    .setText("Mentions")
		    .setIcon(R.drawable.ic_mentions)
		    .setTag("MentionsFragment")
		    .setTabListener(new FragmentTabListener<MentionsFragment>(R.id.flContainer, this,
                        "second", MentionsFragment.class));
		actionBar.addTab(tab2);
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
	
	public void onProfileView(MenuItem mi) {
		Intent intent = new Intent(this, ProfileActivity.class);
		startActivity(intent);
	}
	
	public void onImageClick(View v) {
		Intent intent = new Intent(this, ProfileActivity.class);
		intent.putExtra("screen_name", v.getTag().toString());
		startActivity(intent);
	}
}
