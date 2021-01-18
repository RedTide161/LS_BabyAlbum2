package de.kdsoftworx.ls_babyalbum2.Repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import de.kdsoftworx.ls_babyalbum2.Data.UserLokalStore;
import de.kdsoftworx.ls_babyalbum2.Interface.BookdataDAO;
import de.kdsoftworx.ls_babyalbum2.R;
import de.kdsoftworx.ls_babyalbum2.RoomDatabase.LSBookdata;
import de.kdsoftworx.ls_babyalbum2.RoomDatabase.LSDatabase;

public class BookdataRepository {

    private BookdataDAO bookdataDAO;
    private UserLokalStore userLokalStore;
    private LiveData<List<LSBookdata>> allEntries, allBookdataEntries;
    private static Context mContext;

    public BookdataRepository(Application application) {

        //get Context for DialogBuilder
        mContext = application;
        // get Instance of Database
        LSDatabase lsDatabase = LSDatabase.getInstance(application);

        // get Instance of UserLokalStore
        userLokalStore = UserLokalStore.getInstance(application);
        int id = userLokalStore.getCurrentRecordId();

        bookdataDAO = lsDatabase.bookdataDAO();
        allEntries = bookdataDAO.getAllEntries(UserLokalStore.getInstance(application).getLoggedInUser().getUserId());
        allBookdataEntries = bookdataDAO.getAllBookdataEntries(UserLokalStore.getInstance(application).getLoggedInUser().getUserId(), userLokalStore.getCurrentRecordId());
    }

    public void checkForDataAvailable(Integer customer_id, String child_name) {
        LSBookdata lsBookdata = new LSBookdata();
        lsBookdata.customer_id = customer_id;
        lsBookdata.child_name = child_name;

      new checkForDataAvailableAsync(bookdataDAO).execute(lsBookdata);
    }

    public LiveData<List<LSBookdata>> getAllEntries() {

        return allEntries;
    }

    public LiveData<List<LSBookdata>> getAllBookdataEntries() {

        return allBookdataEntries;
    }

    public void getData(SupportSQLiteQuery query) {
        new getDataAsync(bookdataDAO).execute(query);

    }

    public void insert(LSBookdata lsBookdata) {
        new insertAsync(bookdataDAO).execute(lsBookdata);
    }

    public void update(SupportSQLiteQuery query) {
        new updateAsync(bookdataDAO).execute(query);
    }

    public void updateChildName(String child_name, Integer customer_id, Integer child_id) {
        LSBookdata lsBookdata = new LSBookdata();
        lsBookdata.child_name = child_name;
        lsBookdata.customer_id = customer_id;
        lsBookdata.id= child_id;

        new updateChildNameAsync(bookdataDAO).execute(lsBookdata);
    }

    public void deleteAllEntries(Integer customer_id){
        new deleteAllEntriesAsync(bookdataDAO).execute(customer_id);
    }

    public void delete(LSBookdata lsBookdata){
        new deleteAsync(bookdataDAO).execute(lsBookdata);
    }

    private static class checkForDataAvailableAsync extends AsyncTask<LSBookdata, Void, Boolean> {
        private BookdataDAO bookdataDAO;

        private checkForDataAvailableAsync(BookdataDAO bookdataDAO) {
                this.bookdataDAO = bookdataDAO;

        }

        @Override
        protected Boolean doInBackground(LSBookdata... lsBookdata) {

            List<LSBookdata> Data =  bookdataDAO.checkForDataAvailable(lsBookdata[0].customer_id, lsBookdata[0].child_name);
            Boolean result = false;

            if (Data.isEmpty())
            {
                bookdataDAO.createDataRecord(lsBookdata[0]);
            }
            else{
                // If Account already exist get back result for Message
                result = true;
            }


            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            // If Account already exist show message
            if (result){
                Toast.makeText(mContext, R.string.e_ChildAccountFound, Toast.LENGTH_LONG).show();
            }

        }

    }


    private static class getDataAsync extends AsyncTask<SupportSQLiteQuery, Void, List<LSBookdata>> {
        private BookdataDAO bookdataDAO;

        private getDataAsync(BookdataDAO bookdataDAO) {
            this.bookdataDAO = bookdataDAO;
        }

        @Override
        protected List<LSBookdata> doInBackground(SupportSQLiteQuery... query) {

            return bookdataDAO.getData(query[0]);

        }
    }


    private static class insertAsync extends AsyncTask<LSBookdata, Void, Void> {
        private BookdataDAO bookdataDAO;

        private insertAsync(BookdataDAO bookdataDAO) {
            this.bookdataDAO = bookdataDAO;
        }

        @Override
        protected Void doInBackground(LSBookdata... lsBookdata) {

            bookdataDAO.createDataRecord(lsBookdata[0]);
            return null;
        }
    }


    private static class updateAsync extends AsyncTask<SupportSQLiteQuery, Void, Void> {
        private BookdataDAO bookdataDAO;

        private updateAsync(BookdataDAO bookdataDAO) {
           this.bookdataDAO = bookdataDAO;
        }

        @Override
        protected Void doInBackground(SupportSQLiteQuery... queries) {

            bookdataDAO.updateData(queries[0]);
            return null;
        }
    }

    private static class updateChildNameAsync extends AsyncTask<LSBookdata, Void, Void>{
        private BookdataDAO bookdataDAO;

        private updateChildNameAsync(BookdataDAO bookdataDAO) {
            this.bookdataDAO = bookdataDAO;
        }

        @Override
        protected Void doInBackground(LSBookdata... lsBookdata) {

            bookdataDAO.updateChildName(lsBookdata[0].child_name, lsBookdata[0].customer_id, lsBookdata[0].id);

            return null;
        }
    }

    private static class deleteAllEntriesAsync extends AsyncTask<Integer, Void, Void> {
        private BookdataDAO bookdataDAO;

        private deleteAllEntriesAsync(BookdataDAO bookdataDAO) {
            this.bookdataDAO = bookdataDAO;
        }

        @Override
        protected Void doInBackground(Integer... customer_id) {

            bookdataDAO.deleteAllEntries(customer_id[0]);
            return null;
        }
    }

    private static class deleteAsync extends AsyncTask<LSBookdata, Void, Void>{
        private BookdataDAO bookdataDAO;

        private deleteAsync (BookdataDAO bookdataDAO) {this.bookdataDAO = bookdataDAO;}

        @Override
        protected Void doInBackground(LSBookdata... lsBookdata) {

            bookdataDAO.delete(lsBookdata[0]);
            return null;
        }
    }
}
