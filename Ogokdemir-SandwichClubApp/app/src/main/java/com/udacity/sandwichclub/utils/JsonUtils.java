package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.MainActivity;
import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    /**
     *
     * @param json Name of the sandwich.
     * @return A populated Sandwich object which will be used in the DetailActivity to populate the UI.
     */

    public static Sandwich parseSandwichJson(String json) {

        //Seems like Sandwich object is just a container data type with getters and setters.
        Sandwich sandwich = new Sandwich();


        try {
            JSONObject jsonObject = new JSONObject(json); //raw json, unparsed.

            JSONObject nameObj = jsonObject.getJSONObject("name"); // contains mainName and alsoKnownAs(JSONArray)

            //Parsing the String type data from the json object. These will go into a sandwich object.
            String mainName = nameObj.getString("mainName"); //parsing the mainName.
            String placeOfOrigin = jsonObject.getString("placeOfOrigin");
            String description = jsonObject.getString("description");
            String imageURL = jsonObject.getString("image");

            //Parsing the "alsoKnownAs" JSONArray and storing the content in an ArrayList.
            JSONArray akaJSONArray = nameObj.getJSONArray("alsoKnownAs");

            ArrayList<String> alsoKnownAs = new ArrayList<String>();
            if (akaJSONArray.length() != 0) {

                for (int i = 0; i < akaJSONArray.length(); i++) {
                    alsoKnownAs.add(akaJSONArray.get(i).toString());
                }
            }

            //Parsing the "ingredients" JSONArray and storing its content in an ArrayList.

            JSONArray ingredientsJSONArray = jsonObject.getJSONArray("ingredients");
            ArrayList<String> ingredients = new ArrayList<String>();
            if (ingredientsJSONArray.length() != 0) {
                for (int i = 0; i < ingredientsJSONArray.length(); i++) {
                    ingredients.add(ingredientsJSONArray.get(i).toString());
                }
            }


            //Packing the parsed sandwich data into a "deliverable" Sandwich object.
            sandwich.setMainName(mainName);
            sandwich.setAlsoKnownAs(alsoKnownAs);
            sandwich.setIngredients(ingredients);
            sandwich.setDescription(description);
            sandwich.setImage(imageURL);
            sandwich.setPlaceOfOrigin(placeOfOrigin);

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "JSON string is malformed, parsing failed - object couldn't be created");
            return null;
        }

        return sandwich;

    }
}
