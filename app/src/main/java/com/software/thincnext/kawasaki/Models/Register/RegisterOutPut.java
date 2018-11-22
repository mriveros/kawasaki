package com.software.thincnext.kawasaki.Models.Register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegisterOutPut {
    @SerializedName("Login")
    @Expose
    private List<Login> login = null;

    @SerializedName("OTP")
    @Expose
    private String otp;

    @SerializedName("OTPStatus")
    @Expose
    private String otpStatus;

    public String getOtpStatus() {
        return otpStatus;
    }

    public void setOtpStatus(String otpStatus) {
        this.otpStatus = otpStatus;
    }
}
