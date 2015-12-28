package com.example.kewalkrishna.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends ActionBarActivity {

    String forecastJsonStr = null;
    GridView gv;
    String apiResult;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//         gv = (GridView) findViewById(R.id.grid_view);
//        gv.setOnScrollListener(new ScrollListener(this));
        FetchMovies movieTask = new FetchMovies();
        movieTask.execute("popularity.desc");
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_vote_average) {
            FetchMovies movieTask = new FetchMovies();
            movieTask.execute("vote_average.desc");
            return true;
        }
        if (id == R.id.action_popularity) {
            FetchMovies movieTask = new FetchMovies();
            movieTask.execute("popularity.desc");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public class FetchMovies extends AsyncTask<String, Void, String> {

        @Override
        protected String  doInBackground(String... params) {

            String queryParameter = params[0];

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;


            try {
                final String Movie_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                final String APPID_PARAM = "api_key";
                final String key =getResources().getString(R.string.api_key);
                final String q = "sort_by";
                final String query = queryParameter;

                Uri builtUri = Uri.parse(Movie_BASE_URL).buildUpon()
                        .appendQueryParameter(q, query)
                        .appendQueryParameter(APPID_PARAM, key)
                        .build();

                URL url = new URL(builtUri.toString());



                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();


            } catch (IOException e) {
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }

            try {
                return forecastJsonStr;
            } catch (Exception e) {;
                e.printStackTrace();
            }

            return null;
        }

        @Override

        protected void onPostExecute(String  result) {
            apiResult=result;
            gv = (GridView) findViewById(R.id.grid_view);
            gv.setAdapter(new GridViewAdapter(MainActivity.this, result));
            gv.setOnScrollListener(new ScrollListener(MainActivity.this));
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    String  poster_path =parent.getItemAtPosition(position).toString().split("185/")[1];
                    try{ final JSONObject movieJson = new JSONObject(apiResult);
                        JSONArray movieArray = movieJson.getJSONArray("results");
                        int index=0;
                        for(int i=0;i<movieArray.length();i++){
                            JSONObject movieItem = movieArray.getJSONObject(i);
                            if(movieItem.getString("poster_path").equals(poster_path)){
                                index = i;
                                break;
                            }
                        }
                        JSONObject movieDetails = movieArray.getJSONObject(index);
                        String title = movieDetails.getString("original_title");
                        String Movie_BASE_URL = "http://image.tmdb.org/t/p/w185/";
                        String image = Movie_BASE_URL.concat(movieDetails.getString("poster_path"));
                        String plot = movieDetails.getString("overview");
                        String userRating = movieDetails.getString("vote_average");
                        String releaseDate = movieDetails.getString("release_date");
                        Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("image", image);
                        intent.putExtra("plot", plot);
                        intent.putExtra("userRating", userRating);
                        intent.putExtra("releaseDate", releaseDate);
                        //Start details activity
                        startActivity(intent);
                    }catch(JSONException e){}
                }
            });
        }
    }
}
