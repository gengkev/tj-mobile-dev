package io.github.gengkev.iondatapicker.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.github.gengkev.iondatapicker.util.BetterAsyncTask;

public class DownloadImageTask extends BetterAsyncTask<String, Void, Uri> {
    private static final String TAG = DownloadImageTask.class.getName();
    private static final String ALBUM_NAME = "IonProfiles";

    private String mAccessToken;

    public DownloadImageTask(String accessToken, ResultListener<Uri> listener) {
        super(listener);
        mAccessToken = accessToken;
    }

    @Override
    protected Uri doInBackground(String... params) {
        if (params.length != 1) {
            throw new IllegalArgumentException();
        }
        String id = params[0];
        Uri profileUrl = OAuthHelper.makeProfileImageURL(id);

        // Download image
        Bitmap image;
        try {
            image = OAuthHelper.getProfileImage(profileUrl.toString(), mAccessToken);
        }
        catch (IOException e) {
            Log.e(TAG, "Exception downloading image", e);
            setException(e);
            return null;
        }

        // Save image to private external storage
        File storageDir;
        File imageFile;
        FileOutputStream outStream = null;
        try {
            storageDir = getAlbumStorageDir(ALBUM_NAME);
            imageFile = new File(storageDir, id + ".png");
            outStream = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        }
        catch (IOException e) {
            Log.e(TAG, "Exception saving image", e);
            setException(e);
            return null;
        }
        finally {
            if (outStream != null) {
                try {
                    outStream.close();
                }
                catch (IOException e) {
                    Log.e(TAG, "sjklfdslhkdskjf", e);
                }
            }
        }

        return Uri.fromFile(imageFile);
    }

    public File getAlbumStorageDir(String albumName) throws IOException {
        // Get the directory for the app's private pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs() && !file.isDirectory()) {
            Log.e(TAG, "Directory not created");
            throw new IOException("Directory not created: " + file);
        }
        return file;
    }
}
