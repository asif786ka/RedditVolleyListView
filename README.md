# RedditVolleyListView
Volley example to download data and show in listview.

This example shows how to use Volley as a networking library to parse json data and show in listview.
The approach first attempts to fetch data from cache,if present data is loaded from cache.If data is not present in cache,JSONObject request is made.
Volley populates the parsed response on the UI thread.

Example works for oreintation changes and shows how Volley is an efficient library compared to async tasks and its limitations during orientation changes.
NetworkImageView from volley instead of the normal imageview is used for loading the images.



