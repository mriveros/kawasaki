package com.software.thincnext.kawasaki.Services;

import com.software.thincnext.kawasaki.Models.Register.Otp.OtpOutPut;
import com.software.thincnext.kawasaki.Models.Register.RegisterOutPut;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {



    @POST("service.asmx/CustomerIdentifier")
    Call<RegisterOutPut> login (@Query("MobileNo") String mob_number);



    //send otp deatils
    @POST("Otp/sendOpt")
    Call<ResponseBody> sendOtp(@Query("sNumber") String mNumber);


    //verify otp
    @GET("Otp/verify")
    Call<OtpOutPut> verifyOtp(@Query("otp") String mOtp);


}
