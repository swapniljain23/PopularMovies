package com.swapniljain.popularmovies.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.swapniljain.popularmovies.Model.Movie;

@Database(entities = {Movie.class} , version = 1 , exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final String TAG = Database.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "FavoriteMovies";
    private static MovieDatabase sInstance;

    public static MovieDatabase getsInstance(Context context){
        // first time if the sInstance is null then create a sInstance
        if (sInstance == null){
            synchronized (LOCK){
                Log.d(TAG, "First time creating sInstance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),MovieDatabase.class, MovieDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(TAG, "Getting the sInstance");
        return sInstance;
    }
    public abstract MovieDAO movieDAO();

}
