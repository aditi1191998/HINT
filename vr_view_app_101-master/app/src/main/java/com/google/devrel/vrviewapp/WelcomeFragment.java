/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.devrel.vrviewapp;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * Fragment for handling the Welcome tab.
 */
public class WelcomeFragment extends Fragment {
    private VrPanoramaView panoWidgetView;
    //private ImageLoaderTask backgroundImageLoaderTask;
    private ImageLoaderTask backgroundImageLoaderTask;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.welcome_fragment, container,false);
        View v =  inflater.inflate(R.layout.welcome_fragment, container,false);
        panoWidgetView = (VrPanoramaView) v.findViewById(R.id.pano_view);
        return v;
    }

    private synchronized void loadPanoImage(String panoImageName) {
        ImageLoaderTask task = backgroundImageLoaderTask;
        if (task != null && !task.isCancelled()) {
            // Cancel any task from a previous loading.
            task.cancel(true);
        }

        // pass in the name of the image to load from assets.
        VrPanoramaView.Options viewOptions = new VrPanoramaView.Options();
        viewOptions.inputType = VrPanoramaView.Options.TYPE_STEREO_OVER_UNDER;

        // use the name of the image in the assets/ directory.
        //String panoImageName = "https://firebasestorage.googleapis.com/v0/b/hint-1234.appspot.com/o/images%2Fsample_converted.jpg?alt=media&token=3da3066c-d22e-48ce-8d72-db4ab36e3ccf";

        // create the task passing the widget view and call execute to start.
        task = new ImageLoaderTask(panoWidgetView, viewOptions, panoImageName);
        task.execute(getActivity().getAssets());
        backgroundImageLoaderTask = task;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


            new CountDownTimer(120000, 30000) {
                int i = 0;
                String a, b, c;
                public void onTick(long millisUntilFinished) {
                    if(i==0) {
                        a = getResources().getString(R.string.i10);
                        b = getResources().getString(R.string.i20);
                        c = a +"&" +  b;
                    }
                    else if(i==2) {
                        a = getResources().getString(R.string.i11);
                        b = getResources().getString(R.string.i21);
                        c = a +"&" +  b;
                    }
                    else if(i==4) {
                        a = getResources().getString(R.string.i12);
                        b = getResources().getString(R.string.i22);
                        c = a +"&" +  b;
                    }
                    else
                        c = "https://firebasestorage.googleapis.com/v0/b/hint-1234.appspot.com/o/images%2Fandes.jpg?alt=media&token=0db9b1cd-f787-4528-899b-135da0b5f189";
                    Log.d("app", Integer.toString(i));

                    loadPanoImage(c);
                    i = i + 1;
                }

                public void onFinish() {
                    //mTextField.setText("done!");
                    Log.d("app", "dooooooone");
                    //i = i + 1;
                }
            }.start();





    }

    @Override
    public void onPause() {
        panoWidgetView.pauseRendering();
        super.onPause();
    }

    @Override
    public void onResume() {
        panoWidgetView.resumeRendering();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // Destroy the widget and free memory.
        panoWidgetView.shutdown();
        super.onDestroy();
    }


    public String readFile(String fileName) {
        //reading text from file
        try {
            Context ctx = getContext();
            FileInputStream fileIn=ctx.openFileInput(fileName);
            InputStreamReader InputRead= new InputStreamReader(fileIn);

            char[] inputBuffer= new char[1];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();

            //textmsg.setText(s);
            //Toast.makeText(getBaseContext(), s,
            //       Toast.LENGTH_SHORT).show();

            return s;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
