package com.example.pc.gallerytest;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AlbumAdapter extends BaseAdapter {
    private List<FolderInfo> folderList;
    private Context mContext;
    private String mView;

    public AlbumAdapter(Context c,String view, List<FolderInfo> list) {
        folderList = list;
        mContext = c;
        mView = view;
    }

    @Override
    public int getCount() {
        return folderList.size();
    }

    @Override
    public Object getItem(int position) {
        return folderList.get(position);
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
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            grid = inflater.inflate(R.layout.cellgrid, parent, false);

        } else {
            grid = (View) convertView;
        }

        imageView = (ImageView) grid.findViewById(R.id.imagepart);
        textView = (TextView) grid.findViewById(R.id.textpart);
        textView2 = (TextView) grid.findViewById(R.id.textpart1);

        Glide.with(mContext)
                .load(folderList.get(position).getFirstImagePath())
                .into(imageView);

        imageView.setLayoutParams( new RelativeLayout.LayoutParams(145, 145));
        textView.setText(folderList.get(position).getName());

        if(mView == "grid"){
            RelativeLayout.LayoutParams  lay = new RelativeLayout.LayoutParams(145, 25);
            lay.setMargins(0,120,0,0);
            textView.setLayoutParams(lay );
        } else {
            RelativeLayout.LayoutParams  lay = new RelativeLayout.LayoutParams(250, 40);
            lay.setMargins(170,30,0,0);
            textView.setLayoutParams(lay );
            textView.setBackground(null);
            textView.setTextSize(20);
            textView.setGravity(Gravity.LEFT);
            textView2.setText(folderList.get(position).getCount() + " Photos");
            lay = new RelativeLayout.LayoutParams(250, 40);
            lay.setMargins(170,70,0,0);
            textView2.setLayoutParams(lay );
        }
        return grid;
    }
}
