package com.google.devrel.vrviewapp;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/*
 * Created by shubhika on 17-03-2018.
 */

public class ImageLoaderTask extends AsyncTask<AssetManager, Void, Bitmap> {
    private static final String TAG = "ImageLoaderTask";
    private final String assetName;
    private final WeakReference<VrPanoramaView> viewReference;
    private final VrPanoramaView.Options viewOptions;
    private static WeakReference<Bitmap> lastBitmap = new WeakReference<>(null);
    private static String lastName;

    public ImageLoaderTask(VrPanoramaView view, VrPanoramaView.Options viewOptions, String assetName) {
        viewReference = new WeakReference<>(view);
        this.viewOptions = viewOptions;
        this.assetName = assetName;
    }
    @Override
    protected Bitmap doInBackground(AssetManager... params) {

        AssetManager assetManager = params[0];

        if (assetName.equals(lastName) && lastBitmap.get() != null) {
            return lastBitmap.get();
        }
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            URL url = new URL(assetName);
            try(InputStream istr =(InputStream)url.getContent() ) {
                Bitmap b = BitmapFactory.decodeStream(istr);
                lastBitmap = new WeakReference<>(b);
                lastName = assetName;
                return b;
            } catch (IOException e) {
                Log.e(TAG, "Could not decode default bitmap: " + e);
                return null;
            }

        } catch (MalformedURLException e) {
            Log.e(TAG, "Wrong url: " + e);
            return null;
            //do whatever you want to do if you get the exception here
        }
        //URL url = new URL(assetName);


    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        final VrPanoramaView vw = viewReference.get();
        if (vw != null && bitmap != null) {
            vw.loadImageFromBitmap(bitmap, viewOptions);
        }
    }

}
