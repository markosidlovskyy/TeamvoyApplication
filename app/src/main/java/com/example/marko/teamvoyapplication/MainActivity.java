package com.example.marko.teamvoyapplication;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

    String url_search = "http://food2fork.com/api/search";
    String api_key;
    Spinner fragment_spinner, sort_spinner, count_spinner;
    ListFragment listFragment;
    GridFragment gridFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    TextView view_txt, sort_txt, count_txt;
    EditText search_edit_text;
    Button search_button;
    List<Recipe> recipeList;
    int count = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api_key = getString(R.string.api_key);

        view_txt = (TextView) findViewById(R.id.fragment_txt);
        sort_txt = (TextView) findViewById(R.id.sort_txt);
        count_txt = (TextView) findViewById(R.id.count_txt);

        search_edit_text = (EditText) findViewById(R.id.search_edit_text);
        search_button = (Button) findViewById(R.id.search_button);

        fragment_spinner = (Spinner) findViewById(R.id.fragment_spiner);
        sort_spinner = (Spinner) findViewById(R.id.sort_spiner);
        count_spinner = (Spinner) findViewById(R.id.count_spiner);

        ArrayAdapter<CharSequence> adapterFragment = ArrayAdapter.createFromResource(this,
                R.array.fragment_array, android.R.layout.simple_spinner_item);
        adapterFragment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragment_spinner.setAdapter(adapterFragment);

        fragment_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                    setListFragment(recipeList);
                else
                    setGridFragment(recipeList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<CharSequence> adapterSort = ArrayAdapter.createFromResource(this,
                R.array.sort_array, android.R.layout.simple_spinner_item);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort_spinner.setAdapter(adapterSort);

        ArrayAdapter<CharSequence> adapterCount = ArrayAdapter.createFromResource(this,
                R.array.count_array, android.R.layout.simple_spinner_item);
        adapterCount.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        count_spinner.setAdapter(adapterCount);
        count_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        count = 9;
                        break;
                    case 1:
                        count = 18;
                        break;
                    case 2:
                        count = 30;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask();
                String searchText = "", sort;
                if (sort_spinner.getSelectedItem().toString().equals("Rating"))
                    sort = "r";
                else
                    sort = "t";

                try {
                    searchText = URLEncoder.encode(String.valueOf(search_edit_text.getText()), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                retrieveFeedTask.execute(url_search + "?key=" + api_key + "&q=" + searchText + "&sort=" + sort);
            }
        });


    }


    class RetrieveFeedTask extends AsyncTask<String, Void, List<Recipe>> {


        protected List<Recipe> doInBackground(String... urls) {
            // android.os.Debug.waitForDebugger();
            recipeList = new ArrayList<>();
            JSONArray jsonArray = null;
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(urls[0]);
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
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return recipeList;
        }

        protected void onPostExecute(List<Recipe> recipes) {

            if (fragment_spinner.getSelectedItem().toString().equals("List"))
                setListFragment(recipes);
            else
                setGridFragment(recipes);
        }
    }

    private void setListFragment(List<Recipe> recipes) {
        if (recipes == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putSerializable("recipesList", (Serializable) recipes);

        fragmentManager = getFragmentManager();
        listFragment = new ListFragment();
        listFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, listFragment, "1");
        fragmentTransaction.commit();
    }

    private void setGridFragment(List<Recipe> recipes) {
        if (recipes == null)
            return;

        Bundle bundle = new Bundle();
        bundle.putSerializable("recipesList", (Serializable) recipes);

        fragmentManager = getFragmentManager();
        gridFragment = new GridFragment();
        gridFragment.setArguments(bundle);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, gridFragment, "1");
        fragmentTransaction.commit();
    }
}