package com.twok16.motheropearlhacks.memester;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.Manifest;
import android.app.Activity;

public class MainActivity extends Activity {
    int MY_PERMISSIONS_REQUEST_READ_CONTACTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_SMS},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

    }


    public void newMeme(View view) {
        Intent intent = new Intent(this, MemesRMaiden.class);
        startActivity(intent);
    }
}
