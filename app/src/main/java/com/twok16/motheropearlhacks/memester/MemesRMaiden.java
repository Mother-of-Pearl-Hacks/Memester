package com.twok16.motheropearlhacks.memester;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Canvas;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.view.Display;
import android.graphics.Point;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.content.res.AssetManager;

import java.io.IOException;
import java.util.Locale;
import android.graphics.Matrix;
import java.io.FileOutputStream;
import java.io.File;
import android.os.Environment;

public class MemesRMaiden extends Activity {

    protected String fileName;
    protected String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MemeView(this));

        Bundle p = getIntent().getExtras();
        fileName = p.getString("imageFile");
        text = p.getString("text");
        //new MemeView(this, new Canvas(BitmapFactory.decodeFile(fileName)));
    }

    public class MemeView extends View {

        public MemeView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onDraw(Canvas canvas) {
            this.setDrawingCacheEnabled(true);
            Bitmap bm = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName, options);
            Display display = getWindowManager().getDefaultDisplay();
            int width = options.outWidth;
            int bitmapWidth = 0;
            int bitmapHeight = options.outHeight;
            if (width > canvas.getWidth()) {int widthRatio = Math.round((float) width / (float) canvas.getWidth());
                options.inSampleSize = widthRatio;
                bitmapWidth = widthRatio;
            }
            System.out.println("point x " + canvas.getWidth() + " point y "  + canvas.getHeight());
            options.inJustDecodeBounds = false;


            //Bitmap bitmap = decodeSampledBitmapFromResource(fileName, 100, 100);

            Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);

            try {
                ExifInterface exif = new ExifInterface(fileName);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                if (orientation == 6) {
                    bitmap = rotateBitmap(bitmap, 90);
                }
            } catch (Exception e) {

            }

            //Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(),Bitmap.Config.ARGB_8888);
            //canvas.setBitmap(outBitmap);

            AssetManager am = this.getContext().getApplicationContext().getAssets();

            Typeface typeface = Typeface.createFromAsset(am,
                    String.format(Locale.US, "fonts/%s", "Coda-Heavy.ttf"));

            String[] texts = splitText();

            Paint paint = new Paint();
            canvas.drawPaint(paint);
            paint.setColor(Color.WHITE);
            paint.setTextSize(80);
            paint.setTypeface(typeface);

            canvas.drawBitmap(bitmap, 0, 0, null);
            canvas.drawText(texts[0], 0, bitmap.getHeight() / 3, paint);
            canvas.drawText(texts[1], 0, (bitmap.getHeight() / 3)*2 , paint);

            //this.destroyDrawingCache();
            //bm=this.getDrawingCache();
            //saveBitmap(bitmap);
        }

        public void saveBitmap(Bitmap bitmap) {
            String filename = "pippo.png";
            File sd = Environment.getExternalStorageDirectory();
            File dest = new File(sd, filename);

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(dest);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        }

        public Bitmap rotateBitmap(Bitmap source, float angle)
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        }

        protected String[] splitText() {
            String firstHalf = text.substring(0, text.length()/ 2);
            String secondHalf = text.substring(text.length()/2 + 1, text.length());
            String[] texts = new String[2];
            texts[0] = firstHalf;
            texts[1] = secondHalf;
            return texts;
        }
    }

    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Bitmap decodeSampledBitmapFromResource(String fileName, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(fileName, options);
    }

}
