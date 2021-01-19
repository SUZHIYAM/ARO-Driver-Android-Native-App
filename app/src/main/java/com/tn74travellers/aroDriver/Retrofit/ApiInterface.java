package com.tn74travellers.aroDriver.Retrofit;


import com.tn74travellers.aroDriver.Models.FileUploadOutput;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @Multipart
    @POST("/api/trip-action")
    //@Headers({"Content-Type: application/json"})
    Call<FileUploadOutput> callFileUpload(@Header("Authorization") String authorization,
                                            @Part MultipartBody.Part pickup_img,
                                            @Part MultipartBody.Part drop_img,
                                            @Part("trip_id") RequestBody trip_id,
                                            @Part("driver_id") RequestBody driver_id,
                                            @Part("action_type") RequestBody action_type,
                                            @Part("pickup_km") RequestBody pickup_km,
                                            @Part("drop_km") RequestBody drop_km,
                                            @Part("halt") RequestBody halt,
                                            @Part("hill") RequestBody hill,
                                            @Part("toll") RequestBody toll,
                                            @Part("batta") RequestBody batta,
                                            @Part("penalty") RequestBody penalty,
                                            @Part("penalty_reason") RequestBody penalty_reason,
                                            @Part("closed_amount") RequestBody closed_amount);



//    trip_id
//    driver_id
//    action_type
//    pickup_km
//    drop_km
//    pickup_img
//    drop_img
//    halt
//    hill
//    toll
//    batta
//    penalty
//    penalty_reason
}

