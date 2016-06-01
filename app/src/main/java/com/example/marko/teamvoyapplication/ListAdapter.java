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
 * Created by Marko on 5/31/2016.
 */
public class ListAdapter extends ArrayAdapter<Recipe> {
    Context context;
    List<Recipe> recipes;

    public ListAdapter(Context context, int resource, List<Recipe> recipes) {
        super(context, resource);
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.receipes_list_item, parent, false);

        TextView title = (TextView) row.findViewById(R.id.recipe_title);
        TextView rating = (TextView) row.findViewById(R.id.recipe_rait);
        ImageView image = (ImageView) row.findViewById(R.id.recipe_img);

        title.setText(recipes.get(position).getTitle());
        rating.setText(recipes.get(position).getSocial_rank());
        image.setImageBitmap(recipes.get(position).getImage());

        return row;
    }
}
