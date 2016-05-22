package com.twok16.motheropearlhacks.memester;

import android.app.Activity;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.content.res.AssetManager;

import java.util.Locale;
import android.graphics.Matrix;
import java.io.File;

public class MemesRMaiden extends Activity {
    private ImageFinder imageFinder;
    private PhraseFinder phraseFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MemeView(this));
        imageFinder = new ImageFinder();
        phraseFinder = new PhraseFinder(this);
    }

    // creates a new Meme object from global ImageFinder and PhraseFinder objects
    protected Meme makeMeme() {
        return new Meme(phraseFinder, imageFinder);
    }


    public class MemeView extends View {

        public MemeView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onDraw(Canvas canvas) {
            this.setDrawingCacheEnabled(true);
            Meme meme = makeMeme(); // make a new meme object to draw to canvas
            Bitmap bm = null;

            File file = new File(meme.file_name);
            Bitmap bitmap = BitmapFactory.decodeFile(meme.file_name);

            String[] texts = splitText(meme.message);
            String text1 = texts[0];
            String text2 = texts[1];

            Paint paint = new Paint();
            canvas.drawPaint(paint);
            paint.setColor(Color.WHITE);
            paint.setTextSize(60);
            paint.setTypeface(getTypeface());
            System.out.println("text.length " + meme.message.length());
            System.out.println(meme.message);
            try {
                ExifInterface exif = new ExifInterface(meme.file_name);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                if (orientation == 6) {
                    bitmap = rotateBitmap(bitmap, 90);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            canvas.drawBitmap(bitmap, 0, 0, null);
            canvas.drawText(text1, 0, bitmap.getHeight() / 3, paint);
            canvas.drawText(text2, 0, bitmap.getHeight(), paint);
        }

        public Typeface getTypeface() {

            AssetManager am = this.getContext().getApplicationContext().getAssets();

            return Typeface.createFromAsset(am,
                    String.format(Locale.US, "fonts/%s", "Coda-Heavy.ttf"));
        }

        public Bitmap rotateBitmap(Bitmap source, float angle)
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        }

        protected String[] splitText(String text) {
            String firstHalf = text.substring(0, text.length()/ 2);
            String secondHalf = text.substring(text.length()/2, text.length());
            String[] texts = new String[2];
            texts[0] = firstHalf;
            texts[1] = secondHalf;
            return texts;
        }
    }

}
