package com.example.marko.teamvoyapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;

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
 * Created by Marko on 6/5/2016.
 */
public class RecipesServiсe {

    List<Recipe> recipeList;

    public List<Recipe> getRecipeList(String url, int count) throws JSONException {
        JSONArray jsonArray = null;
        recipeList = new ArrayList<>();
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        HttpResponse response;
        try {
            response = client.execute(request);

            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            String json = reader.readLine();

            JSONObject mainJsonObject = new JSONObject(json);
            jsonArray = mainJsonObject.getJSONArray("recipes");

            for (int i = 0; i < jsonArray.length() && i < count; i++) {

                Recipe recipe = new Recipe();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                recipe.setPublisher(Html.fromHtml(jsonObject.getString("publisher")).toString());
                recipe.setSocial_rank(Html.fromHtml(jsonObject.getString("social_rank")).toString());
                recipe.setF2f_url(Html.fromHtml(jsonObject.getString("f2f_url")).toString());
                recipe.setTitle(Html.fromHtml(jsonObject.getString("title")).toString());
                recipe.setSource_url(Html.fromHtml(jsonObject.getString("source_url")).toString());
                recipe.setRecipe_id(Html.fromHtml(jsonObject.getString("recipe_id")).toString());
                recipe.setImage_url(Html.fromHtml(jsonObject.getString("image_url")).toString());
                recipe.setPublisher_url(Html.fromHtml(jsonObject.getString("publisher_url")).toString());
                try {

                    InputStream in = new java.net.URL(recipe.getImage_url()).openStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(in);
                    recipe.setImage(bitmap);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                recipeList.add(recipe);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return recipeList;
    }
}
