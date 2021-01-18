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
import android.widget.EditText;
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

import static android.app.Activity.RESULT_OK;


public class TimeIsRunningFragment1 extends Fragment {

    private BookdataViewModel bookdataViewModel;
    private boolean isVisibleToUser=false;


    ImageView TIR_image1;
    EditText TIR_inputText1, TIR_inputText2, TIR_inputText3, TIR_inputText4;

    public static final String Log_tag = ChildDataActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_times_running_1, container, false);

        // initialise all editTexts and imageViews
        init(root);

        // get newest Data from Database
        bookdataViewModel = ViewModelProviders.of(this).get(BookdataViewModel.class);
        bookdataViewModel.getAllBookdataEntries().observe(this, new Observer<List<LSBookdata>>() {
            @Override
            public void onChanged(List<LSBookdata> lsBookdata) {

                if (lsBookdata.size() >0) {
                    LSBookdata currentData = lsBookdata.get(0);
                    TIR_inputText1.setText(currentData.TIR_inputText1);
                    TIR_inputText2.setText(currentData.TIR_inputText2);
                    TIR_inputText3.setText(currentData.TIR_inputText3);
                    TIR_inputText4.setText(currentData.TIR_inputText4);

                    if (currentData.TIR_image1 != null) {
                        // get Path of Image from Database
                        String pathImage1 = currentData.TIR_image1;
                        // Load File
                        Bitmap bmp1 = BitmapFactory.decodeFile(pathImage1);
                        // load Bitmap to current imageView
                        TIR_image1.setImageBitmap(bmp1);
                    }
                }
            }
        });

        TIR_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check Permission for reading Storage
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE + Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Ask for Permission
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE + Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2000);
                } else {
                    // start Intent for opening Gallery to get Pictures
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent, 51);
                }
            }
        });

        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case 51: // Load Picture in Cropper
                    Uri picUri1 = data.getData();

                    Intent intentIV1 = CropImage.activity(picUri1)
                            //Customize Cropper
                            .setCropMenuCropButtonIcon(R.drawable.crop_icon).setAspectRatio(1, 1).setActivityMenuIconColor(R.color.colorPrimaryDark).getIntent(getContext());
                    startActivityForResult(intentIV1, 52);
                    break;


                case 52: // Add Picture to ImageView on Fragment "fragment_bath_1
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
                            String imagePath = imageHandler.saveImageAsync("TIR_image1_" + userLokalStore.getCurrentRecordId(), bitmapImage1);

                            // Create a Query for storing image in Database
                            SimpleSQLiteQuery query = new SimpleSQLiteQuery("UPDATE " + getString(R.string.tableName) + " " +
                                    "SET TIR_image1 = '"+ imagePath  +
                                    "' WHERE customer_id = " + userLokalStore.getLoggedInUser().getUserId() + " AND id = " + userLokalStore.getCurrentRecordId());
                            // Store Image in Database
                            bookdataViewModel.update(query);

                            // Load Image to imageView
                            TIR_image1.setImageBitmap(bitmapImage1);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result1.getError();
                        Log.v(Log_tag, "Error Load Pic1 on TimeIsRunning Fragment: " + error);
                    }
                    break;

                default:
                    Toast.makeText(getActivity(), R.string.e_PicNotOk, Toast.LENGTH_SHORT).show();

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // notice Fragment is visible to User
        isVisibleToUser = true;
    }

    @Override
    public void onPause() {
        super.onPause();

        if(isVisibleToUser) {// only when you go out of fragment screen
            // Store Data to Database
            storeDataToDatabase();

            // reset Variable
            isVisibleToUser = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Store only Data if Fragment is visible
        if (isVisibleToUser)
        {
            // Store Data to Database on Phone
            storeDataToDatabase();

            // reset Variable
            isVisibleToUser = false;
        }
    }

    private void storeDataToDatabase ()
    {
        UserLokalStore userLokalStore = UserLokalStore.getInstance(getContext());

        SimpleSQLiteQuery query = new SimpleSQLiteQuery("UPDATE " + getString(R.string.tableName) + " " +
                "SET TIR_inputText1 = '"+ TIR_inputText1.getText().toString()+ "'" +
                ", TIR_inputText2 = '"+ TIR_inputText2.getText().toString()+ "'" +
                ", TIR_inputText3 = '"+ TIR_inputText3.getText().toString()+ "'" +
                ", TIR_inputText4 = '"+ TIR_inputText4.getText().toString() +
                "' WHERE customer_id = " + userLokalStore.getLoggedInUser().getUserId() + " AND id = " + userLokalStore.getCurrentRecordId());

        bookdataViewModel.update(query);
    }

    private void init(View view)
    {
        TIR_inputText1 = view.findViewById(R.id.et_tir_inputText_1);
        TIR_inputText2 = view.findViewById(R.id.et_tir_inputText_2);
        TIR_inputText3 = view.findViewById(R.id.et_tir_inputText_3);
        TIR_inputText4 = view.findViewById(R.id.et_tir_inputText_4);
        TIR_image1 = view.findViewById(R.id.iv_tir_pic_1);
    }

}
