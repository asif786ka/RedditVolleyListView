package com.reddit.codingexercise;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.reddit.codingexercise.adapter.RedditAdapter;
import com.reddit.codingexercise.app.RedditController;
import com.reddit.codingexercise.model.RedditModel;

public class RedditMainActivity extends Activity {
	// Log tag
	private static final String TAG = RedditMainActivity.class.getSimpleName();

	// Reddit json url
	private static final String REDDIT_URL = "http://www.reddit.com/r/pics/hot.json";

	private ProgressDialog pDialog;
	private List<RedditModel> redditList = new ArrayList<RedditModel>();
	private ListView listView;
	private RedditAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listView = (ListView) findViewById(R.id.list);
		adapter = new RedditAdapter(this, redditList);
		listView.setAdapter(adapter);

		showProgressDialog();

		// We first check for cached request
		Cache cache = RedditController.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(REDDIT_URL);
		if (entry != null) {
			// fetch the data from cache
			try {
				String data = new String(entry.data, "UTF-8");
				try {
					parseJsonFeed(new JSONObject(data));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		} else {

			// making fresh volley request and getting json
			JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
					REDDIT_URL, null, new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							hideProgressDialog();
							VolleyLog.d(TAG, "Response: " + response.toString());
							if (response != null) {
								parseJsonFeed(response);
								setProgressBarIndeterminateVisibility(false);
							}

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							hideProgressDialog();
							VolleyLog.d(TAG, "Error: " + error.getMessage());
						}

					});

			// Adding request to volley request queue
			RedditController.getInstance().addToRequestQueue(jsonReq);

		}
	}

	/**
	 * Parsing json reponse and passing the data to reddit adapter
	 * */
	private void parseJsonFeed(JSONObject response) {
		try {
			JSONObject redditObj = response.getJSONObject("data");
			JSONArray redditChildArray = redditObj.getJSONArray("children");

			for (int i = 0; i < redditChildArray.length(); i++) {
				JSONObject redditChildObj = (JSONObject) redditChildArray
						.get(i);

				RedditModel redditItem = new RedditModel();

				redditItem.setTitle(redditChildObj.getJSONObject("data")
						.getString("title"));

				// Image might be null sometimes
				String image = redditChildObj.getJSONObject("data").isNull(
						"thumbnail") ? null : redditChildObj.getJSONObject(
						"data").getString("thumbnail");
				redditItem.setThumbnailUrl(image);

				// Image might be null sometimes
				String largeImage = redditChildObj.getJSONObject("data")
						.isNull("preview") ? null : redditChildObj
						.getJSONObject("data").getJSONObject("preview")
						.getJSONArray("images").getJSONObject(0)
						.getJSONObject("source").getString("url");
				redditItem.setLargeUrl(largeImage);
				redditList.add(redditItem);
			}

			// notify data changes to reddit adapter
			adapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hideProgressDialog();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void showProgressDialog() {
		pDialog = new ProgressDialog(this);
		// Showing progress dialog before making http request
		pDialog.setMessage("Loading...");
		pDialog.show();
	}

	private void hideProgressDialog() {
		if (pDialog != null) {
			pDialog.dismiss();
			pDialog = null;
		}
	}
}
