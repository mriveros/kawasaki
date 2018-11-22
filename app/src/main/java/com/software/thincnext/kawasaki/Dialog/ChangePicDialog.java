package com.software.thincnext.kawasaki.Dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.software.thincnext.kawasaki.Activity.HomeActivity;
import com.software.thincnext.kawasaki.R;

public class ChangePicDialog extends DialogFragment implements View.OnClickListener {

    //Declaring views
    private LinearLayout cameraClick, galleryClick, removeClick;
    private ImageView closeClick;

    public ChangePicDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Creating dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View rootView = inflater.inflate(R.layout.layout_change_pic_dialog, null);

        //Initialising views
        closeClick = (ImageView) rootView.findViewById(R.id.iv_changePicDialog_close);
        cameraClick = (LinearLayout) rootView.findViewById(R.id.ll_changePicDialog_camera);
        galleryClick = (LinearLayout) rootView.findViewById(R.id.ll_changePicDialog_gallery);


        //Setting onclick listner
        closeClick.setOnClickListener(this);
        cameraClick.setOnClickListener(this);
        galleryClick.setOnClickListener(this);



        builder.setView(rootView);
        return builder.create();
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.iv_changePicDialog_close:

                dismiss();

                break;

            case R.id.ll_changePicDialog_camera:

                //Calling choose camera
                ((HomeActivity) getActivity()).chooseCamera();

                dismiss();

                break;

            case R.id.ll_changePicDialog_gallery:

                //Calling choose gallery
                ((HomeActivity) getActivity()).chooseGallery();

                dismiss();

                break;



        }


    }


}