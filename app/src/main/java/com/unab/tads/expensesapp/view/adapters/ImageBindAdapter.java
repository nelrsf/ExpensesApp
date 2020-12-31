package com.unab.tads.expensesapp.view.adapters;


import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

public class ImageBindAdapter {

    @BindingAdapter("image")
    public static void loadImage(ImageView iv, String url){
        if(!url.equals("")){
            Glide.with(iv.getContext()).load(url).into(iv);
        }
    }
}
