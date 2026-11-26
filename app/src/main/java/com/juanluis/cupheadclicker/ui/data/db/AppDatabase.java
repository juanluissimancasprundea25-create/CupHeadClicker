package com.juanluis.cupheadclicker.ui.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Local SQLite database of the app, managed by Room.
 * <p>
 * It is a Singleton: only one instance exists in the whole app. It is built
 * with the application context (never an Activity) to avoid memory leaks.
 * </p>
 *
 * @author Juan Luis Simancas Prundea
 * @version 1.0
 */
@Database(entities = {PlayerEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "cuphead_clicker.db";

    /** volatile: every thread sees the latest value of the instance. */
    private static volatile AppDatabase instance;

    /** @return the DAO of the players table */
    public abstract PlayerDao playerDao();

    /**
     * Returns the only instance of the database (double-checked locking).
     *
     * @param context any context; only its application context is kept
     * @return the database
     */
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }
}
