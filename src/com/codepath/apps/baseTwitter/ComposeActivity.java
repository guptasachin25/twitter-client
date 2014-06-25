package com.codepath.apps.baseTwitter;

import org.json.JSONException;
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
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose);
		setViews();
		client = TwitterApplication.getRestClient();
		getScreenName();		
		etTweetArea.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.length() > 140) {
					tvNumChars.setTextColor(Color.RED);
				} else {
					tvNumChars.setTextColor(Color.BLACK);
				}
				tvNumChars.setText(String.valueOf(MAX_CHARS - s.length()));
				// TODO Auto-generated method stub
				
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
				System.out.println(response.toString());
				try {
					/*Toast.makeText(getApplicationContext(), 
							response.getString("screen_name"), 
							Toast.LENGTH_LONG).show();*/
					ComposeActivity.SCREEN_NAME = response.getString("screen_name");
					tvLoginHandle.setTextSize((float) 12.0);
					tvLoginHandle.setTextColor(Color.GRAY);
					tvLoginName.setTypeface(null, Typeface.BOLD);
					tvLoginName.setTextSize((float) 14.0);

					tvLoginHandle.setText("@" + ComposeActivity.SCREEN_NAME);
					tvLoginName.setText(response.getString("name"));
					ImageLoader loader = ImageLoader.getInstance();
					loader.displayImage(response.getString("profile_image_url"),
							ivLoginImage);
				} catch (JSONException e) {
					e.printStackTrace();
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
		client.onSubmitTweet(etTweetArea.getText().toString(),
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
