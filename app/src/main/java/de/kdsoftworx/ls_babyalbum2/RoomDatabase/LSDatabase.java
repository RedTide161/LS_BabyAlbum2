package de.kdsoftworx.ls_babyalbum2.RoomDatabase;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import de.kdsoftworx.ls_babyalbum2.Interface.BookdataDAO;

@Database(entities = LSBookdata.class, exportSchema = false, version = 1)
public abstract class LSDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "LSDatabase.db";
    public static LSDatabase instance;

    public static synchronized LSDatabase getInstance(Context context)
    {
        //if there is no instance available... create new one
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), LSDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        // return available instance
        return instance;
    }

    public abstract BookdataDAO bookdataDAO();
}
