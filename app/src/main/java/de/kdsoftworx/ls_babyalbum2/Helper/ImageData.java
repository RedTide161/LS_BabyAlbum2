package de.kdsoftworx.ls_babyalbum2.Helper;

import android.graphics.Bitmap;

public class ImageData {

        public static ImageData mImageData;

        public String imageName;
        public Bitmap imageToSave;

        public static ImageData getInstance() {
            if (mImageData == null) { //if there is no instance available... create new one

                // synchronize "getInstance-Method" that a second thread will have to wait until the "getInstance-Method" is completed for the first thread.
                synchronized (ImageData.class) {

                    if (mImageData == null) // double-check if there is a Instance available
                    {
                        mImageData = new ImageData();
                    }
                }
            }
            return mImageData;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }

        public Bitmap getImageToSave() {
            return imageToSave;
        }

        public void setImageToSave(Bitmap imageToSave) {
            this.imageToSave = imageToSave;
        }
}
