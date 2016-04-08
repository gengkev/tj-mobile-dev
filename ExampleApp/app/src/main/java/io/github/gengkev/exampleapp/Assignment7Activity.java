package io.github.gengkev.exampleapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Assignment7Activity extends AppCompatActivity {
    private static final String TAG = Assignment7Activity.class.getName();

    private Intent mRequestFileIntent;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment7);

        mImageView = (ImageView) findViewById(R.id.assgn7_imageview);

        Button button = (Button) findViewById(R.id.assgn7_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(mRequestFileIntent, 0);
            }
        });

        mRequestFileIntent = new Intent(Intent.ACTION_PICK);
        mRequestFileIntent.setType("image/*");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri returnUri = data.getData();
            Bitmap bitmap = readBitmapFromUri(returnUri, 600, 600);
            mImageView.setImageBitmap(bitmap);
        }
    }

    private Bitmap readBitmapFromUri(Uri uri, int reqWidth, int reqHeight) {
        InputStream inputStream;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            assert inputStream != null;
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found.", e);
            return null;
        }

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        try {
            inputStream = getContentResolver().openInputStream(uri);
            assert inputStream != null;
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found.", e);
            return null;
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeStream(inputStream, null, options);

        try {
            inputStream.close();
        }
        catch (IOException e) {
            Log.e(TAG, "Error closing inputPfd", e);
        }

        return bm;
    }

    public static int calculateInSampleSize(
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
}
