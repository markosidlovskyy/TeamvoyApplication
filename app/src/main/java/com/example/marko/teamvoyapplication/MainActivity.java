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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
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
    RetrieveFeedTask retrieveFeedTask;
    RecipesServiсe recipesServiсe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrieveFeedTask = new RetrieveFeedTask();
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
                R.array.fragment_array, R.layout.spinner_item);
        adapterFragment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragment_spinner.setAdapter(adapterFragment);

        fragment_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    setListFragment(recipeList);
                else
                    setGridFragment(recipeList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapterSort = ArrayAdapter.createFromResource(this,
                R.array.sort_array, R.layout.spinner_item);
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort_spinner.setAdapter(adapterSort);

        ArrayAdapter<CharSequence> adapterCount = ArrayAdapter.createFromResource(this,
                R.array.count_array, R.layout.spinner_item);
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

                retrieveFeedTask = new RetrieveFeedTask();
                retrieveFeedTask.execute(url_search + "?key=" + api_key + "&q=" + searchText + "&sort=" + sort);
            }
        });

        retrieveFeedTask.execute(url_search + "?key=" + api_key);
    }

    class RetrieveFeedTask extends AsyncTask<String, String, List<Recipe>> {

        private ProgressDialog dialog;

        public RetrieveFeedTask() {
            dialog = new ProgressDialog(MainActivity.this);
        }

        protected void onPreExecute() {
            this.dialog.setMessage("Please wait for request");
            this.dialog.show();
        }

        protected List<Recipe> doInBackground(String... urls) {
            recipesServiсe = new RecipesServiсe();
            try {
                recipeList = recipesServiсe.getRecipeList(urls[0], count);
            } catch (JSONException e) {
                publishProgress("Server is not responding");
                return null;
            }

            return recipeList;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            showErrorDialog(values[0]);
        }

        protected void onPostExecute(List<Recipe> recipes) {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if (fragment_spinner.getSelectedItem().toString().equals("List"))
                setListFragment(recipes);
            else
                setGridFragment(recipes);
        }
    }

    private void showErrorDialog(final String massage) {
        runOnUiThread(new Runnable() {
            public void run() {
                final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(MainActivity.this).setTitle("Error")
                        .setMessage(massage);
                dialog.setNegativeButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                final android.app.AlertDialog alert = dialog.create();
                alert.show();
            }
        });
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
