package de.kdsoftworx.ls_babyalbum2.Fragment;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;


import java.util.Calendar;
import java.util.List;


import de.kdsoftworx.ls_babyalbum2.Data.UserLokalStore;
import de.kdsoftworx.ls_babyalbum2.R;
import de.kdsoftworx.ls_babyalbum2.RoomDatabase.LSBookdata;
import de.kdsoftworx.ls_babyalbum2.ViewModel.BookdataViewModel;


public class ChildBirthDatesFragment extends Fragment {

    // Declarations
    private BookdataViewModel bookdataViewModel;
    private boolean isVisibleToUser=false;

    private String date;
    private String time;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    EditText CBD_inputText1, CBD_inputText2, CBD_inputText3, CBD_inputText4, CBD_inputText5, CBD_inputText6, CBD_inputText7, CBD_inputText8;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.fragment_child_birth_dates_sdp, container, false);

        // initialise all editTexts
        init(root);

        // get newest Data from Database
        bookdataViewModel = ViewModelProviders.of(this).get(BookdataViewModel.class);
        bookdataViewModel.getAllBookdataEntries().observe(getViewLifecycleOwner(), new Observer<List<LSBookdata>>() {
            @Override
            public void onChanged(List<LSBookdata> lsBookdata) {

                if (lsBookdata.size() >0) {
                    LSBookdata currentData = lsBookdata.get(0);
                    CBD_inputText1.setText(currentData.CBD_inputText1);
                    CBD_inputText2.setText(currentData.CBD_inputText2);
                    CBD_inputText3.setText(currentData.CBD_inputText3);
                    CBD_inputText4.setText(currentData.CBD_inputText4);
                    CBD_inputText5.setText(currentData.CBD_inputText5);
                    CBD_inputText6.setText(currentData.CBD_inputText6);
                    CBD_inputText7.setText(currentData.CBD_inputText7);
                    CBD_inputText8.setText(currentData.CBD_inputText8);
                }
            }
        });

        // Set Date- and TimePicker on EditText-Field for Birthday -----------------------------------------------------------------------------
        CBD_inputText2.setOnClickListener(new View.OnClickListener() {
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


                // If user has set a Birth-Date start TimePicker for Birth-Time
                //create a Calender-Object for Time
                Calendar cal = Calendar.getInstance();

                // get current time
                int hour = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);


                // create TimePickerDialog Object and pass the current time
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog, mTimeSetListener, hour, minute, is24HourStyle);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        };

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {

                boolean is24HourStyle = false;
                String formattedHour = "" + hourOfDay;
                String formattedMinute = "" + minute;
                // Check for Time-Format (24 or 12 hours?)
                is24HourStyle = DateFormat.is24HourFormat(getContext());

                // check if Hour and Minute is < 10 to correct the displayed time
                if (hourOfDay < 10) {
                    formattedHour = "0" + hourOfDay;
                }
                if (minute < 10) {
                    formattedMinute = "0" + minute;
                }

                // Check which Time-Format is used an set the correct Format
                if (is24HourStyle) {
                    // get selscted time
                    time = formattedHour + ":" + formattedMinute + " " + getString(R.string.tp_timeUnit);
                } else {
                    if (hourOfDay == 0) { // check if it´s 24:00
                        time = (hourOfDay + 12) + ":" + formattedMinute + " am";
                    } else if (hourOfDay < 12) // Check if it´s between 00:00 midnight and 12:00 noon
                    {
                        time = formattedHour + ":" + formattedMinute + " am";
                    } else if (hourOfDay == 12) // Check if it´s 12:00 noon
                    {
                        time = formattedHour + ":" + formattedMinute + " pm";
                    } else if (hourOfDay < 22) // Check if it´s before 22:00
                    {
                        time = "0" + (hourOfDay - 12) + ":" + formattedMinute + " pm";
                    } else { // it must be between 22:00 noon and 24:00 midnight
                        time = (hourOfDay - 12) + ":" + formattedMinute + " pm";
                    }
                }


                // build String with current date and time
                String birthDateTime = date + ", " + getString(R.string.tp_atTime) + " " + time;
                // pass date and time-String to EditText-View
                CBD_inputText2.setText(birthDateTime);
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
                "SET CBD_inputText1 = '"+ CBD_inputText1.getText().toString()+ "'" +
                ", CBD_inputText2 = '"+ CBD_inputText2.getText().toString()+ "'" +
                ", CBD_inputText3 = '"+ CBD_inputText3.getText().toString()+ "'" +
                ", CBD_inputText4 = '"+ CBD_inputText4.getText().toString()+ "'" +
                ", CBD_inputText5 = '"+ CBD_inputText5.getText().toString()+ "'" +
                ", CBD_inputText6 = '"+ CBD_inputText6.getText().toString()+ "'" +
                ", CBD_inputText7 = '"+ CBD_inputText7.getText().toString()+ "'" +
                ", CBD_inputText8 = '"+ CBD_inputText8.getText().toString() +
                "' WHERE customer_id = " + userLokalStore.getLoggedInUser().getUserId() + " AND id = " + userLokalStore.getCurrentRecordId());

        bookdataViewModel.update(query);
    }

    private void init(View view)
    {
        CBD_inputText1 = view.findViewById(R.id.et_childname_cbd);
        CBD_inputText2 = view.findViewById(R.id.et_birthday_cbd);
        CBD_inputText3 = view.findViewById(R.id.et_birth_weight_cbd);
        CBD_inputText4 = view.findViewById(R.id.et_birth_size_cbd);
        CBD_inputText5 = view.findViewById(R.id.et_head_circumference_cbd);
        CBD_inputText6 = view.findViewById(R.id.et_eye_color_cbd);
        CBD_inputText7 = view.findViewById(R.id.et_hair_color_cbd);
        CBD_inputText8 = view.findViewById(R.id.et_star_sign_cbd);
    }

}
