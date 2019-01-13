package com.example.pc.gallerytest;


import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

public class GlobalVariables extends Application {

    //Folders

    private List<FolderInfo> folderList = new ArrayList<FolderInfo>();

    public List<FolderInfo> getFolderList() {
        return folderList;
    }

    public void parseFolder() {
        getFolderList().clear();
        try {
            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED };
            Cursor cursorExternal = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    , projection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
                    null, null);

            while (cursorExternal.moveToNext()) {
                int file_ColumnIndex = cursorExternal.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String album  = cursorExternal.getString(cursorExternal.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String countPhoto = getCount(getApplicationContext(), album);
                String firstImagePath =  cursorExternal.getString(file_ColumnIndex);
                String path = cursorExternal.getString(cursorExternal.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)).
                        split(album,0)[0] + album + "/";

                FolderInfo folderInfo = new FolderInfo();
                folderInfo.setPath(path);
                folderInfo.setName(album);
                folderInfo.setCount(Integer.parseInt( countPhoto.split(" ",0)[0]));
                folderInfo.setFirstImagePath(getFirstImage(album));
                getFolderList().add(folderInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private String getFirstImage(String albumName) {
        String path = "";
        Uri uriExternal = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED };
        Cursor cursorExternal = getContentResolver().query(uriExternal, projection,
                "bucket_display_name = \""+albumName+"\"", null, null);
        int i=0;
        while (cursorExternal.moveToNext() && i<1) {
            int file_ColumnIndex = cursorExternal.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            path = cursorExternal.getString(file_ColumnIndex);
        }
        return path;
    }

    private String getCount(Context c, String albumName)
    {
        Uri uriExternal = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED };
        Cursor cursorExternal = c.getContentResolver().query(uriExternal, projection,
                "bucket_display_name = \""+albumName+"\"", null, null);
        return cursorExternal.getCount()+" Photos";
    }


    class SortByName implements Comparator<FolderInfo>
    {
        public int compare(FolderInfo a, FolderInfo b)
        {
            return a.getName().compareTo(b.getName());
        }
    }

    class SortByCountPhoto implements Comparator<FolderInfo>
    {
        public int compare(FolderInfo a, FolderInfo b)
        {
            return a.getCount().compareTo(b.getCount());
        }
    }

    private String viewAlbum;

    public void setViewAlbum(String viewAlbum) {
        this.viewAlbum = viewAlbum;
    }

    public String getViewAlbum() {
        return viewAlbum;
    }

    private String sortAlbum;

    public String getSortAlbum() {
        return sortAlbum;
    }

    public void setSortAlbum(String sortAlbum) {
        this.sortAlbum = sortAlbum;
    }

    //Files

    private List<MediaFileInfo> mediaList = new ArrayList<MediaFileInfo>();

    public List<MediaFileInfo> getMediaList() {
        return mediaList;
    }


    public void getAlbumImage(String album_name) {
        getMediaList().clear();
        Uri uriExternal = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED };
        Cursor cursorExternal = getContentResolver().query(uriExternal, projection,
                "bucket_display_name = \""+album_name+"\"", null, null);

        while (cursorExternal.moveToNext()) {
            int file_ColumnIndex = cursorExternal.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String path = cursorExternal.getString(file_ColumnIndex);
            String fileName = path.substring(path.lastIndexOf("/") + 1, path.length());

            MediaFileInfo mediaFileInfo = new MediaFileInfo();
            mediaFileInfo.setFilePath(path);
            mediaFileInfo.setFileName(fileName);

            double fileSize = 0.0;
            File file = new File(Uri.parse(path).getPath());
            fileSize = Math.round(((double) file.length() / 1024 * 100.0)) / 100.0;
            mediaFileInfo.setFileSize(fileSize);
            getMediaList().add(mediaFileInfo);
        }
    }


    class SortByNameFile implements Comparator<MediaFileInfo>
    {
        public int compare(MediaFileInfo a, MediaFileInfo b)
        {
            return a.getFileName().compareTo(b.getFileName());
        }
    }

    class SortBySizeFile implements Comparator<MediaFileInfo>
    {
        public int compare(MediaFileInfo a, MediaFileInfo b)
        {
            return a.getFileSize().compareTo(b.getFileSize());
        }
    }

    public String getViewFiles() {
        return viewFiles;
    }

    public void setViewFiles(String viewFiles) {
        this.viewFiles = viewFiles;
    }

    private String viewFiles;

    private String sortFiles;

    public String getSortFiles() {
        return sortFiles;
    }

    public void setSortFiles(String sortFiles) {
        this.sortFiles = sortFiles;
    }

    private boolean selectMode;

    public boolean getSelectMode() {
        return selectMode;
    }

    public void setSelectMode(boolean selectMode) {
        this.selectMode = selectMode;
    }

    private void deleteAndScanFile(final Context context, String path,
                                   final File fi, final boolean delete) {
        try {
            //сканирует только директорию
            MediaScannerConnection.scanFile(context, new String[] {
                            path}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            if(delete) {
                                if (uri != null) {
                                    context.getContentResolver().delete(uri, null,
                                            null);
                                }
                                fi.delete();
                            }
                            System.out.println("file Deleted :" + fi.getPath());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        //finish();
    }

    private void scanExternal(final Context context) {
        try {
            //сканирует встроенную
            MediaScannerConnection.scanFile(context, new String[] { Environment
                            .getExternalStorageDirectory().toString()
                    }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String dir = "";

    public void getFiles(String _dir) {
        dir = _dir;
        new LoadListTask().execute("http://192.168.43.97:8081/api/1.0/files/");
    }

    public class LoadListTask extends AsyncTask<String,Void,ArrayList<String>> {
        public static final String BASE_URL = "http://192.168.43.97:8081/api/1.0/files/";
        @Override
        protected ArrayList<String> doInBackground(String... urls) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FilesApi filesApi = retrofit.create(FilesApi.class);
            Response<Message> result = null;
            try {
                result = filesApi.getListFiles().execute();
                return result.body().getList();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> list){
            super.onPostExecute(list);
            if(list != null) {
                for(int i=0;i<list.size();i++) {
                    new LoadFileTask().execute(list.get(i),dir);
                }
            }
        }
    }

    public class LoadFileTask extends AsyncTask<String,Void, String> {
        String saveDir = "";
        String name = "";

        @Override
        protected  String doInBackground(String... urls) {
            saveDir = urls[1];
            name = urls[0];
            String BASE_URL = "http://192.168.43.97:8081/api/1.0/files/get?name=" + name;
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(BASE_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }
                input = connection.getInputStream();
                output = new FileOutputStream(dir + name );
                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    output.write(data, 0, count);
                }
                scanExternal(GlobalVariables.this);
            } catch (Exception e) {
                return e.toString();
            } finally {
                scanExternal(GlobalVariables.this);
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }
                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
    }

    public void setFiles() {
        for (MediaFileInfo m: getMediaList()) {
            if(m.getFileSelect()) {
                File source = new File(m.getFilePath());
                try {
                    File file = new File(m.getFilePath());
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("file",
                            file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                    new SetFileTask().execute(filePart);

                    deleteAndScanFile(this ,m.getFilePath(),source,false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                scanExternal(this);
            }
        }
    }

    public class SetFileTask extends AsyncTask<MultipartBody.Part,Void,Boolean> {
        public static final String BASE_URL = "http://192.168.43.97:8081/api/1.0/files/";

        @Override
        protected Boolean doInBackground(MultipartBody.Part... parts) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            FilesApi filesApi = retrofit.create(FilesApi.class);
            Response<MessageSet> result = null;
            try {
                result = filesApi.setFile(parts[0]).execute();
                if(result.body() != null) {
                    return result.body().isResultSet();
                } else {
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
