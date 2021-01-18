package de.kdsoftworx.ls_babyalbum2.Fragment;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import de.kdsoftworx.ls_babyalbum2.Activity.ChildDataActivity;
import de.kdsoftworx.ls_babyalbum2.Data.UserLokalStore;
import de.kdsoftworx.ls_babyalbum2.R;
import de.kdsoftworx.ls_babyalbum2.RoomDatabase.LSBookdata;
import de.kdsoftworx.ls_babyalbum2.ViewModel.BookdataViewModel;


public class ChildFirstSnow1 extends Fragment {

    public static final String Log_tag = ChildDataActivity.class.getSimpleName();

    private BookdataViewModel bookdataViewModel;
    private boolean isVisibleToUser=false;

    EditText FS_inputText1, FS_inputText2, FS_inputText3, FS_inputText4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_first_snow_1, container, false);

        // initialise all editTexts
        init(root);

        // get newest Data from Database
        bookdataViewModel = ViewModelProviders.of(this).get(BookdataViewModel.class);
        bookdataViewModel.getAllBookdataEntries().observe(this, new Observer<List<LSBookdata>>() {
            @Override
            public void onChanged(List<LSBookdata> lsBookdata) {

                if (lsBookdata.size() >0) {
                    LSBookdata currentData = lsBookdata.get(0);
                    FS_inputText1.setText(currentData.FS_inputText1);
                    FS_inputText2.setText(currentData.FS_inputText2);
                    FS_inputText3.setText(currentData.FS_inputText3);
                    FS_inputText4.setText(currentData.FS_inputText4);
                }
            }
        });

        return root;
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
                "SET FS_inputText1 = '"+ FS_inputText1.getText().toString()+ "'" +
                ", FS_inputText2 = '"+ FS_inputText2.getText().toString()+ "'" +
                ", FS_inputText3 = '"+ FS_inputText3.getText().toString()+ "'" +
                ", FS_inputText4 = '"+ FS_inputText4.getText().toString() +
                "' WHERE customer_id = " + userLokalStore.getLoggedInUser().getUserId() + " AND id = " + userLokalStore.getCurrentRecordId());

        bookdataViewModel.update(query);
    }

    private void init(View view)
    {
        FS_inputText1 = view.findViewById(R.id.et_fs_textinput_1);
        FS_inputText2 = view.findViewById(R.id.et_fs_textinput_2);
        FS_inputText3 = view.findViewById(R.id.et_fs_textinput_3);
        FS_inputText4 = view.findViewById(R.id.et_fs_textinput_4);
    }

}
