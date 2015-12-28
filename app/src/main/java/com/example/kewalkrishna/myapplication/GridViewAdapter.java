package com.example.kewalkrishna.myapplication;

/**
 * Created by kewalkrishna on 26/12/15.
 */
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.widget.ImageView.ScaleType.CENTER_CROP;

final class GridViewAdapter extends BaseAdapter {
    private final Context context;

    private final List<String> urls = new ArrayList<String>();
    String Movie_BASE_URL =
            "http://image.tmdb.org/t/p/w185/";
    String [] posters ;
    public GridViewAdapter(Context context,String  result)  {
        this.context = context;

        try {
            JSONObject movieJson = new JSONObject(result);
            JSONArray movieArray = movieJson.getJSONArray("results");
            posters = new String[movieArray.length()];
            for(int i=0;i<movieArray.length();i++){
              JSONObject movie=movieArray.getJSONObject(i);
                String path = movie.getString("poster_path");
                posters[i]=Movie_BASE_URL.concat(path);
            }

        }catch(JSONException e){

        }

        Collections.addAll(urls, posters);


    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        SquaredImageView view = (SquaredImageView) convertView;
        if (view == null) {
            view = new SquaredImageView(context);
            view.setScaleType(CENTER_CROP);
        }

        // Get the image URL for the current position.
        String url = getItem(position);

        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(context) //
                .load(url) //
                .placeholder(R.drawable.placeholder) //
                .error(R.drawable.error) //
                .fit() //
                .tag(context) //
                .into(view);

        return view;
    }

    @Override public int getCount() {
        return urls.size();
    }

    @Override public String getItem(int position) {
        return urls.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

}
