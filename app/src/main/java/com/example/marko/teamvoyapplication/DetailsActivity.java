package com.example.marko.teamvoyapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marko on 6/2/2016.
 */
public class DetailsActivity extends Activity {
    String urlGet = "http://food2fork.com/api/get";
    String api_key;
    ImageView image;
    TextView title, rating, description, ingredientTxt, publisherTxt, publisherTitle;
    List<String> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        api_key = getString(R.string.api_key);
        ingredients = new ArrayList<String>();

        Bundle bundle = getIntent().getExtras();
        String recipeId = bundle.getString("recipe_id");

        image = (ImageView) findViewById(R.id.image);
        title = (TextView) findViewById(R.id.title_txt);
        rating = (TextView) findViewById(R.id.rank_txt);
        description = (TextView) findViewById(R.id.description_txt);
        ingredientTxt = (TextView) findViewById(R.id.ingredient);
        publisherTxt = (TextView) findViewById(R.id.publisher);
        publisherTitle=(TextView) findViewById(R.id.publisher_title);

        GetRecipeTask getRecipeTask = new GetRecipeTask();
        getRecipeTask.execute(urlGet + "?key=" + api_key + "&rId=" + recipeId);

    }


    class GetRecipeTask extends AsyncTask<String, String, Recipe> {

        private ProgressDialog dialog;

        public GetRecipeTask() {
            dialog = new ProgressDialog(DetailsActivity.this);
        }


        protected void onPreExecute() {
            this.dialog.setMessage("Please wait for request");
            this.dialog.show();
        }

        protected Recipe doInBackground(String... urls) {
            //  android.os.Debug.waitForDebugger();
            Recipe recipe = new Recipe();

            JSONArray jsonArray = null;
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(urls[0]);
            HttpResponse response;
            try {
                response = client.execute(request);

                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                String json = reader.readLine();

                JSONObject jsonObject = new JSONObject(json).getJSONObject("recipe");
                recipe.setPublisher(Html.fromHtml(jsonObject.getString("publisher")).toString());
                recipe.setTitle(Html.fromHtml(jsonObject.getString("title")).toString());
                recipe.setImage_url(Html.fromHtml(jsonObject.getString("image_url")).toString());
                recipe.setSource_url(Html.fromHtml(jsonObject.getString("source_url")).toString());
                recipe.setSocial_rank(Html.fromHtml(jsonObject.getString("social_rank")).toString());
                recipe.setPublisher_url(Html.fromHtml(jsonObject.getString("publisher_url")).toString());

                jsonArray = jsonObject.optJSONArray("ingredients");
                for (int i = 0; i < jsonArray.length(); i++) {
                    ingredients.add(jsonArray.get(i).toString());
                }

                try {

                    InputStream in = new java.net.URL(recipe.getImage_url()).openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    recipe.setImage(bitmap);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return recipe;


        }

        protected void onPostExecute(final Recipe recipe) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            image.setImageBitmap(recipe.getImage());
            title.setText(recipe.getTitle());
            rating.setText("rating: " + recipe.getSocial_rank());

            StringBuilder builder = new StringBuilder();
            for (String details : ingredients) {
                builder.append(details + "\n");
            }

            description.setText(builder.toString());
            ingredientTxt.setVisibility(View.VISIBLE);
            publisherTitle.setVisibility(View.VISIBLE);

            publisherTxt.setText(recipe.getPublisher());
            publisherTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(recipe.getSource_url());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }
    }


}
