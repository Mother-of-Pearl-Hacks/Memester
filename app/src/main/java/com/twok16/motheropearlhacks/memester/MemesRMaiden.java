package com.twok16.motheropearlhacks.memester;

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

public class MemesRMaiden extends AppCompatActivity {

    protected String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MemeView(this));

        Bundle p = getIntent().getExtras();
        fileName = p.getString("imageFile");
        //new MemeView(this, new Canvas(BitmapFactory.decodeFile(fileName)));
    }

    public class MemeView extends View {

        public MemeView(Context context) {
            super(context);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void onDraw(Canvas canvas) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(fileName, options);
            Display display = getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getSize(point); // will return width as point.x and height as point.y
            int width = options.outWidth;
            int bitmapWidth = 0;
            int bitmapHeight = options.outHeight;
            if (width > point.x) {int widthRatio = Math.round((float) width / (float) point.x);
                options.inSampleSize = widthRatio;
                bitmapWidth = widthRatio;
            }
            System.out.println("point x " + point.x + " point y "  + point.y);
            options.inSampleSize = point.x - 5;
            options.inJustDecodeBounds = false;

            float centerX = (point.x - bitmapWidth) / 2;
            //int centerY = point.y / 2;

            Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }

}
