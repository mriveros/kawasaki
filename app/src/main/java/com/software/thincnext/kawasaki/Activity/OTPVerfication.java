package com.software.thincnext.kawasaki.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.software.thincnext.kawasaki.Models.Register.Otp.OtpOutPut;
import com.software.thincnext.kawasaki.R;
import com.software.thincnext.kawasaki.Services.API;
import com.software.thincnext.kawasaki.Services.ConnectionDetector;
import com.software.thincnext.kawasaki.Services.Constants;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OTPVerfication extends AppCompatActivity {


    @BindView(R.id.tv_otpVerification_codeError)
    TextView mOtpError;

    @BindView(R.id.et_otpVerification_code)
    EditText codeText;

    //Sharedpreferences
    private SharedPreferences sharedPreferences;

    private static final String TAG = OTPVerfication.class.getSimpleName();
    private static OkHttpClient.Builder builder;


    //Declaring progress dialog
    private ProgressDialog mProgress;

    //internet connection
    private ConnectionDetector connectionDetector;

    @BindView(R.id.linear_layout)
    LinearLayout parentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverfication);

        ButterKnife.bind(this);


        //Initialising progress dialog
        mProgress = new ProgressDialog(this);

        // internet intialisation
        connectionDetector = new ConnectionDetector(this);


        //Initialising shared preferences
        sharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);


        //Hiding Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Hiding views
        mOtpError.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {

        finish();

    }

    @OnClick({R.id.ll_otpVerification_verifyClick})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_otpVerification_verifyClick:

                //Reading input values
                String verifyCode = codeText.getText().toString();

                //Validating text fields
                boolean isValid = validateTextFields(verifyCode);

                if (isValid) {


                   checkInternetConnection();



                }

                break;


        }


    }

    private void checkInternetConnection() {

        // check internet connection
        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            verifyOtp();
        }
        else
        {
            Snackbar snackbar = Snackbar.make(parentLayout, "No internet connection!", Snackbar.LENGTH_LONG);

            snackbar.show();
        }

    }

    private void verifyOtp() {

        showProgressDialog(getResources().getString(R.string.please_wait));
        String otp = codeText.getText().toString();

      //  Toast.makeText(OTPVerfication.this,otp,Toast.LENGTH_SHORT).show();

        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        API gi = retrofit.create(API.class);


        Call<OtpOutPut> call = (Call<OtpOutPut>) gi.verifyOtp(otp);
        call.enqueue(new Callback<OtpOutPut>() {
            @Override
            public void onResponse(Call<OtpOutPut> call, Response<OtpOutPut> response) {

                Log.i(TAG, String.valueOf(response.body()));
                OtpOutPut otpOutPut = response.body();

                if (mProgress != null) {
                    mProgress.dismiss();
                }

                //Checking for response code
                if (response.code() == 200) {




                    if (otpOutPut.getMessage().equalsIgnoreCase("Verified successyfully..")) {

                        Toast.makeText(OTPVerfication.this, otpOutPut.getMessage(), Toast.LENGTH_SHORT).show();

                        //Saving sharedpreference values
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(Constants.IS_LOGGED_IN, true);

                        editor.commit();

                        Intent homeIntent = new Intent(OTPVerfication.this, HomeActivity.class);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(homeIntent);


                    }

                    else {

                        if (mProgress != null) {
                            mProgress.dismiss();
                        } else {


                            if (mProgress != null) {
                                mProgress.dismiss();
                            }
                            Toast.makeText(OTPVerfication.this, "Something went wrong! Error :" + response.code(), Toast.LENGTH_SHORT).show();


                        }

                    }
                }
            }
            @Override
            public void onFailure(Call<OtpOutPut> call, Throwable t) {
                Toast.makeText(OTPVerfication.this,"Something went wrong!  Try again",Toast.LENGTH_SHORT).show();



            }

        });


    }

    private void showProgressDialog(String msg) {
        if (mProgress == null) {
            mProgress = new ProgressDialog(this);
        }
        mProgress.setMessage(msg);
        mProgress.setCancelable(false);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.show();
    }

    private OkHttpClient.Builder getHttpClient() {
        if (builder == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.addInterceptor(loggingInterceptor);
            client.writeTimeout(60000, TimeUnit.MILLISECONDS);
            client.readTimeout(60000, TimeUnit.MILLISECONDS);
            client.connectTimeout(60000, TimeUnit.MILLISECONDS);
            return client;
        }
        return builder;
    }

    private boolean validateTextFields(String verifyCode) {
        boolean validFlag = true;

        if (verifyCode.isEmpty())
        {
            mOtpError.setText(R.string.enter_4_digits);
            mOtpError.setVisibility(View.VISIBLE);

            validFlag = false;
        }
        else
        {
            if (verifyCode.length() <4 )
            {
                mOtpError.setText(R.string.enter_valid_otp);
                mOtpError.setVisibility(View.VISIBLE);

                validFlag = false;
            }
            else
            {
                mOtpError.setVisibility(View.GONE);
            }
        }

        return validFlag;

    }
}
