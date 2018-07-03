package com.swapniljain.popularmovies.Database;

import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DatabaseExecutors {

    private static final Object LOCK = new Object();
    private static DatabaseExecutors sharedInstance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;

    private DatabaseExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public static DatabaseExecutors getSharedInstance() {
        if (sharedInstance == null) {
            synchronized (LOCK) {
                sharedInstance = new DatabaseExecutors(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return sharedInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor networkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {
        private android.os.Handler mainThreadHandler = new android.os.Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}

