package com.example.madcompetition.multithreaded;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppExecutors {
    private static final AppExecutors INSTANCE = new AppExecutors();
    private ExecutorService mExecutor;
    private AppExecutors(){
        this.mExecutor = Executors.newCachedThreadPool();
    }

    public ExecutorService getExecutor(){return this.mExecutor;}
    public static AppExecutors getInstance(){return INSTANCE;}
}
