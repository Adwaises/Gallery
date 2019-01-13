package com.example.pc.gallerytest;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.Cursor;
import android.database.MergeCursor;
import android.provider.MediaStore;
import android.net.Uri;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    AlbumAdapter albumAdapter;
    GridView gridview;
    GlobalVariables global;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        global=((GlobalVariables)getApplicationContext());
        global.setViewAlbum("grid");
        global.setSortAlbum ( "sort1");
        setTitle("Галерея");
        global.parseFolder();

        Collections.sort(global.getFolderList(), global.new SortByName());
        gridview = (GridView) findViewById(R.id.GridView);
        albumAdapter = new AlbumAdapter(this,global.getViewAlbum(),global.getFolderList());
        gridview.setAdapter(albumAdapter);
        gridview.setOnItemClickListener(gridviewOnItemClickListener);
    }


    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            Intent i = new Intent(getApplicationContext(),
                    OpenFolderActivity.class);
            i.putExtra("id", position);
            startActivity(i);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort:
                if(global.getSortAlbum() == "sort1"){
                    item.setTitle("Сортировка: 2");
                    global.setSortAlbum("sort2");
                    Collections.sort(global.getFolderList(), global.new SortByCountPhoto());
                } else {
                    item.setTitle("Сортировка: 1");
                    global.setSortAlbum("sort1");
                    Collections.sort(global.getFolderList(), global.new SortByName());
                }
                gridview.setAdapter(albumAdapter);
                return true;
            case R.id.action_view:
                if(global.getViewAlbum() == "grid"){
                    item.setTitle("Вид: 2");
                    global.setViewAlbum ("nogrid");
                    GridView grid = (GridView) findViewById(R.id.GridView);
                    grid.setColumnWidth(300);
                } else {
                    item.setTitle("Вид: 1");
                    global.setViewAlbum ( "grid");
                    GridView grid = (GridView) findViewById(R.id.GridView);
                    grid.setColumnWidth(150);
                }
                albumAdapter = new AlbumAdapter(this,global.getViewAlbum(),global.getFolderList());
                gridview.setAdapter(albumAdapter);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
