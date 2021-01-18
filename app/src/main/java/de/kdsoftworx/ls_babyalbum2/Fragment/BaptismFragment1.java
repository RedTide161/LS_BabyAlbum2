package de.kdsoftworx.ls_babyalbum2.Fragment;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.List;

import de.kdsoftworx.ls_babyalbum2.Activity.ChildDataActivity;
import de.kdsoftworx.ls_babyalbum2.Data.UserLokalStore;
import de.kdsoftworx.ls_babyalbum2.R;
import de.kdsoftworx.ls_babyalbum2.RoomDatabase.LSBookdata;
import de.kdsoftworx.ls_babyalbum2.ViewModel.BookdataViewModel;


public class BaptismFragment1 extends Fragment {

    public static final String Log_tag = ChildDataActivity.class.getSimpleName();

    private BookdataViewModel bookdataViewModel;
    private boolean isVisibleToUser=false;

    private String date;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    EditText BTM_inputText1, BTM_inputText2, BTM_inputText3, BTM_inputText4, BTM_inputText5, BTM_inputText6, BTM_inputText7, BTM_inputText8, BTM_inputText9, BTM_inputText10;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_baptism_1, container, false);

        // initialise all editTexts
        init(root);

        // get newest Data from Database
        bookdataViewModel = ViewModelProviders.of(this).get(BookdataViewModel.class);
        bookdataViewModel.getAllBookdataEntries().observe(getViewLifecycleOwner(), new Observer<List<LSBookdata>>() {
            @Override
            public void onChanged(List<LSBookdata> lsBookdata) {

                if (lsBookdata.size() >0) {
                    LSBookdata currentData = lsBookdata.get(0);
                    BTM_inputText1.setText(currentData.BTM_inputText1);
                    BTM_inputText2.setText(currentData.BTM_inputText2);
                    BTM_inputText3.setText(currentData.BTM_inputText3);
                    BTM_inputText4.setText(currentData.BTM_inputText4);
                    BTM_inputText5.setText(currentData.BTM_inputText5);
                    BTM_inputText6.setText(currentData.BTM_inputText6);
                    BTM_inputText7.setText(currentData.BTM_inputText7);
                    BTM_inputText8.setText(currentData.BTM_inputText8);
                    BTM_inputText9.setText(currentData.BTM_inputText9);
                    BTM_inputText10.setText(currentData.BTM_inputText10);
                }
            }
        });

        // Set DatePicker on EditText-Field for Baptism-Date -----------------------------------------------------------------------------
        BTM_inputText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //create a Calender-Object
                Calendar cal = Calendar.getInstance();

                // get current date
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                // create DatePickerDialog Object and pass the current date
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog, mDateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        //create OnDateSetListener Object
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String formattedMonth = "" + month;
                String formattedDay = "" + day;

                boolean is24HourStyle = false;
                // Check for Time-Format (24 or 12 hours?)
                is24HourStyle = DateFormat.is24HourFormat(getContext());

                // put 0 to Month and Day of Month
                if (month < 10) {
                    formattedMonth = "0" + month;
                }
                if (day < 10) {
                    formattedDay = "0" + day;
                }

                // write Date in european or american Format
                if (is24HourStyle) {
                    // get selected date
                    date = formattedDay + "." + formattedMonth + "." + year;
                } else {
                    // get selected date
                    date = formattedMonth + "-" + formattedDay + "-" + year;
                }

                // build String with current date and time
                String selectedDate = date;
                // pass date and time-String to EditText-View for Contraction Start Time
                BTM_inputText1.setText(selectedDate);
            }
        };
        //------------------------------------------------------------------------------------------------------------------------------------

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
                "SET BTM_inputText1 = '"+ BTM_inputText1.getText().toString()+ "'" +
                ", BTM_inputText2 = '"+ BTM_inputText2.getText().toString()+ "'" +
                ", BTM_inputText3 = '"+ BTM_inputText3.getText().toString()+ "'" +
                ", BTM_inputText4 = '"+ BTM_inputText4.getText().toString()+ "'" +
                ", BTM_inputText5 = '"+ BTM_inputText5.getText().toString()+ "'" +
                ", BTM_inputText6 = '"+ BTM_inputText6.getText().toString()+ "'" +
                ", BTM_inputText7 = '"+ BTM_inputText7.getText().toString()+ "'" +
                ", BTM_inputText8 = '"+ BTM_inputText8.getText().toString()+ "'" +
                ", BTM_inputText9 = '"+ BTM_inputText9.getText().toString()+ "'" +
                ", BTM_inputText10 = '"+ BTM_inputText10.getText().toString() +
                "' WHERE customer_id = " + userLokalStore.getLoggedInUser().getUserId() + " AND id = " + userLokalStore.getCurrentRecordId());

        bookdataViewModel.update(query);
    }
    private void init(View view)
    {
        BTM_inputText1 = (EditText) view.findViewById(R.id.et_btm_inputText_1_0);
        BTM_inputText2 = (EditText) view.findViewById(R.id.et_btm_inputText_1_1);
        BTM_inputText3 = (EditText) view.findViewById(R.id.et_btm_inputText_1_2);
        BTM_inputText4 = (EditText) view.findViewById(R.id.et_btm_inputText_2);
        BTM_inputText5 = (EditText) view.findViewById(R.id.et_btm_inputText_3);
        BTM_inputText6 = (EditText) view.findViewById(R.id.et_btm_inputText_4);
        BTM_inputText7 = (EditText) view.findViewById(R.id.et_btm_inputText_5);
        BTM_inputText8 = (EditText) view.findViewById(R.id.et_btm_inputText_6);
        BTM_inputText9 = (EditText) view.findViewById(R.id.et_btm_inputText_7);
        BTM_inputText10 = (EditText) view.findViewById(R.id.et_btm_inputText_8);
    }

}
