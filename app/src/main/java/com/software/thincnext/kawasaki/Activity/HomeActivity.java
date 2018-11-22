package com.software.thincnext.kawasaki.Activity;


import android.Manifest;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.software.thincnext.kawasaki.Dialog.ChangePicDialog;
import com.software.thincnext.kawasaki.Inbox.InboxActivity;
import com.software.thincnext.kawasaki.R;
import com.software.thincnext.kawasaki.Services.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;





public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    //capturing from camera and gallery
    public static final int REQUEST_CAMERA_PROFILEIMAGE = 1088;
    public static final int REQUEST_GALLERY_PROFILEIMAGE = 1089;
    private static final int MY_PERMISSIONS_REQUEST_ACCOUNTS = 1;

    private static final String TAG = HomeActivity.class.getSimpleName();

    private SharedPreferences sharedPreferences;




    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.book_service)
    LinearLayout mBookService;

    CircleImageView chooseImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);


        //Hiding Keyboard
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        sharedPreferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mBookService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,BookService.class);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);

        chooseImage = (CircleImageView) header.findViewById(R.id.imageView);


        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Checking permission Marshmallow
                if (Build.VERSION.SDK_INT >= 23) {

                    if (checkPermissionCameraGallery()) {
                        FragmentManager changePicManager = getFragmentManager();
                        ChangePicDialog changePicDialog = new ChangePicDialog();
                        changePicDialog.show(changePicManager, "CHANGEPIC_DIALOG");

                    } else {

                        //Request permission
                        requestPermissionCameraGallery();
                    }
                } else {

                    FragmentManager changePicManager = getFragmentManager();
                    ChangePicDialog changePicDialog = new ChangePicDialog();
                    changePicDialog.show(changePicManager, "CHANGEPIC_DIALOG");


                }
            }
        });
    }

    private void requestPermissionCameraGallery() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {


            /*Snackbar snackbar = Snackbar
                    .make(changeImageActivityContainer, "Please allow permission in App Settings.", Snackbar.LENGTH_LONG)
                    .setAction("Go settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                            startActivity(intent);

                        }
                    });

            snackbar.show();*/


        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {


           /* Snackbar snackbar = Snackbar
                    .make(changeImageActivityContainer, "Please allow permission in App Settings.", Snackbar.LENGTH_LONG)
                    .setAction("Go settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                            startActivity(intent);

                        }
                    });

            snackbar.show();*/


        } else {

            int permissionCAMERA = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA);


            int storagePermission = ContextCompat.checkSelfPermission(this,


                    Manifest.permission.READ_EXTERNAL_STORAGE);


            List<String> listPermissionsNeeded = new ArrayList<>();
            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,

                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNTS);

            }


        }

    }

    private boolean checkPermissionCameraGallery() {

        int permissionCAMERA = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);


        int storagePermission = ContextCompat.checkSelfPermission(this,


                Manifest.permission.READ_EXTERNAL_STORAGE);


        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,


                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return false;
        }

        return true;


    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {


            Snackbar snackbar = Snackbar
                    .make(drawerLayout, "Please allow permission in App Settings.", Snackbar.LENGTH_LONG)


                    .setAction("Go settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                            startActivity(intent);

                        }
                    });

            snackbar.show();


        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {


            Snackbar snackbar = Snackbar
                    .make(drawerLayout, "Please allow permission in App Settings.", Snackbar.LENGTH_LONG)
                    .setAction("Go settings", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                            startActivity(intent);

                        }
                    });

            snackbar.show();


        } else {

            int permissionCAMERA = ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA);


            int storagePermission = ContextCompat.checkSelfPermission(this,


                    Manifest.permission.READ_EXTERNAL_STORAGE);


            List<String> listPermissionsNeeded = new ArrayList<>();
            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(Manifest.permission.CAMERA);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,

                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNTS);

            }


        }
    }

    //Func - Checking permission granted
    private boolean checkPermission() {

        int permissionCAMERA = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);


        int storagePermission = ContextCompat.checkSelfPermission(this,


                Manifest.permission.READ_EXTERNAL_STORAGE);


        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,


                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_REQUEST_ACCOUNTS);
            return false;
        }

        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCOUNTS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    FragmentManager changePicManager = getFragmentManager();
                    ChangePicDialog changePicDialog = new ChangePicDialog();
                    changePicDialog.show(changePicManager, "CHANGEPIC_DIALOG");

                } else {


                }
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CAMERA_PROFILEIMAGE) {

                Bitmap capturedBitmap = (Bitmap) data.getExtras().get("data");
                chooseImage.setImageBitmap(capturedBitmap);

            }
            else if (requestCode == REQUEST_GALLERY_PROFILEIMAGE) {

                try {
                    Bitmap selectedBitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                    chooseImage.setImageBitmap(selectedBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {

           /* Snackbar snackbar = Snackbar
                    .make(, "Cancelled", Snackbar.LENGTH_LONG);

            snackbar.show();*/

        }
    }



    @OnClick({R.id.sos_list,R.id.book_service_layout,R.id.home_inbox})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sos_list:
                Intent intent0=new Intent(HomeActivity.this,SOS.class);
                startActivity(intent0);
                break;

            case R.id.book_service_layout:

                Intent intent1=new Intent(HomeActivity.this,BookService.class);
                startActivity(intent1);
                break;

            case R.id.home_inbox:

                Intent intent2=new Intent(HomeActivity.this,InboxActivity.class);
                startActivity(intent2);
                break;

                }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_change_bike) {
            // Handle the camera action
        } else if (id == R.id.nav_book_service) {

            //Going to House Page
            Intent intent= new Intent(HomeActivity.this,BookService.class);
            startActivity(intent);

        } else if (id == R.id.nave_service_history) {

        } else if (id == R.id.nav_sos) {

            //Going to House Page
            Intent intent= new Intent(HomeActivity.this,SOS.class);
            startActivity(intent);



        } else if (id==R.id.nav_inbox){

            //Going to Inbox Page
            Intent intent= new Intent(HomeActivity.this,InboxActivity.class);
            startActivity(intent);

        }

        else if (id==R.id.nav_logout){

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();

            Intent intent= new Intent(HomeActivity.this,Register.class);


            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Func -  Select Camera
    public void chooseCamera() {
        //Checking permission Marshmallow
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermissionCameraGallery()) {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CAMERA_PROFILEIMAGE);

            } else {
                //Request permission
                requestPermissionCameraGallery();

            }
        } else {


            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, REQUEST_CAMERA_PROFILEIMAGE);
        }//zero can be replaced with any action code
    }
    //Func - Select Gallery
    public void chooseGallery() {
        //Checking permission Marshmallow
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermissionCameraGallery()) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(galleryIntent, REQUEST_GALLERY_PROFILEIMAGE);


            } else {

                //Request permission
                requestPermissionCameraGallery();
            }
        } else {

            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(galleryIntent, REQUEST_GALLERY_PROFILEIMAGE);


        }
    }


}
