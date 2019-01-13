package com.example.pc.gallerytest;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface FilesApi {
    @GET("getList")
    Call<Message> getListFiles();

    @Multipart
    @POST("set")
    Call<MessageSet> setFile(@Part MultipartBody.Part file);
}
