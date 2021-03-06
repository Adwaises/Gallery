package com.example.pc.gallerytest;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.net.URI;
import java.util.List;
import com.bumptech.glide.Glide;


public class ImageAdapter extends BaseAdapter {
    private List<MediaFileInfo> mediaList;
    private Context mContext;
    private String mView;

    public ImageAdapter(Context c, String view, List<MediaFileInfo> list) {
        mediaList = list;
        mContext = c;
        mView = view;
    }

    @Override
    public int getCount() {
        return mediaList.size();
    }

    @Override
    public Object getItem(int position) {
        return mediaList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View grid = new View(mContext);
        ImageView imageView;
        TextView textView;
        TextView textView2;

        if (convertView == null) {
            grid = new View(mContext);
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            grid = inflater.inflate(R.layout.cellgrid, parent, false);
        } else {
            grid = (View) convertView;
        }

        imageView = (ImageView) grid.findViewById(R.id.imagepart);
        textView = (TextView) grid.findViewById(R.id.textpart);
        textView2 = (TextView) grid.findViewById(R.id.textpart1);

        Glide.with(mContext)
                .load(mediaList.get(position).getFilePath())
                .into(imageView);

        imageView.setLayoutParams(new RelativeLayout.LayoutParams(145, 145));
        if(mediaList.get(position).getFileSelect()) {
            imageView.setColorFilter( 0xff00ff00, PorterDuff.Mode.MULTIPLY );
        }
        textView.setBackground(null);

        if (mView == "grid") {
            RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(145, 25);
            lay.setMargins(0, 120, 0, 0);
            textView.setLayoutParams(lay);
        } else {
            RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(250, 60);
            lay.setMargins(170, 10, 0, 0);
            textView.setLayoutParams(lay);
            textView.setBackground(null);
            textView.setTextSize(15);
            textView.setGravity(Gravity.LEFT);
            textView.setText(mediaList.get(position).getFileName());
            textView2.setText(mediaList.get(position).getFileSize().toString() + " KB");
            lay = new RelativeLayout.LayoutParams(250, 40);
            lay.setMargins(170, 70, 0, 0);
            textView2.setLayoutParams(lay);
        }
        return grid;
    }
}