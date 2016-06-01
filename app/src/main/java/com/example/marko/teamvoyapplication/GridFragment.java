package com.example.marko.teamvoyapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

/**
 * Created by Marko on 6/1/2016.
 */
public class GridFragment extends Fragment {

    GridAdapter gridAdapter;
    GridView gridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, null, false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        Bundle bundle = getArguments();
        List<Recipe> recipeList = (List<Recipe>) bundle.getSerializable("recipesList");
        gridView.setAdapter(new GridAdapter(getActivity(), R.layout.recipe_grid_item, recipeList));

        return view;
    }
}
