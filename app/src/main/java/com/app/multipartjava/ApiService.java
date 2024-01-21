package com.app.multipartjava;



import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public  interface ApiService {


    @Multipart
    @POST("UploadGallery")
    Call<String> uploadImage(
            @Part MultipartBody.Part[] part, // Image file part
            @Part("Gallery_Id") RequestBody Gallery_Id, // Other parts, if any
            @Part("ChannelPartnerReg_Id") RequestBody ChannelPartnerReg_Id, // Other parts, if any
            @Part("Caption") RequestBody Caption // Other parts, if any
    );
}