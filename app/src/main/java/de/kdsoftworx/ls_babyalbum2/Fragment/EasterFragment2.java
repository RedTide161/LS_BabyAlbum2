package de.kdsoftworx.ls_babyalbum2.Fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.kdsoftworx.ls_babyalbum2.Activity.ChildDataActivity;
import de.kdsoftworx.ls_babyalbum2.Data.UserLokalStore;
import de.kdsoftworx.ls_babyalbum2.Helper.ImageHandler;
import de.kdsoftworx.ls_babyalbum2.R;
import de.kdsoftworx.ls_babyalbum2.RoomDatabase.LSBookdata;
import de.kdsoftworx.ls_babyalbum2.ViewModel.BookdataViewModel;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;


public class EasterFragment2 extends Fragment {

    private BookdataViewModel bookdataViewModel;

    ImageView E_image2, E_image3, E_image4;
    public static final String Log_tag = ChildDataActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_easter_2, container, false);

        // initialise all imageViews
        init(root);

        // get newest Data from Database
        bookdataViewModel = ViewModelProviders.of(this).get(BookdataViewModel.class);
        bookdataViewModel.getAllBookdataEntries().observe(this, new Observer<List<LSBookdata>>() {
            @Override
            public void onChanged(List<LSBookdata> lsBookdata) {

                if (lsBookdata.size() >0) {
                    LSBookdata currentData = lsBookdata.get(0);

                    if (currentData.E_image2 != null) {
                        // get Path of Image from Database
                        String pathImage1 = currentData.E_image2;
                        // Load File
                        Bitmap bmp1 = BitmapFactory.decodeFile(pathImage1);
                        // load Bitmap to current imageView
                        E_image2.setImageBitmap(bmp1);
                    }

                    if (currentData.E_image3 != null) {
                        // get Path of Image from Database
                        String pathImage2 = currentData.E_image3;
                        // Load File
                        Bitmap bmp2 = BitmapFactory.decodeFile(pathImage2);
                        // load Bitmap to current imageView
                        E_image3.setImageBitmap(bmp2);
                    }

                    if (currentData.E_image4 != null) {
                        // get Path of Image from Database
                        String pathImage3 = currentData.E_image4;
                        // Load File
                        Bitmap bmp3 = BitmapFactory.decodeFile(pathImage3);
                        // load Bitmap to current imageView
                        E_image4.setImageBitmap(bmp3);
                    }
                }
            }
        });

        E_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check Permission for reading Storage
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) + ActivityCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Ask for Permission
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},  2000);
                } else {
                    // start Intent for opening Gallery to get Pictures
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 43);
                }
            }
        });

        E_image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check Permission for reading Storage
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) + ActivityCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Ask for Permission
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},  2000);
                } else {
                    // start Intent for opening Gallery to get Pictures
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 44);
                }
            }
        });

        E_image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check Permission for reading Storage
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) + ActivityCompat.checkSelfPermission(getActivity(), WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Ask for Permission
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE},  2000);
                } else {
                    // start Intent for opening Gallery to get Pictures
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 45);
                }
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case 43: // Load Picture in Cropper
                    Uri picUri1 = data.getData();

                    Intent intentIV1 = CropImage.activity(picUri1)
                            //Customize Cropper
                            .setCropMenuCropButtonIcon(R.drawable.crop_icon).setAspectRatio(1, 1).setActivityMenuIconColor(R.color.colorPrimaryDark).getIntent(getContext());
                    startActivityForResult(intentIV1, 46);
                    break;

                case 44: // Load Picture in Cropper
                    Uri picUri2 = data.getData();

                    Intent intentIV2 = CropImage.activity(picUri2)
                            //Customize Cropper
                            .setCropMenuCropButtonIcon(R.drawable.crop_icon).setAspectRatio(1, 1).setActivityMenuIconColor(R.color.colorPrimaryDark).getIntent(getContext());
                    startActivityForResult(intentIV2, 47);
                    break;

                case 45: // Load Picture in Cropper
                    Uri picUri3 = data.getData();

                    Intent intentIV3 = CropImage.activity(picUri3)
                            //Customize Cropper
                            .setCropMenuCropButtonIcon(R.drawable.crop_icon).setAspectRatio(1, 1).setActivityMenuIconColor(R.color.colorPrimaryDark).getIntent(getContext());
                    startActivityForResult(intentIV3, 48);
                    break;

                case 46: // Add Picture to ImageView on Fragment "Easterfragment_2"
                    CropImage.ActivityResult result1 = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        Uri resultUri = result1.getUri();
                        try {
                            // Get User-Information
                            UserLokalStore userLokalStore = UserLokalStore.getInstance(getContext());

                            // Get Picture from ImageCropper
                            Bitmap bitmapImage1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);

                            // Get Image and pass it to ImageHandler to Store Image in Directory
                            ImageHandler imageHandler = ImageHandler.getInstance();
                            String imagePath = imageHandler.saveImageAsync("E_image2_" + userLokalStore.getCurrentRecordId(), bitmapImage1);

                            // Create a Query for storing image in Database
                            SimpleSQLiteQuery query = new SimpleSQLiteQuery("UPDATE " + getString(R.string.tableName) + " " +
                                    "SET E_image2 = '"+ imagePath  +
                                    "' WHERE customer_id = " + userLokalStore.getLoggedInUser().getUserId() + " AND id = " + userLokalStore.getCurrentRecordId());
                            // Store Image in Database
                            bookdataViewModel.update(query);

                            // Load Image to imageView
                            E_image2.setImageBitmap(bitmapImage1);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result1.getError();
                        Log.v(Log_tag, "Error Load Pic2 on Easter Fragment 2: " + error);
                    }
                    break;

                case 47: // Add Picture to ImageView on Fragment "Easterfragment_2"
                    CropImage.ActivityResult result2 = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        Uri resultUri = result2.getUri();
                        try {
                            // Get User-Information
                            UserLokalStore userLokalStore = UserLokalStore.getInstance(getContext());

                            // Get Picture from ImageCropper
                            Bitmap bitmapImage2 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);

                            // Get Image and pass it to ImageHandler to Store Image in Directory
                            ImageHandler imageHandler = ImageHandler.getInstance();
                            String imagePath = imageHandler.saveImageAsync("E_image3_" + userLokalStore.getCurrentRecordId(), bitmapImage2);

                            // Create a Query for storing image in Database
                            SimpleSQLiteQuery query = new SimpleSQLiteQuery("UPDATE " + getString(R.string.tableName) + " " +
                                    "SET E_image3= '"+ imagePath  +
                                    "' WHERE customer_id = " + userLokalStore.getLoggedInUser().getUserId() + " AND id = " + userLokalStore.getCurrentRecordId());
                            // Store Image in Database
                            bookdataViewModel.update(query);

                            // Load Image to imageView
                            E_image3.setImageBitmap(bitmapImage2);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result2.getError();
                        Log.v(Log_tag, "Error Load Pic3 on Easter Fragment 2: " + error);
                    }
                    break;

                case 48: // Add Picture to ImageView on Fragment "Easterfragment_2"
                    CropImage.ActivityResult result3 = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        Uri resultUri = result3.getUri();
                        try {
                            // Get User-Information
                            UserLokalStore userLokalStore = UserLokalStore.getInstance(getContext());

                            // Get Picture from ImageCropper
                            Bitmap bitmapImage3 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);

                            // Get Image and pass it to ImageHandler to Store Image in Directory
                            ImageHandler imageHandler = ImageHandler.getInstance();
                            String imagePath = imageHandler.saveImageAsync("E_image4_" + userLokalStore.getCurrentRecordId(), bitmapImage3);

                            // Create a Query for storing image in Database
                            SimpleSQLiteQuery query = new SimpleSQLiteQuery("UPDATE " + getString(R.string.tableName) + " " +
                                    "SET E_image4 = '"+ imagePath  +
                                    "' WHERE customer_id = " + userLokalStore.getLoggedInUser().getUserId() + " AND id = " + userLokalStore.getCurrentRecordId());
                            // Store Image in Database
                            bookdataViewModel.update(query);

                            // Load Image to imageView
                            E_image4.setImageBitmap(bitmapImage3);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result3.getError();
                        Log.v(Log_tag, "Error Load Pic4 on Easter Fragment 2: " + error);
                    }
                    break;

                default:
                    Toast.makeText(getActivity(), R.string.e_PicNotOk, Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void init(View view)
    {
        E_image2 = view.findViewById(R.id.iv_e_easter_pic2);
        E_image3 = view.findViewById(R.id.iv_e_easter_pic3);
        E_image4 = view.findViewById(R.id.iv_e_easter_pic4);
    }

}
