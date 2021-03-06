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
import android.util.DisplayMetrics;
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
import java.text.DecimalFormat;
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
    GetRecipeTask getRecipeTask;
    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        api_key = getString(R.string.api_key);
        ingredients = new ArrayList<String>();
        recipe = new Recipe();
        Bundle bundle = getIntent().getExtras();
        String recipeId = bundle.getString("recipe_id");

        image = (ImageView) findViewById(R.id.image);
        title = (TextView) findViewById(R.id.title_txt);
        rating = (TextView) findViewById(R.id.rank_txt);
        description = (TextView) findViewById(R.id.description_txt);
        ingredientTxt = (TextView) findViewById(R.id.ingredient);
        publisherTxt = (TextView) findViewById(R.id.publisher);
        publisherTitle = (TextView) findViewById(R.id.publisher_title);

        getRecipeTask = new GetRecipeTask();
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

                InputStream in = new java.net.URL(recipe.getImage_url()).openStream();
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                recipe.setImage(bitmap);

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }

            return recipe;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        protected void onPostExecute(final Recipe recipe) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            Bitmap bitmap = resizeBitMap(recipe.getImage(), image);

            image.setImageBitmap(recipe.getImage());
            title.setText(recipe.getTitle());
            float rank = Float.parseFloat(recipe.getSocial_rank());
            rating.setText("rating: " + new DecimalFormat("###.##").format(rank));

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

    private Bitmap resizeBitMap(Bitmap image, ImageView imageView) {

        float height = image.getHeight();
        float width = image.getWidth();
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int density = (int) displayMetrics.density;

        if (width / density < 150)
            return image;

        float d = width / height;
        android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = 150 * density;
        layoutParams.height = (int) ((150 * density) / d);

        imageView.setLayoutParams(layoutParams);

        return image;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (getRecipeTask != null)
            getRecipeTask.cancel(false);
    }


}
