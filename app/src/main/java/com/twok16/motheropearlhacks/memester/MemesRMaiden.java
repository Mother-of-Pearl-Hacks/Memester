package com.twok16.motheropearlhacks.memester;

import android.app.Activity;
import android.graphics.Point;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Typeface;
import android.content.res.AssetManager;

import java.io.IOException;
import java.util.Locale;
import android.graphics.Matrix;

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

            Paint paint = new Paint();
            canvas.drawPaint(paint);
            paint.setColor(Color.WHITE);
            int text_size = 100;
            paint.setTextSize(text_size);
            paint.setTypeface(getTypeface());

            try {
                Bitmap bitmap = createBitmap(meme.image_file_name);
                Point screen_size = getScreenSize();
                // get the x and y values to center the image on the canvas
                int y_position = getYPosition(screen_size.y, bitmap.getHeight());
                int x_position = getXPosition(screen_size.x, bitmap.getWidth());
                canvas.drawBitmap(bitmap, x_position, y_position, paint);

                int font_size = getFontSize(paint, meme.message, bitmap);
                if (font_size < 80) {
                    String[] texts = splitText(meme.message);
                    String text1 = texts[0];
                    String text2 = texts[1];
                    font_size = getFontForTwo(paint, text1, text2, bitmap);
                    paint.setTextSize(font_size);
                    canvas.drawText(text1, x_position, bitmap.getHeight() / 9 + y_position, paint);
                    canvas.drawText(text2, x_position, bitmap.getHeight() + y_position - (bitmap.getWidth() / 15), paint);
                } else {
                    canvas.drawText(meme.message, x_position, bitmap.getHeight() / 9 + y_position, paint);
                }

            } catch (Exception e) { // if the bitmap throws an IOException... get a new Image!
                e.printStackTrace();
            }
        }

        private int getFontSize(Paint paint, String text, Bitmap bitmap) {
            int font_size = 100;
            while (paint.measureText(text) > bitmap.getWidth()) {
                System.out.println("font size " + font_size);
                paint.setTextSize(font_size);
                font_size -= 1;
            }
            return font_size;
        }

        private int getFontForTwo(Paint paint, String text1, String text2, Bitmap bitmap) {
            int font_size = 100;
            while (paint.measureText(text1) > bitmap.getWidth() || paint.measureText(text2) > bitmap.getWidth()) {
                System.out.println("font size 2 " + font_size);
                paint.setTextSize(font_size);
                font_size -= 1;
            }
            return font_size;
        }

        // gets the desired y position to center the image on the canvas
        private int getYPosition(int display_height, int image_height) {
            return (display_height - image_height) / 2;
        }

        // gets the desired x position to center the image on the canvas
        private int getXPosition(int display_width, int image_width) {
            return (display_width - image_width) / 2;
        }

        //make a bitmap from an image file
        private Bitmap createBitmap(String file_name) throws IOException{
            Point display_size = getScreenSize();
            Bitmap bitmap = BitmapFactory.decodeFile(file_name, getSampleSize(display_size.x, file_name));
            if (isHorizontal(file_name)) { // if the image is horizontal
                bitmap = rotateBitmap(bitmap, 90); // rotate it by 90 degrees
            }
            return bitmap;
        }

        // checks if the image is bigger than the display size
        // returns a the ratio between the image width and display width for resizing
        // used this tutorial http://www.informit.com/articles/article.aspx?p=2143148&seqNum=2u
        // TODO: incorporate height into this
        private BitmapFactory.Options getSampleSize(int display_width, String file_name) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file_name, options);
            int width = options.outWidth;
            if (width > display_width) {
                int width_ratio = Math.round((float) width / (float) display_width);
                options.inSampleSize = width_ratio;
            }
            options.inJustDecodeBounds = false;
            return options;
        }

        // checks if an image is horizontal
        private boolean isHorizontal(String file_name) throws IOException {
            ExifInterface exif = new ExifInterface(file_name);
            return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1) == 6;
        }

        // gets the size of the screen, returning it as a point
        private Point getScreenSize() {
            Display display = getWindowManager().getDefaultDisplay();
            Point point = new Point();
            display.getSize(point);
            return point;
        }

        // method to get this Impact-like font from the app's assets
        private Typeface getTypeface() {
            AssetManager am = this.getContext().getApplicationContext().getAssets();
            return Typeface.createFromAsset(am,
                    String.format(Locale.US, "fonts/%s", "Coda-Heavy.ttf"));
        }

        // rotates a bitmap
        private Bitmap rotateBitmap(Bitmap source, float angle)
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        }

        // split a text in (about) half at whitespace
        private String[] splitText(String text) {
            int split_index = text.length() / 2;

            // get the middle index and check if it's a space
            // if not, keep decrementing the index until a space is found or the index is 0
            while (split_index >= 0 && text.charAt(split_index) != ' ') {
                System.out.println("char at " + split_index + " is " + text.charAt(split_index));
                split_index -= 1;
            }
            if (split_index == -1 ) {
                split_index = 0;
            }
            String[] texts;
            if (split_index == 0) { // if the index is 0, just split the text in half and don't worry about being cute
                String firstHalf = text.substring(0, text.length()/ 2);
                String secondHalf = text.substring(text.length()/2, text.length());
                texts = new String[2];
                texts[0] = firstHalf;
                texts[1] = secondHalf;
            } else {
                System.out.println("INDEX " + split_index);
                String firstHalf = text.substring(0, split_index);
                String secondHalf = text.substring(split_index, text.length());
                texts = new String[2];
                texts[0] = firstHalf;
                texts[1] = secondHalf;
            }
            return texts;
        }
    }

}
