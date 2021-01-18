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


public class PregnancyFragment1 extends Fragment {

    public static final String Log_tag = ChildDataActivity.class.getSimpleName();

    private BookdataViewModel bookdataViewModel;
    private boolean isVisibleToUser=false;

    EditText P_inputText1, P_inputText2, P_inputText3, P_inputText4, P_inputText5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_pregnancy_1, container, false);

        // initialise all editTexts
        init(root);

        // get newest Data from Database
        bookdataViewModel = ViewModelProviders.of(this).get(BookdataViewModel.class);
        bookdataViewModel.getAllBookdataEntries().observe(getViewLifecycleOwner(), new Observer<List<LSBookdata>>() {
            @Override
            public void onChanged(List<LSBookdata> lsBookdata) {

                if (lsBookdata.size() >0) {
                    LSBookdata currentData = lsBookdata.get(0);
                    P_inputText1.setText(currentData.P_inputText1);
                    P_inputText2.setText(currentData.P_inputText2);
                    P_inputText3.setText(currentData.P_inputText3);
                    P_inputText4.setText(currentData.P_inputText4);
                    P_inputText5.setText(currentData.P_inputText5);
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
                "SET P_inputText1 = '"+ P_inputText1.getText().toString()+ "'" +
                ", P_inputText2 = '"+ P_inputText2.getText().toString()+ "'" +
                ", P_inputText3 = '"+ P_inputText3.getText().toString()+ "'" +
                ", P_inputText4 = '"+ P_inputText4.getText().toString()+ "'" +
                ", P_inputText5 = '"+ P_inputText5.getText().toString() +
                "' WHERE customer_id = " + userLokalStore.getLoggedInUser().getUserId() + " AND id = " + userLokalStore.getCurrentRecordId());

        bookdataViewModel.update(query);
    }

    private void init(View view)
    {
        P_inputText1 = view.findViewById(R.id.et_p_preg_knowing);
        P_inputText2 = view.findViewById(R.id.et_p_preg_feeling);
        P_inputText3 = view.findViewById(R.id.et_p_preg_well_being);
        P_inputText4 = view.findViewById(R.id.et_p_preg_sex);
        P_inputText5 = view.findViewById(R.id.et_p_preg_movements);
    }


}
