package com.twok16.motheropearlhacks.memester;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.Manifest;

public class MainActivity extends AppCompatActivity {
    int MY_PERMISSIONS_REQUEST_READ_CONTACTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        File image = getImageFiles();
        List<String> texts = getAllSms();
        Intent intent = new Intent(this, MemesRMaiden.class);
        intent.putExtra("imageFile", image.toString());
        startActivity(intent);
    }

     public File getImageFiles() {

         File path = Environment.getExternalStoragePublicDirectory(
                 Environment.DIRECTORY_DCIM + "/Camera");
         File[] files = path.listFiles();

         return files[getRandomIndex(files.length)];
    }

    int getRandomIndex(int max) {
        Random r = new Random();
        return r.nextInt(max) + 0;
    }

    public List<String> getAllSms() {
        ArrayList<String> texts = new ArrayList<String>();
        ContentResolver cr;
        cr = this.getContentResolver();

        Cursor in = cr.query(Telephony.Sms.Inbox.CONTENT_URI,
                new String[] {Telephony.Sms.Inbox.ADDRESS, Telephony.Sms.Inbox.BODY},
                null,
                null,
                Telephony.Sms.Inbox.DEFAULT_SORT_ORDER);

        Cursor out = cr.query(Telephony.Sms.Sent.CONTENT_URI,
                new String[] {Telephony.Sms.Sent.ADDRESS, Telephony.Sms.Sent.BODY},
                null,
                null,
                Telephony.Sms.Outbox.DEFAULT_SORT_ORDER);

        int totalSMSOut = out.getCount();

        if (out.moveToFirst()) {
            for (int i = 0; i < totalSMSOut; i++) {
                //System.out.println(out.getString(1));
                texts.add(out.getString(1));
                out.moveToNext();
            }
        }
        in.close();
        out.close();

        return texts;
    }



}
