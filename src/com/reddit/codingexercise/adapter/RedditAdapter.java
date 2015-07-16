package com.reddit.codingexercise.adapter;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.reddit.codingexercise.R;
import com.reddit.codingexercise.app.RedditController;
import com.reddit.codingexercise.model.RedditModel;

public class RedditAdapter extends BaseAdapter {

	private NetworkImageView thumbNail;
	private RedditModel mReddit;
	private Activity activity;
	private LayoutInflater inflater;
	private List<RedditModel> redditItems;
	ImageLoader imageLoader = RedditController.getInstance().getImageLoader();

	public RedditAdapter(Activity activity, List<RedditModel> movieItems) {
		this.activity = activity;
		this.redditItems = movieItems;

	}

	@Override
	public int getCount() {
		return redditItems.size();
	}

	@Override
	public Object getItem(int location) {
		return redditItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_row, null);

		if (imageLoader == null)
			imageLoader = RedditController.getInstance().getImageLoader();
		thumbNail = (NetworkImageView) convertView.findViewById(R.id.thumbnail);
		TextView title = (TextView) convertView.findViewById(R.id.title);

		// getting reddit data for the row
		mReddit = redditItems.get(position);

		// thumbnail image
		thumbNail.setImageUrl(mReddit.getThumbnailUrl(), imageLoader);

		// title
		title.setText(mReddit.getTitle());

		thumbNail.setTag(new Integer(position));
		thumbNail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {

				LayoutInflater inflater = activity.getLayoutInflater();
				View view = inflater.inflate(R.layout.expandable_image, null);
				NetworkImageView imageView = (NetworkImageView) view
						.findViewById(R.id.imageView);

				imageView.setImageUrl(
						redditItems
								.get(Integer.parseInt(v.getTag().toString()))
								.getLargeUrl(), imageLoader);

				AlertDialog.Builder adb = new AlertDialog.Builder(activity);
				adb.setView(view);
				adb.show();

			}
		});
		return convertView;
	}

}