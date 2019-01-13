package com.example.pc.gallerytest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

//import com.github.chrisbanes.photoview.PhotoView;

public class OpenFolderActivity  extends AppCompatActivity {
    int position;
    ImageAdapter imageAdapter;
    Intent intent;
    GridView gridview;
    GlobalVariables global;
    String dir = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        global=((GlobalVariables)getApplicationContext());
        intent = getIntent();
        position = intent.getExtras().getInt("id");
        global.getAlbumImage(global.getFolderList().get(position).getName());

        global.setSelectMode(false);
        global.setSortFiles( global.getSortAlbum());
        global.setViewFiles (global.getViewAlbum());
        Collections.sort(global.getMediaList(), global.new SortByNameFile());

        if(global.getViewFiles() == "nogrid"){
            GridView grid = (GridView) findViewById(R.id.GridViewAlbum);
            grid.setColumnWidth(300);
        } else {
            GridView grid = (GridView) findViewById(R.id.GridViewAlbum);
            grid.setColumnWidth(150);
        }

        setTitle(global.getFolderList().get(position).getName());
        dir = global.getFolderList().get(position).getPath();
        gridview = (GridView) findViewById(R.id.GridViewAlbum);
        imageAdapter = new ImageAdapter(this,global.getViewFiles(),global.getMediaList());
        gridview.setAdapter(imageAdapter);
        gridview.setOnItemClickListener(gridviewOnItemClickListener);

        gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                Toast.makeText(getApplicationContext(), "LONG PRESS " + position, Toast.LENGTH_SHORT).show();
                if(!global.getSelectMode()) {
                    global.setSelectMode(true);
                    global.getMediaList().get(position).setFileSelect(true);
                }else {
                    global.setSelectMode(false);
                    for (MediaFileInfo n: global.getMediaList()) {
                        n.setFileSelect(false);
                    }
                }
                gridview.setAdapter(imageAdapter);
                return true;
            }
        });
    }

    private GridView.OnItemClickListener gridviewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
             if(global.getSelectMode()) {
                if(!global.getMediaList().get(position).getFileSelect()){
                    global.getMediaList().get(position).setFileSelect(true);
                } else {
                    global.getMediaList().get(position).setFileSelect(false);
                }
                gridview.setAdapter(imageAdapter);
            } else {
                Intent i = new Intent(getApplicationContext(),
                        FullImageActivity.class);
                i.putExtra("id", position);
                startActivity(i);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filles, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_sort_files:
                if(global.getSortFiles() == "sort1"){
                    item.setTitle("Сортировка: 2");
                    global.setSortFiles("sort2");
                    Collections.sort(global.getMediaList(), global.new SortBySizeFile());
                } else {
                    item.setTitle("Сортировка: 1");
                    global.setSortFiles("sort1");
                    Collections.sort(global.getMediaList(), global.new SortByNameFile());
                }
                gridview.setAdapter(imageAdapter);
                return true;
            case R.id.action_view_files:
                if( global.getViewFiles() == "grid"){
                    item.setTitle("Вид: 2");
                    global.setViewFiles ( "nogrid");
                    GridView grid = (GridView) findViewById(R.id.GridViewAlbum);
                    grid.setColumnWidth(300);
                } else {
                    item.setTitle("Вид: 1");
                    global.setViewFiles (  "grid");
                    GridView grid = (GridView) findViewById(R.id.GridViewAlbum);
                    grid.setColumnWidth(150);
                }
                imageAdapter = new ImageAdapter(this,global.getViewFiles(),global.getMediaList());
                gridview.setAdapter(imageAdapter);
                return true;
            case R.id.action_out_files:
                global.setFiles();
                global.setSelectMode(false);
                finish();
                Toast.makeText(this,"Выгрузили", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_in_files:
                global.getFiles(dir);
                finish();
                Toast.makeText(this,"Загрузили", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
