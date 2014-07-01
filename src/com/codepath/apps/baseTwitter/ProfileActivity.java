package com.codepath.apps.baseTwitter;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.baseTwitter.fragments.CurrentUserTimelineFragment;
import com.codepath.apps.baseTwitter.fragments.UserTimelineFragment;
import com.codepath.apps.baseTwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;


public class ProfileActivity extends FragmentActivity {
	TwitterRestClient client = null;

	TextView tvUserFullName;
	TextView tvUserTag;
	ImageView ivUserImage;
	TextView tvUserFollowers;
	TextView tvUserFollowing;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.profile_layout);
		client = TwitterApplication.getRestClient();
		setupViews();
		Intent intent = getIntent();
		String screen_name = intent.getStringExtra("screen_name");
		if(screen_name != null) {
			RequestParams params = new RequestParams();
			params.put("screen_name", screen_name);
			loadProfileInfo(params);
			loadUserFrame(screen_name);
		} else {
			loadCurrentUserProfileInfo();
			loadCurrentUserFrame();
		}


	}

	private void loadUserFrame(String screenName) {
		// Begin the transaction
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Replace the container with the new fragment
		ft.replace(R.id.flUserTimeline, UserTimelineFragment.getInstance(screenName));
		// or ft.add(R.id.your_placeholder, new FooFragment());
		// Execute the changes specified
		ft.commit();
	}

	private void loadCurrentUserFrame() {
		// Begin the transaction
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		// Replace the container with the new fragment
		ft.replace(R.id.flUserTimeline, new CurrentUserTimelineFragment());
		// or ft.add(R.id.your_placeholder, new FooFragment());
		// Execute the changes specified
		ft.commit();
	}

	private void loadProfileInfo(RequestParams params) {
		client.getUserInfo(params, new JsonHttpResponseHandler() {			
			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
				Log.d("DEBUG", arg1.toString());
			}

			@Override
			public void onSuccess(int arg0, JSONObject response) {
				super.onSuccess(arg0, response);
				Log.d("DEBUG", response.toString());
				User user = User.fromJson(response);
				if (user != null) {
					getActionBar().setTitle("@" + user.getScreenName());
					populateUserData(user);
				}
			}

			private void populateUserData(User user) {
				tvUserTag.setTextSize((float) 12.0);
				tvUserTag.setTextColor(Color.GRAY);
				tvUserFullName.setTypeface(null, Typeface.BOLD);
				tvUserFullName.setTextSize((float) 14.0);
				tvUserFollowers.setTextSize((float) 12.0);
				tvUserFollowers.setTextColor(Color.GRAY);
				tvUserFollowing.setTextSize((float) 12.0);
				tvUserFollowing.setTextColor(Color.GRAY);

				tvUserFullName.setText(user.getUserName());
				tvUserTag.setText(user.getTagLine());
				ImageLoader loader = ImageLoader.getInstance();
				loader.displayImage(user.getProfileImage(), ivUserImage);
				tvUserFollowers.setText(String.valueOf(user.getFollowers()) + " followers");
				tvUserFollowing.setText(String.valueOf(user.getFollowing()) + " following");
			}
		});
	}

	private void setupViews() {
		tvUserFullName = (TextView) findViewById(R.id.tvUserFullName);
		tvUserTag = (TextView) findViewById(R.id.tvUserTag);
		ivUserImage = (ImageView) findViewById(R.id.ivUserImage);
		tvUserFollowers = (TextView) findViewById(R.id.tvUserFollowers);
		tvUserFollowing = (TextView) findViewById(R.id.tvUserFollowing);
	}

	private void loadCurrentUserProfileInfo() {
		client.getCurrentUserInfo(new JsonHttpResponseHandler() {			
			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
				Log.d("DEBUG", arg1.toString());
			}

			@Override
			public void onSuccess(int arg0, JSONObject response) {
				super.onSuccess(arg0, response);
				Log.d("DEBUG", response.toString());
				User user = User.fromJson(response);
				if (user != null) {
					getActionBar().setTitle("@" + user.getScreenName());
					populateUserData(user);
				}
			}

			private void populateUserData(User user) {
				tvUserTag.setTextSize((float) 12.0);
				tvUserTag.setTextColor(Color.GRAY);
				tvUserFullName.setTypeface(null, Typeface.BOLD);
				tvUserFullName.setTextSize((float) 14.0);
				tvUserFollowers.setTextSize((float) 12.0);
				tvUserFollowers.setTextColor(Color.GRAY);
				tvUserFollowing.setTextSize((float) 12.0);
				tvUserFollowing.setTextColor(Color.GRAY);

				tvUserFullName.setText(user.getUserName());
				tvUserTag.setText(user.getTagLine());
				ImageLoader loader = ImageLoader.getInstance();
				loader.displayImage(user.getProfileImage(), ivUserImage);
				tvUserFollowers.setText(String.valueOf(user.getFollowers()) + " followers");
				tvUserFollowing.setText(String.valueOf(user.getFollowing()) + " following");
			}
		});
	}	
}
