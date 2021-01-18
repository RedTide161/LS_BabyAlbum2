package de.kdsoftworx.ls_babyalbum2.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import de.kdsoftworx.ls_babyalbum2.Repository.BookdataRepository;
import de.kdsoftworx.ls_babyalbum2.RoomDatabase.LSBookdata;

public class BookdataViewModel extends AndroidViewModel {

    private BookdataRepository repository;
    private LiveData<List<LSBookdata>> allEntries, allBookdataEntries;

    public BookdataViewModel(@NonNull Application application) {
        super(application);

        repository = new BookdataRepository(application);
        allEntries = repository.getAllEntries();
        allBookdataEntries = repository.getAllBookdataEntries();
    }

    public void checkForDataAvailable(Integer customer_id, String child_name) {
        repository = new BookdataRepository(getApplication());
        repository.checkForDataAvailable(customer_id, child_name);
    }

    public void getData(SupportSQLiteQuery query) {
        repository.getData(query);
    }

    public void insert(LSBookdata lsBookdata) {
        repository.insert(lsBookdata);
    }

    public void update(SupportSQLiteQuery query) {
        repository.update(query);
    }

    public void updateChildName(String child_name, Integer customer_id, Integer child_id) {repository.updateChildName(child_name, customer_id, child_id);}

    public void deleteAllEntries(Integer customer_id) {
        repository.deleteAllEntries(customer_id);
    }

    public void delete(LSBookdata lsBookdata){repository.delete(lsBookdata);}

    public LiveData<List<LSBookdata>> getAllEntries() {
        return allEntries;
    }

    public LiveData<List<LSBookdata>> getAllBookdataEntries() {
        return allBookdataEntries;
    }

}
