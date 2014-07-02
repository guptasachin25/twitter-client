package com.codepath.apps.baseTwitter;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.baseTwitter.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ComposeActivity extends Activity {

	EditText etTweetArea;
	Button btnSubmit;
	TextView tvNumChars;
	TwitterRestClient client = null;
	public static String SCREEN_NAME = null;
	TextView tvLoginHandle;
	TextView tvLoginName;
	ImageView ivLoginImage;

	static Integer MAX_CHARS = 140;
	static Integer REMAINING_CHARS = 140;

	String replyScreenName = null;
	String replyTweeyId = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		setViews();
		client = TwitterApplication.getRestClient();
		getScreenName();
		
		Intent intent = getIntent();
		replyScreenName = intent.getStringExtra("screen_name");
		replyTweeyId = intent.getStringExtra("tweet_id");

		if(replyScreenName != null && replyTweeyId != null) {
			int replyScreenNameLength = replyScreenName.length();
			etTweetArea.setText("@" + replyScreenName + " ");
			etTweetArea.setSelection(replyScreenNameLength+2);
			int remainingChars = MAX_CHARS - replyScreenNameLength + 2;
			tvNumChars.setText(String.valueOf(Math.min(remainingChars, MAX_CHARS)));
		}		
		
		etTweetArea.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() > MAX_CHARS) {
					tvNumChars.setTextColor(Color.RED);
				} else {
					tvNumChars.setTextColor(Color.BLACK);
				}
				int remainingChars = MAX_CHARS - s.length();
				tvNumChars.setText(String.valueOf(Math.min(remainingChars, MAX_CHARS)));
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
	}

	private void getScreenName() {
		client.getCurrentUserInfo(new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
				System.out.println(arg1.toString());
			}

			@Override
			public void onSuccess(int arg0, JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0, response);
				populateUserView(User.fromJson(response));
			}

			private void populateUserView(User user) {
				if(user != null) {
					tvLoginHandle.setTextSize((float) 12.0);
					tvLoginHandle.setTextColor(Color.GRAY);
					tvLoginName.setTypeface(null, Typeface.BOLD);
					tvLoginName.setTextSize((float) 14.0);

					tvLoginHandle.setText("@" + user.getScreenName());
					tvLoginName.setText(user.getUserName());
					ImageLoader loader = ImageLoader.getInstance();
					loader.displayImage(user.getProfileImage(), ivLoginImage);

				}
			}
		});
	}

	private void setViews() {
		etTweetArea = (EditText) findViewById(R.id.etTweetArea);
		etTweetArea.setTextSize((float) 15.0);
		btnSubmit = (Button) findViewById(R.id.btnSubmitTweet);
		tvNumChars = (TextView) findViewById(R.id.tvNumChars);
		tvLoginHandle = (TextView) findViewById(R.id.tvLoginHandle);
		ivLoginImage = (ImageView) findViewById(R.id.ivLoginImage);
		tvLoginName = (TextView) findViewById(R.id.tvLoginName);
	}

	public void onSubmitTweet(View v) {
		RequestParams params = new RequestParams();
		params.put("status", etTweetArea.getText().toString());
		if(replyTweeyId != null) { 
			params.put("in_reply_to_status_id", replyTweeyId);
		}

		client.onSubmitTweet(params,
				new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable arg0, String arg1) {
				super.onFailure(arg0, arg1);
				Log.d("DEBUG", arg0.getMessage());
				Log.d("DEBUG", arg1);
				Intent intent = new Intent(getApplicationContext(),
						HomeTimelineActivity.class);
				startActivity(intent);
			}

			@Override
			public void onSuccess(String arg0) {
				super.onSuccess(arg0);
				Log.i("SUCCESS", arg0);
				Intent intent = new Intent(getApplicationContext(),
						HomeTimelineActivity.class);
				startActivity(intent);
			}

		});
	}
}
