package de.kdsoftworx.ls_babyalbum2.Fragment;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import de.kdsoftworx.ls_babyalbum2.Data.UserLokalStore;
import de.kdsoftworx.ls_babyalbum2.R;
import de.kdsoftworx.ls_babyalbum2.RoomDatabase.LSBookdata;
import de.kdsoftworx.ls_babyalbum2.ViewModel.BookdataViewModel;


public class HalfTitleFragment extends Fragment {

    private BookdataViewModel bookdataViewModel;

    private EditText HT_inputText1, HT_inputText2;

    private boolean isVisibleToUser=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_half_title, container, false);
        //root.getTag();

        //initialise all editTexts
        init(root);

        // get newest Data from Database
        bookdataViewModel = ViewModelProviders.of(this).get(BookdataViewModel.class);
        bookdataViewModel.getAllBookdataEntries().observe(getViewLifecycleOwner(), new Observer<List<LSBookdata>>() {
            @Override
            public void onChanged(List<LSBookdata> lsBookdata) {

                if (lsBookdata.size() >0) {
                    LSBookdata currentData = lsBookdata.get(0);
                    HT_inputText1.setText(currentData.HT_inputText1);
                    HT_inputText2.setText(currentData.HT_inputText2);
                }
            }
        });

        return root;
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
    public void onResume() {
        super.onResume();
        // notice Fragment is visible to User
        isVisibleToUser = true;

    }


    private void storeDataToDatabase ()
    {
        UserLokalStore userLokalStore = UserLokalStore.getInstance(getContext());

        SimpleSQLiteQuery query = new SimpleSQLiteQuery("UPDATE " + getString(R.string.tableName) + " " +
                "SET HT_inputText1 = '"+ HT_inputText1.getText().toString()+ "'" +
                ", HT_inputText2 = '"+ HT_inputText2.getText().toString() +
                "' WHERE customer_id = " + userLokalStore.getLoggedInUser().getUserId() + " AND id = " + userLokalStore.getCurrentRecordId());

       bookdataViewModel.update(query);
    }

    private void init(View view)
    {
        HT_inputText1 = view.findViewById(R.id.etChildName);
        HT_inputText2 = view.findViewById(R.id.etCreator);
    }

}
