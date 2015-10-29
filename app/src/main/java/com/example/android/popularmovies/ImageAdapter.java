package com.example.android.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private String[] images;

    public ImageAdapter(Context c) {
        this.context = c;
        images = new String[]{"http://weknowyourdreams.com/images/flowers/flowers-02.jpg",
                "https://tpc.googlesyndication.com/simgad/11674298433767998486",
                "http://www.hdwallpapersimages.com/wp-content/uploads/images/Cute-Blue-Eyes-Puppy-Images.jpg"};
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if(convertView==null) {
             imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
            Picasso.with(context).load(images[position]).into(imageView);
        }
        else
        {
             imageView = (ImageView) convertView;
            Picasso.with(context).load(images[position]).into(imageView);
        }
            return imageView;
    }
}