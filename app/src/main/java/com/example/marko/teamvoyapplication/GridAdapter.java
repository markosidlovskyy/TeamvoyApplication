package com.example.marko.teamvoyapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Marko on 6/1/2016.
 */
public class GridAdapter extends ArrayAdapter<Recipe> {
    Context context;
    List<Recipe> recipeList;

    public GridAdapter(Context context, int resource, List<Recipe> recipeList) {
        super(context, resource);
        this.context = context;
        this.recipeList = recipeList;
    }

    @Override
    public int getCount() {
        return recipeList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.recipe_grid_item,parent,false);
        TextView title = (TextView) view.findViewById(R.id.recipe_title);
        TextView rating = (TextView) view.findViewById(R.id.recipe_rait);
        ImageView image = (ImageView) view.findViewById(R.id.recipe_img);

        title.setText(recipeList.get(position).getTitle());
        rating.setText(recipeList.get(position).getSocial_rank());
        image.setImageBitmap(recipeList.get(position).getImage());
        return view;
    }
}
