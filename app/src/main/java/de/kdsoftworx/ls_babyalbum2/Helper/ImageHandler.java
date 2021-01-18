package de.kdsoftworx.ls_babyalbum2.Helper;


import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class ImageHandler{

    public static ImageHandler mImageHandler;

    public static ImageHandler getInstance() {
        if (mImageHandler == null) { //if there is no instance available... create new one

            // synchronize "getInstance-Method" that a second thread will have to wait until the "getInstance-Method" is completed for the first thread.
            synchronized (ImageHandler.class) {

                if (mImageHandler == null) // double-check if there is a Instance available
                {
                    mImageHandler = new ImageHandler();
                }
            }
        }
        return mImageHandler;
    }

    public String saveImageAsync(String imageName, Bitmap imageToSave) throws ExecutionException, InterruptedException {

        ImageData imageData = ImageData.getInstance();
        // Set ImageName and Image for AsyncTask
        imageData.setImageName(imageName);
        imageData.setImageToSave(imageToSave);

        // Start AsyncTask
        String result = new saveImageAsync().execute(imageData).get();


        return result;
    }

    private static class saveImageAsync extends AsyncTask<ImageData, Void,String>{

        @Override
        protected String doInBackground(ImageData... imageData) {

            // Get all ImageData
            String imageName = imageData[0].getImageName();
            Bitmap imageToSave = imageData[0].getImageToSave();

                // Create Path for Storing Images
                File direct = new File(Environment.getExternalStorageDirectory().getPath() + "/de.kdsofworx.ls_babyalbum2/images/");
                // Create a FilePath and Imagename for Image
                File file = new File(direct, imageName);

                // Check if Directory exist - if not -> Create Folder
                if (!direct.exists()) {
                    File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().getPath() + "/de.kdsofworx.ls_babyalbum2/images/");
                    wallpaperDirectory.mkdirs();
                }

                // Store Image in Directory/Folder
                try {
                    FileOutputStream fileOutput = new FileOutputStream(file);
                    imageToSave.compress(Bitmap.CompressFormat.PNG, 100, fileOutput);
                    fileOutput.flush();
                    fileOutput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            // Return Imagepath for Storing in Database
            return file.toString();
            }

        }
    }