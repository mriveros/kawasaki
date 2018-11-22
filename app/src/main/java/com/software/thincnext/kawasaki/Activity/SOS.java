package com.software.thincnext.kawasaki.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.software.thincnext.kawasaki.R;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SOS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);
        ButterKnife.bind(this);

    }

    @OnClick({ R.id.back_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button:
                finish();
                break;

        }
    }

}
