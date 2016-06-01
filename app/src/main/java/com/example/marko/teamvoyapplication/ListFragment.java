package com.example.marko.teamvoyapplication;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marko on 6/1/2016.
 */
public class ListFragment extends Fragment {

    ListView list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, null, false);
        list = (ListView) view.findViewById(R.id.receipes_list);
        Bundle bundle = getArguments();
        List<Recipe> recipeList = (List<Recipe>) bundle.getSerializable("recipesList");
        list.setAdapter(new ListAdapter(getActivity(), R.layout.receipes_list_item, recipeList));
        return view;
    }
}
