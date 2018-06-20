package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position"; // constant used for tagging extras sent from the MainActivity.
    private static final int DEFAULT_POSITION = -1; // which means nothing came through along with the intent.

    private ImageView mSandwichImageView;
    private TextView mPlaceOfOriginTextView, mDescriptionTextView, mIngredientsTextView, mAkaTextView, mMainNameTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        instantiateWidgets(); // finds and instantiates all the UI elements.

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent, MainActivity couldn't pass the data along.
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details); //getting a hold of the static sandwich jsons.
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }


        populateUI(sandwich);

        //I applied your the reviewer's suggestion on adding a placeholder and an error image to the image fetch.
        //Thank you very much for pointing out to a better practice of using Picasso and Butterknife libraries!
        Picasso.with(this)
                .load(sandwich.getImage())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(mSandwichImageView);

        setTitle(sandwich.getMainName());
    }

    /**
     * Helper method to display an error message and exit the activity.
     */
    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        mMainNameTextView.setText(sandwich.getMainName());
        mDescriptionTextView.setText(sandwich.getDescription());

        //Some of the sandwiches don't have the place-of-origin data.
        if(sandwich.getPlaceOfOrigin().equals(""))
            mPlaceOfOriginTextView.setText("Unknown");
        else
            mPlaceOfOriginTextView.setText(sandwich.getPlaceOfOrigin());

        /*
         I took my reviewer's great advice on using TextUtils.join() method instead of looping through
         ingredients and alsoKnownAs lists. My code is much more concise and resource-wise economical this way, thanks!
        */

        List<String> ingredients = sandwich.getIngredients();
        if(ingredients.size()==0)
            mIngredientsTextView.setText("Unknown Ingredients");
        else
            mIngredientsTextView.setText(TextUtils.join(", ", ingredients));

        List<String> akas = sandwich.getAlsoKnownAs();
        if(akas.size()==0)
            mAkaTextView.setText("Not Applicable");
        else
        mAkaTextView.setText(TextUtils.join(", ", sandwich.getAlsoKnownAs()));

    }


    /**
     * Helper method for finding and instantiating the UI Widgets.
     * Purpose: Cleaning up the onCreate() a little bit.
     */
    private void instantiateWidgets(){
        mSandwichImageView = (ImageView) findViewById(R.id.image_iv);
        mAkaTextView = (TextView) findViewById(R.id.tv_also_known_as);
        mDescriptionTextView = (TextView) findViewById(R.id.tv_description);
        mIngredientsTextView = (TextView) findViewById(R.id.tv_ingredients);
        mPlaceOfOriginTextView = (TextView) findViewById(R.id.tv_origin);
        mMainNameTextView = (TextView)findViewById(R.id.tv_main_name);
     }
}
