package com.example.marko.teamvoyapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
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
        View view;

        if (convertView == null)
            view = inflater.inflate(R.layout.recipe_grid_item, parent, false);
        else view = convertView;

        TextView title = (TextView) view.findViewById(R.id.recipe_title);
        TextView rating = (TextView) view.findViewById(R.id.recipe_rait);
        ImageView image = (ImageView) view.findViewById(R.id.recipe_img);

        title.setText(recipeList.get(position).getTitle());
        float rank= Float.parseFloat(recipeList.get(position).getSocial_rank());
        rating.setText(new DecimalFormat("###.##").format(rank));
        image.setImageBitmap(recipeList.get(position).getImage());
        view.setOnClickListener(new ItemOnClick(context, recipeList.get(position)));

        return view;
    }
}
