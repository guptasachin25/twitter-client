package com.codepath.apps.baseTwitter.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.baseTwitter.R;
import com.codepath.apps.baseTwitter.models.Tweet;
import com.codepath.apps.baseTwitter.models.User;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {
	public TweetArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, R.layout.tweet_layout, tweets);
	}

	private static class ViewHolder {
		TextView username;
		TextView handle;
		TextView body;
		TextView timestamp;
		ImageView profile_image;
		ImageView replyImage;
		ImageView retweetImage;
		ImageView favImage;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet tweet = this.getItem(position);
		ViewHolder viewHolder;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater
					.inflate(R.layout.tweet_layout, parent, false);

			viewHolder.username = (TextView) convertView
					.findViewById(R.id.tvUserName);

			viewHolder.handle = (TextView) convertView
					.findViewById(R.id.tvUserHandle);

			viewHolder.body = (TextView) convertView
					.findViewById(R.id.tvTweetBody);

			viewHolder.timestamp = (TextView) convertView
					.findViewById(R.id.tvTimestamp);

			viewHolder.profile_image = (ImageView) convertView
					.findViewById(R.id.ibUserImage);
			
			viewHolder.replyImage = 
					(ImageView) convertView.findViewById(R.id.ivReply);
			
			viewHolder.retweetImage = 
					(ImageView) convertView.findViewById(R.id.ivRetweet);
			
			viewHolder.favImage = 
					(ImageView) convertView.findViewById(R.id.ivFav);
				
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		// Formatting options
		viewHolder.profile_image.setImageResource(android.R.color.transparent);
		viewHolder.username.setTextSize((float) 14.0);
		viewHolder.handle.setTextSize((float) 11.0);
		viewHolder.handle.setTextColor(Color.GRAY);
		viewHolder.username.setTypeface(null, Typeface.BOLD);
		viewHolder.body.setTextSize((float)15.0);
		viewHolder.timestamp.setTextSize((float) 11.0);
		viewHolder.timestamp.setTextColor(Color.GRAY);
		
		User user = tweet.getUser();
		viewHolder.profile_image.setTag(user.getScreenName());
		viewHolder.replyImage.setTag(tweet);
		viewHolder.retweetImage.setTag(tweet);
		viewHolder.favImage.setTag(tweet);
		viewHolder.username.setText(user.getUserName());
		viewHolder.handle.setText("@" + user.getScreenName());
		viewHolder.body.setText(Html.fromHtml(tweet.getBody()));
		viewHolder.timestamp.setText(tweet.getTimeFromNow());
		ImageLoader loader = ImageLoader.getInstance();
		loader.displayImage(user.getProfileImage(),
				viewHolder.profile_image);
		
		if(tweet.isFavored()) {
			viewHolder.favImage.setImageResource(R.drawable.ic_fav_color);
		}
		
		return convertView;
	}
}
