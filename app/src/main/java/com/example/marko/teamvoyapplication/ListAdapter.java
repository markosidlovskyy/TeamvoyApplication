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
 * Created by Marko on 5/31/2016.
 */
public class ListAdapter extends ArrayAdapter<Recipe> implements View.OnClickListener {
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

        View row;
        if (convertView == null) {
            row = inflater.inflate(R.layout.receipes_list_item, parent, false);
        } else row = convertView;

        TextView title = (TextView) row.findViewById(R.id.recipe_title);
        TextView rating = (TextView) row.findViewById(R.id.recipe_rait);
        ImageView image = (ImageView) row.findViewById(R.id.recipe_img);

        title.setText(recipes.get(position).getTitle());
        float rank= Float.parseFloat(recipes.get(position).getSocial_rank());
        rating.setText(new DecimalFormat("###.##").format(rank));
        image.setImageBitmap(recipes.get(position).getImage());

        row.setOnClickListener(new ItemOnClick(context, recipes.get(position)));
        return row;
    }

    @Override
    public void onClick(View v) {

    }
}
