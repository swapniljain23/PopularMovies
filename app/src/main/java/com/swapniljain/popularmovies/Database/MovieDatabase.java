package com.swapniljain.popularmovies.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.swapniljain.popularmovies.Model.Movie;

@Database(entities = {Movie.class} , version = 1 , exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favorite_movies";
    private static MovieDatabase sharedInstance;

    public static MovieDatabase getSharedInstance(Context context){
        if (sharedInstance == null){
            synchronized (LOCK){
                sharedInstance = Room.databaseBuilder(context.getApplicationContext(),
                                                      MovieDatabase.class,
                                                      MovieDatabase.DATABASE_NAME)
                                    .build();
            }
        }
        return sharedInstance;
    }
    public abstract MovieDAO movieDAO();

}
