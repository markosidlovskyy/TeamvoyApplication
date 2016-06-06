package com.example.marko.teamvoyapplication;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Marko on 5/31/2016.
 */
public class Recipe implements Parcelable {

    String social_rank, f2f_url, title, source_url, publisher, recipe_id, image_url, publisher_url;
    Bitmap image;

    Recipe() {

    }

    protected Recipe(Parcel in) {
        social_rank = in.readString();
        f2f_url = in.readString();
        title = in.readString();
        source_url = in.readString();
        publisher = in.readString();
        recipe_id = in.readString();
        image_url = in.readString();
        publisher_url = in.readString();
        image = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getF2f_url() {
        return f2f_url;
    }

    public void setF2f_url(String f2f_url) {
        this.f2f_url = f2f_url;
    }

    public String getSocial_rank() {
        return social_rank;
    }

    public void setSocial_rank(String social_rank) {
        this.social_rank = social_rank;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher_url() {
        return publisher_url;
    }

    public void setPublisher_url(String publisher_url) {
        this.publisher_url = publisher_url;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(social_rank);
        dest.writeString(f2f_url);
        dest.writeString(title);
        dest.writeString(source_url);
        dest.writeString(publisher);
        dest.writeString(recipe_id);
        dest.writeString(image_url);
        dest.writeString(publisher_url);
        dest.writeParcelable(image, flags);
    }
}
