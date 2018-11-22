package com.software.thincnext.kawasaki.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.software.thincnext.kawasaki.R;
import com.software.thincnext.kawasaki.Services.API;
import com.software.thincnext.kawasaki.Services.ConnectionDetector;
import com.software.thincnext.kawasaki.Services.Constants;
import java.util.concurrent.TimeUnit;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {

    @BindView(R.id.tv_login_mobileNumberError)
    TextView mobileError;

    @BindView(R.id.et_login_mobileNumber)
    EditText mMobileNumber;

    private static final String TAG = Register.class.getSimpleName();
    private static OkHttpClient.Builder builder;


    //internet connection
    private ConnectionDetector connectionDetector;


    //Declaring progress dialog
    private ProgressDialog mProgress;

    @BindView(R.id.relative_layout)
    RelativeLayout parentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        //Initialising progress dialog
        mProgress = new ProgressDialog(this);

        connectionDetector = new ConnectionDetector(this);



        //Hiding Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //Hiding views
        mobileError.setVisibility(View.GONE);




    }

    private void checkInternetCondition() {

        // check internet connection
        boolean isInternetPresent = connectionDetector.isConnectingToInternet();

        if (isInternetPresent) {

            callOtpRandom();
        }
        else
        {
            Snackbar snackbar = Snackbar.make(parentLayout, "No internet connection!", Snackbar.LENGTH_LONG);

            snackbar.show();
        }
    }

    @OnClick({R.id.ll_login_continueClick})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_login_continueClick:

                if (validateDetails())
                {



                    checkInternetCondition();




                }
                break;
                }
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


    private void callOtpRandom() {


        showProgressDialog(getResources().getString(R.string.please_wait));

        String mobileNumber = mMobileNumber.getText().toString();


        builder = getHttpClient();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(builder.build()).build();
        API gi = retrofit.create(API.class);

        Call<ResponseBody> call = (Call<ResponseBody>) gi.sendOtp(mobileNumber);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Log.i(TAG, String.valueOf(response.body()));



                if (mProgress != null) {
                    mProgress.dismiss();
                }

                if (response.code() == 200) {

                    Toast.makeText(Register.this, "Otp Sent", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, OTPVerfication.class);
                    startActivity(intent);

                } else {

                    if (mProgress != null) {
                        mProgress.dismiss();
                    }

                    else {


                        if (mProgress != null) {
                            mProgress.dismiss();
                        }
                        Toast.makeText(Register.this, "Something went wrong! Error :" + response.code(), Toast.LENGTH_SHORT).show();


                    }

                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Register.this,"Something went wrong!  Try again",Toast.LENGTH_SHORT).show();



            }

        });



    }







    private boolean validateDetails() {
        boolean validFlag = true;
        //Reading input values
        String mobileNumber = mMobileNumber.getText().toString();

        if (mobileNumber.isEmpty())
        {
            mobileError.setText(R.string.enter_mobile_number);
            mobileError.setVisibility(View.VISIBLE);

            validFlag = false;
        }
        else
        {
            if (mobileNumber.length() > 10 || mobileNumber.length() >10)
            {
                mobileError.setText(R.string.enter_10_digits_mob_number);
                mobileError.setVisibility(View.VISIBLE);

                validFlag = false;
            }
            else
            {
                mobileError.setVisibility(View.GONE);
            }
        }

        return validFlag;





    }

    public OkHttpClient.Builder getHttpClient() {
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

    @Override
    public void onBackPressed() {

        finish();

    }
}
