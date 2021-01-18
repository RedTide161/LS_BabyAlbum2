package de.kdsoftworx.ls_babyalbum2.Interface;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import de.kdsoftworx.ls_babyalbum2.RoomDatabase.LSBookdata;

@Dao
public interface BookdataDAO {

    @Query("SELECT id, customer_id FROM LSBookdata WHERE customer_id = :customer_id AND child_name = :child_name")
    List <LSBookdata> checkForDataAvailable(int customer_id, String child_name);

    @Query("SELECT * FROM LSBookdata WHERE customer_id = :customer_id")
    LiveData<List<LSBookdata>> getAllEntries(int customer_id);

    @Query("SELECT * FROM LSBookdata WHERE customer_id = :customer_id AND id = :child_id")
    LiveData<List<LSBookdata>> getAllBookdataEntries(int customer_id, int child_id);

    @Query("UPDATE LSBookdata SET child_name = :child_name WHERE customer_id = :customer_id AND id = :child_id")
    void updateChildName(String child_name, int customer_id, int child_id);

    @RawQuery
    List<LSBookdata> getData (SupportSQLiteQuery query);

    @Insert()
    void createDataRecord (LSBookdata bookData);

    @RawQuery ()
    List<LSBookdata> updateData (SupportSQLiteQuery query);

    @Query("DELETE FROM LSBookdata WHERE customer_id = :customer_id")
    void deleteAllEntries(int customer_id);

    @Delete
    void delete(LSBookdata lsBookdata);

}
