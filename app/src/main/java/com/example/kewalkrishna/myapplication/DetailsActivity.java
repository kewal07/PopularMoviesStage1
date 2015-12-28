package com.example.kewalkrishna.myapplication;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView titleView,userRatingView,plotView,releaseDateView;
        ImageView imageView;

        String title = getIntent().getStringExtra("title");
        String image = getIntent().getStringExtra("image");
        String plot = getIntent().getStringExtra("plot");
        String userRating = getIntent().getStringExtra("userRating");
        String releaseDate = getIntent().getStringExtra("releaseDate");
        titleView = (TextView) findViewById(R.id.title);
        imageView = (ImageView) findViewById(R.id.image);
        userRatingView = (TextView) findViewById(R.id.userRating);
        plotView = (TextView) findViewById(R.id.plot);
        releaseDateView = (TextView) findViewById(R.id.releaseDate);


        titleView.setText(Html.fromHtml("<br/><br/>"+title));
        userRatingView.setText(Html.fromHtml("User Rating :"+userRating+"/10"));
        releaseDateView.setText(Html.fromHtml("Release Date : "+releaseDate));
        plotView.setText(Html.fromHtml(" Summary: \n "+plot));
        Picasso.with(this).load(image).into(imageView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
