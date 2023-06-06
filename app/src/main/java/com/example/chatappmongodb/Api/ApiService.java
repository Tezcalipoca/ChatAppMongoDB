package com.example.chatappmongodb.Api;


import com.example.chatappmongodb.Models.ApiResponse;
import com.example.chatappmongodb.Models.ApiResponseDelete;
import com.example.chatappmongodb.Models.ApiResponseMessage;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {


    /* Auth Api Service*/
    @POST("/signup")
    Call<Void> createNewUser(@Body HashMap<String, String> map);

    @POST("/signin")
    Call<ApiResponse>  loginApp(@Body HashMap<String, String> map);

    @GET("/getProfileById/{id}")
    Call<ApiResponse> getProfileById(@Path("id") String id);

    @GET("/getlistUser")
    Call<ApiResponse> getlistUser();

    @POST("/updateNameById")
    Call<ApiResponse> updateNameById(@Body HashMap<String, String> map);

    @POST("/updateStatusById")
    Call<ApiResponse> updateStatusById(@Body HashMap<String, String> map);

    @POST("/updateBackgroundById")
    Call<ApiResponse> updateBackgroundById(@Body HashMap<String, String> map);

    @POST("/updateAvatarById")
    Call<ApiResponse> updateAvatarById(@Body HashMap<String, String> map);
    /* Message Api Service*/

    @POST("/getMessage")
    Call<ApiResponseMessage> getMessage(@Body HashMap<String, String> map);

    @POST("/sendTextMessage")
    Call<ApiResponseMessage> sendTextMessage(@Body HashMap<String, String> map);

    @POST("/sendImageMessage")
    Call<ApiResponseMessage> sendImageMessage(@Body HashMap<String, String> map);

    /* Lấy*/
    @POST("/getAllConversations")
    Call<ApiResponseMessage> getAllConversations(@Body HashMap<String, String> map);

    /* Xóa tin nhắn giữa 2 người*/
    @DELETE("/deleteMessages/{sender_id}/{receiver_id}")
    Call<ApiResponseDelete> deleteMessages(@Path("sender_id") String sender_id,
                                           @Path("receiver_id") String receiver_id);
}
