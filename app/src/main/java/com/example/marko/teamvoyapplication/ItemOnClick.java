package com.example.marko.teamvoyapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import java.io.Serializable;

/**
 * Created by Marko on 6/3/2016.
 */
public class ItemOnClick implements View.OnClickListener {
    Context context;
    Recipe recipe;

    public ItemOnClick(Context context, Recipe recipe) {
        this.context = context;
        this.recipe = recipe;
    }


    @Override
    public void onClick(View v) {
        Intent intent=new Intent(context,DetailsActivity.class);
        Bundle bundle=new Bundle();
        String recipeId=recipe.getRecipe_id();
        bundle.putString("recipe_id",recipeId );
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
